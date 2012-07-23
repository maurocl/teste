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
	 * Obt�m o c�digo da categoria
	 * 
	 * @return
	 */
	public Long getCodCategoria() {
		return codCategoria;
	}

	/**
	 * Atualiza o c�digo da categoria
	 * 
	 * @param codCategoria
	 */
	public void setCodCategoria(Long codCategoria) {
		this.codCategoria = codCategoria;
	}

	/**
	 * Obt�m a descri��o da categoria
	 * 
	 * @return
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * Atualiza a descri��o da Categoria
	 * 
	 * @param descricao
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Retorna a c�digo da categoria e sua descri��o
	 */
	public String toString() {
		return codCategoria + "-" + descricao;
	}

}
