package BusinessLogic;

import GUI.View;
import Model.Server;
import Model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;

import java.util.concurrent.CopyOnWriteArrayList;

public class SimulationManager implements Runnable{
    //for testing
    private static int averageWaitingTime;
    private static int averageServiceTime;
    private static int peakHour;
    public int peakHourNrClients = -1;
    private final int timeLimit;
    private final int maxProcessingTime;
    private final int minProcessingTime;
    private final int minArrivalTime;

    private final int maxArrivalTime;
    private final int numberOfServers;
    private final int numberOfClients;
    private SelectionPolicy selectionPolicy;

    private final Scheduler scheduler;
    private View view;
    private final CopyOnWriteArrayList<Task> tasks;
    public SimulationManager(int timeLimit, int maxProcessingTime, int minProcessingTime, int minArrivalTime, int maxArrivalTime, int numberOfServers, int numberOfClients, View view) {
        averageWaitingTime = 0;
        averageServiceTime = 0;
        peakHour = 0;

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

            boolean allQueuesEmpty;

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
                    peakHour = currentTime;
                }

                view.updateView(loggerQueues, loggerWaiting);

                fw.write(loggerQueues + "\n\nRemaining Clients: " + loggerWaiting + "\n\n");

                if(tasks.isEmpty()) {
                    allQueuesEmpty = true;
                    for(Server server : scheduler.getServers()) {
                        if (!server.getTasks().isEmpty()) {
                            allQueuesEmpty = false;
                            break;
                        }
                    }

                    if(allQueuesEmpty) {
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
            fw.write("Average Waiting Time: " + (averageWaitingTime / (float)numberOfClients) + "\n");
            fw.write("Average Service Time: " + (averageServiceTime / (float)numberOfClients) + "\n");
            fw.write("Peak Hour: " + peakHour + "\n");

            this.view.createResultsFrame(averageWaitingTime / (float)numberOfClients, averageServiceTime / (float)numberOfClients, peakHour);


            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        }

    public static int getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public static void setAverageWaitingTime(int averageWaitingTime) {
        SimulationManager.averageWaitingTime = averageWaitingTime;
    }

    public static int getAverageServiceTime() {
        return averageServiceTime;
    }

    public static void setAverageServiceTime(int averageServiceTime) {
        SimulationManager.averageServiceTime = averageServiceTime;
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
