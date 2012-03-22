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

		// String[] categorias = new String[] { "Combustível", "Alimentação",
		// "Saúde", "Gerais" };

		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, categorias);

		// ListView lista = (ListView) findViewById(R.id.lista);

		lista = (ListView) findViewById(R.id.lista);

		/*
		 * CategoriaDAO dao = new CategoriaDAO(this); categorias = dao.getLista();
		 * dao.close();
		 * 
		 * ArrayAdapter<Categoria> adapter = new ArrayAdapter<Categoria>(this,
		 * android.R.layout.simple_list_item_1, categorias);
		 * 
		 * lista.setAdapter(adapter);
		 */

		carregaLista();

		// -------------------------------------------
		// Tratamento de eventos para click na lista
		// -------------------------------------------
		lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View view, int posicao, long idDoObjeto) {

				Toast.makeText(ListaCategorias.this, "Você clicou no item " + posicao, Toast.LENGTH_LONG).show();

				// Intent altera = new Intent(ListaAlunos.this, Formulario.class);

				// altera.putExtra("alunoSelecionado",(Aluno)
				// adapterView.getItemAtPosition(posicao));

				// startActivity(altera);

			}

		});

		// -----------------------------------------------
		// Tratamento de evento para click longo na lista
		// -----------------------------------------------
		lista.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> adapter, View view, int posicao, long id) {

				Toast.makeText(ListaCategorias.this, "Você clicou longamente no item " + posicao, Toast.LENGTH_LONG).show();
				return false;
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem categorias = menu.add(0, 0, 0, "Novo");
		categorias.setIcon(R.drawable.ic_launcher);

		categorias.setIntent(new Intent(this, FormCategoria.class));

		categorias.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Toast.makeText(ListaCategorias.this, "Novo", Toast.LENGTH_SHORT).show();
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

		ArrayAdapter<Categoria> adapter = new ArrayAdapter<Categoria>(this, android.R.layout.simple_list_item_1, categorias);

		lista.setAdapter(adapter);

	}
}
