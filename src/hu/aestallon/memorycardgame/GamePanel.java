package hu.aestallon.memorycardgame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GamePanel extends JPanel {

    private static final ImageIcon DEFAULT_ICON = new ImageIcon("resources/default.png");
    private static final int SIZE = 100;

    private final List<Card> cards;
    private Card selection = null;

    public GamePanel(Set<ImageIcon> imageSet) {
        // <------- Internal Data ------->
        cards = new ArrayList<>();
        for (ImageIcon icon : imageSet) {
            cards.add(new Card(icon));
            cards.add(new Card(icon));
        }
        Collections.shuffle(cards);

        // <-------- Appearance --------->
        int[] rowsAndCols = calculateRowsAndCols(cards.size());
        GridLayout layout = new GridLayout(rowsAndCols[0], rowsAndCols[1], 5, 5);
        this.setLayout(layout);
        for (Card card : cards) {
            this.add(card);
        }
        // Resizing whole application:
        int width = layout.getColumns() * SIZE;
        int height = layout.getRows() *  SIZE;
        this.setSize(new Dimension(width, height)); // Gamepanel mérete elvieg jó

        this.setBackground(new Color(87, 159, 170));
    }

    public void doVictory() {
        JOptionPane.showMessageDialog(
                null,
                "Gratulálok nyertél!\n" + "Ennyi lépés kellett: " + Card.attemptCount,
                "GYŐZELEM",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private int[] calculateRowsAndCols(int cardCount) {
        int factor = (int) Math.sqrt(cardCount);
        while (cardCount % factor != 0) factor--;
        return new int[]{factor, cardCount / factor};
    }

    // Inner class representing a card in the memory game.
    private static class Card extends JButton {
        private static int attemptCount = 0;

        private final ImageIcon secret;

        private boolean active = true;
        private boolean timeout = false;

        private Card(ImageIcon secret) {
            // <------- Internal Data ------->
            this.secret = secret;

            // <-------- Appearance --------->
            this.setBorder(null);
            this.setIcon(DEFAULT_ICON);
            this.setPreferredSize(new Dimension(100, 100));
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
            Card previous = gamePanel.selection;
            if (previous == null) {
                this.setIcon(secret);
                gamePanel.selection = this;
                this.active = false;
            } else {
                attemptCount++;
                if (previous != this) {
                    this.setIcon(secret);
                    if (this.secret.equals(previous.secret)) {
                        this.active = false;            // ez a kártya is legyen passzív
                        gamePanel.selection = null;   // ne legyen többé választott kártya.
                    } else {
                        // The colors don't match:
                        gamePanel.cards.forEach(c -> c.timeout = true);
                        Timer timer = new Timer(1_000, e -> {
                            this.setIcon(DEFAULT_ICON);
                            previous.setIcon(DEFAULT_ICON);

                            this.active = true;
                            previous.active = true;

                            gamePanel.selection = null;
                            gamePanel.cards.forEach(c -> c.timeout = false);
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                }
            }
            // Check if we won:
            boolean victory = gamePanel.cards.stream().noneMatch(card -> card.active);
            if (victory) gamePanel.doVictory();
        }
    }
}
