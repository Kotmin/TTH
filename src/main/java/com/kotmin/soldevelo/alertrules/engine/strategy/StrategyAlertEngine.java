package com.kotmin.soldevelo.alertrules.engine.strategy;

import com.kotmin.soldevelo.alertrules.engine.AlertEngine;
import java.util.List;

public class StrategyAlertEngine implements AlertEngine {

    private final List<AlertRule> rules;

    public StrategyAlertEngine(List<AlertRule> rules) {
        this.rules = List.copyOf(rules);
    }

    @Override
    public String evaluate(int value) {
        StringBuilder result = new StringBuilder();
        for (AlertRule rule : rules) {
            if (rule.matches(value)) result.append(rule.label());
        }
        return result.isEmpty() ? String.valueOf(value) : result.toString();
    }
}
