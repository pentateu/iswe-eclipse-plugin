package nz.co.iswe.generator.helper;

import nz.co.iswe.generator.IGenerator;
import nz.co.iswe.generator.IGeneratorContext;
import nz.co.iswe.generator.config.xml.HelperConfig;

public interface IHelper {

	void setGeneratorContext(IGeneratorContext generatorContext);

	void setHelperConfig(HelperConfig helperConfig);

	/**
	 * Defines the internal Helper name. To be reference by coding.
	 * The id is specified in the xml and therefore can change and cannot
	 * be hardcoded.
	 * @return
	 */
	String getName();

	void setGenerator(IGenerator generator);

	void clearGenerator();

}
