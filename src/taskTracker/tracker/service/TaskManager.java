package taskTracker.tracker.service;

import taskTracker.tracker.model.Epic;
import taskTracker.tracker.model.SubTask;
import taskTracker.tracker.model.Task;
import java.util.List;

import java.util.ArrayList;



public interface TaskManager {
   /* protected static int taskID;
    private Task task;
    private SubTask subTask;
    private Epic epic;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();*/


    //private int generateId();


    // РАБОТА С ПРОСТЫМИ ЗАДАЧАМИ

    public void addTask(Task task); // добавить новую задачу в мапу


    public ArrayList<Task> getTasks(); // получить все простые задачи


    public void deleteTask();  // очистить мапу с простыми задачами


    public Task getTaskbyId(int taskID); // получить задачу по id


    public void updateTask(Task task); // обновить задачу новой задачей


    public void deleteTaskById(int taskID);// удалить задачу по id


    // РАБОТА С ЭПИКАМИ

    public void addEpic(Epic epic);// добавить новый Эпик в мапу

    public ArrayList<Epic> getEpics();// Получить все Эпики


    public void deleteEpics();// очистить мапу c Эпиками, удалить все подзадачи


    public Epic getEpicbyId(int taskID);// вывести эпик по id


    public ArrayList<SubTask> getsEpicSubtasks(Epic epic);  // получить список подзадач определенного Эпика


    public void updateEpic(Epic epic); // обновить Эпик новым Эпиком


    public boolean deleteEpicById(int taskID); // удалить Эпик по id,а так же его подзадачи

    public void computeEpicStatus(Epic epic);

//ПОДЗАДАЧИ

    public void addSubTask(SubTask subTask);// добавить новую подзадачу.


    public ArrayList<SubTask> getSubTasks();


    public void deleteSubTask(); // очистить мапу с подзадачами, удалить подазадачи из эпиков и обновить их статусы

    public SubTask getSubTaskbyId(int taskID); // вывести подзадачу по id

    public void updateSubTask(SubTask subTask);// обновляем поздачу новой подзадачей и обновить статус Епика


    public boolean deleteSubTaskById(int taskID); // удалить подзадачу по id, удалить из Эпика + обновить статус

     public List<Task> getHistori();

}