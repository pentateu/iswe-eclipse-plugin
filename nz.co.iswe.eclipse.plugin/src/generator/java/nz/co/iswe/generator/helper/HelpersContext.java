package nz.co.iswe.generator.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;

import nz.co.iswe.generator.IGenerator;
import nz.co.iswe.generator.IGeneratorContext;
import nz.co.iswe.generator.config.xml.ReferenceHelper;

public class HelpersContext {

	private Map<String, IHelper> helpers = new HashMap<String, IHelper>();
	
	
	/**
	 * Create the Helpers Context and load all helpers declared in the configuration XML
	 * to the  memory to be used by the templates.
	 * 
	 * @param generatorContext
	 * @param referenceHelpers
	 */
	public HelpersContext(IGeneratorContext generatorContext, List<ReferenceHelper> referenceHelpers) {
		for(ReferenceHelper referenceHelper : referenceHelpers){
			//get the helper config and load it
			IHelper helper = generatorContext.getHelperById(referenceHelper.getRef());
			
			//put it into the context
			helpers.put(referenceHelper.getContextName(), helper);
		}
	}
	
	public IHelper get(String name){
		return helpers.get(name);
	}

	public void setGenerator(IGenerator generator) {
		for(IHelper helper : helpers.values()){
			helper.setGenerator(generator);
		}
	}
	
	public void clearGenerator() {
		for(IHelper helper : helpers.values()){
			helper.clearGenerator();
		}
	}

	public void setup(VelocityContext ctx) {
		for(String key : helpers.keySet()){
			ctx.put(key, helpers.get(key));
		}
	}
	
}
