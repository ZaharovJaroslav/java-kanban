package taskTracker.tracker.model;

public class SubTask extends Task {
    private Epic epicID;

    public SubTask(String subTaskName, String subTaskDescription, TaskStatus subTaskStatus, Epic epicID) {
        super(subTaskName, subTaskDescription,subTaskStatus);
        this.epicID = epicID;
    }

    public SubTask(String taskName, String taskDescription, Epic epicID) {
        super(taskName, taskDescription);
        this.epicID = epicID;
    }

    public Epic getEpicID() {
        return epicID;
    }
    public  void setEpicID(Epic epicID){
        this.epicID = epicID;
    }
}
