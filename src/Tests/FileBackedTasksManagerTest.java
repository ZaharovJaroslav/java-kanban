import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskTracker.tracker.model.Epic;
import taskTracker.tracker.model.SubTask;
import taskTracker.tracker.model.Task;
import taskTracker.tracker.service.FileBackedTasksManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    File file;

    @BeforeEach
    void setUp() {
        this.file = new File("./resources/test.csv");
        super.taskManager = new FileBackedTasksManager(file);
    }

   @AfterEach
    void afterEach() {
        try {
            Files.delete(file.toPath());
        } catch (IOException exception) {
            exception.getStackTrace();
        }
    }

    @Test
    void test2_1ShouldUploadAFileForRecording() {
        long uploadedFileSize = file.length();
        assertEquals(0, uploadedFileSize);
    }

    @Test
    void test2_2ShouldWriteTasksToaFile() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        assertNotEquals(0, file.length());
    }

   @Test
    void test2_3ShouldReadTheTaskFile() {
        FileBackedTasksManager fileManager = FileBackedTasksManager.loadFromFile(new File("tasks.csv"));
        List<Task> tasks = fileManager.getTasks();
        List<Epic> epics = fileManager.getEpics();
        List<SubTask> subTasks = fileManager.getSubTasks();

        assertEquals(2, tasks.size());
        assertEquals(1, epics.size());
        assertEquals(2, subTasks.size());
    }
}






