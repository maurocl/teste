package net.tygerstar.android.negocio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import net.tygerstar.android.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class FotoController extends Activity {
	private ImageButton btVoltar;
	private ImageView ivFoto;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tela_foto);
		
		inicializarViews();
		
		btVoltar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		ivFoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(i, 0);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
			Bitmap x = (Bitmap) data.getExtras().get("data");
			ivFoto.setImageBitmap(x);
			ContentValues values = new ContentValues();
			Time nomefoto = new Time();
			values.put(Images.Media.TITLE, ""+nomefoto.toMillis(true));
			values.put(Images.Media.BUCKET_ID, "test");
			values.put(Images.Media.DESCRIPTION, "test Image taken");
			values.put(Images.Media.MIME_TYPE, "image/jpeg");
			Uri uri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
			OutputStream outstream;
			try {
				outstream = getContentResolver().openOutputStream(uri);

				x.compress(Bitmap.CompressFormat.JPEG, 70, outstream);
				outstream.close();
			} catch (FileNotFoundException e) {
				Toast.makeText(FotoController.this, "Caminho da imagem não encontrado.", Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				Toast.makeText(FotoController.this, "Erro ao salvar imagem.", Toast.LENGTH_LONG).show();
			}
			Toast.makeText(FotoController.this, "Imagem salva com sucesso.", Toast.LENGTH_LONG).show();
		}
	}
	
	private void inicializarViews(){
		btVoltar = (ImageButton) findViewById(R.foto.btVoltar);
		ivFoto = (ImageView) findViewById(R.foto.ivFoto);
	}
}
