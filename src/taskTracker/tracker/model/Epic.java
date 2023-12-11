package taskTracker.tracker.model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList <Integer>subTasksIds;

    public Epic(String epicName, String epicDescription, TaskStatus epicStatus) {
        super(epicName, epicDescription,epicStatus);
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


