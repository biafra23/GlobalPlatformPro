package pro.javacard.gp;


import java.nio.ByteBuffer;

public interface CardChannelInterface {

    CardWrapper getCard();

    int getChannelNumber();

    ResponseAPDUWrapper transmit(CommandAPDUWrapper var1) throws CardExceptionWrapper;

    int transmit(ByteBuffer var1, ByteBuffer var2) throws CardExceptionWrapper;

    void close() throws CardExceptionWrapper;

}
