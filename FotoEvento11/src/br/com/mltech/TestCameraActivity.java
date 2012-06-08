package br.com.mltech;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import br.com.mltech.utils.FileUtils;

/**
 * TestCameraActivity
 * 
 * @author maurocl
 *
 */
public class TestCameraActivity extends Activity {

	private static final String TAG = "TestCameraActivity";

	private static Bitmap mImageBitmap;
	
	private static ImageView mImageView;
	
	private static Uri mUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Log.d(TAG, "*** onCreate() ***");

		setContentView(R.layout.testcameraprev);

		Button btnOk = (Button) findViewById(R.id.btnOk);
		Button btnCancelar = (Button) findViewById(R.id.btnCancelar);
		
		mImageView = (ImageView) findViewById(R.id.imageView); 

		// tratamento do bot�o Ok
		btnOk.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
			
				Log.d(TAG, "btnOk - bot�o ok");

				// cria uma intent para execu��o da CameraActivity
			  Intent i = new Intent(getBaseContext(),CameraActivity.class);
				
			  // inicia a activity
				startActivityForResult(i, 100);

			}
			
		});

		// tratamento do bot�o Cancelar
		btnCancelar.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
			
				Log.d(TAG, "btnCancelar - bot�o cancelar");
				finish();
			}
		});

	}

	/**
	 * onActivityResult(int requestCode, int resultCode, Intent data)
	 * 
	 * @param
	 * @param
	 * @param
	 * 
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);

		Log.i(TAG, "onActivityResult() - requestCode: " + requestCode + ", resultCode: " + ", data: " + data);

		switch (requestCode) {
		case 100:
			
			if(resultCode==RESULT_OK) {
				
				Log.d(TAG, "onActivityResult() - resultado: OK");
				
				if(data!=null) {
					
					Bundle extras = data.getExtras();
					
					mUri =(Uri) extras.get("data");
					
					FileUtils.showBundle(extras);
					
					if(mImageBitmap==null) {
						Log.w(TAG, "onActivityResult() - mImageBitmap � nulo");
					}
					else {
						Log.d(TAG, "onActivityResult() - mImageBitmap � possui um bitmap");
					}
					
					if(mUri==null) {
						Log.w(TAG, "onActivityResult() - mUri � nulo");
					}
					else {
						Log.d(TAG, "onActivityResult() - mUri: "+mUri);
					}
					
				}
				else {
					Log.w(TAG, "onActivityResult() - data � nulo");
				}
				
				// Atualiza a imagem
				//mImageView.setImageBitmap(mImageBitmap);
				
				// exibe a imagem a partir de um Uri
				mImageView.setImageURI(mUri);
				
				
			}else if(resultCode==RESULT_CANCELED) {
				Log.w(TAG, "onActivityResult() - resultado: CANCELED");
			}
			else {
				Log.w(TAG, "onActivityResult() - resultado: "+resultCode+" � desconhecido.");
			}
			
			break;
		default:
			Log.w(TAG, "onActivityResult() - requestCode: "+requestCode+" � desconhecido.");
			break;
		}

	}

	/**
	 * onStart(2)
	 */
	@Override
	protected void onStart() {

		super.onStart();

		Log.d(TAG, "*** onStart() ***");

	}

	/**
	 * onResume(3)
	 * 
	 * Esse callback � chamado a partir da sequencia: onCreate() --> onStart() -->
	 * onResume() ou ap�s ( a aplica��o estar no estado Pause e retorna a
	 * funcionar) onPause() --> on Resume().
	 * 
	 */
	@Override
	protected void onResume() {

		super.onResume();

		Log.d(TAG, "*** onResume() ***");

	}

	/**
	 * onPause(4)
	 * 
	 * Activity foi colocada em pausa
	 * 
	 */
	@Override
	protected void onPause() {

		super.onPause();

		Log.d(TAG, "*** onPause() ***");

	}

	/**
	 * onStop()
	 */
	@Override
	protected void onStop() {

		super.onStop();
		Log.d(TAG, "*** onStop() ***");

	}

	/**
	 * onRestart()
	 * 
	 * Aplica��o foi reinicializada.
	 * 
	 * � executado ap�s um onStop()
	 * 
	 */
	@Override
	protected void onRestart() {

		super.onRestart();

		Log.d(TAG, "*******************");
		Log.d(TAG, "*** onRestart() ***");
		Log.d(TAG, "*******************");
	}

	/**
	 * onDestroy();
	 */
	@Override
	protected void onDestroy() {

		super.onDestroy();

		Log.d(TAG, "*** onDestroy() ***");

	}

	/**
	 * onSaveInstanceState(Bundle outState)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);

		Log.d(TAG, "*** onSaveInstanceState() ***");

	}

	/**
	 * onRestoreInstanceState(Bundle savedInstanceState)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);

		Log.d(TAG, "*** onRestoreInstanceState() ***");

	}

}
