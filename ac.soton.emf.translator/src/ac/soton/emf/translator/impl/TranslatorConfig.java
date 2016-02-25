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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;

import ac.soton.emf.translator.configuration.IAdapter;
import ac.soton.emf.translator.configuration.IRule;

public class TranslatorConfig{
	public String translatorID;
	public EPackage rootSourcePackage;
	public EClassifier rootSourceClass;
	public Map<EClassifier, List<IRule>> ruleMapping;
	public IAdapter adapter;
	
	public TranslatorConfig(String translatorID, EPackage rootSourcePackage, EClassifier rootSourceClass, IAdapter adapter){
		this.translatorID = translatorID;
		this.rootSourcePackage = rootSourcePackage;
		this.rootSourceClass = rootSourceClass;
		this.adapter = adapter;
		ruleMapping = new HashMap<EClassifier, List<IRule>>();
	}
	
	public void addRule(EClassifier sourceClass,IRule rule){
		List<IRule> ruleList = ruleMapping.get(sourceClass);
		if (ruleList == null) ruleList = new ArrayList<IRule>();
		ruleList.add(rule);
		ruleMapping.put(sourceClass, ruleList);
	}
	
}
