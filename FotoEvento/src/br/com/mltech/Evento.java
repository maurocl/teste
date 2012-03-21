package br.com.mltech;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

public class Evento extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.evento);

		Display display = getWindowManager().getDefaultDisplay();
		
		
		Log.i("","DisplayId()="+display.getDisplayId());
		Log.i("","getHeight()="+display.getHeight());
		Log.i("","getWidth()="+display.getWidth());
		
		//display.getSize(size);
		//int width = size.x;
		//int height = size.y;

	}

}
