package com.example.psafx.views;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TaskStatistic {
    private IntegerProperty taskNumber;
    private DoubleProperty denyProbability;

    private IntegerProperty completedTaskCount;

    private IntegerProperty deniedTaskCount;

    private DoubleProperty averageTimeInSystem;

    private DoubleProperty averageWaitTimeInSystem;

    private DoubleProperty dispersionTimeInSystem;

    private DoubleProperty dispersionWaitTimeInSystem;

    public TaskStatistic(int taskNumber, double denyProbability,
                         int completedTaskCount, int deniedTaskCount,
                         double averageTimeInSystem, double averageWaitTimeInSystem,
                         double dispersionTimeInSystem, double dispersionWaitTimeInSystem) {
        this.taskNumber = new SimpleIntegerProperty(taskNumber);
        this.denyProbability = new SimpleDoubleProperty(denyProbability);
        this.completedTaskCount = new SimpleIntegerProperty(completedTaskCount);
        this.deniedTaskCount = new SimpleIntegerProperty(deniedTaskCount);
        this.averageTimeInSystem = new SimpleDoubleProperty(averageTimeInSystem);
        this.averageWaitTimeInSystem = new SimpleDoubleProperty(averageWaitTimeInSystem);
        this.dispersionTimeInSystem = new SimpleDoubleProperty(dispersionTimeInSystem);
        this.dispersionWaitTimeInSystem = new SimpleDoubleProperty(dispersionWaitTimeInSystem);
    }

    public int getTaskNumber() {
        return taskNumber.get();
    }

    public IntegerProperty taskNumberProperty() {
        return taskNumber;
    }

    public double getDenyProbability() {
        return denyProbability.get();
    }

    public DoubleProperty denyProbabilityProperty() {
        return denyProbability;
    }

    public int getCompletedTaskCount() {
        return completedTaskCount.get();
    }

    public IntegerProperty completedTaskCountProperty() {
        return completedTaskCount;
    }

    public int getDeniedTaskCount() {
        return deniedTaskCount.get();
    }

    public IntegerProperty deniedTaskCountProperty() {
        return deniedTaskCount;
    }

    public double getAverageTimeInSystem() {
        return averageTimeInSystem.get();
    }

    public DoubleProperty averageTimeInSystemProperty() {
        return averageTimeInSystem;
    }

    public double getAverageWaitTimeInSystem() {
        return averageWaitTimeInSystem.get();
    }

    public DoubleProperty averageWaitTimeInSystemProperty() {
        return averageWaitTimeInSystem;
    }

    public double getDispersionTimeInSystem() {
        return dispersionTimeInSystem.get();
    }

    public DoubleProperty dispersionTimeInSystemProperty() {
        return dispersionTimeInSystem;
    }

    public double getDispersionWaitTimeInSystem() {
        return dispersionWaitTimeInSystem.get();
    }

    public DoubleProperty dispersionWaitTimeInSystemProperty() {
        return dispersionWaitTimeInSystem;
    }
}
