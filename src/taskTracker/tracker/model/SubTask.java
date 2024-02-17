package taskTracker.tracker.model;

public class SubTask extends Task {
    private Epic epicID;
    private int idEpic;


    public int getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

   public SubTask(int taskID, TypeTask typeTask, String subTaskName, TaskStatus subTaskStatus, String subTaskDescription, Epic epicID ) {
        super(taskID, typeTask, subTaskName,subTaskStatus, subTaskDescription );
        this.epicID = epicID;
      //  this.idEpic = epicID.getTaskID();
    }
   public SubTask(int taskID, TypeTask typeTask, String subTaskName, TaskStatus subTaskStatus, String subTaskDescription, int idEpic ) {
        super(taskID,typeTask, subTaskName,subTaskStatus, subTaskDescription, idEpic);
       this.epicID = getEpicID();


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
