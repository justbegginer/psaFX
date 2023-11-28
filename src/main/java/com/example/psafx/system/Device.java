package com.example.psafx.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.example.psafx.util.TimeScale;


@Getter
@Setter
public class Device {

    private TimeScale timeScale;

    private final int number;

    private static final double timeToComplete = 1.1;

    private Task task;

    private boolean isBusy;

    public Device(int number) {
        this.number = number;
        isBusy = false;
    }


    public boolean addTask(Task task, double startTime) {
        if (this.task != null) {
            return false;
        }
        this.task = task;
        this.timeScale = new TimeScale(startTime, startTime + timeToComplete);
        this.task.getTimeScale().setEndTime(startTime + timeToComplete);
        isBusy = true;
        return true;
    }

    public Task releaseTask() {
        Task task = this.task.clone();
        isBusy = false;
        this.task = null;
        return task;
    }



}
