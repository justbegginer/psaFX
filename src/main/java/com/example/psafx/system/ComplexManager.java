package com.example.psafx.system;

import com.example.psafx.util.Action;
import com.example.psafx.util.TimeScale;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class ComplexManager {
    private double currentTime;

    private final TaskManager taskManager;

    private final BufferManager bufferManager;

    private final DeviceManager deviceManager;

    private int taskCount;

    private int bufferCount;

    private int deviceCount;

    public ComplexManager(int taskCount, int bufferCount, int deviceCount, double timeToGenerate, double timeToFinishSeconds) {
        this.taskManager = new TaskManager(taskCount, timeToGenerate);
        this.bufferManager = new BufferManager(bufferCount);
        this.deviceManager = new DeviceManager(deviceCount, timeToFinishSeconds);
        this.taskCount = taskCount;
        this.bufferCount = bufferCount;
        this.deviceCount = deviceCount;
        currentTime = 0;
    }

    public String iterateString() {
        Double timeNextDeviceEvent = this.deviceManager.getClosestEvent();
        double timeNextTaskEvent = this.taskManager.getClosestEventTime();
        if (timeNextDeviceEvent == null || timeNextTaskEvent < timeNextDeviceEvent) {
            currentTime = timeNextTaskEvent;
            Task task = taskManager.getClosestTask();
            if (deviceManager.isDevicesBusy()) {
                if (bufferManager.isBuffersBusy()) {
                    if (bufferManager.minTaskByNumber().isEmpty()) {
                        return String.format("DENY new Task %s.%s all devices and buffers is busy at time %s", task.getGroup(), task.getNumber(), timeNextTaskEvent);
                    } else {
                        Buffer buffer = bufferManager.minTaskByNumber().get();
                        if (task.getGroup() < buffer.getNum()) {
                            return String.format("DENY new Task %s.%s all devices and buffers is busy at time %s", task.getGroup(), task.getNumber(), timeNextTaskEvent);
                        } else {
                            Task taskFromBuffer = buffer.popTask(currentTime);
                            String result = String.format("DENY Task %s.%s from Buffer %s SCALE(%s) and add New Task %s.%s to this Buffer",
                                    taskFromBuffer.getGroup(), taskFromBuffer.getNumber(), buffer.getNum(), buffer.getTimeScale(), task.getGroup(), task.getNumber());
                            bufferManager.addNewTask(task);// TODO continue
                            return result;
                        }
                    }
                } else {
                    Buffer buffer = bufferManager.addNewTask(task);
                    return String.format("Buffer %d at SCALE(%s) add Task %s.%s", buffer.getNum(), buffer.getTimeScale(), task.getGroup(), task.getNumber());
                }
            } else {
                Device device = deviceManager.addNewTask(task, currentTime);
                return String.format("Task %s.%s received for service\tDevice %d at time(%s) start to execute task", task.getGroup(), task.getNumber(), device.getNumber(), currentTime);
            }
        }
        //TODO doesn't work proper
        else {
            currentTime = timeNextDeviceEvent;
            Device device = this.deviceManager.popDevice();
            StringBuilder output = new StringBuilder(String.format("Device %s at SCALE(%s) complete Task %s.%s at SCALE(%s)",
                    device.getNumber(), device.getTimeScale(), device.getTask().getGroup(),device.getTask().getNumber(), device.getTask().getTimeScale()));
            device.releaseTask();
            if (!bufferManager.isBuffersEmpty()) {
                Buffer buffer = bufferManager.popBuffer();
                Task task = buffer.popTask(currentTime);
                device.addTask(task, currentTime);
                output.append(String.format("\tand get Task %s.%s from Buffer %s at SCALE(%s)", task.getGroup(), task.getNumber(), buffer.getNum(), buffer.getTimeScale()));
            }
            return output.toString();
        }
    }

    public List<Action> iterateAction(){
        Double timeNextDeviceEvent = this.deviceManager.getClosestEvent();
        double timeNextTaskEvent = this.taskManager.getClosestEventTime();
        List<Action> resultList = new ArrayList<>(1);
        if (timeNextDeviceEvent == null || timeNextTaskEvent < timeNextDeviceEvent) {
            currentTime = timeNextTaskEvent;
            Task task = taskManager.getClosestTask();
            if (deviceManager.isDevicesBusy()) {
                if (bufferManager.isBuffersBusy()) {
                    if (bufferManager.minTaskByNumber().isEmpty()) {
                        resultList.add(new Action(Action.Type.TASK_DENY, Action.EntityType.DENY, Optional.empty(), new TimeScale(currentTime), task.getGroup(), task.getNumber(), new TimeScale(currentTime)));
                    } else {
                        Buffer buffer = bufferManager.minTaskByNumber().get();
                        if (task.getGroup() < buffer.getNum()) {
                            resultList.add(new Action(Action.Type.TASK_DENY, Action.EntityType.DENY, Optional.empty(), new TimeScale(currentTime), task.getGroup(), task.getNumber(), new TimeScale(currentTime)));
                        } else {
                            // TODO check this part of code may be a lot of problem here
                            Task taskFromBuffer = buffer.popTask(currentTime);// TODO this may ruin the time if this shit gonna delete by garbage collector
                            resultList.add(new Action(Action.Type.TASK_DENY, Action.EntityType.DEVICE, Optional.of(buffer.getNum()), buffer.getTimeScale(), taskFromBuffer.getGroup(), taskFromBuffer.getNumber(), new TimeScale(currentTime))); // TODO fix end time of timescale
                            resultList.add(new Action(Action.Type.BUFFER_ADD, Action.EntityType.BUFFER, Optional.of(buffer.getNum()), buffer.getTimeScale(), task.getGroup(), task.getNumber(), new TimeScale(currentTime))); // TODO fix timeScale
                            bufferManager.addNewTask(task);
                        }
                    }
                } else {
                    Buffer buffer = bufferManager.addNewTask(task);
                    resultList.add(new Action(Action.Type.BUFFER_ADD, Action.EntityType.BUFFER, Optional.of(buffer.getNum()), buffer.getTimeScale(), task.getGroup(), task.getNumber(), task.getTimeScale()));
                }
            } else {
                Device device = deviceManager.addNewTask(task, currentTime);
                resultList.add(new Action(Action.Type.DEVICE_ADD, Action.EntityType.DEVICE, Optional.of(device.getNumber()), device.getTimeScale(), task.getGroup(), task.getNumber(), task.getTimeScale()));
            }
        }
        // when device release
        else {
            currentTime = timeNextDeviceEvent;
            Device device = this.deviceManager.popDevice();
            resultList.add(new Action(Action.Type.DEVICE_RELEASE, Action.EntityType.DEVICE, Optional.of(device.getNumber()), device.getTimeScale(), device.getTask().getGroup(), device.getTask().getNumber(), device.getTask().getTimeScale()));
            device.releaseTask();
            // get task from buffer if something exists in any buffer
            if (!bufferManager.isBuffersEmpty()) {
                Buffer buffer = bufferManager.popBuffer();
                Task task = buffer.popTask(currentTime);
                device.addTask(task, currentTime);
                resultList.add(new Action(Action.Type.BUFFER_RELEASE, Action.EntityType.BUFFER, Optional.of(buffer.getNum()), buffer.getTimeScale(), task.getGroup(), task.getNumber(), task.getTimeScale()));
                resultList.add(new Action(Action.Type.DEVICE_ADD, Action.EntityType.DEVICE, Optional.of(device.getNumber()), device.getTimeScale(), task.getGroup(), task.getNumber(), task.getTimeScale()));
            }
        }
        return resultList;
    }

}
