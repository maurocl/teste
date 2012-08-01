
package br.com.mltech;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import br.com.mltech.dao.GastoDAO;
import br.com.mltech.modelo.Gasto;

/**
 * ListaGastos.java
 * 
 * @author maurocl
 * 
 *         Activity responsável pela listagem de todos os gastos
 * 
 */
public class ListaGastos extends Activity {

  private static final String TAG = "ListaGastos";

  private List<Gasto> gastos;

  private ListView lista;

  private Gasto gastoSelecionado;

  /**
	 * 
	 */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    //
    super.onCreate(savedInstanceState);

    setContentView(R.layout.listagastos);

    lista = (ListView) findViewById(R.id.lista);

    // Inicializa a lista de categorias de gasto
    carregaLista();

    // -------------------------------------------
    // Tratamento de eventos para click na lista
    // -------------------------------------------
    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

      public void onItemClick(AdapterView<?> adapter, View view,
          int posicao, long idDoObjeto) {

        Toast.makeText(ListaGastos.this,
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

        gastoSelecionado = (Gasto) adapter.getItemAtPosition(posicao);

        // Lista de gasto
        registerForContextMenu(lista);

        // Toast.makeText(ListaGastos.this,
        // "Você clicou longamente no item " + posicao,
        // Toast.LENGTH_LONG).show();

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

    MenuItem gastos = menu.add(0, 0, 0, "Novo");
    gastos.setIcon(R.drawable.ic_launcher);

    gastos.setIntent(new Intent(this, FormGasto.class));

    // -----------------
    // Menu Item: Novo
    // -----------------
    gastos.setOnMenuItemClickListener(new OnMenuItemClickListener() {

      @Override
      public boolean onMenuItemClick(MenuItem item) {

        Toast.makeText(ListaGastos.this, "Novo", Toast.LENGTH_SHORT)
            .show();
        return false;
      }

    });

    return super.onCreateOptionsMenu(menu);

  }

  // ------------------------------
  // Definição do Menu de Contexto
  // ------------------------------
  @Override
  public void onCreateContextMenu(ContextMenu menu, View v,
      ContextMenuInfo menuInfo) {

    // TODO Auto-generated method stub
    super.onCreateContextMenu(menu, v, menuInfo);

    menu.add(0, 0, 0, "Deletar");
    menu.add(0, 1, 0, "Duplicar");

  }

  // -----------------------------
  // seleção de item do contexto
  // -----------------------------
  @Override
  public boolean onContextItemSelected(MenuItem item) {

    // TODO Auto-generated method stub

    switch (item.getItemId()) {
      case 0:

        GastoDAO dao = new GastoDAO(ListaGastos.this);
        dao.deletar(gastoSelecionado);

        Log.i("ListaGastos.java", "Removendo o gasto id: "
            + gastoSelecionado.getData());

        dao.close();

        carregaLista();

        break;
    }

    return super.onContextItemSelected(item);

  }

  /**
   * o onResume é executado quando a Activity anterior é fechada
   * 
   */
  @Override
  protected void onResume() {

    super.onResume();
    carregaLista();
  }

  /**
   * carregaLista()
   * 
   * Carrega a lista de Gasto
   */
  private void carregaLista() {

    GastoDAO dao = new GastoDAO(this);
    gastos = dao.getLista();
    dao.close();

    ArrayAdapter<Gasto> adapter = new ArrayAdapter<Gasto>(this,
        android.R.layout.simple_list_item_1, gastos);

    lista.setAdapter(adapter);

  }

}
