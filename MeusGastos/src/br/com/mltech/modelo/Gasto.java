package br.com.mltech.modelo;

public class Gasto {
	
	private Long codGasto;
	private String data;
	private String descricao;
	private double valor;
	private Categoria categoria;
	
	public Gasto(Long codGasto, String data, String descricao, double valor,
			Categoria categoria) {
		super();
		this.codGasto = codGasto;
		this.data = data;
		this.descricao = descricao;
		this.valor = valor;
		this.categoria = categoria;
	}
	
	public Long getCodGasto() {
		return codGasto;
	}
	public void setCodGasto(Long codGasto) {
		this.codGasto = codGasto;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	@Override
	public String toString() {
		return "Gasto [codGasto=" + codGasto + ", data=" + data
				+ ", descricao=" + descricao + ", valor=" + valor
				+ ", categoria=" + categoria + "]";
	}	

}
