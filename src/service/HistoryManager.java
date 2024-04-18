package service;
import model.Task;
import java.util.List;
import java.util.Map;

//интерфейс для управления историей просмотров
public interface HistoryManager {

    public void add(Task task);
    public void remove (int id);
    public List<Task> getHistory();
    public List<Task> getOldVersionHistori();

    public Map<Integer, Node<Task>> getCustemMap();


    public Node<Task> getHead();


    public Node<Task> getTail();




}
