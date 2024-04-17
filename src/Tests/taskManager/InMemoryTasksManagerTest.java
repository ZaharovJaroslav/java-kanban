
package taskManager;

import org.junit.jupiter.api.BeforeEach;
import taskTracker.tracker.service.InMemoryTaskManager;

public class InMemoryTasksManagerTest extends TaskManagerTest < InMemoryTaskManager >   {

    @BeforeEach
    void setUp () {
        super.taskManager = new InMemoryTaskManager();
    }
}
























/*
    Epic epic1 = new Epic("Epic1_Test", "Epic1_Description");
      SubTask subTask1 = new SubTask(1, "Subtask1_Test", "Subtask1_Description");
      SubTask subTask2 = new SubTask("Subtask2_Test", "Subtask2_Description");
      SubTask subTask3 = new SubTask("Subtask3_Test", "Subtask3_Description");
      Task task1 = new Task("Task1_Test", "Task1_Description");
      Task task2 = new Task("Task2_Test", "Task2_Description");


      Task task3 = new Task(TypeTask.TASK, "Task_Name3", " Task_Description",
              Duration.ofHours(1).plusMinutes(30),
              LocalDateTime.of(2024, Month.DECEMBER, 11, 5, 0));
      SubTask subTask4 = new SubTask(TypeTask.SUBTASK, "SubTask_Name4", " SubTask_Descriptio4",
              Duration.ofHours(1).plusMinutes(30),
              LocalDateTime.of(2024, Month.APRIL, 20, 2, 0),
              0);
      SubTask subTask5 = new SubTask(TypeTask.SUBTASK, "SubTask_Name2", " SubTask_Description2",
              Duration.ofHours(5).plusMinutes(30),
              LocalDateTime.of(2024, Month.APRIL, 20, 2, 0),
              0);

      Epic epic2 = new Epic("Epic2_Test", "Epic2_Description");


* */





