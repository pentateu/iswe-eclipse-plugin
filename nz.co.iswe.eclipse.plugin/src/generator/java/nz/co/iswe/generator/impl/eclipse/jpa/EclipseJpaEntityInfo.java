package nz.co.iswe.generator.impl.eclipse.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nz.co.iswe.generator.info.BaseEntityInfo;
import nz.co.iswe.generator.info.ParsingEntityException;
import nz.co.iswe.generator.info.PropertyInfo;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class EclipseJpaEntityInfo extends BaseEntityInfo {

	private static final String JAVAX_PERSISTENCE_TRANSIENT = "javax.persistence.Transient";

	private IType type;

	private Map<String, PropertyInfo> propertyMap = new HashMap<String, PropertyInfo>();

	private int propertiesStatus = 0; // 0: not loaded, 1: loading , 2: loaded

	public EclipseJpaEntityInfo(IType type) {
		super();
		this.type = type;
	}

	public String getPackageEntidade() {
		if (type == null) {
			return "";
		}
		else {
			return type.getPackageFragment().getElementName();
		}
	}

	@Override
	public String getSimpleName() {
		return type.getElementName();
	}

	@Override
	public String getFullyQualifiedName() {
		return type.getFullyQualifiedName();
	}

	@Override
	public String getPackage() {
		return type.getPackageFragment().getElementName();
	}

	@Override
	public synchronized List<PropertyInfo> getPropertyList() {
		if (propertiesStatus == 0) {
			propertiesStatus = 1;

			try {

				// parse properties form fields
				parsePropertiesFromFields();

				parsePropertiesFromMethods();

			}
			catch (JavaModelException e) {
				e.printStackTrace();
				throw new ParsingEntityException("Error parsing entity fields for properties.", e);
			}

			propertiesStatus = 2;
		}
		return propertyList;
	}

	/**
	 * Create a PropertyInfo object for each JPA persistent field
	 * 
	 * @throws JavaModelException
	 */
	private void parsePropertiesFromFields() throws JavaModelException {
		IField[] fields = type.getFields();

		for (IField field : fields) {

			boolean shouldIgnore = false;
			// check it it is persistent
			IAnnotation transientAnnotation = EclipseJpaUtil.getInstance().getAnnotation(JAVAX_PERSISTENCE_TRANSIENT,
					field);
			
			if (transientAnnotation != null) {
				shouldIgnore = true;
			}
			
			//check for static fields
			Pattern pattern = Pattern.compile("(.?)(\\Qstatic\\E)(\\s)(.+)");
			Matcher matcher = pattern.matcher(field.getSource());
			if (matcher.find()) {
				shouldIgnore = true;
			}

			// check for the modifier transient
			pattern = Pattern.compile("(.?)(\\Qtransient\\E)(\\s)(.+)");
			matcher = pattern.matcher(field.getSource());
			if (matcher.find()) {
				shouldIgnore = true;
			}

			if (!shouldIgnore) {
				// It is a persistent Field -> Property
				PropertyInfo propertyInfo = new EclipseJpaPropertyInfo(this, field);

				if (!propertyMap.containsKey(propertyInfo.getName())) {

					propertyMap.put(propertyInfo.getName(), propertyInfo);
					propertyList.add(propertyInfo);

				}

			}

		}

	}

	private void parsePropertiesFromMethods() throws JavaModelException {

		IMethod[] methods = type.getMethods();

		for (IMethod method : methods) {

			boolean shouldIgnore = false;
			// check it it is persistent
			IAnnotation transientAnnotation = EclipseJpaUtil.getInstance().getAnnotation(JAVAX_PERSISTENCE_TRANSIENT,
					method);

			if (transientAnnotation != null) {
				shouldIgnore = true;
			}

			// verify if that is a getMethod
			if (!method.getElementName().matches("get.+") || method.getTypeParameters().length > 0) {
				shouldIgnore = true;
			}

			// check for the modifier transient
			if (method.getSource().matches("\\s?static\\s.*")) {
				shouldIgnore = true;
			}

			if (!shouldIgnore) {
				// It is a persistent Field -> Property

				EclipseJpaPropertyInfo propertyInfo = new EclipseJpaPropertyInfo(this, method);
				if (propertyMap.containsKey(propertyInfo.getName())) {
					propertyInfo = (EclipseJpaPropertyInfo) propertyMap.get(propertyInfo.getName());

					// parse the method annotation and merge into the existing
					// property info
					propertyInfo.parseAnnotations(method);
				}
				else {
					propertyMap.put(propertyInfo.getName(), propertyInfo);
					propertyList.add(propertyInfo);

				}

			}

		}

	}

}
