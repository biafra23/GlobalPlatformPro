package pro.javacard.gp;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;

public class CardWrapper {
    private final Card card;

    public CardWrapper(Card card) {
        this.card = card;
    }

    public CardChannel getBasicChannel() {
        return card.getBasicChannel();
    }

    public void beginExclusive() throws CardExceptionWrapper {
        try {
            card.beginExclusive();
        } catch (CardException e) {
            throw new CardExceptionWrapper(e);
        }
    }
}
