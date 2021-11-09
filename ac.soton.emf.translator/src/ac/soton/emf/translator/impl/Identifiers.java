/*******************************************************************************
 * Copyright (c) 2014, 2017 University of Southampton.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    University of Southampton - initial API and implementation
 *******************************************************************************/
package ac.soton.emf.translator.impl;

import org.eclipse.osgi.util.NLS;

public class Identifiers extends NLS {
	private static final String BUNDLE_NAME = "ac.soton.emf.translator.impl.identifiers"; //$NON-NLS-1$

	public static String EXTPT_TRANSLATORS_EXTPTID;
	public static String EXTPT_TRANSLATORS_TRANSLATOR_TRANSLATORID;
	public static String EXTPT_TRANSLATORS_TRANSLATOR_ROOTSOURCECLASS;
	public static String EXTPT_TRANSLATORS_TRANSLATOR_COMMANDID;
	public static String EXTPT_TRANSLATORS_TRANSLATOR_SOURCEPACKAGE;
	public static String EXTPT_TRANSLATORS_TRANSLATOR_ADAPTERCLASS;
	public static String EXTPT_TRANSLATORS_TRANSLATOR_SELFMODIFYING;

	public static String EXTPT_RULESETS_EXTPTID;
	public static String EXTPT_RULESETS_RULESET_TRANSLATORID;
	public static String EXTPT_RULESETS_RULESET_RULE;
	public static String EXTPT_RULESETS_RULESET_RULE_RULECLASS;
	public static String EXTPT_RULESETS_RULESET_RULE_SOURCECLASS;
	public static String EXTPT_RULESETS_RULESET_RULE_SOURCEPACKAGE;

	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Identifiers.class);
	}

	private Identifiers() {
	}
}
