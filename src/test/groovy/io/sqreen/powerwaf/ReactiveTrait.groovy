/*
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache-2.0 License.
 *
 * This product includes software developed at Datadog
 * (https://www.datadoghq.com/). Copyright 2021 Datadog, Inc.
 */

package io.sqreen.powerwaf

import org.junit.After

trait ReactiveTrait extends PowerwafTrait {

    Additive additive

    @After
    void clearAdditive() {
        additive?.close()
    }
}
