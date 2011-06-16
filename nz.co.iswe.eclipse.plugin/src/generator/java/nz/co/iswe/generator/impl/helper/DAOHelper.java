package nz.co.iswe.generator.impl.helper;

import nz.co.iswe.generator.helper.AbstractHelper;
import nz.co.iswe.generator.helper.IHelper;
import nz.co.iswe.generator.info.EntityInfo;


public class DAOHelper extends AbstractHelper implements IHelper, IPattern {
	
	private static final String DAO_PACKAGE_TO = "packageTo";
	private static final String DAO_PACKAGE_FROM = "packageFrom";
	public static final String NAME = "daoHelper";
	
	//private static final String DAO_IMPL = "DAOImpl";
	//public static final String DAO = "DAO";
	//public static final String DAO_ABSTRACT = "DAOAbstract";
	//public static final String CONTEXT_NAME = DAO;
	
	protected DAOHelper() {
		super(NAME);
	}
	
	/**
	 * Método responsável por construir o nome de um bean Spring de acordo com a entidade.
	 * @param entityInfo
	 * @return
	 
	public String getSpringName(EntityInfo entityInfo){
		StringBuffer daoSpringName = new StringBuffer();
		String prefix = entityInfo.getGeneratorContext().getProperty(GERADOR_CONFIGS_PREFIX_DAO_NAME);
		if(prefix != null){
			daoSpringName.append(prefix);
		}
		daoSpringName.append(entityInfo.getNormalizedName());
		daoSpringName.append(DAO);
		return daoSpringName.toString();
	}
	 
	*/
	

	public String getAbstractFullyQualifiedName(EntityInfo entityInfo) throws Exception{
		return parseProperty(entityInfo, ABSTRACT_FULLY_QUALIFIED_NAME);
	}

	public String getAbstractSimpleName(EntityInfo entityInfo) throws Exception{
		return toSimpleName( getAbstractFullyQualifiedName(entityInfo));
	}

	private String parseProperty(EntityInfo entityInfo, String property) throws Exception {
		ExpressionParser parser = new ExpressionParser();
		parser.put("dao", this);
		parser.put("entity", entityInfo);
		String expression = getProperty(property);
		String result = parser.parse(expression);
		return result;
	}
	
	private String toSimpleName(String fullyQualifiedName) {
		String [] parts = fullyQualifiedName.split("\\.");
		return parts[parts.length - 1];
	}

	/**
	 * @param entityInfo
	 * @return
	 */
	public String getPackage(EntityInfo entityInfo) {
		return replacePackage(entityInfo, DAO_PACKAGE_FROM, DAO_PACKAGE_TO);
	}

	@Override
	public String getInterfaceFullyQualifiedName(EntityInfo entityInfo) throws Exception {
		return parseProperty(entityInfo, INTERFACE_FULLY_QUALIFIED_NAME);
	}

	@Override
	public String getInterfaceSimpleName(EntityInfo entityInfo)
			throws Exception {
		return toSimpleName( getInterfaceFullyQualifiedName(entityInfo));
	}

	@Override
	public String getImplFullyQualifiedName(EntityInfo entityInfo)
			throws Exception {
		return parseProperty(entityInfo, IMPL_FULLY_QUALIFIED_NAME);
	}

	@Override
	public String getImplSimpleName(EntityInfo entityInfo) throws Exception {
		return toSimpleName( getImplFullyQualifiedName(entityInfo));
	}
}
