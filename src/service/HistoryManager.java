package service;
import model.Task;
import java.util.List;
import java.util.Map;

//интерфейс для управления историей просмотров
public interface HistoryManager {

    void add(Task task);

    void remove(int id);

     List<Task> getHistory();

     List<Task> getOldVersionHistori();

     Map<Integer, Node<Task>> getCustemMap();

     Node<Task> getHead();

     Node<Task> getTail();




}
