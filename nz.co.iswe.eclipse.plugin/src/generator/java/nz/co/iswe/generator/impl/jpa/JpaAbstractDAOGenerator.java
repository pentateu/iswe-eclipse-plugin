package nz.co.iswe.generator.impl.jpa;

import nz.co.iswe.generator.impl.eclipse.AbstractEclipseVelocityGenerator;
import nz.co.iswe.generator.impl.helper.DAOHelper;
import nz.co.iswe.generator.impl.helper.IPattern;

public class JpaAbstractDAOGenerator extends AbstractEclipseVelocityGenerator {

	public JpaAbstractDAOGenerator() {
		super();
	}
	
	@Override
	public void init() {
		super.init();
		this.pattern = (IPattern)generatorContext.getHelperByName(DAOHelper.NAME);
	}

}
