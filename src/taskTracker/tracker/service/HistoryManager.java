package taskTracker.tracker.service;
import taskTracker.tracker.model.Task;
import java.util.List;
//интерфейс для управления историей просмотров
public interface HistoryManager {

    public void linkLast(Task task);
    public void remove (int id);
    public List<Task> getHistory();


}
