package taskTracker.tracker;

public class Task {
    private String taskName;
    private String taskDescription;
    private String taskStatus;

    public Task(String taskName, String taskDescription,  String taskStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskDescription(){
        return taskDescription;
    }
    public String getTaskStatus() {
        return taskStatus;
    }
     public void setTaskStatus(String taskStatus) {
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
