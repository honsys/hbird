package org.hbird.transport.payloadcodec.codecparameters;

import java.util.BitSet;

import org.hbird.transport.spacesystemmodel.encoding.Encoding;
import org.hbird.transport.spacesystemmodel.parameters.Parameter;

/**
 * Parameter Decorator.
 *
 * @author Mark Doyle
 *
 * @param <T>
 */
public abstract class CodecParameter<T> implements Parameter<T> {
	private static final long serialVersionUID = 6597747873295079865L;

	protected Parameter<T> parameter;

	protected Encoding encoding;

	public CodecParameter(final Parameter<T> hostParameter, final Encoding encoding) {
		this.parameter = hostParameter;
		this.encoding = encoding;
	}

	public abstract void decode(byte[] inBytes, int offset);

	public abstract void decode(BitSet inBitset, int offset);

	public abstract Byte[] encodeToByteArray(Byte[] targetBytes, int offset);

	public abstract BitSet encodeToBitSet(BitSet targetBitSet, int offset);

	// Pass through methods which the Codec Parameter does not need to alter in it's decoration.
	// ----------------------------------------------------------------------------------------

	@Override
	public String getQualifiedName() {
		return parameter.getQualifiedName();
	}

	@Override
	public String getName() {
		return parameter.getName();
	}

	@Override
	public String getShortDescription() {
		return parameter.getShortDescription();
	}

	@Override
	public String getLongDescription() {
		return parameter.getLongDescription();
	}

	@Override
	public T getValue() {
		return parameter.getValue();
	}

	@Override
	public void setValue(final T value) {
		parameter.setValue(value);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CodecParameter [parameter=");
		builder.append(parameter);
		builder.append("]");
		return builder.toString();
	}

	public void setEncoding(final Encoding encoding) {
		this.encoding = encoding;
	}

}
