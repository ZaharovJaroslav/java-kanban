package taskTracker.tracker;

import taskTracker.tracker.model.Epic;
import taskTracker.tracker.model.SubTask;
import taskTracker.tracker.model.Task;
import taskTracker.tracker.service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager  taskManage = new TaskManager();

        // Создаем Задачи:
        Task task1 = new Task("Задача 11", "тут описание 1","NEW");
        taskManage.addTask(task1);
        Task task2 = new Task("Задача 12", "тут описание 2","");
        taskManage.addTask(task2);

        taskManage.getTaskbyId(1);
        taskManage.updateTask(new Task(1,"Новая задача", "Описание новой Задачи"));
        taskManage.deleteTaskById(0);
        System.out.println(taskManage.getTasks());


        // Добавляем эпик и его подзадачи:
        Epic epic1 = new Epic("Епик 1,", "Описание епика 1", "");
        taskManage.addEpic(epic1);
        SubTask subTask11 = new SubTask("подзадача 1", "Описание подзадачи 1", "DONE", epic1);
        taskManage.addSubTask(subTask11);
        SubTask subTask12 = new SubTask("подзадача 12", "Описание подзадачи 12", "NEW", epic1);
        taskManage.addSubTask(subTask12);

        Epic epic2 = new Epic("Епик 2,", "Описание епика 2", "");
        taskManage.addEpic(epic2);
        SubTask subTask21 = new SubTask("подзадача 21", "Описание подзадачи 21", "DONE", epic2);
        taskManage.addSubTask(subTask21);

        taskManage.getEpicbyId(2);
        taskManage.getsEpicSubtasks(epic1);
        taskManage.updateEpic(new Epic(2,"Новый Эпик", "Меняем Эпик на новый" ));
        taskManage.deleteEpicById(0);
        System.out.println(taskManage.getSubTasks());
        System.out.println(taskManage.getEpics());
    }
}
