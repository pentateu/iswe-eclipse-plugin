package nz.co.iswe.generator.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import nz.co.iswe.generator.IGeneratorContext;

public abstract class BaseEntityInfo implements EntityInfo {

	protected Properties properties = new Properties();
	
    protected PropertyInfo primaryKeyProperty = null;
    protected PropertyInfo versionProperty = null;
    
	protected IGeneratorContext generatorContext;
	
    protected List<PropertyInfo> propertyList = new ArrayList<PropertyInfo>();
    
    public String getNormalizedName() {
		String temp = getSimpleName();
		return temp.substring(0,1).toLowerCase() + temp.substring(1);
	}

	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
	public String getProperty(String key){
		return properties.getProperty(key);
	}	
	
    public PropertyInfo getVersionProperty() {
        return versionProperty;
    }

    public IGeneratorContext getGeneratorContext() {
		return generatorContext;
	}

    
    
	@Override
    public int hashCode() {
        //
        return (int)getFullyQualifiedName().hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        //
        if(!(obj instanceof BaseEntityInfo)){
            return false;
        }
        BaseEntityInfo other = (BaseEntityInfo)obj;
        
        return other.getFullyQualifiedName().equals(this.getFullyQualifiedName());
    }
}
