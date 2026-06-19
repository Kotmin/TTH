package com.kotmin.soldevelo.alertrules.engine.rule;

import com.kotmin.soldevelo.alertrules.engine.condition.AlertCondition;
import com.kotmin.soldevelo.alertrules.engine.effect.AlertEffect;
import com.kotmin.soldevelo.alertrules.engine.effect.AlertResult;
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
