package com.example.psafx.system;

import com.example.psafx.util.TimeScale;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskManager {

    private final List<Sequence> sequences;

    private final List<Integer> numbersOfTasks;

    public TaskManager(int taskCount, double timeToFinishSeconds) {
        sequences = new ArrayList<>(taskCount);
        numbersOfTasks = new ArrayList<>(taskCount);
        for (int i = 0; i < taskCount; i++) {
            numbersOfTasks.add(1);
            sequences.add(new Sequence(i * 0.5, timeToFinishSeconds));
        }
    }

    private int indexOfClosest() {
        int index = 0;
        for (int i = 1; i < sequences.size(); i++) {
            if (sequences.get(index).getCurrentValue() > sequences.get(i).getCurrentValue()) {
                index = i;
            }
        }
        return index;
    }

    public Double getClosestEventTime() {
        return sequences.get(indexOfClosest()).getCurrentValue();
    }

    public Task getClosestTask() {
        double closestTime = getClosestEventTime();
        int taskNumber = indexOfClosest() + 1;
        Task task = new Task(taskNumber);
        task.setTimeScale(new TimeScale(closestTime));
        task.setNumber(numbersOfTasks.get(taskNumber-1));
        numbersOfTasks.set(taskNumber-1, numbersOfTasks.get(taskNumber-1)+1);
        sequences.get(indexOfClosest()).iterate();
        return task;

    }
}
