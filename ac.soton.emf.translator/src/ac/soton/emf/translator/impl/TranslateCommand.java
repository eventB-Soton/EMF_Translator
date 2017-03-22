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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
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
		
		try {
			
			ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {

				public void run(final IProgressMonitor monitor) throws CoreException{
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
						//try to save all the modified resources
				        monitor.subTask(Messages.TRANSLATOR_MSG_16);
				        List<Resource> saved = new ArrayList<Resource>();
						for (Resource resource : modifiedResources){
							try {
								if (!saved.contains(resource)){
									resource.save(Collections.emptyMap());
									saved.add(resource);
								}
								monitor.worked(1);
							} catch (IOException e) {
								//throw this as a CoreException
								new CoreException(
										new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.TRANSLATOR_MSG_18(resource), e));
							}					
						}
					}
					
				monitor.done();
				}
			},monitor);
			
			return Status.OK_STATUS;

		} catch (CoreException e) {
			e.printStackTrace();
			return new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.TRANSLATOR_MSG_19, e);
		} finally {
			monitor.done();
		}
	}

}