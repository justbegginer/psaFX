package com.example.psafx.system;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.example.psafx.util.TimeScale;

@Getter
@Setter
public class Task implements Cloneable {
    private TimeScale timeScale;

    private final int group;

    private int number;

    public Task(int group) {
        this.group = group;
        this.number = 1;
    }

    private Task(Task another) {
        this.group = another.group;
        this.timeScale = another.getTimeScale().copy();
    }

    public void iterateNumber(){
        this.number++;
    }

    @Override
    public Task clone() {
        return new Task(this);
    }

    @Override
    public String toString() {
        return "Task " + this.group + "." + this.number;
    }
}
