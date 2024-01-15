package taskTracker.tracker.service;
import taskTracker.tracker.model.Epic;
import taskTracker.tracker.model.SubTask;
import taskTracker.tracker.model.Task;
import taskTracker.tracker.model.TaskStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class InMemoryTaskManager implements TaskManager {

    protected static int taskID;
    private Task task;
    private SubTask subTask;
    private Epic epic;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    private InMemoryHistoryManager historyManager;



    public InMemoryTaskManager(InMemoryHistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    private int generateId() {
        return taskID ++;
    }



    // РАБОТА С ПРОСТЫМИ ЗАДАЧАМИ

@Override
    public void addTask(Task task) { // добавить новую задачу в мапу
        int id = generateId();
        if(task.getTaskStatus()==null) {
            task.setTaskID(id);
        }
        task.setStatus(TaskStatus.NEW);
        tasks.put(id, task);
    }
@Override
    public ArrayList<Task> getTasks() { // получить все простые задачи
        ArrayList <Task> taskList = new ArrayList<>();
        for (Integer keyId : tasks.keySet()) {
            taskList.add(tasks.get(keyId));
        }
        return taskList;
    }
@Override
    public void deleteTask() { // очистить мапу с простыми задачами
        tasks.clear();

    }
@Override
    public Task getTaskbyId(int taskID) { // получить задачу по id
        if(tasks.containsKey(taskID)){
            task = tasks.get(taskID);
            historyManager.add(task);
        }
        return task;
    }
@Override
    public void updateTask(Task task) { // обновить задачу новой задачей
        int id = task.getTaskID();
        tasks.put(id, task);
    }
@Override
    public void deleteTaskById(int taskID) { // удалить задачу по id
        if (tasks.containsKey(taskID)) {
            tasks.remove(taskID);
            historyManager.remove(taskID);
        }
    }


    // РАБОТА С ЭПИКАМИ
@Override
    public void addEpic(Epic epic) { // добавить новый Эпик в мапу
        int id = generateId();
        epic.setTaskID(id);
        epic.setStatus(TaskStatus.NEW);
        epics.put(id, epic);
    }
@Override
    public ArrayList<Epic> getEpics() {// Получить все Эпики
        ArrayList<Epic> epicList = new ArrayList<>();
        for (Integer keyId :epics.keySet()){
            epicList.add(epics.get(keyId));
        }
        return epicList;
    }
@Override
    public void deleteEpics() { // очистить мапу c Эпиками, удалить все подзадачи
        epics.clear();
        subTasks.clear();
    }
@Override
    public Epic getEpicbyId (int taskID) { // вывести эпик по id
        if(epics.containsKey(taskID)){
            epic = epics.get(taskID);
            historyManager.add(epic);
        }
        return epic;
    }
@Override
    public ArrayList<SubTask> getsEpicSubtasks(Epic epic) {  // получить список подзадач определенного Эпика
        ArrayList<SubTask> epicSubtasks= new ArrayList<>();
        for (Integer i : epic.getsubTasksIds()) {
            epicSubtasks.add(subTasks.get(i));
        }
        return epicSubtasks;
    }
@Override
    public void updateEpic(Epic epic) { // обновить Эпик новым Эпиком
        int id = epic.getTaskID();
        Epic epicToUpdate = epics.get(id);
        epicToUpdate.setName(epic.getName());
        epicToUpdate.setDescription(epic.getDescription());
    }
@Override
    public boolean deleteEpicById(int taskID) { // удалить Эпик по id,а так же его подзадачи
        int subTaskId = 0;
        List<Integer> subTasks = new ArrayList<>();

        if (epics.containsKey(taskID)){
            Epic epic = epics.get(taskID);
            subTasks = epic.getsubTasksIds();
            for (int i = 0; i < subTasks.size(); i++) {
                subTaskId = (int) subTasks.get(i);
                historyManager.remove(subTaskId);
            }
        }
    historyManager.remove(taskID);
    epic.clearSubtasks(epic.getsubTasksIds());
    epics.remove(taskID);
    return true;
    }

    @Override
    public void computeEpicStatus(Epic epic) { // логика статуса Эпика
        boolean epicTaskStatusNew = true;
        boolean epicTaskStatusDone = true;

        for (Integer subEpicId : epic.getsubTasksIds()) {
            TaskStatus status = subTasks.get(subEpicId ).getTaskStatus();
            if (!(status == TaskStatus.NEW)) {
                epicTaskStatusNew = false;
            }
            if (!(status ==TaskStatus.DONE)) {
                epicTaskStatusDone = false;
            }
        }
        if (epicTaskStatusNew) { // если все подзадачи Эпика имеют статус NEW
            epic.setStatus(TaskStatus.NEW);
        } else if (epicTaskStatusDone) {
            epic.setStatus(TaskStatus.DONE); // если все подзадачи имеют статус DONE, то и эпик считается завершённым
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS); // во всех остальных случаях Эпик имеет статус в работе
        }
    }
@Override
    public void addSubTask(SubTask subTask) { // добавить новую подзадачу.
        int id = generateId();

        if (subTask.getTaskStatus() == null) {
            subTask.setStatus(TaskStatus.NEW);
            subTask.setTaskID(id);
        }
        subTasks.put(id, subTask);
        subTask.setTaskID(id);
        Epic epic = subTask.getEpicID();
        epic.addSubtask(id);
        computeEpicStatus(epic);
    }
@Override
    public ArrayList<SubTask> getSubTasks() {
        ArrayList<SubTask> subTasksList = new ArrayList<>();
        for (Integer keId: subTasks.keySet()) {
            subTasksList.add(subTasks.get(keId));
        }
        return subTasksList;
    }
@Override
    public void deleteSubTask() { // очистить мапу с подзадачами, удалить подазадачи из эпиков и обновить их статусы
        subTasks.clear();
        epic.getsubTasksIds().clear();
        computeEpicStatus(epic);
    }
@Override
    public SubTask getSubTaskbyId(int taskID) { // вывести подзадачу по id
        if(subTasks.containsKey(taskID)){
            subTask = subTasks.get(taskID);
            historyManager.add(subTask);
        }
        return subTask;

    }
@Override
    public void updateSubTask(SubTask subTask) { // обновляем поздачу новой подзадачей и обновить статус Епика
        int id = subTask.getTaskID();
        subTasks.put(id, subTask);
        Epic epic = subTask.getEpicID();
        computeEpicStatus(epic);
    }
@Override
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
            historyManager.remove(taskID);
        }
        return true;
    }
}
