package br.com.mltech;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import br.com.mltech.modelo.Foto;
import br.com.mltech.utils.ManipulaImagem;

/**
 * CameraSimples
 * 
 * @author maurocl
 * 
 */
public class CameraSimples extends Activity implements OnClickListener {

  public static final String TAG = "CameraSimples";

  public static final int TIRA_FOTO = 111;

  private Button btnConfirma;
  private Button btnLista;
  private ImageView image;
  
  private static File file;
  private static Uri outputFileUri;

  ManipulaImagem mi;

  public static int contador = 0;

  public static int i = 0;
  public int j = 0;
  
  public static int numCreate=0;
  public static int numRestart=0;
  
  public static int numFotosCarregadas=0;
  public static int numFotosTiradas=0;

  public static List<Foto> listaFotos;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    // retorna informações sobre a intent que iniciou essa activity
    Intent intent = getIntent();
    
    if(intent.getExtras()!=null) {
      Log.w(TAG, "A intent que inicou essa activity possui um bundle");
    }
    
    
    Log.d(TAG, "*** onCreate() contador=" + contador + " ***");

    setContentView(R.layout.camerasimples);
    
    contador++;
    numCreate++;
    i++;
    j++;

    mi = new ManipulaImagem();
    if (mi == null) {
      Log.w(TAG, "não foi possível criar uma instância da classe ManipulaImagem.");
      return;
    }
    
    // cria uma lista de fotos
    listaFotos = new ArrayList<Foto>();

    btnConfirma = (Button) findViewById(R.id.btnConfirma);
    btnConfirma.setOnClickListener(this);

    btnLista = (Button) findViewById(R.id.btnLista);
    btnLista.setOnClickListener(this);
    
    image = (ImageView) findViewById(R.id.imageView1);

    showXXX();

  }

  /**
   * onClick(View v)
   * 
   * @param v
   * 
   */
  public void onClick(View v) {

    if (v == btnConfirma) {
      processaBotaoConfirma();
    }
    else if(v==btnLista) {
      processaBotaoLista();
    }

  }

  /**
   * processaBotaoConfirma()
   */
  private void processaBotaoConfirma() {

    Log.d(TAG, "============================");
    Log.d(TAG, "===> processaBotaoConfirma()");
    Log.d(TAG, "============================");

    // nome do arquivo onde a foto será armazenada
    String arquivo = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg";

    // ---------------------------------
    //
    // ---------------------------------
    //
    //executaActivityTiraFoto(arquivo);
    executaActivityTiraFotoDummy(arquivo);

  }

  /**
   * processaBotaoLista
   */
  private void processaBotaoLista() {
    Log.i(TAG,"");
    Log.i(TAG,"----------------------");
    Log.i(TAG,"- processaBotaoLista()");
    Log.i(TAG,"----------------------");
    
    if(listaFotos==null) {
      Log.w(TAG,"processaBotaoLista() - lista está vazia !");
      return;
    }
    
    Log.d(TAG,"Nº de fotos: "+listaFotos.size());
    
    for(Foto foto: listaFotos) {
      if(foto!=null) {
        Log.d(TAG,foto.toString());
      }
    }
    
  }
  
  /**
   * executaActivityTiraFoto()
   */
  private void executaActivityTiraFoto(String arquivo) {

    file = new File(arquivo);

    Log.d(TAG, "executaActivityTiraFoto() - arquivo=" + file.getAbsolutePath());

    outputFileUri = Uri.fromFile(file);

    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

    startActivityForResult(intent, TIRA_FOTO);

  }

  /**
   * executaActivityTiraFotoDummy(String arquivo)
   * 
   * @param arquivo
   */
  private void executaActivityTiraFotoDummy(String arquivo) {

    file = new File(arquivo);

    Log.d(TAG, "executaActivityTiraFoto() - arquivo=" + file.getAbsolutePath());

    outputFileUri = Uri.fromFile(file);

    // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    // intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
    
    Intent intent = new Intent(this, ActivityCameraSimplesDummy.class);
    intent.putExtra("nome", "mauro");
    
    intent.putExtra("x", outputFileUri);

    startActivityForResult(intent, TIRA_FOTO);

  }

  /**
   * onActivityResult(int requestCode, int resultCode, Intent data)
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);
    
    

    Log.d(TAG,"-----------------------------------------------------------------");
    Log.d(TAG, "*** onActivityResult() - requestCode=" + requestCode + ", resultCode=" + resultCode);
    Log.d(TAG, "*** onActivityResult() - data=" + ((data == null) ? "null" : "not null"));
    Log.d(TAG,"-----------------------------------------------------------------");

    numFotosTiradas++;
    
    if (resultCode == RESULT_OK) {

      if (requestCode == TIRA_FOTO) {
        
        if(data!=null) {
          
          Log.w(TAG,"data.getData()= " + data.getData());
          Log.w(TAG,"extra: "+data.getStringExtra("extra1"));
          Log.w(TAG,"file: "+data.getStringExtra("file"));
          
          //File ff = new File(data.getStringExtra("file"));
          file = new File(data.getStringExtra("file"));
          
          outputFileUri = Uri.fromFile(file);
          
        }
        

        carregaImagem();

      }

    } else if (resultCode == RESULT_CANCELED) {
      // operação cancelada
      Log.w(TAG, "onActivityResult() - Operação cancelada pelo usuário");
      return;
    } else {
      Log.w(TAG, "onActivityResult() - não foi possível tratar o valor: " + resultCode);
      return;
    }

    Log.d(TAG,">>> alterando a orientação da tela");
    atualizaModoTela(Configuration.ORIENTATION_PORTRAIT);

  }

  /**
   * carregaImagem()
   * 
   * Carrega uma imagem a partir de um arquivo
   * 
   * 
   */
  private void carregaImagem() {

    Log.d(TAG, "carregaImagem()");

    if (file == null) {
      //
      Log.w(TAG, "carregaImagem() ==> file é nulo");
      return;
    }

    Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());

    if (bm == null) {
      Log.w(TAG, "carregaImage() - não foi possível carregar o bitmap a partir do arquivo: " + file.getAbsolutePath());
      return;
    }

    // redimensiona o bitmap
    bm = Bitmap.createScaledBitmap(bm, 200, 200, true);

    // exibe o bitmap
    image.setImageBitmap(bm);

    numFotosCarregadas++;
    
    // cria uma foto
    Foto foto = new Foto(file.getAbsolutePath());    
    foto.setImagem(bm);
    
    // adiciona a nova foto a lista de fotos
    listaFotos.add(foto);
    
    Log.i(TAG, "++++ numFotosTiradas: "+numFotosTiradas+", numFotosCarregadas: " +  numFotosCarregadas);
    
  }

  /**
   * 
   */
  @Override
  protected void onStart() {

    super.onStart();
    Log.d(TAG, "*** onStart() ***");
    showXXX();

  }

  /**
   * 
   */

  @Override
  protected void onResume() {

    super.onResume();
    Log.d(TAG, "*** onResume() ***");
    showXXX();
  }

  /**
   * 
   */

  @Override
  protected void onPause() {

    super.onPause();
    Log.d(TAG, "*** onPause() ***");
    showXXX();
  }

  /**
   * 
   */

  @Override
  protected void onStop() {

    super.onStop();
    Log.d(TAG, "*** onStop() ***");
    showXXX();
  }

  /**
   * 
   */

  @Override
  protected void onRestart() {

    super.onRestart();
    Log.w(TAG, "*******************");
    Log.w(TAG, "*** onRestart() ***");
    Log.w(TAG, "*******************");
    contador++;
    i++;
    j++;
    numRestart++;
    showXXX();
  }

  /**
   * 
   */

  @Override
  protected void onDestroy() {
    // TODO Auto-generated method stub
    super.onDestroy();
    Log.d(TAG, "*** onDestroy() ***");
    showXXX();
  }

  /**
   * onRestoreInstanceState(Bundle savedInstanceState)
   */

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {

    super.onRestoreInstanceState(savedInstanceState);
        
    Log.i(TAG, "*** onRestoreInstanceState(j="+j+")");
    
    if(savedInstanceState.containsKey("j")) {
      j = savedInstanceState.getInt("j");
    }
    
    showXXX();
    
  }

  /**
   * onSaveInstanceState(Bundle outState) 
   */

  @Override
  protected void onSaveInstanceState(Bundle outState) {

    super.onSaveInstanceState(outState);
    Log.i(TAG, "*** onSaveInstanceState()");
    
    outState.putInt("j", j);
    
    showXXX();
  }

  /**
   * showXXX()
   */
  void showXXX() {
    Log.v(TAG, "    showXXX() - file: " + file);
    Log.v(TAG, "    showXXX() - outputFileUri: " + outputFileUri);
    Log.v(TAG, "    showXXX() - Contador: " + contador + ", i=" + i + ", j=" + j);
    Log.v(TAG, "    showXXX() - numCreate: " + numCreate + ", numRestart: " + numRestart);

  }

  /**
   * atualizaModoTela(int novaOrientacao)
   * 
   * Atualiza a orientação da tela.
   * 
   * @param novaOrientacao
   */
  private void atualizaModoTela(int novaOrientacao) {

    int orientacaoAtual = this.getResources().getConfiguration().orientation;

    Log.d(TAG, "atualizaModoTela() - Orientação atual: " + orientacaoAtual);

    if (novaOrientacao != orientacaoAtual) {
      this.setRequestedOrientation(novaOrientacao);
    }

    if (orientacaoAtual == Configuration.ORIENTATION_LANDSCAPE) {
      Log.d(TAG, "atualizaModoTela() - Orientação da tela em LANDSCAPE");
    }

    else if (orientacaoAtual == Configuration.ORIENTATION_PORTRAIT) {
      Log.d(TAG, "atualizaModoTela() - Orientação da tela em PORTRAIT");
    }

  }

}
