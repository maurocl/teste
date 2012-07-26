package br.com.mltech.modelo;

/**
 * 
 * @author maurocl
 *
 */
public class Dimensao {
 
  /**
   * largura da dimens�o
   */
  private int largura;
   
  /**
   * altura da dimens�o
   */
  private int altura;

  /**
   * Construtor de dimens�o nula.
   * 
   */
  public Dimensao() {
    this(0, 0);
  }

  /**
   * Constroi uma dimens�o dada sua laegura e altura
   * 
   * @param largura
   *          largura
   * @param altura
   *          altura
   * 
   */
  public Dimensao(int largura, int altura) {
    this.largura = largura;
    this.altura = altura;
  }

  /**
   * Calcula o aspect ratio da dimens�o
   * 
   * @return a rela��o entre largura e altura
   * 
   */
  public double aspectRatio() {
    
    if (altura != 0) {
      return largura / altura;
    }
    
    return -1;
    
  }

  /**
   * Obt�m a largura da dimens�o
   * 
   * @return
   */
  public int getLargura() {
    return largura;
  }

  /**
   * Atribui a largura da dimens�o
   * 
   * @param largura
   */
  public void setLargura(int largura) {
    this.largura = largura;
  }

  /**
   * Obt�m a altura da dimens�o
   * 
   * @return
   */
  public int getAltura() {
    return altura;
  }

  /**
   * Obt�m a altura da dimens�o
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
   * Obtem a maior largura dado duas dimens�es
   * 
   * @param dim1 Inst�ncia de Dimens�o
   * @param dim2 Inst�ncia de Dimens�o
   * 
   * @return
   */
  public static int getMaiorLargura(Dimensao dim1, Dimensao dim2) {

    int maior = 0;

    if (dim2 == null) {
      // TODO lan�ar uma exce��o
      return -1;
    }

    if (dim1.largura > dim2.largura) {
      maior = dim1.largura;
    }

    return maior;

  }

  /**
   * Obtem uma nova dimens�o a partir da maior altura e largura das dimens�es fornecidas.
   * 
   * @param dim1
   * @param dim2
   * 
   * @return Uma nova dimens�o formada pela maior altura e largura entre duas dimens�es
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
