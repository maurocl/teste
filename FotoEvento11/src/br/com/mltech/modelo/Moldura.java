package br.com.mltech.modelo;

/**
 * Moldura
 * 
 * @author maurocl
 * 
 */
public class Moldura extends Foto {

  // largura da moldura
  private int largura;

  // altura da moldura
  private int altura;

  private String descricao;

  /**
   * 
   * @param arquivo
   */
  public Moldura(String arquivo) {
    super(arquivo);
    // TODO Auto-generated constructor stub
  }

  /**
   * getLargura()
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
   * @return
   */
  public int getAltura() {
    return altura;
  }

  /**
   * setAltura(int altura)
   * @param altura
   */
  public void setAltura(int altura) {
    this.altura = altura;
  }

  /**
   * getDescricao()
   * @return
   */
  public String getDescricao() {
    return descricao;
  }

  /**
   * setDescricao(String descricao)
   * @param descricao
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
