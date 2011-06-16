package nz.co.iswe.eclipse.plugin.popup.actions;

import java.util.ArrayList;
import java.util.List;

import nz.co.iswe.generator.impl.eclipse.EclipseGeneratorContext;
import nz.co.iswe.generator.impl.eclipse.IEclipseEntityInfoFactory;
import nz.co.iswe.generator.impl.eclipse.IEclipseGeneratorContext;
import nz.co.iswe.generator.info.EntityInfo;
import nz.co.iswe.generator.info.EntityInfoFactory;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;

public class GenerateModelClasses implements IObjectActionDelegate, IViewActionDelegate {

	private Shell		shell;

	private IViewPart	viewPart;

	private IProject project;
	
	private List<IType> selectedEntities	= new ArrayList<IType>();

	/**
	 * Constructor for Action1.
	 */
	public GenerateModelClasses() {
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
		
		if(selectedEntities.size() == 0){
			MessageDialog.openInformation(shell, "ISWE Framework - Generator Plugin", "No entities found !");
			return;
		}
		
		if( project == null ){
			MessageDialog.openInformation(shell, "ISWE Framework - Generator Plugin", "No project metadata found. Action cannot be executed !");
			return;
		}
		
		//Load the generator context
		IEclipseGeneratorContext context = EclipseGeneratorContext.getInstance();
		
		//Loading all entities meta-data
		IEclipseEntityInfoFactory factory = getEclipseEntityInfoFactory();
		factory.loadAllEntities();
		
		//load the meta-data for the selected entities
		List<EntityInfo> entityList = factory.getEntityList(selectedEntities);
		
		//TEST ONLY
		entityList.get(0).getPropertyList();
		
		
		try {
			context.run("Model", entityList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private IEclipseEntityInfoFactory getEclipseEntityInfoFactory() {
		return (IEclipseEntityInfoFactory)EntityInfoFactory.getInstance();
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		setSelectedElements(selection);
	}

	@SuppressWarnings("rawtypes")
	protected void setSelectedElements(ISelection selection) {
		selectedEntities.clear();

		List selectedElements = null;
		if (selection != null && selection instanceof IStructuredSelection) {
			selectedElements = ((IStructuredSelection) selection).toList();
		}
		
		if (selectedElements == null) {
			return;
		}

		for (Object i : selectedElements) {

			if(project == null){
				if (i instanceof ICompilationUnit) {
	                project = ((ICompilationUnit) i).getResource().getProject();
	            }
				else if (i instanceof IResource) {
	                project = ((IResource) i).getProject();
	            }
				else if (i instanceof IProject) {
					project = (IProject)i;
				}
				if(project != null){
					IEclipseGeneratorContext context = EclipseGeneratorContext.getInstance();
					//set the project to be used in the Generator Context
					context.setProject(project);
					context.init();
				}
			}
			
			if (i instanceof ICompilationUnit) {
				addTargetElements((ICompilationUnit) i);
			}
			else if (i instanceof IType) {
				addTargetElements((IType)i);
			}
			
		}
	}

	/**
	 * @param i
	 */
	protected void addTargetElements(ICompilationUnit i) {
		try {
			IType[] types = i.getAllTypes();
			for (IType type : types) {
				addTargetElements(type);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addTargetElements(IType type) {
		IEclipseEntityInfoFactory factory = getEclipseEntityInfoFactory();
		if(factory.isValidEntityType(type)){
			selectedEntities.add(type);
		}
	}

	@Override
	public void init(IViewPart viewPart) {
		this.viewPart = viewPart;
	}

}
