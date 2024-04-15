package Model;

import java.util.concurrent.atomic.AtomicInteger;

public class Task {
    private int ID;
    private int arrivalTime;
    private int serviceTime;

    public Task(int ID, int arrivalTime, int serviceTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getID() {
        return ID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void decrementServiceTime(Server server) {
        this.serviceTime--;
        server.setWaitingPeriod(new AtomicInteger(server.getWaitingPeriod().decrementAndGet()));
    }
}
