package taskTracker.tracker.model;

public class SubTask extends Task {
    private Epic epicID;

    public SubTask(String subTaskName, String subTaskDescription, String subTaskStatus, Epic epicID) {
        super(subTaskName, subTaskDescription,subTaskStatus);
        this.epicID = epicID;
    }

    public Epic getEpicID() {
        return epicID;
    }
    public  void setEpicID(Epic epicID){
        this.epicID = epicID;
    }
}
