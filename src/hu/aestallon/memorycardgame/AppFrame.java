package hu.aestallon.memorycardgame;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class AppFrame extends JFrame {

    private GamePanel gamePanel;

    public AppFrame() {
        // <------------ INIT ------------>
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Memory Game");
        this.setSize(400, 300);
        this.setResizable(false);
        this.setLayout(null);

        // <--------- Customizing -------->
        JButton startButton = new JButton("New Game");
        startButton.setSize(100, 40);
        startButton.setLocation(0, 0);
        startButton.addActionListener(e -> createNewGame(Main.FISH));
        this.add(startButton);

        // <------------ MISC ------------>
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    private void createNewGame(Set<ImageIcon> imageSet) {
        if (gamePanel != null) this.remove(gamePanel);
        gamePanel = new GamePanel(imageSet);
        gamePanel.setLocation(0, 41);
        this.add(gamePanel);
        Dimension gamePanelSize = gamePanel.getSize();
        this.setSize(gamePanelSize.width + 10, gamePanelSize.height + 100);
        SwingUtilities.updateComponentTreeUI(gamePanel);
    }
}
