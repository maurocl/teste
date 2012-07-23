package br.com.mltech.modelo;

/**
 * Categoria
 * 
 * @author maurocl
 *
 */
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
	 * Obtém o código da categoria
	 * 
	 * @return
	 */
	public Long getCodCategoria() {
		return codCategoria;
	}

	/**
	 * Atualiza o código da categoria
	 * 
	 * @param codCategoria
	 */
	public void setCodCategoria(Long codCategoria) {
		this.codCategoria = codCategoria;
	}

	/**
	 * Obtém a descrição da categoria
	 * 
	 * @return
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * Atualiza a descrição da Categoria
	 * 
	 * @param descricao
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Retorna a código da categoria e sua descrição
	 */
	public String toString() {
		return codCategoria + "-" + descricao;
	}

}
