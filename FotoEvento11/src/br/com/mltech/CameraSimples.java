package br.com.mltech;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * CameraSimples
 * 
 * @author maurocl
 * 
 */
public class CameraSimples extends Activity {

  public static final String TAG = "CameraSimples";

  public static final int TIRA_FOTO = 111;

  private ImageView image;
  private File file;
  private Uri outputFileUri;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    Log.d(TAG, "*** onCreate() ***");

    setContentView(R.layout.camerasimples);

    Button btnConfirma = (Button) findViewById(R.id.btnConfirma);

    image = (ImageView) findViewById(R.id.imageView1);

    btnConfirma.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        /*
        String arquivo = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg";

        Log.d(TAG, "*** onClick() ***");

        file = new File(arquivo);

        Log.d(TAG, "arquivo=" + file.getAbsolutePath());

        outputFileUri = Uri.fromFile(file);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        Log.d(TAG, "*** before ***");
        startActivityForResult(intent, TIRA_FOTO);
        Log.d(TAG, "*** after ***");
        */

      }
    });

  }

  /**
   * 
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    Log.d(TAG, "requestCode=" + requestCode + ", resultCode=" + resultCode);

    Log.d(TAG, "data=" + ((data == null) ? "null" : "not null"));

    if (requestCode == TIRA_FOTO) {

      if (resultCode != RESULT_OK) {
        // operação cancelada
        Log.w(TAG, "Operação cancelada pelo usuário");
      }

      carregaImagem();

    
      
      
    }

  }

  /**
   * 
   */
  private void carregaImagem() {

    Log.d(TAG, "carregaImage()");

    if (file != null) {

      Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());

      if (bm != null) {

        bm = Bitmap.createScaledBitmap(bm, 100, 100, true);

        image.setImageBitmap(bm);

      } else {
        Log.d(TAG, "Bitmap gerado é nulo");
      }

    } else {
      Log.w(TAG, "==> file is null");
    }

  }

  /**
   * 
   */
  private void salvaImagem() {

    Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());

  }

  private boolean gravaArquivo(byte[] data, String nomeArquivo) {

    /*********************************************************************************************
     * Quando o telefone está conectado e em testes ele não permite montar o
     * cartão de mem´roia
     ********************************************************************************************/

    boolean b = false;

    FileOutputStream fos = null;

    File imageFile = null;

    File picsDir = null;

    try {

      try {

        Log.d(TAG, "**** getExternalStorageState()=" + Environment.getExternalStorageState());

        // if (Environment.getExternalStorageState() ==
        // Environment.MEDIA_MOUNTED) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

          if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {

            Log.d(TAG, "MEDIA montada porém está ReadOnly");
          } else {
            Log.d(TAG, "MEDIA montada RW");
          }

          // cria uma arquivo para armazernar a foto
          imageFile = new File(picsDir, nomeArquivo);

          fos = new FileOutputStream(imageFile);
          fos.write(data);

          Log.d(TAG, "Arquivo: " + imageFile.getName() + " foi gerado e ocupa " + imageFile.length() + " bytes");

          b = true;

        } else {
          Log.w(TAG, "MEDIA não está montada");
        }

      } catch (FileNotFoundException e) {

        Log.d(TAG, "File not found exception: " + imageFile.getName());

      } catch (IOException e) {

        Log.d(TAG, "IOException: " + imageFile.getName());

      } catch (Exception e) {

        Log.d(TAG, "Falha na gravação do arquivo: " + imageFile.getName());

      } finally {

        if (fos != null) {
          fos.close();
        }

      }

    } catch (IOException e) {
      Log.d(TAG, "IOException", e);
    }

    return b;

  }

}
