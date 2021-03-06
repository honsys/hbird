/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.hbird.application.halcyon;

import java.util.HashMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.hbird.application.halcyon.branding.Branding;
import org.hbird.application.halcyon.branding.BrandingPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

/**
 * Links the servlet context to the OSGI event services; it simply registers OSGi events when
 * the servlet context calls back on the listener. since it's a {@link ServletContextListener} this happens when the
 * context is initialised and destroyed.
 * 
 * @author Mark Doyle
 * 
 */
public class HalcyonServletContextListener implements BundleActivator, ServletContextListener {

	private static BrandingPlugin brandingPlugin;

	private static EventAdmin eventAdmin;

	private static BundleContext bundleContext;

	ServiceReference eventAdminServiceRef;

	synchronized static EventAdmin getEventAdmin() {
		return eventAdmin;
	}

	synchronized static void setEventAdmin(final EventAdmin eventAdmin) {
		HalcyonServletContextListener.eventAdmin = eventAdmin;
	}

	@Override
	public void contextInitialized(final ServletContextEvent sce) {
		if (getEventAdmin() != null) {
			getEventAdmin().sendEvent(new Event("jersey/halcyon/DEPLOYED", new HashMap<String, String>() {
				private static final long serialVersionUID = 1L;
				{
					put("context-path", sce.getServletContext().getContextPath());
				}
			}));
		}
	}

	@Override
	public void contextDestroyed(final ServletContextEvent sce) {
		if (getEventAdmin() != null) {
			getEventAdmin().sendEvent(new Event("jersey/halcyon/UNDEPLOYED", new HashMap<String, String>() {
				private static final long serialVersionUID = 1L;
				{
					put("context-path", sce.getServletContext().getContextPath());
				}
			}));
		}
	}

	@Override
	public void start(final BundleContext context) throws Exception {
		HalcyonServletContextListener.bundleContext = context;
		eventAdminServiceRef = bundleContext.getServiceReference(EventAdmin.class.getName());
		if (eventAdminServiceRef != null) {
			setEventAdmin((EventAdmin) bundleContext.getService(eventAdminServiceRef));
		}

		// Load branding plugins
		Branding brandingLoader = new Branding();
		brandingLoader.loadProperties();
		HalcyonServletContextListener.brandingPlugin = brandingLoader;
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		if (eventAdminServiceRef != null) {
			setEventAdmin(null);
			bundleContext.ungetService(eventAdminServiceRef);
		}
	}

	public static BundleContext getBundleContext() {
		return HalcyonServletContextListener.bundleContext;
	}

	public static BrandingPlugin getBrandingPlugin() {
		return brandingPlugin;
	}

}
