package com.kotmin.soldevelo.alertrules.engine.simple;

import com.kotmin.soldevelo.alertrules.engine.AlertEngine;

public class SimpleAlertEngine implements AlertEngine {

    @Override
    public String evaluate(int value) {
        if (value % 15 == 0) return "LOWADVISORY";
        if (value % 3 == 0) return "LOW";
        if (value % 5 == 0) return "ADVISORY";
        return String.valueOf(value);
    }
}
