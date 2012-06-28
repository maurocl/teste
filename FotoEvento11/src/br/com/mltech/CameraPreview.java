package br.com.mltech;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * CameraPreview
 * 
 * A basic Camera preview class
 * 
 * Uma classe b�sica para pre-visualiza��o da imagem capturada pela c�mera
 * 
 * @author maurocl
 * 
 * 
 *         SurfaceView. SurfaceHolder.Callback SurfaceHolder
 * 
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

  private static final String TAG = "CameraPreview";

  // � inicializado no construtor e testado quando da mudan�a do preview
  private static SurfaceHolder mHolder;

  // inst�ncia de um objeto Camera
  private static Camera mCamera;

  /**
   * CameraPreview(Context context, Camera camera)
   * 
   * Construtor
   * 
   * @param context
   *          Interface to global information about an application environment.
   * 
   * @param camera
   *          Inst�ncia da Camera
   * 
   */
  public CameraPreview(Context context, Camera camera) {

    super(context);

    // atualiza a c�mera corrente
    mCamera = camera;

    // obtem acesso a surface (superf�cie)
    //
    // Return the SurfaceHolder providing access and control over this SurfaceView's underlying surface.
    //
    // Retorna uma inst�ncia do objeto SurfaceHolder que prov� acesso e controle sobre
    // essa superf�cie sob a SurfaceView 
    //
    // SurfaceHolder: The holder of the surface.
    //    
    mHolder = this.getHolder();

    // SurfaceHolder is an abstract interface to someone holding a display surface.
    // Allows you to control the surface size and format, edit the pixels in the surface, 
    //and monitor changes to the surface. 
    // This interface is typically available through the SurfaceView class.
    // When using this interface from a thread other than the one running its SurfaceView, you will want to 
    // carefully read the methods lockCanvas() and Callback.surfaceCreated(). 

    // SurfaceHolder � uma interface abstrata para algu�m mantendo sob controle uma superf�cie de exibi��o
    // Permite que voc� controle o tamanho e formato da superf�cie, editar os pixels na superf�cie e 
    // monitorar mudan�as na superf�cie
    // Essa interface � tipicamente dispon�vel atrav�s da classe SurfaceView
    // Quando usar essa interface de uma thread outra da qual aquela rodando a SurfaceView, voc� ir� querer
    // ler cuidadosamente os m�todos lockCanvas() e Callback.surfaceCreated().

    // adiciona o tratator de eventos
    // Add a Callback interface for this holder. 
    // There can several Callback interfaces associated with a holder.
    mHolder.addCallback(this);

    mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    Log.d(TAG, "CameraPreview() - inicializada");

  }

  /**
   * surfaceCreated(SurfaceHolder holder)
   * 
   * Esse m�todo � chamado imediatamente ap�s a primeira cria��o da superf�cie
   * 
   * the surface has been created, adquired the camera and tell it where to draw
   * 
   * @param holder
   *          o SurfaceHolder cuja superf�cie (surface) est� sendo criada
   * 
   */
  public void surfaceCreated(SurfaceHolder holder) {

    Log.d(TAG, "surfaceCreated() ...");

    try {

      // estabelece o display para visualiza��o do preview da c�mera

      // containing the Surface on which to place the preview, or null to remove
      // the preview surface

      // Sets the Surface to be used for live preview. 
      // Either a surface or surface texture is necessary for preview, and preview 
      // is necessary to take pictures. 

      // The same surface can be re-set without harm. 
      // Setting a preview surface will un-set any preview surface texture that 
      // was set via setPreviewTexture(SurfaceTexture). 
      mCamera.setPreviewDisplay(holder);

      // inicia o preview
      // Starts capturing and drawing preview frames to the screen. 
      // Preview will not actually start until a surface is supplied with 
      // setPreviewDisplay(SurfaceHolder) or setPreviewTexture(SurfaceTexture). 

      // inicia a captura e desenho dos quadros de preview (pr�-visualiza��o na tela)
      // a pr�-visualiza��o n�o ir� iniciar realmente at� que a superf�cie (surface) seja suprida com os
      // m�todos setPreviewDisplay(SurfaceHolder) ou setPreviewTexture(SurfaceTexture). 
      mCamera.startPreview();

    } catch (IOException e) {

      // if the method fails (for example, if the surface is unavailable or
      // unsuitable).
      Log.w(TAG, "surfaceCreated() - IOException - Error setting camera preview: " + e.getMessage());

    } catch (Exception e) {

      Log.w(TAG, "surfaceCreated() - Exception - Error setting camera preview: ", e);

    }

  }

  /**
   * surfaceDestroyed(SurfaceHolder holder)
   * 
   * M�todo chamado imediatamente antes da superficie ser destru�da. Ap�s essa
   * chamada voc� n�o dever� mais tentar acesso a essa superf�cie.
   * 
   * Se voc� possuir uma thread de rendering (desenho) que acessa diretamente a
   * superf�cie, voc� dever� garantir que a thread n�o esteja mais atuando sobre
   * (tocando) a superf�cie antes de retornar dessa fun��o.
   * 
   * 
   * This is called immediately before a surface is being destroyed.
   * 
   * After returning from this call, you should no longer try to access this
   * surface.
   * 
   * If you have a rendering thread that directly accesses the surface, you must
   * ensure that thread is no longer touching the Surface before returning from
   * this function.
   * 
   * Parameters holder The SurfaceHolder whose surface is being destroyed.
   * 
   * 
   * @param holder
   *          o SurfaceHolder, isto �, a superf�cie mantida sob contoele, cuja
   *          superf�cie (surface) est� sendo destru�da
   * 
   */
  public void surfaceDestroyed(SurfaceHolder holder) {

    // empty. Take care of releasing the camera preview in your activity
    // m�todo vazio. Tome cuidado em liberar a preview da c�mera em sua activity

    Log.d(TAG, "surfaceDestroyed()");

  }

  /**
   * This is called immediately after any structural changes (format or size)
   * have been made to the surface.
   * 
   * You should at this point update the imagery in the surface. This method is
   * always called at least once, after surfaceCreated(SurfaceHolder).
   * 
   * Parameters holder The SurfaceHolder whose surface has changed. format The
   * new PixelFormat of the surface. width The new width of the surface. height
   * The new height of the surface.
   */

  /**
   * surfaceChanged(SurfaceHolder holder, int format, int width, int height)
   * 
   * Esse m�todo � chamado ap�s alguma mudan�a estrutural, isto �, no formato ou
   * tamanho, feito na superf�cie (surface). � nesse ponto que devemos atualizar
   * a imagem na superf�cie. Esse m�todo � chamado pelo menos uma vez depois do
   * SurfaceCreated.
   * 
   * @param holder
   *          a superf�cie mantida sob controle cuja superf�cie mudou
   *          (alterou-se) (superf�cie de exibi��o)
   * 
   * @param format
   *          o novo PixelFormat (formato do pixel) da superf�cie (surface) -
   *          veja classe PixelFormat
   * 
   * @param width
   *          a nova largura da superf�cie (surface)
   * 
   * @param height
   *          a nova altura da superf�
   *          cie (surface)
   * 
   */
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    // TODO a grande d�vida aqui � saber se uso a vari�vel holder ou mHolder

    Log.w(TAG, "holder=" + holder + ", mHolder=" + mHolder);
    showSurfaceHolder(holder, "holder");
    showSurfaceHolder(mHolder, "mHolder");

    if (mHolder.getSurface() == null) {

      // preview surface does not exist
      // superf�cie de pre-visualiza��o n�o existe
      Log.d(TAG, "surfaceChanged() - getSurface() �  nula");
      return;

    } else {

      // PixelFormat 4 = RGB_565
      Log.d(TAG, "surfaceChanged() - format: " + format + ", w=" + width + ", h=" + height);

    }

  }

  /**
   * showSurfaceHolder(SurfaceHolder sh)
   * 
   * @param sh uma inst�ncia de SurfaceHolder
   * 
   */
  void showSurfaceHolder(SurfaceHolder sh, String msg) {

    if (sh == null) {
      return;
    }
    
    Log.d(TAG, "showSurfaceHolder() - "+msg);
    
    Log.d(TAG, "showSurfaceHolder() - sh.toString(): " + sh.toString());
    Log.d(TAG, "showSurfaceHolder() - sh.isCreating(): " + sh.isCreating());
    Log.d(TAG, "showSurfaceHolder() - sh.getSurfaceFrame(): " + sh.getSurfaceFrame());

  }

}
