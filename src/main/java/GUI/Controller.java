package GUI;

import BussinesLogic.SelectionPolicy;
import BussinesLogic.SimulationManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Controller implements ActionListener {
    private View view;
    public Controller(View view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if(Objects.equals(command, "EXECUTE")) {
            int minServiceTime = Integer.parseInt(this.view.getServiceTimeMinInputField().getText());
            int maxServiceTime = Integer.parseInt(this.view.getServiceTimeMaxInputField().getText());
            int numberOfClients = Integer.parseInt(this.view.getClientInputField().getText());
            int numberOfQueues = Integer.parseInt(this.view.getQueueInputField().getText());
            int simulationTime = Integer.parseInt(this.view.getSimulationTimeInputField().getText());
            int minArrivalTime = Integer.parseInt(this.view.getArrivalTimeMinInputField().getText());
            int maxArrivalTime = Integer.parseInt(this.view.getArrivalTimeMaxInputField().getText());
            String chosenStrategy = String.valueOf(this.view.getStrategyComboBox().getSelectedItem());

            SimulationManager simulationManager = new SimulationManager(simulationTime, maxServiceTime, minServiceTime, minArrivalTime, maxArrivalTime, numberOfQueues, numberOfClients, this.view);

            simulationManager.setView(this.view);

            if(Objects.equals(chosenStrategy, "Shortest Queue")) {
                simulationManager.setSelectionPolicy(SelectionPolicy.SHORTEST_QUEUE);
            } else if (Objects.equals(chosenStrategy, "Shortest Time")) {
                simulationManager.setSelectionPolicy(SelectionPolicy.SHORTEST_TIME);
            }

            simulationManager.getScheduler().changeStrategy(simulationManager.getSelectionPolicy());

            Thread t = new Thread(simulationManager);
            t.start();
        }
    }
}
