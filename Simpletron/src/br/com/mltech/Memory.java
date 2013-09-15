package br.com.mltech;

import java.util.Arrays;


/**
 * 
 * @author maurocl
 *
 */
public class Memory {
  
  public int size;
  
  private int[] pos;

  /**
   * Construtor - Cria uma memória com 100 posições 
   */
  public Memory() {
    this(100);    
  }

  
  /**
   * Construtor - Cria uma memória com n posições 
   * @param size tamanho da memória
   * 
   */
  public Memory(int size) {

    super();
    this.size = size;
    pos = new int[this.size];
    
  }
  
  /**
   * 
   * @param p
   * 
   * @return
   */
  int read(int p) {
    return pos[p];
  }
  
  /**
   * Escreve um valor na posição de memória
   * 
   * @param p posição de memória
   * @param value valor
   */
  void write(int p, int value) {
    pos[p]=value;
  }

  /**
   * Limpa a memória
   */
  void clear() {
    for(int i=0;i<this.size;i++) {
      pos[i]=0;
    }
  }
  
  @Override
  public String toString() {

    return "Memory [pos=" + Arrays.toString(pos) + "]";
  }


  /**
   * Obtem o tamanho da memória
   * @return
   */
  public int getSize() {
  
    return size;
  }
  
}
