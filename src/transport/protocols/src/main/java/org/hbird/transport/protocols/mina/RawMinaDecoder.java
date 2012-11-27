package org.hbird.transport.protocols.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class RawMinaDecoder extends CumulativeProtocolDecoder {

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		System.out.println("doDecode2");
		System.out.println("position = " + in.position());
		byte[] bytesIn = new byte[in.limit()];
		in.get(bytesIn);
		out.write(bytesIn);
		return false;
	}

}