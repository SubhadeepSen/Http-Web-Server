package sdp.processor;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The AnnotatedClassLoader loads the available classes from a particular
 * package.
 * 
 * @author Subhadeep Sen
 */
@SuppressWarnings("rawtypes")
public class AnnotatedClassLoader {

	@SuppressWarnings("deprecation")
	public List<Class> getClasses(ClassLoader classLoader, String packageName) throws Exception {
		String urlPath = packageName.replaceAll("[.]", "/");
		List<Class> classes = new ArrayList<Class>();
		URL upackage = classLoader.getResource(urlPath);

		DataInputStream dis = new DataInputStream((InputStream) upackage.getContent());
		String line = null;
		while ((line = dis.readLine()) != null) {
			if (line.endsWith(".class")) {
				classes.add(Class.forName(packageName + "." + line.substring(0, line.lastIndexOf('.'))));
			}
		}
		return classes;
	}
}
