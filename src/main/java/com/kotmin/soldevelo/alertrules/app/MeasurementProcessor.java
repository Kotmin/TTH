package com.kotmin.soldevelo.alertrules.app;

import java.util.List;

public interface MeasurementProcessor {
    List<String> process(List<Integer> values);
}
