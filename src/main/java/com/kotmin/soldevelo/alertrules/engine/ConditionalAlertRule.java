package com.kotmin.soldevelo.alertrules.engine;

import java.util.Objects;

public final class ConditionalAlertRule implements AlertRule {

    private final AlertCondition condition;
    private final AlertEffect effect;

    public ConditionalAlertRule(AlertCondition condition, AlertEffect effect) {
        this.condition = Objects.requireNonNull(condition, "Condition must not be null");
        this.effect = Objects.requireNonNull(effect, "Effect must not be null");
    }

    @Override
    public boolean matches(int value) {
        return condition.matches(value);
    }

    @Override
    public void apply(AlertResult result) {
        effect.apply(result);
    }
}
