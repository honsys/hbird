package org.hbird.core.commons.data;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

public class GenericPayload implements Serializable {
	private static final long serialVersionUID = 7823269614387628654L;

	public byte[] payload;
	public String layoutIdentifier;
	public long timeStamp;

	public GenericPayload(final byte[] payload, final String layoutIdentifier, final long timeStamp) {
		this.payload = ArrayUtils.clone(payload);
		this.layoutIdentifier = layoutIdentifier;
		this.timeStamp = timeStamp;
	}

	public byte[] getPayload() {
		return payload;
	}

	public String getLayoutIdentifier() {
		return layoutIdentifier;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

}
