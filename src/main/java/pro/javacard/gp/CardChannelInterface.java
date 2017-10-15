package pro.javacard.gp;


import pro.javacard.gp.smardcardio.Card;
import pro.javacard.gp.smardcardio.CardException;
import pro.javacard.gp.smardcardio.CommandAPDU;
import pro.javacard.gp.smardcardio.ResponseAPDU;

import java.nio.ByteBuffer;

public interface CardChannelInterface {

    Card getCard();

    int getChannelNumber();

    ResponseAPDU transmit(CommandAPDU var1) throws CardException;

    int transmit(ByteBuffer var1, ByteBuffer var2) throws CardException;

    void close() throws CardException;

}
