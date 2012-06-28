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
 * Uma classe básica para pre-visualização da imagem capturada pela câmera
 * 
 * @author maurocl
 * 
 * 
 *         SurfaceView. SurfaceHolder.Callback SurfaceHolder
 * 
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

  private static final String TAG = "CameraPreview";

  // é inicializado no construtor e testado quando da mudança do preview
  private static SurfaceHolder mHolder;

  // instância de um objeto Camera
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
   *          Instância da Camera
   * 
   */
  public CameraPreview(Context context, Camera camera) {

    super(context);

    // atualiza a câmera corrente
    mCamera = camera;

    // obtem acesso a surface (superfície)
    //
    // Return the SurfaceHolder providing access and control over this SurfaceView's underlying surface.
    //
    // Retorna uma instância do objeto SurfaceHolder que provê acesso e controle sobre
    // essa superfície sob a SurfaceView 
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

    // SurfaceHolder é uma interface abstrata para alguém mantendo sob controle uma superfície de exibição
    // Permite que você controle o tamanho e formato da superfície, editar os pixels na superfície e 
    // monitorar mudanças na superfície
    // Essa interface é tipicamente disponível através da classe SurfaceView
    // Quando usar essa interface de uma thread outra da qual aquela rodando a SurfaceView, você irá querer
    // ler cuidadosamente os métodos lockCanvas() e Callback.surfaceCreated().

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
   * Esse método é chamado imediatamente após a primeira criação da superfície
   * 
   * the surface has been created, adquired the camera and tell it where to draw
   * 
   * @param holder
   *          o SurfaceHolder cuja superfície (surface) está sendo criada
   * 
   */
  public void surfaceCreated(SurfaceHolder holder) {

    Log.d(TAG, "surfaceCreated() ...");

    try {

      // estabelece o display para visualização do preview da câmera

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

      // inicia a captura e desenho dos quadros de preview (pré-visualização na tela)
      // a pré-visualização não irá iniciar realmente até que a superfície (surface) seja suprida com os
      // métodos setPreviewDisplay(SurfaceHolder) ou setPreviewTexture(SurfaceTexture). 
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
   * Método chamado imediatamente antes da superficie ser destruída. Após essa
   * chamada você não deverá mais tentar acesso a essa superfície.
   * 
   * Se você possuir uma thread de rendering (desenho) que acessa diretamente a
   * superfície, você deverá garantir que a thread não esteja mais atuando sobre
   * (tocando) a superfície antes de retornar dessa função.
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
   *          o SurfaceHolder, isto é, a superfície mantida sob contoele, cuja
   *          superfície (surface) está sendo destruída
   * 
   */
  public void surfaceDestroyed(SurfaceHolder holder) {

    // empty. Take care of releasing the camera preview in your activity
    // método vazio. Tome cuidado em liberar a preview da câmera em sua activity

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
   * Esse método é chamado após alguma mudança estrutural, isto é, no formato ou
   * tamanho, feito na superfície (surface). É nesse ponto que devemos atualizar
   * a imagem na superfície. Esse método é chamado pelo menos uma vez depois do
   * SurfaceCreated.
   * 
   * @param holder
   *          a superfície mantida sob controle cuja superfície mudou
   *          (alterou-se) (superfície de exibição)
   * 
   * @param format
   *          o novo PixelFormat (formato do pixel) da superfície (surface) -
   *          veja classe PixelFormat
   * 
   * @param width
   *          a nova largura da superfície (surface)
   * 
   * @param height
   *          a nova altura da superfí
   *          cie (surface)
   * 
   */
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    // TODO a grande dúvida aqui é saber se uso a variável holder ou mHolder

    Log.w(TAG, "holder=" + holder + ", mHolder=" + mHolder);
    showSurfaceHolder(holder, "holder");
    showSurfaceHolder(mHolder, "mHolder");

    if (mHolder.getSurface() == null) {

      // preview surface does not exist
      // superfície de pre-visualização não existe
      Log.d(TAG, "surfaceChanged() - getSurface() é  nula");
      return;

    } else {

      // PixelFormat 4 = RGB_565
      Log.d(TAG, "surfaceChanged() - format: " + format + ", w=" + width + ", h=" + height);

    }

  }

  /**
   * showSurfaceHolder(SurfaceHolder sh)
   * 
   * @param sh uma instância de SurfaceHolder
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
