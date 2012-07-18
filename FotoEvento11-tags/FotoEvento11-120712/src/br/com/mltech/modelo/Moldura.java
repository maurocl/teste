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

  // descrição da moldura
  private String descricao;

  /**
   * Moldura(String arquivo)
   * 
   * @param arquivo nome do arquivo onde onde está o bitmap da moldura
   */
  public Moldura(String arquivo) {
    super(arquivo);

  }

  /**
   * Moldura(String arquivo, String descricao)
   * 
   * Cria uma moldura com uma descrição
   * 
   * @param arquivo nome do arquivo onde onde está o bitmap da moldura
   * @param descricao texto com a descrição da moldura
   * 
   */
  public Moldura(String arquivo, String descricao) {
    super(arquivo);
    this.descricao = descricao;

  }

  /**
   * Moldura(String arquivo, Bitmap imagem) 
   * 
   * @param arquivo nome do arquivo onde onde está o bitmap da moldura
   * @param imagem bitmap da moldura
   * 
   */
  public Moldura(String arquivo, Bitmap imagem) {
    super(arquivo);
    this.setImagem(imagem);
    this.descricao = null;
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
    return "Moldura [largura=" + this.getDimensao().getLargura() + ", altura=" + this.getDimensao().getAltura() + ", descricao="
        + descricao + "]";
  }

  /**
   * leArquivoMoldura(String arquivoMoldura)
   * 
   * Lê um arquivo que possui um bitmap com uma moldura.
   * 
   * @param arquivoMoldura
   *          Nome do arquivo contendo o bitmap
   * 
   * @return um bitmap com o arquivo contendo a moldura ou null no caso de algum
   *         problema
   * 
   */
  public Bitmap leArquivoMoldura(String arquivoMoldura) {

    File fileMoldura = new File(arquivoMoldura);

    if ((fileMoldura != null) && (!fileMoldura.exists())) {
      Log.w(TAG, "leArquivoMoldura() - arquivoMoldura: [" + arquivoMoldura + "] não existe.");
      return null;
    }

    // lê o bitmap contendo a moldura
    Bitmap bmMoldura = ManipulaImagem.getBitmapFromFile(fileMoldura);

    if (bmMoldura == null) {

      Log.w(TAG, "leArquivoMoldura() - arquivo "+arquivoMoldura+" não pode ser lido.");

      // garante que o bitmap associado a moldura está vazio
      this.setImagem(null);
      
      return null;

    } else {
      Log.v(TAG, "leArquivoMoldura() - largura x altura da moldura: " + ManipulaImagem.getStringBitmapSize(bmMoldura));

      // atualiza o bitmap associado a moldura
      this.setImagem(bmMoldura);

      // retorna o bitmap associado a moldura
      return bmMoldura;

    }

  }

}
