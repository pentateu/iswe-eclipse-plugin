package nz.co.iswe.generator;

public interface IGeneratorHandler {

	boolean exists(String fullyQualifiedName);

	void saveJavaClass(String packageName, String className, String content);

}
