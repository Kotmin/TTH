package com.kotmin.soldevelo.alertrules.engine.condition;

import java.util.Objects;

@FunctionalInterface
public interface AlertCondition {

    boolean matches(int value);

    default AlertCondition and(AlertCondition other) {
        Objects.requireNonNull(other, "Other condition must not be null");
        return value -> this.matches(value) && other.matches(value);
    }

    default AlertCondition or(AlertCondition other) {
        Objects.requireNonNull(other, "Other condition must not be null");
        return value -> this.matches(value) || other.matches(value);
    }

    default AlertCondition not() {
        return value -> !this.matches(value);
    }

    static AlertCondition divisibleBy(int divisor) {
        if (divisor == 0) {
            throw new IllegalArgumentException("Divisor must not be zero");
        }
        return value -> value % divisor == 0;
    }

    static AlertCondition always() {
        return value -> true;
    }
}
