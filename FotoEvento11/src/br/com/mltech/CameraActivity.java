
package br.com.mltech;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import br.com.mltech.modelo.Participacao;
import br.com.mltech.utils.FileUtils;
import br.com.mltech.utils.ManipulaImagem;
import br.com.mltech.utils.camera.CameraTools;

/**
 * CameraActivity
 * 
 * Activity respons�vel por controlar (implementar) as funcionalidades de uma
 * uma c�mera fotogr�fica.<br>
 * 
 * Retorna um array contendo as fotos.
 * 
 * @author maurocl
 * 
 */
public class CameraActivity extends Activity implements Constantes {

  private static final String TAG = "CameraActivity";

  // controla o tipo de disparo da c�mera
  private static final int MANUAL = 1;

  private static final int AUTOMATICO = 2;

  // intervalo entre uma foto e outra
  private static int intervalo = 3;

  // n�mero de fotos que ser�o tiradas
  private static int num_fotos = 3;

  // inst�ncia da c�mera
  private Camera mCamera;

  // inst�ncia da CameraPreview
  private CameraPreview mPreview;

  // flag usado para controle da aplica��o
  private static int flag = 1;

  // Vari�vel usada para desenhar a imagem da c�mera na tela
  private FrameLayout layoutPreview;

  // bot�es da m�quina
  private Button btnOk;

  // bot�o nova foto
  private Button btnNovo;

  // bot�o cancelar
  private Button btnCancelar;

  // bot�o capturar
  private Button btnCapture;

  // handler usado para controle do disparo autom�tico
  private Handler handler = new TestHandler();

  // controla o tipo de disparo da c�mera
  private int tipoDisparo = 2;

  // controla o n� de vezes que a activity foi reinicializada.
  private static int numRestarts = 0;

  // Indica que a c�mera est� em modo preview
  private boolean inPreview = false;

  // n� de fotos tiradas
  private int contador = 0;

  // estrutura para guardar as fotos
  //private Foto[] fotos = new Foto[NUM_FOTOS];
  private String[] fotos = new String[3];

  //
  private Participacao mParticipacao;

  /**
   * identificador da c�mera do dispositivo
   */
  private static int cameraId = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    // Obt�m as informa��es da intent chamadora.
    Intent i = getIntent();

    tipoDisparo = i.getIntExtra("TIPO_DISPARO", 2);
    num_fotos = i.getIntExtra("NUM_FOTOS", 1);

    // Obt�m informa��es do participante
    obtemInfoParticipacao(i);

    // Obt�m o identificador da c�mera
    obtemIdentificadorCamera(i);

    Log.d(TAG, "onCreate() - tipoDisparo=" + tipoDisparo);
    Log.d(TAG, "onCreate() - num_fotos=" + num_fotos);

    // a intent poderia solicitar:
    // - n� de fotos
    // tipo do disparo: manual ou autom�tico
    //

    Log.d(TAG, "*** onCreate() ***");

    // 
    setContentView(R.layout.cameraprev);

    if (!CameraTools.isCameraWorking(cameraId)) {
      Log.e(TAG, "onCreate() - c�mera: " + cameraId + " n�o est� funcionando");
      return;
    }

    // objeto onde ser� exibido a tela da c�mera
    layoutPreview = (FrameLayout) findViewById(R.id.camera_preview);

    btnCapture = (Button) findViewById(R.id.button_capture);
    btnOk = (Button) findViewById(R.id.btnOk);
    btnNovo = (Button) findViewById(R.id.btnNovo);
    btnCancelar = (Button) findViewById(R.id.btnCancelar);

    if (tipoDisparo == MANUAL) {

      // inicialmente exibe apenas o bot�o capturar
      btnCapture.setVisibility(android.view.View.VISIBLE);
      btnOk.setVisibility(android.view.View.GONE);
      btnNovo.setVisibility(android.view.View.GONE);
      btnCancelar.setVisibility(android.view.View.GONE);

    } else if (tipoDisparo == AUTOMATICO) {

      // n�o exibe bot�es
      btnCapture.setVisibility(android.view.View.GONE);
      btnOk.setVisibility(android.view.View.GONE);
      btnNovo.setVisibility(android.view.View.GONE);
      btnCancelar.setVisibility(android.view.View.GONE);

    }

    /**
     * Bot�o Capturar
     */
    btnCapture.setOnClickListener(new OnClickListener() {

      public void onClick(View view) {

        // bot�o de captura foi pressionado
        Log.i(TAG, "---------------------------------");
        Log.i(TAG, "bot�o de captura foi pressionado");
        Log.i(TAG, "---------------------------------");

        if (mCamera != null) {
          // dispara uma foto
          mCamera.takePicture(shutter, null, jpeg);
        }
        else {
          Log.e(TAG, "onClick(btnCapture) - bot�o Capturar - mCamera is null na hora de acionar o dispatador !!!");
        }

      }
    });

    /**
     * Bot�o Nova foto
     */
    btnNovo.setOnClickListener(new OnClickListener() {

      public void onClick(View view) {

        Log.d(TAG, "onClick(btnNovo) - bot�o nova foto");

        // reinicia o contador.
        contador = 0;

        // desabilita os bot�es n�o usados
        btnOk.setVisibility(android.view.View.GONE);
        btnNovo.setVisibility(android.view.View.GONE);
        btnCancelar.setVisibility(android.view.View.GONE);

        // torna vis�vel o bot�o capturar
        btnCapture.setVisibility(android.view.View.VISIBLE);

        //reiniciaCamera();
        startPreview();

      }
    });

    /**
     * Trata o pressionamento do Bot�o Ok.<br>
     * 
     * Confirma a foto.<br>
     * 
     * e sai da activity.<br>
     * 
     */
    btnOk.setOnClickListener(new OnClickListener() {

      public void onClick(View view) {

        Log.d(TAG, "onClick(btnOk) - bot�o Ok - Confirma a foto");

        // cria uma intent de resposta
        Intent intent = new Intent();

        //intent.setData(mUri);

        // estabelece o resultado da execu��o da activity
        setResult(RESULT_OK, intent);

        Log.d(TAG, "onClick(btnOk) - retorno");

        // finaliza a activity
        finish();

      }
    });

    /**
     * Bot�o cancelar
     */
    btnCancelar.setOnClickListener(new OnClickListener() {

      public void onClick(View view) {

        // finalizaActivity();
        Log.d(TAG, "onClick(btnCancelar) - bot�o cancelar");

        finalizaActivityResultCanceled();

      }
    });

  }

  /**
   * Obt�m o identificador da c�mera que ser� usada.<br>
   * 
   * Caso o par�metro de configura��o n�o seja configurado ser� usado 0 como
   * default.
   * 
   * @param intent
   *          intent Intent chamadora da Activity
   */
  private void obtemIdentificadorCamera(Intent intent) {

    // obt�m o identificador da c�mera
    cameraId = intent.getIntExtra("br.com.mltech.cameraId", -1);

    if (cameraId == -1) {
      Log.w(TAG, "obtemIdentificadorCamera() - O identificador da c�mera n�o foi encontrado. Ser� usado 0 como padr�o.");
      cameraId = 0;
    }

    Log.d(TAG, "obtemIdentificadorCamera() - atualizando o dientificador da c�mera - cameraId = " + cameraId);
  }

  /**
   * Obtem informa��o sobre a participa��o, isto �, um objeto da classe
   * Participacao.
   * 
   * @param intent
   *          Intent chamadora da Activity
   */
  private void obtemInfoParticipacao(Intent intent) {

    if (intent.getSerializableExtra(Constantes.PARTICIPACAO) != null) {

      mParticipacao = (Participacao) intent.getSerializableExtra(Constantes.PARTICIPACAO);

    } else {
      Log.w(TAG, "obtemInfoParticipacao() - mParticipa��o � nula");
      mParticipacao = new Participacao(null, Constantes.TIPO_FOTO_POLAROID, Constantes.CORES, null);
      Log.w(TAG, "obtemInfoParticipacao() - Usando um contratante null, formato de foto polaroid a cores, e nome do arquivo nulo");
    }

  }

  final ShutterCallback shutter = new ShutterCallback() {

    public void onShutter() {

      Log.v(TAG, "----------------------------------");
      Log.v(TAG, "onShutter() callback disparado !!!");
      Log.v(TAG, "----------------------------------");

    }

  };

  // Cria nova inst�ncia da classe PictureCallback
  final PictureCallback jpeg = new PictureCallback() {

    /**
     * M�todo chamado quando houver uma foto JPEG dispon�vel (callback da
     * opera��o de tirar foto)
     */
    public void onPictureTaken(byte[] data, Camera camera) {

      Log.i(TAG, "-----------------------");
      Log.i(TAG, "onPictureTaken() - jpeg");
      Log.i(TAG, "-----------------------");

      // Cria um nome para o arquivo onde ser� gravado a foto
      File f = FileUtils.obtemNomeArquivo(".jpg");
      String nomeArquivo = f.getAbsolutePath();

      try {

        gravaFoto(nomeArquivo, data);
        Log.d(TAG, "onPictureTaken() - arquivo: [" + nomeArquivo + "] gravado com sucesso.");

      } catch (Exception e) {

        Log.w(TAG, "onPictureTaken() - Falha na grava��o do arquivo: [" + nomeArquivo + "]");

      }

      // cria o bitmap a partir do array de bytes capturados
      Bitmap mImageBitmap = ManipulaImagem.getBitmapFromByteArray(data);

      if (mImageBitmap == null) {
        Log.w(TAG, "mImageBitmap � nulo");
      }

      //Foto foto = new Foto(mImageBitmap, nomeArquivo);

      //fotos[contador] = foto;

      fotos[contador] = nomeArquivo;

      // atualiza o n� de fotos disparadas
      contador++;

      mCamera.startPreview();

      // Atualiza as funcionalidades dos bot�es
      atualizaBotoesDeControle();

      // TODO qual � a situa��o nesse momento ?
      // o que � necess�rio fazer para ter a visualiza��o da c�mera novamente ?
      // pelo que eu entendo, ap�s tirar uma foto a situa��o da c^maera � stopped, correto ?

      // TODO verificar o disparo autom�tico da c�mera
      if (tipoDisparo == AUTOMATICO) {

        if (contador < num_fotos) {
          Log.d(TAG, "==>");
          Log.d(TAG, "==> Contador: " + contador);
          Log.d(TAG, "==>");
          setDisparoAutomatico(intervalo);
        }

        else {

          confirmaFoto();

          exibeFotos(fotos);

        }
      } else if (tipoDisparo == MANUAL) {
        // n�o faz nada        
        exibeFotos(fotos);
      }

    }

  };

  @Override
  protected void onStart() {

    super.onStart();

    Log.d(TAG, "*** onStart() ***");

  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {

    super.onRestoreInstanceState(savedInstanceState);

    Log.d(TAG, "*** onRestoreInstanceState() ***");

  }

  @Override
  protected void onResume() {

    super.onResume();

    Log.d(TAG, "*** onResume() ***");

    // obt�m uma inst�ncia da c�mera
    mCamera = CameraTools.getCameraInstance(cameraId);

    // Configura os par�metros da c�mera
    configParametrosCamera();

    Log.i(TAG, "onResume() - ==> mCamera=" + mCamera);

    if (mPreview == null) {

      Log.d(TAG, "onResume() - ==> mPreview � nulo");

      // configura os par�metros da c�mera apenas se mPreview for nulo.
      Log.w(TAG, "onResume() - chama configuraParamCamera()");

      // Configura os par�metros da c�mera
      //configuraParamCamera(11);

      // obtem o tipo do efeito de cores que deve ser aplicado
      int tipoEfeito = mParticipacao.getEfeitoFoto();

      // Configura a c�mera
      configuraParamCamera2(tipoEfeito);

    } else {

      Log.d(TAG, "onResume() - ==> mPreview n�o � nulo");
      Log.w(TAG, "onResume() - Configura��o dos par�metros n�o foi feita !!!");

    }

    // cria uma inst�ncia da "tela" de preview
    mPreview = new CameraPreview(this, mCamera);

    // TODO verificar o layout da c�mera
    layoutPreview.addView(mPreview);

    startPreview();

    if (tipoDisparo == AUTOMATICO) {
      setDisparoAutomatico(intervalo);
    } else if (tipoDisparo == MANUAL) {
      // n�o faz nada
    }

    if (mCamera == null) {

      Log.w(TAG, "onResume - mCamera is null");

    } else {

      // camera � diferente de null

      Log.d(TAG, "onResume() - -------------");
      Log.d(TAG, "onResume() - flag=" + flag);
      Log.d(TAG, "onResume() - -------------");

      if (flag == 0) {

        flag = 1;

      } else {

        // Stops capturing and drawing preview frames to the surface, and resets
        // the camera for a future call to startPreview().
        mCamera.stopPreview();

        Log.d(TAG, "onResume() - antes da chamada do m�todo mCamera.startPreview()");

        // Starts capturing and drawing preview frames to the screen
        mCamera.startPreview();

        Log.d(TAG, "onResume() - ap�s chamada do m�todo mCamera.startPreview()");

      }

    }

  }

  /**
   * Configura os par�metros de uso da c�mera
   */
  private void configParametrosCamera() {

    // Obtem a lista de par�metros suportados pela c�mera
    Camera.Parameters params = mCamera.getParameters();

    // Obt�m a lista dos modos de foco
    List<String> focusModes = params.getSupportedFocusModes();

    // --------------------------------------------------------
    // configura os par�metros de configura��o da c�mera
    // --------------------------------------------------------
    if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {

      // auto focus mode is supported
      Log.d(TAG, "n# focus modes supported: " + focusModes.size());
      for (String modes : focusModes) {
        Log.d(TAG, "focus mode: " + modes);
      }

    }

  }

  /**
   * Configura os par�metros da c�mera
   * 
   * @param efeitoFoto
   *          Efeito de cores aplicado nas fotos
   * 
   */
  private void configuraParamCamera2(int efeitoFoto) {

    // Set the clockwise rotation of preview display in degrees.
    // This affects the preview frames and the picture displayed after snapshot.
    // This method is useful for portrait mode applications.
    // Note that preview display of front-facing cameras is flipped horizontally
    // before the rotation, that is, the image is reflected along the central vertical axis of the
    // camera sensor.
    // So the users can see themselves as looking into a mirror.
    int displayOrientation = 0;

    Log.d(TAG, "configuraParamCamera2() - in�cio");

    Log.d(TAG, "configuraParamCamera2() - setDisplayOrientation(" + displayOrientation + ")");
    mCamera.setDisplayOrientation(displayOrientation);

    Log.d(TAG, "configuraParamCamera2() - setCameraDisplayOrientation");
    CameraTools.setCameraDisplayOrientation(this, cameraId, mCamera);

    // Obt�m os par�metros de configura��o da c�mera
    // Obt�m a configura��o corrente para esse servi�o de c�mera.
    Camera.Parameters params = mCamera.getParameters();

    //---------------------------------------------------

    List<String> focusModes = params.getSupportedFocusModes();

    // --------------------------------------------------------
    // configura os par�metros de configura��o da c�mera
    // --------------------------------------------------------
    if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
      // auto focus mode is supported
      Log.d(TAG, "n# focus modes supported: " + focusModes.size());
      for (String modes : focusModes) {
        Log.d(TAG, "focus mode: " + modes);
      }

    }
    //------------------------------------------------------    

    // HashMap<String, List<String>> hash = CameraTools.getParametersDetail(mCamera);

    // exibe todos os par�metros de configura��o da c�mera.
    CameraTools.showParametersDetail(mCamera);

    // altera o tamanho da foto
    params.setPictureSize(640, 480);

    
    Log.i(TAG,"Configura��es da C�mera:");
    List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
    
    if(previewSizes!=null) {
            
      int i = 0;
      for(Camera.Size size: previewSizes) {
        Log.d(TAG," PreviewSize("+i+") = "+size.width+"x"+size.height);
        i++;
      }
      
      Camera.Size currentPreviewSize = params.getPreviewSize();
      
      Log.d(TAG,"Current Preview Size: "+ +currentPreviewSize.width+"x"+currentPreviewSize.height);
      
      
    }
    
    params.setPreviewSize(720, 576);
    
    Camera.Size currentPreviewSize = params.getPreviewSize();

    
    Log.d(TAG,"Current Preview Size: "+ +currentPreviewSize.width+"x"+currentPreviewSize.height);
    
    if (efeitoFoto == CORES) {
      Log.d(TAG, "configuraParamCamera2() - altera o color effect para cores");
      params.setColorEffect(Camera.Parameters.EFFECT_NONE);
    } else if (efeitoFoto == PB) {
      Log.d(TAG, "configuraParamCamera2() - altera o color effect para preto & branco");
      params.setColorEffect(Camera.Parameters.EFFECT_MONO);
    } else {
      // o valor default � nenhum efeito
      params.setColorEffect(Camera.Parameters.EFFECT_NONE);
    }

    // Atualiza a configura��o para esse servi�o de c�mera.
    try {
      mCamera.setParameters(params);
      Log.d(TAG, "configuraParamCamera() -  par�metros atualizados com sucesso");
    } catch (RuntimeException e) {
      Log.w(TAG, "configuraParamCamera() -  RuntimeException durante a configura��o da c�mera");
    }

    // Exibe alguns parametros da camera
    // showCameraParameters(mCamera);

    Log.d(TAG, "configuraParamCamera() - fim");

  }


  @Override
  protected void onSaveInstanceState(Bundle outState) {

    super.onSaveInstanceState(outState);

    Log.d(TAG, "*** onSaveInstanceState() ***");

  }

  /**
   * M�todo chamado quando a activity � pausada.
   */
  @Override
  protected void onPause() {

    Log.d(TAG, "*** onPause() ***");

    if (inPreview) {
      // para a visualiza��o da c�mera
      mCamera.stopPreview();
    }

    // libera a c�mera
    mCamera.release();

    mCamera = null;

    inPreview = false;

    super.onPause();

  }

  @Override
  protected void onStop() {

    super.onStop();
    Log.d(TAG, "*** onStop() ***");

  }

  @Override
  protected void onRestart() {

    super.onRestart();

    // incrementa o n� de vezes que a activity foi reinicializada.
    numRestarts++;

    Log.w(TAG, "*");
    Log.w(TAG, "*** onRestart(" + numRestarts + ") ***");
    Log.w(TAG, "*");

  }

  @Override
  protected void onDestroy() {

    super.onDestroy();

    Log.d(TAG, "*** onDestroy() ***");

    if (mCamera != null) {

      Log.d(TAG, "onDestroy() - before release");

      // TODO verificar se essa opera��o � realmente necess�ria
      // Disconnects and releases the Camera object resources.
      mCamera.release();

      Log.d(TAG, "onDestroy() - after release");

    } else {

      Log.i(TAG, "onDestroy() - c�mera est� liberada");

    }

  }

  /**
   * Exibe informa��es sobre as fotos.
   * 
   * @param fotos
   *          Array de fotos.
   * 
   */
  private void exibeFotos(String[] fotos) {

    if (fotos == null) {
      Log.d(TAG, "Fotos est� vazio");
      return;
    }

    int i = 0;
    for (String foto : fotos) {
      Log.d(TAG, "exibeFotos() - Foto: " + i + " - " + foto);
      i++;
    }
  }

  /**
   * Grava a foto obtida pela c�mera
   * 
   * @param nomeArquivo
   *          Nome completo do arquivo onde a foto ser� armazenada
   * @param data
   *          Array de bytes contendo a foto (no formato jpeg).
   * 
   * @throws Exception
   *           Caso haja algum problema na grava��o da foto.
   * 
   */
  private void gravaFoto(String nomeArquivo, byte[] data) throws Exception {

    if (nomeArquivo == null || nomeArquivo.equals("")) {
      throw new IllegalArgumentException("O nome do arquivo est� vazio");
    }

    if (data == null) {
      throw new IllegalArgumentException("O array de bytes est� vazio");
    }

    Log.d(TAG, "onPictureTaken() - data: " + fmtFloat((float) (data.length / 1024.0)) + " KBytes");

    FileOutputStream fos = new FileOutputStream(new File(nomeArquivo));
    fos.write(data);
    fos.close();

  }

  /**
   * Atualiza os bot�es de acordo com o modo de disparo da c�mera.
   */
  private void atualizaBotoesDeControle() {

    if (tipoDisparo == MANUAL) {

      // habilita os bot�es de Ok, Novo, Cancelar.
      // permite que a foto seja aprovada (Ok), que seja tirada uma nova foto
      // (Novo) ou que a opera��o seja cancelada sem retornar a foto.
      btnOk.setVisibility(android.view.View.VISIBLE);
      btnNovo.setVisibility(android.view.View.VISIBLE);
      btnCancelar.setVisibility(android.view.View.VISIBLE);
      // torna invis�vel o bot�o capturar
      btnCapture.setVisibility(android.view.View.GONE);
      //btnCapture.setVisibility(android.view.View.VISIBLE);

    } else if (tipoDisparo == AUTOMATICO) {

      // desabilita bot�es
      // TODO aqui deveremos habilitar a op��o de aprova��o da foto (Ok), Novo
      // e Cancelar
      btnOk.setVisibility(android.view.View.GONE);
      btnNovo.setVisibility(android.view.View.GONE);
      btnCancelar.setVisibility(android.view.View.GONE);
      btnCapture.setVisibility(android.view.View.GONE);

    }

  }

  /**
   * Inicia o preview da c�mera (se a c�mera n�o for nula).
   */
  private void startPreview() {

    if (mCamera != null) {

      mCamera.startPreview();

      inPreview = true;

    }

  }

  /**
   * Prepara a c�mera para disparar a captura de uma foto.
   * 
   * @param segundos
   *          N� de segundos que ser�o esperados
   * 
   * @throws IllegalArgumentException
   *           se o n� de segundos n�o for v�lido
   * 
   */
  private void setDisparoAutomatico(int segundos) throws IllegalArgumentException {

    if (segundos < 0) {
      throw new IllegalArgumentException("O tempo n�o poder� ser negativo");
    }

    if (mCamera == null) {
      Log.e(TAG, "setDisparoAutomatico() - c�mera est� nula");
      return;
    }

    // Exibe o aviso de disparo autom�tico
    Toast.makeText(getBaseContext(), "Disparo autom�tico em " + segundos + " segundos.", Toast.LENGTH_SHORT).show();

    // cria uma nova mensagem (identificador 1)
    Message msg = new Message();
    // identificador da mensagem
    msg.what = 1;
    // envia a mensagem
    handler.sendMessageDelayed(msg, segundos * 1000);

    Log.i(TAG, "-------------------------------------");
    Log.i(TAG, "captura foi disparada automaticamente");
    Log.i(TAG, "-------------------------------------");

  }

  /**
   * Finaliza a captura das fotos.
   */
  private void confirmaFoto() {

    Log.i(TAG, "---------------------------------------------------------");
    Log.i(TAG, " => confirmaFoto()");
    Log.i(TAG, "---------------------------------------------------------");

    Log.d(TAG, "N�mero de fotos tiradas: " + contador);

    // Finaliza activity com sucesso
    finalizaActivityResultOk();

  }

  /**
   * Cria uma intent de resposta Ok<br>
   * Finaliza a activity com resultado Ok.
   */
  private void finalizaActivityResultOk() {

    Log.d(TAG, "finalizaActivityResultOk()");

    // cria uma intent de resposta
    Intent intent = new Intent();

    // Set the data this intent is operating on.
    // A intent retorna a endere�o (Uri) onde a foto est� armazenada.
    //intent.setData(mUri);

    /*
     * String[] xxx = new String[fotos.length]; for (int i = 0; i <
     * fotos.length; i++) { xxx[i] = fotos[i].getArquivo();
     * Log.d(TAG,"Foto "+i+": "+xxx[i]); }
     */

    //intent.putExtra("br.com.mltech.xxx", xxx);
    intent.putExtra("br.com.mltech.fotos", fotos);

    // estabelece o resultado da execu��o da activity
    setResult(RESULT_OK, intent);

    finaliza();

  }

  /**
   * Finaliza a activity com um cancelamento.<br>
   */
  private void finalizaActivityResultCanceled() {

    Log.d(TAG, "finalizaActivityResultCanceled()");

    Intent intent = new Intent();

    intent.setData(null);

    // estabelece o resultado da execu��o da activity
    setResult(RESULT_CANCELED, intent);

    finaliza();

  }

  /**
   * Executa o fechamento da activity retornando o controle o m�todo
   * onActivityResult().
   * 
   */
  private void finaliza() {

    Log.d(TAG, "finaliza()");

    // finaliza a activity
    finish();
  }

  /**
   * Exibe par�metros de configura��o da c�mera.
   * 
   * @param c
   *          Inst�ncia da c�mera
   * 
   */
  public static void showCameraParameters(Camera c) {

    if (c == null) {
      Log.w(TAG, "showCameraParameters() - A inst�ncia da c�mera est� nula");
      return;
    }

    // Obt�m a configura��o dos par�metros atuais
    Camera.Parameters parameters = c.getParameters();

    if (parameters == null) {
      Log.w(TAG, "showCameraParameters() - C�mera n�o retornou nenhum par�metro de configura��o");
      return;
    }

    // obt�m o tamanho da imagem
    Size size = parameters.getPictureSize();

    Log.v(TAG, "showCameraParameters() - size: " + size.width + " x " + size.height);
    Log.v(TAG, "showCameraParameters() - getHorizontalViewAngle: " + parameters.getHorizontalViewAngle());
    Log.v(TAG, "showCameraParameters() - getJpegThumbnailQuality: " + parameters.getJpegThumbnailQuality());

  }

  /**
   * Obt�m uma string indicando a face para onde a c�mera est� apontando (face
   * traseira ou face frontal)
   * 
   * @param facing
   *          C�digo do facing.
   * 
   * @return Um string indicando se a c�mera est� virada para traz ou para
   *         frente.
   */
  public static String getStrFacing(int facing) {

    if (facing == CameraInfo.CAMERA_FACING_BACK) {
      return "CAMERA_FACING_BACK";
    }
    else if (facing == CameraInfo.CAMERA_FACING_FRONT) {
      return "CAMERA_FACING_FRONT";
    }
    return null;
  }

  /**
   * M�todo utilit�rio que retorna uma string com um n�mero formatado usando
   * duas casas decimais.
   * 
   * @param args
   *          N�mero que ser� formatado
   * 
   * @return Uma string com um n�mero formatado.
   */
  private static String fmtFloat(float args) {

    java.util.Formatter fmt = new java.util.Formatter();

    String formattedString = fmt.format("%.2f", args).toString();

    return formattedString;

  }

  /**
   * Respons�vel pelo disparo da captura de uma foto.
   * 
   * @author maurocl
   * 
   */
  private class TestHandler extends Handler {

    /**
     * Lida com as mensagens
     */
    @Override
    public void handleMessage(Message msg) {

      super.handleMessage(msg);

      // o atributo msg.what permite identificar a mensagem
      if (msg.what == 1) {

        Toast.makeText(getBaseContext(), "Disparando a captura ...", Toast.LENGTH_SHORT).show();

        // tira uma foto (dispara o obturador da m�quina).
        mCamera.takePicture(shutter, null, jpeg);

      }

    }

  }

}

/**
 * Esta classe implementa a interface Camera.AutoFocusCallback.
 * 
 * @author maurocl
 * 
 */
class ConfiguraAutoFocus2 implements Camera.AutoFocusCallback {

  private static final String TAG = "XXX";

  //
  // TODO habilitar o auto focus da c�mera
  //
  public void onAutoFocus(boolean success, Camera camera) {

    Log.d(TAG, "ConfiguraAutoFocus.onAutoFocurs() - sucess: " + success + ", camera: " + camera);

  }

}
