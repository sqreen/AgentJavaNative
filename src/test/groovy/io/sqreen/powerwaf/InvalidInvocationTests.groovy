package io.sqreen.powerwaf

import groovy.transform.CompileStatic
import io.sqreen.powerwaf.exception.InvalidObjectPowerwafException
import io.sqreen.powerwaf.exception.UnclassifiedPowerwafException
import org.junit.Assume
import org.junit.Test

import java.nio.ByteBuffer

import static groovy.test.GroovyAssert.shouldFail
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.containsString

class InvalidInvocationTests implements ReactiveTrait {
    @CompileStatic
    static class BadMap<K, V> implements Map<K, V> {
        @Delegate
        Map<K, V> delegate

        @Override
        Set<Map.Entry<K, V>> entrySet() {
            throw new IllegalStateException('error here')
        }
    }

    @Test
    void 'force exception during conversion of rule definitions'() {
        def exc = shouldFail(RuntimeException) {
            ctx = Powerwaf.createContext('test', new BadMap(delegate: [version: '1.0', events: []]))
        }
        assert exc.message =~ 'Exception encoding rule definitions'
        assert exc.cause instanceof IllegalStateException
        assert exc.cause.message == 'error here'
    }

    @Test
    void 'runRule with conversion throwing exception'() {
        ctx = Powerwaf.createContext('test', ARACHNI_ATOM)
        def exc = shouldFail(UnclassifiedPowerwafException) {
            ctx.runRules(new BadMap(delegate: [:]), limits)
        }
        assert exc.cause.message =~ 'Exception encoding parameters'
        assert exc.cause.cause instanceof IllegalStateException
        assert exc.cause.cause.message == 'error here'
    }

    @Test
    void 'runRule with conversion throwing exception additive variant'() {
        ctx = Powerwaf.createContext('test', ARACHNI_ATOM)
        additive = ctx.openAdditive()
        def exc = shouldFail(UnclassifiedPowerwafException) {
            additive.run(new BadMap(delegate: [:]), limits)
        }
        assert exc.cause.message =~ 'Exception encoding parameters'
        assert exc.cause.cause instanceof IllegalStateException
        assert exc.cause.cause.message == 'error here'
    }

    @Test
    void 'rule is run on closed context'() {
        ctx = Powerwaf.createContext('test', ARACHNI_ATOM)
        ctx.delReference()
        def exc = shouldFail(UnclassifiedPowerwafException) {
            ctx.runRules([:], limits)
        }
        assertThat exc.message, containsString('This context is already offline')
        ctx = null
    }

    @Test
    void 'bytebuffer passed does not represent a map'() {
        Assume.assumeTrue Powerwaf.ENABLE_BYTE_BUFFERS

        ctx = Powerwaf.createContext('test', ARACHNI_ATOM)
        additive = ctx.openAdditive()

        ByteBufferSerializer serializer = new ByteBufferSerializer(limits)
        serializer.serialize([a: 'b']).withCloseable { lease ->
            ByteBuffer buffer = lease.firstPWArgsByteBuffer
            def slice = buffer.slice()
            slice.position(ByteBufferSerializer.SIZEOF_PWARGS)
            shouldFail(InvalidObjectPowerwafException) {
                additive.runAdditive(slice, limits)
            }
        }
    }

    @Test
    void 'bytebuffer passed is not direct buffer'() {
        Assume.assumeTrue Powerwaf.ENABLE_BYTE_BUFFERS

        ctx = Powerwaf.createContext('test', ARACHNI_ATOM)
        additive = ctx.openAdditive()

        shouldFail(IllegalArgumentException) {
            additive.runAdditive(ByteBuffer.allocate(ByteBufferSerializer.SIZEOF_PWARGS), limits)
        }
    }
}
