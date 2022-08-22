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
package ac.soton.emf.translator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import ac.soton.emf.translator.configuration.IAdapter;
import ac.soton.emf.translator.configuration.IRule;
import ac.soton.emf.translator.impl.Identifiers;
import ac.soton.emf.translator.impl.Messages;
import ac.soton.emf.translator.impl.TranslateCommand;
import ac.soton.emf.translator.impl.Translator;
import ac.soton.emf.translator.impl.TranslatorConfig;
import ac.soton.emf.translator.impl.UnTranslateCommand;


public class TranslatorFactory {

	// The shared instance
	private static TranslatorFactory factory = null;
	
	//map of the translators available for each command
	private Map<String,Collection<TranslatorConfig> > translatorsForCommand = new HashMap<String, Collection<TranslatorConfig>>();
	
	/*
	 * get the translator Id for the given command and root source class or null if none
	 */
	private TranslatorConfig getTranslatorConfig(String commandId, EClassifier rootSourceClass) {
		Collection<TranslatorConfig> translators = translatorsForCommand.get(commandId);
		if (translators== null) return null;
		for (TranslatorConfig translatorConfig : translators){
			if (translatorConfig.rootSourceClass.equals(rootSourceClass)) return translatorConfig;
		}
		return null;		
	}
	
	/*
	 * The constructor for the shared instance of factory,
	 * populates the registry of translator configurations from extensions point
	 */
	private TranslatorFactory() throws CoreException {

		// populate translator configuration data from registered extensions
		for (final IExtension translatorExtension : Platform.getExtensionRegistry().getExtensionPoint(Identifiers.EXTPT_TRANSLATORS_EXTPTID).getExtensions()) {
			for (final IConfigurationElement translatorExtensionElement : translatorExtension.getConfigurationElements()) {
				EPackage rootSourcePackage = EPackage.Registry.INSTANCE.getEPackage(translatorExtensionElement.getAttribute(Identifiers.EXTPT_TRANSLATORS_TRANSLATOR_SOURCEPACKAGE));
				EClassifier rootSourceClass = rootSourcePackage.getEClassifier(translatorExtensionElement.getAttribute(Identifiers.EXTPT_TRANSLATORS_TRANSLATOR_ROOTSOURCECLASS));
				String translatorID = translatorExtensionElement.getAttribute(Identifiers.EXTPT_TRANSLATORS_TRANSLATOR_TRANSLATORID);
				String commandID = translatorExtensionElement.getAttribute(Identifiers.EXTPT_TRANSLATORS_TRANSLATOR_COMMANDID);
				if (getTranslatorConfig(commandID, rootSourceClass)==null){
					final IAdapter adapter = (IAdapter) translatorExtensionElement.createExecutableExtension(Identifiers.EXTPT_TRANSLATORS_TRANSLATOR_ADAPTERCLASS);
					if (rootSourcePackage!= null) {
						TranslatorConfig translatorConfig = new TranslatorConfig(translatorID, rootSourcePackage, rootSourceClass, adapter);
						
						for (final IExtension rulesetExtension : Platform.getExtensionRegistry().getExtensionPoint(Identifiers.EXTPT_RULESETS_EXTPTID).getExtensions()) {				
							for (final IConfigurationElement rulesetExtensionElement : rulesetExtension.getConfigurationElements()) {
								if (translatorID.equals(rulesetExtensionElement.getAttribute(Identifiers.EXTPT_RULESETS_RULESET_TRANSLATORID))){
									for (final IConfigurationElement ruleExtensionElement : rulesetExtensionElement.getChildren(Identifiers.EXTPT_RULESETS_RULESET_RULE)) {
										
										//see whether a source EPackage has been explicitly defined for this rule
										EPackage sourcePackage = EPackage.Registry.INSTANCE.getEPackage(ruleExtensionElement.getAttribute(Identifiers.EXTPT_RULESETS_RULESET_RULE_SOURCEPACKAGE));
										if (sourcePackage == null) {
											//no explicit EPackage so use the rootSourcePackage of the translator
											sourcePackage = rootSourcePackage;
										}
	
										//find the source EClass in the source EPackage
										EClassifier sourceClass = sourcePackage.getEClassifier(ruleExtensionElement.getAttribute(Identifiers.EXTPT_RULESETS_RULESET_RULE_SOURCECLASS));
										//if not in the rootPackage, try its subPackages
										if(sourceClass == null){
											for (EPackage subPackage  : sourcePackage.getESubpackages()){
												sourceClass = subPackage.getEClassifier(ruleExtensionElement.getAttribute(Identifiers.EXTPT_RULESETS_RULESET_RULE_SOURCECLASS));
												if (sourceClass != null) break;
											}
										}
									
										// if we found the class, add the rule
										if (sourceClass != null) {
											final IRule rule = (IRule) ruleExtensionElement.createExecutableExtension(Identifiers.EXTPT_RULESETS_RULESET_RULE_RULECLASS);									
											translatorConfig.addRule(sourceClass, rule);
										}
									}
								}
							}
						}
						//save config data for this translator
						if (translatorConfig != null) {
							Collection<TranslatorConfig> translators = translatorsForCommand.get(commandID);
							if (translators == null) translators = new HashSet<TranslatorConfig>();
							translators.add(translatorConfig);
							translatorsForCommand.put(commandID, translators);
						}
					}
				}else{	//two translators with the same command id and root source class are not allowed
					String errorMessage = "Conflicting translators for Command id: "+commandID+" , source class:"+rootSourceClass.getName()+" ; ignoring "+ translatorID;
					Activator.logError(errorMessage);
				}
			}
		}
	}
	
	/**
	 * return the TranslatorFactory shared instance
	 * (on the first call, this creates it by loading the translation extensions)
	 * @return
	 * @throws CoreException
	 */
	public static TranslatorFactory getFactory() throws CoreException{
		if (factory == null){
			factory = new TranslatorFactory();
		}
		return factory;
	}

	/**
	 * checks whether the factory has a translator for the given command and root source class
	 * @param commandId
	 * @param rootSourceClass
	 * @return
	 */
	public boolean canTranslate(String commandId, EClassifier rootSourceClass){
		return getTranslatorConfig(commandId, rootSourceClass) != null;
	}

	
	/**
	 * Translate the given source element using the translator matching the given command ID
	 * (using the EMF Command framework)
	 * 
	 * If no editing domain is given, a new one will be created. 
	 * However, in this case, the resource containing the sourceElement is not in the new editing domain
	 * and therefore can not be changed by the translation rules (an exception will be generated) 
	 * To avoid this, pass the editing domain associated with the resource set containing the source element's resource
	 * i.e.. TransactionalEditingDomain.Factory.INSTANCE.getEditingDomain(sourceElement.eResource().getResourceSet())
	 * 
	 * 
	 * @param editingDomain (or null to create a new one)
	 * @param sourceElement
	 * @param commandId
	 * @param monitor 
	 * @throws ExecutionException 
	 * @since 3.0
	 */
	public IStatus translate(TransactionalEditingDomain editingDomain, EObject sourceElement, String commandId, IProgressMonitor monitor) throws ExecutionException {
		IStatus status = null;
		monitor.subTask(Messages.TRANSLATOR_MSG_14);
		//try to create an appropriate translator
		if (canTranslate(commandId, sourceElement.eClass())){
			TranslatorConfig translatorConfig = getTranslatorConfig(commandId, sourceElement.eClass());
			Translator translator = new Translator(translatorConfig);
			monitor.worked(2);
			Assert.isTrue(editingDomain!=null, "Editing Domain must not be null");
			final TranslateCommand translateCommand = new TranslateCommand(editingDomain, sourceElement, translator);	
			if (translateCommand.canExecute()) {	
				// run with progress
		    	 monitor.beginTask(Messages.TRANSLATOR_MSG_05, IProgressMonitor.UNKNOWN);
		         status = translateCommand.execute(monitor, null);
			}else{
				status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Cannot execute translation command "+translateCommand.getLabel());
			}
		}else{
			monitor.subTask(Messages.TRANSLATOR_MSG_07);
			status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "could not create translator for  "+commandId); 
		}
        monitor.done();
		return status;
	}

	/**
	 * Un-Translate the given source element using the translator matching the given command ID
	 * (using the EMF Command framework).
	 * This means remove all elements in potentially affected resources that are tagged as being generated from the source element
	 * 
	 * If no editing domain is given, a new one will be created. 
	 * However, in this case, the resource containing the sourceElement is not in the new editing domain
	 * and therefore can not be changed by the translation rules (an exception will be generated) 
	 * To avoid this, pass the editing domain associated with the resource set containing the source element's resource
	 * i.e.. TransactionalEditingDomain.Factory.INSTANCE.getEditingDomain(sourceElement.eResource().getResourceSet())
	 * 
	 * 
	 * @param editingDomain (or null to create a new one)
	 * @param sourceElement
	 * @param commandId
	 * @param monitor 
	 * @throws ExecutionException 
	 * @since 3.0
	 */
	
	public IStatus untranslate(TransactionalEditingDomain editingDomain, EObject sourceElement, String commandId, IProgressMonitor monitor) throws ExecutionException {
		IStatus status = null;
		monitor.subTask(Messages.TRANSLATOR_MSG_14);
		//try to create an appropriate translator
		if (canTranslate(commandId, sourceElement.eClass())){
			TranslatorConfig translatorConfig = getTranslatorConfig(commandId, sourceElement.eClass());
			Translator translator = new Translator(translatorConfig);
			monitor.worked(2);
			if (editingDomain==null){
				editingDomain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();
			}	
			final UnTranslateCommand unTranslateCommand = new UnTranslateCommand(editingDomain, sourceElement, translator);	
			if (unTranslateCommand.canExecute()) {	
				// run with progress
		    	 monitor.beginTask(Messages.TRANSLATOR_MSG_05, IProgressMonitor.UNKNOWN);
		         status = unTranslateCommand.execute(monitor, null);
			}else{
				status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Cannot execute un-translate command "+unTranslateCommand.getLabel());
			}
		}else{
			monitor.subTask(Messages.TRANSLATOR_MSG_07);
			status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "could not create translator for  "+commandId); 
		}
        monitor.done();
		return status;
	}
	
}
