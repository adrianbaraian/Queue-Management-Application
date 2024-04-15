package utils;

public class InputValidator {
    public static boolean isNumeric(String str) {
        if(str == null) {
            return false;
        }
        try {
            Integer d = Integer.parseInt(str);
        } catch(NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    public static boolean isValidInput(int minServiceTime, int maxServiceTime, int numberOfClients, int numberOfQueues, int simulationTime, int minArrivalTime, int maxArrivalTime) {
        if(minServiceTime < 0 || maxServiceTime < 0 || numberOfClients < 0 || numberOfQueues < 0 || simulationTime < 0 || minArrivalTime < 0 || maxArrivalTime < 0) {
            return false;
        }
        if(maxServiceTime < minServiceTime || maxArrivalTime < minArrivalTime) {
            return false;
        }

        return simulationTime >= maxArrivalTime;
    }
}
