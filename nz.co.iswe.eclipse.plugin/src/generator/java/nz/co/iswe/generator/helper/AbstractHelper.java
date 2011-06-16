package nz.co.iswe.generator.helper;

import java.util.List;

import org.w3c.dom.Element;

import nz.co.iswe.generator.IGenerator;
import nz.co.iswe.generator.IGeneratorContext;
import nz.co.iswe.generator.config.xml.HelperConfig;
import nz.co.iswe.generator.config.xml.ListOfProperties;
import nz.co.iswe.generator.info.EntityInfo;

public abstract class AbstractHelper implements IHelper {

	private static final String ABSTRACT_PACKAGE = "abstract.package";
	protected IGeneratorContext generatorContext;
	protected HelperConfig helperConfig;
	protected String name;
	protected IGenerator generator;
	
	protected AbstractHelper(String name){
		this.name = name;
	}
	
	@Override
	public void setGeneratorContext(IGeneratorContext generatorContext) {
		this.generatorContext = generatorContext;
	}

	@Override
	public void setHelperConfig(HelperConfig helperConfig) {
		this.helperConfig = helperConfig;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setGenerator(IGenerator generator) {
		this.generator = generator;
	}
	
	@Override
	public void clearGenerator() {
		this.generator = null;
	}
	
	
	protected String getAbstractPackagePrefix() {
		return getProperty(ABSTRACT_PACKAGE);
	}
	
	/**
	 * Utility class that retrieve the properties by key.
	 * It first looks at the Helpers properties defined in the
	 * xml config. If nothing found look at the global properties.
	 * @param string
	 * @return
	 */
	protected String getProperty(String key) {
		ListOfProperties properties = helperConfig.getProperties();
		
		//First look at the Helpers properties
		List<Element> elements = properties.getAny();
		for(Element element : elements){
			if(element.getNodeName().equals(key)){
				return element.getTextContent();
			}
		}
		
		//second look at the global properties
		return generatorContext.getProperty(key);
	}
	
	/**
	 * Utility method that given an entity this method obtains the complete package info form this entity
	 * and replaces the portion contained in the property 'from' with the value of the
	 * property 'to' 
	 * 
	 * @param entityInfo
	 * @param from
	 * @param to
	 * @return
	 */
	protected String replacePackage(EntityInfo entityInfo, String from, String to) {
		
		String packageName = entityInfo.getPackage();
		
		String valueFrom = getProperty(from);
		String valueTo = getProperty(to);
		
		String result = packageName.replaceAll(valueFrom, valueTo);
		
		return result;
	}

}
