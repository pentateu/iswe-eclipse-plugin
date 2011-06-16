package nz.co.iswe.generator.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import nz.co.iswe.generator.config.xml.GeneratorConfig;
import nz.co.iswe.generator.config.xml.GeneratorContextConfig;
import nz.co.iswe.generator.config.xml.GroupConfig;
import nz.co.iswe.generator.config.xml.HelperConfig;
import nz.co.iswe.generator.config.xml.ReferenceGenerator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.w3c.dom.Element;

public class ConfigurationContext {

	private static ConfigurationContext instance = null;

	public static ConfigurationContext getInstance() {
		if (instance == null) {
			instance = new ConfigurationContext();
		}
		return instance;
	}

	private ConfigurationContext() {
	}

	public GeneratorContextConfig loadGeneratorContextConfig(String filePath) {
		FileInputStream fileInputStream = null;
		
		try{
			fileInputStream = new FileInputStream(filePath);
			return loadGeneratorContextConfig(fileInputStream);
		}
		catch(FileNotFoundException e){
			throw new RuntimeException("ConfigurationContext error loading the default configuration xml file from FilePath: " + filePath + " (FileNotFoundException)", e);
		}
		finally{
			if(fileInputStream != null){
				try {
					fileInputStream.close();
				}
				catch (IOException e) {/*ignore*/}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public GeneratorContextConfig loadGeneratorContextConfig(InputStream configInputStream) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(GeneratorContextConfig.class.getPackage().getName());

			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			JAXBElement<GeneratorContextConfig> doc = (JAXBElement<GeneratorContextConfig>) unmarshaller
					.unmarshal(configInputStream);

			return doc.getValue();
		}
		catch (JAXBException e) {
			throw new RuntimeException("ConfigurationContext error loading the default configuration xml file from InputStrem! (JAXBException)", e);
		}
	}

	public GeneratorContextConfig merge(GeneratorContextConfig original, GeneratorContextConfig newConfig) {
		if (isNotEmpty(newConfig.getEntityInfoFactory())) {
			original.setEntityInfoFactory(newConfig.getEntityInfoFactory());
		}
		if (isNotEmpty(newConfig.getGeneratorContext())) {
			original.setGeneratorContext(newConfig.getGeneratorContext());
		}

		// merge <groups> ... </groups>
		if (newConfig.getGroups() != null) {
			if (original.getGroups() == null) {
				// there is nothing in the original config, just set
				// what comes from the new config
				original.setGroups(newConfig.getGroups());
			}
			else {
				// merge
				//mergeListOfGroups(original.getGroups(), newConfig.getGroups());
				mergeListObject("group", "id", original.getGroups(), newConfig.getGroups());
			}
		}
		
		// merge <oneToOne> ... </oneToOne>
		if(newConfig.getOneToOne() != null){
			if(original.getOneToOne() == null){
				//just set the values from the newConfig into the originalConfig
				original.setOneToOne(newConfig.getOneToOne());
			}
			else{
				//merge
				mergeListObject("generator", "id", original.getOneToOne(), newConfig.getOneToOne());
			}
		}
		
		// merge <manyToOne> ... </manyToOne>
		if(newConfig.getManyToOne() != null){
			if(original.getManyToOne() == null){
				//just set the values from the newConfig into the originalConfig
				original.setManyToOne(newConfig.getManyToOne());
			}
			else{
				//merge
				mergeListObject("generator", "id", original.getManyToOne(), newConfig.getManyToOne());
			}
		}
		
		// merge <helpers> ... </helpers>
		if(newConfig.getHelpers() != null){
			if(original.getHelpers() == null){
				//just set the values from the newConfig into the originalConfig
				original.setHelpers(newConfig.getHelpers());
			}
			else{
				//merge
				mergeListObject("helper", "id", original.getHelpers(), newConfig.getHelpers());
			}
		}
		
		// merge <properties> ... </properties>
		if(newConfig.getProperties() != null){
			if(original.getProperties() == null){
				//just set the values from the newConfig into the originalConfig
				original.setProperties(newConfig.getProperties());
			}
			else{
				//merge
				mergePropertyList(
						original.getProperties().getAny(), 
						newConfig.getProperties().getAny());
			}
		}

		return original;
	}
	
	

	private void mergeListObject(String listProperty, String objectKeyProperty, Object originalListObject, Object newListObject) {
		mergeListObject(listProperty, objectKeyProperty, originalListObject, newListObject, false);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void mergeListObject(String listProperty, String objectKeyProperty, Object originalListObject, Object newListObject, boolean simpleCopy) {
		
		List newList = (List)getProperty(listProperty, newListObject);
		List originalList = (List)getProperty(listProperty, originalListObject);
		
		if (newList == null || newList.size() == 0) {
			return;// nothing to merge
		}
		
		if (originalList.size() == 0) {
			//original list is empty, so add all from the new list
			originalList.addAll(newList);
			return;
		}
		
		// there are items in both lists
		for (Object newItem : newList) {
			// go through each new item and look if it exists in the
			// original list

			String newKey = (String)getProperty(objectKeyProperty, newItem);
			
			Object originalItem = findByAttribute(originalList,
					objectKeyProperty,
					newKey);
			

			if (originalItem == null) {
				// If there is no original item, just add the new item to the originalList
				originalList.add(newItem);
			}
			else {
				if(simpleCopy){
					simpleCopyMerge(originalItem, newItem);
				}
				else{
					// the same item is found on both lists.
					//perform a merge of the two items
					mergeObject(originalItem, newItem);
				}
			}
		}
		
	}
	
	protected void mergeHelperConfig(HelperConfig originalHelperConfig, HelperConfig newHelperConfig){
		if(originalHelperConfig == null || newHelperConfig == null){
			throw new RuntimeException("originalHelperConfig and newHelperConfig cannot be null!");
		}
		
		//class
		if(isNotEmpty(newHelperConfig.getClazz())){
			originalHelperConfig.setClazz(newHelperConfig.getClazz());
		}
		
		//properties
		if(newHelperConfig.getProperties() != null){
			if(originalHelperConfig.getProperties() == null){
				originalHelperConfig.setProperties(newHelperConfig.getProperties());
			}
			else{
				mergePropertyList(
						originalHelperConfig.getProperties().getAny(), 
						newHelperConfig.getProperties().getAny());
			}
		}
		
	}

	protected void mergeGeneratorConfig(GeneratorConfig originalGeneratorConfig, GeneratorConfig newGeneratorConfig){
		
		if(originalGeneratorConfig == null || newGeneratorConfig == null){
			throw new RuntimeException("originalGeneratorConfig and newGeneratorConfig cannot be null!");
		}
		
		//class
		if(isNotEmpty(newGeneratorConfig.getClazz())){
			originalGeneratorConfig.setClazz(newGeneratorConfig.getClazz());
		}
		
		//input
		if(newGeneratorConfig.getInput() != null){
			//no need to make a merge property per property, since all attributes are
			//required, the input object will always be complete and will always
			//override all values in the original object
			originalGeneratorConfig.setInput(newGeneratorConfig.getInput());
		}
		
		//output
		if(newGeneratorConfig.getOutput() != null){
			originalGeneratorConfig.setOutput(newGeneratorConfig.getOutput());
		}
		
		//helpers
		if(newGeneratorConfig.getHelpers() != null){
			if(originalGeneratorConfig.getHelpers() == null){
				originalGeneratorConfig.setHelpers(newGeneratorConfig.getHelpers());
			}
			else{
				mergeListObject("helper", "contextName", 
						originalGeneratorConfig.getHelpers(), 
						newGeneratorConfig.getHelpers(),
						true);//simple copy
			}
		}
		
		
		//properties
		if(newGeneratorConfig.getProperties() != null){
			if(originalGeneratorConfig.getProperties() == null){
				originalGeneratorConfig.setProperties(newGeneratorConfig.getProperties());
			}
			else{
				mergePropertyList(
						originalGeneratorConfig.getProperties().getAny(), 
						newGeneratorConfig.getProperties().getAny());
			}
		}
	}

	private void mergePropertyList(List<Element> originalListOfProperties, List<Element> newListOfProperties) {
		if(originalListOfProperties == null || newListOfProperties == null){
			throw new RuntimeException("originalListOfProperties and newListOfProperties cannot be null!");
		}
		
		if(newListOfProperties.size() > 0){
			if(originalListOfProperties.size() == 0){
				//the original list is empty, so just add all new stuff
				originalListOfProperties.addAll(newListOfProperties);
			}
			else{
				for(Element newItem : newListOfProperties){
					//check if the item exist in the original list
					Element originalItem = (Element)findByAttribute(
							originalListOfProperties, 
							"nodeName", 
							newItem.getNodeName());
					
					if(originalItem == null){
						//just add the new one to the original list
						originalListOfProperties.add(newItem);
					}
					else{
						//merge the text content (Property value)
						originalItem.setTextContent(newItem.getTextContent());
					}
					
				}
			}
		}
		
		
	}

	private void simpleCopyMerge(Object originalItem, Object newItem) {
		if(originalItem == null || newItem == null){
			throw new RuntimeException("originalItem and newItem must not be null !");
		}
		
		try {
			BeanUtils.copyProperties(originalItem, newItem);
		}
		catch (Throwable t) {/* ignore */
			throw new RuntimeException("Error trying a simple copy merge object class: " + originalItem.getClass().getName(), t);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void mergeObject(Object originalItem, Object newItem) {
		if(originalItem == null || newItem == null){
			throw new RuntimeException("originalItem and newItem must not be null !");
		}
		//get the class of the object being merged
		Class clazz = originalItem.getClass();
		try {
			Method mergeMethod = this.getClass().getDeclaredMethod("merge" + clazz.getSimpleName(), new Class[] {clazz, clazz});
			mergeMethod.invoke(this, new Object[] {originalItem, newItem});
		}
		catch (Throwable t) {/* ignore */
			throw new RuntimeException("Error invoking merge method for class: " + clazz.getName(), t);
		}
	}
	
	protected void mergeGroupConfig(GroupConfig originalGroupConfig, GroupConfig newGroupConfig){
		mergeReferenceGeneratorList(originalGroupConfig.getGenerator(), newGroupConfig.getGenerator());
	}
	
	private void mergeReferenceGeneratorList(List<ReferenceGenerator> originalReferenceGeneratorList,
			List<ReferenceGenerator> newReferenceGeneratorList) {
		// add the references that does not exist
		for (ReferenceGenerator newReferenceGenerator : newReferenceGeneratorList) {			
			if(findByAttribute(originalReferenceGeneratorList, "ref", newReferenceGenerator.getRef()) == null){
				originalReferenceGeneratorList.add(newReferenceGenerator);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private Object findByAttribute(List items, String attr, String value) {
		if (value == null || items == null || attr == null) {
			return null;
		}
		for (Object item : items) {
			String attrValue = (String)getProperty(attr, item);
			if (attrValue != null && attrValue.equals(value)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Method that return the value of the property for a given object
	 * Instance. returns null if the object does not have the property 
	 * specified.
	 * 
	 * @param item
	 * @return
	 */
	private Object getProperty(String propertyName, Object item) {
		try {
			return BeanUtilsBean.getInstance().getPropertyUtils().getProperty(item, propertyName);
			
			/*
			//normalize attr name
			propertyName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
			
			String methodName = "get" + propertyName;
			
			Method[] methods = item.getClass().getMethods();
			
			Method getMethod = item.getClass().getDeclaredMethod(methodName, new Class[] {});
			return getMethod.invoke(item, new Object[] {});
			*/
		}
		catch (Throwable t) {/* ignore */
			t.printStackTrace();
		}
		return null;
	}

	/*
	 * private void mergeConfigList(List original, List newList) { for(Object
	 * newItem : newList){ if(!original.contains(newItem)){
	 * original.add(newItem); } } }
	 */

	private boolean isNotEmpty(String stringValue) {
		return stringValue != null && stringValue.trim().length() > 0;
	}
}
