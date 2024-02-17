package taskTracker.tracker.model;

public class Task {


    private int taskID;
    private String taskName;
    private String taskDescription;
    private  TaskStatus taskStatus;
    private TypeTask typeTask;
    private  int idEpic;



    public Task (int taskID, TypeTask typeTask, String taskName, TaskStatus taskStatus,String taskDescription, int idEpic ) {
        this.taskID = taskID;
        this.typeTask = typeTask;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.idEpic = idEpic;
    }
    public Task (int taskID, TypeTask typeTask, String taskName, TaskStatus taskStatus , String taskDescription  ) {
        this.taskID = taskID;
        this.typeTask = typeTask;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;

    }




    public int getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

    public Task(String value) {

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

    public void setTypeTask(TypeTask typeTask){
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

    public TypeTask getTypeTask(){
        return typeTask;
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
