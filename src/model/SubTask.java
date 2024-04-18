package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int idEpic;

    public SubTask(TypeTask typeTask, String subTaskName, String SubTaskDescription, Duration duration , LocalDateTime startTime , int idEpic) {
        super(typeTask, subTaskName ,SubTaskDescription,duration,startTime);
        this.idEpic = idEpic;
    }

    public SubTask(int taskID, TypeTask typeTask, String subTaskName, TaskStatus subTaskStatus, String subTaskDescription, int idEpic,
                   Duration duration,
                   LocalDateTime startTime) {
        super(taskID,typeTask, subTaskName,subTaskStatus, subTaskDescription, duration,startTime);
        this.idEpic = idEpic;
        LocalDateTime endTime = getEndTime();
    }

    public SubTask(int taskID, TypeTask typeTask, String taskName, TaskStatus taskStatus, String taskDescription, int idEpic, LocalDateTime startTime, Duration duration, LocalDateTime endTime) {
        super(taskID, typeTask, taskName, taskStatus, taskDescription, idEpic, startTime, duration, endTime);
        this.idEpic = idEpic;
    }

    public SubTask(String taskName, String taskDescription, int idEpic) {
        super(taskName, taskDescription);
        this.idEpic  = idEpic;
    }

    public SubTask(String taskName, String taskDescription) {
        super(taskName, taskDescription);
    }

    public SubTask(int taskID, String taskName, String taskDescription) {
        super(taskID,taskName, taskDescription);
    }

    public SubTask(int taskID,String taskName, String taskDescription, int idEpic) {
        super(taskID,taskName, taskDescription);
        this.idEpic = idEpic ;
    }

    public int getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }


}
