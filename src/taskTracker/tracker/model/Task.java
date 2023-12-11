package taskTracker.tracker.model;

public class Task {


    private int taskID;
    private String taskName;
    private String taskDescription;
    private  TaskStatus taskStatus;

    public Task(String taskName, String taskDescription,  TaskStatus taskStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
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

    public String getDescription(){
        return taskDescription;
    }
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }
     public void setStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;

     }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                '}';
    }
}
