package nz.co.iswe.generator.config;

import java.util.Properties;

import junit.framework.Assert;

import nz.co.iswe.generator.config.xml.GeneratorConfig;
import nz.co.iswe.generator.config.xml.GeneratorContextConfig;
import nz.co.iswe.generator.config.xml.HelperConfig;
import nz.co.iswe.generator.test.TestSuitConfig;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

public class ConfigurationContextTest {

	private String pluginProjectPath = null;

	private ConfigurationContext configurationContext = null;

	private GeneratorContextConfig defaultSettings = null;

	private GeneratorContextConfig projectSettings = null;

	@Before
	public void setup() {
		Properties properties = TestSuitConfig.getTestProperties();
		pluginProjectPath = properties.getProperty("iswe-plugin-project.path");

		configurationContext = ConfigurationContext.getInstance();

		// load default settings
		defaultSettings = configurationContext.loadGeneratorContextConfig(pluginProjectPath
				+ "/src/test/resources/default-eclipse-iswe-generator.xml");

		// load project specific settings
		projectSettings = configurationContext.loadGeneratorContextConfig(pluginProjectPath
				+ "/src/test/resources/project-iswe-generator.xml");

	}

	@Test
	public void testDefaultConfig() {
		// asserts on the original configuration
		Assert.assertEquals("original entityInfoFactory", defaultSettings.getEntityInfoFactory());
		Assert.assertEquals("original generatorContext", defaultSettings.getGeneratorContext());

		// ## <groups> ## //
		assertDefaultGroups(defaultSettings);
		// ## </groups> ## //

		// ## <oneToOne> ## //
		assertDefaultOneToOne(defaultSettings);
		// ## </oneToOne> ## //

		// ## <manyToOne> ## //
		assertDefaultManyToOne(defaultSettings);
		// ## </manyToOne> ## //

		// ## <helpers> ## //
		assertDefaultHelpers(defaultSettings);
		// ## </helpers> ## //
		
		// ## <properties> ## //
		assertDefaultProperties(defaultSettings);
		// ## </properties> ## //
	}

	

	@Test
	public void testMerge() {

		configurationContext.merge(defaultSettings, projectSettings);

		// ### assert merged result ###//

		// ## <groups> ## //
		assertMergedGroups(defaultSettings);
		// ## </groups> ## //

		// ## <oneToOne> ## //
		assertMergedOneToOne(defaultSettings);
		// ## </oneToOne> ## //

		// ## <manyToOne> ## //
		assertMergedManyToOne(defaultSettings);
		// ## </manyToOne> ## //
		
		// ## <helpers> ## //
		assertMergedHelpers(defaultSettings);
		// ## </helpers> ## //
		
		// ## <properties> ## //
		assertMergedProperties(defaultSettings);
		// ## </properties> ## //
	}
	
	private void assertMergedProperties(GeneratorContextConfig settings) {
		
		Assert.assertNotNull("properties", settings.getProperties());
		Assert.assertEquals("properties list size", 1, settings.getProperties().getAny().size());

		Element property = settings.getProperties().getAny().get(0);
		Assert.assertEquals("property name", "velocity.default.templates", property.getNodeName());
		Assert.assertEquals("property value", "./customTemplates", property.getTextContent().trim());
		
	}
	
	private void assertDefaultProperties(GeneratorContextConfig settings) {
		
		Assert.assertNotNull("properties", settings.getProperties());
		Assert.assertEquals("properties list size", 1, settings.getProperties().getAny().size());

		Element property = settings.getProperties().getAny().get(0);
		Assert.assertEquals("property name", "velocity.default.templates", property.getNodeName());
		Assert.assertEquals("property value", "./templates", property.getTextContent().trim());
		
	}

	private void assertMergedHelpers(GeneratorContextConfig settings) {
		Assert.assertNotNull("helpers", settings.getHelpers());
		Assert.assertEquals("helpers list size", 2, settings.getHelpers().getHelper().size());

		// ##################################################################### //
		// ## Helper 1 ## //
		// ##################################################################### //
		HelperConfig helperConfig = settings.getHelpers().getHelper().get(0);
		Assert.assertEquals("helper - id", "JpaDAOHelper", helperConfig.getId());
		Assert.assertEquals("helper - class", "nz.co.iswe.generator.impl.helper.DAOHelper", helperConfig.getClazz());

		Assert.assertNotNull("helpers 1 - properties", helperConfig.getProperties());

		Assert.assertEquals("properties items", 5, helperConfig.getProperties().getAny()
				.size());

		Element property = helperConfig.getProperties().getAny().get(0);
		Assert.assertEquals("property 1 name", "packageFrom", property.getNodeName());
		Assert.assertEquals("property 1 value", "entity", property.getTextContent().trim());

		property = helperConfig.getProperties().getAny().get(1);
		Assert.assertEquals("property 2 name", "packageTo", property.getNodeName());
		Assert.assertEquals("property 2 value", "dao", property.getTextContent().trim());

		property = helperConfig.getProperties().getAny().get(2);
		Assert.assertEquals("property 3 name", "abstract.fullyQualifiedName", property.getNodeName());
		Assert.assertEquals("property 3 value", "${dao.package}.${entity.simpleName}DAOAbstract", property.getTextContent().trim());

		property = helperConfig.getProperties().getAny().get(3);
		Assert.assertEquals("property 4 name", "interface.fullyQualifiedName", property.getNodeName());
		Assert.assertEquals("property 4 value", "${dao.package}.I${entity.simpleName}DAO", property.getTextContent().trim());

		property = helperConfig.getProperties().getAny().get(4);
		Assert.assertEquals("property 5 name", "impl.fullyQualifiedName", property.getNodeName());
		Assert.assertEquals("property 5 value", "${dao.package}.${entity.simpleName}DAOImpl", property.getTextContent().trim());
		
		// ##################################################################### //
		// ## Helper 2 ## //
		// ##################################################################### //
		helperConfig = settings.getHelpers().getHelper().get(1);
		Assert.assertEquals("helper - id", "BOHelper", helperConfig.getId());
		Assert.assertEquals("helper - class", "nz.co.iswe.generator.impl.helper.BOHelper", helperConfig.getClazz());

		Assert.assertNotNull("helpers - properties", helperConfig.getProperties());

		Assert.assertEquals("properties items", 6, helperConfig.getProperties().getAny().size());

		property = helperConfig.getProperties().getAny().get(0);
		Assert.assertEquals("property 1 name", "packageFrom", property.getNodeName());
		Assert.assertEquals("property 1 value", "entity", property.getTextContent().trim());

		property = helperConfig.getProperties().getAny().get(1);
		Assert.assertEquals("property 2 name", "packageTo", property.getNodeName());
		Assert.assertEquals("property 2 value", "bo", property.getTextContent().trim());

		property = helperConfig.getProperties().getAny().get(2);
		Assert.assertEquals("property 3 name", "abstract.fullyQualifiedName", property.getNodeName());
		Assert.assertEquals("property 3 value", "${bo.package}.${entity.simpleName}BOAbstract", property.getTextContent().trim());

		property = helperConfig.getProperties().getAny().get(3);
		Assert.assertEquals("property 4 name", "interface.fullyQualifiedName", property.getNodeName());
		Assert.assertEquals("property 4 value", "${bo.package}.I${entity.simpleName}BO", property.getTextContent().trim());

		property = helperConfig.getProperties().getAny().get(4);
		Assert.assertEquals("property 5 name", "impl.fullyQualifiedName", property.getNodeName());
		Assert.assertEquals("property 5 value", "${bo.package}.${entity.simpleName}BOImpl", property.getTextContent().trim());
	
		property = helperConfig.getProperties().getAny().get(5);
		Assert.assertEquals("property 5 name", "testProperty", property.getNodeName());
		Assert.assertEquals("property 5 value", "test value", property.getTextContent().trim());
	}

	private void assertMergedManyToOne(GeneratorContextConfig settings) {
		Assert.assertNotNull("manyToOne", settings.getManyToOne());

		Assert.assertEquals("2 manyToOne items", 2, settings.getManyToOne().getGenerator().size());

		// ######################################################## //
		// generator 1
		// ######################################################## //
		GeneratorConfig generatorConfig = settings.getManyToOne().getGenerator().get(0);
		Assert.assertEquals("first generator id", 
							"HibernateConfig", 
							generatorConfig.getId());
		Assert.assertEquals("first generator class", 
							"co.nz.iswe.generator.impl.hibrenate.HibernateConfig", 
							generatorConfig.getClazz());
		// input
		Assert.assertNotNull("input", generatorConfig.getInput());
		Assert.assertEquals("input", "velocity", generatorConfig.getInput().getType());
		Assert.assertEquals("input", "CustomHibernateConfig.vm", generatorConfig.getInput().getFilename());

		// output
		Assert.assertNotNull("output", generatorConfig.getOutput());
		Assert.assertEquals("output override", "no", generatorConfig.getOutput().getOverride());

		// helpers
		Assert.assertNotNull("helpers", generatorConfig.getHelpers());
		Assert.assertEquals("helper items", 1, generatorConfig.getHelpers().getHelper().size());
		Assert.assertEquals("helper ref", "HibernateHelper", generatorConfig.getHelpers().getHelper().get(0)
				.getRef());
		Assert.assertEquals("helper contextName", "hibernate", generatorConfig.getHelpers().getHelper().get(0)
				.getContextName());

		// properties
		Assert.assertNotNull("properties", generatorConfig.getProperties());
		Assert.assertEquals("properties items", 1, generatorConfig.getProperties().getAny().size());

		Assert.assertEquals("property 1 name", "hibernate.config.filename", generatorConfig.getProperties().getAny()
				.get(0).getNodeName());
		Assert.assertEquals("property 1 value", "hibernate.config.xml", generatorConfig.getProperties().getAny().get(0)
				.getTextContent().trim());
	
		// ######################################################## //
		// generator 2
		// ######################################################## //
		generatorConfig = settings.getManyToOne().getGenerator().get(1);
		Assert.assertEquals("first generator id", 
							"SpringConfig", 
							generatorConfig.getId());
		Assert.assertEquals("first generator class", 
							"co.nz.iswe.generator.impl.spring.SpringConfigGenerator", 
							generatorConfig.getClazz());
		// input
		Assert.assertNotNull("input", generatorConfig.getInput());
		Assert.assertEquals("input", "velocity", generatorConfig.getInput().getType());
		Assert.assertEquals("input", "SpringConfig.vm", generatorConfig.getInput().getFilename());

		// output
		Assert.assertNotNull("output", generatorConfig.getOutput());
		Assert.assertEquals("output override", "always", generatorConfig.getOutput().getOverride());

		// helpers
		Assert.assertNull("helpers", generatorConfig.getHelpers());

		// properties
		Assert.assertNotNull("properties", generatorConfig.getProperties());
		Assert.assertEquals("properties items", 1, generatorConfig.getProperties().getAny().size());

		Assert.assertEquals("property 1 name", "spring.config.filename", generatorConfig.getProperties().getAny()
				.get(0).getNodeName());
		Assert.assertEquals("property 1 value", "appContext.xml", generatorConfig.getProperties().getAny().get(0)
				.getTextContent().trim());
	}

	/**
	 * 
	 * Default <oneToOne></oneToOne>
	 * 
	 * @param settings
	 */
	private void assertMergedOneToOne(GeneratorContextConfig settings) {
		Assert.assertNotNull("oneToOne", settings.getOneToOne());

		Assert.assertEquals("2 oneToOne item", 2, settings.getOneToOne().getGenerator().size());

		// ### first generator ### //
		GeneratorConfig generatorConfig = settings.getOneToOne().getGenerator().get(0);
		Assert.assertEquals("first generator id", "JpaDAO_Abstract", generatorConfig.getId());
		Assert.assertEquals("first generator class", "nz.co.iswe.generator.impl.jpa.CustomJpaAbstractDAOGenerator",
				generatorConfig.getClazz());
		// input
		Assert.assertNotNull("input", generatorConfig.getInput());
		Assert.assertEquals("input", "velocity", generatorConfig.getInput().getType());
		Assert.assertEquals("input", "CustomJpaDAOAbstract.vm", generatorConfig.getInput().getFilename());

		// output
		Assert.assertNotNull("output", generatorConfig.getOutput());
		Assert.assertEquals("output override", "no", generatorConfig.getOutput().getOverride());

		// helpers
		Assert.assertNotNull("helpers", generatorConfig.getHelpers());
		Assert.assertEquals("helper items", 2, generatorConfig.getHelpers().getHelper().size());
		Assert.assertEquals("first helper ref", "CustomJpaDAOHelper", generatorConfig.getHelpers().getHelper().get(0)
				.getRef());
		Assert.assertEquals("first helper contextName", "dao", generatorConfig.getHelpers().getHelper().get(0)
				.getContextName());

		Assert.assertEquals("second helper ref", "TesteHelper", generatorConfig.getHelpers().getHelper().get(1)
				.getRef());
		Assert.assertEquals("second helper contextName", "testHelper", generatorConfig.getHelpers().getHelper().get(1)
				.getContextName());

		// properties
		Assert.assertNotNull("properties", generatorConfig.getProperties());
		Assert.assertEquals("properties items", 3, generatorConfig.getProperties().getAny().size());

		Assert.assertEquals("property 1 name", "default.property1.name", generatorConfig.getProperties().getAny()
				.get(0).getNodeName());
		Assert.assertEquals("property 1 value", "default value 1", generatorConfig.getProperties().getAny().get(0)
				.getTextContent().trim());

		Assert.assertEquals("property 2 name", "default.property2.name", generatorConfig.getProperties().getAny()
				.get(1).getNodeName());
		Assert.assertEquals("property 2 value", "modified value 2", generatorConfig.getProperties().getAny().get(1)
				.getTextContent().trim());

		Assert.assertEquals("property 3 name", "query.match.strategy", generatorConfig.getProperties().getAny().get(2)
				.getNodeName());
		Assert.assertEquals("property 3 value", "1", generatorConfig.getProperties().getAny().get(2).getTextContent().trim());

		// ### END: first generator ### //

		// ############################################################### //
		// ### Second generator ### //
		// ############################################################### //
		generatorConfig = settings.getOneToOne().getGenerator().get(1);
		Assert.assertEquals("second generator id", "BO_Abstract", generatorConfig.getId());
		Assert.assertEquals("second generator class", "nz.co.iswe.generator.impl.BOAbstractGenerator",
				generatorConfig.getClazz());

		// input
		Assert.assertNotNull("input", generatorConfig.getInput());
		Assert.assertEquals("input", "velocity", generatorConfig.getInput().getType());
		Assert.assertEquals("input", "BOAbstract.vm", generatorConfig.getInput().getFilename());

		// output
		Assert.assertNotNull("output", generatorConfig.getOutput());
		Assert.assertEquals("output override", "always", generatorConfig.getOutput().getOverride());

		// helpers
		Assert.assertNotNull("helpers", generatorConfig.getHelpers());
		Assert.assertEquals("1 helper item(s)", 1, generatorConfig.getHelpers().getHelper().size());
		Assert.assertEquals("first helper ref", "BOHelper", generatorConfig.getHelpers().getHelper().get(0).getRef());
		Assert.assertEquals("first helper contextName", "bo", generatorConfig.getHelpers().getHelper().get(0)
				.getContextName());

		// properties
		Assert.assertNull("properties", generatorConfig.getProperties());
		// ### END: Second generator ### //

	}

	private void assertDefaultHelpers(GeneratorContextConfig settings) {
		Assert.assertNotNull("helpers", settings.getHelpers());

		Assert.assertEquals("helpers list size", 1, settings.getHelpers().getHelper().size());

		HelperConfig helperConfig = settings.getHelpers().getHelper().get(0);
		Assert.assertEquals("helper 1 - id", "JpaDAOHelper", helperConfig.getId());
		Assert.assertEquals("helper 1 - class", "nz.co.iswe.generator.impl.helper.DAOHelper", settings.getHelpers()
				.getHelper().get(0).getClazz());

		Assert.assertNotNull("helpers 1 - properties", helperConfig.getProperties());

		Assert.assertEquals("properties items", 5, helperConfig.getProperties().getAny()
				.size());

		Element property = helperConfig.getProperties().getAny().get(0);
		Assert.assertEquals("property 1 name", "packageFrom", property.getNodeName());
		Assert.assertEquals("property 1 value", "entities", property.getTextContent().trim());

		property = helperConfig.getProperties().getAny().get(1);
		Assert.assertEquals("property 2 name", "packageTo", property.getNodeName());
		Assert.assertEquals("property 2 value", "dao", property.getTextContent().trim());

		property = helperConfig.getProperties().getAny().get(2);
		Assert.assertEquals("property 3 name", "abstract.fullyQualifiedName", property.getNodeName());
		Assert.assertEquals("property 3 value", "${dao.package}.${entity.simpleName}DAOAbstract", property.getTextContent().trim());

		property = helperConfig.getProperties().getAny().get(3);
		Assert.assertEquals("property 4 name", "interface.fullyQualifiedName", property.getNodeName());
		Assert.assertEquals("property 4 value", "${dao.package}.I${entity.simpleName}DAO", property.getTextContent().trim());

		property = helperConfig.getProperties().getAny().get(4);
		Assert.assertEquals("property 5 name", "impl.fullyQualifiedName", property.getNodeName());
		Assert.assertEquals("property 5 value", "${dao.package}.${entity.simpleName}DAOImpl", property.getTextContent().trim());
	}

	/**
	 * Merged <groups></groups>
	 * 
	 * @param settings
	 */
	private void assertMergedGroups(GeneratorContextConfig settings) {
		Assert.assertNotNull("groups", settings.getGroups());

		Assert.assertEquals("3 group items", 3, settings.getGroups().getGroup().size());

		Assert.assertEquals("group 1 - id", "Model", settings.getGroups().getGroup().get(0).getId());
		Assert.assertEquals("group 1 - generators", 3, settings.getGroups().getGroup().get(0).getGenerator().size());

		Assert.assertEquals("group 1 - generator 1", "JpaDAO_Abstract", settings.getGroups().getGroup().get(0)
				.getGenerator().get(0).getRef());
		Assert.assertEquals("group 1 - generator 2", "HibernateConfig", settings.getGroups().getGroup().get(0)
				.getGenerator().get(1).getRef());
		Assert.assertEquals("group 1 - generator 3", "BO_Abstract", settings.getGroups().getGroup().get(0)
				.getGenerator().get(2).getRef());

		Assert.assertEquals("group 2 - id", "ControllerLayer", settings.getGroups().getGroup().get(1).getId());
		Assert.assertEquals("group 2 - generators", 2, settings.getGroups().getGroup().get(1).getGenerator().size());

		Assert.assertEquals("group 2 - generator 1", "Controller_Abstract", settings.getGroups().getGroup().get(1)
				.getGenerator().get(0).getRef());
		Assert.assertEquals("group 2 - generator 2", "SpringModelConfig", settings.getGroups().getGroup().get(1)
				.getGenerator().get(1).getRef());

		Assert.assertEquals("group 3 - id", "FacadeLayer", settings.getGroups().getGroup().get(2).getId());
		Assert.assertEquals("group 2 - generators", 2, settings.getGroups().getGroup().get(2).getGenerator().size());

		Assert.assertEquals("group 2 - generator 1", "Facade_Abstract", settings.getGroups().getGroup().get(2)
				.getGenerator().get(0).getRef());
		Assert.assertEquals("group 2 - generator 2", "SpringFacadeConfig", settings.getGroups().getGroup().get(2)
				.getGenerator().get(1).getRef());
	}

	/**
	 * Default <manyToOne> ... </manyToOne>
	 * 
	 * @param defaultSettings
	 */
	private void assertDefaultManyToOne(GeneratorContextConfig settings) {
		Assert.assertNotNull("manyToOne", settings.getManyToOne());

		Assert.assertEquals("1 manyToOne item", 1, settings.getManyToOne().getGenerator().size());

		// generator
		GeneratorConfig generatorConfig = settings.getManyToOne().getGenerator().get(0);
		Assert.assertEquals("first generator id", "HibernateConfig", generatorConfig.getId());
		Assert.assertEquals("first generator class", "co.nz.iswe.generator.impl.hibrenate.HibernateConfig", settings
				.getManyToOne().getGenerator().get(0).getClazz());
		// input
		Assert.assertNotNull("input", generatorConfig.getInput());
		Assert.assertEquals("input", "velocity", generatorConfig.getInput().getType());
		Assert.assertEquals("input", "HibernateConfig.vm", generatorConfig.getInput().getFilename());

		// output
		Assert.assertNotNull("output", generatorConfig.getOutput());
		Assert.assertEquals("output override", "always", generatorConfig.getOutput().getOverride());

		// helpers
		Assert.assertNull("helpers", generatorConfig.getHelpers());

		// properties
		Assert.assertNotNull("properties", generatorConfig.getProperties());
		Assert.assertEquals("properties items", 1, generatorConfig.getProperties().getAny().size());

		Assert.assertEquals("property 1 name", "hibernate.config.filename", generatorConfig.getProperties().getAny()
				.get(0).getNodeName());
		Assert.assertEquals("property 1 value", "hibernate.config.xml", generatorConfig.getProperties().getAny().get(0)
				.getTextContent().trim());
	}

	/**
	 * Default <oneToOne></oneToOne>
	 * 
	 * @param settings
	 */
	private void assertDefaultOneToOne(GeneratorContextConfig settings) {
		Assert.assertNotNull("oneToOne", settings.getOneToOne());

		Assert.assertEquals("1 oneToOne item", 1, settings.getOneToOne().getGenerator().size());

		// generator
		GeneratorConfig generatorConfig = settings.getOneToOne().getGenerator().get(0);
		Assert.assertEquals("first generator id", "JpaDAO_Abstract", generatorConfig.getId());
		Assert.assertEquals("first generator class", "nz.co.iswe.generator.impl.jpa.JpaAbstractDAOGenerator", settings
				.getOneToOne().getGenerator().get(0).getClazz());
		// input
		Assert.assertNotNull("input", generatorConfig.getInput());
		Assert.assertEquals("input", "velocity", generatorConfig.getInput().getType());
		Assert.assertEquals("input", "JpaDAOAbstract.vm", generatorConfig.getInput().getFilename());

		// output
		Assert.assertNotNull("output", generatorConfig.getOutput());
		Assert.assertEquals("output override", "always", generatorConfig.getOutput().getOverride());

		// helpers
		Assert.assertNotNull("helpers", generatorConfig.getHelpers());
		Assert.assertEquals("helper items", 1, generatorConfig.getHelpers().getHelper().size());
		Assert.assertEquals("first helper ref", "JpaDAOHelper", generatorConfig.getHelpers().getHelper().get(0)
				.getRef());
		Assert.assertEquals("first helper contextName", "dao", generatorConfig.getHelpers().getHelper().get(0)
				.getContextName());

		// properties
		Assert.assertNotNull("properties", generatorConfig.getProperties());
		Assert.assertEquals("properties items", 2, generatorConfig.getProperties().getAny().size());

		Assert.assertEquals("property 1 name", "default.property1.name", generatorConfig.getProperties().getAny()
				.get(0).getNodeName());
		Assert.assertEquals("property 1 value", "default value 1", generatorConfig.getProperties().getAny().get(0)
				.getTextContent().trim());

		Assert.assertEquals("property 2 name", "default.property2.name", generatorConfig.getProperties().getAny()
				.get(1).getNodeName());
		Assert.assertEquals("property 2 value", "default value 2", generatorConfig.getProperties().getAny().get(1)
				.getTextContent().trim());
	}

	/**
	 * Default <groups></groups>
	 * 
	 * @param settings
	 */
	private void assertDefaultGroups(GeneratorContextConfig settings) {
		Assert.assertNotNull("groups", settings.getGroups());

		Assert.assertEquals("2 group items", 2, settings.getGroups().getGroup().size());

		Assert.assertEquals("first group", "Model", settings.getGroups().getGroup().get(0).getId());
		Assert.assertEquals("2 generators", 2, settings.getGroups().getGroup().get(0).getGenerator().size());

		Assert.assertEquals("first generator", "JpaDAO_Abstract", settings.getGroups().getGroup().get(0).getGenerator()
				.get(0).getRef());
		Assert.assertEquals("second generator", "HibernateConfig", settings.getGroups().getGroup().get(0)
				.getGenerator().get(1).getRef());

		Assert.assertEquals("second group", "ControllerLayer", settings.getGroups().getGroup().get(1).getId());
		Assert.assertEquals("2 generators", 2, settings.getGroups().getGroup().get(1).getGenerator().size());

		Assert.assertEquals("first generator", "Controller_Abstract", settings.getGroups().getGroup().get(1)
				.getGenerator().get(0).getRef());
		Assert.assertEquals("second generator", "SpringModelConfig", settings.getGroups().getGroup().get(1)
				.getGenerator().get(1).getRef());

	}

}
