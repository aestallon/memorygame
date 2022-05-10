package hu.aestallon.memorycardgame;

import javax.swing.*;

public class AppFrame extends JFrame {
    public AppFrame() {
        // <------------ INIT ------------>
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(400, 300);

        // <--------- Customizing -------->
        GamePanel gamePanel = new GamePanel();
        this.add(gamePanel);

        // <------------ MISC ------------>
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
