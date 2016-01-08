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
package ac.soton.emf.translator;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * Clients should implement this interface to define Rules for their translator
 * 
 * @author cfs
 *
 */

public interface IRule {
	
	/**
	 * This should return a boolean to indicate whether or not the rule is applicable for the given source element
	 * 
	 * @param sourceElement
	 * @return
	 */
	public boolean enabled (final EObject sourceElement) throws Exception;
	
	/**
	 * This does the translation from the given source element producing a List of translated elements.
	 * If there are no translated elements an empty list should be returned. 
	 * Return null only if the rule is fired when dependencies are not ok.
	 * Throw an exception if this rule is fired when it is not enabled.
	 * 
	 * [Note that The translator will set translated properties and translator ID attributes for all translated elements. It
	 * is not necessary for rules to do this].
	 * 
	 * @param sourceElement
	 * @return
	 * @throws Exception 
	 */
	public List<TranslationDescriptor> fire(EObject sourceElement, List<TranslationDescriptor> translatedElements) throws Exception;
	
//	/**
//	 * This returns the EventB component (i.e. a particular Machine or Context etc.) where the translated elements
//	 * should be added.
//	 * 
//	 * CURRENTLY NOT USED
//	 * 
//	 * @param sourceComponent
//	 * @param sourceElement
//	 * @return
//	 */
//	public EventBNamedCommentedComponentElement getTargetEventBComponent(EventBNamedCommentedComponentElement sourceComponent, EventBElement sourceElement) throws Exception;

	/**
	 * This should return a boolean to indicate whether dependencies needed by this source Element/Rule combination
	 * have already been translated. If not the rule will be tried again later.
	 * N.b. Do not include conditions here, only implement ordering dependencies which will at some point be true.
	 * If the dependency does not become true during the translation, the translation has failed and is not saved. 
	 * 
	 * @param sourceElement
	 * @param translatedElements
	 * @return
	 * @throws Exception
	 */
	boolean dependenciesOK(EObject sourceElement,List<TranslationDescriptor> translatedElements) throws Exception;

	/**
	 * This should return a boolean to indicate whether the rule should be fired late.
	 * Late rules will not be fired until no progress is being made firing non-late rules.
	 * Late rules are usually used when some translation of elements is conditional on whether other rules have translated elements.
	 * (This is different from dependencies which is un-conditional but requires order)
	 * 
	 */
	boolean fireLate();
	
}
