package org.hbird.transport.xtce;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hbird.transport.spacesystemmodel.SpaceSystemModel;
import org.hbird.transport.spacesystemmodel.exceptions.UnknownParameterException;
import org.hbird.transport.spacesystemmodel.exceptions.UnknownParameterGroupException;
import org.hbird.transport.spacesystemmodel.parameters.Parameter;
import org.hbird.transport.spacesystemmodel.tmtcgroups.ParameterGroup;

/**
 *
 * @author Mark Doyle
 * @author Johannes Klug
 *
 */
public class XtceSpaceSystemModel implements SpaceSystemModel {
	private static final long serialVersionUID = 2532805548202927668L;
//	private static final Logger LOG = LoggerFactory.getLogger(XtceSpaceSystemModel.class);

	private final Map<String, ParameterGroup> parameterGroups = new HashMap<>();

	private final Map<String, List<Object>> restrictions = new HashMap<>();

	public XtceSpaceSystemModel() {
	}

	@Override
	public Collection<ParameterGroup> getAllParameterGroups() {
		return parameterGroups.values();
	}

	@Override
	public ParameterGroup getParameterGroup(final String qualifiedName) throws UnknownParameterGroupException {
		final ParameterGroup container = parameterGroups.get(qualifiedName);

		if (container == null) {
			throw new UnknownParameterGroupException(parameterGroups, "Your container lookup for '" + qualifiedName
					+ "' did not return any containers. Check your SpaceSystem configuration.");
		}

		return container;
	}

	/**
	 * Iterates over all the parameter groups and creates a list of all their parameters.
	 *
	 * @throws UnknownParameterException
	 */
	@Override
	public Map<String, Parameter<?>> getAllParameters() {
		Map<String, Parameter<?>> allParameters = new HashMap<>();
		for (ParameterGroup pg : this.parameterGroups.values()) {
			for (String parameterId : pg.getAllParameters().keySet()) {
				allParameters.put(parameterId, pg.getAllParameters().get(parameterId));
			}
		}
		return allParameters;
	}

	@Override
	public Parameter<?> getParameter(final String qualifiedName) throws UnknownParameterException {
		for (ParameterGroup pg : this.parameterGroups.values()) {
			if (pg.getAllParameters().containsKey(qualifiedName)) {
				return pg.getParameter(qualifiedName);
			}
		}
		throw new UnknownParameterException(qualifiedName);
	}

	@Override
	public Parameter<Integer> getIntParameter(final String qualifiedName) throws UnknownParameterException {
		for (ParameterGroup pg : this.parameterGroups.values()) {
			if (pg.getIntegerParameters().containsKey(qualifiedName)) {
				return pg.getIntegerParameter(qualifiedName);
			}
		}
		throw new UnknownParameterException(qualifiedName);
	}

	@Override
	public Parameter<Long> getLongParameter(final String qualifiedName) throws UnknownParameterException {
		for (ParameterGroup pg : this.parameterGroups.values()) {
			if (pg.getLongParameters().containsKey(qualifiedName)) {
				return pg.getLongParameter(qualifiedName);
			}
		}
		throw new UnknownParameterException(qualifiedName);
	}

	@Override
	public Map<String, List<Object>> getAllPayloadRestrictions() {
		return restrictions;
	}

	@Override
	public void replaceParameterInModel(final String qualifiedName, final Parameter<?> newParameter) throws UnknownParameterException {
		for (ParameterGroup pg : this.parameterGroups.values()) {
			if(pg.getAllParameters().put(qualifiedName, newParameter) != null) {
				// Parameter replaced, no need to iterate over the rest of the parameters.
				break;
			}
		}
		throw new UnknownParameterException(newParameter.getName());
	}

	@Override
	public Map<String, Parameter<Integer>> getAllIntegerParameters() {
		Map<String, Parameter<Integer>> allParameters = new HashMap<>();
		for (ParameterGroup pg : this.parameterGroups.values()) {
			for (String qualifiedName : pg.getIntegerParameters().keySet()) {
				allParameters.put(qualifiedName, pg.getIntegerParameters().get(qualifiedName));
			}
		}
		return allParameters;
	}

	@Override
	public Map<String, Parameter<Long>> getAllLongParameters() {
		Map<String, Parameter<Long>> allParameters = new HashMap<>();
		for (ParameterGroup pg : this.parameterGroups.values()) {
			for (String qualifiedName : pg.getLongParameters().keySet()) {
				allParameters.put(qualifiedName, pg.getLongParameters().get(qualifiedName));
			}
		}
		return allParameters;
	}


	@Override
	public Map<String, Parameter<BigDecimal>> getAllBigDecimalParameters() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, Parameter<Float>> getAllFloatParameters() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, Parameter<Double>> getAllDoubleParameters() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, Parameter<String>> getAllStringParameters() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, Parameter<Byte[]>> getAllRawParameters() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Parameter<BigDecimal> getBigDecimalParameter(final String qualifiedName) throws UnknownParameterException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Parameter<String> getStringParameter(final String qualifiedName) throws UnknownParameterException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Parameter<Float> getFloatParameter(final String qualifiedName) throws UnknownParameterException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Parameter<Double> getDoubleParameter(final String qualifiedName) throws UnknownParameterException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Parameter<Byte[]> getRawParameter(final String qualifiedName) throws UnknownParameterException {
		throw new UnsupportedOperationException();
	}

}