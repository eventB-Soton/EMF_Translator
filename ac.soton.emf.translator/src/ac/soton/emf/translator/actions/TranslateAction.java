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
package ac.soton.emf.translator.actions;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import ac.soton.emf.translator.Activator;
import ac.soton.emf.translator.command.TranslateCommand;
import ac.soton.emf.translator.impl.Messages;
import ac.soton.emf.translator.impl.TranslatorFactory;

/**
 * Import action handler.
 * 
 * @author colin 
 *
 */
public class TranslateAction extends AbstractHandler {
	
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
				while (((EObject)obj).eContainer()!=null) obj = ((EObject)obj).eContainer();
				sourceElement = (EObject)obj;
			}
		}
		if (sourceElement!=null && TranslatorFactory.getFactory().canTranslate(sourceElement.eClass())){
			// save before transformation
			final IEditorPart editor = HandlerUtil.getActiveEditor(event);
			if (!(editor instanceof IEditingDomainProvider)) return null;
			if (editor.isDirty())
				editor.doSave(new NullProgressMonitor());
			
			TransactionalEditingDomain editingDomain = TransactionalEditingDomain.Factory.INSTANCE.createEditingDomain();
			final TranslateCommand translateCommand = new TranslateCommand(editingDomain, sourceElement);
			
			if (translateCommand.canExecute()) {	
				// run with progress
				ProgressMonitorDialog dialog = new ProgressMonitorDialog(editor.getSite().getShell());
				try {
					dialog.run(true, true, new IRunnableWithProgress(){
					     public void run(IProgressMonitor monitor) { 
					    	 monitor.beginTask(Messages.TRANSLATOR_MSG_05, IProgressMonitor.UNKNOWN);
					         try {
					        	if (!translateCommand.execute(monitor, editor).isOK()){
									MessageDialog
											.openError(editor.getSite().getShell(),
													Messages.TRANSLATOR_MSG_09,
													Messages.TRANSLATOR_MSG_10);
								}
					         } catch (ExecutionException e) {
								Activator.logError(Messages.TRANSLATOR_MSG_06, e);
					         }
					         monitor.done();
					     }
					 });
				} catch (InvocationTargetException e) {
					Activator.logError(Messages.TRANSLATOR_MSG_07, e);
					return null;
				} catch (InterruptedException e) {
					Activator.logError(Messages.TRANSLATOR_MSG_08, e);
					return null;
				} 

				// error feedback

			}
		}
		return null;
	}
}
