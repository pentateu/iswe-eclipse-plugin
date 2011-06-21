package nz.co.iswe.generator.info;

import nz.co.iswe.generator.annotation.MatchMode;


/**
 * 
 * @author Rafael Almeida
 *
 * 
 *
 */
public abstract class BasePropertyInfo implements PropertyInfo {

	
	protected String name;
	
	protected PropertyType propertyType;
	
	protected EntityInfo sourceEntity;
	
	protected EntityInfo referenceEntity;
	
	protected boolean primaryKey = false;
	
	protected boolean nullable = true;
	
	protected String label;
	
	protected String matchMode = MatchMode.ANYWHERE.toString();// DEFAULT
	
	public String getName() {
		return name;
	}
	
	public EntityInfo getSourceEntity() {
		return sourceEntity;
	}
	
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	
	public EntityInfo getReferenceEntity() {
		return referenceEntity;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getMatchMode() {
		return matchMode;
	}

	public PropertyType getPropertyType() {
		return propertyType;
	}
	
	@Override
	public boolean isNullable() {
		return nullable;
	}
	
	@Override
	public boolean isTransient() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isOneToOne() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isManyToMany() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isManyToOne() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isOneToMany() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
