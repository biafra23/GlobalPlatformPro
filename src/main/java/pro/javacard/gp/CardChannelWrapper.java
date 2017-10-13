package pro.javacard.gp;

import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import java.nio.ByteBuffer;

public class CardChannelWrapper implements CardChannelInterface {

    private CardChannel cardChannel;

    public CardChannelWrapper(CardChannel cardChannel) {
        this.cardChannel = cardChannel;
    }

    @Override
    public CardWrapper getCard() {
        return new CardWrapper(cardChannel.getCard());
    }

    @Override
    public int getChannelNumber() {
        return cardChannel.getChannelNumber();
    }

    @Override
    public ResponseAPDUWrapper transmit(CommandAPDUWrapper var1) throws CardExceptionWrapper {
        try {
            return new ResponseAPDUWrapper(cardChannel.transmit(var1.getWrapped()));
        } catch (CardException e) {
            throw new CardExceptionWrapper(e);
        }
    }

    @Override
    public int transmit(ByteBuffer var1, ByteBuffer var2) throws CardExceptionWrapper {
        try {
            return cardChannel.transmit(var1, var2);
        } catch (CardException e) {
            throw new CardExceptionWrapper(e);
        }
    }

    @Override
    public void close() throws CardExceptionWrapper {
        try {
            cardChannel.close();
        } catch (CardException e) {
            throw new CardExceptionWrapper(e);
        }
    }
}
