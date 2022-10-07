/*******************************************************************************
 * Copyright (c) 2014, 2022 University of Southampton.
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import ac.soton.emf.translator.Activator;
import ac.soton.emf.translator.TranslationDescriptor;
import ac.soton.emf.translator.configuration.IRule;

/**
 * A generic Translator which is configured from rule classes which have been declared in an extension point.
 * The rules are fired (if enabled) and return a collection of descriptors for adding elements to the model
 * or adding references within the model.
 * Rules may be deferred until later if they depend on other rules which have not yet fired.
 * Descriptors may be prioritised to influence the order of elements in collections. 
 * 
 * @author cfs
 *
 */
public class Translator {
	
	//	configuration data for this instance of a translator
	private TranslatorConfig translatorConfig;
	private String translationID;

	
	// VARIABLE DATA
	/*
	 * These mappings are populated from the translation descriptors prior to updating the model
	 * priorities maps from a range of priority numbers (10 down to -10) to a list of new child elements for each priority
	 * parents maps each new element to the parent element that should own it
	 * features maps each new element to the feature of the parent in which it should be contained
	 * 
	 */
	private Map<Integer,List<TranslationDescriptor>> priorities = new HashMap<Integer,List<TranslationDescriptor>>();
	//private Map<EventBObject, EventBObject> trace = new HashMap<EventBObject, EventBObject>();	
	private List<TranslationDescriptor> translatedElements = new ArrayList<TranslationDescriptor>();
	
	
/**
 * Construct a translator. This should only be called from the TranslatorFactory.
 * 	
 * @param translatorConfig	- the configuration for the translator
 */
	
	public Translator(TranslatorConfig translatorConfig){ 	
			this.translatorConfig = translatorConfig;
	}

/**
 * unTranslate - this should be called passing the editing domain and a source element to be un-translated
 * @param editingDomain 
 * @param sourceElement 
 * @throws CoreException 
 */
	public List<Resource> unTranslate (TransactionalEditingDomain editingDomain, final EObject sourceElement) throws CoreException{
	
		List<Resource> modifiedResources = new ArrayList<Resource>();
		try {
			
			//check we have a valid configuration for the translator
			if (translatorConfig==null) {
				throw new CoreException(new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.TRANSLATOR_MSG_01(sourceElement)));
			}
			
			//check we have the correct translator configuration for the source element
			if (sourceElement.eClass() != translatorConfig.rootSourceClass){
				throw new CoreException(new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.TRANSLATOR_MSG_02(sourceElement)));
			}
			
			//initialise the adapter for this translation
			translatorConfig.adapter.initialiseAdapter(sourceElement);
			
			//Obtain an ID from the source element
			translationID = translatorConfig.adapter.getTranslationId(translatorConfig.translatorID, sourceElement);

			Collection<Resource> affectedResources = translatorConfig.adapter.getAffectedResources(editingDomain, sourceElement);
			
			//remove previously generated elements
			Remover remover = new Remover(affectedResources, translationID, translatorConfig.adapter);
			modifiedResources.addAll(remover.removeTranslated());
			
		} catch (Exception e) {
			throw new CoreException(new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.TRANSLATOR_MSG_07 ,e));
		} 
		
		return modifiedResources;
			
	}

/**
 * translate - this should be called passing the editing domain and a source element to be translated
 * @param editingDomain 
 * @param sourceElement 
 * @throws CoreException 
 */
	public List<Resource> translate (TransactionalEditingDomain editingDomain, final EObject sourceElement) throws CoreException{
		
		List<Resource> modifiedResources = new ArrayList<Resource>();
		try {
			
			//check we have a valid configuration for the translator
			if (translatorConfig==null) {
				throw new CoreException(new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.TRANSLATOR_MSG_01(sourceElement)));
			}
			
			//check we have the correct translator configuration for the source element
			if (sourceElement.eClass() != translatorConfig.rootSourceClass){
				throw new CoreException(new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.TRANSLATOR_MSG_02(sourceElement)));
			}
			
			//initialise the adapter for this translation
			translatorConfig.adapter.initialiseAdapter(sourceElement);
			
			//Obtain an ID from the source element
			translationID = translatorConfig.adapter.getTranslationId(translatorConfig.translatorID, sourceElement);
			
			//do the translation
			doGenerate(sourceElement);
			
			//verifyDescriptors;
			for (TranslationDescriptor translationDescriptor : translatedElements){
				if (translationDescriptor.feature!=null &&
						!translationDescriptor.feature.getEType().isInstance(translationDescriptor.value)){
					throw new CoreException(new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.TRANSLATOR_MSG_21(translationDescriptor.value,translationDescriptor.feature)));
				}
				if (translationDescriptor.parent!=null &&
					!translationDescriptor.parent.eClass().getEAllStructuralFeatures().contains(translationDescriptor.feature)){
					throw new CoreException(new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.TRANSLATOR_MSG_22(translationDescriptor.parent, translationDescriptor.feature)));
				}
			}
			
			Collection<Resource> affectedResources = translatorConfig.adapter.getAffectedResources(editingDomain, sourceElement);
			
			//remove previously generated elements
			Remover remover = new Remover(affectedResources, translationID, translatorConfig.adapter);
			modifiedResources.addAll(remover.removeTranslated());
			
			//add new roots to resources
			modifiedResources.addAll(placeRootElements(editingDomain, sourceElement));
			
		} catch (Exception e) {
			throw new CoreException(new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.TRANSLATOR_MSG_07 ,e));
		} 

		//place the newly translated elements in their correct parent features
		//(this is done in a separate post-translation phase and only if the deletion of old translated elements succeeds. 
		// This is so that we do not leave the model in an inconsistent state if the translation fails)
		try {
			modifiedResources.addAll(
					placeGenerated(editingDomain)
					);
			//removeComponents(editingDomain, sourceElement);
		} catch (Exception e) {
			throw new CoreException(new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.TRANSLATOR_MSG_04, e));
		}
				
		return modifiedResources;	
	}
	
//	/**
//	 * Removes components according to the descriptors
//	 * 
//	 * 
//	 * @param editingDomain
//	 * @param sourceElement
//	 * @throws IOException 
//	 */
//	private void removeComponents(TransactionalEditingDomain editingDomain, EObject sourceElement) throws IOException{
//		String projectName = EcoreUtil.getURI(sourceElement).trimFragment().trimSegments(1).lastSegment();
//		URI projectUri = URI.createPlatformResourceURI(projectName, true);
//		for (TranslationDescriptor translationDescriptor : translatedElements){
//			if (translationDescriptor.feature == CorePackage.Literals.PROJECT__COMPONENTS &&
//					translationDescriptor.value instanceof EventBNamedCommentedComponentElement){
//				String fileName = ((EventBNamedCommentedComponentElement)translationDescriptor.value).getName();
//				String ext = translationDescriptor.value instanceof Context? "buc" :  "bum";
//				URI fileUri = projectUri.appendSegment(fileName).appendFileExtension(ext); //$NON-NLS-1$
//				if(translationDescriptor.remove == true){	
//					Resource oldResource = editingDomain.getResourceSet().getResource(fileUri, false);
//					if(oldResource != null) {
//						oldResource.delete(Collections.EMPTY_MAP);
//					}	
//				}
//			}
//		}
//	}

	

		
/*
 * If any translated elements are a new root level element, this attaches the new element as the content of the resource.
 * The resource should already be loaded in the editing domains resource set from previous.
 * Note that we do not save the resource yet in case the translation process does not complete. 
 * 
 * N.B. CURRENTLY ALL RESOURCES ARE ASSUMED TO BE WITHIN THE SAME PROJECT AS THE SOURCE ELEMENT. 
 * (i.e. translationDescriptor.parent is ignored when adding new root level elements)
 * 
 * @param editingDomain
 * @param sourceElement
 * @return list of affected Resources
 */
	private Collection<? extends Resource> placeRootElements(TransactionalEditingDomain editingDomain, EObject sourceElement) throws IOException {
		List<Resource> modifiedResources = new ArrayList<Resource>();

		for (TranslationDescriptor translationDescriptor : translatedElements){
			if (translatorConfig.adapter.isRoot(translationDescriptor)){
				URI fileUri = translatorConfig.adapter.getComponentURI(translationDescriptor, sourceElement);
				if (fileUri!=null){
					//annotate with translation details using the adapter (adapter may specialise this for the translation)
					translatorConfig.adapter.setGeneratedBy(translationDescriptor.value, translationID);
					//annotate with source element using the adapter
					translatorConfig.adapter.setSourceElement(translationDescriptor.value, translationDescriptor.source);

					Resource resource = editingDomain.getResourceSet().getResource(fileUri, false);
					if (resource==null){
						resource = editingDomain.createResource(fileUri.toString());
					}
					resource.getContents().add((EObject)translationDescriptor.value);
					if (!modifiedResources.contains(resource)) modifiedResources.add(resource);		
				}
			}
		}
		return modifiedResources;
	}

		
/*
 * puts the translated elements into the model
 * @param editingDomain 
 * @return modified resources
 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Collection<? extends Resource> placeGenerated(EditingDomain editingDomain) throws Exception {
		//arrange the translation descriptors into priority order
		for (TranslationDescriptor translationDescriptor : translatedElements){
			Integer pri = translationDescriptor.priority;
			List<TranslationDescriptor> objects = priorities.get(pri);
			if (objects ==null) objects = new ArrayList<TranslationDescriptor>();
			objects.add(translationDescriptor); //newChild);
			priorities.put(pri,objects);
		}
		//process the prioritised mappings of translation descriptors
		List<Resource> modifiedResources = new ArrayList<Resource>();
		Map<EList,Integer> positions = new HashMap<EList,Integer>();
		for (int pri=10; pri>=-10; pri--){
			positions.clear();
			if (priorities.containsKey(pri)){
				for (TranslationDescriptor translationDescriptor : priorities.get(pri)){

					if (!translationDescriptor.remove && !translatorConfig.adapter.outputFilter(translationDescriptor)){
						continue;								
					}

					Object value = translationDescriptor.value;
					if (translationDescriptor.parent != null){
						if (translationDescriptor.parent.eIsProxy()){
							translationDescriptor.parent = EcoreUtil.resolve(translationDescriptor.parent,editingDomain.getResourceSet());
							if (translationDescriptor.parent.eIsProxy()){
								throw new Exception("cannot resolve "+translationDescriptor.parent);
							}
						}
						if (translationDescriptor.parent.eClass().getEAllStructuralFeatures().contains(translationDescriptor.feature) &&
							translationDescriptor.feature.getEType().isInstance(value)){
							
							Object featureValue = translationDescriptor.parent.eGet(translationDescriptor.feature);
		
							if (featureValue instanceof EList){	
								if(translationDescriptor.remove == false){	
									
									//the following alterations to the value should NOT be performed when the value is being added as a (non-containment) reference
									if (!(translationDescriptor.feature instanceof EReference) || ((EReference)translationDescriptor.feature).isContainment()){
										//annotate with translation details using the adapter (adapter may specialise this for the translation)
										translatorConfig.adapter.setGeneratedBy(translationDescriptor.value, translationID);
										//annotate with source element using the adapter
										translatorConfig.adapter.setSourceElement(translationDescriptor.value, translationDescriptor.source);
										//also the priority may be set to influence the position within a containment
										translatorConfig.adapter.setPriority(translationDescriptor.value, pri);
									}
									
									int pos=-1;
									//if a 'before' object has been given and it is in the feature, the position is before that object 
									if (translationDescriptor.before != null) {
										pos = ((EList)featureValue).indexOf(translationDescriptor.before);
									}
									
									//otherwise let the adapter decide (possibly using the annotated priority)
									if (pos==-1) {
										pos = translatorConfig.adapter.getPos(((EList)featureValue), translationDescriptor.value);
									}
									((EList)featureValue).add(pos, translationDescriptor.value);
									
								}
								else{
									ArrayList<Object> toRemove = new ArrayList<Object>();
									for(Object obj : (EList)featureValue){
										if(translatorConfig.adapter.match(obj, translationDescriptor.value))
											toRemove.add(obj);
									}
									((EList)featureValue).removeAll(toRemove);
								}
							}else {
								if(translationDescriptor.remove == false){
									//FIXME: this should be analysed more
									translationDescriptor.parent.eSet(translationDescriptor.feature, translationDescriptor.value);
								}
								else
									if  (translationDescriptor.feature.isUnsettable())
										translationDescriptor.parent.eUnset(translationDescriptor.feature);
									else
										translationDescriptor.parent.eSet(translationDescriptor.feature, translationDescriptor.feature.getDefaultValue());
							}
							//add to list of modifiedResources if not already there
							Resource resource = translationDescriptor.parent.eResource();
							if (resource !=null && !modifiedResources.contains(resource)){
								modifiedResources.add(resource);
							}
						}else{
							//Error messages are translated elsewhere - should not get here.
						}
					}
				}
			}
		}
		return modifiedResources;
	}


	/*
	 * a record of rules that have been deferred
	 */
	private Map<EObject, List<IRule>> deferredRules = new HashMap<EObject,List<IRule>>();
	private void defer(EObject sourceElement2, IRule rule){
		List<IRule> rules = deferredRules.get(sourceElement2);
		if (rules == null) rules = new ArrayList<IRule>();
		rules.add(rule);
		deferredRules.put(sourceElement2, rules);
	}
	
	/*
	 * The translation is done in two stage:
	 * 1) traverse the model firing any appropriate rules that are enabled. This may result in deferred
	 *    rules that are enabled but cannot be fired due to dependencies on other rules.
	 * 2) repeatedly iterate through the deferred rules firing any that can fire until none are left or
	 *    if no progress is made raise an exception.
	 * 
	 * @param sourceElement
	 * @throws Exception - for any exception, abort
	 */
	private void doGenerate(final EObject sourceElement2) throws Exception {
		boolean late = false;
		// stage 1 - traverse the model firing appropriate enabled rules
		traverseModel(sourceElement2);
		
		// stage 2 - deal with any deferred rules
		while (deferredRules.size() >0 ){
			boolean progress = false;
			List<EObject> empties = new ArrayList<EObject>();
			for (EObject sourceElement : deferredRules.keySet()){
				List<IRule> firedRules = new ArrayList<IRule>();
				for (IRule rule : deferredRules.get(sourceElement)){
					if (rule != null && rule.enabled(sourceElement)) {
						if ((late || !rule.fireLate()) && rule.dependenciesOK(sourceElement, translatedElements)){
							translatedElements.addAll(rule.fire(sourceElement, translatedElements));
							firedRules.add(rule);
						}
					}
				}
				if (firedRules.size()>0) {
					progress = true;
					deferredRules.get(sourceElement).removeAll(firedRules);
				}
				if (deferredRules.get(sourceElement).size()==0){
					empties.add(sourceElement);
				}
			}
			
			deferredRules.keySet().removeAll(empties);
			if (progress == false) {
				if (late){ //o-oh, no progress when already doing the late rules 
					printRules();
					throw new Exception(Messages.TRANSLATOR_MSG_00);
				}
				late = true; //enable the late rules
			}
		} 
	}
	
	/**
	 * 
	 */
	private void printRules() {
		System.out.println("Not making progress on the following rules:");
		for (Entry<EObject, List<IRule>> entry : deferredRules.entrySet()){
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
		}
	}


	/*
	 * recursive routine to traverse model firing appropriate rules
	 */
	private void traverseModel(final EObject sourceElement) throws Exception {
		
		//this ensures that we do not translate from our own translated elements
		if (!translatorConfig.adapter.inputFilter(sourceElement, translationID))
			//translatorConfig.translatorID.equals(translatorConfig.adapter.getGeneratedById(sourceElement)))
				return;
		
		//try to fire all the rules listed for this kind of source element
		List<IRule> rules = new ArrayList<IRule>();
		List<EClass> types = new ArrayList<EClass>();
		types.addAll(sourceElement.eClass().getEAllSuperTypes());
		types.add(sourceElement.eClass());
		for (EClass type : types ){
			if (translatorConfig.ruleMapping.get(type)!=null)
				rules.addAll(translatorConfig.ruleMapping.get(type));
		}
		for (final IRule rule : rules){
			if (rule != null && rule.enabled(sourceElement)) {
				if (!rule.fireLate() && rule.dependenciesOK(sourceElement, translatedElements)){
					translatedElements.addAll(rule.fire(sourceElement, translatedElements));
				}else{
					defer(sourceElement,rule);
				}
			}
		}

		//recursively traverse contents but avoiding derived containments
		EList<EReference> containments = sourceElement.eClass().getEAllContainments();		
		for (EReference containment : containments) {
			if (!containment.isDerived()) {
				Object containmentValue = sourceElement.eGet(containment);
				if (containmentValue instanceof EList<?>) {
					for (Object child : (EList<?>)containmentValue) {
						if (child instanceof EObject) {
							traverseModel((EObject) child);
						}
					}
				}
			}
		}

	}	

}
