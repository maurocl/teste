
package br.com.mltech.modelo;

import java.io.Serializable;

/**
 * Gasto.java
 * 
 * @author maurocl
 * 
 */
public class Gasto implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private Long id;

  /**
   * Data do gasto
   */
  private String data;

  /**
   * Descrição do gasto
   */
  private String descricao;

  /**
   * Valor do gasto
   */
  private double valor;

  /**
   * Categoria a qual pertence o gasto
   */
  private Categoria categoria;
  
  /**
   * Usuario do Gasto
   */
  private Usuario usuario;

  /**
	 * 
	 */
  public Gasto() {

    super();
  }

  /**
   * 
   * @param id
   * @param data
   * @param descricao
   * @param valor
   * @param categoria
   */
  public Gasto(Long id, String data, String descricao, double valor, Categoria categoria) {

    super();
    
    this.id = id;
    this.data = data;
    this.descricao = descricao;
    this.valor = valor;
    this.categoria = categoria;
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

  @Override
  public String toString() {

    return "Gasto [id=" + id + ", data=" + data + ", descricao=" + descricao + ", valor=" + valor + ", categoria=" + categoria
        + ", usuario=" + usuario + "]";
  }

  /**
	 * 
	 */

}
