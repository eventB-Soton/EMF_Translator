/*******************************************************************************
 * Copyright (c) 2014, 2019 University of Southampton.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import ac.soton.emf.translator.configuration.IAdapter;
import ac.soton.emf.translator.utils.Find;

/**
 * @author cfs
 *
 */
public class Remover {

	private final Collection<Resource> resources;
	private final String translationID;
	private final IAdapter adapter;
	
	private List<Resource> modifiedResources;
	
	/**
	 * @htson: The set of searched EObjects. This is used in
	 *         {@link #getPreviouslyTranslatedElements()} to stop recursion in the cases where
	 *         cyclic references between EObjects presents.
	 */
	private Set<EObject> searchedEObjects;
	
	public Remover(final Collection<Resource> affectedResources, String translationID, IAdapter adapter) {
		this.resources = affectedResources;
		this.translationID = translationID;
		this.adapter = adapter;
	}
	
	public boolean canExecute(){
		return resources!=null && translationID!=null ;
	}	
	
	public List<Resource> removeTranslated(){
		modifiedResources = new ArrayList<Resource>();
		if (canExecute()){
			for (Resource res : resources){
				if (res!=null && !res.getContents().isEmpty()) {
					EObject component = res.getContents().get(0);
					if (component instanceof EObject){
						// @htson: Create the (empty) set of searched EObjects
						searchedEObjects = new HashSet<EObject>();
						List<EObject> previouslyTranslatedElements = getPreviouslyTranslatedElements(component);
						for (EObject eObject : previouslyTranslatedElements){
							EcoreUtil.delete(eObject, true);	//this deletes the object from its containment and removes all references to it and its content
							if (!modifiedResources.contains(res))
								modifiedResources.add(res);
						}
					}
				}
			}
		}
		return modifiedResources;
	}
	
	/**
	 * finds all elements that have previously been translated from this translations source
	 * 
	 * As a side effect, updates the list of the modified resources
	 * 
	 * @return List of elements
	 */
	private ArrayList<EObject> getPreviouslyTranslatedElements(final EObject root) {
		ArrayList<EObject> remove = new ArrayList<EObject>();

		// @htson: Returns (an empty list) if this EObject has been searched before.
		if (searchedEObjects.contains(root)) {
			return remove;
		}
		List<EObject> contents = Find.eAllContents(root, EcorePackage.Literals.EOBJECT);
		contents.remove(null);
		contents.add(0,root);

		// @htson: Add all the contents of the root EObject to the set of searched objects. 
		searchedEObjects.addAll(contents);
		for(EObject eObject : contents){
			
			//also check elements that are referenced in other resources
			// (uses recursion)
			EClass eClass = eObject.eClass();
			EList<EReference> refs = eClass.getEReferences();
			for (EReference ref : refs){
				if (!ref.isContainment()){
					Object target = eObject.eGet(ref);
					if (target instanceof EObject && 
							((EObject)target).eResource()!=null &&
							!((EObject)target).eResource().equals(root.eResource())){
						remove.addAll(getPreviouslyTranslatedElements((EObject) target));
					}
				}
			}

			//if this element is annotated with the sourceID
			// remove it
			if (adapter.wasGeneratedBy(eObject,translationID)){
				remove.add(eObject);
				if(!modifiedResources.contains(root.eResource())){
					modifiedResources.add(root.eResource());
				}
			}
			
		}
		return remove;
	}
	
}
