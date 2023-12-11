package taskTracker.tracker.service;

import taskTracker.tracker.model.Task;

import java.util.List;
//интерфейс для управления историей просмотров
public interface HistoryManager {

    public void add(Task task);
    public List<Task> getHistory();


}
