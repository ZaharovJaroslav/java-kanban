package taskTracker.tracker.service;

public class Node<T> {
    public T data;
    public Node <T> next;
    public Node <T> prev;

    public Node(T data){
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}
