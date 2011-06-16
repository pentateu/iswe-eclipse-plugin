package nz.co.iswe.generator.impl.eclipse.jpa;


import nz.co.iswe.generator.impl.eclipse.AbstractEclipseEntityInfoFactory;
import nz.co.iswe.generator.info.EntityInfo;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IType;

public class EclipseJpaEntityInfoFactory extends AbstractEclipseEntityInfoFactory {

	public EclipseJpaEntityInfoFactory(){
		super();
	}
	
	@Override
	protected EntityInfo buildNewEntityInfo(IType itype) {
		return new EclipseJpaEntityInfo(itype);
	}
	
	@Override
	public boolean isValidEntityType(IType type) {
		
		IAnnotation entityAnnotation = EclipseJpaUtil.getInstance().getAnnotation("javax.persistence.Entity", type);
		
		return entityAnnotation != null;
	}
	
}
