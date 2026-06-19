package com.kotmin.soldevelo.alertrules.engine.simple;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SimpleAlertEngineCombinationTest {

    private final SimpleAlertEngine engine = new SimpleAlertEngine();

    @Test
    void combinedLowAdvisoryEmergesFromConcatenation() {
        assertEquals("LOWADVISORY", engine.evaluate(15));
    }
}
