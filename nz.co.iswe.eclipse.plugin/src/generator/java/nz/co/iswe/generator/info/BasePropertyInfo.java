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
	
	protected boolean isTransient = false;
	
	protected String label;
	
	protected String matchMode = MatchMode.ANYWHERE.toString();// DEFAULT

	private boolean oneToOne = false;

	private boolean manyToMany = false;

	private boolean manyToOne = false;

	private boolean oneToMany = false;
	
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
		return isTransient;
	}
	
	@Override
	public boolean isOneToOne() {
		return oneToOne;
	}
	@Override
	public boolean isManyToMany() {
		return manyToMany;
	}
	@Override
	public boolean isManyToOne() {
		return manyToOne;
	}
	@Override
	public boolean isOneToMany() {
		return oneToMany;
	}
	
	
	
}
