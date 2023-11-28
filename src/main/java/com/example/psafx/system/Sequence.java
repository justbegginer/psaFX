package com.example.psafx.system;

import lombok.Getter;

public class Sequence {
    @Getter
    private double currentValue;

    private double step;

    public Sequence(double startValue, double step) {
        this.currentValue = startValue;
        this.step = step;
    }

    public double iterate() {
        double lastValue = this.currentValue;
        this.currentValue += step;
        return lastValue;
    }
}
