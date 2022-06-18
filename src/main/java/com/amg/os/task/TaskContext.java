package com.amg.os.task;

import java.io.Serializable;
import java.util.Arrays;
import java.util.StringJoiner;

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
    public boolean inUse;
    public boolean done;
    public Integer lastIndicesIndex=-1;
    public Integer lastSleepIndex =-1;
    public int totalTimeSlept;

    private int lastSleepDuration;
    private int currentSum;

    public TaskContext() {
        currentSum = 0;
        lastSleepDuration = 0;
        inUse = false;
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
            return sleeps[lastSleepIndex + 1];
        }
    }

    public Integer getNextReadIndex() {
        if (lastIndicesIndex > indices.length - 2) return null;
        return lastIndicesIndex + 1;
    }
public int getTotalEstimatedTime(){
        int total=0;
        for(int i:sleeps){
            total+=i;
        }
        return total;
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

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getCurrentSum() {
        return currentSum;
    }

    public void setCurrentSum(int currentSum) {
        this.currentSum = currentSum;
    }

    public Integer getLastIndicesIndex() {
        return lastIndicesIndex;
    }

    public Integer getLastSleepIndex() {
        return lastSleepIndex;
    }

    public int getLastSleepDuration() {
        return lastSleepDuration;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TaskContext.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("indices=" + Arrays.toString(indices))
                .add("sleeps=" + Arrays.toString(sleeps))
                .add("inUse=" + inUse)
                .add("done=" + done)
                .add("lastIndicesIndex=" + lastIndicesIndex)
                .add("lastSleepIndex=" + lastSleepIndex)
                .add("totalTimeSlept=" + totalTimeSlept)
                .add("lastSleepDuration=" + lastSleepDuration)
                .add("currentSum=" + currentSum)
                .toString();
    }
}
