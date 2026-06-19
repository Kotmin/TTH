package com.kotmin.soldevelo.alertrules.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kotmin.soldevelo.alertrules.engine.strategy.DivisibilityAlertRule;
import com.kotmin.soldevelo.alertrules.engine.strategy.StrategyAlertEngine;
import java.util.List;
import org.junit.jupiter.api.Test;

class AlertEngineProcessorAdapterTest {

    private final AlertEngineProcessorAdapter adapter = new AlertEngineProcessorAdapter(new StrategyAlertEngine(
            List.of(new DivisibilityAlertRule(3, "LOW"), new DivisibilityAlertRule(5, "ADVISORY"))));

    @Test
    void adapterProcessesListInInputOrder() {
        List<Integer> input = List.of(1, 3, 5, 15, 7);
        List<String> expected = List.of("1", "LOW", "ADVISORY", "LOWADVISORY", "7");
        assertEquals(expected, adapter.process(input));
    }

    @Test
    void emptyInputProducesEmptyOutput() {
        assertEquals(List.of(), adapter.process(List.of()));
    }

    @Test
    void adapterHandlesTrickyValuesInOrder() {
        var extended = new AlertEngineProcessorAdapter(new StrategyAlertEngine(List.of(
                new DivisibilityAlertRule(3, "LOW"),
                new DivisibilityAlertRule(5, "ADVISORY"),
                new DivisibilityAlertRule(7, "WARN"))));
        List<Integer> input = List.of(-21, 63, 210, Integer.MAX_VALUE);
        List<String> expected = List.of("LOWWARN", "LOWWARN", "LOWADVISORYWARN", String.valueOf(Integer.MAX_VALUE));
        assertEquals(expected, extended.process(input));
    }
}
