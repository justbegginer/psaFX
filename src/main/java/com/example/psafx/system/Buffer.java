package com.example.psafx.system;

import lombok.Getter;
import com.example.psafx.util.TimeScale;

public class Buffer {
    @Getter
    private TimeScale timeScale;

    @Getter
    private final int num;

    @Getter
    private Task task;

    public Buffer(int num){
        this.num = num;
    }

    public boolean isBusy(){
        return this.task != null;
    }

    public boolean addTask(Task task){
        if (isBusy()){
            return false;
        }
        else{
            this.timeScale = task.getTimeScale().copy();
            this.task = task;
            return true;
        }
    }

    public Task popTask(double currentTime){
        this.timeScale.setEndTime(currentTime);
        Task task = this.task;
        this.task = null;
        return task;
    }

}
