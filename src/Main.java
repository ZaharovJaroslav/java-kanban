import model.Epic;
import model.SubTask;
import model.Task;
import model.TypeTask;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class Main {

    public static void main(String[] args) {
        Managers managers = new Managers();
        TaskManager taskManage =  managers.getDefault();

        Epic epic1 = new Epic(TypeTask.EPIC, "Epic_Name", " Epic_Description");
        taskManage.addEpic(epic1);

        Task task1 = new Task(TypeTask.TASK, "Task_Name1", " Task_Description",
                Duration.ofHours(1).plusMinutes(30),
                LocalDateTime.of(2024, Month.APRIL,25,3,0));
        taskManage.addTask(task1);

        Task task2 = new Task(TypeTask.TASK, "Task_Name2", " Task_Description2",
                Duration.ofHours(1).plusMinutes(30),
                LocalDateTime.of(2024, Month.APRIL,25,3,0));
        taskManage.addTask(task2);

        SubTask subTask1 = new SubTask(TypeTask.SUBTASK, "SubTask_Name1", " SubTask_Description",
                Duration.ofHours(1).plusMinutes(30),
                LocalDateTime.of(2024, Month.APRIL,20,2,0),
                0);
        taskManage.addSubTask(subTask1);

        SubTask subTask2 = new SubTask(TypeTask.SUBTASK, "SubTask_Name2", " SubTask_Description2",
                Duration.ofHours(1).plusMinutes(30),
                LocalDateTime.of(2024, Month.APRIL,20,2,0),
                0);
        taskManage.addSubTask(subTask2);

        SubTask subTask3= new SubTask(TypeTask.SUBTASK, "SubTask_Name3", " SubTask_Description3",
                Duration.ofHours(1).plusMinutes(30),
                LocalDateTime.of(2024, Month.APRIL,25,5,0),
                0);
        taskManage.addSubTask(subTask3);
    }
}
