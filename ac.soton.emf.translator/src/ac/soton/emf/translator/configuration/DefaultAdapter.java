/*******************************************************************************
 * Copyright (c) 2014, 2018 University of Southampton.
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import ac.soton.emf.translator.TranslationDescriptor;

/**
 * This implementation of IAdapter is a default for translation to an EMF other than Event-B
 * for match it uses the java object equals method, for other methods 
 * it does the minimum by returning false or doing nothing
 * 
 * @author cfs
 *
 */

public class DefaultAdapter implements IAdapter {


	/**
	 * do nothing
	 * @see ac.soton.emf.translator.configuration.IAdapter#initialiseAdapter(java.lang.Object)
	 */
	@Override
	public void initialiseAdapter(Object sourceElement) {		
	}
	
	/**
	 * return true if feature is null
	 */
	public boolean isRoot(TranslationDescriptor translationDescriptor) {
		return translationDescriptor.feature==null;
	}
	
	/**
	 * return null
	 */
	@Override
	public URI getComponentURI(TranslationDescriptor translationDescriptor, EObject rootElement) {
		return null;
	}
	
	/**
	 * This default implementation returns all EMF resources in the same project as the source element
	 * 
	 * @param editingDomain
	 * @param sourceElement
	 * @return list of affected Resources
	 */
	@Override
	public Collection<Resource> getAffectedResources(TransactionalEditingDomain editingDomain, EObject sourceElement) throws IOException {
		List<Resource> affectedResources = new ArrayList<Resource>();
		//affectedResources.add(sourceElement.eResource());
		String projectName = EcoreUtil.getURI(sourceElement).segment(1);
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (project.exists()){
			try {
				IResource[] members = project.members();
				ResourceSet resourceSet = editingDomain.getResourceSet();
				for (IResource res : members){
					final URI fileURI = URI.createPlatformResourceURI(projectName + "/" + res.getName(), true);
					Registry registry = Resource.Factory.Registry.INSTANCE;
					if (registry.getExtensionToFactoryMap().containsKey(fileURI.fileExtension())){
						Resource resource = resourceSet.getResource(fileURI, false);
						if (resource != null) {
							if (!resource.isLoaded()) {
								resource.load(Collections.emptyMap());
							}
							if (resource.isLoaded()) {
								affectedResources.add(resource);
							} 
						}
					}
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return affectedResources;
	}
		
	/**
	 * This implementation always returns true (i.e. do not filter)
	 * 
	 * @see ac.soton.emf.translator.configuration.IAdapter#inputFilter(java.lang.Object)
	 */
	@Override
	public boolean inputFilter(Object object, Object sourceID) {
		return true;
	}
	
	/**
	 * This implementation always returns true (i.e. do not filter)
	 * 
	 * @see ac.soton.emf.translator.configuration.IAdapter#outputFilter(java.lang.Object)
	 */
	@Override
	public boolean outputFilter(TranslationDescriptor translationDescriptor) {
		return true;
	}


	/**
	 * match
	 * test whether two elements are equal using the equals method of el1
	 * (or both null if el1 is null)
	 */
	
	@Override
	public boolean match(Object el1, Object el2) {
		return el1==null? el2==null : el1.equals(el2);
	}


	/**
	 * returns null
	 */
	@Override
	public Object getSourceId(Object object){
		return null;
	}

	/**
	 * do nothing
	 */
	@Override
	public void annotateTarget(Object sourceID, Object object) {
	}
	
	/**
	 * returns false
	 */
	@Override
	public boolean isAnnotatedWith(Object object, Object sourceID) {
		return false;
	}
	
	/**
	 * do nothing
	 */
	@Override
	public void setPriority(int priority, Object object) {
	}

	/** 
	 * returns the end of the list
	 */
	@Override
	public int getPos(List<?> list, Object object) {
		return list.size();
	}


	
}
