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
package ac.soton.emf.translator.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;

import ac.soton.emf.translator.Activator;
import ac.soton.emf.translator.IAdapter;
import ac.soton.emf.translator.IRule;
import ac.soton.emf.translator.impl.TranslatorConfig;


public class TranslatorFactory {

	// The shared instance
	private static TranslatorFactory factory = null;
	
	
	//cached store of translator configurations that have been loaded from extension points
	private Map<EClassifier,TranslatorConfig> translatorConfigRegistry = new HashMap<EClassifier, TranslatorConfig  >();
	
	
	/*
	 * The constructor for the shared instance of factory,
	 * populates the registry of translator configurations from extensions point
	 */
	private TranslatorFactory() {

		// populate translator configuration data from registered extensions
		for (final IExtension extension : Platform.getExtensionRegistry().getExtensionPoint(Identifiers.EXTPT_RULE_ID).getExtensions()) {
			for (final IConfigurationElement translatorExtensionElement : extension.getConfigurationElements()) {
				try {
					EPackage rootSourcePackage = EPackage.Registry.INSTANCE.getEPackage(translatorExtensionElement.getAttribute(Identifiers.EXTPT_RULE_SOURCEPACKAGE));
					EClassifier rootSourceClass = rootSourcePackage.getEClassifier(translatorExtensionElement.getAttribute(Identifiers.EXTPT_RULE_ROOTSOURCECLASS));
					String translatorID = translatorExtensionElement.getAttribute(Identifiers.EXTPT_RULE_TRANSLATORID);
					final IAdapter adapter = (IAdapter) translatorExtensionElement.createExecutableExtension(Identifiers.EXTPT_RULE_ADAPTERCLASS);
					if (rootSourcePackage!= null) {
						TranslatorConfig translatorConfig = new TranslatorConfig(translatorID, rootSourcePackage, rootSourceClass, adapter);
						for (final IConfigurationElement ruleExtensionElement : translatorExtensionElement.getChildren(Identifiers.EXTPT_RULE_RULE)) {
	
								EClassifier sourceClass = null;
								//see if a EPackage has been explicitly defined
								EPackage sourcePackage = EPackage.Registry.INSTANCE.getEPackage(ruleExtensionElement.getAttribute(Identifiers.EXTPT_RULE_SOURCEPACKAGE));
								if (sourcePackage != null) {
									sourceClass = sourcePackage.getEClassifier(ruleExtensionElement.getAttribute(Identifiers.EXTPT_RULE_SOURCECLASS));
								}else{
									//no explicit EPackage so try the rootSourcePackage of the translator
									sourcePackage = rootSourcePackage;
									sourceClass = sourcePackage.getEClassifier(ruleExtensionElement.getAttribute(Identifiers.EXTPT_RULE_SOURCECLASS));
									//if not in rootSourcePackage, try its subPackages
									if(sourceClass == null){
										for (EPackage subPackage  : sourcePackage.getESubpackages()){
											sourceClass = subPackage.getEClassifier(ruleExtensionElement.getAttribute(Identifiers.EXTPT_RULE_SOURCECLASS));
											if (sourceClass != null) break;
										}
									}
								}
								
								// if we found the class, add the rule
								if (sourceClass != null) {
									final IRule rule = (IRule) ruleExtensionElement.createExecutableExtension(Identifiers.EXTPT_RULE_RULECLASS);									
									translatorConfig.addRule(sourceClass, rule);
								}
								
	
						}
						//save config data in case another translator instance is needed for this EClass
						if (translatorConfig != null) translatorConfigRegistry.put(rootSourceClass,translatorConfig);
					}
				} catch (final CoreException e) {
					Activator.logError(e.getMessage(),e);
					break;
				}
			}
		}
	}
	


	public static TranslatorFactory getFactory(){
		if (factory == null){
			factory = new TranslatorFactory();
		}
		return factory;
	}



	public boolean canTranslate(EClassifier rootSourceClass){
		return translatorConfigRegistry.containsKey(rootSourceClass);
	}
	
	/**
	 * Construct a translator.
	 * This should be called from a command handler action.
	 * 	
	 * @param rootSourceClass	- the EClass of the root element that this is a translator for
	 */
		
	public Translator createTranslator(EClass rootSourceClass){ 	
		if (translatorConfigRegistry.containsKey(rootSourceClass)){
			return new Translator(translatorConfigRegistry.get(rootSourceClass));
		}else{
			return null;
		}
	}
	
	
}
