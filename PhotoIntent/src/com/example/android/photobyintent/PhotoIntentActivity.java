package com.example.android.photobyintent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

/**
 * PhotoIntentActivity
 * 
 * @author maurocl
 * 
 */
public class PhotoIntentActivity extends Activity {

  private static final int ACTION_TAKE_PHOTO_B = 1; // B=Big

  private static final int ACTION_TAKE_PHOTO_S = 2; // S=Small

  private static final int ACTION_TAKE_VIDEO = 3;

  private static final String BITMAP_STORAGE_KEY = "viewbitmap";

  private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";

  private ImageView mImageView;

  private Bitmap mImageBitmap;

  private static final String VIDEO_STORAGE_KEY = "viewvideo";

  private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";

  private VideoView mVideoView;

  private Uri mVideoUri;

  private String mCurrentPhotoPath;

  private static final String JPEG_FILE_PREFIX = "IMG_";

  private static final String JPEG_FILE_SUFFIX = ".jpg";

  private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

  /* Photo album for this application */
  private String getAlbumName() {

    return getString(R.string.album_name);
  }

  /**
   * File getAlbumDir()
   * 
   * @return
   */
  private File getAlbumDir() {

    File storageDir = null;

    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

      storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

      if (storageDir != null) {

        if (!storageDir.mkdirs()) {

          if (!storageDir.exists()) {

            Log.d("CameraSample", "failed to create directory");
            return null;

          }

        }

      }

    } else {
      Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
    }

    return storageDir;

  }

  /**
   * File createImageFile()
   * 
   * @return uma referência a um arquivo com o nome formado pelo prefixo +
   *         timeStamp + sufixo
   * 
   * @throws IOException
   *           Exceção lançada no caso de erro
   */
  private File createImageFile() throws IOException {

    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

    // nome do arquivo que irá armazenar a imagem
    String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";

    // obter o diretório onde o album de fotos será armazenado
    File albumF = getAlbumDir();

    // cria um arquivo temporário para gravação do arquivo
    File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);

    // um referência ao arquivo criado
    return imageF;

  }

  /**
   * setUpPhotoFile()
   * 
   * @return uma referência ao arquivo
   * 
   * @throws IOException
   * 
   */
  private File setUpPhotoFile() throws IOException {

    File f = createImageFile();

    mCurrentPhotoPath = f.getAbsolutePath();

    return f;

  }

  /**
   * setPic()
   * 
   * redimensiona a foto de acordo com o tamanho do objeto onde ela será exibida
   * 
   */
  private void setPic() {

    /* There isn't enough memory to open up more than a couple camera photos */
    /* So pre-scale the target bitmap into which the file is decoded */

    /* Get the size of the ImageView */
    int targetW = mImageView.getWidth();
    int targetH = mImageView.getHeight();

    /* Get the size of the image */
    BitmapFactory.Options bmOptions = new BitmapFactory.Options();

    // altera o parâmetro
    bmOptions.inJustDecodeBounds = true;

    // cria um bitmap a partir do arquivo onde a foto está armazenada
    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

    int photoW = bmOptions.outWidth; // largura da foto
    int photoH = bmOptions.outHeight; // altura da foto

    /* Figure out which way needs to be reduced less */
    int scaleFactor = 1;

    if ((targetW > 0) || (targetH > 0)) {
      scaleFactor = Math.min(photoW / targetW, photoH / targetH);
    }

    /* Set bitmap options to scale the image decode target */

    // altera o parâmetro
    bmOptions.inJustDecodeBounds = false;

    bmOptions.inSampleSize = scaleFactor;
    bmOptions.inPurgeable = true;

    /* Decode the JPEG file into a Bitmap */
    // decodifica o arquivo JPEG em um bitmap 
    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

    /* Associate the Bitmap to the ImageView */
    mImageView.setImageBitmap(bitmap);

    // limpa a referência a vídeo
    mVideoUri = null;

    // torna a foto visível 
    mImageView.setVisibility(View.VISIBLE);

    // torna o vídeo invisível
    mVideoView.setVisibility(View.INVISIBLE);

  }

  /**
   * galleryAddPic()
   * 
   * Adiciona a foto (Picture) a base de dados do Media Provider tornando-a
   * disponível na aplicação Android Gallery e para outras aplicações
   * 
   */
  private void galleryAddPic() {

    // cria uma intent para buscar arquivos para galeria
    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");

    // arquivo corrente com a foto armazenada
    File f = new File(mCurrentPhotoPath);

    // cria uma URI com o diretório
    Uri contentUri = Uri.fromFile(f);

    // Fornece a URI a Intent
    mediaScanIntent.setData(contentUri);

    // envia uma mensagem de broadcast com a intent criada
    this.sendBroadcast(mediaScanIntent);

  }

  /**
   * dispatchTakePictureIntent(int actionCode)
   * 
   * Invoca (dispara) a intent para capturar uma foto.
   * 
   * A foto capturada depende do actionCode fornecido.
   * 
   * O padrão é capturar uma foto Small (pequena, um thumbnail) Se o actionCode
   * ACTION_TAKE_PHOTO_B for fornecido então uma imagem normal será retornada.
   * 
   * 
   * @param actionCode
   *          código da ação que será executada e tratada
   * 
   */
  private void dispatchTakePictureIntent(int actionCode) {

    // cria uma intent com a inteção de tirar uma foto (capturar uma imagem)
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    switch (actionCode) {

      case ACTION_TAKE_PHOTO_B: // B=Big

        File f = null;

        try {

          // obtém um nome de arquivo
          f = setUpPhotoFile(); // obtém um nome de arquivo

          // atualiza a variável com o nome completo do arquivo onde a foto será armazenada
          mCurrentPhotoPath = f.getAbsolutePath();

          // fornece um "extra" indicando a URI com o nome do arquivo onde a foto será armazenada
          takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

        } catch (IOException e) {
          // caso haja alguma exceção de IO ...
          e.printStackTrace();
          // limpa a referência do arquivo
          f = null;
          // limpa o nome atual do arquivo
          mCurrentPhotoPath = null;
        }

        break;

      default:
        break;

    } // switch

    // inicia a activity a partir da intent takePictureIntent com o actionCode
    startActivityForResult(takePictureIntent, actionCode);

  }

  /**
   * dispatchTakeVideoIntent()
   * 
   * Invoca (dispara) a intent para capturar um vídeo.
   * 
   */
  private void dispatchTakeVideoIntent() {

    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

    startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);

  }

  /**
   * handleSmallCameraPhoto(Intent intent)
   * 
   * Trata o resultado da execução da intent de foto Small (S)
   * 
   * @param intent
   *          Intent retornada pela aplicação externa
   * 
   */
  private void handleSmallCameraPhoto(Intent intent) {

    // obtem os parâmetros retornados pela intent
    Bundle extras = intent.getExtras();

    // a chave "data" possui um pequeno Bitmap (um thumbnail da imagem)
    mImageBitmap = (Bitmap) extras.get("data");

    // exibe a imagem
    mImageView.setImageBitmap(mImageBitmap);

    // "anula" a referência ao vídeo
    mVideoUri = null;

    // torna visível o componente que exibe a foto
    mImageView.setVisibility(View.VISIBLE);

    // torna invisível o componente que exibe o vídeo
    mVideoView.setVisibility(View.INVISIBLE);

  }

  /**
   * handleBigCameraPhoto()
   * 
   * Trata o resultado da execução da foto original, isto é, do tamanho real
   * 
   * Trata o resultado da execução da intent de foto Big (B)
   * 
   */
  private void handleBigCameraPhoto() {

    // se o arquivo não for vazio ... 
    if (mCurrentPhotoPath != null) {
      setPic(); // redimensiona a imagem
      galleryAddPic(); // adiciona a imagem a galeria de fotos
      mCurrentPhotoPath = null; // limpa o caminho com o nome do arquivo que guardou a foto
    }

  }

  /**
   * handleCameraVideo(Intent intent)
   * 
   * Trata o resultado da execução da intent de captura de vídeo.
   * 
   * @param intent
   *          Intent retornada após execução da activity.
   * 
   */
  private void handleCameraVideo(Intent intent) {

    mVideoUri = intent.getData();

    mVideoView.setVideoURI(mVideoUri);

    mImageBitmap = null;

    // torna o vídeo visível
    mVideoView.setVisibility(View.VISIBLE);

    // torna a imagem invisível
    mImageView.setVisibility(View.INVISIBLE);

  }

  /**
	 * 
	 */
  Button.OnClickListener mTakePicOnClickListener = new Button.OnClickListener() {

    @Override
    public void onClick(View v) {

      // dispara intent para captura de uma foto Big (B)
      dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
    }
  };

  /**
	 * 
	 */
  Button.OnClickListener mTakePicSOnClickListener = new Button.OnClickListener() {

    @Override
    public void onClick(View v) {

      // dispara intent para captura de uma foto Small (S)
      dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
    }
  };

  /**
	 * 
	 */
  Button.OnClickListener mTakeVidOnClickListener = new Button.OnClickListener() {

    @Override
    public void onClick(View v) {

      //  dispara intent para captura de um vídeo
      dispatchTakeVideoIntent();
    }
  };

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);

    mImageView = (ImageView) findViewById(R.id.imageView1);
    mVideoView = (VideoView) findViewById(R.id.videoView1);

    mImageBitmap = null;
    mVideoUri = null;

    Button picBtn = (Button) findViewById(R.id.btnIntend);

    setBtnListenerOrDisable(picBtn, mTakePicOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);

    Button picSBtn = (Button) findViewById(R.id.btnIntendS);
    setBtnListenerOrDisable(picSBtn, mTakePicSOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);

    Button vidBtn = (Button) findViewById(R.id.btnIntendV);
    setBtnListenerOrDisable(vidBtn, mTakeVidOnClickListener, MediaStore.ACTION_VIDEO_CAPTURE);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
      mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
    } else {
      mAlbumStorageDirFactory = new BaseAlbumDirFactory();
    }
  }

  /**
   * onActivityResult(int requestCode, int resultCode, Intent data)
   * 
   * @param requestCode
   * @param resultCode
   * @param data
   * 
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    switch (requestCode) {

      case ACTION_TAKE_PHOTO_B: { // B=Big

        if (resultCode == RESULT_OK) {
          handleBigCameraPhoto();
        }
        break;

      } // ACTION_TAKE_PHOTO_B

      case ACTION_TAKE_PHOTO_S: { // S=Small

        if (resultCode == RESULT_OK) {
          handleSmallCameraPhoto(data);
        }
        break;

      } // ACTION_TAKE_PHOTO_S

      case ACTION_TAKE_VIDEO: { // Video

        if (resultCode == RESULT_OK) {
          handleCameraVideo(data);
        }
        break;
      } // ACTION_TAKE_VIDEO

    } // switch

  }

  /**
   * onSaveInstanceState(Bundle outState)
   */
  // Some lifecycle callbacks so that the image can survive orientation change
  @Override
  protected void onSaveInstanceState(Bundle outState) {

    outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
    outState.putParcelable(VIDEO_STORAGE_KEY, mVideoUri);

    outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null));
    outState.putBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY, (mVideoUri != null));

    super.onSaveInstanceState(outState);

  }

  /**
   * onRestoreInstanceState(Bundle savedInstanceState)
   */
  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {

    super.onRestoreInstanceState(savedInstanceState);

    mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
    mVideoUri = savedInstanceState.getParcelable(VIDEO_STORAGE_KEY);

    mImageView.setImageBitmap(mImageBitmap);

    mImageView.setVisibility(savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ? ImageView.VISIBLE
        : ImageView.INVISIBLE);

    mVideoView.setVideoURI(mVideoUri);

    mVideoView.setVisibility(savedInstanceState.getBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY) ? ImageView.VISIBLE
        : ImageView.INVISIBLE);

  }

  /**
   * isIntentAvailable(Context context, String action)
   * 
   * Indicates whether the specified action can be used as an intent. This
   * method queries the package manager for installed packages that can respond
   * to an intent with the specified action. If no suitable package is found,
   * this method returns false.
   * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
   * 
   * @param context
   *          The application's environment.
   * 
   * @param action
   *          The Intent action to check for availability.
   * 
   * @return True if an Intent with the specified action can be sent and
   *         responded to, false otherwise.
   */
  public static boolean isIntentAvailable(Context context, String action) {

    final PackageManager packageManager = context.getPackageManager();
    final Intent intent = new Intent(action);

    List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

    return list.size() > 0;

  }

  /**
   * setBtnListenerOrDisable
   * 
   * Associa um tratador de eventos ao botão o desabilita-o
   * 
   * @param btn
   *          referência ao botão
   * @param onClickListener
   *          referência ao tratador de eventos
   * @param intentName
   *          intent que será verificada
   * 
   */
  private void setBtnListenerOrDisable(Button btn, Button.OnClickListener onClickListener, String intentName) {

    if (isIntentAvailable(this, intentName)) {

      // há pelo menos uma activity disponível para o tratamento da intent 

      // estabelece o tratador de eventos
      btn.setOnClickListener(onClickListener);

    } else {

      // não há uma activity disponível para o tratamento da intent      

      btn.setText(getText(R.string.cannot).toString() + " " + btn.getText());

      // desabilita o botão (pois a função não está disponível)
      btn.setClickable(false);

    }

  }

}