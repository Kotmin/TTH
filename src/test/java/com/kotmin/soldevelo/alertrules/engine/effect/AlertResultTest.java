package com.kotmin.soldevelo.alertrules.engine.effect;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AlertResultTest {

    @Test
    void newResultIsEmpty() {
        assertTrue(new AlertResult().isEmpty());
    }

    @Test
    void appendMakesNonEmpty() {
        AlertResult result = new AlertResult();
        result.append("LOW");
        assertFalse(result.isEmpty());
        assertEquals("LOW", result.toString());
    }

    @Test
    void appendConcatenatesInOrder() {
        AlertResult result = new AlertResult();
        result.append("A");
        result.append("B");
        assertEquals("AB", result.toString());
    }

    @Test
    void removeLastCharacterTrimsOneCharacter() {
        AlertResult result = new AlertResult();
        result.append("AB");
        result.removeLastCharacter();
        assertEquals("A", result.toString());
    }

    @Test
    void removeLastCharacterOnEmptyResultIsNoop() {
        AlertResult result = new AlertResult();
        result.removeLastCharacter();
        assertTrue(result.isEmpty());
    }

    @Test
    void valueOrDefaultReturnsLabelWhenNonEmpty() {
        AlertResult result = new AlertResult();
        result.append("LOW");
        assertEquals("LOW", result.valueOrDefault(3));
    }

    @Test
    void valueOrDefaultReturnsStringOfValueWhenEmpty() {
        assertEquals("42", new AlertResult().valueOrDefault(42));
    }
}
