package openkms.gpj;

import java.nio.ByteBuffer;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

public class LoggingCardTerminal extends CardTerminal {
	// This code has been taken from Apache commons-codec 1.7
	private static final char[] LOWER_HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	public static String encodeHexString(final byte[] data) {
		
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = LOWER_HEX[(0xF0 & data[i]) >>> 4];
            out[j++] = LOWER_HEX[0x0F & data[i]];
        }
        return new String(out);
    }
	// End of copied code from commons-codec
	
	protected static CardTerminal terminal = null;
	private static LoggingCardTerminal instance;
	
	public static LoggingCardTerminal getInstance(CardTerminal term) {
		if (instance == null)
			instance = new LoggingCardTerminal(term);
		if (!term.equals(terminal))
			instance = new LoggingCardTerminal(term);
		return instance;
	}

	private LoggingCardTerminal (CardTerminal term) {
		terminal = term;
	}

	@Override
	public Card connect(String arg0) throws CardException {
		return new LoggingCard(terminal, arg0);
	}

	@Override
	public String getName() {
		return terminal.getName();
	}

	@Override
	public boolean isCardPresent() throws CardException {
		return terminal.isCardPresent();
	}

	@Override
	public boolean waitForCardAbsent(long arg0) throws CardException {
		return terminal.waitForCardAbsent(arg0);

	}

	@Override
	public boolean waitForCardPresent(long arg0) throws CardException {
		return terminal.waitForCardPresent(arg0);
	}

	
	public final class LoggingCard extends Card {
		private Card card;
		private LoggingCard(CardTerminal term, String protocol) throws CardException {
			card = terminal.connect(protocol);
		}
		
		@Override
		public void beginExclusive() throws CardException {
			card.beginExclusive();	
		}

		@Override
		public void disconnect(boolean arg0) throws CardException {
			System.err.println("SCardDisconnect()");
			System.err.flush();
			card.disconnect(arg0);
		}

		@Override
		public void endExclusive() throws CardException {
			card.endExclusive();
		}

		@Override
		public ATR getATR() {
			return card.getATR();
		}

		@Override
		public CardChannel getBasicChannel() {
			return new LoggingCardChannel(card);
		}

		@Override
		public String getProtocol() {
			return card.getProtocol();
		}

		@Override
		public CardChannel openLogicalChannel() throws CardException {
			return null;
		}

		@Override
		public byte[] transmitControlCommand(int arg0, byte[] arg1) throws CardException {
			return null;
		}
		
		class LoggingCardChannel extends CardChannel {
			private CardChannel channel;
			private Card card;
			public LoggingCardChannel(Card card) {
				this.card = card;
				this.channel = card.getBasicChannel();
			}
			@Override
			public void close() throws CardException {
				channel.close();
			}

			@Override
			public Card getCard() {
				return card;
			}

			@Override
			public int getChannelNumber() {
				return channel.getChannelNumber();
			}

			@Override
			public ResponseAPDU transmit(CommandAPDU apdu) throws CardException {

				int len = apdu.getData().length > 255 ? 7 : 5;
				System.err.println("A>> " + card.getProtocol() + " ("+ len + "+" + apdu.getData().length + ") " + encodeHexString(apdu.getBytes()));
				System.err.flush();
				ResponseAPDU response = channel.transmit(apdu);
				System.err.println("A<< (" + response.getData().length + ") " + encodeHexString(response.getBytes()));
				System.err.flush();
				return response;
			}

			@Override
			public int transmit(ByteBuffer cmd, ByteBuffer rsp) throws CardException {
				byte[] commandBytes = new byte[cmd.remaining()];
		        cmd.get(commandBytes);
		        cmd.position(0);
		        
				System.err.println("B>> " + card.getProtocol() + " (" + commandBytes.length + ") " + encodeHexString(commandBytes));
				System.err.flush();
				int response = channel.transmit(cmd, rsp);
				byte[] responseBytes = new byte[response];
				rsp.get(responseBytes);
				rsp.position(0);
				System.err.println("B<< (" + responseBytes.length + ") " + encodeHexString(responseBytes));
				System.err.flush();
				return response;
			}		
		}
	}
}
