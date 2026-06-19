package com.kotmin.soldevelo.alertrules.engine;

public interface AlertRule {

    boolean matches(int value);

    void apply(AlertResult result);
}
