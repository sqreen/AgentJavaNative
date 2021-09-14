/*
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache-2.0 License.
 *
 * This product includes software developed at Datadog
 * (https://www.datadoghq.com/). Copyright 2021 Datadog, Inc.
 */

package io.sqreen.powerwaf.exception;

public class TimeoutPowerwafException extends AbstractPowerwafException {
    public TimeoutPowerwafException() {
        super("Timeout", -1);
    }
}
