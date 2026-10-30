import javax.swing.*;

public class App {
    static JFrame frame;

    public static void main(String[] args) {
        frame = new JFrame("FlappyBird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.add(new MenuPanel(() -> startGame()));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static void startGame() {
        frame.getContentPane().removeAll();

        Logic logika = new Logic();
        View tampilan = new View(logika);
        logika.setView(tampilan);

        frame.add(tampilan);
        frame.revalidate();
        frame.repaint();
        tampilan.requestFocusInWindow();
    }
}
