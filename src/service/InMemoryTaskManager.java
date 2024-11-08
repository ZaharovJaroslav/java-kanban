package service;

import exceptions.CollisionTaskException;
import model.*;

import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager {
    protected int taskID;
    protected Task task;
    protected SubTask subTask;
    protected Epic epic;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();


public boolean checkForTimeIntersections(Task newTask) {
    boolean result = false;

    for (Task task : getPrioritizedTasks()) {
        if (task.getStartTime().isBefore(newTask.getStartTime()) && task.getEndTime().isBefore(newTask.getStartTime())) {
            continue;
        } else if (newTask.getStartTime().isBefore(task.getStartTime()) && newTask.getEndTime().isBefore(task.getStartTime())) {
          result = true;
        } else
            throw new CollisionTaskException("Время выполнения задачи пересекается со временем уже существующей " +
                    "задачи. Выберите другую дату.");
    }
            return result;
}

    @Override
    public ArrayList<Task> getPrioritizedTasks() {
        Set<Task> sortedList = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        sortedList.addAll(getTasks());
        sortedList.addAll(getSubTasks());
        return new ArrayList<>(sortedList);
}

    @Override
    public int getID(Task task) {
       return task.getTaskID();
    }

    protected int generateId() {
        return taskID++;
    }
    // РАБОТА С ПРОСТЫМИ ЗАДАЧАМИ

    @Override
    public void addTask(Task task) { // добавить новую задачу в мапу
        int id = generateId();
        if (task.getTaskStatus() == null) {
            task.setTaskID(id);
        }
        task.setStatus(TaskStatus.NEW);
        task.setTypeTask(TypeTask.TASK);

    Predicate<Task> checkTime = this::checkForTimeIntersections;
    if (!checkTime.test(task)) {
        tasks.put(id, task);
    }
    }

    @Override
    public ArrayList<Task> getTasks() { // получить все простые задачи
        ArrayList<Task> taskList = new ArrayList<>();
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
        if (tasks.containsKey(taskID)) {
            task = tasks.get(taskID);
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public void updateTask(Task task, Task newTask) { // обновить задачу новой задачей
        int id = task.getTaskID();
        newTask.setTaskID(id);
        deleteTaskById(id);
        Predicate<Task> checkTime = this::checkForTimeIntersections;
        tasks.put(id, newTask);
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
    Predicate<Task> checkTime = this::checkForTimeIntersections;
    if (!checkTime.test(epic)) {
        epics.put(id, epic);
    }
    }

    @Override
    public ArrayList<Epic> getEpics() { // Получить все Эпики
        ArrayList<Epic> epicList = new ArrayList<>();
        for (Integer keyId :epics.keySet()) {
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
    public Epic getEpicbyId(int taskID) { // вывести эпик по id
        if (epics.containsKey(taskID)) {
            epic = epics.get(taskID);
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public ArrayList<SubTask> getsEpicSubtasks(Epic epic) {  // получить список подзадач определенного Эпика
     List<SubTask> epicSubtasks = epic.getSubTasksIds()
             .stream()
             .map(id -> subTasks.get(id))
             .collect(Collectors.toList());
     return new ArrayList<>(epicSubtasks);



    }

    @Override
    public void updateEpic(Epic epic, Epic newEpic) { // обновить Эпик новым Эпиком
        int id = epic.getTaskID();
        newEpic.setTaskID(id);
        newEpic.setTypeTask(TypeTask.EPIC);
        deleteEpicById(id);
        epics.put(id, newEpic);
    }

    @Override
    public boolean deleteEpicById(int taskID) { // удалить Эпик по id,а так же его подзадачи
        int subTaskId = 0;
        Epic epic = null;
        List<Integer> subTasks = new ArrayList<>();


        if (epics.containsKey(taskID)) {
            epic = epics.get(taskID);
            subTasks = epic.getSubTasksIds();
            for (int i = 0; i < subTasks.size(); i++) {
                subTaskId = (int) subTasks.get(i);
                historyManager.remove(subTaskId);
            }
        }
    historyManager.remove(taskID);
    epic.clearSubtasks(epic.getSubTasksIds());
    epics.remove(taskID);
    return true;
    }

    @Override
    public void computeEpicDataTime(Epic epic, SubTask subTask) {
    long durationEpic = 0;

        if (epic.getDuration() == null) {
            epic.setDuration(subTask.getDuration());
            epic.setStartTime(subTask.getStartTime());
            epic.setEndTime(subTask.getEndTime());
        } else {
            List<SubTask> startTimeIdSubtasks =  epic.getSubTasksIds()
                    .stream()
                    .filter(id -> subTasks.containsKey(id))
                    .map(id -> getSubTaskbyId(id))
                    .sorted(Comparator.comparing(SubTask::getStartTime))
                    .collect(Collectors.toList());

            for (SubTask subtask : startTimeIdSubtasks) {
                if (subtask.getDuration() != null) {
                    durationEpic += subtask.getDuration().toMinutes();
                }
            }
            epic.setDuration(Duration.ofMinutes(durationEpic));
            epic.setStartTime(startTimeIdSubtasks.get(0).getStartTime());

            List<SubTask> endTimeIdSubtasks = startTimeIdSubtasks.stream()
                    .sorted(Comparator.comparing(SubTask::getEndTime))
                    .collect(Collectors.toList());

            for (int i = 0; i < endTimeIdSubtasks.size(); i++) {
                if (i == endTimeIdSubtasks.size() - 1) {
                    epic.setEndTime(endTimeIdSubtasks.get(i).getEndTime());
                }
            }
        }
    }

    @Override
    public void computeEpicStatus(Epic epic) { // логика статуса Эпика
        boolean epicTaskStatusNew = true;
        boolean epicTaskStatusDone = true;

        for (Integer subEpicId : epic.getSubTasksIds()) {
          if (subTasks.containsKey(subEpicId)) {
            SubTask subTask = subTasks.get(subEpicId);
            TaskStatus status = subTask.getTaskStatus();
            if (!(status == TaskStatus.NEW)) {
                epicTaskStatusNew = false;
            }
            if (!(status == TaskStatus.DONE)) {
                epicTaskStatusDone = false;
            }
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
    Predicate<Task> checkTime = this::checkForTimeIntersections;
    if (!checkTime.test(subTask)) {
        subTasks.put(id, subTask);
    }
    subTask.setTaskID(id);
    Epic epic =  epics.get(subTask.getIdEpic());
    epic.addSubtask(id);
    subTask.setIdEpic(epic.getTaskID());
    if (subTask.getDuration() != null) {
        computeEpicDataTime(epic,subTask);
    }
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
        for (Epic epic : epics.values()) {
            epic.getSubTasksIds().clear();
            computeEpicStatus(epic);
        }
    }

    @Override
    public SubTask getSubTaskbyId(int taskID) { // вывести подзадачу по id
        if (subTasks.containsKey(taskID)) {
            subTask = subTasks.get(taskID);
            historyManager.add(subTask);
        }
        return subTask;
    }

    @Override
    public void updateSubTask(SubTask subTask, SubTask newSubTask) { // обновляем поздачу новой подзадачей и обновить статус Епика
        int id = subTask.getTaskID();
        int epicId = subTask.getIdEpic();

        newSubTask.setTaskID(id);
        newSubTask.setIdEpic(epicId);
        newSubTask.setStatus(TaskStatus.NEW);
        deleteSubTaskById(id);
        Predicate<SubTask> checkTime = this::checkForTimeIntersections;
        subTasks.put(id, newSubTask);
        Epic epic =  epics.get(newSubTask.getIdEpic());
        epic.addSubtask(id);
        computeEpicStatus(epic);
    }

    @Override
    public boolean deleteSubTaskById(int taskID) { // удалить подзадачу по id, удалить из Эпика + обновить статус
        if (subTasks.containsKey(taskID)) {
            SubTask subTask = subTasks.get(taskID);
            Epic epic =  epics.get(subTask.getIdEpic());
            for (Integer i : epic.getSubTasksIds()) {
                if (i == taskID) {
                    epic.getSubTasksIds().remove(i);
                    break;
                }
            }
            subTasks.remove(taskID);
            computeEpicStatus(epic);
            historyManager.remove(taskID);
        }
        return true;
    }

    @Override
   public List<Task> getHistori() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getOldVersionHistori() {
        return historyManager.getOldVersionHistori();
    }



}
