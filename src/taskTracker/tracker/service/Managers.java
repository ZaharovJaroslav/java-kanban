package taskTracker.tracker.service;

import taskTracker.tracker.model.Task;

import java.util.List;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager( new InMemoryHistoryManager());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}