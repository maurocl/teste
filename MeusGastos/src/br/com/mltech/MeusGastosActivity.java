package br.com.mltech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MeusGastosActivity.java
 * 
 * @author maurocl
 * 
 */
public class MeusGastosActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		final Button categorias = (Button) findViewById(R.id.categorias);

		final Button gastos = (Button) findViewById(R.id.gastos);

		final Button relatorios = (Button) findViewById(R.id.relatorios);

		final TextView texto = (TextView) findViewById(R.id.textView1);

		/*------------------*/
		/* Bot�o Categorias */
		/*------------------*/
		categorias.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.i("MeusGastosActivity", "bot�o Categorias");
				texto.setText("Categorias");
			}

		});

		/*------------------*/
		/* Bot�o Gastos */
		/*------------------*/
		gastos.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.i("MeusGastosActivity", "bot�o Gastos");
				texto.setText("Gastos");
			}

		});

		/*------------------*/
		/* Bot�o Relat�rios */
		/*------------------*/
		relatorios.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.i("MeusGastosActivity", "bot�o Relat�rios");
				texto.setText("Relat�rios");
			}

		});

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i("MeusGastosActivity", "onRestart()");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("MeusGastosActivity", "onResume()");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("MeusGastosActivity", "onDestroy()");
	}

	/**
	 * Tratamento dos menus de op��es
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		// ---------------------
		// MenuItem: Categorias
		// ---------------------
		MenuItem categorias = menu.add(0, 0, 0, "Categorias");
		categorias.setIcon(R.drawable.ic_launcher);

		categorias.setIntent(new Intent(this, ListaCategorias.class));

		categorias.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Toast.makeText(MeusGastosActivity.this, "Categorias", Toast.LENGTH_SHORT).show();
				return false;
			}

		});

		// -----------------
		// MenuItem: Gastos
		// -----------------
		MenuItem gastos = menu.add(0, 1, 0, "Gastos");
		gastos.setIcon(R.drawable.ic_launcher);
		gastos.setIntent(new Intent(this, ListaGastos.class));
		gastos.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Toast.makeText(MeusGastosActivity.this, "Gastos", Toast.LENGTH_SHORT).show();
				return false;
			}

		});

		// ---------------------
		// MenuItem: Relat�rios
		// ---------------------
		MenuItem relatorios = menu.add(0, 2, 0, "Relat�rios");
		relatorios.setIcon(R.drawable.ic_launcher);
		relatorios.setIntent(new Intent(this, FormCategoria.class));

		relatorios.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Toast.makeText(MeusGastosActivity.this, "Relat�rios", Toast.LENGTH_SHORT).show();
				return false;
			}

		});

		// ---------------------
		// MenuItem: Login
		// ---------------------
		MenuItem login = menu.add(0, 3, 0, "Login");
		login.setIcon(R.drawable.ic_launcher);
		login.setIntent(new Intent(this, Login.class));

		login.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Toast.makeText(MeusGastosActivity.this, "Login", Toast.LENGTH_SHORT).show();
				return false;
			}

		});

		return super.onCreateOptionsMenu(menu);

	}
}