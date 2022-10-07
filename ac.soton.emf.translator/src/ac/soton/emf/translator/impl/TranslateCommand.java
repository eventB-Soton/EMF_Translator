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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;

import ac.soton.emf.translator.Activator;

//////////////////////TRANSLATE COMMAND//////////////////////////
public class TranslateCommand extends AbstractEMFOperation {

	private EObject element;
	private Translator translator;

	/**
	 * @param commandId 
	 * @param domain
	 * @param label
	 * @param affectedFiles
	 */
	public TranslateCommand(TransactionalEditingDomain editingDomain, EObject rootEl, Translator translator) {
		super(editingDomain, Messages.TRANSLATOR_MSG_11, null);
		setOptions(Collections.singletonMap(Transaction.OPTION_UNPROTECTED, Boolean.TRUE));
		if (rootEl.eIsProxy()){
			this.element = EcoreUtil.resolve(rootEl, editingDomain.getResourceSet());
		}else{
			this.element = rootEl;			
		}
		this.translator = translator;		
	}
	
	@Override
	public boolean canExecute(){
		return super.canExecute() && element!=null && !element.eIsProxy();
	}
	
	@Override
	public boolean canRedo(){
		return false;
	}

	@Override
	public boolean canUndo(){
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand#doExecuteWithResult(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.core.runtime.IAdaptable)
	 */
	@Override
	protected IStatus doExecute(IProgressMonitor monitor, IAdaptable info) {
		IStatus status = Status.OK_STATUS;
		try {
			TransactionalEditingDomain editingDomain = getEditingDomain();
			final List<Resource> modifiedResources;
			
			monitor.beginTask(Messages.TRANSLATOR_MSG_13(element.eClass().getName()),10);							
			monitor.worked(1);
			
	        monitor.subTask(Messages.TRANSLATOR_MSG_15);	
	        modifiedResources = translator.translate(editingDomain, element);
	        
			monitor.worked(4);
			if (modifiedResources == null){
				monitor.subTask(Messages.TRANSLATOR_MSG_07);
			}else{ 
				//set modified attribute for modified resources to indicate they need to be saved
				for (Resource resource : modifiedResources){
					resource.setModified(true);	
					monitor.worked(2);
				}
			}
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