package com.kotmin.soldevelo.alertrules.engine.strategy;

public interface AlertRule {
    boolean matches(int value);

    String label();
}
