package br.com.mltech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

public class FotoEventoActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// ---------------------------------------------
		MenuItem usuario = menu.add(0, 0, 0, "Usuário");
		usuario.setIcon(R.drawable.ic_launcher);

		usuario.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// Intent intent = new Intent(this, UsuarioActivity.class);
				Intent intent = new Intent(FotoEventoActivity.this, UsuarioActivity.class);
				startActivity(intent);
				return (false);
			}
		});

		// ---------------------------------------------
		MenuItem relatorio = menu.add(0, 1, 0, "Relatórios");
		relatorio.setIcon(R.drawable.ic_launcher);

		relatorio.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(FotoEventoActivity.this, Relatorio.class);
				//Intent altera = new Intent(ListaAlunos.this, Formulario.class);
				startActivity(intent);
				return (false);
			}
		});

		// ---------------------------------------------
		MenuItem evento = menu.add(0, 2, 0, "Eventos");
		evento.setIcon(R.drawable.ic_launcher);

		evento.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// Intent intent = new Intent(this, UsuarioActivity.class);
				// startActivity(intent);
				return (false);
			}
		});

		// ---------------------------------------------
		MenuItem foto = menu.add(0, 3, 0, "Enviar Foto");
		foto.setIcon(R.drawable.ic_launcher);

		foto.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// Intent intent = new Intent(this, UsuarioActivity.class);
				// startActivity(intent);
				return (false);
			}
		});

		return super.onCreateOptionsMenu(menu);
	}

}