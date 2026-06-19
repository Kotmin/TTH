package com.kotmin.soldevelo.alertrules.engine.simple;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleAlertEngineCombinationTest {

    private final SimpleAlertEngine engine = new SimpleAlertEngine();

    @Test
    void combinedLowAdvisoryEmergesFromConcatenation() {
        assertEquals("LOWADVISORY", engine.evaluate(15));
    }
}
