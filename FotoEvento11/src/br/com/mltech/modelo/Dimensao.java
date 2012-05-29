package br.com.mltech.modelo;

public class Dimensao {

  private int largura;
  private int altura;

  /**
   * Dimensao()
   */
  public Dimensao() {
    this(0, 0);
  }

  /**
   * Dimensao(int w, int h)
   * 
   * @param w
   *          largura
   * @param h
   *          altura
   * 
   */
  public Dimensao(int w, int h) {
    largura = w;
    altura = h;
  }

  /**
   * aspectRatio()
   * 
   * @return a relação entre largura e altura
   * 
   */
  public double aspectRatio() {
    if (altura != 0) {
      return largura / altura;
    }
    return -1;
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
   * 
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
   * @param altura
   */
  public void setAltura(int altura) {
    this.altura = altura;
  }

  /**
   * toString()
   */
  @Override
  public String toString() {
    return "Dimensao [largura=" + largura + ", altura=" + altura + "]";
  }

  /**
   * getMaiorLargura(Dimensao dim1, Dimensao dim2)
   * 
   * @param dim1
   * @param dim2
   * 
   * @return
   */
  public static int getMaiorLargura(Dimensao dim1, Dimensao dim2) {

    int maior = 0;

    if (dim2 == null) {
      // TODO lançar uma exceção
      return -1;
    }

    if (dim1.largura > dim2.largura) {
      maior = dim1.largura;
    }

    return maior;

  }

  /**
   * getMaiorLarguraAltura(Dimensao dim2)
   * 
   * @param dim2
   * @return
   */
  public static Dimensao getMaiorLarguraAltura(Dimensao dim1, Dimensao dim2) {
    
    if ( (dim1==null) || (dim2==null) ) {
      return null;
    }
    
    int maiorLargura = (dim1.largura > dim2.largura) ? dim1.largura : dim2.largura;
    int maiorAltura = (dim1.altura > dim2.altura) ? dim2.altura : dim2.altura;

    Dimensao d = new Dimensao(maiorLargura, maiorAltura);

    return d;
  }

}
