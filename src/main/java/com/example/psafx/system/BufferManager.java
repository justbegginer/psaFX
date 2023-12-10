package com.example.psafx.system;


import lombok.Getter;

import java.util.*;

public class BufferManager {

    @Getter
    private Integer lastTaken;

    private List<Buffer> bufferList;

    public BufferManager(int buffersCount) {
        bufferList = new ArrayList<>(buffersCount);
        for (int i = 0; i < buffersCount; i++) {
            bufferList.add(new Buffer(i + 1));
        }
        this.lastTaken = null;
    }

    public Buffer addNewTask(Task task) {
        for (Buffer buffer : this.bufferList) {
            if (!buffer.isBusy()) {
                buffer.addTask(task);
                return buffer;
            }
        }
        return null;
    }

    private void setMinTaskNumber() {
        lastTaken = bufferList.stream()
                .filter(buffer -> buffer.getTask() != null)
                .min(Comparator.comparingDouble(buffer -> buffer.getTask().getGroup()))
                .get()
                .getTask().getGroup();
    }

    public Buffer popBuffer() {
        if (isBuffersEmpty()){
            return null;
        }
        if (lastTaken == null
                || bufferList.stream()
                .filter(buffer -> buffer.getTask() != null)
                .filter(buffer -> buffer.getTask().getGroup() == lastTaken).findAny().isEmpty()) {
            this.setMinTaskNumber();
        }
        return this.bufferList.stream()
                .filter(buffer -> buffer != null)
                .filter(buffer -> buffer.getTask() != null)
                .filter((buffer -> buffer.getTask().getGroup() == lastTaken))
                .findFirst().get();


    }
    public boolean isBuffersEmpty(){
        for (Buffer buffer : bufferList) {
            if (buffer.isBusy()){
                return false;
            }
        }
        return true;
    }

    public boolean isBuffersBusy(){
        for (Buffer buffer : bufferList) {
            if(!buffer.isBusy()){
                return false;
            }
        }
        return true;
    }

    public Optional<Buffer> minTaskByNumber(){
        return this.bufferList.stream()
                .filter(buffer -> buffer.getTask() != null)
                .min(Comparator.comparingInt(buffer -> buffer.getTask().getGroup()));
    }
}
