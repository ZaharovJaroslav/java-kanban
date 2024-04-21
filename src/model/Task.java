package model;

import java.time.Duration;
import java.time.LocalDateTime;

import static service.TaskManager.DATA_TIME_FORMAT;

public class Task {
    private int taskID;
    private String taskName;
    private String taskDescription;
    private  TaskStatus taskStatus;
    private TypeTask typeTask;
    private Duration duration;
    private LocalDateTime startTime;


    public Task(TypeTask typeTask, String taskName, String taskDescription) {
        this.typeTask = typeTask;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }

    public Task(TypeTask typeTask, String taskName, String taskDescription, Duration duration, LocalDateTime startTime) {
        this.typeTask = typeTask;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(int taskID, TypeTask typeTask, String taskName, TaskStatus taskStatus, String taskDescription,
                 LocalDateTime startTime,
                 Duration duration) {
        this.taskID = taskID;
        this.typeTask = typeTask;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.taskDescription = taskDescription;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(int taskID, TypeTask typeTask, String taskName, TaskStatus taskStatus, String taskDescription,
                 LocalDateTime startTime,
                 Duration duration,
                 LocalDateTime endTime) {
        this.taskID = taskID;
        this.typeTask = typeTask;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.taskDescription = taskDescription;
        this.duration = duration;
        this.startTime = startTime;
        endTime = endTime;
    }

    public int getTaskID() {
        return taskID;
    }

    public Task(int taskID, String taskName, String taskDescription) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }

    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }

    public void setTypeTask(TypeTask typeTask) {
        this.typeTask = typeTask;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public void setName(String taskName) {
        this.taskName = taskName;
    }

    public String getName() {
        return taskName;
    }

    public void setDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public TypeTask getTypeTask() {
        return typeTask;
    }

    public String getDescription() {
        return taskDescription;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

     public void setStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;

     }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return  startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plus(duration);
        } else
            return null;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "Тип задачи: " + typeTask + "\n" +
                "Название: "  + taskName + "\n" +
                "Описание: " + taskDescription + "\n"  +
                "Статус: " + taskStatus + "\n" +
                "Длительность: " + duration.toHours() + ":" + duration.toMinutesPart() + "\n"  +
                "Время начала выполнения: " + getStartTime().format(DATA_TIME_FORMAT) + "\n"  +
                "Дедлайн задачи: " +  getEndTime().format(DATA_TIME_FORMAT);

    }

}
