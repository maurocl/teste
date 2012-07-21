package net.tygerstar.android.negocio;

import net.tygerstar.android.R;
import net.tygerstar.android.componente.ImageAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Gallery;
import android.widget.ImageButton;

public class SlideShowController extends Activity {
	
	private Gallery galeria;
	private ImageButton btFechar;
	
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.tela_slideshow);
		
		inicializarViews();
		
		galeria.setAdapter(new ImageAdapter(this));
		btFechar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void inicializarViews(){
		galeria = (Gallery) findViewById(R.slideshow.gallery);
		btFechar = (ImageButton) findViewById(R.slideshow.btVoltar);
	}
}
