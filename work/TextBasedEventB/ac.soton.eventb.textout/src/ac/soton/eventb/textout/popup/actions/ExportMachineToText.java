package ac.soton.eventb.textout.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eventb.core.IMachineRoot;
import org.eventb.core.basis.MachineRoot;

import ac.soton.eventb.textout.core.ExportTextManager;

public class ExportMachineToText implements IObjectActionDelegate {

	public Shell shell;
	public IStructuredSelection selection;

	/**
	 * Constructor for Action1.
	 */
	public ExportMachineToText() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		Object object = selection.getFirstElement();
		if (object.getClass() == MachineRoot.class) {
			IMachineRoot machineRoot = (IMachineRoot) object;
			try {
				new ExportTextManager().export(machineRoot);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = (IStructuredSelection) selection;
	}

}
