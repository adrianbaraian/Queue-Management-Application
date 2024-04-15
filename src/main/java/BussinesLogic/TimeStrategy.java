package BussinesLogic;

import Model.Server;
import Model.Task;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class TimeStrategy implements Strategy{
    @Override
    public void addTask(CopyOnWriteArrayList<Server> servers, Task task) {
       if(servers.isEmpty()) {
           return;
       }

       Server minServiceTimeServer = null;

       int minServiceTime = 0x7FFFFFFF;

       for(Server server : servers) {
           if(server.getTasks().isEmpty()) {
                minServiceTimeServer = server;
                break;
           }
           if(server.getWaitingPeriod().get() < minServiceTime) {
               minServiceTime = server.getWaitingPeriod().get();
               minServiceTimeServer = server;
           }
       }

       if(minServiceTimeServer != null) {
           minServiceTimeServer.addTask(task);
       }

    }
}
