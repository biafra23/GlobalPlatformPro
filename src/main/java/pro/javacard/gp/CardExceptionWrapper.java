package pro.javacard.gp;

import javax.smartcardio.CardException;

public class CardExceptionWrapper extends Exception {
    private final CardException cardException;

    public CardExceptionWrapper(CardException e) {
        cardException = e;
    }
}
