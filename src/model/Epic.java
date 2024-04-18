package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private LocalDateTime endTime;
    private ArrayList <Integer>subTasksIds;


 /*   public Epic(TypeTask typeTask, String epicName, String epicDescription, Duration duration , LocalDateTime startTime) {
        super(typeTask, epicName ,epicDescription,duration,startTime);
        subTasksIds = new ArrayList<>();

    }*/
 public Epic(TypeTask typeTask, String taskName, String taskDescription) {
     super(typeTask, taskName, taskDescription);
     this.subTasksIds = new ArrayList<>();


 }




   public Epic(int taskID, TypeTask typeTask, String epicName, TaskStatus epicStatus, String epicDescription,
               LocalDateTime startTime,Duration duration, LocalDateTime endTime)  {
        super( taskID,typeTask,epicName,epicStatus, epicDescription,startTime,duration, endTime);
        this.endTime = endTime;
       subTasksIds = new ArrayList<>();
    }

   public Epic (int taskID, TypeTask typeTask, String taskName, TaskStatus taskStatus , String taskDescription ) {
        super(taskID,typeTask,taskName,taskStatus,taskDescription);
        subTasksIds = new ArrayList<>();

    }

    public Epic(int taskID, String taskName, String taskDescription) {
        super(taskID, taskName, taskDescription);
        this.subTasksIds = new ArrayList<>();
    }
    public Epic(int taskID, String taskName, String taskDescription, int IdEpic) {
        super(taskID, taskName, taskDescription);
        this.subTasksIds = new ArrayList<>();

    }

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        this.subTasksIds = new ArrayList<>();
    }



    public ArrayList<Integer> getsubTasksIds() {
        return subTasksIds;

    }
    public void setSubTasksIds(ArrayList<Integer>subTasksIds) {
        this.subTasksIds = subTasksIds;
    }

    public void clearSubtasks(ArrayList<Integer> subTasksIds){
        subTasksIds.clear();
    }

    public void setEndTime (LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void addSubtask(int id) {
        subTasksIds.add(id);

    }

/*
    @Override
    public String toString() {
        return "Epic{" +
                "endTime=" + endTime +
                ", subTasksIds=" + subTasksIds +
                '}';
    }
*/

/*
@Override
public String toString() {
    return "Тип задачи: " + getTypeTask() + "\n" +
            "Название: "  + getName() + "\n" +
            "Описание: " + getDescription() + "\n"  +
            "Статус: " + getTaskStatus() + "\n" +
            "Длительность: " + getDuration().toHours() + ":" + getDuration().toMinutesPart() + "\n"  +
            "Время начала выполнения: " + getStartTime().format(DATA_TIME_FORMAT) + "\n"  +
            "Дедлайн задачи: " +  endTime.format(DATA_TIME_FORMAT);

}
*/




}


