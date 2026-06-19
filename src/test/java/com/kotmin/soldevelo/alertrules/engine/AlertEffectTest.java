package com.kotmin.soldevelo.alertrules.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AlertEffectTest {

    @Test
    void appendAddsLabel() {
        AlertResult result = new AlertResult();
        AlertEffect.append("LOW").apply(result);
        assertEquals("LOW", result.toString());
    }

    @Test
    void appendConcatenatesOnSubsequentCalls() {
        AlertResult result = new AlertResult();
        AlertEffect.append("A").apply(result);
        AlertEffect.append("B").apply(result);
        assertEquals("AB", result.toString());
    }

    @Test
    void removeLastCharacterTrimsTail() {
        AlertResult result = new AlertResult();
        result.append("AB");
        AlertEffect.removeLastCharacter().apply(result);
        assertEquals("A", result.toString());
    }

    @Test
    void noOpLeavesResultUnchanged() {
        AlertResult result = new AlertResult();
        result.append("X");
        AlertEffect.noOp().apply(result);
        assertEquals("X", result.toString());
    }
}
