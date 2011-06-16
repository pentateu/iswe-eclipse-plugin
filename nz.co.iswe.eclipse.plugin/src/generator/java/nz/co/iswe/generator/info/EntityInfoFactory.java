package nz.co.iswe.generator.info;

import java.util.HashMap;

import nz.co.iswe.generator.GeneratorContext;

public abstract class EntityInfoFactory implements IEntityInfoFactory {
	
	private static final ThreadLocal<EntityInfoFactory> threadLocalInstance = new ThreadLocal<EntityInfoFactory>();
	
	//private static String implClass = null;
	
	@SuppressWarnings("unchecked")
	public static EntityInfoFactory getInstance(){
		if(threadLocalInstance.get() == null){
			String implClass = GeneratorContext.getInstance().getConfiguration().getEntityInfoFactory();
			
			if(implClass == null){
				throw new RuntimeException("EntityInfoFactory.implClass is null!");
			}
			
			EntityInfoFactory instance = null;
			
			try {
				Class<EntityInfoFactory> clazz = (Class<EntityInfoFactory>) Class.forName(implClass);
				instance = (EntityInfoFactory) clazz.newInstance();
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("EntityInfoFactory.implClass class not found!", e);
			} catch (InstantiationException e) {
				throw new RuntimeException("EntityInfoFactory.implClass class instantiation error!", e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("EntityInfoFactory.implClass class instantiation error!", e);
			}
			
			threadLocalInstance.set(instance);
		}
		return threadLocalInstance.get();
	}
	
	private HashMap<String, EntityInfo> poolEntidades = null;
	
	protected EntityInfoFactory(){
		poolEntidades = new HashMap<String, EntityInfo>(); 
	}

	public EntityInfo getEntityInfo(String fullyQualifiedName) {
		EntityInfo retorno = poolEntidades.get(fullyQualifiedName);
		if(retorno == null){
			retorno = buildInfoEntidade(fullyQualifiedName);
			addToPool(retorno);
		}
		return retorno;
	}

	protected boolean contains(String fullyQualifiedName) {
		return poolEntidades.containsKey(fullyQualifiedName);
	}
	
	protected void addToPool(EntityInfo entityInfo) {
		poolEntidades.put(entityInfo.getFullyQualifiedName(), entityInfo);
	}
	
	protected void processEntitiesInfo() {
		/*
		for (EntityInfo entidade : poolEntidades.values()) {
			
			List<PropertyInfo> lstProps = entidade.getProperties();
			
			for (PropertyInfo propriedade : lstProps) {
				if(propriedade.getPropertyType() == PropertyInfo.TIPO_COLL_REF 
						|| propriedade.getPropertyType() == PropertyInfo.TIPO_OBJ_REF
						|| propriedade.getPropertyType() == PropertyInfo.TIPO_COLL_REF_DETAIL){
					//Setando na InfoEntidade a pasta do JSP
					ClassEntityInfoImpl refEntidade = (ClassEntityInfoImpl)propriedade.getReferenceEntity();
					
					String pastaJsp = refEntidade.replacePackage(GeradorJSP.GERADOR_JSP_PACKAGE_FROM, GeradorJSP.GERADOR_JSP_PACKAGE_TO);
					if(pastaJsp != null){
						pastaJsp = pastaJsp.replace('.', '/');
						refEntidade.setProperty(BaseEntityInfo.PASTA_JSP, pastaJsp);
					}
					
					if(propriedade.getPropertyType() == PropertyInfo.TIPO_COLL_REF){
						refEntidade.setProperty(BaseEntityInfo.IS_COLL_REF, TRUE);
					}
					else if(propriedade.getPropertyType() == PropertyInfo.TIPO_OBJ_REF){
						refEntidade.setProperty(BaseEntityInfo.IS_OBJ_REF, TRUE);
					}
					else if(propriedade.getPropertyType() == PropertyInfo.TIPO_COLL_REF_DETAIL){
						if(entidade.getEntidadePai() != null){
							refEntidade.setProperty(BaseEntityInfo.COLL_REF_DETAIL_NOME_MESTRE, entidade.getEntidadePai().getSimpleName());
							refEntidade.setProperty(BaseEntityInfo.COLL_REF_DETAIL_PKG_MESTRE, entidade.getEntidadePai().getPackageEntidadeFull());
						}
						else{
							refEntidade.setProperty(BaseEntityInfo.COLL_REF_DETAIL_NOME_MESTRE, entidade.getSimpleName());
							refEntidade.setProperty(BaseEntityInfo.COLL_REF_DETAIL_PKG_MESTRE, entidade.getPackageEntidadeFull());
						}
						refEntidade.setProperty(BaseEntityInfo.IS_COLL_REF_DETAIL, TRUE);
						
						String fullPackageBO = refEntidade.replacePackage(GeradorBO.GERADOR_BO_PACKAGE_FROM, GeradorBO.GERADOR_BO_PACKAGE_TO);
						refEntidade.setProperty(FULL_PACKAGE_BO, fullPackageBO);
					}
				}
			}
		}
		*/
	}

	protected abstract EntityInfo buildInfoEntidade(String entityFullName);
}
