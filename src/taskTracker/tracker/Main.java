package taskTracker.tracker;

import taskTracker.tracker.model.SubTask;
import taskTracker.tracker.model.Task;
import taskTracker.tracker.model.TypeTask;
import taskTracker.tracker.service.Managers;
import taskTracker.tracker.service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class Main {


    public static void main(String[] args) {




        Managers managers = new Managers();
        TaskManager taskManage =  managers.getDefault();






/*
        Epic epic1 = new Epic(TypeTask.EPIC,
                "Epic_Name",
                " Epic_Description");
        taskManage.addEpic(epic1);
*/



        Task task1 = new Task(TypeTask.TASK, "Task_Name1", " Task_Description",
                Duration.ofHours(1).plusMinutes(30),
                LocalDateTime.of(2024, Month.APRIL,25,3,0));
        taskManage.addTask(task1);
        System.out.println(taskManage.getTaskbyId(1));
        System.out.println();
        System.out.println();



        Task task2 = new Task(TypeTask.TASK, "Task_Name2", " Task_Description2",
                Duration.ofHours(1).plusMinutes(30),
                LocalDateTime.of(2024, Month.APRIL,25,3,0));
        taskManage.addTask(task2);











        SubTask subTask1 = new SubTask(TypeTask.SUBTASK,
                "SubTask_Name1",
                " SubTask_Description",
                Duration.ofHours(1).plusMinutes(30),
                LocalDateTime.of(2024, Month.APRIL,20,2,0),
                0);
        taskManage.addSubTask(subTask1);


        SubTask subTask2 = new SubTask(TypeTask.SUBTASK,
                "SubTask_Name2",
                " SubTask_Description2",
                Duration.ofHours(1).plusMinutes(30),
                LocalDateTime.of(2024, Month.APRIL,20,2,0),
                0);
        taskManage.addSubTask(subTask2);

        SubTask subTask3= new SubTask(TypeTask.SUBTASK,
                "SubTask_Name3",
                " SubTask_Description3",
                Duration.ofHours(1).plusMinutes(30),
                LocalDateTime.of(2024, Month.APRIL,25,5,0),
                0);
        taskManage.addSubTask(subTask3);

       //System.out.println(epic1);
        System.out.println(taskManage.getPrioritizedTasks());
        System.out.println();
      //  System.out.println(taskManage.getsEpicSubtasks(epic1));




/*        Task task1 = new Task( TypeTask.TASK,
                "Task_Name",
                " Task_Description",
                Duration.ofHours(1).plusMinutes(20),
                LocalDateTime.of(2024, Month.APRIL,6,5,0));

        taskManage.addTask(task1);
        System.out.println(task1);*/



       /* // Создаем Задачи:
        Task task1 = new Task("Задача 1", "тут описание 1");//0
        taskManage.addTask(task1);



        Task task2 = new Task("Задача 2", "тут описание 2");//1
        taskManage.updateTask(task2);
        taskManage.addTask(task2);

        // Добавляем эпик и его подзадачи:
        Epic epic1 = new Epic("Епик 1,", "Описание епика 1");//2
        taskManage.addEpic(epic1);
        ArrayList <Integer> list = new ArrayList<>();
        list.add(epic1.getTaskID());
        epic1.setSubTasksIds(list);
        taskManage.getsEpicSubtasks(epic1);
        SubTask subTask11 = new SubTask("подзадача 1.1", "Описание подзадачи 1.1",2);//3
        taskManage.addSubTask(subTask11);
        SubTask subTask12 = new SubTask("подзадача 1.2", "Описание подзадачи 1.2", 2);//4
        taskManage.addSubTask(subTask12);
        SubTask subTask13 = new SubTask("подзадача 1.3", "Описание подзадачи 1.3",2);//5
        taskManage.addSubTask(subTask13);

        Epic epic2 = new Epic( "Епик 2", "Описание епика 2");//6
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

        System.out.println("История просмотренных задач" + "\n" + taskManage.getHistori());*/
    }
}
