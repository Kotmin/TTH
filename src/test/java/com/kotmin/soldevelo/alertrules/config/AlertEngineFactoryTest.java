package com.kotmin.soldevelo.alertrules.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AlertEngineFactoryTest {

    @Test
    void defaultEngineEvaluatesBaseRules() {
        var engine = AlertEngineFactory.createDefault("base");
        assertEquals("LOW", engine.evaluate(3));
        assertEquals("ADVISORY", engine.evaluate(5));
        assertEquals("LOWADVISORY", engine.evaluate(15));
        assertEquals("1", engine.evaluate(1));
    }

    @Test
    void defaultEngineEvaluatesExtendedRules() {
        var engine = AlertEngineFactory.createDefault("extended");
        assertEquals("LOWWARN", engine.evaluate(21));
        assertEquals("ADVISORYWARN", engine.evaluate(35));
        assertEquals("LOWADVISORYWARN", engine.evaluate(105));
    }

    @Test
    void simpleEngineCanBeCreatedByName() {
        var engine = AlertEngineFactory.create("simple", "base");
        assertEquals("LOW", engine.evaluate(3));
        assertEquals("LOWADVISORY", engine.evaluate(15));
    }

    @Test
    void factoryRejectsUnknownEngineName() {
        assertThrows(IllegalArgumentException.class, () -> AlertEngineFactory.create("unknown", "base"));
    }

    @Test
    void factoryRejectsUnknownRuleSetName() {
        assertThrows(IllegalArgumentException.class, () -> AlertEngineFactory.createDefault("unknown"));
    }
}
