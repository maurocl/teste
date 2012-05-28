package br.com.mltech.modelo;

public class Moldura extends Foto {

	// largura da moldura
	int largura;
	
	// altura da moldura
	int altura;
	
	String descricao;
	
	public Moldura(String arquivo) {
		super(arquivo);
		// TODO Auto-generated constructor stub
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return "Moldura [largura=" + largura + ", altura=" + altura
				+ ", descricao=" + descricao + "]";
	}
	
	

}
