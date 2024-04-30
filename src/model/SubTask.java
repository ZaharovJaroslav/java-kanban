package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int idEpic;


    public SubTask(String taskName, String taskDescription, int idEpic) {
        super(taskName,taskDescription);
        this.idEpic = idEpic;
    }

    public SubTask(int taskID, String taskName, String taskDescription) {
        super(taskID,taskName,taskDescription);
    }

    public SubTask(int taskID, String taskName, String taskDescription, int idEpic) {
        super(taskID,taskName,taskDescription);
        this.idEpic = idEpic;
    }

    public SubTask(TypeTask typeTask, String subTaskName, String subTaskDescription, Duration duration, LocalDateTime startTime, int idEpic) {
        super(typeTask,subTaskName,subTaskDescription,duration,startTime);
        this.idEpic = idEpic;
    }

    public SubTask(int taskID, TypeTask typeTask, String subTaskName, TaskStatus subTaskStatus, String subTaskDescription, int idEpic,
                   LocalDateTime startTime, Duration duration, LocalDateTime endTime) {
        super(taskID,typeTask,subTaskName,subTaskStatus,subTaskDescription, startTime, duration);
        this.idEpic = idEpic;
        endTime = getEndTime();
    }


    public int getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }
}
