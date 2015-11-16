/**
 * Copyright (c) 2012 University of Southampton.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package ac.soton.eventb.emf.diagrams.importExport.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.parts.DiagramDocumentEditor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eventb.emf.core.CorePackage;
import org.eventb.emf.core.EventBElement;
import org.eventb.emf.core.EventBNamed;

import ac.soton.eventb.emf.diagrams.importExport.Activator;
import ac.soton.eventb.emf.diagrams.importExport.command.DeleteGeneratedCommand;
import ac.soton.eventb.emf.diagrams.importExport.command.ImportCommand;
import ac.soton.eventb.emf.diagrams.importExport.impl.ImporterFactory;
import ac.soton.eventb.emf.diagrams.importExport.impl.Messages;

/**
 * Generate action handler.
 * 
 * @author cfs
 *
 */
public class ImportAllHandler extends AbstractHandler {

	List<EventBElement> generateList = null;
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IEditorPart editor = HandlerUtil.getActiveEditorChecked(event);
		if (editor instanceof DiagramDocumentEditor) {
			final DiagramDocumentEditor diagramDocumentEditor = (DiagramDocumentEditor)editor;
			if (editor.isDirty()) editor.doSave(new NullProgressMonitor());	 // save before transformation
			//if (ValidateAction.validate(diagramDocumentEditor)) {			//do not proceed if validation of current diagram fails
				EObject element = diagramDocumentEditor.getDiagram().getElement();
				generateAllDiagrams(element, editor.getSite().getShell(), diagramDocumentEditor.getDiagramEditPart().getEditingDomain(), editor);
			//}
		}
		return null;
	}

	/**
	 * 
	 * This can be called programmatically to do the same as the Generate All command Handler
	 * 
	 * @param element        - The root level element that might contain diagrams
	 * @param shell          - The active shell for messages, or null
	 * @param editingDomain  - The editing domain
	 * @param info           -  THIS IS NOT USED.. pass null
	 */
	public String generateAllDiagrams(EObject element, Shell shell, TransactionalEditingDomain editingDomain, final IAdaptable info ) {
		String report = "";
		if (element instanceof EventBElement){
			EObject component = ((EventBElement)element).getContaining(CorePackage.Literals.EVENT_BNAMED_COMMENTED_COMPONENT_ELEMENT);
			if (component==null ){
				Activator.getDefault().getLog().log(new Status(Status.WARNING, Activator.PLUGIN_ID, 
						"Attempt to Generate All diagrams was ignored because element is not in a component\n"+
						"Element: "+element, null));
				report = report+ "\nGeneration ABORTED - see Error Log";
				return report;
			}
			if (component.eIsProxy()){
				component =  EcoreUtil.resolve(component, editingDomain.getResourceSet()); // EMFRodinDB.INSTANCE.loadElement(component.getURI());
			}
			//First delete all generated elements (i.e. all elements with a generated by id attribute)
			//command to delete any model elements that have been generated from the diagram element
			// (this command is provided by the generator plug-in)
			DeleteGeneratedCommand deleteGeneratedCommand = new DeleteGeneratedCommand(editingDomain, component);
			try {
				//try to execute the command to delete generated elements
				deleteGeneratedCommand.execute(null, null);
				//save the model
//				component.eResource().save(Collections.emptyMap());
			} catch (Exception e) {
				Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, 
						"Could not delete generated elements, generation aborted "+
								"(component = "+((EventBNamed)component).getName()+")"
								, e));
				report = report+ "\nGeneration ABORTED - see Error Log";
				return report;
			}

			// now re-generate from all root diagram elements
			generateList = new ArrayList<EventBElement>();
			getDiagramRoots(component, null);
			for (EventBElement eventBElement : generateList){
				if (eventBElement.eIsProxy()){
					eventBElement =  (EventBElement) EcoreUtil.resolve(eventBElement, editingDomain.getResourceSet());
				}
				if (validateElement(eventBElement, shell)){ //validate this element
					final ImportCommand importCommand = new ImportCommand(
							editingDomain, 
							eventBElement); 
					if (importCommand.canExecute()) {	
						// run with progress
						ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
						try {
							dialog.run(false, true, new IRunnableWithProgress(){
							     public void run(IProgressMonitor monitor) { 
							    	 monitor.beginTask(Messages.GENERATOR_MSG_05, IProgressMonitor.UNKNOWN);
							         try {
							        	 importCommand.execute(monitor, info);
							         } catch (ExecutionException e) {
										Activator.logError(Messages.GENERATOR_MSG_06, e);
							         }
							         monitor.done();
							     }
							 });
						} catch (InvocationTargetException e) {
							Activator.logError(Messages.GENERATOR_MSG_07, e);
							report = report+ "\nGeneration ABORTED - see Error Log";
							return report;
						} catch (InterruptedException e) {
							Activator.logError(Messages.GENERATOR_MSG_08, e);
							report = report+ "\nGeneration ABORTED - see Error Log";
							return report;
						} 
	
						// error feedback
						if (false == importCommand.getCommandResult().getStatus().isOK()) {

							MessageDialog
									.openError(shell,
											Messages.GENERATOR_MSG_09,
											Messages.GENERATOR_MSG_10);
							report = report+"\nGeneration FAILED for "+eventBElement.getReference();
						}else{
							report = report+"\nGeneration SUCCEEDED for "+eventBElement.getReference();							
						}
					}
				}else{
					report = report+"\nValidation FAILED for "+eventBElement.getReference();
				}
			}
		}
		return report;
	}

	private static boolean validateElement(EventBElement eventBElement, Shell shell){
	    Diagnostic diagnostic = Diagnostician.INSTANCE.validate(eventBElement);
	    if (diagnostic.getSeverity() == Diagnostic.ERROR || diagnostic.getSeverity() == Diagnostic.WARNING){
	      MessageDialog.openError(shell,
	    		  Messages.GENERATOR_MSG_12(eventBElement), 
	    		  Messages.GENERATOR_MSG_17(eventBElement));
	      return false;
	    }
	    return true;
	  }
	  
	  
	/* This recursively traverses the contents tree looking for the root diagram elements that can be generated
	 * Note that generate-able elements may be nested in others and not these should not be generated unless 
	 * there is an intermediate generate-able element of another type
	 */
	private void getDiagramRoots(EObject element, EClass lastType) {
		if (element instanceof EventBElement && element.eClass()!=lastType && ImporterFactory.getFactory().canGenerate(element.eClass())){
			generateList.add((EventBElement)element);
			lastType = element.eClass();
		}
		for (EObject eObject : element.eContents()){
			getDiagramRoots(eObject, lastType);
		}
	
	}
}
