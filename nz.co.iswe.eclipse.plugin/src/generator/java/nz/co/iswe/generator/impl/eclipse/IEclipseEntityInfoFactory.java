package nz.co.iswe.generator.impl.eclipse;

import java.util.List;

import org.eclipse.jdt.core.IType;

import nz.co.iswe.generator.info.EntityInfo;
import nz.co.iswe.generator.info.IEntityInfoFactory;

public interface IEclipseEntityInfoFactory extends IEntityInfoFactory {
	List<EntityInfo> getEntityList(List<IType> types);
	void loadAllEntities();
	boolean isValidEntityType(IType type);
}
