package nz.co.iswe.generator.impl.eclipse.jpa;

import nz.co.iswe.generator.annotation.Generator;
import nz.co.iswe.generator.info.BasePropertyInfo;
import nz.co.iswe.generator.info.EntityInfo;
import nz.co.iswe.generator.info.PropertyType;

import org.eclipse.jdt.core.IAnnotatable;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

public class EclipseJpaPropertyInfo extends BasePropertyInfo {

	
	public EclipseJpaPropertyInfo(EntityInfo sourceEntity, IMethod method) throws JavaModelException {

		this.sourceEntity = sourceEntity;
		
		//Property Name
		this.name = method.getElementName().replace("get", "");
		this.name = this.name.substring(0, 1).toLowerCase() + this.name.substring(1);
		
		//Property Type
		this.propertyType = resolvePropertyType(method);
		
		parseAnnotations(method);
		
	}
	
	public EclipseJpaPropertyInfo(EntityInfo sourceEntity, IField field) throws JavaModelException {

		this.sourceEntity = sourceEntity;
		
		//Property Name
		this.name = field.getElementName();
		
		//Property Type
		this.propertyType = resolvePropertyType(field);
		
		parseAnnotations(field);
		
	}

	public void parseAnnotations(IAnnotatable annotable) {
		
		EclipseJpaUtil jpaUtil = EclipseJpaUtil.getInstance();
		
		//check for Generator annotation
		IAnnotation generatorAnnotation = jpaUtil.getAnnotation(Generator.class.getName(), annotable);
		if(generatorAnnotation != null){
			//label
			//generatorAnnotation.get
		}
		
		//Check for PrimaryKey
		if(jpaUtil.getAnnotation("javax.persistence.Id", annotable) != null){
			primaryKey = true;
		}
		
		IAnnotation columnAnnotation = jpaUtil.getAnnotation("javax.persistence.Column", annotable);
		if(columnAnnotation != null){

			//Nullable
			nullable = true;
			
		}
		
		
	}

	private PropertyType resolvePropertyType(IMethod method) {
		try {
			String typeSignature = method.getReturnType();
			String propertyType = Signature.toString(typeSignature);
			if(propertyType.indexOf('.') > -1){
				propertyType = propertyType.substring(propertyType.lastIndexOf('.') + 1);
			}
			return resolvePropertyType(propertyType);
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
			return resolvePropertyType(propertyType);
		}
		catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private PropertyType resolvePropertyType(String propertyType) {
		if("String".equals(propertyType)){
			return PropertyType.TEXT;
		}
		else{
			return PropertyType.TEXT;
		}
	}

	
	
}
