package hu.aestallon.memorycardgame;

import javax.swing.*;
import java.awt.*;

public class Card extends JButton {

    public static final Color DEFAULT_COLOR = Color.GRAY;
    public static final int SIZE = 50;
    public static int attemptCount = 0;

    private final Color secret;

    private boolean active = true;
    private boolean timeout = false;

    public Card(Color secret) {
        // <------- Internal Data ------->
        this.secret = secret;

        // <-------- Appearance --------->
        this.setBorder(null);
        this.setBackground(DEFAULT_COLOR);
        this.setFocusable(false);
        this.setBorderPainted(false);
        this.setFocusPainted(false);

        // <--------- Behaviour --------->
        this.addActionListener(e -> cardAction());

    }

    private void cardAction() {
        if (!this.active || this.timeout) return;
        if (!(this.getParent() instanceof GamePanel)) throw new AssertionError();

        GamePanel gamePanel = (GamePanel) this.getParent();
        Card previous = gamePanel.getSelection();
        if (previous == null) {
            this.setBackground(secret);
            gamePanel.setSelection(this);
            this.active = false;
        } else {
            attemptCount++;
            if (previous != this) {
                this.setBackground(secret);
                if (this.secret.equals(previous.secret)) {
                    this.active = false;            // ez a kártya is legyen passzív
                    gamePanel.setSelection(null);   // ne legyen többé választott kártya.
                } else {
                    // The colors don't match:
                    gamePanel.getCards().forEach(c -> c.timeout = true);
                    Timer timer = new Timer(1_000, e -> {
                        this.setBackground(DEFAULT_COLOR);
                        previous.setBackground(DEFAULT_COLOR);

                        this.active = true;
                        previous.active = true;

                        gamePanel.setSelection(null);
                        gamePanel.getCards().forEach(c -> c.timeout = false);
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        }
        // Check if we won:
        boolean victory = gamePanel.getCards().stream().noneMatch(card -> card.active);
        if (victory) gamePanel.doVictory();
    }

    @Override
    public String toString() {
        return secret.toString();
    }
}
