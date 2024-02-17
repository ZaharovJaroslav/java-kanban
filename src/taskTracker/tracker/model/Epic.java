package taskTracker.tracker.model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList <Integer>subTasksIds;

   public Epic(int taskID, TypeTask typeTask, String epicName, TaskStatus epicStatus, String epicDescription,int idEpic) {
        super( taskID,typeTask,epicName,epicStatus, epicDescription, idEpic);
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

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        this.subTasksIds = new ArrayList<>();
    }



    public ArrayList<Integer> getsubTasksIds() {
        return subTasksIds;

    }
    public void setSubEpicIds(ArrayList<Integer>subTasksIds) {
        this.subTasksIds = getsubTasksIds();
    }

    public void clearSubtasks(ArrayList<Integer> subTasksIds){
        subTasksIds.clear();

    }
    public void addSubtask(int id) {
        subTasksIds.add(id);

    }
}


