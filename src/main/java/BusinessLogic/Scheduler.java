package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.concurrent.CopyOnWriteArrayList;

public class Scheduler {
    private final CopyOnWriteArrayList<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        this.servers = new CopyOnWriteArrayList<>();

        for(int i = 1; i <= maxNoServers; i++) {
            Server server = new Server(maxTasksPerServer);
            this.servers.add(server);
            Thread newThread = new Thread(server);
            newThread.start();
        }
    }

    public void changeStrategy(SelectionPolicy selectionPolicy) {
        if(selectionPolicy == SelectionPolicy.SHORTEST_QUEUE) {
            this.strategy = new ShortestQueueStrategy();
        }
        if(selectionPolicy == SelectionPolicy.SHORTEST_TIME) {
            this.strategy = new TimeStrategy();
        }
    }

    public void dispatchTask(Task task) {
        strategy.addTask(this.servers, task);
    }

    public CopyOnWriteArrayList<Server> getServers() {
        return servers;
    }
}
