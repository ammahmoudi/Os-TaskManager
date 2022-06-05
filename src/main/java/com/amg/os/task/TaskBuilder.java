package com.amg.os.task;

public class TaskBuilder {
    public static int lastId = 0;
    public int id;
    public int[] indices;
    public int[] sleeps;
    public Integer lastIndicesIndex;
    public Integer lastSleepIndex;

    public TaskBuilder(String taskString) {
        id = lastId++;
        String[] taskStringArray = taskString.split(" ");
        indices = new int[taskStringArray.length / 2];
        sleeps = new int[taskStringArray.length / 2];

        for (int i = 0; i < taskStringArray.length; i += 2) {
            sleeps[i / 2] = Integer.parseInt(taskStringArray[i]);
            indices[i / 2] = Integer.parseInt(taskStringArray[i + 1]);
        }

    }
    public  TaskContext getContext(){
        TaskContext taskContext=new TaskContext();
        taskContext.sleeps=sleeps;
        taskContext.indices=indices;
        taskContext.id=id;
        return taskContext;
    }
}
