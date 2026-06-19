package com.kotmin.soldevelo.alertrules;

import com.kotmin.soldevelo.alertrules.engine.simple.SimpleAlertEngine;

import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        var engine = new SimpleAlertEngine();
        IntStream.rangeClosed(1, 20)
                .mapToObj(engine::evaluate)
                .forEach(System.out::println);
    }
}
