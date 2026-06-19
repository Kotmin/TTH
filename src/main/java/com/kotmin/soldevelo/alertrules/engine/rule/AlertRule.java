package com.kotmin.soldevelo.alertrules.engine.rule;

import com.kotmin.soldevelo.alertrules.engine.effect.AlertResult;

public interface AlertRule {

    boolean matches(int value);

    void apply(AlertResult result);
}
