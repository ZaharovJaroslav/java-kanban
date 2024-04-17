import org.junit.jupiter.api.BeforeEach;
import taskTracker.tracker.service.InMemoryTaskManager;

public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    @BeforeEach
    void setUp () {
        super.taskManager = new InMemoryTaskManager();
    }
}