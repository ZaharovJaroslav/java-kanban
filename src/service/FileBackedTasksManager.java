package service;

import exceptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public static void main(String[] args) {

        FileBackedTasksManager fileBackedTasksManager1 = loadFromFile(new File("tasks.csv"));
        System.out.println(fileBackedTasksManager1.getTasks());
        System.out.println();
        System.out.println(fileBackedTasksManager1.getEpics());
        System.out.println();
        System.out.println(fileBackedTasksManager1.getSubTasks());
        System.out.println();
        System.out.println();
        System.out.println(fileBackedTasksManager1.getHistori());
}

    private final File file;
    private static final String FIRST_LINE = "id,type,name,status,description,epic,start_Time,end_Time, duration";

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        int maxId = 0;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);

            while (br.ready()) {
                String line = br.readLine();
                if (line.equals(FIRST_LINE)) { // Если строка содержит поля то пропускаем
                    continue;
                } else if (line.contains("TASK") || line.contains("EPIC") || line.contains("SUBTASK")) {
                        Task task = fromString(line);
                        if (task.getTypeTask().equals(TypeTask.TASK)) { // Если же это одна из типов задач, превращаем в Task, и добавляем задачу
                        fileManager.tasks.put(task.getTaskID(), task);
                        for (Integer id : fileManager.tasks.keySet()) {
                            if (id > maxId) {
                                maxId = id;
                            }
                        }
                        fileManager.taskID = maxId;
                        } else if (task.getTypeTask().equals(TypeTask.EPIC)) {
                            Epic epic = (Epic) task;
                            fileManager.epics.put(epic.getTaskID(), new Epic(epic.getTaskID(),
                            epic.getTypeTask(),
                            epic.getName(),
                            epic.getTaskStatus(),
                            epic.getDescription(),
                            epic.getStartTime(),
                            epic.getDuration(),
                            epic.getEndTime()));
                            for (Integer id : fileManager.epics.keySet()) {
                                if (id > maxId) {
                                    maxId = id;
                                }
                            }
                            fileManager.taskID = maxId;
                        } else if (task.getTypeTask().equals(TypeTask.SUBTASK)) {
                            SubTask subTask = (SubTask) task;
                            fileManager.taskID++;
                            Epic epic = fileManager.epics.get(subTask.getIdEpic());
                            fileManager.subTasks.put(subTask.getTaskID(), new SubTask(subTask.getTaskID(),
                            subTask.getTypeTask(),
                            subTask.getName(),
                            subTask.getTaskStatus(),
                            subTask.getDescription(),
                            subTask.getIdEpic(),
                            subTask.getStartTime(),
                            subTask.getDuration(),
                            subTask.getEndTime()));
                            epic.addSubtask(subTask.getTaskID());
                            SubTask subtask = fileManager.subTasks.get(subTask.getTaskID());
                            subtask.setIdEpic(epic.getTaskID());
                            for (Integer id : fileManager.subTasks.keySet()) {
                                if (id > maxId) {
                                    maxId = id;
                                }
                            }
                            fileManager.taskID = maxId + 1;  // если будем создавать новую задачу, id новой задачи будет равен maxId+1;
                        }
                } else {
                    List<Integer> idVivedTasks = new ArrayList<>(historyFromString(line));
                    for (Integer id : idVivedTasks) {
                        if (fileManager.epics.containsKey(id)) {
                            fileManager.historyManager.add(fileManager.epics.get(id));
                        } else if (fileManager.tasks.containsKey(id)) {
                            fileManager.historyManager.add(fileManager.tasks.get(id));
                        } else if (fileManager.subTasks.containsKey(id)) {
                            fileManager.historyManager.add(fileManager.subTasks.get(id));
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
             System.out.println("Ошибка при чтении файла" + e.getMessage());
        }
        return fileManager;
    }

    private  void save() {
         try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, UTF_8))) {
         writer.write(FIRST_LINE);
         writer.write("\n");
         saveTasksToFile(writer);
         List<String> ids = new ArrayList<>(); //сохраняем историю просмотров
         for (Task task : getHistori()) {
             ids.add(String.valueOf(task.getTaskID()));
         }
         writer.write(String.join(",", ids));
        } catch (IOException e) {
         throw new ManagerSaveException("Ошибка при попытке записи в файл");
        }
    }

    private void saveTasksToFile(BufferedWriter writer) throws IOException {
         for (Task task : tasks.values()) {
             writer.write(toString(task));
             writer.write("\n");
         }
        for (Epic epic : epics.values()) {
            writer.write(toString(epic));
            writer.write("\n");
        }
        for (SubTask subTask : subTasks.values()) {
            writer.write(toString(subTask));
            writer.write("\n");
        }
    }

    public static void saveHistoryViews(List<Task> history) { // Метод для записи id просмотренных задач в файл
         List<Task> taskID = new ArrayList<>(history);
         StringBuilder stringBuilder = new StringBuilder();

           for (int i = 0; i < taskID.size(); i++) {
               int id = taskID.get(i).getTaskID();
               if (i == taskID.size() - 1) {
                   stringBuilder.append(Integer.toString(id));
               } else {
                   stringBuilder.append(Integer.toString(id)).append(",");
               }
           }
    }

    private static List<Integer> historyFromString(String value) {  // Метод для получения id gросмотренных задач из файла
        List<Integer> taskID = new ArrayList<>();
        String[] array = value.split(",");
        for (String id: array) {
            taskID.add(Integer.parseInt(id));
        }
        return taskID;
    }


    private String toString(Task task) { ///  Из Task ---> в String
        String taskToSave;

       if (task.getTypeTask() == TypeTask.TASK) {
           return taskToSave = task. getTaskID() +
                "," + task.getTypeTask() +
                "," + task.getName() +
                "," + task.getTaskStatus() +
                "," + task.getDescription() +
                 "," + "-" +
                "," + task.getStartTime().format(DATA_TIME_FORMAT) +
                "," + task.getEndTime().format(DATA_TIME_FORMAT) +
                "," + task.getDuration().toMinutes();

       } else if (task.getTypeTask() == TypeTask.EPIC) {
           Epic epic = (Epic) task;
           if (epic.getStartTime() == null) {
              return taskToSave = epic.getTaskID() +
                       "," + epic.getTypeTask() +
                       "," + epic.getName() +
                       "," + epic.getTaskStatus() +
                       "," + epic.getDescription() +
                       "," + "-" +
                       "," + "-" +
                       "," + "-" +
                       "," + "-";

           } else {
               return taskToSave = epic.getTaskID() +
                       "," + epic.getTypeTask() +
                       "," + epic.getName() +
                       "," + epic.getTaskStatus() +
                       "," + epic.getDescription() +
                       "," + "-" +
                       "," + epic.getStartTime().format(DATA_TIME_FORMAT) +
                       "," + epic.getEndTime().format(DATA_TIME_FORMAT) +
                       "," + epic.getDuration().toMinutes();
           }

       } else {
           SubTask subTask = (SubTask) task;
           return taskToSave = subTask.getTaskID() +
                   "," + subTask.getTypeTask() +
                   "," + subTask.getName() +
                   "," + subTask.getTaskStatus() +
                   "," + subTask.getDescription() +
                   "," + subTask.getIdEpic() +
                   "," + subTask.getStartTime().format(DATA_TIME_FORMAT) +
                   "," + subTask.getEndTime().format(DATA_TIME_FORMAT) +
                   "," + subTask.getDuration().toMinutes();
       }
    }

    private static Task fromString(String value) { // из String ---> в Task
        String[] arrays = (value.split(","));
        int id = 0;
        TypeTask type = null;
        String name = null;
        TaskStatus status = null;
        String description = null;
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        Duration duration = null;
        int epicID = 0;

        for (int i = 0; i < arrays.length; i++) {
            switch (i) {
                case 0:
                    id = Integer.parseInt(arrays[i]);
                    break;
                case 1:
                    if (arrays[i].equals("TASK")) {
                        type = TypeTask.TASK;
                        break;
                    } else if (arrays[i].equals("EPIC")) {
                        type = TypeTask.EPIC;
                        break;
                    } else {
                        type = TypeTask.SUBTASK;
                        break;
                    }
                case 2:
                    name = arrays[i];
                    break;
                case 3:
                    if (arrays[i].equals("NEW")) {
                        status = TaskStatus.NEW;
                    } else if (arrays[1].equals(" IN_PROGRESS")) {
                        status = TaskStatus.IN_PROGRESS;
                    } else {
                        status = TaskStatus.DONE;
                    }
                case 4:
                    description = arrays[i];
                    break;

                case 5:
                    if (type == TypeTask.SUBTASK) {
                     epicID = Integer.parseInt(arrays[i]);
                    } else
                        continue;
                    break;

                case 6:
                    startTime = LocalDateTime.parse(arrays[i],DATA_TIME_FORMAT);
                    break;

                case 7:
                    endTime = LocalDateTime.parse(arrays[i],DATA_TIME_FORMAT);
                    break;

                case 8:
                    long conversionToLong = Long.parseLong(arrays[i]);

                    duration = Duration.ofMinutes(conversionToLong);
                    break;
            }
        }
        if (type == TypeTask.SUBTASK) {
            return new SubTask(id, type, name, status, description, epicID, startTime, duration, endTime);
        }  else if (type == TypeTask.EPIC) {
            return new Epic(id, type, name, status, description, startTime, duration, endTime);
        } else
            return new Task(id, type, name, status, description, startTime, duration, endTime);
    }

// РАБОТА С ПРОСТЫМИ ЗАДАЧАМИ

    @Override
    public ArrayList<Task> getPrioritizedTasks() {
     return super.getPrioritizedTasks();
    }

    @Override
    public boolean checkForTimeIntersections(Task newTask) {
       return super.checkForTimeIntersections(newTask);
    }

    @Override
    public void computeEpicDataTime(Epic epic, SubTask subTask) {
        super.computeEpicDataTime(epic, subTask);
    }

    @Override
    public void addTask(Task task) { // добавить новую задачу в мапу
        super.addTask(task);
        save();
    }

    @Override
    public void deleteTask() { // очистить мапу с простыми задачами
       super.deleteTask();
       save();
    }

    @Override
    public Task getTaskbyId(int taskID) { // получить задачу по id
        Task task = super.getTaskbyId(taskID);
        if (task != null) {
            save();
        }

        return task;
    }

    @Override
    public void updateTask(Task task, Task newTask) { // обновить задачу новой задачей
        super.updateTask(task, newTask);
        save();
    }

    @Override
    public void deleteTaskById(int taskID) { // удалить задачу по id
        super.deleteTaskById(taskID);
        save();

    }

    // РАБОТА С ЭПИКАМИ
    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void deleteEpics() { // очистить мапу c Эпиками, удалить все подзадачи
        super.deleteEpics();
        save();
    }

    @Override
    public Epic getEpicbyId(int taskID) { // вывести эпик по id
        Epic epic = super.getEpicbyId(taskID);
        if (epic != null) {
            save();
        }
        return epic;
    }

    @Override
    public void updateEpic(Epic epic, Epic newEpic) { // обновить Эпик новым Эпиком
        super.updateEpic(epic, newEpic);
        save();
    }

    @Override
    public boolean deleteEpicById(int taskID) { // удалить Эпик по id,а так же его подзадачи
        super.deleteEpicById(taskID);
        save();
        return true;
    }


    @Override
    public void computeEpicStatus(Epic epic) {
       super.computeEpicStatus(epic);
       save();
    }

    @Override
    public void addSubTask(SubTask subTask) { // добавить новую подзадачу.
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void deleteSubTask() { // очистить мапу с подзадачами, удалить подазадачи из эпиков и обновить их статусы
        super.deleteSubTask();
        save();
    }

    @Override
    public SubTask getSubTaskbyId(int taskID) { // вывести подзадачу по id
       SubTask subTask = super.getSubTaskbyId(taskID);
        if (subTask != null) {
            save();
        }
       return subTask;
    }

    @Override
    public void updateSubTask(SubTask subTask, SubTask newSubTask) { // обновляем поздачу новой подзадачей и обновить статус Епика
        super.updateSubTask(subTask, newSubTask);
        save();
    }

    @Override
    public boolean deleteSubTaskById(int taskID) { // удалить подзадачу по id, удалить из Эпика + обновить статус
       super.deleteSubTaskById(taskID);
       save();
       return true;
    }

    @Override
    public List<Task> getHistori() {
        return historyManager.getHistory();
    }
}
