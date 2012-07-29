import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class TestePattern {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// teste1();
		// teste2();
		// teste3();
		// teste4();
		// teste5();
		// teste6();
		teste7();

	}

	private static void teste1() {
		String s = "Executando um teste de RegEx...";

		Pattern p = Pattern.compile("[tT]este");
		// Pattern p = Pattern.compile(s);

		Matcher m = p.matcher(s);

		if (m.matches()) {
			System.out.println("ok");
		}

		System.out.println("s=" + s);

		showMatcher(m);

		if (m.find()) {
			if (m.matches()) {
				System.out.println("ok");
			}
			System.out.println("Pattern: [" + p + "] econtrado em [" + s + "]");
		}

		showMatcher(m);
	}

	private static void teste2() {

		Pattern p = Pattern.compile("ab?");

		Matcher m1 = p.matcher("a");
		Matcher m2 = p.matcher("ab");
		Matcher m3 = p.matcher("abb");

		myMatches(m1);
		myMatches(m2);
		myMatches(m3);

	}

	private static void teste3() {

		Pattern p = Pattern.compile("ab*");

		Matcher m1 = p.matcher("a");
		Matcher m2 = p.matcher("ab");
		Matcher m3 = p.matcher("abb");
		Matcher m4 = p.matcher("abbb");

		myMatches(m1);
		myMatches(m2);
		myMatches(m3);
		myMatches(m4);

	}

	private static void teste4() {

		Pattern p = Pattern.compile("ab+");

		Matcher m1 = p.matcher("a");
		Matcher m2 = p.matcher("ab");
		Matcher m3 = p.matcher("abb");
		Matcher m4 = p.matcher("abbb");

		myMatches(m1);
		myMatches(m2);
		myMatches(m3);
		myMatches(m4);

	}

	private static void teste5() {

		try {
			Pattern p = Pattern.compile("ab{3,}");

			Matcher m1 = p.matcher("a");
			Matcher m2 = p.matcher("ab");
			Matcher m3 = p.matcher("abb");
			Matcher m4 = p.matcher("abbb");

			myMatches(m1);
			myMatches(m2);
			myMatches(m3);
			myMatches(m4);
		} catch (PatternSyntaxException ex) {
			System.out.println(ex);
		}

	}

	private static void teste6() {

		try {
			Pattern p = Pattern.compile("(\\d{1,})/(\\d{1,})/(\\d{2,})");

			Matcher m1 = p.matcher("11/11/1111");
			Matcher m2 = p.matcher("11/99/1111");
			Matcher m3 = p.matcher("1/1/11");
			Matcher m4 = p.matcher("abbb");

			myMatches(m1);
			myMatches(m2);
			myMatches(m3);
			myMatches(m4);
		} catch (PatternSyntaxException ex) {
			System.out.println(ex);
		}

	}

	private static void teste7() {

		try {
			//Pattern p = Pattern.compile("(\\d{1,})/(\\d{1,})/(\\d{2,})");
			Pattern p = Pattern.compile("\\+??(\\d\\d)(\\s){0,1}\\((\\d\\d)\\)(\\s){0,1}(\\d{3,5})(-{0,1})(\\d{4})(.){0,}");

			//Matcher m = p.matcher("22/7/12");
			//Matcher m = p.matcher("+55 (19) 8143-8978");
			//Matcher m = p.matcher("+55(19)143-8978");
			Matcher m = p.matcher("55 (19) 8143-8978");

			if (m.matches()) {
				
				System.out.println("m.grooup: "+m.group());

				for (int i = 0; i <= m.groupCount(); i++) {
					System.out.println("grupo: " + i + ": " + m.group(i));
				}

			} else {
				System.out.println("m inválidada");
			}

		} catch (PatternSyntaxException ex) {

		}

	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	public static boolean validaData(String data) {

	  if(data==null) {
	    return false;
	  }
	  
		boolean dataValida = false;
		
		try {
			Pattern p = Pattern.compile("(\\d{1,})/(\\d{1,})/(\\d{2,})");

			Matcher m = p.matcher(data);

			if (m.matches()) {
				
				System.out.println("m.grooup: "+m.group());

				for (int i = 0; i <= m.groupCount(); i++) {
					System.out.println("grupo: " + i + ": " + m.group(i));
				}

				dataValida=true;
				
			} else {
				System.out.println("m inválidada");
			}

		} catch (PatternSyntaxException ex) {

		}

		return dataValida;
		
	}

	
	private static void myMatches(Matcher m1) {
		if (m1.matches()) {
			System.out.println("m1 validada: " + m1.group());
		} else {
			System.out.println("m1 invalidada");
		}
	}

	/**
	 * Exibe o resultado
	 * 
	 * @param m
	 *          Matcher
	 */
	public static void showMatcher(Matcher m) {

		System.out.println("\n==> Matcher()");

		if (m == null) {
			return;
		}

		boolean resultado = m.find();

		System.out.println("find(): " + resultado);

		if (resultado) {
			System.out.println("start():" + m.start());
			System.out.println("end():" + m.end());
			System.out.println("group():" + m.group());
			System.out.println("groupCount():" + m.groupCount());
		}

	}

}
