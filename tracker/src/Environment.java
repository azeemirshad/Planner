
import java.util.Enumeration;
import java.util.ResourceBundle;

public class Environment {

	private static ResourceBundle resources;
	static {
		loadResources();
	}

	private static void loadResources() {
		System.out.println("*****************************Loading bundle");
		resources = ResourceBundle.getBundle("bundle");
		 printResources( resources );
	}

	public static String getProperty(String key) {
		//System.out.println("*****************************Getting docs property");
		String value = resources.getString(key);
		return value;
	}
	private static void printResources( ResourceBundle resources )
	{
		System.out.println("*****************************Printing Docs bundle");
		Enumeration printEnum = resources.getKeys();
		while( printEnum.hasMoreElements() )
		{
			String key = printEnum.nextElement().toString();
			System.out.println( key + " :docs: " + resources.getString(key) );
		}
	}
}