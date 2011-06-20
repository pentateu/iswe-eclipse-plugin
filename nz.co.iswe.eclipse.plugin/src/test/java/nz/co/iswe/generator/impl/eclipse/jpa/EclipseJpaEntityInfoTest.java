/**
 * 
 */
package nz.co.iswe.generator.impl.eclipse.jpa;

import static org.junit.Assert.*;

import nz.co.iswe.eclipse.plugin.ISWEPlugin;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rafael Almeida
 *
 */
public class EclipseJpaEntityInfoTest {

	IProject project = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		project = root.getProject("ISWE-JPAProject");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link nz.co.iswe.generator.impl.eclipse.jpa.EclipseJpaEntityInfo#getPropertyList()}.
	 */
	@Test
	public void testGetPropertyList() {
		EclipseJpaEntityInfo entityInfo = null;
		assertNull(entityInfo);
		
		IType type = (IType)project.findMember("nz.co.iswe.samples.jpa.ClientEntity.java");
		entityInfo = new EclipseJpaEntityInfo(type);
		
		
		
		//ISWEPlugin iswePlugin = ISWEPlugin.getDefault();
		
		
	}
}
