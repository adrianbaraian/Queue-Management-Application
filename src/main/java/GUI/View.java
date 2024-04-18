package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class View extends JFrame {
    private JPanel contentPane;
    private JPanel inputPanel;
    private JPanel logPanel;
    private JLabel queueLabel;
    private JLabel clientLabel;
    private JTextField queueInputField;
    private JTextField clientInputField;
    private JLabel simulationTimeLabel;
    private JTextField simulationTimeInputField;
    private JLabel arrivalTimeMinLabel;
    private JLabel arrivalTimeMaxLabel;
    private JTextField arrivalTimeMinInputField;
    private JTextField arrivalTimeMaxInputField;
    private JLabel serviceTimeMinLabel;
    private JLabel serviceTimeMaxLabel;
    private JTextField serviceTimeMinInputField;
    private JTextField serviceTimeMaxInputField;

    private JComboBox<String> strategyComboBox;

    private JButton executeButton;
    private JButton validateButton;
    private JLabel errorLabel;
    private JTextArea loggerAreaQueues;
    private JTextArea loggerAreaWaiting;
    Controller controller = new Controller(this);
    public View(String name) {
        super(name);
        this.prepareGUI();
    }

    void prepareGUI() {
        this.setSize(1366, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.contentPane = new JPanel(new GridLayout(1, 2));
        this.contentPane.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        this.prepareInputPanel();
        this.prepareLogPanel();
        this.setContentPane(this.contentPane);
    }

    public void prepareInputPanel() {
        this.inputPanel = new JPanel();
        this.inputPanel.setLayout(new GridLayout(7, 2));

        JPanel tempPanel1 = new JPanel();
        tempPanel1.setLayout(new FlowLayout());

        this.clientLabel = new JLabel("Number of Clients:");
        this.clientLabel.setBorder(new EmptyBorder(0, 0, 0, 40));
        tempPanel1.add(this.clientLabel);
        this.clientInputField = new JTextField();
        this.clientInputField.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.clientInputField.setColumns(5);
        tempPanel1.add(clientInputField);
        this.inputPanel.add(tempPanel1);

        JPanel tempPanel2 = new JPanel();
        tempPanel2.setLayout(new FlowLayout());

        this.queueLabel = new JLabel("Number of Queues:");
        this.queueLabel.setBorder(new EmptyBorder(0, 0, 0, 40));
        tempPanel2.add(this.queueLabel);
        this.queueInputField = new JTextField();
        this.queueInputField.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.queueInputField.setColumns(5);
        tempPanel2.add(this.queueInputField);

        this.inputPanel.add(tempPanel2);

        JPanel tempPanel3 = new JPanel();
        tempPanel3.setLayout(new FlowLayout());

        this.simulationTimeLabel = new JLabel("Simulation Time:");
        this.simulationTimeLabel.setBorder(new EmptyBorder(0, 0, 0, 40));
        this.simulationTimeInputField = new JTextField();
        this.simulationTimeInputField.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.simulationTimeInputField.setColumns(5);
        tempPanel3.add(this.simulationTimeLabel);
        tempPanel3.add(this.simulationTimeInputField);

        this.inputPanel.add(tempPanel3);

        JPanel tempPanel4 = new JPanel();
        tempPanel4.setLayout(new FlowLayout());

        this.arrivalTimeMinLabel = new JLabel("Min Arrival Time:");
        this.arrivalTimeMinLabel.setBorder(new EmptyBorder(0, 0, 0, 15));
        this.arrivalTimeMaxLabel = new JLabel("Max Arrival Time");
        this.arrivalTimeMaxLabel.setBorder(new EmptyBorder(0, 15, 0, 15));
        this.arrivalTimeMinInputField = new JTextField();
        this.arrivalTimeMinInputField.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.arrivalTimeMinInputField.setColumns(5);
        this.arrivalTimeMaxInputField = new JTextField();
        this.arrivalTimeMaxInputField.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.arrivalTimeMaxInputField.setColumns(5);

        tempPanel4.add(arrivalTimeMinLabel);
        tempPanel4.add(arrivalTimeMinInputField);
        tempPanel4.add(arrivalTimeMaxLabel);
        tempPanel4.add(arrivalTimeMaxInputField);

        this.inputPanel.add(tempPanel4);

        JPanel tempPanel5 = new JPanel();
        tempPanel5.setLayout(new FlowLayout());

        this.serviceTimeMinLabel = new JLabel("Min Service Time:");
        this.serviceTimeMinLabel.setBorder(new EmptyBorder(0, 0, 0, 15));
        this.serviceTimeMaxLabel = new JLabel("Max Service Time");
        this.serviceTimeMaxLabel.setBorder(new EmptyBorder(0, 15, 0, 15));
        this.serviceTimeMinInputField = new JTextField();
        this.serviceTimeMinInputField.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.serviceTimeMinInputField.setColumns(5);
        this.serviceTimeMaxInputField = new JTextField();
        this.serviceTimeMaxInputField.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.serviceTimeMaxInputField.setColumns(5);

        tempPanel5.add(serviceTimeMinLabel);
        tempPanel5.add(serviceTimeMinInputField);
        tempPanel5.add(serviceTimeMaxLabel);
        tempPanel5.add(serviceTimeMaxInputField);

        this.inputPanel.add(tempPanel5);

        JPanel tempPanel6 = new JPanel();
        tempPanel6.setLayout(new GridLayout(2, 1));

        this.strategyComboBox = new JComboBox<String>(new String[]{"Shortest Queue", "Shortest Time"});
        tempPanel6.add(this.strategyComboBox);

        this.inputPanel.add(tempPanel6);

        JPanel tempPanel7 = new JPanel();
        tempPanel7.setLayout(new GridLayout(2, 1));

        /*this.validateButton = new JButton("Validate");
        this.validateButton.setActionCommand("VALIDATE");
        this.validateButton.addActionListener(this.controller);*/

        this.errorLabel = new JLabel("Error! Invalid input! Please make sure all fields are filled in correctly.");
        this.errorLabel.setVisible(false);
        this.executeButton = new JButton("Execute");
        this.executeButton.setActionCommand("EXECUTE");
        this.executeButton.addActionListener(this.controller);

        tempPanel7.add(errorLabel);
        tempPanel7.add(executeButton);

        this.inputPanel.add(tempPanel7);

        //this.inputPanel.add(executeButton);


//        this.executeButton.setEnabled(false);

        this.contentPane.add(inputPanel);
    }

    public void prepareLogPanel() {
        this.logPanel = new JPanel();

        this.logPanel.setLayout(new GridLayout(2, 3, 40, 20));

        this.loggerAreaQueues = new JTextArea();
        this.loggerAreaWaiting = new JTextArea();

        this.loggerAreaQueues.setLineWrap(true);
        this.loggerAreaWaiting.setLineWrap(true);
        this.loggerAreaQueues.setWrapStyleWord(true);
        this.loggerAreaWaiting.setWrapStyleWord(true);

        JScrollPane jScrollPane1 = new JScrollPane(this.loggerAreaQueues);
        JScrollPane jScrollPane2 = new JScrollPane(this.loggerAreaWaiting);

        this.logPanel.add(jScrollPane1);
        this.logPanel.add(jScrollPane2);
        this.contentPane.add(logPanel);
    }

    public void updateView(String loggerQueues, String loggerWaiting) {
        this.loggerAreaQueues.setText(loggerQueues);
        this.loggerAreaWaiting.setText(loggerWaiting);
    }

    public void createResultsFrame(float averageWaitingTime, float averageServiceTime, int peakHour) {
        JFrame newFrame = new JFrame("Simulation result");
        newFrame.setSize(500, 500);
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setLayout(new GridLayout(3, 1));
        JLabel averageWaitingTimeLabel = new JLabel("Average waiting time: " + averageWaitingTime);
        JLabel averageServiceTimeLabel = new JLabel("Average service time: " + averageServiceTime);
        JLabel peakHourLabel = new JLabel("Peak hour: " + peakHour);

        contentPanel.add(averageWaitingTimeLabel);
        contentPanel.add(averageServiceTimeLabel);
        contentPanel.add(peakHourLabel);

        newFrame.setContentPane(contentPanel);

        newFrame.setVisible(true);
    }

    public JTextField getQueueInputField() {
        return queueInputField;
    }

    public JTextField getClientInputField() {
        return clientInputField;
    }

    public JTextField getSimulationTimeInputField() {
        return simulationTimeInputField;
    }

    public JTextField getArrivalTimeMinInputField() {
        return arrivalTimeMinInputField;
    }

    public JTextField getArrivalTimeMaxInputField() {
        return arrivalTimeMaxInputField;
    }

    public JTextField getServiceTimeMinInputField() {
        return serviceTimeMinInputField;
    }

    public JTextField getServiceTimeMaxInputField() {
        return serviceTimeMaxInputField;
    }

    public JComboBox<String> getStrategyComboBox() {
        return strategyComboBox;
    }

    public JLabel getErrorLabel() {
        return errorLabel;
    }
}
