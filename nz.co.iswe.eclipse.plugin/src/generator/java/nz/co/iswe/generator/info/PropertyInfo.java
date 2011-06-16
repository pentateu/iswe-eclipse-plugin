package nz.co.iswe.generator.info;


public interface PropertyInfo {

	PropertyType getPropertyType();
	
	String getName();
	
	EntityInfo getSourceEntity();

	boolean isPrimaryKey();
	
	EntityInfo getReferenceEntity();
	
	String getLabel();
	
	boolean isNullable();
	
	boolean isTransient();
	
	boolean isOneToOne();
    
    boolean isManyToMany();
    
    boolean isManyToOne();
    
    boolean isOneToMany();

	String getMatchMode();
}
