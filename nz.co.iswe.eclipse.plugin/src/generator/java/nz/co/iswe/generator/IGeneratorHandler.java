package nz.co.iswe.generator;

public interface IGeneratorHandler {

	boolean exists(String fullyQualifiedName);

	void saveContent(String fullyQualifiedName, String content);

}
