package nz.co.iswe.generator.impl.eclipse;

import java.util.ArrayList;
import java.util.List;

import nz.co.iswe.generator.impl.eclipse.jpa.EclipseJpaEntityInfo;
import nz.co.iswe.generator.info.EntityInfo;
import nz.co.iswe.generator.info.EntityInfoFactory;

import org.eclipse.jdt.core.IType;

public abstract class AbstractEclipseEntityInfoFactory extends EntityInfoFactory implements IEclipseEntityInfoFactory {

	@Override
	protected EntityInfo buildInfoEntidade(String entityFullName) {
		IType itype = findType(entityFullName);
		return new EclipseJpaEntityInfo(itype);
	}

	private IType findType(final String entityFullName) {
		
		if(entityFullName == null || entityFullName.trim().length() == 0){
			return null;
		}
		
		//get the context
		IEclipseGeneratorContext context = EclipseGeneratorContext.getInstance();
		
		List<IType> types = context.queryTypes(new ITypeFilter() {
			@Override
			public boolean match(IType type) {
				String typeFullName = type.getFullyQualifiedName();
				return entityFullName.equals(typeFullName);
			}
		});
		
		if(types.size() > 0){
			return types.get(0);
		}
		else{
			return null;
		}
	}

	public List<EntityInfo> getEntityList(List<IType> types) {
		List<EntityInfo> result = new ArrayList<EntityInfo>();
		for(IType itype : types){
			if(contains(itype.getFullyQualifiedName())){
				result.add(getEntityInfo(itype.getFullyQualifiedName()));
			}
			else{
				EntityInfo entityInfo = new EclipseJpaEntityInfo(itype);
				result.add(entityInfo);
				addToPool(entityInfo);
			}
		}
		return result;
	}

	public void loadAllEntities() {
		ITypeFilter filter = new ITypeFilter() {
			@Override
			public boolean match(IType type) {
				return isValidEntityType(type);
			}
		};
		
		//get the context
		IEclipseGeneratorContext context = EclipseGeneratorContext.getInstance();
		
		List<IType> types = context.queryTypes(filter);
		
		for(IType itype : types){
			if( ! contains(itype.getFullyQualifiedName())){
				addToPool(buildNewEntityInfo(itype));
			}
		}
		
		processEntitiesInfo();
	}

	public abstract boolean isValidEntityType(IType type);

	protected abstract EntityInfo buildNewEntityInfo(IType itype);
	
}
