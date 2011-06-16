package nz.co.iswe.generator.impl.helper;

import nz.co.iswe.generator.helper.IHelper;
import nz.co.iswe.generator.info.EntityInfo;

public interface IPattern extends IHelper {

	//Properties available on each pattern helper config
	String ABSTRACT_FULLY_QUALIFIED_NAME = "abstract.fullyQualifiedName";
	String INTERFACE_FULLY_QUALIFIED_NAME = "interface.fullyQualifiedName";
	String IMPL_FULLY_QUALIFIED_NAME = "impl.fullyQualifiedName";
	
	
	String getAbstractFullyQualifiedName(EntityInfo entityInfo) throws Exception;
	String getAbstractSimpleName(EntityInfo entityInfo)  throws Exception;
	
	String getInterfaceFullyQualifiedName(EntityInfo entityInfo) throws Exception;
	String getInterfaceSimpleName(EntityInfo entityInfo)  throws Exception;
	
	String getImplFullyQualifiedName(EntityInfo entityInfo) throws Exception;
	String getImplSimpleName(EntityInfo entityInfo)  throws Exception;
	
	String getPackage(EntityInfo entityInfo);
	
}
