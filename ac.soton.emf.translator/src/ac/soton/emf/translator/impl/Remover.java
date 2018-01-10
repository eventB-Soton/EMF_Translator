/*******************************************************************************
 *  Copyright (c) 2015-2017 University of Southampton.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *   
 *  Contributors:
 *  University of Southampton - Initial implementation
 *******************************************************************************/
package ac.soton.emf.translator.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import ac.soton.emf.translator.configuration.IAdapter;
import ac.soton.emf.translator.utils.Find;

public class Remover {

	private final Collection<Resource> resources;
	private final Object sourceID;
	private final IAdapter adapter;
	
	private List<Resource> modifiedResources;
	
	public Remover(final Collection<Resource> affectedResources, Object sourceID, IAdapter adapter) {
		this.resources = affectedResources;
		this.sourceID = sourceID;
		this.adapter = adapter;
	}
	
	public boolean canExecute(){
		return resources!=null && sourceID!=null ;
	}	
	
	public List<Resource> removeTranslated(){
		modifiedResources = new ArrayList<Resource>();
		if (canExecute()){
			for (Resource res : resources){
				if (res!=null && !res.getContents().isEmpty()) {
					EObject component = res.getContents().get(0);
					if (component instanceof EObject){
						List<EObject> previouslyTranslatedElements = getPreviouslyTranslatedElements(component);
						for (EObject eObject : previouslyTranslatedElements){
							EcoreUtil.delete(eObject, true);	//this deletes the object from its containment and removes all references to it and its content
							if (!modifiedResources.contains(res)) modifiedResources.add(res);
						}
					}
				}
			}
		}
		return modifiedResources;
	}
	
	/*
	 * finds all elements that have previously been translated from this translations source
	 * 
	 * As a side effect, updates the list of the modified resources
	 * 
	 * @return List of elements
	 */
	private ArrayList<EObject> getPreviouslyTranslatedElements(final EObject root) {
		List<EObject> contents = Find.eAllContents(root, EcorePackage.Literals.EOBJECT);
		contents.remove(null);
		contents.add(0,root);
		ArrayList<EObject> remove = new ArrayList<EObject>();
		for(EObject eObject : contents){
			EClass eClass = eObject.eClass();
			EList<EReference> refs = eClass.getEReferences();
			for (EReference ref : refs){
				if (!ref.isContainment()){
					Object target = eObject.eGet(ref);
					if (target instanceof EObject && ((EObject)target).eResource()!=null &&
							!((EObject)target).eResource().equals(root.eResource())){
						remove.addAll(getPreviouslyTranslatedElements((EObject) target));
					}
				}
			}

			if (adapter.isAnnotatedWith(eObject,sourceID)){
				remove.add(eObject);
				if(!modifiedResources.contains(root.eResource())){
					modifiedResources.add(root.eResource());
				}
			}
		}
		return remove;
	}
	
}
