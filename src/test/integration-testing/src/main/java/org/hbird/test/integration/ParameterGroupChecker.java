package org.hbird.test.integration;

import org.hbird.transport.spacesystemmodel.parameters.Parameter;
import org.hbird.transport.spacesystemmodel.tmtcgroups.ParameterGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParameterGroupChecker {

	private static final Logger LOG = LoggerFactory.getLogger(ParameterGroupChecker.class);
	public static void checkParameterGroup(ParameterGroup pg) {
		if (pg == null) {
			LOG.error("Received a null pg.");
			System.exit(1);
		}
		for (Parameter<?> parameter : pg.getAllParameters().values()) {
			LOG.debug(parameter.getName() + ": " + parameter.getValue());
		}
		
	}
}