package taskTracker.tracker.service;

import taskTracker.tracker.model.Task;

import java.util.ArrayList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
private static List<Task> viewedTasks = new ArrayList<>();
private static final int MAX_SIZE = 10;

    @Override
    public void add(Task task) {
        if(viewedTasks.size() < MAX_SIZE){
            viewedTasks.add(task);
        } else {
            viewedTasks.remove(0);
            viewedTasks.add(task);
        }

    }

    @Override
    public List<Task> getHistory() {
        return viewedTasks;
    }


}
