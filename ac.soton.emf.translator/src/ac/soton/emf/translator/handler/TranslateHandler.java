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
package ac.soton.emf.translator.handler;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eventb.emf.persistence.EMFRodinDB;
import org.rodinp.core.IInternalElement;

import ac.soton.emf.translator.Activator;
import ac.soton.emf.translator.impl.Messages;
import ac.soton.emf.translator.impl.TranslateCommand;
import ac.soton.emf.translator.impl.TranslatorFactory;

/**
 * handler for commands to run the translator.
 * 
 * @author cfs 
 *
 */
public class TranslateHandler extends AbstractHandler {
	
	IStatus status = null;
	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		EObject sourceElement = null;
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof TreeSelection){
			Object obj = ((TreeSelection)selection).getFirstElement();
			if (obj instanceof Resource){
				sourceElement = ((Resource)obj).getContents().get(0);
			}
			if (obj instanceof EObject){
//				while (((EObject)obj).eContainer()!=null) obj = ((EObject)obj).eContainer();
				sourceElement = (EObject)obj;
			}else if (obj instanceof IInternalElement){
				sourceElement = new EMFRodinDB().loadEventBComponent((IInternalElement)obj) ;
			}else if (obj instanceof IAdaptable) {
				Object adaptedObj = ((IAdaptable) obj).getAdapter(EObject.class);
				if (adaptedObj instanceof EObject){
					sourceElement = (EObject) adaptedObj; 
				}
			}
		}
		
		if (sourceElement==null) return null;
		IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		final Shell shell = activeWorkbenchWindow.getShell();
			String commandId = event.getCommand().getId();
			
			//get trasnslator factory
			TranslatorFactory factory = null;
			try {
				factory = TranslatorFactory.getFactory();
			} catch (CoreException e) {
				Activator.logError(Messages.TRANSLATOR_MSG_07, e);
				MessageDialog.openError(shell, Messages.TRANSLATOR_MSG_07, e.getMessage());
			}
		
			if (factory != null && factory.canTranslate(commandId, sourceElement.eClass())){
				
				TransactionalEditingDomain editingDomain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();
				final TranslateCommand translateCommand = new TranslateCommand(editingDomain, sourceElement, commandId);

				if (translateCommand.canExecute()) {	
					// run with progress
					ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
					
					try {
						dialog.run(true, true, new IRunnableWithProgress(){
						     public void run(IProgressMonitor monitor) { 
						    	 monitor.beginTask(Messages.TRANSLATOR_MSG_05, IProgressMonitor.UNKNOWN);
						         try {
						        	status = translateCommand.execute(monitor, null);
						         } catch (ExecutionException e) {
						        	status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.TRANSLATOR_MSG_06, e);
									Activator.logError(Messages.TRANSLATOR_MSG_06, e);
						         }
						         monitor.done();
						     }
						 });
						
					} catch (InvocationTargetException e) {
			        	status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.TRANSLATOR_MSG_07, e);
						Activator.logError(Messages.TRANSLATOR_MSG_07, e);
						return null;
						
					} catch (InterruptedException e) {
			        	status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.TRANSLATOR_MSG_08, e);        	
						Activator.logError(Messages.TRANSLATOR_MSG_08, e);
						return null;
						
					}finally{
						if (!status.isOK()){
							MessageDialog.openError(shell, Messages.TRANSLATOR_MSG_09, status.getMessage());
						}
					}
					
				}
			}
		return null;
	}
	
}
