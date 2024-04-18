package service;
import model.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer,Node<Task>> custemMap = new HashMap<>();
    private List<Task> oldVersionHistori = new ArrayList<>();
    private Node<Task> head;
    private Node<Task> tail;

    @Override
    public Map<Integer, Node<Task>> getCustemMap() {
        return custemMap;
    }

    @Override
    public Node<Task> getHead() {
        return head;
    }

    @Override
    public Node<Task> getTail() {
        return tail;
    }

    @Override
    public void add(Task task) {
    if (custemMap.isEmpty()) {
        linkLast(task);
    } else {
        oldVersionHistori = getTasks();
        remove(task.getTaskID());
        linkLast(task);
    }
    }

    @Override
    public void remove(int id) {
       if (custemMap.containsKey(id)) {
           oldVersionHistori = getTasks();
            removeNode(id);
        }
    }

    private void removeNode(int id) {
        final Node<Task> node = custemMap.get(id);
        if (node == head) {
            removeFirst(node);
            custemMap.remove(id);
        } else if (node == tail) {
            removeLast(node);
            custemMap.remove(id);
        } else {
            custemMap.remove(id);
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
        }
    }

    public void removeFirst(Node<Task> node) {
        if (head.getNext() == null) {
            head = null;
            tail = null;
        } else {
            head.getNext().setPrev(null);
            head = head.getNext();
        }
    }

    public void removeLast(Node<Task> node) {
        tail.getPrev().setNext(null);
        tail = tail.getPrev();
    }

    private void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(task,null, tail);
        tail = newNode;
        custemMap.put(task.getTaskID(), newNode);
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.setNext(tail);
        }
    }

    private List<Task> getTasks() {
    LinkedList<Task> viewedTasks = new LinkedList<>();
    Node<Task> node = head;

    while (node != null) {
        viewedTasks.add(node.getData());
        node = node.getNext();
    }
    return viewedTasks;
    }

    @Override
    public List<Task> getHistory() {
      return getTasks();
    }
    @Override
    public List<Task> getOldVersionHistori() {
    return oldVersionHistori;
    }
}


