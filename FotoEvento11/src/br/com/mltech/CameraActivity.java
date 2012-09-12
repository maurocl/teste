
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
 * Activity responsável por controlar (implementar) as funcionalidades de uma
 * uma câmera fotográfica.<br>
 * 
 * Retorna um array contendo as fotos.
 * 
 * @author maurocl
 * 
 */
public class CameraActivity extends Activity implements Constantes {

  private static final String TAG = "CameraActivity";

  // controla o tipo de disparo da câmera
  private static final int MANUAL = 1;

  private static final int AUTOMATICO = 2;

  // intervalo entre uma foto e outra
  private static int intervalo = 3;

  // número de fotos que serão tiradas
  private static int num_fotos = 3;

  // instância da câmera
  private Camera mCamera;

  // instância da CameraPreview
  private CameraPreview mPreview;

  // flag usado para controle da aplicação
  private static int flag = 1;

  // Variável usada para desenhar a imagem da câmera na tela
  private FrameLayout layoutPreview;

  // botões da máquina
  private Button btnOk;

  // botão nova foto
  private Button btnNovo;

  // botão cancelar
  private Button btnCancelar;

  // botão capturar
  private Button btnCapture;

  // handler usado para controle do disparo automático
  private Handler handler = new TestHandler();

  // controla o tipo de disparo da câmera
  private int tipoDisparo = 2;

  // controla o nº de vezes que a activity foi reinicializada.
  private static int numRestarts = 0;

  // Indica que a câmera está em modo preview
  private boolean inPreview = false;

  // nº de fotos tiradas
  private int contador = 0;

  // estrutura para guardar as fotos
  //private Foto[] fotos = new Foto[NUM_FOTOS];
  private String[] fotos = new String[3];

  //
  private Participacao mParticipacao;

  /**
   * identificador da câmera do dispositivo
   */
  private static int cameraId = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    // Obtém as informações da intent chamadora.
    Intent i = getIntent();

    tipoDisparo = i.getIntExtra("TIPO_DISPARO", 2);
    num_fotos = i.getIntExtra("NUM_FOTOS", 1);

    // Obtém informações do participante
    obtemInfoParticipacao(i);

    // Obtém o identificador da câmera
    obtemIdentificadorCamera(i);

    Log.d(TAG, "onCreate() - tipoDisparo=" + tipoDisparo);
    Log.d(TAG, "onCreate() - num_fotos=" + num_fotos);

    // a intent poderia solicitar:
    // - nº de fotos
    // tipo do disparo: manual ou automático
    //

    Log.d(TAG, "*** onCreate() ***");

    // 
    setContentView(R.layout.cameraprev);

    if (!CameraTools.isCameraWorking(cameraId)) {
      Log.e(TAG, "onCreate() - câmera: " + cameraId + " não está funcionando");
      return;
    }

    // objeto onde será exibido a tela da câmera
    layoutPreview = (FrameLayout) findViewById(R.id.camera_preview);

    btnCapture = (Button) findViewById(R.id.button_capture);
    btnOk = (Button) findViewById(R.id.btnOk);
    btnNovo = (Button) findViewById(R.id.btnNovo);
    btnCancelar = (Button) findViewById(R.id.btnCancelar);

    if (tipoDisparo == MANUAL) {

      // inicialmente exibe apenas o botão capturar
      btnCapture.setVisibility(android.view.View.VISIBLE);
      btnOk.setVisibility(android.view.View.GONE);
      btnNovo.setVisibility(android.view.View.GONE);
      btnCancelar.setVisibility(android.view.View.GONE);

    } else if (tipoDisparo == AUTOMATICO) {

      // não exibe botões
      btnCapture.setVisibility(android.view.View.GONE);
      btnOk.setVisibility(android.view.View.GONE);
      btnNovo.setVisibility(android.view.View.GONE);
      btnCancelar.setVisibility(android.view.View.GONE);

    }

    /**
     * Botão Capturar
     */
    btnCapture.setOnClickListener(new OnClickListener() {

      public void onClick(View view) {

        // botão de captura foi pressionado
        Log.i(TAG, "---------------------------------");
        Log.i(TAG, "botão de captura foi pressionado");
        Log.i(TAG, "---------------------------------");

        if (mCamera != null) {
          // dispara uma foto
          mCamera.takePicture(shutter, null, jpeg);
        }
        else {
          Log.e(TAG, "onClick(btnCapture) - botão Capturar - mCamera is null na hora de acionar o dispatador !!!");
        }

      }
    });

    /**
     * Botão Nova foto
     */
    btnNovo.setOnClickListener(new OnClickListener() {

      public void onClick(View view) {

        Log.d(TAG, "onClick(btnNovo) - botão nova foto");

        // reinicia o contador.
        contador = 0;

        // desabilita os botões não usados
        btnOk.setVisibility(android.view.View.GONE);
        btnNovo.setVisibility(android.view.View.GONE);
        btnCancelar.setVisibility(android.view.View.GONE);

        // torna visível o botão capturar
        btnCapture.setVisibility(android.view.View.VISIBLE);

        //reiniciaCamera();
        startPreview();

      }
    });

    /**
     * Trata o pressionamento do Botão Ok.<br>
     * 
     * Confirma a foto.<br>
     * 
     * e sai da activity.<br>
     * 
     */
    btnOk.setOnClickListener(new OnClickListener() {

      public void onClick(View view) {

        Log.d(TAG, "onClick(btnOk) - botão Ok - Confirma a foto");

        // cria uma intent de resposta
        Intent intent = new Intent();

        //intent.setData(mUri);

        // estabelece o resultado da execução da activity
        setResult(RESULT_OK, intent);

        Log.d(TAG, "onClick(btnOk) - retorno");

        // finaliza a activity
        finish();

      }
    });

    /**
     * Botão cancelar
     */
    btnCancelar.setOnClickListener(new OnClickListener() {

      public void onClick(View view) {

        // finalizaActivity();
        Log.d(TAG, "onClick(btnCancelar) - botão cancelar");

        finalizaActivityResultCanceled();

      }
    });

  }

  /**
   * Obtém o identificador da câmera que será usada.<br>
   * 
   * Caso o parâmetro de configuração não seja configurado será usado 0 como
   * default.
   * 
   * @param intent
   *          intent Intent chamadora da Activity
   */
  private void obtemIdentificadorCamera(Intent intent) {

    // obtém o identificador da câmera
    cameraId = intent.getIntExtra("br.com.mltech.cameraId", -1);

    if (cameraId == -1) {
      Log.w(TAG, "obtemIdentificadorCamera() - O identificador da câmera não foi encontrado. Será usado 0 como padrão.");
      cameraId = 0;
    }

    Log.d(TAG, "obtemIdentificadorCamera() - atualizando o dientificador da câmera - cameraId = " + cameraId);
  }

  /**
   * Obtem informação sobre a participação, isto é, um objeto da classe
   * Participacao.
   * 
   * @param intent
   *          Intent chamadora da Activity
   */
  private void obtemInfoParticipacao(Intent intent) {

    if (intent.getSerializableExtra(Constantes.PARTICIPACAO) != null) {

      mParticipacao = (Participacao) intent.getSerializableExtra(Constantes.PARTICIPACAO);

    } else {
      Log.w(TAG, "obtemInfoParticipacao() - mParticipação é nula");
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

  // Cria nova instância da classe PictureCallback
  final PictureCallback jpeg = new PictureCallback() {

    /**
     * Método chamado quando houver uma foto JPEG disponível (callback da
     * operação de tirar foto)
     */
    public void onPictureTaken(byte[] data, Camera camera) {

      Log.i(TAG, "-----------------------");
      Log.i(TAG, "onPictureTaken() - jpeg");
      Log.i(TAG, "-----------------------");

      // Cria um nome para o arquivo onde será gravado a foto
      File f = FileUtils.obtemNomeArquivo(".jpg");
      String nomeArquivo = f.getAbsolutePath();

      try {

        gravaFoto(nomeArquivo, data);
        Log.d(TAG, "onPictureTaken() - arquivo: [" + nomeArquivo + "] gravado com sucesso.");

      } catch (Exception e) {

        Log.w(TAG, "onPictureTaken() - Falha na gravação do arquivo: [" + nomeArquivo + "]");

      }

      // cria o bitmap a partir do array de bytes capturados
      Bitmap mImageBitmap = ManipulaImagem.getBitmapFromByteArray(data);

      if (mImageBitmap == null) {
        Log.w(TAG, "mImageBitmap é nulo");
      }

      //Foto foto = new Foto(mImageBitmap, nomeArquivo);

      //fotos[contador] = foto;

      fotos[contador] = nomeArquivo;

      // atualiza o nº de fotos disparadas
      contador++;

      mCamera.startPreview();

      // Atualiza as funcionalidades dos botões
      atualizaBotoesDeControle();

      // TODO qual é a situação nesse momento ?
      // o que é necessário fazer para ter a visualização da câmera novamente ?
      // pelo que eu entendo, após tirar uma foto a situação da c^maera é stopped, correto ?

      // TODO verificar o disparo automático da câmera
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
        // não faz nada        
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

    // obtém uma instância da câmera
    mCamera = CameraTools.getCameraInstance(cameraId);

    // Configura os parâmetros da câmera
    configParametrosCamera();

    Log.i(TAG, "onResume() - ==> mCamera=" + mCamera);

    if (mPreview == null) {

      Log.d(TAG, "onResume() - ==> mPreview é nulo");

      // configura os parâmetros da câmera apenas se mPreview for nulo.
      Log.w(TAG, "onResume() - chama configuraParamCamera()");

      // Configura os parâmetros da câmera
      //configuraParamCamera(11);

      // obtem o tipo do efeito de cores que deve ser aplicado
      int tipoEfeito = mParticipacao.getEfeitoFoto();

      // Configura a câmera
      configuraParamCamera2(tipoEfeito);

    } else {

      Log.d(TAG, "onResume() - ==> mPreview não é nulo");
      Log.w(TAG, "onResume() - Configuração dos parâmetros não foi feita !!!");

    }

    // cria uma instância da "tela" de preview
    mPreview = new CameraPreview(this, mCamera);

    // TODO verificar o layout da câmera
    layoutPreview.addView(mPreview);

    startPreview();

    if (tipoDisparo == AUTOMATICO) {
      setDisparoAutomatico(intervalo);
    } else if (tipoDisparo == MANUAL) {
      // não faz nada
    }

    if (mCamera == null) {

      Log.w(TAG, "onResume - mCamera is null");

    } else {

      // camera é diferente de null

      Log.d(TAG, "onResume() - -------------");
      Log.d(TAG, "onResume() - flag=" + flag);
      Log.d(TAG, "onResume() - -------------");

      if (flag == 0) {

        flag = 1;

      } else {

        // Stops capturing and drawing preview frames to the surface, and resets
        // the camera for a future call to startPreview().
        mCamera.stopPreview();

        Log.d(TAG, "onResume() - antes da chamada do método mCamera.startPreview()");

        // Starts capturing and drawing preview frames to the screen
        mCamera.startPreview();

        Log.d(TAG, "onResume() - após chamada do método mCamera.startPreview()");

      }

    }

  }

  /**
   * Configura os parâmetros de uso da câmera
   */
  private void configParametrosCamera() {

    // Obtem a lista de parâmetros suportados pela câmera
    Camera.Parameters params = mCamera.getParameters();

    // Obtém a lista dos modos de foco
    List<String> focusModes = params.getSupportedFocusModes();

    // --------------------------------------------------------
    // configura os parâmetros de configuração da câmera
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
   * Configura os parâmetros da câmera
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

    Log.d(TAG, "configuraParamCamera2() - início");

    Log.d(TAG, "configuraParamCamera2() - setDisplayOrientation(" + displayOrientation + ")");
    mCamera.setDisplayOrientation(displayOrientation);

    Log.d(TAG, "configuraParamCamera2() - setCameraDisplayOrientation");
    CameraTools.setCameraDisplayOrientation(this, cameraId, mCamera);

    // Obtém os parâmetros de configuração da câmera
    // Obtém a configuração corrente para esse serviço de câmera.
    Camera.Parameters params = mCamera.getParameters();

    //---------------------------------------------------

    List<String> focusModes = params.getSupportedFocusModes();

    // --------------------------------------------------------
    // configura os parâmetros de configuração da câmera
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

    // exibe todos os parâmetros de configuração da câmera.
    CameraTools.showParametersDetail(mCamera);

    // altera o tamanho da foto
    params.setPictureSize(640, 480);

    
    Log.i(TAG,"Configurações da Câmera:");
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
      // o valor default é nenhum efeito
      params.setColorEffect(Camera.Parameters.EFFECT_NONE);
    }

    // Atualiza a configuração para esse serviço de câmera.
    try {
      mCamera.setParameters(params);
      Log.d(TAG, "configuraParamCamera() -  parâmetros atualizados com sucesso");
    } catch (RuntimeException e) {
      Log.w(TAG, "configuraParamCamera() -  RuntimeException durante a configuração da câmera");
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
   * Método chamado quando a activity é pausada.
   */
  @Override
  protected void onPause() {

    Log.d(TAG, "*** onPause() ***");

    if (inPreview) {
      // para a visualização da câmera
      mCamera.stopPreview();
    }

    // libera a câmera
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

    // incrementa o nº de vezes que a activity foi reinicializada.
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

      // TODO verificar se essa operação é realmente necessária
      // Disconnects and releases the Camera object resources.
      mCamera.release();

      Log.d(TAG, "onDestroy() - after release");

    } else {

      Log.i(TAG, "onDestroy() - câmera está liberada");

    }

  }

  /**
   * Exibe informações sobre as fotos.
   * 
   * @param fotos
   *          Array de fotos.
   * 
   */
  private void exibeFotos(String[] fotos) {

    if (fotos == null) {
      Log.d(TAG, "Fotos está vazio");
      return;
    }

    int i = 0;
    for (String foto : fotos) {
      Log.d(TAG, "exibeFotos() - Foto: " + i + " - " + foto);
      i++;
    }
  }

  /**
   * Grava a foto obtida pela câmera
   * 
   * @param nomeArquivo
   *          Nome completo do arquivo onde a foto será armazenada
   * @param data
   *          Array de bytes contendo a foto (no formato jpeg).
   * 
   * @throws Exception
   *           Caso haja algum problema na gravação da foto.
   * 
   */
  private void gravaFoto(String nomeArquivo, byte[] data) throws Exception {

    if (nomeArquivo == null || nomeArquivo.equals("")) {
      throw new IllegalArgumentException("O nome do arquivo está vazio");
    }

    if (data == null) {
      throw new IllegalArgumentException("O array de bytes está vazio");
    }

    Log.d(TAG, "onPictureTaken() - data: " + fmtFloat((float) (data.length / 1024.0)) + " KBytes");

    FileOutputStream fos = new FileOutputStream(new File(nomeArquivo));
    fos.write(data);
    fos.close();

  }

  /**
   * Atualiza os botões de acordo com o modo de disparo da câmera.
   */
  private void atualizaBotoesDeControle() {

    if (tipoDisparo == MANUAL) {

      // habilita os botões de Ok, Novo, Cancelar.
      // permite que a foto seja aprovada (Ok), que seja tirada uma nova foto
      // (Novo) ou que a operação seja cancelada sem retornar a foto.
      btnOk.setVisibility(android.view.View.VISIBLE);
      btnNovo.setVisibility(android.view.View.VISIBLE);
      btnCancelar.setVisibility(android.view.View.VISIBLE);
      // torna invisível o botão capturar
      btnCapture.setVisibility(android.view.View.GONE);
      //btnCapture.setVisibility(android.view.View.VISIBLE);

    } else if (tipoDisparo == AUTOMATICO) {

      // desabilita botões
      // TODO aqui deveremos habilitar a opção de aprovação da foto (Ok), Novo
      // e Cancelar
      btnOk.setVisibility(android.view.View.GONE);
      btnNovo.setVisibility(android.view.View.GONE);
      btnCancelar.setVisibility(android.view.View.GONE);
      btnCapture.setVisibility(android.view.View.GONE);

    }

  }

  /**
   * Inicia o preview da câmera (se a câmera não for nula).
   */
  private void startPreview() {

    if (mCamera != null) {

      mCamera.startPreview();

      inPreview = true;

    }

  }

  /**
   * Prepara a câmera para disparar a captura de uma foto.
   * 
   * @param segundos
   *          Nº de segundos que serão esperados
   * 
   * @throws IllegalArgumentException
   *           se o nº de segundos não for válido
   * 
   */
  private void setDisparoAutomatico(int segundos) throws IllegalArgumentException {

    if (segundos < 0) {
      throw new IllegalArgumentException("O tempo não poderá ser negativo");
    }

    if (mCamera == null) {
      Log.e(TAG, "setDisparoAutomatico() - câmera está nula");
      return;
    }

    // Exibe o aviso de disparo automático
    Toast.makeText(getBaseContext(), "Disparo automático em " + segundos + " segundos.", Toast.LENGTH_SHORT).show();

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

    Log.d(TAG, "Número de fotos tiradas: " + contador);

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
    // A intent retorna a endereço (Uri) onde a foto está armazenada.
    //intent.setData(mUri);

    /*
     * String[] xxx = new String[fotos.length]; for (int i = 0; i <
     * fotos.length; i++) { xxx[i] = fotos[i].getArquivo();
     * Log.d(TAG,"Foto "+i+": "+xxx[i]); }
     */

    //intent.putExtra("br.com.mltech.xxx", xxx);
    intent.putExtra("br.com.mltech.fotos", fotos);

    // estabelece o resultado da execução da activity
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

    // estabelece o resultado da execução da activity
    setResult(RESULT_CANCELED, intent);

    finaliza();

  }

  /**
   * Executa o fechamento da activity retornando o controle o método
   * onActivityResult().
   * 
   */
  private void finaliza() {

    Log.d(TAG, "finaliza()");

    // finaliza a activity
    finish();
  }

  /**
   * Exibe parâmetros de configuração da câmera.
   * 
   * @param c
   *          Instância da câmera
   * 
   */
  public static void showCameraParameters(Camera c) {

    if (c == null) {
      Log.w(TAG, "showCameraParameters() - A instância da câmera está nula");
      return;
    }

    // Obtém a configuração dos parâmetros atuais
    Camera.Parameters parameters = c.getParameters();

    if (parameters == null) {
      Log.w(TAG, "showCameraParameters() - Câmera não retornou nenhum parâmetro de configuração");
      return;
    }

    // obtém o tamanho da imagem
    Size size = parameters.getPictureSize();

    Log.v(TAG, "showCameraParameters() - size: " + size.width + " x " + size.height);
    Log.v(TAG, "showCameraParameters() - getHorizontalViewAngle: " + parameters.getHorizontalViewAngle());
    Log.v(TAG, "showCameraParameters() - getJpegThumbnailQuality: " + parameters.getJpegThumbnailQuality());

  }

  /**
   * Obtém uma string indicando a face para onde a câmera está apontando (face
   * traseira ou face frontal)
   * 
   * @param facing
   *          Código do facing.
   * 
   * @return Um string indicando se a câmera está virada para traz ou para
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
   * Método utilitário que retorna uma string com um número formatado usando
   * duas casas decimais.
   * 
   * @param args
   *          Número que será formatado
   * 
   * @return Uma string com um número formatado.
   */
  private static String fmtFloat(float args) {

    java.util.Formatter fmt = new java.util.Formatter();

    String formattedString = fmt.format("%.2f", args).toString();

    return formattedString;

  }

  /**
   * Responsável pelo disparo da captura de uma foto.
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

        // tira uma foto (dispara o obturador da máquina).
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
  // TODO habilitar o auto focus da câmera
  //
  public void onAutoFocus(boolean success, Camera camera) {

    Log.d(TAG, "ConfiguraAutoFocus.onAutoFocurs() - sucess: " + success + ", camera: " + camera);

  }

}
