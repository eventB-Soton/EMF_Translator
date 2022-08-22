/*******************************************************************************
 * Copyright (c) 2014, 2020 University of Southampton.
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

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import ac.soton.emf.translator.TranslationDescriptor;

public interface IAdapter {

	/**
	 * If the adapter needs to store translation wide state it can be set up here
	 * This method will be called at the start of each translation with the source element of
	 * the translation
	 * @param sourceElement
	 */
	void initialiseAdapter(Object sourceElement);
	
	/**
	 * returns true if the translation descriptor describes a new root level element
	 * @param translationDescriptor
	 * @return
	 * @since 3.0
	 */
	boolean isRoot(TranslationDescriptor translationDescriptor);
	
	/**
	 * 
	 * This should return the resource URI to be used in creating the new resource.
	 * The root element of this translation is passed in case it is needed to construct the URI. (E.g. to find 
	 * the containing project.
	 * 
	 * @param translationDescriptor
	 * @param rootElement
	 * @return
	 */
	URI getComponentURI(TranslationDescriptor translationDescriptor, EObject rootElement);

	/**
	 * This should return a collection of potentially affected resources
	 * The resources should all be loaded in the resource set of the given editing domain.
	 * 
	 * 
	 * N.B. CURRENTLY ALL RESOURCES ARE ASSUMED TO BE WITHIN THE SAME PROJECT AS THE SOURCE ELEMENT. 
	 * (i.e. translationDescriptor.parent is ignored when adding new root level elements)
	 * 
	 * @param editingDomain
	 * @param sourceElement
	 * @return a list of affected Resources
	 * @since 3.0
	 */
	Collection<Resource> getAffectedResources(TransactionalEditingDomain editingDomain, EObject sourceElement) throws IOException ;
		
	/**
	 * Filters out any source elements that should not be translated.
	 * @param translatorConfig 
	 *
	 * @param element
	 * @return true if this object should be translated and false if it should be filtered out (ignored)
	 */
	boolean inputFilter(Object object, Object sourceID);
	
	/**
	 * Filters out any translationDescriptors that should not be acted upon.
	 * This may be because a child is already visible via extension of the refined parent
	 *
	 * @param translationDescriptor
	 * @return true if this translation descriptor should be acted upon and false if it should be filtered out (ignored)
	 */
	boolean outputFilter(TranslationDescriptor translationDescriptor);
	
	/**
	 * whether these two objects are considered to be essentially the same thing
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	boolean match(Object obj1, Object obj2);
	
	/**
	 * gets the ID of the given root source element.
	 * This ID will be used to annotate all translated elements.
	 * return null if annotation is not required.
	 * 
	 *  (Note that translated elements with the same ID are deleted at the start of any translation
	 *  hence, if translated elements are not annotated, subsequent translations are likely to cause duplication
	 *  of the translated elements)
	 * 
	 * @param object - the root source element being translated
	 * @return
	 */
	Object getSourceId(Object object);

	/**
	 * Sets the translatedBy attribute of the given object to the given String value
	 * In translator the object will be an EObject for this to succeed.
	 * 
	 * @param sourceID
	 * @param object
	 */
	void annotateTarget(Object sourceID, Object object);

	/**
	 * checks whether the given eObject is annotated with the given sourceID object
	 * 
	 * @param object
	 * @param sourceID
	 * @return
	 */
	boolean isAnnotatedWith(Object object, Object sourceID);
	
	/**
	 * Sets the priority attribute of the given object to the given integer value
	 * In translator the object will be an EObject for this to succeed.
	 * 
	 * @param pri - the integer priority of the given object
	 * @param object - the object to have its priority set
	 */
	void setPriority(int pri, Object object);

	/**
	 * returns the position in the given List feature for the addition of the given new object
	 * @param list - the list in which the given object needs to be placed
	 * @param object - the object to be placed in the list
	 * @return - the position in the list for the object to be placed
	 */
	int getPos(List<?> list, Object object);
	
}
