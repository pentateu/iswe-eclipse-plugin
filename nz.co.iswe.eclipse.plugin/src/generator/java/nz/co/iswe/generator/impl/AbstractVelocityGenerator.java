package nz.co.iswe.generator.impl;

import java.util.List;

import nz.co.iswe.generator.IGenerator;
import nz.co.iswe.generator.IGeneratorContext;
import nz.co.iswe.generator.IGeneratorHandler;
import nz.co.iswe.generator.config.GlobalPropertiesResolver;
import nz.co.iswe.generator.config.xml.GeneratorConfig;
import nz.co.iswe.generator.config.xml.ListOfProperties;
import nz.co.iswe.generator.helper.HelpersContext;
import nz.co.iswe.generator.impl.helper.ExpressionParser;
import nz.co.iswe.generator.info.EntityInfo;

import org.apache.velocity.VelocityContext;
import org.w3c.dom.Element;

public abstract class AbstractVelocityGenerator implements IGenerator {

	private static final String GENERATOR = "generator";

	private static final String FULLY_QUALIFIED_NAME = "fullyQualifiedName";
	
	private static final String PACKAGE_TO = "packageTo";
	private static final String PACKAGE_FROM = "packageFrom";
	
	private static final String CONTEXT = "context";

	private static final String GLOBAL = "global";
	
	//private static final String CHARSET_UTF = "UTF-8";

    protected IGeneratorContext generatorContext;

    protected GeneratorConfig generatorConfig;

    protected IGeneratorHandler generatorHandler;
    
    protected HelpersContext helpersContext;
    
    public void setGeneratorContext(IGeneratorContext generatorContext) {
		this.generatorContext = generatorContext;
	}

	public void setGeneratorConfig(GeneratorConfig generatorConfig) {
		this.generatorConfig = generatorConfig;
	}

	public void setGeneratorHandler(IGeneratorHandler generatorHandler) {
		this.generatorHandler = generatorHandler;
	}
	
	@Override
	public void init() {
		if(generatorConfig.getHelpers() != null && generatorConfig.getHelpers().getHelper() != null){
			helpersContext = new HelpersContext(generatorContext, generatorConfig.getHelpers().getHelper());
			helpersContext.setGenerator(this);
		}
	}
	
	@Override
	public void end() {
		if(helpersContext != null){
			helpersContext.clearGenerator();
		}
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
	
	
	protected String toSimpleName(String fullyQualifiedName) {
		String [] parts = fullyQualifiedName.split("\\.");
		return parts[parts.length - 1];
	}
	
	
	
	// Helper methods invoked from Velocity Templates
	/**
	 * @param entityInfo
	 * @return
	 */
	public String getPackage(EntityInfo entityInfo) {
		return replacePackage(entityInfo, PACKAGE_FROM, PACKAGE_TO);
	}
	
	public String getSimpleClassName(EntityInfo entityInfo) {
		return toSimpleName(getFullyQualifiedName(entityInfo));
	}
	
	public String getFullyQualifiedName(EntityInfo entityInfo) {
		return getAndParseProperty(entityInfo, FULLY_QUALIFIED_NAME);
	}
	
	public String getEntityPrefixName(EntityInfo entityInfo){
		String sufixToRemove = getAndParseProperty(entityInfo, "sufixToRemove");
		return entityInfo.getSimpleName().replaceAll(sufixToRemove, "");
	}
	
	/**
	 * Cria um contexto Velocity com as ferramentas e utilidades necessarias.
	 * 
	 * @return
	 */
	protected VelocityContext buildVelocityContext(){
		VelocityContext ctx = new VelocityContext();
		
		// ### Loading helpers
		// ${dao.getSomething()}
		if(helpersContext != null){
			helpersContext.setup(ctx);
		}
		
		// ### Global Properties
		// ${global.get("property.name")}
		//SimplePropertiesContextResolver propertiesContext = new SimplePropertiesContextResolver(generatorConfig.getProperties());
		
		ctx.put(GLOBAL, generatorContext.getGlobalPropertiesResolver());
		
		// ### context -> IGeneratorContext
		ctx.put(CONTEXT, generatorContext);
		
		ctx.put(GENERATOR, this);
		
		
		return ctx;
	}
	
	protected String getAndParseProperty(EntityInfo entityInfo, String property) {
		ExpressionParser parser = new ExpressionParser();
		parser.put(GENERATOR, this);
		parser.put("entity", entityInfo);
		String expression = getProperty(property);
		String result = parser.parse(expression);
		return result;
	}	
	
	protected String getProperty(String key) {
		ListOfProperties properties = generatorConfig.getProperties();
		
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
}
