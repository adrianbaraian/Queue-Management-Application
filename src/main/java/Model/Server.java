package Model;

import BusinessLogic.SimulationManager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public Server(int maxTasksPerServer) {
        this.tasks = new ArrayBlockingQueue<>(maxTasksPerServer);
        this.waitingPeriod = new AtomicInteger(0);
    }

    public void addTask(Task newTask) {
        this.tasks.add(newTask);
        SimulationManager.setAverageWaitingTime(SimulationManager.getAverageWaitingTime() + this.waitingPeriod.get());
        SimulationManager.setAverageServiceTime(SimulationManager.getAverageServiceTime() + newTask.getServiceTime());
        this.waitingPeriod.addAndGet(newTask.getServiceTime());

    }

    @Override
    public void run() {
        while(true) {
            if(!tasks.isEmpty()) {
                Task currentTask = tasks.peek();
                try {
                    Thread.sleep(currentTask.getServiceTime() * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.waitingPeriod.addAndGet(-currentTask.getServiceTime());
                tasks.remove();
            }
        }
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setWaitingPeriod(AtomicInteger waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }
}
