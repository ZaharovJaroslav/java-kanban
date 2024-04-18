package service;

import exceptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public static void main(String[] args) {
        TaskManager fileBackedTasksManager = Managers.getDefaultFileBackedTasks();

           Epic epic1 = new Epic("Epic1_Test", "Epic1_Description");
           fileBackedTasksManager.addEpic(epic1);


      SubTask subTask1 = new SubTask(TypeTask.SUBTASK, "SubTask_Name1", " SubTask_Descriptio4",
                Duration.ofHours(1).plusMinutes(30),
                LocalDateTime.of(2024, Month.APRIL, 20, 2, 0),
                0);
        fileBackedTasksManager.addSubTask(subTask1);



   SubTask subTask2 = new SubTask(TypeTask.SUBTASK, "SubTask_Name2", " SubTask_Description2",
                Duration.ofHours(5).plusMinutes(30),
                LocalDateTime.of(2024, Month.APRIL, 21, 2, 0),
                0);
        fileBackedTasksManager.addSubTask(subTask2);


    Task task1 = new Task(TypeTask.TASK, "Task_Name1", " Task_Description1",
                Duration.ofHours(1).plusMinutes(30),
                LocalDateTime.of(2024, Month.DECEMBER, 11, 5, 0));
        fileBackedTasksManager.addTask(task1);
    Task task2 = new Task(TypeTask.TASK, "Task_Name2", " Task_Description2",
                Duration.ofHours(1).plusMinutes(30),
                LocalDateTime.of(2024, Month.DECEMBER, 11, 5, 0));
        fileBackedTasksManager.addTask(task2);

        fileBackedTasksManager.getTaskbyId(3);
        fileBackedTasksManager.getTaskbyId(4);
}



/*       FileBackedTasksManager fileManager = FileBackedTasksManager.loadFromFile(new File("tasks.csv"));

    ;

       Task task3 = new Task(TypeTask.TASK, "Task_Name3", " Task_Description3",
                              Duration.ofHours(1).plusMinutes(30),
                              LocalDateTime.of(2024, Month.APRIL,25,1,0));
        fileManager.addTask(task3);

        Task task4 = new Task(TypeTask.TASK, "Task_Name4", " Task_Description4",
                              Duration.ofHours(1).plusMinutes(30),
                              LocalDateTime.of(2024, Month.APRIL,27,1,0));
        fileManager.addTask(task4);

         Task task5 = new Task(TypeTask.TASK, "Task_Name4", " Task_Description4",
                               Duration.ofHours(1).plusMinutes(30),
                               LocalDateTime.of(2024, Month.APRIL,28,1,0));
        fileManager.addTask(task4);

        SubTask subTask2 = new SubTask(TypeTask.SUBTASK,"Subtask_Name2"," Subtask_Description2",
                                       Duration.ofHours(1).plusMinutes(30),
                                       LocalDateTime.of(2024, Month.APRIL,29,11,0),
                                       2);// id = 03
        fileManager.addSubTask(subTask2);*/
   // }
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
                } else if (line.contains("TASK") || line.contains("EPIC") || line.contains("SUBTASK")) {  // Если же это одна из типов задач, превращаем в Task, и добавляем задачу
                    Task task = fromString(line);
                    if (task.getTypeTask() == TypeTask.TASK) {
                       fileManager.tasks.put(task.getTaskID(), task);
                      for (Integer id : fileManager.tasks.keySet()) { // итерируем по id ищем максимальное значение
                          if (id > maxId) {
                              maxId = id;
                          }
                      }
                        fileManager.taskID = maxId;

                    } else if (task.getTypeTask() == TypeTask.EPIC) {
                        fileManager.epics.put(task.getTaskID(),new Epic(task.getTaskID(),
                                task.getTypeTask(),
                                task.getName(),
                                task.getTaskStatus(),
                                task.getDescription(),
                                task.getStartTime(),
                                task.getDuration(),
                                task.getEndTime()));
                        for (Integer id : fileManager.epics.keySet()) {
                            if (id > maxId) {
                                maxId = id;
                            }
                        }
                        fileManager.taskID = maxId;

                    } else {
                        fileManager.taskID++;
                        Epic epic = fileManager.epics.get(task.getIdEpic());
                        fileManager.subTasks.put(task.getTaskID(),new SubTask(task.getTaskID(),
                                task.getTypeTask(),
                                task.getName(),
                                task.getTaskStatus(),
                                task.getDescription(),
                                task.getIdEpic(),
                                task.getStartTime(),
                                task.getDuration(),
                                task.getEndTime()));
                        epic.addSubtask(task.getTaskID());
                        SubTask subtask =  fileManager.subTasks.get(task.getTaskID());
                        subtask.setIdEpic(epic.getTaskID());
                        for (Integer id : fileManager.subTasks.keySet()) {
                            if (id > maxId) {
                                maxId = id;
                            }
                        }
                        fileManager.taskID = maxId + 1;  // если будем создавать новую задачу, id новой задачи будет равен maxId+1;
                       }

                    } else {
                    List<Integer> list = new ArrayList<>(historyFromString(line));
                    List<Task> viewedTasks = new ArrayList<>();
                    for (Integer el : list) {
                        if (fileManager.epics.containsKey(el)) {
                            viewedTasks.add(fileManager.epics.get(el));

                        } else if (fileManager.tasks.containsKey(el)) {
                            viewedTasks.add(fileManager.tasks.get(el));

                        } else {
                            viewedTasks.add(fileManager.subTasks.get(el));
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



    private void save() {
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
           if (task.getStartTime() == null) {
              return taskToSave = task. getTaskID() +
                       "," + task.getTypeTask() +
                       "," + task.getName() +
                       "," + task.getTaskStatus() +
                       "," + task.getDescription() +
                       "," + "-" +
                       "," + "-" +
                       "," + "-" +
                       "," + "-";

           } else {
               return taskToSave = task. getTaskID() +
                       "," + task.getTypeTask() +
                       "," + task.getName() +
                       "," + task.getTaskStatus() +
                       "," + task.getDescription() +
                       "," + "-" +
                       "," + task.getStartTime().format(DATA_TIME_FORMAT) +
                       "," + task.getEndTime().format(DATA_TIME_FORMAT) +
                       "," + task.getDuration().toMinutes();
           }

       } else {
           return taskToSave = task.getTaskID() +
                   "," + task.getTypeTask() +
                   "," + task.getName() +
                   "," + task.getTaskStatus() +
                   "," + task.getDescription() +
                   "," + task.getIdEpic() +
                   "," + task.getStartTime().format(DATA_TIME_FORMAT) +
                   "," + task.getEndTime().format(DATA_TIME_FORMAT) +
                   "," + task.getDuration().toMinutes();
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
                        status =  TaskStatus.IN_PROGRESS;
                    } else {
                        status =  TaskStatus.DONE;
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
        super.computeEpicDataTime(epic,subTask);
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
    public void deleteEpics() {  // очистить мапу c Эпиками, удалить все подзадачи
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
    public void deleteSubTask() {  // очистить мапу с подзадачами, удалить подазадачи из эпиков и обновить их статусы
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
