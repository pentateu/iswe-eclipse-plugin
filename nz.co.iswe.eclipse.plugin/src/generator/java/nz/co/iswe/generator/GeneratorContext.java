package nz.co.iswe.generator;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import nz.co.iswe.generator.config.ConfigurationContext;
import nz.co.iswe.generator.config.GlobalPropertiesResolver;
import nz.co.iswe.generator.config.xml.GeneratorConfig;
import nz.co.iswe.generator.config.xml.GeneratorContextConfig;
import nz.co.iswe.generator.config.xml.GroupConfig;
import nz.co.iswe.generator.config.xml.HelperConfig;
import nz.co.iswe.generator.config.xml.ListOfGroups;
import nz.co.iswe.generator.config.xml.ListOfProperties;
import nz.co.iswe.generator.config.xml.ReferenceGenerator;
import nz.co.iswe.generator.helper.IHelper;
import nz.co.iswe.generator.info.EntityInfo;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.velocity.app.Velocity;
import org.w3c.dom.Element;

public abstract class GeneratorContext implements IGeneratorContext {

	final String FILE_RESOURCE_LOADER_PATH = "file.resource.loader.path";
	
	// #############################################//
	// ## Static members
	// #############################################//
	private static final ThreadLocal<IGeneratorContext> threadLocalInstance = new ThreadLocal<IGeneratorContext>();	
	
	@SuppressWarnings("unchecked")
	protected static void createInstance(GeneratorContextConfig config) {
		String implClass = config.getGeneratorContext();

		if (implClass == null) {
			throw new RuntimeException("GeneratorContext.implClass is null!");
		}
		IGeneratorContext instance = null;

		// creating a new instance of the GeneratorContext
		try {
			Class<IGeneratorContext> clazz = (Class<IGeneratorContext>) Class
					.forName(implClass);
			instance = (IGeneratorContext) clazz.newInstance();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(
					"GeneratorContext.implClass class not found!", e);
		} catch (InstantiationException e) {
			throw new RuntimeException(
					"GeneratorContext.implClass class instantiation error!", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(
					"GeneratorContext.implClass class instantiation error!", e);
		}

		// set the configuration into the new instance of the GeneratorContext
		instance.addConfiguration(config);

		threadLocalInstance.set(instance);
	}

	protected static boolean instanceLoaded() {
		return (threadLocalInstance.get() != null);
	}

	public static IGeneratorContext getInstance() {
		if (threadLocalInstance.get() == null) {
			throw new RuntimeException(
					"GeneratorContext instance not ready! Method GeneratorContext.createInstance not invoked!");
		}
		return threadLocalInstance.get();
	}

	// #############################################//
	// ## Instance members
	// #############################################//

	// hold the configuration
	private GeneratorContextConfig configuration = null; //must be kept private and be accessed through the get method
	
	//hold the Global Property Resolver
	private GlobalPropertiesResolver globalPropertiesContext = null;//must be kept private and be accessed through the get method

	private Map<String, IGenerator> cachedGenerators = new HashMap<String, IGenerator>();
	private Map<String, IHelper> cachedHelpersById = new HashMap<String, IHelper>();
	private Map<String, IHelper> cachedHelpersByName = new HashMap<String, IHelper>();

	public void addConfiguration(GeneratorContextConfig newConfiguration) {
		if (this.configuration == null) {
			this.configuration = newConfiguration;
		} else {
			ConfigurationContext merger = ConfigurationContext.getInstance();
			this.configuration = merger.merge(this.configuration,
					newConfiguration);
		}
	}

	public GeneratorContextConfig getConfiguration() {
		if (configuration == null) {
			throw new RuntimeException(
					"GeneratorContext.configuration not loaded!");
		}
		return configuration;
	}
	
	@Override
	public GlobalPropertiesResolver getGlobalPropertiesResolver() {
		if(globalPropertiesContext == null){
			globalPropertiesContext = new GlobalPropertiesResolver(getConfiguration());
			initGlobalPropertiesContext(globalPropertiesContext);
		}
		return globalPropertiesContext;
	}

	/**
	 * Method that can be override to populate the context of the GlobalPropertiesResolver object
	 */
	protected void initGlobalPropertiesContext(GlobalPropertiesResolver globalPropertiesContext) {
		
	}

	public String getProperty(String path) {

		getConfiguration();//make sure it is available
		
		String value = null;

		try {
			value = BeanUtils.getProperty(getConfiguration(), path);
		} catch (Exception e) {
			// try to get the property from the generic property list
			ListOfProperties properties = configuration.getProperties();

			List<Element> elements = properties.getAny();
			for (Element element : elements) {
				if (element.getNodeName().equals(path)) {
					return element.getTextContent();
				}
			}

			// throw new
			// RuntimeException("GeneratorContext.getPropertyAsString error accessing property: "
			// + path);
		}

		return value;
	}

	@Override
	public void run(String group, List<EntityInfo> entityList) throws Exception {
		beforeRun(group);

		// look for the group name into the configuration
		GroupConfig groupConfig = getGroupConfig(group);

		if (groupConfig != null) {

			// for each entity
			for (EntityInfo entityInfo : entityList) {

				// OneToOne Generators : for each Generator in the group
				for (ReferenceGenerator referenceGenerator : groupConfig
						.getGenerator()) {
					GeneratorConfig generatorConfig = getOneToOneGeneratorConfig(referenceGenerator);
					if (generatorConfig != null) {
						IGenerator generator = getGenerator(generatorConfig);
						try {
							initGenerator(generator);
							processGenerator(generator, entityInfo);
						} finally {
							endGenerator(generator);
						}
					}
				}

				// ManyToOne Generators
				for (ReferenceGenerator referenceGenerator : groupConfig
						.getGenerator()) {
					GeneratorConfig generatorConfig = getManyToOneGeneratorConfig(referenceGenerator);
					if (generatorConfig != null) {
						IGenerator generator = getGenerator(generatorConfig);
						try {
							initGenerator(generator);
							processGenerator(generator, entityInfo);
						} finally {
							endGenerator(generator);
						}
					}
				}

			}
		}
		afterRun(group);
	}

	/**
	 * Methods invoked after the body of the method run is executed
	 * 
	 * @param group
	 */
	protected void afterRun(String group) {

	}

	/**
	 * Methods invoked before the body of the method run is executed
	 * 
	 * @param group
	 */
	protected void beforeRun(String group) {
		// TODO Auto-generated method stub

	}

	protected void endGenerator(IGenerator generator) {
		generator.end();
	}

	protected void processGenerator(IGenerator generator, EntityInfo entityInfo)
			throws Exception {
		generator.process(entityInfo);
	}

	protected void initGenerator(IGenerator generator) {
		generator.setGeneratorContext(this);
		generator.init();
	}

	@Override
	public void init() {
		// load all helpers
		loadHelpers();

		setupVelocity();
	}

	protected void setupVelocity() {

		//
		String defaultTemplates = getGlobalPropertiesResolver().get("velocity.default.templates");

		String pathsVelocity = defaultTemplates + ",";

		Properties velocityProps = new Properties();

		File pastaRaiz = new File(defaultTemplates);
		if (pastaRaiz.exists() && pastaRaiz.isDirectory()) {
			File[] subPastas = pastaRaiz.listFiles();
			for (File sub : subPastas) {
				if (sub.isDirectory()) {
					pathsVelocity += sub.getPath() + ",";
				}
			}
		}
		// Remove a ultima virgula se existir if(pathsVelocity.length() > 0){
		pathsVelocity = pathsVelocity.substring(0, pathsVelocity.length() - 1);

		velocityProps.setProperty(FILE_RESOURCE_LOADER_PATH, pathsVelocity);

		Velocity.init(velocityProps);
	}

	protected void loadHelpers() {
		for (HelperConfig helperConfig : configuration.getHelpers().getHelper()) {
			// get the helper config and load it to the local cache
			getHelperById(helperConfig.getId());
		}
	}

	/**
	 * Return a IGenerator instance for the configuration provided.
	 * 
	 * @param generatorConfig
	 * @return
	 */
	protected IGenerator getGenerator(GeneratorConfig generatorConfig) {

		IGenerator generator = cachedGenerators.get(generatorConfig.getId());

		if (generator == null) {

			// instantiate the generator
			String clazz = generatorConfig.getClazz();
			try {
				generator = (IGenerator) loadClass(clazz).newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(
						"Error instantiating the Generator. clazz: " + clazz, e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(
						"Error instantiating the Generator. clazz: " + clazz, e);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(
						"Class not found for Generator. clazz: " + clazz, e);
			}
			cachedGenerators.put(generatorConfig.getId(), generator);
			setupGenerator(generator, generatorConfig);
		}

		return generator;
	}

	protected void setupGenerator(IGenerator generator,
			GeneratorConfig generatorConfig) {
		generator.setGeneratorConfig(generatorConfig);
	}

	@SuppressWarnings("rawtypes")
	protected Class loadClass(String clazz) throws ClassNotFoundException {
		return Thread.currentThread().getContextClassLoader().loadClass(clazz);
	}

	@Override
	public IHelper getHelperByName(String name) {
		return cachedHelpersByName.get(name);
	}

	/**
	 * Return a helper instance based on the id.
	 */
	@Override
	public IHelper getHelperById(String id) {

		IHelper helper = cachedHelpersById.get(id);

		if (helper == null) {
			HelperConfig helperConfig = getHelperConfig(id);

			try {
				helper = (IHelper) loadClass(helperConfig.getClazz())
						.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(
						"Error instantiating the Helper. clazz: "
								+ helperConfig.getClazz(), e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(
						"Error instantiating the Helper. clazz: "
								+ helperConfig.getClazz(), e);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(
						"Class not found for Helper. clazz: "
								+ helperConfig.getClazz(), e);
			}

			helper.setGeneratorContext(this);
			helper.setHelperConfig(helperConfig);

			cachedHelpersById.put(id, helper);
			cachedHelpersByName.put(helper.getName(), helper);
		}

		return helper;
	}

	/**
	 * Locate the helper by the id in the configuration and return it.
	 */
	protected HelperConfig getHelperConfig(String id) {
		for (HelperConfig helperConfig : configuration.getHelpers().getHelper()) {
			if (helperConfig.getId().equals(id)) {
				return helperConfig;
			}
		}
		return null;
	}

	/**
	 * Look for a Generator definition in the OneToOne list
	 * 
	 * @param referenceGenerator
	 * @return
	 */
	private GeneratorConfig getOneToOneGeneratorConfig(
			ReferenceGenerator referenceGenerator) {
		for (GeneratorConfig generatorConfig : configuration.getOneToOne()
				.getGenerator()) {
			if (generatorConfig.getId().equals(referenceGenerator.getRef())) {
				return generatorConfig;
			}
		}
		return null;
	}

	/**
	 * Look for a Generator definition in the ManyToOne list
	 * 
	 * @param referenceGenerator
	 * @return
	 */
	private GeneratorConfig getManyToOneGeneratorConfig(
			ReferenceGenerator referenceGenerator) {
		for (GeneratorConfig generatorConfig : configuration.getManyToOne()
				.getGenerator()) {
			if (generatorConfig.getId().equals(referenceGenerator.getRef())) {
				return generatorConfig;
			}
		}
		return null;
	}

	private GroupConfig getGroupConfig(String group) {
		ListOfGroups groups = configuration.getGroups();
		for (GroupConfig groupConfig : groups.getGroup()) {
			if (groupConfig.getId().equals(group)) {
				return groupConfig;
			}
		}
		return null;
	}

}
