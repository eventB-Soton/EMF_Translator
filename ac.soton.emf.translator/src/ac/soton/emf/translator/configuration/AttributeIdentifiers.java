/*******************************************************************************
 * Copyright (c) 2020 University of Southampton.
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

package ac.soton.emf.translator.configuration;

import org.eclipse.osgi.util.NLS;

/**
 * Attribute identifiers used by the translator.
 * Note that the values of these attribute identifiers must be declared to Rodin as they are keys to the types persisted in Rodin
 * @see /ac.soton.emf.translator/plugin.xml
 * @see /ac.soton.emf.translator/src/ac/soton/emf/translator/configuration/attributeIdentifiers.properties
 * 
 * @since 4.0
 */
public class AttributeIdentifiers extends NLS {
	private static final String BUNDLE_NAME = "ac.soton.emf.translator.configuration.attributeIdentifiers"; //$NON-NLS-1$
	public static String TRANSLATOR__TRANSLATION_ID_KEY;
	public static String TRANSLATOR__SOURCE_ELEMENT_KEY;	
	public static String TRANSLATOR__PLACEMENT_PRIORITY_KEY;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, AttributeIdentifiers.class);
	}

	private AttributeIdentifiers() {
	}

}
