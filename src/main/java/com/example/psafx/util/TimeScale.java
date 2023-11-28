package com.example.psafx.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeScale {
    private Double startTime;

    private Double endTime;

    public TimeScale(double startTime) {
        this.startTime = startTime;
    }

    public TimeScale(TimeScale another) {
        this.startTime = another.getStartTime();
        this.endTime = another.getEndTime();
    }

    public TimeScale copy() {
        return new TimeScale(this);
    }

    @Override
    public String toString() {
        return String.format("%.1f-%.1f", this.startTime, this.endTime);
    }
}
