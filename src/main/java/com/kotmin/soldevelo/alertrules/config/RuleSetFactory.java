package com.kotmin.soldevelo.alertrules.config;

import com.kotmin.soldevelo.alertrules.engine.AlertCondition;
import com.kotmin.soldevelo.alertrules.engine.AlertEffect;
import com.kotmin.soldevelo.alertrules.engine.AlertRule;
import com.kotmin.soldevelo.alertrules.engine.ConditionalAlertRule;
import java.util.List;

public class RuleSetFactory {

    public static List<AlertRule> create(String name) {
        return switch (name.toLowerCase()) {
            case "base" -> List.of(
                    new ConditionalAlertRule(AlertCondition.divisibleBy(3), AlertEffect.append("LOW")),
                    new ConditionalAlertRule(AlertCondition.divisibleBy(5), AlertEffect.append("ADVISORY")));
            case "extended" -> List.of(
                    new ConditionalAlertRule(AlertCondition.divisibleBy(3), AlertEffect.append("LOW")),
                    new ConditionalAlertRule(AlertCondition.divisibleBy(5), AlertEffect.append("ADVISORY")),
                    new ConditionalAlertRule(AlertCondition.divisibleBy(7), AlertEffect.append("WARN")));
            default -> throw new IllegalArgumentException("Unknown rule set: " + name);
        };
    }
}
