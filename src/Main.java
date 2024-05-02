import model.Epic;
import model.Task;
import model.TypeTask;
import service.Managers;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        Managers managers = new Managers();
        TaskManager taskManage =  managers.getDefault();

        Epic epic1 = new Epic(TypeTask.EPIC, "Epic_Name", " Epic_Description");
        taskManage.addEpic(epic1);
        System.out.println(epic1.getEndTime());

        Task task1 = new Task(TypeTask.TASK, "Task_Name1", " Task_Description");
        taskManage.addTask(task1);
        System.out.println(task1.getEndTime());

    }
}
