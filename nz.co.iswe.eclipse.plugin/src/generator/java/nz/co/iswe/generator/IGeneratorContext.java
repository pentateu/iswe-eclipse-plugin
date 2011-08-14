package nz.co.iswe.generator;

import java.util.List;

import nz.co.iswe.generator.config.GlobalPropertiesResolver;
import nz.co.iswe.generator.config.xml.GeneratorContextConfig;
import nz.co.iswe.generator.helper.IHelper;
import nz.co.iswe.generator.info.EntityInfo;


public interface IGeneratorContext {
	String getProperty(String key);
	
	void init();

	GeneratorContextConfig getConfiguration();
	
	void run(String group, List<EntityInfo> entityList) throws Exception;

	IHelper getHelperById(String id);
	
	IHelper getHelperByName(String name);

	IGeneratorHandler getGeneratorHandler();

	void addConfiguration(GeneratorContextConfig config);

	GlobalPropertiesResolver getGlobalPropertiesResolver();
}
