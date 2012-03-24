package br.com.mltech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;

/**
 * Activity principal da aplicação
 * 
 * @author maurocl
 * 
 */
public class FotoEventoActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	/**
	 * Criação dos Menus
	 * 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// ---------------------------------------------
		// Menu: Cadastro de Usuários
		// ----------------------------------------------
		MenuItem participante = menu.add(0, 0, 0, "Participante");
		participante.setIcon(R.drawable.ic_launcher);
		
		participante.setIntent(new Intent(this,Participante.class));
	
		// ---------------------------------------------
		// Menu: Relatórios
		// ----------------------------------------------
		MenuItem relatorio = menu.add(0, 1, 0, "Relatórios");
		relatorio.setIcon(R.drawable.ic_launcher);

		relatorio.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(FotoEventoActivity.this, Relatorio.class);				
				startActivity(intent);
				return (false);
			}
		});

		// ----------------------------------------------
		// Menu: Eventos
		// ---------------------------------------------
		MenuItem evento = menu.add(0, 2, 0, "Eventos");
		evento.setIcon(R.drawable.ic_launcher);

		evento.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(FotoEventoActivity.this, Evento.class);
				startActivity(intent);
				return (false);
			}
		});

		// ----------------------------------------------
		// Menu: Enviar Foto
		// ---------------------------------------------
		MenuItem foto = menu.add(0, 3, 0, "Enviar Foto");
		foto.setIcon(R.drawable.ic_launcher);

		foto.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// Intent intent = new Intent(this, UsuarioActivity.class);
				// startActivity(intent);
				Toast.makeText(FotoEventoActivity.this, "Enviar Foto", Toast.LENGTH_LONG).show();
				return (false);
			}
		});

		return super.onCreateOptionsMenu(menu);
	}
}