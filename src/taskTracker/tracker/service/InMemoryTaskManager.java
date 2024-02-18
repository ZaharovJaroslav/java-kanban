package taskTracker.tracker.service;
import taskTracker.tracker.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryTaskManager implements TaskManager {

    protected  int taskID;
    protected Task task;
    protected SubTask subTask;
    protected Epic epic;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();



/*    public InMemoryTaskManager(InMemoryHistoryManager historyManager) {
        this.historyManager = historyManager;
    }*/

    protected int generateId() {
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
        task.setTypeTask(TypeTask.TASK);

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
        epic.setTypeTask(TypeTask.EPIC);
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
        subTask.setTypeTask(TypeTask.SUBTASK);
        subTask.setTaskID(id);
    }
    subTasks.put(id, subTask);
    subTask.setTaskID(id);
    Epic epic =  epics.get(subTask.getIdEpic());
    epic.addSubtask(id);
    subTask.setIdEpic(epic.getTaskID());
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
        Epic epic =  epics.get(subTask.getIdEpic());
        computeEpicStatus(epic);
    }
@Override
    public boolean deleteSubTaskById(int taskID) { // удалить подзадачу по id, удалить из Эпика + обновить статус
        if (subTasks.containsKey(taskID)) {
            SubTask subTask = subTasks.get(taskID);
            Epic epic =  epics.get(subTask.getIdEpic());
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
    @Override
   public List<Task> getHistori(){
        return historyManager.getHistory();
    }
}
