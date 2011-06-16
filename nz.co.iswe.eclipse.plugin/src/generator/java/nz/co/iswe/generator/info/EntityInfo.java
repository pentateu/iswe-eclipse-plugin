package nz.co.iswe.generator.info;

import java.util.List;

import nz.co.iswe.generator.IGeneratorContext;

public interface EntityInfo {

	void setProperty(String key, String value);
	String getProperty(String key);
	
	IGeneratorContext getGeneratorContext();

	String getSimpleName();
	
	String getNormalizedName();
	
	String getPackage();

	String getFullyQualifiedName();

	List<PropertyInfo> getPropertyList();
}
