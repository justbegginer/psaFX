package com.example.psafx.views;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class DeviceStatistic {
    private final IntegerProperty deviceNumber;
    private final DoubleProperty busyRatio;

    public DeviceStatistic(int deviceNumber, double deviceRatio){
        this.deviceNumber = new SimpleIntegerProperty(deviceNumber);
        this.busyRatio = new SimpleDoubleProperty(deviceRatio);
    }

    public int getDeviceNumber() {
        return deviceNumber.get();
    }

    public IntegerProperty deviceNumberProperty() {
        return deviceNumber;
    }

    public double getBusyRatio() {
        return busyRatio.get();
    }

    public DoubleProperty busyRatioProperty() {
        return busyRatio;
    }
}
