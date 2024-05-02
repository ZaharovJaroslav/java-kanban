import model.Epic;
import model.SubTask;
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

        SubTask subTask1 = new SubTask(TypeTask.SUBTASK, "SubTask_Name1", " SubTask_Descriptio4",
                Duration.ofHours(1).plusMinutes(30),
                LocalDateTime.of(2025, Month.APRIL, 20, 2, 0),
                0);
        taskManage.addSubTask(subTask1);
        System.out.println(taskManage.getsEpicSubtasks(epic1));
    }
}
