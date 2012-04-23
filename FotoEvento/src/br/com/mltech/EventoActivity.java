package br.com.mltech;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * EventoActivity.java
 * 
 * @author maurocl
 *
 * Activity para criação de um evento
 * 
 */
public class EventoActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.evento);

		getDisplayInfo();

		// Obtendo os campos da tela
		TextView cliente = (TextView) findViewById(R.evento.editCliente);
		TextView nome = (TextView) findViewById(R.evento.editNome);

		cliente.setText("Nome do Cliente");

		nome.setText("Show RPM");

		// display.getSize(size);
		// int width = size.x;
		// int height = size.y;

	}

	private void getDisplayInfo() {
		Display display = getWindowManager().getDefaultDisplay();

		Log.i("Evento", "DisplayId()=" + display.getDisplayId());
		Log.i("Evento", "getHeight()=" + display.getHeight());
		Log.i("Evento", "getWidth()=" + display.getWidth());
	}

	//------------------------------------
	// Criação de Menu da Activity Evento
	//------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// TODO Auto-generated method stub

		// Menu Item: Upload (executa o upload das bordas do evento)
		MenuItem upload = menu.add(0, 0, 0, "Upload");
		upload.setIcon(R.drawable.ic_launcher);
		upload.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				Toast.makeText(EventoActivity.this, "Upload de Bordas", Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		return super.onCreateOptionsMenu(menu);
	}

}
