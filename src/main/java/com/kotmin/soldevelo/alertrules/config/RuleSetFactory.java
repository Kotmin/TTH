package com.kotmin.soldevelo.alertrules.config;

import com.kotmin.soldevelo.alertrules.engine.strategy.AlertRule;
import com.kotmin.soldevelo.alertrules.engine.strategy.DivisibilityAlertRule;

import java.util.List;

public class RuleSetFactory {

    public static List<AlertRule> create(String name) {
        return switch (name.toLowerCase()) {
            case "base" -> List.of(
                    new DivisibilityAlertRule(3, "LOW"),
                    new DivisibilityAlertRule(5, "ADVISORY")
            );
            case "extended" -> List.of(
                    new DivisibilityAlertRule(3, "LOW"),
                    new DivisibilityAlertRule(5, "ADVISORY"),
                    new DivisibilityAlertRule(7, "WARN")
            );
            default -> throw new IllegalArgumentException("Unknown rule set: " + name);
        };
    }
}
