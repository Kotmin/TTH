package com.kotmin.soldevelo.alertrules.engine.strategy;

import static org.junit.jupiter.api.Assertions.*;

import com.kotmin.soldevelo.alertrules.engine.simple.SimpleAlertEngine;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class StrategyAlertEngineTest {

    private static final List<AlertRule> BASE_RULES =
            List.of(new DivisibilityAlertRule(3, "LOW"), new DivisibilityAlertRule(5, "ADVISORY"));

    private static final List<AlertRule> EXTENDED_RULES = List.of(
            new DivisibilityAlertRule(3, "LOW"),
            new DivisibilityAlertRule(5, "ADVISORY"),
            new DivisibilityAlertRule(7, "WARN"));

    @Test
    void simpleAndStrategyEnginesProduceSameBaseOutput() {
        var simple = new SimpleAlertEngine();
        var strategy = new StrategyAlertEngine(BASE_RULES);
        List<String> simpleOut =
                IntStream.rangeClosed(1, 20).mapToObj(simple::evaluate).collect(Collectors.toList());
        List<String> strategyOut =
                IntStream.rangeClosed(1, 20).mapToObj(strategy::evaluate).collect(Collectors.toList());
        assertEquals(simpleOut, strategyOut);
    }

    @Test
    void extendedRuleSetCombinesWarnRule() {
        var engine = new StrategyAlertEngine(EXTENDED_RULES);
        assertEquals("LOWWARN", engine.evaluate(21));
        assertEquals("ADVISORYWARN", engine.evaluate(35));
        assertEquals("LOWADVISORYWARN", engine.evaluate(105));
    }

    @Test
    void nonMatchingValueReturnsOriginalNumber() {
        var engine = new StrategyAlertEngine(BASE_RULES);
        assertEquals("1", engine.evaluate(1));
        assertEquals("7", engine.evaluate(7));
    }

    @Test
    void zeroMatchesAllDivisibilityRules() {
        var base = new StrategyAlertEngine(BASE_RULES);
        assertEquals("LOWADVISORY", base.evaluate(0));

        var extended = new StrategyAlertEngine(EXTENDED_RULES);
        assertEquals("LOWADVISORYWARN", extended.evaluate(0));
    }

    @Test
    void negativeValueCanMatchRule() {
        var engine = new StrategyAlertEngine(BASE_RULES);
        assertEquals("LOW", engine.evaluate(-3));
        assertEquals("ADVISORY", engine.evaluate(-5));
    }

    @Test
    void emptyRuleListReturnsOriginalNumber() {
        var engine = new StrategyAlertEngine(List.of());
        assertEquals("42", engine.evaluate(42));
    }

    @Test
    void ruleOrderIsPreserved() {
        var engine = new StrategyAlertEngine(
                List.of(new DivisibilityAlertRule(7, "WARN"), new DivisibilityAlertRule(3, "LOW")));
        assertEquals("WARNLOW", engine.evaluate(21));
    }

    @Test
    void divisibilityRuleRejectsZeroDivisor() {
        assertThrows(IllegalArgumentException.class, () -> new DivisibilityAlertRule(0, "INVALID"));
    }
}
