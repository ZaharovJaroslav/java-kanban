package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public interface TaskManager {

    DateTimeFormatter DATA_TIME_FORMAT = DateTimeFormatter.ofPattern("MM.dd.yy HH:mm");
    int getID(Task task);

    ArrayList<Task> getPrioritizedTasks();

    // РАБОТА С ПРОСТЫМИ ЗАДАЧАМИ

    void addTask(Task task); // добавить новую задачу в мапу

    ArrayList<Task> getTasks(); // получить все простые задачи

    void deleteTask();  // очистить мапу с простыми задачами

    Task getTaskbyId(int taskID); // получить задачу по id

     void updateTask(Task task, Task newTask); // обновить задачу новой задачей

    void deleteTaskById(int taskID);// удалить задачу по id

    // РАБОТА С ЭПИКАМИ

    void addEpic(Epic epic);// добавить новый Эпик в мапу

    ArrayList<Epic> getEpics();// Получить все Эпики

    void deleteEpics();// очистить мапу c Эпиками, удалить все подзадачи

    Epic getEpicbyId(int taskID);// вывести эпик по id

    ArrayList<SubTask> getsEpicSubtasks(Epic epic);  // получить список подзадач определенного Эпика

    void updateEpic(Epic epic, Epic newEpic); // обновить Эпик новым Эпиком

    boolean deleteEpicById(int taskID); // удалить Эпик по id,а так же его подзадачи

    void computeEpicStatus(Epic epic);

    void computeEpicDataTime(Epic epic, SubTask subTask);

//ПОДЗАДАЧИ

    void addSubTask(SubTask subTask);// добавить новую подзадачу.

    ArrayList<SubTask> getSubTasks();

    void deleteSubTask(); // очистить мапу с подзадачами, удалить подазадачи из эпиков и обновить их статусы

    SubTask getSubTaskbyId(int taskID); // вывести подзадачу по id

    void updateSubTask(SubTask subTask, SubTask newSubtask);// обновляем поздачу новой подзадачей и обновить статус Епика

    boolean deleteSubTaskById(int taskID); // удалить подзадачу по id, удалить из Эпика + обновить статус

     List<Task> getHistori();

     List<Task> getOldVersionHistori();

}