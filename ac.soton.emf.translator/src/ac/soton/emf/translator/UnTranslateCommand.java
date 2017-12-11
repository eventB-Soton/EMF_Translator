/*******************************************************************************
 * Copyright (c) 2017 University of Southampton.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package ac.soton.emf.translator;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;

import ac.soton.emf.translator.configuration.IAdapter;
import ac.soton.emf.translator.impl.Messages;
import ac.soton.emf.translator.impl.Remover;

/**
 * This provides a command to remove previously translated elements from all
 * resources in the given collection of resources. 
 * It uses the same remover as the translator and requires an adapter
 *  and the source elements ID string as well as an editing domain
 * 
 * @author cfs
 *
 */
public class UnTranslateCommand extends AbstractEMFOperation {

	private String sourceID;
	private Collection<Resource> resources;
	private IAdapter adapter;
	
	/**
	 * create a new command instance
	 * 
	 * @param editingDomain
	 * @param resources
	 * @param sourceID
	 * @param adapter
	 */
	public UnTranslateCommand(TransactionalEditingDomain editingDomain, Collection<Resource> resources, String sourceID, IAdapter adapter) {
		super(editingDomain, "Delete generated elements", null);
		setOptions(Collections.singletonMap(Transaction.OPTION_UNPROTECTED, Boolean.TRUE));
		this.resources = resources;
		this.sourceID = sourceID;
		this.adapter = adapter;
	}
	
	@Override
	public boolean canExecute(){
		return sourceID!=null && sourceID.length()>0 && resources!=null && resources.size()>0 && adapter!=null ;
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
			ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
				public void run(final IProgressMonitor monitor) throws CoreException{
					monitor.beginTask(Messages.TRANSLATOR_MSG_13(sourceID),10);							
					monitor.worked(1);
					//remove previously generated elements
					Remover remover = new Remover(resources, sourceID, adapter);
					Collection<Resource> modifiedResources = remover.removeTranslated();
					monitor.worked(2);
					//set modified attribute for modified resources to indicate they need to be saved
					for (Resource resource : modifiedResources){
						resource.setModified(true);	
						monitor.worked(2);
					}
					monitor.done();
				}
			},monitor);
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