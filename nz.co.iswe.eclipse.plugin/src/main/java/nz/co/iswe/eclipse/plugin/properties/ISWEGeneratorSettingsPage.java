package nz.co.iswe.eclipse.plugin.properties;

import nz.co.iswe.eclipse.plugin.ISWEPlugin;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ISWEGeneratorSettingsPage extends FieldEditorOverlayPage implements
		IWorkbenchPreferencePage {

	public static final String PAGE_ID = "nz.co.iswe.eclipse.plugin.properties.ISWEGeneratorSettingsPage.defaultPreferences";

	public static final String PROJECT_XML_CONFIG = "iswe.generator.project.xml.config";
	
	//private static final String BASE_DIR = "gerador.pasta.base.saida";

	private FileFieldEditor projectConfigurationFile;
	
	private DirectoryFieldEditor standarTemplatesFolder;
	private DirectoryFieldEditor projectTemplatesFolder;
	
	/*
	private DirectoryFieldEditor baseDirectory;
	private DirectoryFieldEditor hibernateConfigOutput;
	private DirectoryFieldEditor entitiesOutput;
	private DirectoryFieldEditor daoOutput;
	private DirectoryFieldEditor boOutput;
	private DirectoryFieldEditor controllerOutput;
	private DirectoryFieldEditor gwtUIOutput;
	
	*/
	
	public ISWEGeneratorSettingsPage() {
		super(GRID);
	}

	/*
	 * public IPreferenceStore doGetPreferenceStore() { return
	 * SpellCheckerPlugin.getDefault().getPreferenceStore(); }
	 */
	public void init(IWorkbench workbench) {
		setDescription("TExt form the init method ...");
	}

	protected String getPageId() {
		return PAGE_ID;
	}

	private void addSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		separator.setLayoutData(gridData);
	}

	public IPreferenceStore doGetPreferenceStore() {
		return ISWEPlugin.getDefault().getPreferenceStore();
	}

	public void createFieldEditors() {

		Composite parent = getFieldEditorParent();

		if (isPropertyPage()) {
			// Project specific settings
			projectConfigurationFile = new FileFieldEditor(PROJECT_XML_CONFIG, 
					"&Project Configuration File : ", 
					parent);
			addField(projectConfigurationFile);
			
			projectTemplatesFolder = new DirectoryFieldEditor(
					"velocity.file.resource.loader.path",
					"&Project Templates Folder : ", parent);
			addField(projectTemplatesFolder);
			
			/*
			baseDirectory = new DirectoryFieldEditor(BASE_DIR, "&Base Folder",
					parent);
			addField(baseDirectory);

			// Controller
			controllerOutput = new DirectoryFieldEditor(
					"gerador.controller.pasta.saida",
					"&Controller Output Folder", parent);
			addField(controllerOutput);

			boOutput = new DirectoryFieldEditor("gerador.bo.pasta.saida",
					"&BO Output Folder", parent);
			addField(boOutput);

			daoOutput = new DirectoryFieldEditor("gerador.dao.pasta.saida",
					"&DAO Output Folder", parent);
			addField(daoOutput);

			hibernateConfigOutput = new DirectoryFieldEditor(
					"gerador.configs.hibernate.pasta.saida",
					"&Hibernate Configuration Output Folder", parent);
			addField(hibernateConfigOutput);

			entitiesOutput = new DirectoryFieldEditor(
					"gerador.entity.pasta.saida", "&Entities Output Folder",
					parent);
			addField(entitiesOutput);

			gwtUIOutput = new DirectoryFieldEditor("gerador.gwt.pasta.saida",
					"&GWT UI Output Folder", parent);
			addField(gwtUIOutput);

			// addSeparator(parent);

			
			
			//Entities Packages
			ListEditor entityPackageList = new ListEditor("generator.entity.packages",
					"&Entities Packages", parent) {
				@Override
				protected String[] parseString(String stringValue) {
					return stringValue.split(";");
				}
				
				@Override
				protected String getNewInputObject() {
					// TODO Auto-generated method stub
					return "getNewInputObject";
				}
				
				@Override
				protected String createList(String[] array) {
					
					if(array == null || array.length == 0){
						return "";
					}
					
					StringBuilder builder = new StringBuilder();
					
					for (int i = 0; i < array.length; i++) {
						builder.append(array[i]);
						if(i < (array.length - 1)){
							builder.append(";");
						}
					}
					
					
					return "";
				}
				
			};
			addField(entityPackageList);
			*/
		}

		// Non project specific settings
		standarTemplatesFolder = new DirectoryFieldEditor(
				"velocity.file.resource.loader.root.path",
				"&Standard Templates Folder : ", parent);
		addField(standarTemplatesFolder);

		/*
		addField(new StringFieldEditor("gerador.abstract.pakage",
				"&Abstract Object Package Sufix", parent));

		addField(new StringFieldEditor("gerador.abstract.pakage",
				"Abstract Object Package Sufix", parent));
		 */
	}

	@Override
	protected void performDefaults() {
		super.performDefaults();
/*
		IProject project = ((IResource) getElement()).getProject();

		IWorkspace workspace = project.getWorkspace();

		
		String baseDir = workspace.getRoot().getLocation().toPortableString();
		baseDirectory.setStringValue(baseDir);
		*/
	}

}
