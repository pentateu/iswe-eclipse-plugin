package nz.co.iswe.generator.impl.eclipse;

import org.eclipse.jdt.core.IType;

public interface ITypeFilter {

	boolean match(IType type);

}
