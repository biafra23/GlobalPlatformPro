package pro.javacard.gp;

import javax.smartcardio.CommandAPDU;

public class CommandAPDUWrapper {
    private CommandAPDU commandAPDU;

    public CommandAPDUWrapper(byte[] var1) {
        commandAPDU = new CommandAPDUWrapper(var1);
    }

    public CommandAPDUWrapper(byte[] var1, int var2, int var3) {
        commandAPDU = new CommandAPDUWrapper(var1, var2, var3);
    }

    public CommandAPDUWrapper(int var1, int var2, int var3, int var4) {
        this.commandAPDU = new CommandAPDUWrapper(var1, var2, var3, var4, (byte[]) null, 0, 0, 0);
    }

    public CommandAPDUWrapper(int var1, int var2, int var3, int var4, int var5) {
        this.commandAPDU = new CommandAPDUWrapper(var1, var2, var3, var4, (byte[]) null, 0, 0, var5);
    }

    public CommandAPDUWrapper(int var1, int var2, int var3, int var4, byte[] var5) {
        this.commandAPDU = new CommandAPDUWrapper(var1, var2, var3, var4, var5, 0, arrayLength(var5), 0);
    }

    public CommandAPDUWrapper(int var1, int var2, int var3, int var4, byte[] var5, int var6, int var7) {
        this.commandAPDU = new CommandAPDUWrapper(var1, var2, var3, var4, var5, var6, var7, 0);
    }

    public CommandAPDUWrapper(int var1, int var2, int var3, int var4, byte[] var5, int var6) {
        this.commandAPDU = new CommandAPDUWrapper(var1, var2, var3, var4, var5, 0, arrayLength(var5), var6);
    }

    public CommandAPDUWrapper(CommandAPDU commandAPDU) {
        this.commandAPDU = commandAPDU;
    }

    public CommandAPDU getWrapped() {
        return commandAPDU;
    }

    private static int arrayLength(byte[] var0) {
        return var0 != null ? var0.length : 0;
    }

}
