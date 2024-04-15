package BussinesLogic;

import GUI.View;
import Model.Server;
import Model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationManager implements Runnable{
    //for testing
    private static AtomicInteger averageWaitingTime;
    private static AtomicInteger averageServiceTime;
    private static AtomicInteger peakHour;
    public int peakHourNrClients = -1;
    private int timeLimit;
    private int maxProcessingTime;
    private int minProcessingTime;
    private int minArrivalTime;

    private int maxArrivalTime;
    private int numberOfServers;
    private int numberOfClients;
    private SelectionPolicy selectionPolicy;

    private Scheduler scheduler;
    private View view;
    private CopyOnWriteArrayList<Task> tasks;
    public SimulationManager(int timeLimit, int maxProcessingTime, int minProcessingTime, int minArrivalTime, int maxArrivalTime, int numberOfServers, int numberOfClients, View view) {
        averageWaitingTime = new AtomicInteger(0);
        averageServiceTime = new AtomicInteger(0);
        peakHour = new AtomicInteger(0);

        this.timeLimit = timeLimit;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;

        this.tasks = new CopyOnWriteArrayList<>();
        this.scheduler = new Scheduler(this.numberOfServers, this.numberOfClients);
        generateNRandomTasks();
    }

    public void generateNRandomTasks() {
        int currentID = 1;
        for(int i = 1; i <= numberOfClients; i++) {
            int processingTime = (int)(Math.random() * (maxProcessingTime - minProcessingTime) + minProcessingTime);
            int arrivalTime = (int)(Math.random() * (maxArrivalTime - minArrivalTime) + minArrivalTime);
            Task task = new Task(currentID++, arrivalTime, processingTime);
            this.tasks.add(task);
        }

        this.tasks.sort(Comparator.comparingInt(Task::getArrivalTime));

        for (Task task : tasks) {
            System.out.println("{ " + task.getID() + ", " + task.getArrivalTime() + ", " + task.getServiceTime() + " }");
        }
    }

    @Override
    public void run() {
        try {
            FileWriter fw = new FileWriter("src/main/java/log.txt");
            fw.flush();
            int currentTime = 0;
            String loggerWaiting = "";
            String loggerQueues = "";
            for(Task t : this.tasks) {
                if(t.getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(t);
                    this.tasks.remove(t);
                }

                loggerWaiting = loggerWaiting.concat("{ " + t.getID() + ", " + t.getArrivalTime() + ", " + t.getServiceTime() + " } ");
            }
            fw.write("Initial Clients: " + loggerWaiting + "\n\n");
            view.updateView(loggerQueues, loggerWaiting);

            boolean waitingLineEmpty = false;
            boolean allQueuesEmpty = true;

            while(currentTime < timeLimit) {
                loggerQueues = "";
                loggerWaiting = "";
                loggerQueues = loggerQueues.concat("Current Time: " + currentTime + "\n\n");

                for(Task t : this.tasks) {
                    if(t.getArrivalTime() == currentTime) {
                        scheduler.dispatchTask(t);
                        this.tasks.remove(t);
                    }
                }

                for(Task t : this.tasks) {
                    loggerWaiting = loggerWaiting.concat("{ " + t.getID() + ", " + t.getArrivalTime() + ", " + t.getServiceTime() + " } ");
                }

                int currentHourNrClients = 0;

                for(int i = 0; i < scheduler.getServers().size(); i++) {
                    currentHourNrClients += scheduler.getServers().get(i).getTasks().size();
                    loggerQueues = loggerQueues.concat("Queue " + i + ": ");
                    for(Task t : scheduler.getServers().get(i).getTasks()) {
                        loggerQueues = loggerQueues.concat("{ " + t.getID() + ", " + t.getArrivalTime() + ", " + t.getServiceTime() + " } ");
                        if(t == scheduler.getServers().get(i).getTasks().peek()) {
                            t.decrementServiceTime(scheduler.getServers().get(i));
                        }
                    }
                    loggerQueues = loggerQueues.concat("\n");
                }

                if(currentHourNrClients > peakHourNrClients) {
                    peakHourNrClients = currentHourNrClients;
                    peakHour.set(currentTime);
                }

                view.updateView(loggerQueues, loggerWaiting);

                fw.write(loggerQueues + "\n\nRemaining Clients: " + loggerWaiting + "\n\n");

                if(tasks.isEmpty()) {
                    waitingLineEmpty = true;
                    allQueuesEmpty = true;
                    for(Server server : scheduler.getServers()) {
                        if (!server.getTasks().isEmpty()) {
                            allQueuesEmpty = false;
                            break;
                        }
                    }

                    if(waitingLineEmpty && allQueuesEmpty) {
                        break;
                    }
                }

                currentTime++;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            loggerQueues = "Program finished! Finish time: " + currentTime;
            loggerWaiting = "Program finished!";

            view.updateView(loggerQueues, loggerWaiting);

            fw.write(loggerQueues + "\n\n" + "\n\n");
            fw.write("Average Waiting Time: " + (float)(averageWaitingTime.get() / (float)numberOfClients) + "\n");
            fw.write("Average Service Time: " + (float)(averageServiceTime.get() / (float)numberOfClients) + "\n");
            fw.write("Peak Hour: " + peakHour.get() + "\n");

            View.createResultsFrame((float)(averageWaitingTime.get() / (float)numberOfClients), (float)(averageServiceTime.get() / (float)numberOfClients), peakHour.get());


            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        }

    public static AtomicInteger getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public static void setAverageWaitingTime(AtomicInteger averageWaitingTime) {
        SimulationManager.averageWaitingTime = averageWaitingTime;
    }

    public static AtomicInteger getAverageServiceTime() {
        return averageServiceTime;
    }

    public static void setAverageServiceTime(AtomicInteger averageServiceTime) {
        SimulationManager.averageServiceTime = averageServiceTime;
    }

    public static AtomicInteger getPeakHour() {
        return peakHour;
    }

    public static void setPeakHour(AtomicInteger peakHour) {
        SimulationManager.peakHour = peakHour;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setSelectionPolicy(SelectionPolicy selectionPolicy) {
        this.selectionPolicy = selectionPolicy;
    }

    public SelectionPolicy getSelectionPolicy() {
        return selectionPolicy;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }


}
