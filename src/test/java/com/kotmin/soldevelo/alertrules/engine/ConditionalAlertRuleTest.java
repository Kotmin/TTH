package com.kotmin.soldevelo.alertrules.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ConditionalAlertRuleTest {

    @Test
    void matchesDelegatesToCondition() {
        AlertRule rule = new ConditionalAlertRule(AlertCondition.divisibleBy(3), AlertEffect.noOp());
        assertTrue(rule.matches(9));
        assertFalse(rule.matches(7));
    }

    @Test
    void applyDelegatesToEffect() {
        AlertResult result = new AlertResult();
        AlertRule rule = new ConditionalAlertRule(AlertCondition.always(), AlertEffect.append("LOW"));
        rule.apply(result);
        assertEquals("LOW", result.toString());
    }

    @Test
    void nullConditionThrows() {
        assertThrows(NullPointerException.class, () -> new ConditionalAlertRule(null, AlertEffect.noOp()));
    }

    @Test
    void nullEffectThrows() {
        assertThrows(NullPointerException.class, () -> new ConditionalAlertRule(AlertCondition.always(), null));
    }
}
