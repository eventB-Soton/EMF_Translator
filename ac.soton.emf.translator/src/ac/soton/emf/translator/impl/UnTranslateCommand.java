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
package ac.soton.emf.translator.impl;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;

import ac.soton.emf.translator.Activator;

/**
 * This provides a command to remove previously translated elements
 * given an editing domain, sourceElement and suitable translator
 * It uses the translators untranslate method.
 * 
 * @author cfs
 *
 */
public class UnTranslateCommand extends AbstractEMFOperation {

	private EObject sourceElement;
	private Translator translator;
	
	public UnTranslateCommand(TransactionalEditingDomain editingDomain, EObject sourceElement, Translator translator) {
		super(editingDomain, "Delete generated elements", null);
		setOptions(Collections.singletonMap(Transaction.OPTION_UNPROTECTED, Boolean.TRUE));
		this.sourceElement = sourceElement;
		this.translator = translator;
	}

	@Override
	public boolean canExecute(){
		return super.canExecute() && sourceElement!=null;
	}	
	
	@Override
	public boolean canRedo(){
		return false;
	}

	@Override
	public boolean canUndo(){
		return false;
	}

	@Override
	protected  IStatus doExecute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		IStatus status = Status.OK_STATUS;
		try {
			TransactionalEditingDomain editingDomain = getEditingDomain();
			final List<Resource> modifiedResources;
			monitor.beginTask(Messages.TRANSLATOR_MSG_13(sourceElement.eClass().getName()),10);							
			monitor.worked(1);
			modifiedResources = translator.unTranslate(editingDomain, sourceElement);
			monitor.worked(2);
			//set modified attribute for modified resources to indicate they need to be saved
			for (Resource resource : modifiedResources){
				resource.setModified(true);	
				monitor.worked(2);
			}
			monitor.done();
		} catch (Exception e) {
			e.printStackTrace();
			Activator.logError(Messages.TRANSLATOR_MSG_19+ " : "+e.getMessage(), e);
			status = new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.TRANSLATOR_MSG_19+ " : see error log", e);
		} finally {
			monitor.done();
		}
		return status;
	}

}