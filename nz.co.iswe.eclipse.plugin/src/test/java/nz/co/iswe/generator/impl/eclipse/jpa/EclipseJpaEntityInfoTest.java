/**
 * 
 */
package nz.co.iswe.generator.impl.eclipse.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import nz.co.iswe.generator.annotation.MatchMode;
import nz.co.iswe.generator.impl.eclipse.EclipseGeneratorContext;
import nz.co.iswe.generator.impl.eclipse.IEclipseEntityInfoFactory;
import nz.co.iswe.generator.impl.eclipse.IEclipseGeneratorContext;
import nz.co.iswe.generator.info.EntityInfo;
import nz.co.iswe.generator.info.EntityInfoFactory;
import nz.co.iswe.generator.info.PropertyInfo;
import nz.co.iswe.generator.info.PropertyType;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rafael Almeida
 *
 */
public class EclipseJpaEntityInfoTest {

	IJavaProject javaProject = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject("TestProject-JPA");
		javaProject =JavaCore.create(project);
		
		IEclipseGeneratorContext context = EclipseGeneratorContext.getInstance();
		//set the project to be used in the Generator Context
		context.setProject(project);
		context.init();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link nz.co.iswe.generator.impl.eclipse.jpa.EclipseJpaEntityInfo#getPropertyList()}.
	 * @throws JavaModelException 
	 */
	@Test
	public void testGetPropertyList() throws JavaModelException {
		IType type = javaProject.findType("nz.co.iswe.samples.jpa.ClientEntity");
		
		List<IType> selectedEntities = new ArrayList<IType>();
		selectedEntities.add(type);
		
		//IEclipseGeneratorContext context = EclipseGeneratorContext.getInstance();
		
		//Loading all entities meta-data
		IEclipseEntityInfoFactory factory = (IEclipseEntityInfoFactory)EntityInfoFactory.getInstance();
		factory.loadAllEntities();
		
		
		//load the meta-data for the selected entities
		List<EntityInfo> entityList = factory.getEntityList(selectedEntities);
		
		EntityInfo entityInfo = entityList.get(0);
		
		List<PropertyInfo> props = entityInfo.getPropertyList();
		
		assertEquals("# of properties", 4, props.size());
		
		
		PropertyInfo propClientId = props.get(0);
		assertEquals(PropertyType.NUMERIC_INTEGER, propClientId.getPropertyType());
		assertEquals("clientId", propClientId.getName());
		assertEquals(entityInfo, propClientId.getSourceEntity());
		assertTrue(propClientId.isPrimaryKey());
		assertNull(propClientId.getReferenceEntity());
		assertEquals("clientId", propClientId.getLabel());
		assertFalse(propClientId.isNullable());
		assertFalse(propClientId.isTransient());
		assertFalse(propClientId.isOneToOne());
		assertFalse(propClientId.isManyToMany());
		assertFalse(propClientId.isManyToOne());
		assertFalse(propClientId.isOneToMany());
		assertEquals(MatchMode.ANYWHERE.toString(), propClientId.getMatchMode());
		
	}
}

