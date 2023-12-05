package com.example.psafx.system;

import com.example.psafx.util.TimeScale;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DeviceManager {

    private final double timeToFinishSeconds;

    private List<Device> deviceList;

    public DeviceManager(int deviceCount, double timeToFinishSeconds) {
        deviceList = new ArrayList<>(deviceCount);
        for (int i = 0; i < deviceCount; i++) {
            deviceList.add(new Device(i + 1, timeToFinishSeconds));
        }
        this.timeToFinishSeconds = timeToFinishSeconds;
    }

    public Device addNewTask(Task task, double currentTime) {
        for (Device device : deviceList) {
            if (!device.isBusy()) {
                device.addTask(task, currentTime);
                return device;
            }
        }
        return null;
    }

    public Double getClosestEvent() {
        Optional<Device> deviceTemp = deviceList.stream()
                .filter(device -> device.getTimeScale() != null)
                .filter(device -> device.getTimeScale().getEndTime() != null)
                .filter(Device::isBusy)// фильтрация для исключения null значений
                .min(Comparator.comparingDouble(device -> device.getTimeScale().getEndTime()));
        if (deviceTemp.isPresent()){
            return deviceTemp.get().getTimeScale().getEndTime();
        }
        else{
            return null;
        }
    }

    public Device popDevice() {
        double closestEvent = getClosestEvent();
        for (Device device : deviceList) {
            if (device.isBusy() && device.getTimeScale().getEndTime() == closestEvent){
                return device;
            }
        }
        return null;
    }

    public boolean isDevicesBusy(){
        for (Device device : deviceList) {
            if(!device.isBusy()){
                return false;
            }
        }
        return true;
    }

    public boolean isDevicesEmpty(){
        for (Device device : deviceList){
            if(device.isBusy()){
                return false;
            }
        }
        return true;
    }

}
