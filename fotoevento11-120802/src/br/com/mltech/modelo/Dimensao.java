package br.com.mltech.modelo;

/**
 * 
 * @author maurocl
 *
 */
public class Dimensao {
 
  /**
   * largura da dimensão
   */
  private int largura;
   
  /**
   * altura da dimensão
   */
  private int altura;

  /**
   * Construtor de dimensão nula.
   * 
   */
  public Dimensao() {
    this(0, 0);
  }

  /**
   * Constroi uma dimensão dada sua laegura e altura
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
   * Calcula o aspect ratio da dimensão
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
   * Obtém a largura da dimensão
   * 
   * @return
   */
  public int getLargura() {
    return largura;
  }

  /**
   * Atribui a largura da dimensão
   * 
   * @param largura
   */
  public void setLargura(int largura) {
    this.largura = largura;
  }

  /**
   * Obtém a altura da dimensão
   * 
   * @return
   */
  public int getAltura() {
    return altura;
  }

  /**
   * Obtém a altura da dimensão
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
   * Obtem a maior largura dado duas dimensões
   * 
   * @param dim1 Instância de Dimensão
   * @param dim2 Instância de Dimensão
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
   * Obtem uma nova dimensão a partir da maior altura e largura das dimensões fornecidas.
   * 
   * @param dim1
   * @param dim2
   * 
   * @return Uma nova dimensão formada pela maior altura e largura entre duas dimensões
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
