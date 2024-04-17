package taskManager;

import org.junit.jupiter.api.Test;
import taskTracker.tracker.exceptions.CollisionTaskException;
import taskTracker.tracker.model.*;
import taskTracker.tracker.service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager> {
    protected T taskManager;

   final Epic epic1 = new Epic("Epic1_Test", "Epic1_Description");
   final Epic epic2 = new Epic("Epic2_Test", "Epic2_Description");

   final SubTask subTask1 = new SubTask(TypeTask.SUBTASK, "SubTask_Name1", " SubTask_Descriptio4",
            Duration.ofHours(1).plusMinutes(30),
            LocalDateTime.of(2024, Month.APRIL, 20, 2, 0),
            0);

   final SubTask subTask2 = new SubTask(TypeTask.SUBTASK, "SubTask_Name2", " SubTask_Description2",
            Duration.ofHours(5).plusMinutes(30),
            LocalDateTime.of(2024, Month.APRIL, 29, 2, 0),
            0);

    final Task task1 = new Task(TypeTask.TASK, "Task_Name1", " Task_Description1",
            Duration.ofHours(1).plusMinutes(30),
            LocalDateTime.of(2024, Month.JUNE, 11, 5, 0));
    final Task task2 = new Task(TypeTask.TASK, "Task_Name2", " Task_Description2",
            Duration.ofHours(1).plusMinutes(30),
            LocalDateTime.of(2024, Month.DECEMBER, 11, 5, 0));



// ТЕСТЫ ДЛЯ TASK

    @Test
    void Test1_addTask_shouldCreateATask() {
        taskManager.addTask(task1);
        List<Task> listOfTasks = taskManager.getTasks();

        assertTrue(listOfTasks.contains(task1));
    }

    @Test
    void Test2_getTasks_shouldReturnTheCreatedTasks() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        List<Task> listOfTasks = taskManager.getTasks();

        assertEquals(2, listOfTasks.size());
    }

    @Test
    void Test3_deleteTask_shouldDeleteAllTasks () {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.deleteTask();
        List<Task> listOfTasks = taskManager.getTasks();

        assertTrue(listOfTasks.isEmpty());
    }


    @Test
    void Test4_getTaskById_shouldReturnTheTaskById() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        int taskId = task1.getTaskID();
        Task task = taskManager.getTaskbyId(taskId);

        assertEquals(task1, task);
    }

    @Test
    void Test5_updateTask_shouldUpdateTheTask() {
        taskManager.addTask(task1);


        taskManager.updateTask(task1, task2);
        List<Task> listOfTasks = taskManager.getTasks();

        assertEquals(task1.getTaskID(), task2.getTaskID());
        assertTrue(listOfTasks.contains(task2));
        assertFalse(listOfTasks.contains(task1));
    }

    @Test
    void Test6_deleteTaskById_shouldDeleteTheTaskById() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        taskManager.deleteTaskById(task1.getTaskID());
        List<Task> listOfTasks = taskManager.getTasks();

        assertFalse(listOfTasks.contains(task1));
    }


    //ТЕСТЫ ДЛЯ EPIC


    @Test
    void Test7_addEpic_shouldCreateAEpic() {
        taskManager.addEpic(epic1);
        List<Epic> listOfEpics = taskManager.getEpics();

        assertTrue(listOfEpics.contains(epic1));
    }

    @Test
    void Test8_getEpics_shouldReturnTheCreatedEpics(){
        taskManager.addEpic(epic1);
        List<Epic> listOfEpics = taskManager.getEpics();

        assertEquals(1, listOfEpics.size());
    }

    @Test
    void Test9_deleteEpics_shouldDeleteAllEpicsAndItsSubtasks() {
        taskManager.addEpic(epic1);

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.deleteEpics();
        List<SubTask> liseOfSubTasks = taskManager.getSubTasks();
        List<Epic> listOfEpics = taskManager.getEpics();

        assertTrue(liseOfSubTasks.isEmpty());
        assertTrue(listOfEpics.isEmpty());
    }

    @Test
    void Test10_getEpicById_shouldReturnTheEpicById() {
        taskManager.addEpic(epic1);
        Epic epic = taskManager.getEpicbyId(epic1.getTaskID());

        assertEquals(epic1, epic);
    }

    @Test
    void Test11_getsEpicSubtasks_shouldReturnEpicSubtasks() { //
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        List<SubTask> epicSubtasks = taskManager.getsEpicSubtasks(epic1);

        assertEquals(2, epicSubtasks.size());
        assertTrue(epicSubtasks.contains(subTask1));
        assertTrue(epicSubtasks.contains(subTask2));
    }

    @Test
    void Test12_updateEpics_shouldUpdateTheEpic() {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.updateEpic(epic1, epic2);

        List<Epic> listOfEpic = taskManager.getEpics();

        assertEquals(1, listOfEpic.size());
        assertTrue(listOfEpic.contains(epic2));
        assertFalse(listOfEpic.contains(epic1));
    }

    @Test
    void Test13_deleteEpicById_shouldDeleteTheEpicById() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.deleteEpicById(epic1.getTaskID());
        List<Epic> listOfEpic = taskManager.getEpics();

        assertFalse(listOfEpic.contains(epic1));
    }

    @Test
    void Test14_computeEpicStatus_shouldSetTheStatusToEpic() {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        subTask2.setStatus(TaskStatus.DONE);
        taskManager.computeEpicStatus(epic1);

        assertEquals(TaskStatus.IN_PROGRESS, epic1.getTaskStatus());
        assertEquals(TaskStatus.DONE, subTask2.getTaskStatus());
        assertEquals(TaskStatus.IN_PROGRESS, subTask1.getTaskStatus());
    }

    //ТЕСТЫ ДЛЯ SUBTASK


    @Test
    void Test15_addSubTask_shouldCreateSubTask() {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);
        List<SubTask> listOfSubTasks = taskManager.getSubTasks();

        assertTrue(listOfSubTasks.contains(subTask1));
    }

    @Test
    void Test16_getSubTasks_shouldReturnTheCreatedSubTasks() {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);
        List<SubTask> listOfSubTasks = taskManager.getSubTasks();

        assertEquals(1, listOfSubTasks.size());
    }

    @Test
    void Test17_deleteSubTask_shouldRemoveAllSubtasksAndAlsoRemoveFromEpics() {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        taskManager.deleteSubTask();
        List<SubTask> listOfSubTasks = taskManager.getSubTasks();
        List<Integer> subTasksIds = epic1.getsubTasksIds();

        assertTrue(listOfSubTasks.isEmpty());
        assertTrue(subTasksIds.isEmpty());
        assertEquals(TaskStatus.NEW, epic1.getTaskStatus());
    }

    @Test
    void Test18_getSubTaskById_shouldReturnTheSubTaskById() {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        int taskId = subTask1.getTaskID();
        SubTask task = taskManager.getSubTaskbyId(taskId);

        assertEquals(subTask1, task);
    }

    @Test
    void Test19_updateSubTask_shouldUpdateTheSubTask() {// обновляем поздачу новой подзадачей и обновить статус Епика
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);
        taskManager.updateSubTask(subTask1, subTask2);
        subTask2.setStatus(TaskStatus.IN_PROGRESS);

        taskManager.computeEpicStatus(epic1);
        List<SubTask> listOfSubTasks = taskManager.getSubTasks();

        assertEquals(1, listOfSubTasks.size());
        assertTrue(listOfSubTasks.contains(subTask2));
        assertFalse(listOfSubTasks.contains(subTask1));
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getTaskStatus());
    }

    @Test
    void Test19_deleteSubTaskById_shouldDeleteTheSubTaskById() {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);

        taskManager.deleteSubTaskById(subTask1.getTaskID());
        List<SubTask> listOfSubTasks = taskManager.getSubTasks();

        assertTrue(listOfSubTasks.isEmpty());
        assertEquals(TaskStatus.NEW, epic1.getTaskStatus());
    }

    @Test
    void Test20_shouldСheckIfTheSubtaskContainsAnEpic() { //b. Для подзадач нужно дополнительно проверить наличие эпика, а для эпика — расчёт статуса.
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        List<Integer> subTaskIds = epic1.getsubTasksIds();
        assertTrue(subTaskIds.contains(subTask1.getTaskID()));
        assertEquals(epic1.getTaskID(), subTask1.getIdEpic());
    }

    // Тесты на DateTime

    @Test
    void test21_shouldСheckTheDurationIsSet() { // Проверка на то что продолжительнсоть задачи задана.
        taskManager.addTask(task1);

        assertNotNull(task1.getDuration());
        assertNotNull(task1.getStartTime());
        assertNotNull(task1.getEndTime());
    }

    @Test
    void test22_shouldCheckIfTheDurationOfTheEpicIsSet() { // Проверка на то что продолжительнсоть подзадачи и эпика задана
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);

        assertNotNull(epic1.getDuration());
        assertNotNull(epic1.getStartTime());
        assertNotNull(epic1.getEndTime());
    }

    @Test
    void test23_shouldReturnTheTaskCompletionTime() { // тест на правильный росчет времени завершения задачи
        taskManager.addTask(task1);
        Duration duration = task1.getDuration();
        LocalDateTime startTime = task1.getStartTime();
        LocalDateTime endTime = task1.getEndTime();

        assertEquals(startTime.plus(duration), endTime);
    }


    @Test
    void test24_ShouldReturnTheCompletionTimeOfTheEpic() { // задаеться ли поле заверешения задачи у эпика
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        LocalDateTime endTime = epic1.getEndTime();

        assertNotNull(endTime);
    }

    @Test
    void test25_shouldCheckTheCalculationOfTheDurationOfTheTask() {// Продолжительность эпика — сумма продолжительностей всех его подзадач.
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);  //2024, Month.APRIL, 20, 2, 0), дл - 1.3 зак 3-30
        taskManager.addSubTask(subTask2);  //2024, Month.APRIL, 21, 2, 0), дл - 5.3 - 7-30

        LocalDateTime startTime = epic1.getStartTime(); //2024, Month.APRIL, 20, 2, 0)
        LocalDateTime endTime = epic1.getEndTime();  ////2024, Month.APRIL, 21, 2, 0), 7-30
        Duration durationTask = subTask1.getDuration().plus(subTask2.getDuration());  // 7 часов


        assertEquals(durationTask,epic1.getDuration());
    }

    @Test
    void test26_ShouldСheckTheCalculationOfTheEpicStartTime() {//. Время начала — дата старта самой ранней подзадачи,
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        LocalDateTime startTime = epic1.getStartTime();
        if (subTask1.getStartTime().isBefore(subTask2.getStartTime())){
            assertEquals(startTime, subTask1.getStartTime());
        } else
            assertEquals(startTime, subTask2.getStartTime());
    }
    @Test
    void test27_ShouldСheckTheCalculationOfTheEpicEndTime() {// а время завершения — время окончания самой поздней из задач.
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        LocalDateTime subTask1EndTime = subTask1.getEndTime();
        LocalDateTime subTask2EndTime = subTask2.getEndTime();
        LocalDateTime epicEndTime = epic1.getEndTime();

        if (subTask1EndTime.isBefore(subTask2EndTime)){
            assertEquals( subTask2EndTime ,epicEndTime );
        } else
            assertEquals(subTask1EndTime, epicEndTime );
    }

    @Test
    void test28_ShouldThrowAnExceptionCollisionTaskException() {
   Task task_A  = new Task(TypeTask.TASK, "Task_Name1", " Task_Description1",
                Duration.ofHours(1).plusMinutes(30),
                LocalDateTime.of(2024, Month.JUNE, 11, 5, 0));

        Task task_B  = new Task(TypeTask.TASK, "Task_Name1", " Task_Description1",
                Duration.ofHours(1).plusMinutes(30),
                LocalDateTime.of(2024, Month.JUNE, 11, 5, 0));


      assertThrows(CollisionTaskException.class,
                () -> {
                    taskManager.addTask(task_A);
                    taskManager.addTask(task_B);

                },"Время выполнения задачи пересекается со временем уже существующей " +
                        "задачи. Выберите другую дату.");
    }
}


