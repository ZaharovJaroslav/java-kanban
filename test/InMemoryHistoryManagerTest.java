import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import service.HistoryManager;
import service.Managers;
import service.Node;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

  HistoryManager historyManager;
  Task task1;
  Task task2;
  Epic epic1;

  @BeforeEach
  void createTasks() {
      historyManager = Managers.getDefaultHistory();

      task1 = new Task(0,TypeTask.TASK, "Task_Name1", TaskStatus.NEW, " Task_Dscription",
               LocalDateTime.of(2024, Month.OCTOBER, 11, 5, 0),
               Duration.ofHours(1).plusMinutes(30));

       task2 = new Task(1,TypeTask.TASK, "Task_Name1", TaskStatus.NEW, " Task_Dscription",
               LocalDateTime.of(2024, Month.SEPTEMBER, 11, 5, 0),
               Duration.ofHours(1).plusMinutes(30));

       epic1 = new Epic(2,"Epic1_Test", "Epic1_Description");
 }

    @Test
    void test1_ShouldAddTasksToYourBrowsingHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        List<Task> viewedTasks = historyManager.getHistory();

        assertTrue(viewedTasks.contains(task1));
        assertTrue(viewedTasks.contains(task2));
    }

    @Test
    void test2_ShouldReturnAnEmptyList() {
        List<Task> viewedTasks = historyManager.getHistory();

        assertTrue(viewedTasks.isEmpty());
    }

    @Test
    void test3_ShouldAllowRepetitionsOfTasks() {
        historyManager.add(task1);
        historyManager.add(task1);
        List<Task> viewedTasks = historyManager.getHistory();

        assertTrue(viewedTasks.contains(task1));
    }

    @Test
    void test4_ShouldDeleteTheFirstViewedTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(epic1);

        historyManager.remove(task1.getTaskID());
        List<Task> viewedTasks = historyManager.getHistory();

        assertFalse(viewedTasks.contains(task1));
    }

    @Test
    void test5_shouldDeleteTheViewedTaskFromTheMiddle() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(epic1);

        historyManager.remove(task2.getTaskID());
        List<Task> viewedTasks = historyManager.getHistory();
        assertFalse(viewedTasks.contains(task2));
    }

    @Test
    void test6_shouldDeleteTheLastViewedTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(epic1);

        historyManager.remove(epic1.getTaskID());
        List<Task> viewedTasks = historyManager.getHistory();
        assertFalse(viewedTasks.contains(epic1));
    }

    @Test
    void test7_ShouldTheOldVersionOfTheBrowsingHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getTaskID());
        List<Task> OldVersionHistori = historyManager.getOldVersionHistori();

        assertTrue(OldVersionHistori.contains(task1));
    }

    @Test
    void test8_shouldReturnThFirstTask() {
        historyManager.add(task1);
        Node<Task> node1 = historyManager.getHead();

        assertEquals(node1.getData(),task1);
    }

    @Test
    void test9_shouldReturnTheLastTask() {
        historyManager.add(task1);
        Node<Task> node1 = historyManager.getHead();

        assertEquals(node1.getData(),task1);
    }
}

