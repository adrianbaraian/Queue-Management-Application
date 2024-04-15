package BussinesLogic;

import Model.Server;
import Model.Task;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class ShortestQueueStrategy implements Strategy{
    @Override
    public void addTask(CopyOnWriteArrayList<Server> servers, Task task) {
        if(servers.isEmpty()) {
            return;
        }
        //System.out.println("aici2");
        Server currentShortestServer = servers.getFirst();

        for(Server server : servers) {
            if(server.getTasks().size() < currentShortestServer.getTasks().size()) {
                currentShortestServer = server;
            }
        }
        if (currentShortestServer != null) {
            currentShortestServer.addTask(task);
            //System.out.println("shortest queue: " + Thread.currentThread().getName());
        }
    }
}
