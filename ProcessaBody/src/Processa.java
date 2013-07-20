import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * O objetivo dessa classe é fazer o parser the strings no formato:
 * 
 * "Compra aprovada no seu FIAT ITAU VISA PLAT final 2655               - NETFLIX           valor RS 16,90  em 18/07/2013, as 10h45."
 * "Compra aprovada no     FIAT MASTER PLAT p/ DANIELE CRISTINA J LOPES - CARREFOUR ECA 074 valor RS 126,25 em 17/07/2013 as 10h30."
 * 
 * O formato das string é inicialmente dividido em duas partes separadas pelo
 * caracter "-". São as chamadas parte1 e parte2
 * 
 * Na parte1 temos as informações sobre o cartão, se for uma compra feita pelo
 * titular ou por um dependente. No caso de uma compra feita pelo titular do
 * cartão são informadas as informações sobre o nome do cartão e o final do
 * cartão No caso de uma compra feita por um dependente do cartão são informados
 * o nomer do cartão e o nome do dependente
 * 
 * Na parte dois temos informações sobre a compra constando das seguintes
 * informações: - nome do estabelecimento - valor pago (prefixo da moeda +
 * valor) - data (formato dd/mm/aaaa) e hora (hh:mi).
 * 
 */
public class Processa {

	/**
	 * 
	 * @param body
	 * 
	 *            "Compra aprovada no seu FIAT ITAU VISA PLAT final 2655               - NETFLIX           valor RS 16,90  em 18/07/2013, as 10h45."
	 *            "Compra aprovada no     FIAT MASTER PLAT p/ DANIELE CRISTINA J LOPES - CARREFOUR ECA 074 valor RS 126,25 em 17/07/2013 as 10h30."
	 * 
	 */
	List<String> processaBody(String body) {

		List<String> lista = new ArrayList<String>();

		List<String> lista1;
		List<String> lista2;

		// nome do cartão
		// p/ <nome>
		// final XXXX
		// -
		// Lugar
		// valor
		// moeda
		//
		// em
		// dd/mm/aaaa
		// hihmi
		// .

		String part1 = null;
		String part2 = null;

		String[] splitString = (body.split("-"));

		if (splitString != null) {

			part1 = splitString[0];
			part2 = splitString[1];

			System.out.println("");

			System.out.println("part1 = [" + part1 + "]");
			System.out.println("part2 = [" + part2 + "]");

			lista1 = checkPart1(part1);

			lista.addAll(lista1);

			lista2 = checkPart2(part2);

			lista.addAll(lista2);

		}

		return lista;

	}

	/**
	 * 
	 * @param part1
	 */
	public List<String> checkPart1(String part1) {

		List<String> lista = new ArrayList<String>();

		// "Compra aprovada no seu FIAT ITAU VISA PLAT final 2655               - NETFLIX           valor RS 16,90  em 18/07/2013, as 10h45."
		// "Compra aprovada no     FIAT MASTER PLAT p/ DANIELE CRISTINA J LOPES - CARREFOUR ECA 074 valor RS 126,25 em 17/07/2013 as 10h30."

		// "Compra aprovada no seu FIAT ITAU VISA PLAT final 2655"
		System.out.println("\nProcessando part1:");

		Pattern pattern = Pattern
				.compile("^Compra aprovada no seu\\s(.*)\\sfinal\\s(.*)\\s");

		Pattern pattern2 = Pattern
				.compile("^Compra aprovada no\\s(.*)\\sp/\\s(.*)\\s");

		Matcher matcher = pattern.matcher(part1);

		Matcher matcher2 = pattern2.matcher(part1);

		// Check all occurance

		System.out.println(("ok1"));
		while (matcher.find()) {

			System.out.println("0: [" + matcher.group(0) + "]");
			System.out.println("1: [" + matcher.group(1) + "]");
			System.out.println("2: [" + matcher.group(2) + "]");
			// System.out.println("3: ["+matcher.group(3)+"]");

			lista.add(matcher.group(1));
			lista.add(matcher.group(2));

		}

		System.out.println(("ok2"));

		while (matcher2.find()) {

			System.out.println("0: [" + matcher2.group(0) + "]");
			System.out.println("1: [" + matcher2.group(1) + "]");
			System.out.println("2: [" + matcher2.group(2) + "]");
			// System.out.println("3: ["+matcher.group(3)+"]");

			lista.add(matcher2.group(1));
			lista.add(matcher2.group(2));

		}

		return lista;

	}

	/**
	 * 
	 * @param part2
	 */
	public List<String> checkPart2(String part2) {

		List<String> lista = new ArrayList<String>();

		System.out.println("\nProcessando part2:");

		// CARREFOUR ECA 074 valor RS 126,25 em 17/07/2013 as 10h30.
		// CARREFOUR ECA 074 valor RS 126,25 em 17/07/2013 as 10h30."
		// NETFLIX valor RS 16,90 em 18/07/2013, as 10h45."

		// Pattern pattern =
		// Pattern.compile("\\s*(.*)\\s+valor\\s+(.*)\\s+em\\s*(.*)(\\,)?\\sas\\s*(.*)\\.");

		Pattern pattern = Pattern
				.compile("\\s*(.*)\\s+valor\\s+(.*)\\s+em\\s*(\\d\\d/\\d\\d/\\d\\d\\d\\d)(\\,)?\\sas\\s*(.*)\\.");

		// In case you would like to ignore case sensitivity you could use this
		// statement
		// Pattern pattern = Pattern.compile("\\s+", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(part2);

		System.out.println("GroupCount(): " + matcher.groupCount());

		/*
		 * for(int i=0; i<matcher.groupCount();i++) { System.out.println(i+": ["
		 * + matcher.group(i) + "]");
		 * 
		 * }
		 */

		// Check all occurance

		while (matcher.find()) {

			System.out.print("Start index: " + matcher.start());
			System.out.print(" End index: " + matcher.end() + " ");
			System.out.println("\nmatcher.group(): [" + matcher.group() + "]");

			System.out.println("0: [" + matcher.group(0) + "]");
			System.out.println("1: [" + matcher.group(1) + "]");

			System.out.println("2: [" + matcher.group(2) + "]");
			System.out.println("3: [" + matcher.group(3) + "]");
			System.out.println("4: [" + matcher.group(4) + "]");
			System.out.println("5: [" + matcher.group(5) + "]");

			lista.add(matcher.group(1));
			lista.add(matcher.group(2));
			lista.add(matcher.group(3));
			lista.add(matcher.group(4));
			lista.add(matcher.group(5));

		}

		return lista;

	}

	/**
	 * Exibe no console o número de elementos da lista e em seguida cada um de
	 * seus elementos em uma linha separada.
	 * 
	 * @param lista
	 *            Lista de Strings
	 * 
	 */
	public void showList(List<String> lista) {

		if (lista == null) {
			return;
		}

		System.out.println(("Tamanho da lista: " + lista.size()));

		for (String s : lista) {
			System.out.println("s=[" + s+"]");
		}

	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Processa processa = new Processa();

		List<String> lista = new ArrayList<String>();

		// "Compra aprovada no seu FIAT ITAU VISA PLAT final 2655 - NETFLIX valor RS 16,90 em 18/07/2013, as 10h45."
		// "Compra aprovada no FIAT MASTER PLAT p/ DANIELE CRISTINA J LOPES - CARREFOUR ECA 074 valor RS 126,25 em 17/07/2013 as 10h30."

		String body = "Compra aprovada no seu FIAT ITAU VISA PLAT final 2655 - LEROY MERLIN CAMPINAS valor RS 57,24 em 13/07/2013, as 11h03.";

		// Compra aprovada no FIAT MASTER PLAT p/ DANIELE CRISTINA J LOPES - RI
		// HAPPY SHOP IGU CAMP valor RS 29,99 em 13/07/2013 as 12h00.
		// Compra aprovada no seu FIAT ITAU VISA PLAT final 2655 - LEROY MERLIN
		// CAMPINAS valor RS 57,24 em 13/07/2013, as 11h03.

		lista = processa.processaBody(body);
		processa.showList(lista);

		System.out.println("\n----------------------------\n");
		String body2 = "Compra aprovada no FIAT MASTER PLAT p/ DANIELE CRISTINA J LOPES - RI HAPPY SHOP IGU CAMP valor RS 29,99 em 13/07/2013 as 12h00.";

		lista = processa.processaBody(body2);
		processa.showList(lista);

	}

}
