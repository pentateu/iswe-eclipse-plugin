package nz.co.iswe.generator.impl.eclipse;

import nz.co.iswe.generator.IGeneratorHandler;

import org.eclipse.core.resources.IProject;

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

	private IProject project;
	
	public EclipseGeneratorHandler(IProject project) {
		this.project = project;
	}
	
	@Override
	public boolean exists(String fullyQualifiedName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void saveContent(String fullyQualifiedName, String content) {
		// TODO Auto-generated method stub
		
	}

	
	
}
