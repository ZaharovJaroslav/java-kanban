package taskTracker.tracker.service;

import taskTracker.tracker.model.Task;

import java.io.File;
import java.util.List;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileBackedTasks(){
        return new FileBackedTasksManager(new File("tasks.csv"));
    }
}