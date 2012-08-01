
package br.com.mltech;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.com.mltech.dao.CategoriaDAO;
import br.com.mltech.modelo.Categoria;

/**
 * FormCategoria.java
 * 
 * @author maurocl
 * 
 *         Formulário de Cadastro de Categorias
 * 
 */
public class FormCategoria extends Activity {

  private static final String TAG = "FormCategoria";

  CategoriaDAO dao;

  //  private Categoria categoria = new Categoria();

  /**
	 * 
	 */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.formcategoria);

    Button botao = (Button) findViewById(R.id.inserir);

    botao.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        Log.i("Categoria", v.toString());

        //Toast.makeText(FormCategoria.this, "Você clicou no item ", Toast.LENGTH_LONG).show();

        EditText descricao = (EditText) findViewById(R.id.descricao);

        if (descricao.getEditableText().toString().equalsIgnoreCase("")) {
          Toast.makeText(FormCategoria.this, "Descrição não pode ser nula", Toast.LENGTH_SHORT).show();
          return;
        }

        // Cria uma nova categoria
        Categoria categoria = new Categoria(descricao.getEditableText().toString());

        //CategoriaDAO dao = new CategoriaDAO(FormCategoria.this);
        dao.inserir(categoria);
        //dao.close();

        Log.d(TAG, categoria + " inserida com sucesso");

        //finish();

      }
    });

  }

  @Override
  protected void onResume() {

    super.onResume();

    dao = new CategoriaDAO(FormCategoria.this);

  }

  @Override
  protected void onPause() {

    if (dao != null) {
      dao.close();
    }

    super.onPause();
  }

}
