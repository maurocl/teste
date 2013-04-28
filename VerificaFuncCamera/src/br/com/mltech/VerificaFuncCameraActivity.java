package br.com.mltech;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import br.com.mltech.utils.camera.CameraTools;

/**
 * VerificaFuncCameraActivity
 * 
 * Essa activity é responsável por obter as configurações possíveis de uma
 * câmera de vídeo
 * 
 * @author maurocl
 * 
 */
public class VerificaFuncCameraActivity extends Activity {

  public static final String TAG = "VerificaFuncCameraActivity";

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Log.d(TAG, "*** onCreate() ***");

    // verifica se existe uma câmera disponível
    boolean isCameraAvailable = CameraTools.checkCameraHardware(this);

    if (!isCameraAvailable) {
      Log.w(TAG, "hardware não está disponvel");
      finish();
      return;
    }

    EditText edit = (EditText) findViewById(R.id.editText1);

    CameraTools.checkCameraFeatures(this);

    Log.i(TAG, "Nº de câmeras: " + CameraTools.getNumCameras());

    for (int i = 0; i < CameraTools.getNumCameras(); i++) {

      Camera c = CameraTools.getCameraInstance(i);

      Log.i(TAG, "==> ");
      Log.i(TAG, "==> Processando câmera: " + i);
      Log.i(TAG, "==> ");

      HashMap<String, List<String>> hash = CameraTools.getParametersDetail(c);

      edit.append("\n");
      edit.append("==> Câmera: " + i);
      edit.append("\n");

      exibeParametrosCamera(edit, hash);

      //exibeGravaParametrosCamera(edit, hash);

      if (c != null) {
        // Disconnects and releases the Camera object resources.
        c.release();
      }

    }

    //
    //obtemAtributoCameras();

  }


  /**
   * Obtem os atributos das câmeras
   * 
   */
  private void obtemAtributoCameras() {

    if (CameraTools.getNumCameras() < 2) {
      return;
    }

    // Exibe o nº de câmeras do dispositivo.
    Log.i(TAG, "Nº de câmeras: " + CameraTools.getNumCameras());

    Camera c = null;

    /*
    HashMap[] x = new HashMap[2];

    for (int i = 0; i < CameraTools.getNumCameras(); i++) {
      
      c = CameraTools.getCameraInstance(i);

      x[i] = CameraTools.getParametersDetail(c);

    }
    */

    // obtem uma instância da câmera 0 (traseira)
    c = CameraTools.getCameraInstance(0);

    HashMap<String, List<String>> hash0 = CameraTools.getParametersDetail(c);

    if (c != null) {
      // Disconnects and releases the Camera object resources.
      c.release();
    }

    c = CameraTools.getCameraInstance(1);

    HashMap<String, List<String>> hash1 = CameraTools.getParametersDetail(c);

    if (c != null) {
      // Disconnects and releases the Camera object resources.
      c.release();
    }

    // Compara os atributos de duas câmeras
    comparaAtributosCamera(hash0, hash1);

  }
  
  
  /** 
   * <p>Exibe os parâmetros de configuração da câmera em um componente texto fornecido.
   * 
   * @param edit
   *          componente onde as informações serão exibidas
   * @param hash
   *          HashMap contendo a lista de parâmetros e seus respectivos valores
   *          suportados
   */
  private void exibeParametrosCamera(EditText edit, HashMap<String, List<String>> hash) {

    int numeroDoAtributo = 0;

    // percorre a lista de chaves (atributos)
    for (String chave : hash.keySet()) {

      numeroDoAtributo++;
      
      Log.d(TAG, "\nchave(" + numeroDoAtributo + ")=" + chave);
      edit.append("\nchave(" + numeroDoAtributo + ")=" + chave + "\n");

      // obtem a lista de valores da associados a chave
      List<String> listaDePossiveisValores = hash.get(chave);

      if (listaDePossiveisValores != null) {
        // Obtida a lista de valores suportados pelo atributo
        for (String valor : listaDePossiveisValores) {
          Log.d(TAG,  "  value=" + valor);
          edit.append("  value=" + valor + "\n");
        }

      }

    }
  }

  /**
   * Lê todos os parâmetros de configuração da câmera e exibe em um campo de
   * texto e grava o resultado em um arquivo texto.
   * 
   * @param edit
   *          componente onde as informações serão exibidas
   * 
   * @param hash
   *          HashMap contendo a lista de parâmetros e seus respectivos valores
   *          válidos
   */
  private void exibeGravaParametrosCamera(EditText edit, HashMap<String, List<String>> hash) {

    if (hash == null) {
      Log.w(TAG, "lista de parâmetros está vazia");
      return;
    }

    // percorre a lista de chaves
    for (String chave : hash.keySet()) {

      StringBuilder sb = new StringBuilder();

      sb.append(chave).append(";");

      // adiciona a informação
      edit.append(chave);
      edit.append(";");

      // percorre a lista de valores
      List<String> listaValues = hash.get(chave);

      if (listaValues != null) {

        for (String valor : listaValues) {

          sb.append(valor + ";");
          edit.append(valor + ";");

        }

      }

      // pula uma linha
      edit.append("\n");

      // escreve a linha
      Log.d(TAG, sb.toString());

    }

    String s = edit.getText().toString();

    // grava a string no arquivo
    gravaString(s, "data.txt");

  }

  /**
   * <p>Grava o conteúdo de uma string em um arquivo texto dado. O nome do arquivo será fornecido pelo usuário.<br>
   * O arquivo será armazenado no diretório dado por:
   * Environment.getExternalStorageDirectory()
   * 
   * @param s
   *          String Conteúdo da String
   * 
   * @param filename
   *          nome do arquivo destino relativo ao Environment.getExternalStorageDirectory().
   */
  private void gravaString(String s, String filename) {

  	// obtém o nome do diretório de armazenamento externo
    File dirs = Environment.getExternalStorageDirectory();

    // cria o arquivo no diretório especificado
    File f = new File(dirs, filename);

    if (dirs.mkdirs()) {

      Log.d(TAG, "diretório " + dirs.getAbsolutePath() + " foi criado");

    } else {

      if (dirs.exists()) {
        Log.d(TAG, "diretório " + dirs.getAbsolutePath() + " NÃO foi criado pois já existe");
      } else {
        Log.w(TAG, "diretório NÃO foi criado");
      }

    }

    FileOutputStream stream = null;

    try {

      stream = new FileOutputStream(f);

      DataOutputStream dos = new DataOutputStream(stream);

      // grava o texto no arquivo
      dos.write(s.getBytes());

      // fecha o strean
      dos.close();

      // exibe o nº de caracteres gravados
      Log.d(TAG, s.getBytes().length + " caracteres gravados");

      Log.d(TAG, "Arquivo: " + f.getAbsolutePath() + " gravado com sucesso");

      Toast.makeText(this, "Arquivo: " + f.getAbsolutePath() + " gravado com sucesso", Toast.LENGTH_SHORT).show();

    } catch (FileNotFoundException e) {
      Log.w(TAG, "Arquivo: " + f.getAbsolutePath() + " não foi encontrado", e);

    } catch (IOException e) {
      Log.w(TAG, "Erro na gravação do arquivo: " + f.getAbsolutePath(), e);
    }

  }

  /**
   * Compara as propriedades das câmeras 0 e 1 e exibe seus valores e
   * diferenças.
   * 
   * @param hash0
   *          Propriedades da câmera 0
   *          
   * @param hash1
   *          Propriedades da câmera 1
   * 
   */
  private void comparaAtributosCamera(HashMap<String, List<String>> hash0, HashMap<String, List<String>> hash1) {

  	// TODO arrumar essa rotina que está com problemas !!!
  	
    if ((hash0 == null) || (hash1 == null)) {
      Log.w(TAG, "lista de parâmetros está vazia");
      return;
    }

    // Cria um conjunto para armazenar as propriedades
    Set<String> set = new HashSet<String>();

    // insere as chaves no conjunto
    Log.d(TAG, "Num elem 1: " + hash1.size());
    for (String chave : hash1.keySet()) {
      boolean b = set.add(chave);
      if (!b) {
        Log.v(TAG, "chave: " + chave + " duplicada.");
      }
    }

    // insere as chaves no conjunto
    Log.d(TAG, "Num elem 0: " + hash0.size());
    for (String chave : hash0.keySet()) {
      boolean b = set.add(chave);
      if (!b) {
        //Log.v(TAG, "chave: " + chave + " duplicada.");
      }
      else {
        Log.v(TAG, "chave: " + chave + " inserida com sucesso.");
      }
    }

    // Exibe o nº de elementos do cojunto
    Log.d(TAG, "Num elem set: " + set.size());

    int i = 0;

    for (String chave : set) {

      Log.d(TAG, i + " - Chave: " + chave);

      // exibe os valores associados as chaves da câmera 0
      if (hash0.containsKey(chave)) {
        Log.d(TAG, "  0: " + hash0.get(chave));
      }
      else {
        Log.w(TAG, "  1: " + chave + " inexistente");
      }

      // exibe os valores associados as chaves da câmera 1      
      if (hash1.containsKey(chave)) {
        Log.d(TAG, "  1: " + hash1.get(chave));
      } else {
        Log.w(TAG, "  1: " + chave + " inexistente");
      }

      i++;

    }

  }

}