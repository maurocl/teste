package br.com.mltech.modelo;

public class Dimensao {

	private int largura;
	private int altura;

	/**
	 * 
	 * @return
	 */
	public double aspectRatio() {
		if (altura != 0) {
			return largura / altura;
		}
		return -1;
	}

	public int getLargura() {
		return largura;
	}

	public void setLargura(int largura) {
		this.largura = largura;
	}

	public int getAltura() {
		return altura;
	}

	public void setAltura(int altura) {
		this.altura = altura;
	}

	@Override
	public String toString() {
		return "Dimensao [largura=" + largura + ", altura=" + altura + "]";
	}

	
	
}
