package com.kotmin.soldevelo.alertrules.engine;

import com.kotmin.soldevelo.alertrules.engine.effect.AlertResult;
import com.kotmin.soldevelo.alertrules.engine.rule.AlertRule;
import java.util.List;
import java.util.Objects;

public final class StrategyAlertEngine implements AlertEngine {

    private final List<AlertRule> rules;

    public StrategyAlertEngine(List<AlertRule> rules) {
        Objects.requireNonNull(rules, "Rules must not be null");
        if (rules.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("Rules must not contain null elements");
        }
        this.rules = List.copyOf(rules);
    }

    @Override
    public String evaluate(int value) {
        AlertResult result = new AlertResult();
        for (AlertRule rule : rules) {
            if (rule.matches(value)) {
                rule.apply(result);
            }
        }
        return result.valueOrDefault(value);
    }
}
