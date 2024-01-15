package taskTracker.tracker.service;

import taskTracker.tracker.model.Node;
import taskTracker.tracker.model.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    private static List<Task> customLinkedList = new LinkedList<>();
    private static Map <Integer,Node<Task>> custemMap = new HashMap<>();
    private static List<Task> taskHistory = new ArrayList<>();
    private Node<Task> head;
    private Node<Task> tail;


@Override
    public void  linkLast(Task task ) {
    Node<Task> newNode = new Node<>(task);
    if (customLinkedList.isEmpty()) {
        customLinkedList.add(task);
        head = newNode;
        tail = newNode;
        custemMap.put(task.getTaskID(), newNode);
        taskHistory = customLinkedList;
    } else if (!customLinkedList.isEmpty() && !custemMap.containsKey(task.getTaskID())) {
        customLinkedList.add(task);
        taskHistory = customLinkedList;
        tail.next = newNode;
        newNode.prev = tail;
        tail = newNode;
        custemMap.put(task.getTaskID(), newNode);
    } else {
            if (custemMap.containsKey(task.getTaskID())) {
                 int idTask = task.getTaskID();
                Node<Task> node = custemMap.get(task.getTaskID());
                removeNode(node);

                custemMap.remove(idTask);
                customLinkedList.add(task);
                taskHistory = customLinkedList;
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
                custemMap.put(task.getTaskID(), newNode);
            }
        }
    }


    public void removeNode(Node<Task> node){
        if(node == head){
            removeFirst(node);
        } else if(node == tail){
            removeLast(node);
        } else {
            customLinkedList.remove(node.data);
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }


    public void removeFirst(Node <Task> data){
        if(head.next == null){
            tail = null;
            customLinkedList.remove(head.data);
        } else {
            customLinkedList.remove(head.data);
            head.next.prev = null;
            head = head.next;
        }
    }
    public void removeLast(Node <Task> data){
        if(head.next == null){
            tail = null;
        } else {
            customLinkedList.remove(tail.data);
            tail.prev.next = null;
            tail = tail.prev;
        }
    }


    @Override
    public void remove(int id){
        if (custemMap.containsKey(id)) {
            Node <Task> node = custemMap.get(id);
            removeNode(node);
            taskHistory = customLinkedList;
            custemMap.remove(id);
        }
    }


    @Override
    public List<Task> getHistory() {
        return taskHistory;
    }
}
