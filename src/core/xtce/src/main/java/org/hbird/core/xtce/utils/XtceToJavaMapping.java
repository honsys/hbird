package org.hbird.core.xtce.utils;

import org.hbird.core.generatedcode.xtce.IntegerParameterType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class XtceToJavaMapping {
	private static final Logger LOG = LoggerFactory.getLogger(XtceToJavaMapping.class);

	private XtceToJavaMapping() {
		// Utility class
	}

	public final static boolean doesXtceIntRequireJavaLong(final IntegerParameterType type) {
		boolean longRequired = false;
		// If signed
		if (type.getSigned()) {
			if (type.getSizeInBits() > 32) {
				longRequired = true;
			}
		}
		// else if unsigned
		else {
			if (type.getSizeInBits() > 31) {
				longRequired = true;
			}
		}

		if(LOG.isDebugEnabled()) {
			LOG.debug("Type " + type.getName() + " returns " + longRequired + " for long requried");
		}
		return longRequired;
	}
}
