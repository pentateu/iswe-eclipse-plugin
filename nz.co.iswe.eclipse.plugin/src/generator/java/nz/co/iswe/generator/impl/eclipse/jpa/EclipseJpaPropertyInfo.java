package nz.co.iswe.generator.impl.eclipse.jpa;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nz.co.iswe.generator.annotation.Generator;
import nz.co.iswe.generator.info.BasePropertyInfo;
import nz.co.iswe.generator.info.EntityInfo;
import nz.co.iswe.generator.info.EntityInfoFactory;
import nz.co.iswe.generator.info.PropertyType;

import org.eclipse.jdt.core.IAnnotatable;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

public class EclipseJpaPropertyInfo extends BasePropertyInfo {

	
	
	private static final String JAVAX_PERSISTENCE_ID = "javax.persistence.Id";
	private static final String NULLABLE2 = "nullable";
	private static final String JAVAX_PERSISTENCE_COLUMN = "javax.persistence.Column";
	private static final String LABEL2 = "label";

	public EclipseJpaPropertyInfo(EntityInfo sourceEntity, IMethod method) throws JavaModelException {

		this.sourceEntity = sourceEntity;
		
		//Property Name
		this.name = method.getElementName().replace("get", "");
		this.name = this.name.substring(0, 1).toLowerCase() + this.name.substring(1);
		
		//Property Type
		this.propertyType = resolvePropertyType(method);
		
		parseAnnotations(method);
		
	}
	

	public EclipseJpaPropertyInfo(EntityInfo sourceEntity, IField field, boolean isTransient) throws JavaModelException {
		this.sourceEntity = sourceEntity;
		
		//Property Name
		this.name = field.getElementName();
		
		//Property Type
		this.propertyType = resolvePropertyType(field);
		
		parseAnnotations(field);
		
		this.isTransient = isTransient;
	}

	public void parseAnnotations(IAnnotatable annotable) {
		EclipseJpaUtil jpaUtil = EclipseJpaUtil.getInstance();
		
		//check for Generator annotation
		IAnnotation generatorAnnotation = jpaUtil.getAnnotation(Generator.class.getName(), annotable);
		if(generatorAnnotation != null){
			//label
			this.label = (String)jpaUtil.getAnnotationPropertyValue(generatorAnnotation, LABEL2);
			
			//TODO: Implement the rest: manyToOneConfigs, uploadConfigs and etc
		}
		
		//Check for PrimaryKey
		if(jpaUtil.getAnnotation(JAVAX_PERSISTENCE_ID, annotable) != null){
			primaryKey = true;
		}
		
		IAnnotation columnAnnotation = jpaUtil.getAnnotation(JAVAX_PERSISTENCE_COLUMN, annotable);
		if(columnAnnotation != null){
			//Nullable
			Boolean bValue = (Boolean)jpaUtil.getAnnotationPropertyValue(columnAnnotation, NULLABLE2);
			if(bValue == null){
				bValue = false;
			}
			nullable = bValue;
			//TODO: IMplement the rest of the JPA properties
		}
		
		//label
		if(getLabel() == null){
			//if no label was defined through annotation
			this.label = this.name;
		}
	}

	private PropertyType resolvePropertyType(IMethod method) {
		try {
			String typeSignature = method.getReturnType();
			String propertyType = Signature.toString(typeSignature);
			if(propertyType.indexOf('.') > -1){
				propertyType = propertyType.substring(propertyType.lastIndexOf('.') + 1);
			}
			return resolvePropertyType(method.getCompilationUnit(), propertyType);
		}
		catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private PropertyType resolvePropertyType(IField field) {
		try {
			String typeSignature = field.getTypeSignature();
			String propertyType = Signature.toString(typeSignature);
			if(propertyType.indexOf('.') > -1){
				propertyType = propertyType.substring(propertyType.lastIndexOf('.') + 1);
			}
			return resolvePropertyType(field.getCompilationUnit(), propertyType);
		}
		catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private PropertyType resolvePropertyType(ICompilationUnit compilationUnit, String propertyType) {
		Matcher listMatcher = Pattern.compile("^(\\QList\\E)(<{1})(.+)(>{1})$").matcher(propertyType);
		
		if("String".equals(propertyType)){
			return PropertyType.TEXT;
		}
		else if("Long".equalsIgnoreCase(propertyType)){
			return PropertyType.NUMERIC_INTEGER;
		}
		else if("Integer".equalsIgnoreCase(propertyType)){
			return PropertyType.NUMERIC_INTEGER;
		}
		else if("Short".equalsIgnoreCase(propertyType)){
			return PropertyType.NUMERIC_INTEGER;
		}
		else if("Double".equalsIgnoreCase(propertyType)){
			return PropertyType.NUMERIC_DECIMAL;
		}
		else if("Float".equalsIgnoreCase(propertyType)){
			return PropertyType.NUMERIC_DECIMAL;
		}
		else if(listMatcher.matches()){
			String entityName = listMatcher.group(3);
			String entityClass = EclipseJpaUtil.getInstance().lookUpImport(compilationUnit, entityName);
			if(entityClass == null){
				//if the class was not found in the import section it means the class is in the same package as the current class.
				try {
					//get current class package
					entityClass = compilationUnit.getPackageDeclarations()[0].getElementName() + "." + entityName; 
				} catch (JavaModelException e) {
					throw new RuntimeException("Error resolving property: " + propertyType, e);
				}
			}
			if(entityClass != null){
				referenceEntity = EntityInfoFactory.getInstance().getEntityInfo(entityClass);
			}
			return PropertyType.COLLECTION_ENTITY;
		}
		else{
			return PropertyType.TEXT;
		}
	}

	
	
}

