package taskTracker.tracker.service;
import taskTracker.tracker.Epic;
import taskTracker.tracker.SubTask;
import taskTracker.tracker.Task;
import java.util.ArrayList;
import java.util.HashMap;



public class TaskManager {
    protected static int taskID;
    private Task tasks;
    private SubTask subTasks;
    private Epic epics;
    HashMap<Integer, Task> task = new HashMap<>();
    HashMap<Integer, SubTask> subTask = new HashMap<>();
    HashMap<Integer, Epic> epic = new HashMap<>();


    // РАБОТА С ПРОСТЫМИ ЗАДАЧАМИ

    public void addTask(Task tasks) { // добавить новую задачу в мапу
        taskID++;
        if (tasks.getTaskStatus().isEmpty()) {
            tasks.setTaskStatus("NEW");
        }
        task.put(taskID, tasks);
    }

    public HashMap<Integer, Task> getTask() { // получит все простые задачи
        return task;
    }

    public void deleteTask() { // очистить мапу с простыми задачами
        task.clear();

    }

    public ArrayList<Task> getTaskbyId(int taskID) { // получить задачу по id
        ArrayList<Task> getTaskId = new ArrayList<>();

        for (Integer keyId : task.keySet()) {
            if (taskID == keyId) {
                getTaskId.add(task.get(keyId));
            }
        }
        return getTaskId;
    }

    public void updateTask(Task tasks) { // обновить задачу новой задачей
        task.put(taskID++, tasks);
    }

    public void deleteTaskById(int taskID) { // удалить задачу по id
        for (Integer keyId : task.keySet()) {
            if (taskID == keyId) {
                task.remove(keyId);
                break;
            }

        }
    }


    // РАБОТА С ЭПИКАМИ

    public void addEpic(Epic epics) { // добавить новый Эпик в мапу
        taskID++;
        if (epics.getTaskStatus().isEmpty()) {
            epics.setTaskStatus("NEW");
        }
        epic.put(taskID, epics);
    }

    public HashMap<Integer, Epic> getEpic() { // Получить все Эпики
        return epic;
    }

    public void deleteEpic() { // очистить мапу c Эпиками
        epic.clear();
    }

    public ArrayList<Task> getEpicbyId(int taskID) { // вывести эпик по id
        ArrayList<Task> getEpicId = new ArrayList<>();

        for (Integer keyId : epic.keySet()) {
            if (taskID == keyId) {
                //int key = idTask;
                getEpicId.add(epic.get(keyId));
            }
        }
        return getEpicId;
    }

    public ArrayList<SubTask> getsTheEpikSubtask(Epic epics) {  // получить список подзадач определенного Эпика
        ArrayList<SubTask> epicSubtasks= new ArrayList<>();
        for (Integer i : epics.getsubEpicID()) {
            epicSubtasks.add(subTask.get(i));
        }
        return epicSubtasks;
    }

    public void updateEpic(Epic epics) { // обновить Эпик новым Эпиком
        task.put(taskID++, epics);
    }

    public void deleteEpicById(int taskID) { // удалить Эпик по id
        for (Integer keyId : epic.keySet()) {
            if (taskID == keyId) {
                epic.remove(keyId);
                break;
            }
        }
    }
    public void StatusEpic(Epic epics) { // логика статуса Эпика
        boolean epicTaskStatusNew = true;
        boolean epicTaskStatusDone = true;

        for (Integer subEpicId : epics.getsubEpicID()) {
            String status = subTask.get(subEpicId ).getTaskStatus();
            if (!status.equals("NEW")) {
                epicTaskStatusNew = false;
            }
            if (!status.equals("DONE")) {
                epicTaskStatusDone = false;
            }

            if (epicTaskStatusNew) { // если все подзадачи Эпика имеют статус NEW
                epics.setTaskStatus("NEW");
            } else if (epicTaskStatusDone) {
                epics.setTaskStatus("DONE"); // если все подзадачи имеют статус DONE, то и эпик считается завершённым
            } else {
                epics.setTaskStatus("IN_PROGRESS"); // во всех остальных случаях Эпик имеет статус в работе
            }
        }
    }


    // РАБОТА С ПОДЗАДАЧАМИ


    public void addSubTask(SubTask subTasks) { // добавить новую подзадачу
        taskID++;
        if (subTasks.getTaskStatus().isEmpty()) {
            subTasks.setTaskStatus("NEW");
        }
        subTask.put(taskID, subTasks);
        subTasks.getEpicID().getsubEpicID().add(taskID);
    }

    public HashMap<Integer, SubTask> getSubTask() { // получить все подзадачи
        return subTask;
    }


    public void deleteSubTask() { // очистить мапу с подзадачами
        subTask.clear();
    }

    public ArrayList<Task> getSubTaskbyId(int taskID) { // вывести подзадачу по id
        ArrayList<Task> getSubTaskId = new ArrayList<>();

        for (Integer keyId : subTask.keySet()) {
            if (taskID == keyId) {
                getSubTaskId.add(subTask.get(keyId));
            }
        }
        return getSubTaskId;
    }

    public void updateTask(SubTask subTasks) { // обновляем поздачу новой подзадачей
        task.put(taskID++, subTasks);
    }

    public void deleteSubTaskById(int taskID) { // удалить подзадачу по id
        for (Integer keyId : subTask.keySet()) {
           int key = keyId;
            if (taskID == key) {
                subTask.remove(keyId);
                break;
            }
        }
    }
}