package br.com.mltech;

import java.util.Scanner;

/**
 * 
 * 
 *
 */
public class Jogo {

	/**
	 * 
	 * @param a
	 */
	public static void main(String[] a) {
		Jogo2();

	}

	public static void Jogo1() {
		int[][] matriz = new int[][] { { 1, 2, 3 }, { 4, 5, 0 }, { 6, 7, 8 } };

		Tabuleiro t = new Tabuleiro(matriz);

		t.exibeTabuleiro();
		System.out.println();

		System.out.println(t.getPosicaoVazia());

		System.out.println(t.listaMovimentosValidos());

		System.out.println("===>");

		if (t.isMovimentoValido(Operation.DOWN)) {
			System.out.println("Operação DOWN é válida");
		}

		if (t.isMovimentoValido(Operation.UP)) {
			System.out.println("Operação UP é válida");
		}

		if (t.isMovimentoValido(Operation.LEFT)) {
			System.out.println("Operação LEFT é válida");
		}

		if (t.isMovimentoValido(Operation.RIGHT)) {
			System.out.println("Operação RIGHT é válida");
		}

		boolean b = t.executaMovimento(Operation.DOWN);

		if (b) {
			System.out.println("Movimento realizado com sucesso");
		} else {
			System.out.println("Movimento não realizado");
		}

		t.exibeTabuleiro();
		System.out.println();

	}

	/**
	 * 
	 */
	public static void Jogo2() {

		int[][] matriz = new int[][] { { 1, 2, 3 }, { 4, 0, 5 }, { 6, 7, 8 } };

		Tabuleiro t = new Tabuleiro(matriz);

		int numRodadas=0;
		
		do {

			t.exibeTabuleiro();
			System.out.println();

			System.out.println("Movimentos válidos: "
					+ t.listaMovimentosValidos());

			numRodadas++;
			
			System.out.println(" ===> (1=Up;2=Down;3=Left;4=Right): ");
			
			Scanner sc = new Scanner(System.in);
		    int i = sc.nextInt();
		    
		    
		    
		    if(i==-1) {
		    	break;
		    }

		    boolean b=false;
		    
		    switch(i) {
		    case 1:
				b = t.executaMovimento(Operation.DOWN);
		    	break;
		    case 2:
				b = t.executaMovimento(Operation.UP);
		    	break;
		    case 3:
				b = t.executaMovimento(Operation.LEFT);
		    	break;
		    case 4:
				b = t.executaMovimento(Operation.RIGHT);
		    	break;
		    }

			if (b) {
				System.out.println("Movimento realizado com sucesso");
			} else {
				System.out.println("Movimento não realizado");
			}

			//t.exibeTabuleiro();
			//System.out.println();

		} while (true);

		System.out.println("fim!");
		
		System.out.println("N´mero de rodadas: "+numRodadas);
		
	}

}
