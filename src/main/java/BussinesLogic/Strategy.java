package BussinesLogic;

import Model.Server;
import Model.Task;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public interface Strategy {
    public void addTask(CopyOnWriteArrayList<Server> servers, Task task);
}
