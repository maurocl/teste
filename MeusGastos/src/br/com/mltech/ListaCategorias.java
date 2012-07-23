package br.com.mltech;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import br.com.mltech.dao.CategoriaDAO;
import br.com.mltech.modelo.Categoria;

/**
 * ListaCategorias.java
 * 
 * @author maurocl
 * 
 *         Exibe uma lista de categorias de gastos.
 * 
 */
public class ListaCategorias extends Activity {

	private List<Categoria> categorias;

	private ListView lista;

	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.listacat);

		lista = (ListView) findViewById(R.id.lista);

		// Inicializa a lista de categorias de gasto
		carregaLista();

		// -------------------------------------------
		// Tratamento de eventos para click na lista
		// -------------------------------------------
		lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View view,
					int posicao, long idDoObjeto) {

				Toast.makeText(ListaCategorias.this,
						"Você clicou no item " + posicao, Toast.LENGTH_LONG)
						.show();

			}

		});

		// -----------------------------------------------
		// Tratamento de evento para click longo na lista
		// -----------------------------------------------
		lista.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> adapter, View view,
					int posicao, long id) {

				Toast.makeText(ListaCategorias.this,
						"Você clicou longamente no item " + posicao,
						Toast.LENGTH_LONG).show();

				return false;
			}

		});
	}

	/**
	 * Menu de Opções
	 * 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem categorias = menu.add(0, 0, 0, "Novo");
		categorias.setIcon(R.drawable.ic_launcher);

		categorias.setIntent(new Intent(this, FormCategoria.class));

		// -----------------
		// Menu Item: Novo
		// -----------------
		categorias.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Toast.makeText(ListaCategorias.this, "Novo", Toast.LENGTH_SHORT)
						.show();
				return false;
			}

		});

		return super.onCreateOptionsMenu(menu);

	}

	/**
	 * o onResume é executado quando a Activity anterior é fechada
	 * 
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		carregaLista();
	}

	/**
	 * carregaLista()
	 */
	private void carregaLista() {

		CategoriaDAO dao = new CategoriaDAO(this);
		categorias = dao.getLista();
		dao.close();

		ArrayAdapter<Categoria> adapter = new ArrayAdapter<Categoria>(this,
				android.R.layout.simple_list_item_1, categorias);

		lista.setAdapter(adapter);

	}
}
