package com.kotmin.soldevelo.alertrules.config;

import com.kotmin.soldevelo.alertrules.engine.AlertEngine;
import com.kotmin.soldevelo.alertrules.engine.simple.SimpleAlertEngine;
import com.kotmin.soldevelo.alertrules.engine.strategy.StrategyAlertEngine;

import java.util.List;

public class AlertEngineFactory {

    public static AlertEngine create(String engineName, String ruleSetName) {
        return switch (engineName.toLowerCase()) {
            case "simple" -> new SimpleAlertEngine();
            case "strategy" -> new StrategyAlertEngine(RuleSetFactory.create(ruleSetName));
            default -> throw new IllegalArgumentException("Unknown engine: " + engineName);
        };
    }

    public static AlertEngine createDefault(String ruleSetName) {
        return new StrategyAlertEngine(RuleSetFactory.create(ruleSetName));
    }
}
