package com.kotmin.soldevelo.alertrules.engine.effect;

public final class AlertResult {

    private final StringBuilder value = new StringBuilder();

    public void append(String label) {
        value.append(label);
    }

    public void removeLastCharacter() {
        if (!value.isEmpty()) {
            value.deleteCharAt(value.length() - 1);
        }
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public String valueOrDefault(int originalValue) {
        return value.isEmpty() ? String.valueOf(originalValue) : value.toString();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
