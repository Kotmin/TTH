package com.kotmin.soldevelo.alertrules.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AlertConditionTest {

    @Test
    void divisibleByMatchesDivisibleValue() {
        assertTrue(AlertCondition.divisibleBy(3).matches(9));
    }

    @Test
    void divisibleByDoesNotMatchNonDivisibleValue() {
        assertFalse(AlertCondition.divisibleBy(3).matches(7));
    }

    @Test
    void divisibleByMatchesZeroValue() {
        assertTrue(AlertCondition.divisibleBy(3).matches(0));
    }

    @Test
    void divisibleByMatchesNegativeDivisibleValue() {
        assertTrue(AlertCondition.divisibleBy(3).matches(-3));
    }

    @Test
    void divisibleByRejectsZeroDivisor() {
        assertThrows(IllegalArgumentException.class, () -> AlertCondition.divisibleBy(0));
    }

    @Test
    void alwaysMatchesAnyValue() {
        assertTrue(AlertCondition.always().matches(1));
        assertTrue(AlertCondition.always().matches(0));
        assertTrue(AlertCondition.always().matches(-99));
    }

    @Test
    void andRequiresBothToMatch() {
        AlertCondition both = AlertCondition.divisibleBy(3).and(AlertCondition.divisibleBy(5));
        assertTrue(both.matches(15));
        assertFalse(both.matches(3));
        assertFalse(both.matches(5));
    }

    @Test
    void orRequiresEitherToMatch() {
        AlertCondition either = AlertCondition.divisibleBy(3).or(AlertCondition.divisibleBy(5));
        assertTrue(either.matches(3));
        assertTrue(either.matches(5));
        assertFalse(either.matches(7));
    }

    @Test
    void notInvertsMatch() {
        AlertCondition notThree = AlertCondition.divisibleBy(3).not();
        assertFalse(notThree.matches(3));
        assertTrue(notThree.matches(4));
    }
}
