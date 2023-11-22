import taskTracker.tracker.Epic;
import taskTracker.tracker.SubTask;
import taskTracker.tracker.Task;
import taskTracker.tracker.service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager  taskManage = new TaskManager();

        Task task1 = new Task("Задача 1", "тут описание 1","NEW");
        taskManage.addTask(task1);
        Task task2 = new Task("Задача 2", "тут описание 2","");
        taskManage.addTask(task2);
        taskManage.deleteTaskById(2);
        System.out.println(taskManage.getTask());

        Epic epic1 = new Epic("Епик 1,", "Описание епика 1", "");
        taskManage.addEpic(epic1);
        SubTask subTask11 = new SubTask("подзадача 1", "Описание подзадачи 1", "DONE", epic1);
        taskManage.addSubTask(subTask11);
        taskManage.StatusEpic(epic1);
        SubTask subTask22 = new SubTask("подзадача 2", "Описание подзадачи 2", "NEW", epic1);
        taskManage.addSubTask(subTask22);
        taskManage.StatusEpic(epic1);
        taskManage.deleteSubTaskById(4);

        Epic epic2 = new Epic("Епик 2,", "Описание епика 2", "");
        taskManage.addEpic(epic2);
        SubTask subTask21 = new SubTask("подзадача 21", "Описание подзадачи 21", "DONE", epic1);
        taskManage.addSubTask(subTask21);
        taskManage.StatusEpic(epic2);
        System.out.println(taskManage.getSubTask());
        System.out.println(taskManage.getEpic());
    }
}
