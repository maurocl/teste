public class Log {

	static void i(String tag, String value) {
		System.out.println("INFO: " + tag + " - " + value);
	}

	static void w(String tag, String value) {
		System.out.println("WARNING: " + tag + " - " + value);
	}
	
	static void d(String tag, String value) {
		System.out.println("DEBUG: " +tag + " - " + value);
	}
	
	static void e(String tag, String value) {
		System.out.println("ERROR: "+tag + " - " + value);
	}
	
}
