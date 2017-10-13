package pro.javacard.gp;

import javax.smartcardio.ResponseAPDU;

public class ResponseAPDUWrapper {
    private final ResponseAPDU responseAPDU;

    public ResponseAPDUWrapper(ResponseAPDU responseAPDU) {
        this.responseAPDU = responseAPDU;
    }

    public ResponseAPDUWrapper(byte[] bytes) {
        responseAPDU = new ResponseAPDU(bytes);
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

    public byte[] getBytes() {
        return responseAPDU.getBytes();
    }

    public int getSW1() {
        return responseAPDU.getSW1();
    }

    public int getSW2() {
        return responseAPDU.getSW2();
    }
}
