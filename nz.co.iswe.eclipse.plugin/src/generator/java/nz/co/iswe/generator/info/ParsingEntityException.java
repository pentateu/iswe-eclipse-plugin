package nz.co.iswe.generator.info;


/**
 * 
 * Exception thrown when a entityinfo cannot be created or parsed
 * 
 * @author Rafael Almeida
 *
 */
@SuppressWarnings("serial")
public class ParsingEntityException extends RuntimeException {

	public ParsingEntityException(String message, Throwable e) {
		super(message, e);
	}

}
