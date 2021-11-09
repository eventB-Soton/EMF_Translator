/*******************************************************************************
 * Copyright (c) 2014, 2016 University of Southampton.
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

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import ac.soton.emf.translator.TranslationDescriptor;


/**
 * a simple abstract version of a rule that is always enabled, dependencies are ok, and adds translated elements to 
 * the target Component. Clients may extend this rule to adopt this default behaviour.
 * 
 * @author cfs
 *
 */
public abstract class AbstractRule implements IRule {

	
	@Override
	public boolean enabled(final EObject sourceElement) throws Exception  {
		return true;
	}

	@Override
	public List<TranslationDescriptor> fire(EObject sourceElement, List<TranslationDescriptor> translatedElements) throws Exception {
		assert(enabled(sourceElement));
		return Collections.emptyList();
	}
	
	@Override
	public boolean dependenciesOK(EObject sourceElement, final List<TranslationDescriptor> translatedElements) throws Exception  {
		return true;
	}

		
//	@Override
//	public EventBNamedCommentedComponentElement getTargetEventBComponent(EventBNamedCommentedComponentElement sourceComponent, EventBElement sourceElement) throws Exception  {
//		return sourceComponent;
//	}
	
	@Override
	public boolean fireLate() {
		return false;
	}

}
