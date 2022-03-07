/*
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache-2.0 License.
 *
 * This product includes software developed at Datadog
 * (https://www.datadoghq.com/). Copyright 2021 Datadog, Inc.
 */

package io.sqreen.powerwaf

import groovy.json.JsonSlurper
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static groovy.test.GroovyAssert.shouldFail
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.is

class AdditiveTest implements ReactiveTrait {

    private final static Logger LOGGER = LoggerFactory.getLogger(AdditiveTest)

    @Test
    void 'Reference sample should pass'() {
        def rule = '''
          {
            "version": "1.0",
            "events": [
              {
                "id": "arachni_rule",
                "name": "Arachni",
                "conditions": [
                  {
                    "operation": "match_regex",
                    "parameters": {
                      "inputs": ["arg1"],
                      "regex": ".*"
                    }
                  },
                  {
                    "operation": "match_regex",
                    "parameters": {
                      "inputs": ["arg2"],
                      "regex": ".*"
                    }
                  }
                ],
                "tags": {
                  "type": "flow1"
                },
                "action": "record"
              }
            ]
          }
        '''

        ctx = new PowerwafContext('test', new JsonSlurper().parseText(rule))
        additive = ctx.openAdditive()
        metrics = ctx.createMetricsCollector()

        Powerwaf.ActionWithData awd = additive.run([arg1: 'string 1'], limits, metrics)
        LOGGER.debug('ActionWithData after 1st runAdditive: {}', awd)
        assertThat awd.action, is(Powerwaf.Action.OK)

        awd = additive.run([arg2: 'string 2'], limits, metrics)
        LOGGER.debug('ActionWithData after 2nd runAdditive: {}', awd)
        assertThat awd.action, is(Powerwaf.Action.MONITOR)

        def iter = metrics.iterator()
        assert iter.hasNext()

        PowerwafMetrics.RuleExecDuration red = iter.next()
        assert red.rule as String == 'arachni_rule'
        assert red.timeInNs > 0

        assert !iter.hasNext()
    }

    @Test
    void 'constructor throws if given a null context'() {
        shouldFail(NullPointerException) {
            new Additive(null)
        }
    }

    @Test(expected = RuntimeException)
    void 'Should throw RuntimeException if double free'() {
        ctx = new PowerwafContext('test', ARACHNI_ATOM)
        additive = ctx.openAdditive()
        additive.close()
        try {
            additive.close()
        } finally {
            additive = null
        }
    }

    @Test(expected = IllegalArgumentException)
    void 'Should throw IllegalArgumentException if Limits is null while run'() {
        ctx = new PowerwafContext('test', ARACHNI_ATOM_V2_1)
        additive = ctx.openAdditive()
        additive.run([:], null, metrics)
    }

    @Test
    void 'should throw iae if PowerwafMetrics is foreign'() {
        ctx = new PowerwafContext('test', ARACHNI_ATOM_V2_1)
        PowerwafContext ctx2 = new PowerwafContext('test2', ARACHNI_ATOM_V2_1)
        metrics = ctx2.createMetricsCollector()
        ctx2.delReference()

        additive = ctx.openAdditive()
        def exc = shouldFail(IllegalArgumentException) {
            additive.run([:], null, metrics)
        }
        assert exc.message.contains('metrics collector with foreign handle')
    }

    @Test
    void 'should defer context destruction if the context is closed'() {
        ctx = new PowerwafContext('test', ARACHNI_ATOM_V2_1)
        additive = ctx.openAdditive()
        assert ctx.refcount.get() == 2
        ctx.delReference()
        additive.run([:], limits, metrics)
        assert ctx.refcount.get() == 1
        additive.close()
        assert ctx.refcount.get() == 0

        /* prevent @After hooks from trying to close them */
        ctx = null
        additive = null
    }
}
