package org.hbird.osgi.xtceosgi;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.reset;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.hbird.core.spacesystemmodel.interfaces.SpaceSystemModelUpdateListener;
import org.hbird.core.xtce.XtceSpaceSystemModelFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;
import org.osgi.service.cm.ConfigurationException;

@RunWith(MockitoJUnitRunner.class)
public class XtceManagedConfigUpdaterTest {

	private XtceManagedConfigUpdater updater;

	@Mock
	private XtceSpaceSystemModelFactory mockFactory;

	@Mock
	private SpaceSystemModelUpdateListener mockListener;

	private List<SpaceSystemModelUpdateListener> mockUpdateListeners;

	private final static String TEST_NAME = "SpaceshipAce";

	@SuppressWarnings("rawtypes") // OSGi supports 1.4 and therefore passes untyped classes instead of generics.
	private final Dictionary config = new Hashtable(1);

	private static final String SPACE_SYSTEM_MODLE_FILENAME_FIELD = "spaceSystemModelFilename";

	@Before
	public void setup() {
		updater = new XtceManagedConfigUpdater();

		updater.setFactory(mockFactory);

		mockUpdateListeners = new ArrayList<SpaceSystemModelUpdateListener>(1);
		mockUpdateListeners.add(mockListener);
		updater.setModelUpdateListeners(mockUpdateListeners);
	}

	@Test
	public void testSetSpaceSystemModelFilename() {
		updater.setSpaceSystemModelFilename(TEST_NAME);
		verify(mockFactory, times(1)).setSpaceSystemModelFilename(TEST_NAME);
	}

	@SuppressWarnings("unchecked") // OSGi supports 1.4 and therefore passes untyped classes instead of generics.
	@Test
	public void testUpdated() throws ConfigurationException {
		config.put(SPACE_SYSTEM_MODLE_FILENAME_FIELD, TEST_NAME);

		updater.setModelUpdateListeners(mockUpdateListeners);

		updater.updated(config);

		verify(mockFactory, times(1)).setSpaceSystemModelFilename(TEST_NAME);
		verify(mockListener, times(1)).modelChanged();
	}

}
