
package br.com.mltech;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import br.com.mltech.dao.CategoriaDAO;
import br.com.mltech.dao.GastoDAO;
import br.com.mltech.modelo.Categoria;
import br.com.mltech.modelo.Gasto;




/**
 * MeusGastosActivity.java
 * 
 * @author maurocl
 * 
 */
public class MeusGastosActivity extends Activity {

  public static final String TAG = "MeusGastosActivity";

  public static final int ACTION_CATEGORIA = 100;

  public static final int ACTION_GASTO = 101;

  public static final int ACTION_RELATORIOS = 102;

  public static final int ACTION_LOGIN = 103;
  
  public static final int ACTION_DATAS = 104;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);

    /**
     * 
     */
    iniciaTabelas();

    final Button categorias = (Button) findViewById(R.id.categorias);

    final Button gastos = (Button) findViewById(R.id.gastos);

    final Button relatorios = (Button) findViewById(R.id.relatorios);

    //final TextView texto = (TextView) findViewById(R.id.textView1);

    /*------------------*/
    /* Botão Categorias */
    /*------------------*/
    categorias.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View arg0) {

        Log.i(TAG, "botão Categorias");
        showToast("Categorias");

        Intent intent = new Intent(getApplicationContext(), FormCategoria.class);

        startActivityForResult(intent, ACTION_CATEGORIA);

      }

    });

    /*------------------*/
    /* Botão Gastos */
    /*------------------*/
    gastos.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View arg0) {

        Log.i(TAG, "botão Gastos");
        showToast("Gastos");

        Intent intent = new Intent(getApplicationContext(), FormGasto.class);

        startActivityForResult(intent, ACTION_GASTO);

      }

    });

    /*------------------*/
    /* Botão Relatórios */
    /*------------------*/
    relatorios.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View arg0) {

        Log.i(TAG, "botão Relatórios");
        showToast("Relatórios");
      }

    });

  }

  /**
   * 
   */
  private void iniciaTabelas() {

    Categoria c = new Categoria();
    c.setDescricao("Combustível");

    Log.d(TAG, "iniciaTabelas() - categoria: " + c);

    Categoria c2 = new Categoria("Alimentação");
    Categoria c3 = new Categoria("Saúde");
    Categoria c4 = new Categoria("Educação");

    CategoriaDAO dao = new CategoriaDAO(this);

    dao.inserir(c);

    List<Categoria> lista = dao.getLista();
    showCategoria(lista);

    dao.close();

    GastoDAO gasto = new GastoDAO(this);
    //Gasto g = new Gasto(1, "19/07/2012", "descricao", 1000.0);

    
    android.text.format.DateFormat df = new android.text.format.DateFormat();
    //df.format("yyyy-MM-dd hh:mm:ss", new java.util.Date());
    
    
    Gasto g1 = new Gasto(1L, "codgasto", df.format("dd-MM-yyyy", new java.util.Date()).toString(), (double) 10, c);

    gasto.inserir(g1);

    List<Gasto> gastos1 = gasto.getLista();

    showGastos(gastos1);
    
    gasto.close();

  }

  /**
   * @param gasto
   */
  private void showGastos(List<Gasto> gastos) {

    if (gastos != null) {
      Log.d(TAG, "Nº total de gastos: " + gastos.size());
    }

    int i = 0;
    for (Gasto gasto : gastos) {
      Log.d(TAG, "gasto(" + i + "): " + gasto);
      i++;
    }
  }

  /**
   * 
   * @param categorias
   */
  void showCategoria(List<Categoria> categorias) {

    if (categorias != null) {
      Log.d(TAG, "Nº total de categorias: " + categorias.size());
    }

    int i = 0;
    for (Categoria c : categorias) {
      Log.d(TAG, "Categoria(" + i + "): " + c);
      i++;
    }

  }

  @Override
  protected void onRestart() {

    super.onRestart();
    Log.i(TAG, "onRestart()");
  }

  @Override
  protected void onResume() {

    super.onResume();
    Log.i(TAG, "onResume()");
  }

  @Override
  protected void onDestroy() {

    super.onDestroy();
    Log.i(TAG, "onDestroy()");
  }

  /**
   * Tratamento dos menus de opções
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // ---------------------
    // MenuItem: Categorias
    // ---------------------
    MenuItem categorias = menu.add(0, 0, 0, "Categorias");
    categorias.setIcon(R.drawable.ic_launcher);

    categorias.setIntent(new Intent(this, ListaCategorias.class));

    categorias.setOnMenuItemClickListener(new OnMenuItemClickListener() {

      @Override
      public boolean onMenuItemClick(MenuItem item) {

        Toast.makeText(MeusGastosActivity.this, "Categorias",
            Toast.LENGTH_SHORT).show();
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

        Toast.makeText(MeusGastosActivity.this, "Gastos",
            Toast.LENGTH_SHORT).show();
        return false;
      }

    });

    // ---------------------
    // MenuItem: Relatórios
    // ---------------------
    MenuItem relatorios = menu.add(0, 2, 0, "Relatórios");
    relatorios.setIcon(R.drawable.ic_launcher);
    relatorios.setIntent(new Intent(this, FormCategoria.class));

    relatorios.setOnMenuItemClickListener(new OnMenuItemClickListener() {

      @Override
      public boolean onMenuItemClick(MenuItem item) {

        Toast.makeText(MeusGastosActivity.this, "Relatórios",
            Toast.LENGTH_SHORT).show();
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

        showToast("Login");
        return false;
      }

    });
    
    
    
    // ---------------------
    // MenuItem: Datas
    // ---------------------
    MenuItem datas = menu.add(0, 4, 0, "Datas");
    datas.setIcon(R.drawable.ic_launcher);
    //datas.setIntent(new Intent(this, FormDatas.class));

    datas.setOnMenuItemClickListener(new OnMenuItemClickListener() {

      @Override
      public boolean onMenuItemClick(MenuItem item) {

        showToast("Datas");
        
        startActivityForResult(new Intent(MeusGastosActivity.this,FormDatas.class),ACTION_DATAS);
        
        return false;
      }

    });

    return super.onCreateOptionsMenu(menu);

  }

  /**
   * Exibe uma mensagem em um toast
   * 
   * @param msg
   *          Mensagem que será exibida
   * 
   */
  public void showToast(String msg) {

    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    Log.d(TAG, "requestCode=" + requestCode + ", resultCode=" + resultCode + ", data=" + data);

    //super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case ACTION_CATEGORIA:
        break;
      case ACTION_GASTO:
        break;
      case ACTION_DATAS:
        
        if(data!=null) {
          String dtInicial = data.getStringExtra("br.com.mltech.dtInicial");
          String dtFinal = data.getStringExtra("br.com.mltech.dtFinal");
          
          if(dtInicial!=null) {
            Log.d(TAG,"Data Inicial: "+dtInicial);
          }

          if(dtFinal!=null) {
            Log.d(TAG,"Data Inicial: "+dtFinal);
          }

          
        }
        
        break;        
      default:
        break;
    }

  }

}