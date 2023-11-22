package taskTracker.tracker;
import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList <Integer> subEpicID;

    public Epic(String epicName, String epicDescription, String epicStatus) {
        super(epicName, epicDescription,epicStatus);
        subEpicID = new ArrayList<>();
    }

    public ArrayList<Integer> getsubEpicID() {
        return subEpicID;

    }
    public void setSubEpicID(ArrayList<Integer> subEpicID) {
        this.subEpicID = getsubEpicID();
    }

}
