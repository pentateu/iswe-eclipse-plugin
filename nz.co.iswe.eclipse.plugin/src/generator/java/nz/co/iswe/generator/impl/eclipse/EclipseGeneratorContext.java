package nz.co.iswe.generator.impl.eclipse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nz.co.iswe.eclipse.plugin.ISWEPlugin;
import nz.co.iswe.eclipse.plugin.PluginUtil;
import nz.co.iswe.eclipse.plugin.properties.ISWEGeneratorSettingsPage;
import nz.co.iswe.generator.GeneratorContext;
import nz.co.iswe.generator.IGenerator;
import nz.co.iswe.generator.IGeneratorHandler;
import nz.co.iswe.generator.config.ConfigurationContext;
import nz.co.iswe.generator.config.GlobalPropertiesResolver;
import nz.co.iswe.generator.config.xml.GeneratorConfig;
import nz.co.iswe.generator.config.xml.GeneratorContextConfig;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;

public class EclipseGeneratorContext extends GeneratorContext implements
		IEclipseGeneratorContext {
	private static final String DEFAULT_XML = "META-INF/default-eclipse-iswe-generator.xml";
	private static final String JAVANATURE = "org.eclipse.jdt.core.javanature";

	/*
	 * private static final String VELOCITY_FILE_RESOURCE_LOADER_CACHE =
	 * "velocity.file.resource.loader.cache"; private static final String
	 * VELOCITY_FILE_RESOURCE_LOADER_PATH =
	 * "velocity.file.resource.loader.path"; private static final String
	 * FILE_RESOURCE_LOADER_CACHE = "file.resource.loader.cache"; private static
	 * 
	 */
	
	public static IEclipseGeneratorContext getInstance() {
		if (!instanceLoaded()) {
			// get the default configuration xml file
			InputStream defaultEclipseConfig = Thread.currentThread()
					.getContextClassLoader().getResourceAsStream(DEFAULT_XML);
			
			ConfigurationContext configurationContext = ConfigurationContext.getInstance();
			
			GeneratorContextConfig config = configurationContext.loadGeneratorContextConfig(defaultEclipseConfig);
			
			GeneratorContext.createInstance(config);
		}
		return (IEclipseGeneratorContext) GeneratorContext.getInstance();
	}

	private IJavaProject project;

	private IGeneratorHandler generatorHandler;

	private boolean projectConfigLoaded = false;

	@Override
	protected void initGlobalPropertiesContext(GlobalPropertiesResolver globalPropertiesContext) {
		//add the project path to the context
		String projectRootPath = project.getProject().getRawLocation().toOSString();
	
		globalPropertiesContext.put("project_root_path", projectRootPath);
		
		
	}
	
	@Override
	protected void afterRun(String group) {
		/*
		//after each run refresh the project
		try {
			project.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
		}
		catch (CoreException e) {
			//error doing refresh at the project after the artfacts have been generated
			//TODO: Log using the proper Eclipse log
		}
		*/
	}
	
	@Override
	public void setProject(IProject project) throws CoreException {
		if (project.isNatureEnabled(JAVANATURE)) {
			this.project = JavaCore.create(project);
			// load the project specific config
			if (!projectConfigLoaded) {
				GeneratorContextConfig projectConfiguration = lookupProjectConfiguration();
				if (projectConfiguration != null) {
					addConfiguration(projectConfiguration);
				}
				projectConfigLoaded = true;
			}
		}
		else{
			throw new RuntimeException("ISWE Pluing is only supported for project with Java Nature");
		}
	}

	private GeneratorContextConfig lookupProjectConfiguration() {
		if (project == null) {
			return null;
		}

		String projectConfigFile = PluginUtil.getOverlayedPreferenceValue(
				ISWEPlugin.getDefault().getPreferenceStore(), project.getProject(),
				ISWEGeneratorSettingsPage.PAGE_ID,
				ISWEGeneratorSettingsPage.PROJECT_XML_CONFIG);

		if (projectConfigFile != null && projectConfigFile.trim().length() > 0) {
				
				ConfigurationContext configurationContext = ConfigurationContext.getInstance();
				
				GeneratorContextConfig config = configurationContext.loadGeneratorContextConfig(projectConfigFile);
				
				return config;
		}
		return null;
	}

	@Override
	protected void setupGenerator(IGenerator generator,
			GeneratorConfig generatorConfig) {
		super.setupGenerator(generator, generatorConfig);
		generator.setGeneratorHandler(generatorHandler);
	}

	@Override
	public List<IType> queryTypes(ITypeFilter filter) {

		if (filter == null) {
			return null;
		}

		List<IType> result = new ArrayList<IType>();

		//
		try { 
			IPackageFragment[] packages = project.getPackageFragments();

			for (IPackageFragment pkg : packages) {
				// Package fragments include all packages in the
				// classpath
				// We will only look at the package from the source
				// folder
				// K_BINARY would include also included JARS, e.g.
				// rt.jar
				if (pkg.getKind() == IPackageFragmentRoot.K_SOURCE) {
					for (ICompilationUnit unit : pkg.getCompilationUnits()) {

						for (IType type : unit.getTypes()) {
							if (filter.match(type)) {
								result.add(type);
							}
						}
					}
				}
			}

			
		} catch (CoreException e) {
			// Do nothing
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public void init() {
		if (generatorHandler == null) {
			generatorHandler = new EclipseGeneratorHandler(project);
		}
		super.init();
	}

	public IGeneratorHandler getGeneratorHandler() {
		return generatorHandler;
	}

	/*
	 * private void setupVelocity() throws Exception {
	 * 
	 * // Velocity properties Properties velocityProps = new Properties();
	 * 
	 * String pathsVelocity = "";
	 * 
	 * //Varre as pastas para configurar o velocity File pastaRaiz = new File(
	 * getProperty("velocity.file.resource.loader.root.path") );
	 * if(pastaRaiz.exists() && pastaRaiz.isDirectory()){ File [] subPastas =
	 * pastaRaiz.listFiles(); for (File sub : subPastas) {
	 * if(sub.isDirectory()){ pathsVelocity += sub.getPath() + ","; } } } String
	 * temp = getProperty(VELOCITY_FILE_RESOURCE_LOADER_PATH); if(temp != null
	 * && !"".equals(temp)) { pathsVelocity += temp + ","; }
	 * 
	 * //Remove a ultima virgula se existir if(pathsVelocity.length() > 0){
	 * pathsVelocity = pathsVelocity.substring(0, pathsVelocity.length()-1);
	 * velocityProps.setProperty(FILE_RESOURCE_LOADER_PATH, pathsVelocity); }
	 * 
	 * velocityProps.setProperty(FILE_RESOURCE_LOADER_CACHE,
	 * getProperty(VELOCITY_FILE_RESOURCE_LOADER_CACHE));
	 * 
	 * Velocity.init(velocityProps); }
	 */

}
