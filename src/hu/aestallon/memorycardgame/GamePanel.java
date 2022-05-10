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
            Color.RED, Color.YELLOW/*,*/
//            new Color(209, 78, 222),
//            new Color(37, 77, 10),
//            new Color(13, 29, 121),
//            new Color(34, 112, 77)
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


    public List<Card> getCards() {
        return cards;
    }

    public Card getSelection() {
        return selection;
    }

    public void setSelection(Card selection) {
        this.selection = selection;
    }
}
