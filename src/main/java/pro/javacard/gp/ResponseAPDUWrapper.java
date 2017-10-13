package pro.javacard.gp;

import javax.smartcardio.ResponseAPDU;

public class ResponseAPDUWrapper {
    private final ResponseAPDU responseAPDU;

    public ResponseAPDUWrapper(ResponseAPDU responseAPDU) {
        this.responseAPDU = responseAPDU;
    }

    ResponseAPDU getWrapped() {
        return responseAPDU;
    }

    public int getSW() {
        return responseAPDU.getSW();
    }

    public byte[] getData() {
        return responseAPDU.getData();
    }
}
