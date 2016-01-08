/*******************************************************************************
 *  Copyright (c) 2015 University of Southampton.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *   
 *  Contributors:
 *  University of Southampton - Initial implementation
 *******************************************************************************/
package ac.soton.emf.translator.impl;

import org.eclipse.osgi.util.NLS;

public class Identifiers extends NLS {
	private static final String BUNDLE_NAME = "ac.soton.emf.translator.impl.identifiers"; //$NON-NLS-1$

	public static String EXTPT_RULE_ID;
	public static String EXTPT_RULE_ROOTSOURCECLASS;
	public static String EXTPT_RULE_TRANSLATORID;
	public static String EXTPT_RULE_SOURCEPACKAGE;
	public static String EXTPT_RULE_ADAPTERCLASS;
	public static String EXTPT_RULE_RULE;
	public static String EXTPT_RULE_RULECLASS;
	public static String EXTPT_RULE_SOURCECLASS;

	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Identifiers.class);
	}

	private Identifiers() {
	}
}
