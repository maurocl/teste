package br.com.mltech;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * 
 *
 */
public class Tabuleiro {

	// nº máximo de linhas do tabuleiro
	private static int MAX_LIN = 3;

	// nº máximo de colunas do tabuleiro
	private static int MAX_COL = 3;

	// matriz 3x3 de posições
	private Posicao[][] posicoes = new Posicao[3][3];

	// posição do elemento em branco (posição atualmente vazia)
	private int y, x;

	/**
	 * Método construtor - constroi um tabuleiro a partir de uma matriz 3x3. O
	 * elemento não preenchido é representado pelo nº 0.
	 * 
	 * @param matriz Matriz 3x3 de inteiros
	 * 
	 */
	public Tabuleiro(int[][] matriz) {

		super();

		for (int i = 0; i < MAX_LIN; i++) {

			for (int j = 0; j < MAX_COL; j++) {

				if (matriz[i][j] == 0) {
					// atualiza a posição atualmente em branco
					this.y = i;
					this.x = j;
				}

				posicoes[i][j] = new Posicao(i, j, matriz[i][j]);

			}

		}

	}

	/**
	 * Altera um posição no tabuleiro
	 * 
	 * @param y
	 *            Linha
	 * @param x
	 *            Coluna
	 * 
	 * @param num
	 *            Valor associado a posição
	 * 
	 */
	//public void setPosicao(Posicao p) {
	//	posicoes[p.getY()][p.getX()] = p;
	//}

	/**
	 * Altera um posição no tabuleiro
	 * 
	 * @param y
	 * @param x
	 * @param p
	 * 
	 */
	public void setPosicao(int y, int x, Posicao p) {
		if (p != null) {
			posicoes[y][x] = p;
		}
	}

	/**
	 * Obtém uma posição do tabuleiro
	 * 
	 * @param y
	 *            Linha
	 * @param x
	 *            Coluna
	 * 
	 * @return a Posição
	 */
	public Posicao getPosicao(int y, int x) {
		return posicoes[y][x];
	}

	/**
	 * Obtem a posição atualmente vazia
	 * 
	 * @return uma instância de posição
	 * 
	 */
	public Posicao getPosicaoVazia() {
		return posicoes[y][x];
	}

	
	/**
	 * Exibe o tabuleiro
	 * 
	 */
	public void exibeTabuleiro() {

		for (int i = 0; i < MAX_LIN; i++) {

			for (int j = 0; j < MAX_COL; j++) {

				if (posicoes[i][j].getValue() != 0) {
					System.out.print(posicoes[i][j].getValue() + "\t");
				} else {
					System.out.print("\t");
				}

			}

			System.out.println();

		}

	}

	
	/**
	 * Lista todos os movimentos válidos
	 * 
	 * @return Uma lista com os movimentos válidos
	 * 
	 */
	public List<Operation> listaMovimentosValidos() {

		Posicao p = getPosicaoVazia();

		List<Operation> operacoes = new ArrayList<Operation>();

		// posso mover up
		if (p.getY() - 1 >= 0) {
			System.out.println("Movimento up é válido");
			operacoes.add(Operation.UP);
		}

		// posso mover down
		if (p.getY() + 1 < MAX_LIN) {
			System.out.println("Movimento down é válido");
			operacoes.add(Operation.DOWN);
		}

		// posso mover left
		if (p.getX() - 1 >= 0) {
			System.out.println("Movimento left é válido");
			operacoes.add(Operation.LEFT);
		}

		// posso mover right
		if (p.getX() + 1 < MAX_COL) {
			System.out.println("Movimento right é válido");
			operacoes.add(Operation.RIGHT);
		}

		return operacoes;
	}

	/**
	 * Verifica se um movimento é válido
	 * 
	 * Dada a posição "em aberto" ...
	 * 
	 * @param operacao
	 * 
	 * @return
	 */
	public boolean isMovimentoValido(Operation operacao) {

		Posicao p = getPosicaoVazia();

		// posso mover up
		if (operacao == Operation.UP) {
			if (p.getY() - 1 >= 0) {
				System.out.println("Movimento up é válido");
				return true;
			} else {
				System.out.println("Movimento up é inválido");
				return false;
			}

		}

		if (operacao == Operation.DOWN) {
			// posso mover down
			if (p.getY() + 1 < MAX_LIN) {
				System.out.println("Movimento down é válido");
				return true;
			} else {
				System.out.println("Movimento down é inválido");
				return false;
			}

		}

		if (operacao == Operation.LEFT) {
			// posso mover left
			if (p.getX() - 1 >= 0) {
				System.out.println("Movimento left é válido");
				return true;
			} else {
				System.out.println("Movimento left é inválido");
				return false;
			}

		}

		if (operacao == Operation.RIGHT) {
			// posso mover right
			if (p.getX() + 1 < MAX_COL) {
				System.out.println("Movimento right é válido");
				return true;
			} else {
				System.out.println("Movimento right é inválido");
				return false;
			}

		}

		return false;
	}

	/**
	 * Executa um movimento, isto é, uma troca da posição vazia com a posição solicitada.
	 * 
	 * @param operacao
	 * 
	 * @return
	 * 
	 */
	public boolean executaMovimento(Operation operacao) {

		if (isMovimentoValido(operacao)) {

		  // Guarda a posição atual do espaço em branco
			int y2 = y;
			int x2 = x;

			
			// guarda o valor da posição destino

			// Calcula a posição destino
			if (operacao == Operation.UP) {
				y2--;
			} else if (operacao == Operation.DOWN) {
				y2++;
			} else if (operacao == Operation.LEFT) {
				x2--;
			} else if (operacao == Operation.RIGHT) {
				x2++;
			}

			// posição corrente
			Posicao posicaoOrigem = this.getPosicao(y, x);

			// posição destino
			Posicao posicaoDestino = this.getPosicao(y2, x2);

   		// troca duas posições
			//troca(posicaoOrigem, posicaoDestino);
			swap(posicaoOrigem, posicaoDestino);

			// atualiza a posição do espaço em branco
			this.x = x2;
			this.y = y2;

			return true;

		} else {

			return false;

		}

	}

	/**
	 * Troca a posição origem e a posição destino
	 * 
	 * @param posicaoOrigem
	 *            Posição origem
	 * @param posicaoDestino
	 *            Posição destino
	 * 
	 */
	private void troca(Posicao posicaoOrigem, Posicao posicaoDestino) {

	  int tempValue;
		  
		//Posicao temp;

		//temp = posicaoOrigem;
		tempValue = posicaoOrigem.getValue();

		posicaoOrigem = posicaoDestino;
		posicaoOrigem.setValue(posicaoDestino.getValue());

		//posicaoDestino = temp;
		posicaoDestino.setValue(tempValue);

		//this.posicoes[posicaoOrigem.getY()][posicaoOrigem.getX()] = posicaoDestino;

		//this.posicoes[posicaoDestino.getY()][posicaoDestino.getX()] = posicaoOrigem;

	}

	private void swap(Posicao posicaoOrigem, Posicao posicaoDestino) {

    int tempValue;

    tempValue = posicaoOrigem.getValue();

    posicaoOrigem.setValue(posicaoDestino.getValue());

    posicaoDestino.setValue(tempValue);

  }
	
	
	/**
	 * 
	 * @param y
	 * @param x
	 */
	private void setYX(int y, int x) {
		this.y = y;
		this.x = x;
	}

	/**
	 * 
	 * @return
	 */
	public Posicao[][] getPosicoes() {
		return posicoes;
	}

	/**
	 * 
	 * @param posicoes
	 */
	public void setPosicoes(Posicao[][] posicoes) {
		this.posicoes = posicoes;
	}

	/**
	 * 
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 * 
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * 
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * 
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	void compara(Tabuleiro t2) {
	  
	  int num=0;
	  
	  for (int i = 0; i < MAX_LIN; i++) {

      for (int j = 0; j < MAX_COL; j++) {

        if(this.posicoes[i][j].equals(t2.posicoes[i][j])) {
          
        }
        else {
          num++;
          System.out.println("Posicao "+i+", "+j+ " possuem valores diferentes: "+this.posicoes[i][j].getValue()+", "+t2.posicoes[i][j].getValue()+", num="+num);
        }
        

      }

    }	  
	}

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(posicoes);
    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Tabuleiro other = (Tabuleiro) obj;
    if (!Arrays.equals(posicoes, other.posicoes))
      return false;
    return true;
  }
	
	
	
}
