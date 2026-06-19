package com.kotmin.soldevelo.alertrules.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kotmin.soldevelo.alertrules.engine.StrategyAlertEngine;
import com.kotmin.soldevelo.alertrules.engine.condition.AlertCondition;
import com.kotmin.soldevelo.alertrules.engine.effect.AlertEffect;
import com.kotmin.soldevelo.alertrules.engine.rule.ConditionalAlertRule;
import java.util.List;
import org.junit.jupiter.api.Test;

class AlertEngineProcessorAdapterTest {

    private final AlertEngineProcessorAdapter adapter = new AlertEngineProcessorAdapter(new StrategyAlertEngine(List.of(
            new ConditionalAlertRule(AlertCondition.divisibleBy(3), AlertEffect.append("LOW")),
            new ConditionalAlertRule(AlertCondition.divisibleBy(5), AlertEffect.append("ADVISORY")))));

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
                new ConditionalAlertRule(AlertCondition.divisibleBy(3), AlertEffect.append("LOW")),
                new ConditionalAlertRule(AlertCondition.divisibleBy(5), AlertEffect.append("ADVISORY")),
                new ConditionalAlertRule(AlertCondition.divisibleBy(7), AlertEffect.append("WARN")))));
        List<Integer> input = List.of(-21, 63, 210, Integer.MAX_VALUE);
        List<String> expected = List.of("LOWWARN", "LOWWARN", "LOWADVISORYWARN", String.valueOf(Integer.MAX_VALUE));
        assertEquals(expected, extended.process(input));
    }
}
