package br.com.mltech.modelo;

import java.io.File;

import android.graphics.Bitmap;
import android.util.Log;
import br.com.mltech.utils.ManipulaImagem;

/**
 * Moldura
 * 
 * @author maurocl
 * 
 */
public class Moldura extends Foto {
  
  public static final String TAG = "Moldura";

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
   *          altura
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

  /**
   * leArquivoMoldura(String arquivoMoldura)
   * 
   * Carrega arquivo de moldura
   * 
   * @param arquivoMoldura
   *          Nome do arquivo
   * 
   * @return um bitmap com o arquivo contendo a moldura ou null no caso de algum
   *         problema
   * 
   */
  public Bitmap leArquivoMoldura(String arquivoMoldura) {

    //Log.d(TAG, "leArquivoMoldura() - arquivoMoldura: [" + arquivoMoldura + "].");

    File moldura = new File(arquivoMoldura);

    if ((moldura != null) && (!moldura.exists())) {
      //Log.w(TAG, "leArquivoMoldura() - arquivoMoldura: [" + arquivoMoldura + "] não existe.");
      return null;
    }

    // lê o bitmap contendo a moldura
    Bitmap bmMoldura = ManipulaImagem.getBitmapFromFile(moldura);

    if (bmMoldura == null) {

      Log.w(TAG, "leArquivoMoldura() - arquivo contento a moldura está vazio.");
      return null;

    } else {
      Log.v(TAG, "leArquivoMoldura() - largura x altura da moldura: " + ManipulaImagem.getStringBitmapSize(bmMoldura));

      return bmMoldura;

    }

  }

}
