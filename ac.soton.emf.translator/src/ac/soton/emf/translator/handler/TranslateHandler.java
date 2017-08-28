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
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
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
import ac.soton.emf.translator.TranslatorFactory;
import ac.soton.emf.translator.impl.Messages;

/**
 * handler for commands to run the translator.
 * 
 * @author cfs 
 *
 */
public class TranslateHandler extends AbstractHandler {
	
	protected Shell shell;
	IStatus status = null;
	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public final Object execute(ExecutionEvent event) throws ExecutionException {
		final EObject sourceElement; // = null;
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof TreeSelection){
			Object obj = ((TreeSelection)selection).getFirstElement();
			if (obj instanceof EObject){
				sourceElement = (EObject)obj;
			}else if (obj instanceof IInternalElement){
				sourceElement = new EMFRodinDB().loadEventBComponent((IInternalElement)obj) ;
			}else if (obj instanceof IAdaptable) {
				Object adaptedObj = ((IAdaptable) obj).getAdapter(EObject.class);
				if (adaptedObj instanceof EObject){
					sourceElement = (EObject) adaptedObj; 
				} else return null;
			}else if (obj instanceof Resource){
				sourceElement = ((Resource)obj).getContents().get(0);
			}else return null;
		} else return null;
		
		if (sourceElement==null) return null;
		
		IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		shell = activeWorkbenchWindow.getShell();
		final String commandId = event.getCommand().getId();
		
		//get translator factory
		try {
			final TranslatorFactory factory = TranslatorFactory.getFactory();
			if (factory != null && factory.canTranslate(commandId, sourceElement.eClass())){
				ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
				try {
					dialog.run(true, true, new IRunnableWithProgress(){
						public void run(IProgressMonitor monitor) throws InvocationTargetException { 
							try {
								SubMonitor submonitor = SubMonitor.convert(monitor, "preProcessing", 3);
								preProcessing(sourceElement, commandId, submonitor.newChild(1));
								status = factory.translate(sourceElement, commandId, submonitor.newChild(1));
								postProcessing(sourceElement, commandId, submonitor.newChild(1));
							} catch (Exception e) {
								throw new InvocationTargetException(e);
							}
						}
					});
				} catch (InvocationTargetException e) {
			    	status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.TRANSLATOR_MSG_07, e);
					Activator.logError(Messages.TRANSLATOR_MSG_07, e);
					
				} catch (InterruptedException e) {
			    	status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.TRANSLATOR_MSG_08, e);        	
					Activator.logError(Messages.TRANSLATOR_MSG_08, e);
					
				}finally{
					if (status != null && !status.isOK()){
						MessageDialog.openError(shell, Messages.TRANSLATOR_MSG_09, status.getMessage());
					}
				}
			}
		} catch (CoreException e) {
			Activator.logError(Messages.TRANSLATOR_MSG_07, e);
			MessageDialog.openError(shell, Messages.TRANSLATOR_MSG_07, e.getMessage());
		}
		return null;
	}

	
	/**
	 * This can be overridden to add some pre-processing before the translation
	 * default implementation does nothing
	 * @param sourceElement
	 * @param commandId
	 * @param monitor
	 */
	protected void preProcessing(EObject sourceElement, String commandId, IProgressMonitor monitor) throws Exception {
        monitor.done();
	}
		
	
	/**
	 * This can be overridden to add some post-processing after the translation
	 * default implementation does nothing
	 * @param sourceElement
	 * @param commandId
	 * @param monitor
	 * @throws CoreException 
	 */
	protected void postProcessing(EObject sourceElement, String commandId, IProgressMonitor monitor) throws Exception {
        monitor.done();
	}
	
}
