
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
 * Essa activity � respons�vel por obter as configura��es poss�veis de uma
 * c�mera de v�deo
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

    boolean isCameraAvailable = CameraTools.checkCameraHardware(this);

    if (!isCameraAvailable) {
      Log.w(TAG, "hardware n�o est� disponvel");
      return;
    }

    EditText edit = (EditText) findViewById(R.id.editText1);

    CameraTools.checkCameraFeatures(this);

    Log.i(TAG, "N� de c�meras: " + CameraTools.getNumCameras());

    for (int i = 0; i < CameraTools.getNumCameras(); i++) {

      Camera c = CameraTools.getCameraInstance(i);

      Log.i(TAG, "==> ");
      Log.i(TAG, "==> Processando c�mera: " + i);
      Log.i(TAG, "==> ");

      HashMap<String, List<String>> hash = CameraTools.getParametersDetail(c);

      edit.append("\n");
      edit.append("==> C�mera: " + i);
      edit.append("\n");

      exibeParametrosCamera(edit, hash);

      exibeGravaParametrosCamera(edit, hash);

      if (c != null) {
        // Disconnects and releases the Camera object resources.
        c.release();
      }

    }

    //
    obtemAtributoCameras();

  }


  private void obtemAtributoCameras() {

    if (CameraTools.getNumCameras() < 2) {
      return;
    }

    Log.i(TAG, "N� de c�meras: " + CameraTools.getNumCameras());

    Camera c = null;

    /*
    HashMap[] x = new HashMap[2];

    for (int i = 0; i < CameraTools.getNumCameras(); i++) {
      
      c = CameraTools.getCameraInstance(i);

      x[i] = CameraTools.getParametersDetail(c);

    }
    */

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

    // Compara os atributos de duas c�meras
    comparaAtributosCamera(hash0, hash1);

  }
  
  
  /**
   * exibeParametrosCamera(EditText edit, HashMap<String, List<String>> hash)
   * 
   * L� todos os par�metros de configura��o da c�mera e exibe em um campo de
   * texto
   * 
   * @param edit
   *          componente onde as informa��es ser�o exibidas
   * @param hash
   *          HashMap contendo a lista de par�metros e seus respectivos valores
   *          v�lidos
   */
  private void exibeParametrosCamera(EditText edit, HashMap<String, List<String>> hash) {

    int num = 0;

    for (String chave : hash.keySet()) {

      num++;

      List<String> listaValues = hash.get(chave);

      Log.d(TAG, "\nchave(" + num + ")=" + chave);
      edit.append("\nchave(" + num + ")=" + chave + "\n");

      if (listaValues != null) {

        for (String s2 : listaValues) {
          Log.d(TAG, "  value=" + s2);
          edit.append("  value=" + s2 + "\n");
        }

      }

    }
  }

  /**
   * L� todos os par�metros de configura��o da c�mera e exibe em um campo de
   * texto e grava o resultado em um arquivo texto.
   * 
   * @param edit
   *          componente onde as informa��es ser�o exibidas
   * 
   * @param hash
   *          HashMap contendo a lista de par�metros e seus respectivos valores
   *          v�lidos
   */
  private void exibeGravaParametrosCamera(EditText edit, HashMap<String, List<String>> hash) {

    if (hash == null) {
      Log.w(TAG, "lista de par�metros est� vazia");
      return;
    }

    // percorre a lista de chaves
    for (String chave : hash.keySet()) {

      StringBuilder sb = new StringBuilder();

      sb.append(chave).append(";");

      // adiciona a informa��o
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
   * Grava uma string em um arquivo.<br>
   * O arquivo ser� armazenado no diret�rio dado por:
   * Environment.getExternalStorageDirectory()
   * 
   * @param s
   *          String
   * 
   * @param filename
   *          nome do arquivo
   */
  private void gravaString(String s, String filename) {

    File dirs = Environment.getExternalStorageDirectory();

    // cria o arquivo no diret�rio especificado
    File f = new File(dirs, filename);

    if (dirs.mkdirs()) {

      Log.d(TAG, "diret�rio " + dirs.getAbsolutePath() + " foi criado");

    } else {

      if (dirs.exists()) {
        Log.d(TAG, "diret�rio " + dirs.getAbsolutePath() + " N�O foi criado pois j� existe");
      } else {
        Log.w(TAG, "diret�rio N�O foi criado");
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

      // exibe o n� de caracteres gravados
      Log.d(TAG, s.getBytes().length + " caracteres gravados");

      Log.d(TAG, "Arquivo: " + f.getAbsolutePath() + " gravado com sucesso");

      Toast.makeText(this, "Arquivo: " + f.getAbsolutePath() + " gravado com sucesso", Toast.LENGTH_SHORT).show();

    } catch (FileNotFoundException e) {
      Log.w(TAG, "Arquivo: " + f.getAbsolutePath() + " n�o foi encontrado", e);

    } catch (IOException e) {
      Log.w(TAG, "Erro na grava��o do arquivo: " + f.getAbsolutePath(), e);
    }

  }

  /**
   * Compara as propriedades das c�meras 0 e 1 e exibe seus valores e
   * diferen�as.
   * 
   * @param hash0
   *          Propriedades da c�mera 0
   *          
   * @param hash1
   *          Propriedades da c�mera 1
   * 
   */
  private void comparaAtributosCamera(HashMap<String, List<String>> hash0, HashMap<String, List<String>> hash1) {

    if ((hash0 == null) || (hash1 == null)) {
      Log.w(TAG, "lista de par�metros est� vazia");
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

    // Exibe o n� de elementos do cojunto
    Log.d(TAG, "Num elem set: " + set.size());

    int i = 0;

    for (String chave : set) {

      Log.d(TAG, i + " - Chave: " + chave);

      // exibe os valores associados as chaves da c�mera 0
      if (hash0.containsKey(chave)) {
        Log.d(TAG, "  0: " + hash0.get(chave));
      }
      else {
        Log.w(TAG, "  1: " + chave + " inexistente");
      }

      // exibe os valores associados as chaves da c�mera 1      
      if (hash1.containsKey(chave)) {
        Log.d(TAG, "  1: " + hash1.get(chave));
      } else {
        Log.w(TAG, "  1: " + chave + " inexistente");
      }

      i++;

    }

  }

}