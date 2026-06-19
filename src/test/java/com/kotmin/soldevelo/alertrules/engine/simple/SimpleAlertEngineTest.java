package com.kotmin.soldevelo.alertrules.engine.simple;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleAlertEngineTest {

    private final SimpleAlertEngine engine = new SimpleAlertEngine();

    @Test
    void baseRuleSetProducesExpectedOutputForOneToTwenty() {
        List<String> expected = List.of(
                "1", "2", "LOW", "4", "ADVISORY",
                "LOW", "7", "8", "LOW", "ADVISORY",
                "11", "LOW", "13", "14", "LOWADVISORY",
                "16", "17", "LOW", "19", "ADVISORY"
        );
        List<String> actual = IntStream.rangeClosed(1, 20)
                .mapToObj(engine::evaluate)
                .collect(Collectors.toList());
        assertEquals(expected, actual);
    }
}
