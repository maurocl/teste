package br.com.mltech.modelo;

import java.io.Serializable;

/**
 * Atributo
 * 
 * @author maurocl
 *
 */
public class Atributo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2386538206919855202L;
  
  private String nome;
  private String tipo;
  private String valor;
  private boolean obrigatorio;

  /**
   * Atributo
   * 
   * @param nome Nome do atributo
   * @param tipo Tipo do atributo
   * @param valor Valor
   * @param obrigatorio true/false
   */
  public Atributo(String nome, String tipo, String valor, boolean obrigatorio) {
    super();
    this.nome = nome;
    this.tipo = tipo;
    this.valor = valor;
    this.obrigatorio = obrigatorio;
  }

  /**
   * 
   * @return
   */
  public String getNome() {
    return nome;
  }

  /**
   * 
   * @param nome
   */
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * 
   * @return
   */
  public String getTipo() {
    return tipo;
  }

  /**
   * 
   * @param tipo
   */
  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  /**
   * 
   * @return
   */
  public String getValor() {
    return valor;
  }

  /**
   * 
   * @param valor
   */
  public void setValor(String valor) {
    this.valor = valor;
  }

  /**
   * 
   * @return
   */
  public boolean isObrigatorio() {
    return obrigatorio;
  }

  /**
   * 
   * @param obrigatorio
   */
  public void setObrigatorio(boolean obrigatorio) {
    this.obrigatorio = obrigatorio;
  }

  @Override
  public String toString() {
    return "Atributo [nome=" + nome + ", tipo=" + tipo + ", valor=" + valor + ", obrigatorio=" + obrigatorio + "]";
  }

}
