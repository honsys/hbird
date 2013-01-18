package org.hbird.transport.protocols.kiss;

import org.apache.commons.lang.ArrayUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * See standard: http://www.ka9q.net/papers/kiss.html
 * 
 * @author Mark Doyle
 * @author Johannes Klug
 * 
 */
public class KissFrameDecoder extends CumulativeProtocolDecoder {
	private static final Logger LOG = LoggerFactory.getLogger(KissFrameDecoder.class);

	private static final int LOW_NIBBLE_MASK = 0x0F;

	/** Frame end */
	public static final byte FEND = (byte) 0xC0;

	/** Frame escape */
	public static final byte FESC = (byte) 0xDB;

	/** Transposed frame end */
	public static final byte TFEND = (byte) 0xDC;

	/** Transposed frame escape */
	public static final byte TFESC = (byte) 0xDD;

	/**
	 * The id of the data frame TNC type. The rest of the frame is data to be sent on the HDLC channel.
	 */
	public static final byte DATA_FRAME = (byte) 0;

	/**
	 * The next byte is the transmitter keyup delay in 10 ms units. The default start-up value is 50 (i.e., 500 ms).
	 */
	public static final byte TX_DELAY = (byte) 1;

	/**
	 * The next byte is the persistence parameter, p, scaled to the range 0 - 255 with the following formula: P = p *
	 * 256 - 1
	 * 
	 * The default value is P = 63 (i.e., p = 0.25).
	 */
	public static final byte P = (byte) 2;

	/**
	 * The next byte is the slot interval in 10 ms units. The default is 10 (i.e., 100ms).
	 */
	public static final byte SLOT_TIME = (byte) 3;

	/**
	 * The next byte is the time to hold up the TX after the FCS has been sent, in 10 ms units. This command is
	 * obsolete, and is included here only for compatibility with some existing implementations.
	 */
	public static final byte TX_TAIL = (byte) 4;

	/**
	 * The next byte is 0 for half duplex, nonzero for full duplex. The default is 0 (i.e., half duplex).
	 */
	public static final byte FULL_DUPLEX = (byte) 5;

	/**
	 * Specific for each TNC. In the TNC-1, this command sets the modem speed. Other implementations may use this
	 * function for other hardware-specific functions.
	 */
	public static final byte SET_HARDWARE = (byte) 6;

	/**
	 * Exit KISS and return control to a higher-level program. This is useful only when KISS is incorporated into the
	 * TNC along with other applications.
	 */
	public static final byte RETURN = (byte) 0xFF;

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		boolean state = true;

		byte first = in.get();

		// If we have a FEND we are ready to go
		if (first == FEND) {
			byte type = in.get();

			// If the type is RETURN we need to get out of here.
			if (type == RETURN) {
				// exit KISS
				state = false; // TODO Check this.
			}
			else {
				if (type == FEND) {
					// drop second FEND as this could be a sync flush
					type = in.get();
				}

				// The type indicator is split across two nibbles so we need to get those first...
				byte commandType = (byte) (type & LOW_NIBBLE_MASK);

				@SuppressWarnings("unused")
				// Not implemented send to port yet
				byte port = (byte) ((type >> 4) & LOW_NIBBLE_MASK);

				// Now we can perform the correct logic given the type.
				switch (commandType) {
					case DATA_FRAME:
						state = handleDataFrame(in, out);
						break;
					case TX_DELAY:
						break;
					case P:
						break;
					case SLOT_TIME:
						break;
					case TX_TAIL:
						break;
					case FULL_DUPLEX:
						break;
					case SET_HARDWARE:
						break;
					default:
						// Corruption
						state = false;
						break;
				}
			}
		}

		return state;
	}

	/**
	 * Writes out the data portion of the frame.
	 * 
	 * @param in
	 * @param out
	 * @return
	 */
	private static boolean handleDataFrame(IoBuffer in, ProtocolDecoderOutput out) {
		// FIXME handle transposed ends
		byte[] data = ArrayUtils.EMPTY_BYTE_ARRAY;
		byte next = (byte) 0x00;
		while ((next = in.get()) != FEND) {
			if (next == FESC) {
				// escape mode active
				next = in.get();
				if (next == TFEND) {
					next = FEND;
				}
				else if (next == TFESC) {
					next = FESC;
				}
				else {
					// escaped mode error; no action is taken and frame assembly continues
					LOG.warn("Unexpected byte 0x" + Integer.toHexString(next & 0xFF)
							+ " after escape in KISS TNC frame. Expected TFESC or TFEND. Ignoring escape.");
				}
			}
			data = ArrayUtils.add(data, next);
		}

		// The next byte is now FEND so we have the full data payload and can write it out.
		out.write(data);

		return false;
	}
}
