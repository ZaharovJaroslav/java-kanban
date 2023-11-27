package taskTracker.tracker.service;
import taskTracker.tracker.Epic;
import taskTracker.tracker.SubTask;
import taskTracker.tracker.Task;
import java.util.ArrayList;
import java.util.HashMap;



public class TaskManager {
    protected static int taskID;
    private Task task;
    private SubTask subTask;
    private Epic epic;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();


    public int generateId() {
         return taskID ++;
    }



    // РАБОТА С ПРОСТЫМИ ЗАДАЧАМИ

    public void addTask(Task task) { // добавить новую задачу в мапу
        int id = generateId();
        if (task.getTaskStatus().isEmpty()) {
            task.setStatus("NEW");
        }
        tasks.put(id, task);
    }

    public ArrayList<Task> getTask() { // получить все простые задачи
        ArrayList <Task> taskList = new ArrayList<>();
        for (Integer keyId : tasks.keySet()) {
            taskList.add(tasks.get(keyId));
        }
        return taskList;
    }

    public void deleteTask() { // очистить мапу с простыми задачами
        tasks.clear();

    }

    public ArrayList<Task> getTaskbyId(int taskID) { // получить задачу по id
        ArrayList<Task> taskList = new ArrayList<>();

        for (Integer keyId : tasks.keySet()) {
            if (taskID == keyId) {
                taskList.add(tasks.get(keyId));
                return taskList;
            }
        }
        return null;
    }

    public void updateTask(Task task) { // обновить задачу новой задачей
        int id = generateId();
        tasks.put(id, task);
    }

    public boolean deleteTaskById(int taskID) { // удалить задачу по id
        for (Integer keyId : tasks.keySet()) {
            if (taskID == keyId) {
                tasks.remove(keyId);
                return true;
            }
        }
        return false;
    }


    // РАБОТА С ЭПИКАМИ

    public void addEpic(Epic epic) { // добавить новый Эпик в мапу
        int id = generateId();
        epic.setStatus("NEW");
        epics.put(id, epic);
    }

    public ArrayList<Epic> getEpic() {// Получить все Эпики
        ArrayList<Epic> epicList = new ArrayList<>();
        for (Integer keyId :epics.keySet()){
            epicList.add(epics.get(keyId));
        }
        return epicList;
    }

    public void deleteEpic() { // очистить мапу c Эпиками, удалить все подзадачи
        epics.clear();
        subTasks.clear();
    }

    public ArrayList<Task> getEpicbyId(int taskID) { // вывести эпик по id
        ArrayList<Task> epicList = new ArrayList<>();

        for (Integer keyId : epics.keySet()) {
            if (taskID == keyId) {
                epicList.add(epics.get(keyId));
                return epicList;
            }
        }
        return null;
    }

    public ArrayList<SubTask> getsTheEpikSubtask(Epic epic) {  // получить список подзадач определенного Эпика
        ArrayList<SubTask> epicSubtasks= new ArrayList<>();
        for (Integer i : epic.getsubTasksIds()) {
            epicSubtasks.add(subTasks.get(i));
        }
        return epicSubtasks;
    }

    public void updateEpic(Epic epic) { // обновить Эпик новым Эпиком
        int id = generateId();
        tasks.put(id, epic);
    }

   public boolean deleteEpicById(int taskID) { // удалить Эпик по id,а так же его подзадачи
        for (Integer keyId : epics.keySet()) {
            if (taskID == keyId) {
                Epic epic = epics.get(keyId);
                if (epic.getsubTasksIds() != null){
                    for (Integer subId: epic.getsubTasksIds()) {
                        subTasks.remove(subId);
                    }
                }
                epics.remove(keyId);
                return true;
            }
        }
        return false;
    }
    public void StatusEpic(Epic epic) { // логика статуса Эпика
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


    // РАБОТА С ПОДЗАДАЧАМИ


    public void addSubTask(SubTask subTask) { // добавить новую подзадачу
        int id = generateId();

        if (subTask.getTaskStatus().isEmpty()) {
            subTask.setStatus("NEW");
        }
        subTasks.put(id, subTask);
        Epic epic = subTask.getEpicID();
        epic.getsubTasksIds().add(id);
        StatusEpic(epic);

    }

    public ArrayList<SubTask> getSubTask() {
        ArrayList<SubTask> subTasksList = new ArrayList<>();
        for (Integer keId: subTasks.keySet()) {
            subTasksList.add(subTasks.get(keId));
        }
        return subTasksList;
    }

    public void deleteSubTask() { // очистить мапу с подзадачами, удалить подазадачи из эпиков и обновить их статусы
        subTasks.clear();
        for (Integer keyId : epics.keySet()) {
            Epic epic = epics.get(keyId);
            epic.getsubTasksIds().clear();
            StatusEpic(epic);
        }

    }

    public ArrayList<Task> getSubTaskbyId(int taskID) { // вывести подзадачу по id
        ArrayList<Task> SubTaskIdLust = new ArrayList<>();

        for (Integer keyId : subTasks.keySet()) {
            if (taskID == keyId) {
                SubTaskIdLust.add(subTasks.get(keyId));
                return SubTaskIdLust;
            }
        }
        return null;
    }

    public void updateSubTask(SubTask subTasks) { // обновляем поздачу новой подзадачей и обновить статус Епика
        int id = generateId();
        tasks.put(id, subTasks);
        Epic epic = subTasks.getEpicID();
        StatusEpic(epic);
    }

    public boolean deleteSubTaskById(int taskID) { // удалить подзадачу по id, удалить из Эпика + обновить статус
        for (Integer keyId : subTasks.keySet()) {
           int key = keyId;
            if (taskID == key) {
                subTasks.remove(keyId);
                for (Integer keyEpic : epics.keySet()){
                    Epic epic = epics.get(keyEpic);
                    epic.getsubTasksIds().clear();
                }
                return true;
            }
        }
        return false;
    }
}