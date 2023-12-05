package com.example.psafx.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.example.psafx.util.TimeScale;

import java.util.Optional;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Action {
    public enum Type{
        TASK_DENY,

        BUFFER_RELEASE,
        DEVICE_RELEASE,
        BUFFER_ADD,
        DEVICE_ADD
    }

    public enum EntityType{
        BUFFER,
        DEVICE,
        DENY
    }

    private Type type;

    private EntityType entityType;

    private Optional<Integer> entityNumber;

    private TimeScale entityTimeScale;

    private int taskGroup;

    private int taskNumber;

    private TimeScale taskTimeScale;
}
