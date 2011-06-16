package nz.co.iswe.generator;

import nz.co.iswe.generator.config.xml.GeneratorConfig;
import nz.co.iswe.generator.info.EntityInfo;

public interface IGenerator {
	
	void init();

    void process(EntityInfo entityInfo) throws Exception;
    
	void end();
	
	void setGeneratorContext(IGeneratorContext generatorContext);

	void setGeneratorConfig(GeneratorConfig generatorConfig);

	void setGeneratorHandler(IGeneratorHandler generatorHandler);

}
