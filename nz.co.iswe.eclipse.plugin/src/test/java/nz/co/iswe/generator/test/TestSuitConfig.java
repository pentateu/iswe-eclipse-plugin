package nz.co.iswe.generator.test;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class TestSuitConfig {

	public static Properties getTestProperties(){
		Properties properties = new Properties();
		InputStream is = null;
		try {
			is = TestSuitConfig.class.getResourceAsStream("test_suite.properties");
			properties.load(is);
		}
		catch (IOException e) {
			throw new RuntimeException("Error trying to load test_suite.properties", e);
		}
		finally{
			try {
				is.close();
			}
			catch (Throwable e) {/*ignore*/}
		}
		return properties;
	}
	
}
