package br.com.mltech.modelo;

/**
 * Gasto.java
 * 
 * @author maurocl
 *
 */
public class Gasto {

	private Long codGasto;
	private String data;
	private String descricao;
	private double valor;
	private Categoria categoria;

	/**
	 * 
	 */
	public Gasto() {
		super();
	}
	
	/**
	 * 
	 * @param codGasto
	 * @param data
	 * @param descricao
	 * @param valor
	 * @param categoria
	 */
	public Gasto(Long codGasto, String data, String descricao, double valor, Categoria categoria) {
		super();
		this.codGasto = codGasto;
		this.data = data;
		this.descricao = descricao;
		this.valor = valor;
		this.categoria = categoria;
	}

	/**
	 * 
	 * @return
	 */
	public Long getCodGasto() {
		return codGasto;
	}

	/**
	 * 
	 * @param codGasto
	 */
	public void setCodGasto(Long codGasto) {
		this.codGasto = codGasto;
	}

	/**
	 * 
	 * @return
	 */
	public String getData() {
		return data;
	}

	/**
	 * 
	 * @param data
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * 
	 * @param descricao
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * 
	 * @return
	 */
	public double getValor() {
		return valor;
	}

	/**
	 * 
	 * @param valor
	 */
	public void setValor(double valor) {
		this.valor = valor;
	}

	/**
	 * 
	 * @return
	 */
	public Categoria getCategoria() {
		return categoria;
	}

	/**
	 * 
	 * @param categoria
	 */
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		//return "Gasto [codGasto=" + codGasto + ", data=" + data + ", descricao=" + descricao + ", valor=" + valor + ", categoria="+ categoria + "]";
		return codGasto + " - " + data + " - " + descricao + " - " + valor;
	}

}
