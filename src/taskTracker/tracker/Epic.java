package taskTracker.tracker;
import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList <Integer>subTasksIds;

    public Epic(String epicName, String epicDescription, String epicStatus) {
        super(epicName, epicDescription,epicStatus);
       subTasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getsubTasksIds() {
        return subTasksIds;

    }
    public void setSubEpicID(ArrayList<Integer>subTasksIds) {
        this.subTasksIds = getsubTasksIds();
    }

}
