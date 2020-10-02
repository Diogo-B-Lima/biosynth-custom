package loader;

public class utils {


	public static String parseQuotes(String propertyValue) {

		return propertyValue.replace("\n", "").replace("\"", "'");
	}

	public static String parseBackslash(String propertyValue) {
		return propertyValue.replace("\\", "\\\\");
	}

	public static String parseHyphen(String propertyName) {
		return propertyName.replace("-", "_");
	}
}
