package com.kotmin.soldevelo.alertrules.engine;

@FunctionalInterface
public interface AlertEffect {

    void apply(AlertResult result);

    static AlertEffect append(String label) {
        return result -> result.append(label);
    }

    static AlertEffect removeLastCharacter() {
        return AlertResult::removeLastCharacter;
    }

    static AlertEffect noOp() {
        return result -> {};
    }
}
