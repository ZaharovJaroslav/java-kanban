package taskTracker.tracker;

import taskTracker.tracker.model.Epic;
import taskTracker.tracker.model.SubTask;
import taskTracker.tracker.model.Task;
import taskTracker.tracker.model.TaskStatus;
import taskTracker.tracker.service.*;

public class Main {


    public static void main(String[] args) {
        Managers managers = new Managers();
        TaskManager taskManage =  managers.getDefault();


        // Создаем Задачи:
        Task task1 = new Task("Задача 1", "тут описание 1");//0
        taskManage.addTask(task1);
        Task task2 = new Task("Задача 2", "тут описание 2");//1
        taskManage.addTask(task2);

        // Добавляем эпик и его подзадачи:
        Epic epic1 = new Epic("Епик 1,", "Описание епика 1");//2
        taskManage.addEpic(epic1);
        SubTask subTask11 = new SubTask("подзадача 1.1", "Описание подзадачи 1.1",epic1);//3
        taskManage.addSubTask(subTask11);
        SubTask subTask12 = new SubTask("подзадача 1.2", "Описание подзадачи 1.2", TaskStatus.NEW, epic1);//4
        taskManage.addSubTask(subTask12);
        SubTask subTask13 = new SubTask("подзадача 1.3", "Описание подзадачи 1.3",epic1);//5
        taskManage.addSubTask(subTask13);

        Epic epic2 = new Epic("Епик 2,", "Описание епика 2",TaskStatus.NEW);//6
        taskManage.addEpic(epic2);

        taskManage.getTaskbyId(1); // 0.1.2.3.4.5.6
        taskManage.getTaskbyId(0);
        taskManage.getSubTaskbyId(3);
        taskManage.getEpicbyId(6);
        taskManage.getSubTaskbyId(4);
        taskManage.getEpicbyId(2);
        taskManage.getSubTaskbyId(5);
        taskManage.getSubTaskbyId(3);
        taskManage.getSubTaskbyId(3);

        taskManage. deleteTaskById(1);
        taskManage.deleteEpicById(2);

        System.out.println(Managers.getDefaultHistory().getHistory());
    }
}
