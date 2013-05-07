package br.com.mltech;

/**
 * Representa uma posi��o em um tabuleiro
 * 
 * 
 *
 */
public class Posicao {

	private int y, x;
	private int value;

	/**
	 * Cria uma nova posi��o
	 * 
	 * @param y linha  (0 <= y <= 2)
	 * @param x coluna (0 <= x <= 2)
	 * @param value Valor associado a posi��o
	 * 
	 */
	public Posicao(int y, int x, int value) {
		super();
		this.setY(y);
		this.setX(x);
		this.value = value;
	}

	/**
	 * Retorna a posi��o y
	 * 
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 * Altera a posi��o y
	 * 
	 * @param y
	 */
	public void setY(int y) {
		if (y >= 0 && y <= 2) {
			this.y = y;
		}
	}

	/**
	 * Obtem a posi��o x
	 * 
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * Altera a posi��o x 
	 * 
	 * @param x 
	 * 
	 */
	public void setX(int x) {
		if (x >= 0 && x <= 2) {
			this.x = x;
		}
	}

	/**
	 * Retorna o valor associado a posi��o
	 * 
	 * @return Retorna o valor associado a posi��o
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Altera o valor associado a posi��o
	 * 
	 * @param value Valor associado a posi��o
	 * 
	 */
	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Posicao [y=" + y + ", x=" + x + ", value=" + value + "]";
		
		
		
	}

	@Override
	public int hashCode() {
		
		final int prime = 31;
		
		int result = 1;
		
		result = prime * result + value;
		result = prime * result + x;
		result = prime * result + y;
		
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
		
		Posicao other = (Posicao) obj;
		
		if (value != other.value)
			return false;
		
		if (x != other.x)
			return false;
		
		if (y != other.y)
			return false;
		
		return true;
		
	}
		
}
