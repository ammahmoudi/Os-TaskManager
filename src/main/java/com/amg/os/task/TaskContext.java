package com.amg.os.task;

import java.io.Serializable;

public class TaskContext implements Serializable {

    /**
     * List of read indicies
     * List of sleeps
     * <p>
     * Total time slept
     * Sum of read numbers
     */

    public int id;
    public int[] indices;
    public int[] sleeps;
    public Integer lastIndicesIndex;
    public Integer lastSleepIndex;
    public int totalTimeSlept;

    private int lastSleepDuration;
    private int currentSum;

    public TaskContext() {
        currentSum = 0;
        lastSleepDuration = 0;
    }

    public void setLastSleepDuration(int duration) {
        this.lastSleepDuration = duration;
    }

    public void setLastSleepIndex(int index) {
        this.lastSleepIndex = index;
    }

    public void setLastReadAttempt(int index) {
        lastIndicesIndex = index;
    }

    public Integer getNextSleep() {
        if (lastSleepDuration != 0) {
            return lastSleepDuration;
        } else {
            if (lastSleepIndex > sleeps.length - 2) return null;
            return sleeps[lastSleepIndex+1];
        }
    }

    public Integer getNextReadIndex() {
        if (lastIndicesIndex > indices.length - 2) return null;
        return lastIndicesIndex + 1;
    }

    public int getId() {
        return id;
    }

    public int getResult() {
        return currentSum;
    }

    public int getTotalTimeSlept() {
        return totalTimeSlept;
    }

    public void setTotalTimeSlept(int totalTimeSlept) {
        this.totalTimeSlept = totalTimeSlept;
    }
}
