package br.com.mltech.modelo;

/**
 * Moldura
 * 
 * @author maurocl
 * 
 */
public class Moldura extends Foto {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 3479924442144647933L;

  // largura da moldura
  private int largura;

  // altura da moldura
  private int altura;

  // descrição da moldura
  private String descricao;

  /**
   * Moldura(String arquivo)
   * 
   * @param arquivo
   */
  public Moldura(String arquivo) {
    super(arquivo);
    
  }

  /**
   * getLargura()
   * 
   * @return
   */
  public int getLargura() {
    return largura;
  }

  /**
   * setLargura(int largura)
   * @param largura
   */
  public void setLargura(int largura) {
    this.largura = largura;
  }

  /**
   * getAltura()
   * 
   * @return
   */
  public int getAltura() {
    return altura;
  }

  /**
   * setAltura(int altura)
   * 
   * @param altura altura 
   * 
   */
  public void setAltura(int altura) {
    this.altura = altura;
  }

  /**
   * getDescricao()
   * 
   * @return descrição da moldura
   * 
   */
  public String getDescricao() {
    return descricao;
  }

  /**
   * setDescricao(String descricao)
   * 
   * @param descricao
   * 
   */
  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  /**
	 * toString()
	 */
  @Override
  public String toString() {
    return "Moldura [largura=" + largura + ", altura=" + altura + ", descricao=" + descricao + "]";
  }

}
