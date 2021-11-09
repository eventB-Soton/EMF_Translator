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
package ac.soton.emf.translator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;


	/**
	 * A Descriptor for translated model changes. 
	 * The feature of the parent will be changed in the following ways:
	 * 
	 * 	If remove is false:
	 * 1) If the feature is a containment and the value is an element of the correct kind, the 
	 *    value will be added to the containment in a position according to the priority
	 * 2) If the feature is a reference and the value is an element of the correct kind, the 
	 *    value will be added to the reference in a position according to the priority
	 * 3) If the feature is an EAttribute and the value is of the correct type, the 
	 *    feature will be set to the value
	 *    
	 *    priority can be used to control the relative position of the translated elements  
	 *    1 - must come first
	 *    10 - not important
	 *    ---user entered items---
	 *    0 must come after user entered items
	 *    -10 must come last
	 *    
	 *  If remove is true:
	 * 1) If the feature is a containment and the value is an element of the correct kind, the 
	 *    value will be deleted from the containment
	 * 2) If the feature is a reference and the value is an element of the correct kind, the 
	 *    value will be removed from the reference
	 * 3) If the feature is an EAttribute, the 
	 *    feature will be unset 
	 *  
	 *    
	 * @author cfs
	 *
	 */
public class TranslationDescriptor{
	public EObject parent;
	public EStructuralFeature feature;
	public Object value;
	public Integer priority;
	public Boolean remove;
	
	public TranslationDescriptor(EObject parent, EStructuralFeature feature, Object value, Integer priority){
		this.parent = parent; this.feature = feature; this.value = value; this.priority = priority; this.remove = false;
	}
	
	public TranslationDescriptor(EObject parent, EStructuralFeature feature, Object value){
		this.parent = parent; this.feature = feature; this.value = value; this.priority = 0; this.remove = false;
	}

	public TranslationDescriptor(EObject parent, EStructuralFeature feature, Object value, Integer priority, Boolean remove){
		this(parent, feature, value, priority);
		this.remove = remove;  
	}
	
	public TranslationDescriptor(EObject parent, EStructuralFeature feature, Object value, Boolean remove){
		this(parent, feature, value);
		this.remove = remove;
		
	}
	
}
