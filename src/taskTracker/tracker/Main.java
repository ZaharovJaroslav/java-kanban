package taskTracker.tracker;

import taskTracker.tracker.model.Epic;
import taskTracker.tracker.model.SubTask;
import taskTracker.tracker.model.Task;
import taskTracker.tracker.model.TaskStatus;
import taskTracker.tracker.service.*;

public class Main {
/*
  managers.getDefaultHistory(); - Не могу понять, почему мы не можем таким образом получить список.
  System.out.println(  managers.getDefaultHistory()); но можем получить в виде строки.
  Заранее спасибо)

 */

    public static void main(String[] args) {
        Managers managers = new Managers();
        TaskManager taskManage =  managers.getDefault();


        // Создаем Задачи:
        Task task1 = new Task("Задача 11", "тут описание 1");
        taskManage.addTask(task1);
        Task task2 = new Task("Задача 12", "тут описание 2");
        taskManage.addTask(task2);
        taskManage.getTaskbyId(0);
        taskManage.getTaskbyId(1);


        // Добавляем эпик и его подзадачи:
        Epic epic1 = new Epic("Епик 1,", "Описание епика 1");
        taskManage.addEpic(epic1);
        SubTask subTask11 = new SubTask("подзадача 1", "Описание подзадачи 1",epic1);
        taskManage.addSubTask(subTask11);
        SubTask subTask12 = new SubTask("подзадача 12", "Описание подзадачи 12", TaskStatus.NEW, epic1);
        taskManage.addSubTask(subTask12);

        taskManage.getEpicbyId(2);
        taskManage.getEpicbyId(2);
        taskManage.getEpicbyId(2);
        taskManage.getEpicbyId(2);
        taskManage.getSubTaskbyId(3);
        taskManage.getSubTaskbyId(4);
        taskManage.getTaskbyId(1);
        taskManage.getTaskbyId(1);
        taskManage.getEpicbyId(2);

        System.out.println(  managers.getDefaultHistory());
         managers.getDefaultHistory();



        Epic epic2 = new Epic("Епик 2,", "Описание епика 2",TaskStatus.NEW);
        taskManage.addEpic(epic2);
        SubTask subTask21 = new SubTask("подзадача 21", "Описание подзадачи 21", TaskStatus.DONE, epic2);
        taskManage.addSubTask(subTask21);

        taskManage.getEpicbyId(7);
        taskManage.getsEpicSubtasks(epic1);
        taskManage.updateEpic(new Epic(2,"Новый Эпик", "Меняем Эпик на новый" ));
        taskManage.deleteEpicById(2);
        System.out.println(taskManage.getSubTasks());
        System.out.println(taskManage.getEpics());
    }
}
