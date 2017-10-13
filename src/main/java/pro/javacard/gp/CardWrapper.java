package pro.javacard.gp;

import javax.smartcardio.Card;

public class CardWrapper {
    private final Card card;

    public CardWrapper(Card card) {
        this.card = card;
    }
}
