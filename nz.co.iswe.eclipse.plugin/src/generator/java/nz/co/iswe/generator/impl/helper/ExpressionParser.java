package nz.co.iswe.generator.impl.helper;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.parser.ParseException;

/**
 * Class responsible to parse/resolve expressions used inside the XML configuration files.
 * 
 * It uses Velocity internally to parse the expressions.
 * 
 * @author "Rafael Almeida"
 *
 */
public class ExpressionParser {

	private VelocityContext velocityContext = new VelocityContext();

	public void put(String name, Object value) {
		velocityContext.put(name, value);
	}

	public String parse(String expression) {
		try{
			Template template = new RuntimeStringTemplate(expression);
	
			StringWriter writer = new StringWriter();
			
			template.process();
	
			template.merge(velocityContext, writer);
	
			String result = writer.toString();
			
			writer.close();
			
			return result;
		}
		catch(Exception exp){
			throw new RuntimeException("Error parsing expression: " + expression, exp);
		}
	}

	class RuntimeStringTemplate extends Template {

		private VelocityException errorCondition = null;

		private StringReader reader;

		RuntimeStringTemplate(String content) {
			super();
			this.rsvc = new RuntimeInstance();
			reader = new StringReader(content);
		}

		@Override
		public boolean process() throws ResourceNotFoundException,
				ParseErrorException {

			data = null;
			errorCondition = null;

			try {
				data = rsvc.parse(reader, name);
				initDocument();
				return true;
			} catch (ParseException pex) {
				/*
				 * remember the error and convert
				 */
				errorCondition = new ParseErrorException(pex, name);
				throw errorCondition;
			} catch (TemplateInitException pex) {
				errorCondition = new ParseErrorException(pex, name);
				throw errorCondition;
			}
			/**
			 * pass through runtime exceptions
			 */
			catch (RuntimeException e) {
				errorCondition = new VelocityException(
						"Exception thrown processing Template " + getName(), e);
				throw errorCondition;
			} finally {
				reader.close();
			}
		}

		@SuppressWarnings("rawtypes")
		public void merge(Context context, Writer writer, List macroLibraries)
				throws ResourceNotFoundException, ParseErrorException,
				MethodInvocationException {
			/*
			 * we shouldn't have to do this, as if there is an error condition,
			 * the application code should never get a reference to the Template
			 */

			if (errorCondition != null) {
				throw errorCondition;
			}

			super.merge(context, writer, macroLibraries);
		}

	}

}
