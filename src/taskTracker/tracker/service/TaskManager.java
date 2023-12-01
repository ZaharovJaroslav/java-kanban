package taskTracker.tracker.service;
import taskTracker.tracker.model.Epic;
import taskTracker.tracker.model.SubTask;
import taskTracker.tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

// СПАСИБО=)

public class TaskManager {
    protected static int taskID;
    private Task task;
    private SubTask subTask;
    private Epic epic;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();


    private int generateId() {


         return taskID ++;
    }



    // РАБОТА С ПРОСТЫМИ ЗАДАЧАМИ

    public void addTask(Task task) { // добавить новую задачу в мапу

        int id = generateId();
        task.setTaskID(id);
        if (task.getTaskStatus().isEmpty()) {
            task.setStatus("NEW");
        }
        tasks.put(id, task);
    }

    public ArrayList<Task> getTasks() { // получить все простые задачи
        ArrayList <Task> taskList = new ArrayList<>();
        for (Integer keyId : tasks.keySet()) {
            taskList.add(tasks.get(keyId));
        }
        return taskList;
    }

    public void deleteTask() { // очистить мапу с простыми задачами
        tasks.clear();

    }

    public Task getTaskbyId(int taskID) { // получить задачу по id
        if(tasks.containsKey(taskID)){
            task = tasks.get(taskID);
        }
        return task;
    }

    public void updateTask(Task task) { // обновить задачу новой задачей
        int id = task.getTaskID();
        tasks.put(id, task);
    }

    public void deleteTaskById(int taskID) { // удалить задачу по id
            if (tasks.containsKey(taskID)) {
                tasks.remove(taskID);
            }
    }


    // РАБОТА С ЭПИКАМИ

    public void addEpic(Epic epic) { // добавить новый Эпик в мапу
        int id = generateId();
        epic.setTaskID(id);
        epic.setStatus("NEW");
        epics.put(id, epic);
    }

    public ArrayList<Epic> getEpics() {// Получить все Эпики
        ArrayList<Epic> epicList = new ArrayList<>();
        for (Integer keyId :epics.keySet()){
            epicList.add(epics.get(keyId));
        }
        return epicList;
    }

    public void deleteEpics() { // очистить мапу c Эпиками, удалить все подзадачи
        epics.clear();
        subTasks.clear();
    }

    public Epic getEpicbyId (int taskID) { // вывести эпик по id
        if(epics.containsKey(taskID)){
            epic = epics.get(taskID);
        }
        return epic;
    }

    public ArrayList<SubTask> getsEpicSubtasks(Epic epic) {  // получить список подзадач определенного Эпика
        ArrayList<SubTask> epicSubtasks= new ArrayList<>();
        for (Integer i : epic.getsubTasksIds()) {
            epicSubtasks.add(subTasks.get(i));
        }
        return epicSubtasks;
    }

    public void updateEpic(Epic epic) { // обновить Эпик новым Эпиком
        int id = epic.getTaskID();
        Epic epicToUpdate = epics.get(id);
        epicToUpdate.setName(epic.getName());
        epicToUpdate.setDescription(epic.getDescription());
    }

   public boolean deleteEpicById(int taskID) { // удалить Эпик по id,а так же его подзадачи
        if (epics.containsKey(taskID)){
            Epic epic = epics.get(taskID);
            epic.clearSubtasks(epic.getsubTasksIds());
        }
                epics.remove(taskID);
                return true;
    }
    public void computeEpicStatus(Epic epic) { // логика статуса Эпика
        boolean epicTaskStatusNew = true;
        boolean epicTaskStatusDone = true;

        for (Integer subEpicId : epic.getsubTasksIds()) {
            String status = subTasks.get(subEpicId ).getTaskStatus();
            if (!status.equals("NEW")) {
                epicTaskStatusNew = false;
            }
            if (!status.equals("DONE")) {
                epicTaskStatusDone = false;
            }
        }
        if (epicTaskStatusNew) { // если все подзадачи Эпика имеют статус NEW
            epic.setStatus("NEW");
        } else if (epicTaskStatusDone) {
            epic.setStatus("DONE"); // если все подзадачи имеют статус DONE, то и эпик считается завершённым
        } else {
            epic.setStatus("IN_PROGRESS"); // во всех остальных случаях Эпик имеет статус в работе
        }
    }

    public void addSubTask(SubTask subTask) { // добавить новую подзадачу.
        int id = generateId();

        if (subTask.getTaskStatus().isEmpty()) {
            subTask.setStatus("NEW");
        }
        subTasks.put(id, subTask);
        Epic epic = subTask.getEpicID();
        epic.addSubtask(id);
        computeEpicStatus(epic);
    }

    public ArrayList<SubTask> getSubTasks() {
        ArrayList<SubTask> subTasksList = new ArrayList<>();
        for (Integer keId: subTasks.keySet()) {
            subTasksList.add(subTasks.get(keId));
        }
        return subTasksList;
    }

    public void deleteSubTask() { // очистить мапу с подзадачами, удалить подазадачи из эпиков и обновить их статусы
        subTasks.clear();
        epic.getsubTasksIds().clear();
        computeEpicStatus(epic);
    }

    public SubTask getSubTaskbyId(int taskID) { // вывести подзадачу по id
        if(subTasks.containsKey(taskID)){
            subTask = subTasks.get(taskID);
        }
        return subTask;

    }

    public void updateSubTask(SubTask subTask) { // обновляем поздачу новой подзадачей и обновить статус Епика
        int id = subTask.getTaskID();
        subTasks.put(id, subTask);
        Epic epic = subTask.getEpicID();
        computeEpicStatus(epic);
    }

    public boolean deleteSubTaskById(int taskID) { // удалить подзадачу по id, удалить из Эпика + обновить статус
       if (subTasks.containsKey(taskID)) {
           SubTask subTask = subTasks.get(taskID);
           Epic epic = subTask.getEpicID();
      for (Integer i : epic.getsubTasksIds()) {
          if (i == taskID){
              epic.getsubTasksIds().remove(i);
          }
      }
           subTasks.remove(taskID);
       }
       return true;
    }
}
