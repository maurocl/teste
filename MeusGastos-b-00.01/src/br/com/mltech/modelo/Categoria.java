package br.com.mltech.modelo;

public class Categoria {

	private Long codCategoria;
	private String descricao;

	/**
	 * Construtor vazio
	 */
	public Categoria() {
		super();
		this.codCategoria = null;
		this.descricao = null;
	}

	
	/**
	 * Categoria(Long codCategoria, String descricao) 
	 * 
	 * @param codCategoria
	 * @param descricao
	 */
	public Categoria(Long codCategoria, String descricao) {
		super();
		this.codCategoria = codCategoria;
		this.descricao = descricao;
	}

	/**
	 * 
	 * @return
	 */
	public Long getCodCategoria() {
		return codCategoria;
	}

	/**
	 * 
	 * @param codCategoria
	 */
	public void setCodCategoria(Long codCategoria) {
		this.codCategoria = codCategoria;
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
	 */
	public String toString() {
		return codCategoria + "-" + descricao;
	}

}
