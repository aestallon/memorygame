package hu.aestallon.memorycardgame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GamePanel extends JPanel {

    private static final Set<Color> colours = Set.of(
            Color.BLUE, Color.GREEN, Color.ORANGE, Color.PINK,
            Color.RED, Color.YELLOW,
            new Color(209, 78, 222),
            new Color(37, 77, 10),
            new Color(13, 29, 121),
            new Color(34, 112, 77)
    );

    private final List<Card> cards;
    private Card selection = null;

    public GamePanel() {
        // <------- Internal Data ------->
        cards = new ArrayList<>();
        for (Color c : colours) {
            cards.add(new Card(c));
            cards.add(new Card(c));
        }
        Collections.shuffle(cards);

        // <-------- Appearance --------->
        int[] rowsAndCols = calculateRowsAndCols(cards.size());
        this.setLayout(new GridLayout(rowsAndCols[0], rowsAndCols[1]));
        for (Card card : cards) {
            this.add(card);
        }
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

        private static final Color DEFAULT_COLOR = Color.GRAY;
        private static final int SIZE = 50;
        private static int attemptCount = 0;

        private final Color secret;

        private boolean active = true;
        private boolean timeout = false;

        private Card(Color secret) {
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
            Card previous = gamePanel.selection;
            if (previous == null) {
                this.setBackground(secret);
                gamePanel.selection = this;
                this.active = false;
            } else {
                attemptCount++;
                if (previous != this) {
                    this.setBackground(secret);
                    if (this.secret.equals(previous.secret)) {
                        this.active = false;            // ez a kártya is legyen passzív
                        gamePanel.selection = null;   // ne legyen többé választott kártya.
                    } else {
                        // The colors don't match:
                        gamePanel.cards.forEach(c -> c.timeout = true);
                        Timer timer = new Timer(1_000, e -> {
                            this.setBackground(DEFAULT_COLOR);
                            previous.setBackground(DEFAULT_COLOR);

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
