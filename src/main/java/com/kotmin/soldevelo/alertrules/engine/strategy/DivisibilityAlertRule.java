package com.kotmin.soldevelo.alertrules.engine.strategy;

public final class DivisibilityAlertRule implements AlertRule {

    private final int divisor;
    private final String label;

    public DivisibilityAlertRule(int divisor, String label) {
        if (divisor == 0) throw new IllegalArgumentException("Divisor must not be zero");
        this.divisor = divisor;
        this.label = label;
    }

    @Override
    public boolean matches(int value) {
        return value % divisor == 0;
    }

    @Override
    public String label() {
        return label;
    }
}
