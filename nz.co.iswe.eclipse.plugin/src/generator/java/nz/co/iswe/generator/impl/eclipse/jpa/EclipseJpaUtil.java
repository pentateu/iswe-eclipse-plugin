package nz.co.iswe.generator.impl.eclipse.jpa;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IAnnotatable;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;

public class EclipseJpaUtil {

	private static EclipseJpaUtil instance;
	
	public static EclipseJpaUtil getInstance() {
		if(instance == null){
			instance = new EclipseJpaUtil();
		}
		return instance;
	}

	public IAnnotation getAnnotation(String completeName, IAnnotatable object) {
		String simpleName = completeName.substring(completeName.lastIndexOf('.') + 1);
		IAnnotation result = null;
		try {

			IAnnotation[] annotations = object.getAnnotations();
			for (IAnnotation annotation : annotations) {

				if (annotation.getElementName().equals(completeName)) {
					result = annotation;
					break;
				}
				else if (annotation.getElementName().equals(simpleName)) {
					// check the imports for a full match of the Entity
					// annotation
					
					if(object instanceof IMember){
						
						IMember member = (IMember)object;
						
						ICompilationUnit compilationUnit = member.getCompilationUnit();
	
						IImportDeclaration[] imports = compilationUnit.getImports();
	
						for (IImportDeclaration importDeclaration : imports) {
							if (importDeclaration.getElementName().equals(completeName)) {
								result = annotation;
								break;
							}
						}
						
					}
					else{
						
					}
					
				}

			}
		}
		catch (JavaModelException e) {
			throw new RuntimeException("Error looking-up for annotation: " + completeName, e);
		}
		
		return result;
	}

	public String lookUpImport(ICompilationUnit compilationUnit, String className) {
		try {
			IImportDeclaration[] imports = compilationUnit.getImports();
			
			for (IImportDeclaration importDeclaration : imports) {
				String impFullName = importDeclaration.getElementName();
				String impClassName = impFullName.substring(impFullName.lastIndexOf('.') + 1);
				
				if (impFullName.equals(className) || impClassName.equals(className)) {
					return importDeclaration.getElementName();
				}
			}
			return null;
		}
		catch (JavaModelException e) {
			throw new RuntimeException("Error looking-up for import: " + className, e);
		}
	}

}
