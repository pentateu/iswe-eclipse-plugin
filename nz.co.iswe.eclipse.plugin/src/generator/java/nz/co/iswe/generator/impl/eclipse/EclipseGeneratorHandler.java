package nz.co.iswe.generator.impl.eclipse;

import nz.co.iswe.generator.IGeneratorHandler;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Eclipse implementation of the IGeneratorHandler
 * 
 * This class is responsible for:
 *  - Verifying is an existing Entity exists (IType inside the Eclipse IProject)
 *  - Save the content of the Entity. Either create a new one if none exists or update
 *    an existing entity.
 * 
 * @author Rafael Almeida
 *
 */
public class EclipseGeneratorHandler implements IGeneratorHandler {

	private IJavaProject javaProject;
	
	public EclipseGeneratorHandler(IJavaProject javaProject) {
		this.javaProject = javaProject;
	}
	
	@Override
	public boolean exists(String fullyQualifiedName) {
		IType type = null;
		try {
			type = javaProject.findType(fullyQualifiedName);
		}
		catch (JavaModelException e) {
			//not found or error trying to find
		}
		return type != null && type.exists();
	}

	@Override
	public void saveJavaClass(String packageName, String className, String content){
		
		String fileName = className + ".java";
		
		try {
			//TODO: get the package root based on the source folder defined in the configuration
			IPackageFragmentRoot fragmentRoot = javaProject.getPackageFragmentRoots()[0];
			
			IPackageFragment packageFragment = fragmentRoot.getPackageFragment(packageName);
			
			//if(packageFragment == null || ! packageFragment.isConsistent()){
				packageFragment = fragmentRoot.createPackageFragment(packageName, true, null);
			//}
			
			ICompilationUnit compilationUnit = packageFragment.getCompilationUnit(fileName);
			IResource resource = compilationUnit.getResource();
			if( ! resource.exists() ){
				compilationUnit = packageFragment.createCompilationUnit(fileName, content, true, null);
			}
			else{
				compilationUnit.delete(true, null);
				compilationUnit = packageFragment.createCompilationUnit(fileName, content, true, null);
			}
			
			
		} 
		catch (JavaModelException e) {
			throw new RuntimeException("Error creating a new Java Class. packageName: " + packageName + " className: " + className + " content: " + content, e);
		}
		
		
	}

	
	
}
