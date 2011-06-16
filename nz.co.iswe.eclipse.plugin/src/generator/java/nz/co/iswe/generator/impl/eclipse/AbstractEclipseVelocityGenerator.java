package nz.co.iswe.generator.impl.eclipse;

import java.io.StringWriter;

import nz.co.iswe.generator.impl.AbstractVelocityGenerator;
import nz.co.iswe.generator.impl.helper.IPattern;
import nz.co.iswe.generator.info.EntityInfo;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;


public abstract class AbstractEclipseVelocityGenerator extends AbstractVelocityGenerator {

	private static final String NO = "no";
	private static final String ENTITY = "Entity";
	
	protected IPattern pattern;
	
	public AbstractEclipseVelocityGenerator(){
		
	}
	
	@Override
	public synchronized void process(EntityInfo entityInfo) throws Exception  {
		
		//create the velocity context
		VelocityContext ctx = buildVelocityContext();
		
		// ### Put the entity in the 
		// ${Entity.getSimpleName()}
		ctx.put(ENTITY, entityInfo);
		
		String fullyQualifiedName = getFullyQualifiedName(entityInfo);
		
		boolean outputExists = generatorHandler.exists(fullyQualifiedName);
		
		boolean generateOutput = true;
		
		if(outputExists && generatorConfig.getOutput().getOverride().equals(NO)){
			generateOutput = false;
		}
		
		if(generateOutput){
			// get the template.
    		Template template = Velocity.getTemplate(generatorConfig.getInput().getFilename());
			
			//Output
			StringWriter wrt = new StringWriter();
			
			// merge the template
			template.merge(ctx, wrt);
			
			//save the result
			generatorHandler.saveContent(fullyQualifiedName, wrt.toString());
			
			wrt.close();
		}
	}

	protected String getFullyQualifiedName(EntityInfo entityInfo) throws Exception {
		// create the complete path for the output file
		return pattern.getAbstractFullyQualifiedName(entityInfo);
	}	
}
