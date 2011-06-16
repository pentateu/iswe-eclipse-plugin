package nz.co.iswe.generator.impl;

import nz.co.iswe.generator.IGenerator;
import nz.co.iswe.generator.IGeneratorContext;
import nz.co.iswe.generator.IGeneratorHandler;
import nz.co.iswe.generator.config.xml.GeneratorConfig;
import nz.co.iswe.generator.config.xml.ListOfProperties;
import nz.co.iswe.generator.helper.HelpersContext;

import org.apache.velocity.VelocityContext;
import org.w3c.dom.Element;

public abstract class AbstractVelocityGenerator implements IGenerator {

	private static final String CONTEXT = "context";

	private static final String GLOBAL = "Global";
	
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
		helpersContext = new HelpersContext(generatorContext, generatorConfig.getHelpers().getHelper());
		helpersContext.setGenerator(this);
	}
	
	@Override
	public void end() {
		if(helpersContext != null){
			helpersContext.clearGenerator();
		}
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
		helpersContext.setup(ctx);
		
		
		// ### Global Properties
		// ${Global.get("property.name")}
		GlobalPropertiesContext globalPropertiesContext = new GlobalPropertiesContext(generatorConfig.getProperties());
		ctx.put(GLOBAL, globalPropertiesContext);
		
		
		// ### context -> IGeneratorContext
		ctx.put(CONTEXT, generatorContext);
		
		return ctx;
	}
	
	class GlobalPropertiesContext {

		private ListOfProperties listOfProperties;
		public GlobalPropertiesContext(ListOfProperties listOfProperties) {
			this.listOfProperties = listOfProperties;
		}
		
		public String get(String key){
			for(Element element : listOfProperties.getAny()){
				if(element.getNodeName().endsWith(key)){
					return element.getTextContent();
				}
			}
			return null;
		}
	}
}
