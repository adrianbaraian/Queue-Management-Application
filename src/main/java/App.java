import GUI.View;

public class App {
    public App() {
        View view = new View("Queue Management Application");
        view.setVisible(true);
    }

    public static void main(String[] args) {
        new App();
    }
}
