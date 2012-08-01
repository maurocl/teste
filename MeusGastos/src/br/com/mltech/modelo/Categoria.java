
package br.com.mltech.modelo;

import java.io.Serializable;

/**
 * Categoria
 * 
 * @author maurocl
 * 
 */
public class Categoria implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private Long id;

  private String descricao;

  /**
   * Construtor vazio
   */
  public Categoria() {

    super();

    this.id = -1L;
    this.descricao = null;

  }

  /**
   * @param id
   * @param descricao
   */
  public Categoria(Long id, String descricao) {

    super();
    this.id = id;
    this.descricao = descricao;
  }

  /**
   * Cria uma nova categoria dada sua descrição
   * 
   * @param descricao
   *          Descrição da categoria
   * 
   */
  public Categoria(String descricao) {

    super();
    this.descricao = descricao;

  }

  /**
   * 
   * @return
   */
  public Long getId() {

    return id;
  }

  /**
   * 
   * @param id
   */
  public void setId(Long id) {

    this.id = id;
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

  @Override
  public String toString() {

    return "Categoria [id=" + id + ", descricao=" + descricao + "]";
  }

  
  
}
