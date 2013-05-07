
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
    //JogoTeste();

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
      System.out.println("Opera��o DOWN � v�lida");
    }

    if (t.isMovimentoValido(Operation.UP)) {
      System.out.println("Opera��o UP � v�lida");
    }

    if (t.isMovimentoValido(Operation.LEFT)) {
      System.out.println("Opera��o LEFT � v�lida");
    }

    if (t.isMovimentoValido(Operation.RIGHT)) {
      System.out.println("Opera��o RIGHT � v�lida");
    }

    boolean b = t.executaMovimento(Operation.DOWN);

    if (b) {
      System.out.println("Movimento realizado com sucesso");
    } else {
      System.out.println("Movimento n�o realizado");
    }

    t.exibeTabuleiro();
    System.out.println();

  }

  /**
	 * 
	 */
  public static void Jogo2() {

    int[][] matriz1 = new int[][] { { 1, 2, 3 }, { 4, 0, 5 }, { 6, 7, 8 } };
    //int[][] matriz2 = new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
    int[][] matriz2 = new int[][] { { 1, 2, 3 }, { 4, 5, 0 }, { 6, 7, 8 } };

    Tabuleiro t1 = new Tabuleiro(matriz1);
    Tabuleiro t2 = new Tabuleiro(matriz2);

    t1.compara(t2);
    System.out.println();
    
    int numRodadas = 0;

    do {

      t1.exibeTabuleiro();
      System.out.println();

      System.out.println("Movimentos v�lidos: "
          + t1.listaMovimentosValidos());

      numRodadas++;

      System.out.println(" ===> "+numRodadas+" - (8=Up;2=Down;4=Left;6=Right): ");

      Scanner sc = new Scanner(System.in);
      int i = sc.nextInt();

      if (i == -1) {
        break;
      }

      boolean b = false;

      switch (i) {
        case 8:
          b = t1.executaMovimento(Operation.UP);
          break;
        case 2:
          b = t1.executaMovimento(Operation.DOWN);
          break;
        case 4:
          b = t1.executaMovimento(Operation.LEFT);
          break;
        case 6:
          b = t1.executaMovimento(Operation.RIGHT);
          break;
      }

      if (b) {
        System.out.println("Movimento realizado com sucesso");
      } else {
        System.out.println("Movimento n�o realizado");
      }

      //t.exibeTabuleiro();
      //System.out.println();
      
      if (t1.equals(t2)) {
        System.out.println("\nParab�ns !!! Voc� resolveu em "+numRodadas+" movimenta��es");
        break;
      }

    } while (true);

    System.out.println("fim!");

    System.out.println("N�mero de rodadas: " + numRodadas);

  }

 
}
