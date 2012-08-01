
package br.com.mltech;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import br.com.mltech.dao.CategoriaDAO;
import br.com.mltech.dao.GastoDAO;
import br.com.mltech.modelo.Categoria;
import br.com.mltech.modelo.Gasto;

/**
 * FormGasto.java
 * 
 * @author maurocl
 * 
 *         Formulário de entrada de gastos
 * 
 */
public class FormGasto extends Activity {

  private static final String TAG = "FormGasto";

  List<Categoria> categorias;

  List<String> lista;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.formgasto);

    Intent i = getIntent();

    if (i.getData() != null) {
      Log.d(TAG, "Data: " + i.getData());
    }

    CategoriaDAO catDAO = new CategoriaDAO(FormGasto.this);

    categorias = catDAO.getLista();

    catDAO.close();

    Button botao = (Button) findViewById(R.id.inserir);

    // Ação associada ao botão Inserir
    botao.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        Context ctx = FormGasto.this;

        Log.i(TAG, v.toString());

        //Toast.makeText(ctx, "Voce clicou no item ", Toast.LENGTH_LONG).show();

        //EditText id = (EditText) findViewById(R.id.codigo);
        EditText data = (EditText) findViewById(R.id.data);
        EditText descricao = (EditText) findViewById(R.id.descricao);
        EditText valor = (EditText) findViewById(R.id.valor);
        Spinner spin = (Spinner) findViewById(R.id.spinCategoria);

        int num = categorias != null ? categorias.size() : 0;

        Log.d(TAG, "Nº de categorias: " + num);

        String[] xxx = new String[num];

        int j = 0;
        for (Categoria c1 : categorias) {
          xxx[j] = c1.getDescricao();
          j++;
        }

        String[] pqp = new String[] { "1", "2" };

        // Create an ArrayAdapter using the string array and a default spinner layout

        Spinner spinner = (Spinner) findViewById(R.id.spinCategoria);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ctx,
            R.array.categorias_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Cria um novo gasto
        Gasto gasto = new Gasto();

        gasto.setData(data.getText().toString());
        gasto.setDescricao(descricao.getText().toString());
        gasto.setValor(Double.parseDouble(valor.getText().toString()));

        Categoria c = new Categoria();
        gasto.setCategoria(c);

        // cat.setDescricao(descricao.getEditableText().toString());

        GastoDAO dao = new GastoDAO(FormGasto.this);

        dao.inserir(gasto);

        dao.close();

        Intent intent = new Intent();

        intent.putExtra("br.com.mltech.gasto", gasto);

        Log.d(TAG, "Gasto: " + gasto);

        setResult(RESULT_OK, intent);

        finish();

      }

    });

  }
}
