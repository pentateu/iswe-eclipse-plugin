package nz.co.iswe.generator.impl.jpa;

import nz.co.iswe.generator.impl.eclipse.AbstractEclipseVelocityGenerator;

public class JpaDAOGenerator extends AbstractEclipseVelocityGenerator {

	
	
	public JpaDAOGenerator() {
		super();
	}
	
	@Override
	public void init() {
		super.init();
		//this.pattern = (IPattern)generatorContext.getHelperByName(DAOHelper.NAME);
	}
	
}
