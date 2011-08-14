package nz.co.iswe.generator.impl.eclipse;


import java.util.List;

import nz.co.iswe.generator.IGeneratorContext;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;

public interface IEclipseGeneratorContext extends IGeneratorContext {

	void setProject(IProject project) throws CoreException;

	List<IType> queryTypes(ITypeFilter filter);

	
}
