/*******************************************************************************
 *  Copyright (c) 2016 University of Southampton.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *   
 *  Contributors:
 *  University of Southampton - Initial implementation
 *******************************************************************************/
package ac.soton.iumlb.scxml.importer.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.tests.sample.scxml.ScxmlAssignType;
import org.eclipse.sirius.tests.sample.scxml.ScxmlOnexitType;
import org.eclipse.sirius.tests.sample.scxml.ScxmlPackage;
import org.eclipse.sirius.tests.sample.scxml.ScxmlScxmlType;
import org.eclipse.sirius.tests.sample.scxml.ScxmlStateType;
import org.eventb.emf.core.machine.Action;
import org.eventb.emf.core.machine.Machine;
import org.eventb.emf.core.machine.MachinePackage;

import ac.soton.emf.translator.IRule;
import ac.soton.emf.translator.TranslationDescriptor;
import ac.soton.emf.translator.utils.Find;
import ac.soton.eventb.statemachines.State;
import ac.soton.eventb.statemachines.StatemachinesPackage;
import ac.soton.iumlb.scxml.importer.strings.Strings;
import ac.soton.iumlb.scxml.importer.utils.Make;
import ac.soton.iumlb.scxml.importer.utils.Utils;

public class ScxmlOnexitTypeRule extends AbstractSCXMLImporterRule implements IRule {

	ScxmlStateType stateContainer=null;
	List<State> states = new ArrayList<State>();
	
	public boolean enabled(final EObject sourceElement) throws Exception  {
		stateContainer = (ScxmlStateType) Find.containing(ScxmlPackage.Literals.SCXML_STATE_TYPE, sourceElement.eContainer());
		return stateContainer!=null;
	}
	
	@Override
	public boolean dependenciesOK(EObject sourceElement, final List<TranslationDescriptor> generatedElements) throws Exception  {
		ScxmlScxmlType scxmlContainer = (ScxmlScxmlType) Find.containing(ScxmlPackage.Literals.SCXML_SCXML_TYPE, sourceElement);
		states.clear();
		int refinementLevel = Utils.getRefinementLevel(sourceElement);
		int depth = Utils.getRefinementDepth(sourceElement);
		for (int i=refinementLevel; i<=depth; i++){
			Machine m = (Machine) Find.translatedElement(generatedElements, null, null, MachinePackage.Literals.MACHINE, Utils.getMachineName(scxmlContainer,i));
			State st =  (State) Find.element(m, null, nodes, StatemachinesPackage.Literals.STATE, stateContainer.getId());
			if (st==null) return false;
			states.add(st);
		}
		return true;
	}

	@Override
	public List<TranslationDescriptor> fire(EObject sourceElement, List<TranslationDescriptor> generatedElements) throws Exception {
		for (State st : states){
			int i=0;
			for (ScxmlAssignType assign : ((ScxmlOnexitType)sourceElement).getAssign()){
				Action action = (Action) Make.action(stateContainer.getId()+"_onexit_"+i, Strings.ASSIGN_ACTION(assign));
				st.getExitActions().add(action);
				i++;
			}	
		}
		return Collections.emptyList();
	}
	
}
