package br.com.mltech;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

	private Gasto gasto = new Gasto();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.formgasto);

		Button botao = (Button) findViewById(R.id.inserir);

		// Ação associada ao botão Inserir
		botao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("FormGasto", v.toString());

				Toast.makeText(FormGasto.this, "Voce clicou no item ",
						Toast.LENGTH_LONG).show();

				EditText descricao = (EditText) findViewById(R.id.descricao);
				EditText data = (EditText) findViewById(R.id.data);
				EditText valor = (EditText) findViewById(R.id.valor);
				EditText codigo = (EditText) findViewById(R.id.codigo);

				Categoria c = new Categoria();

				// gasto.setCodGasto(codGasto);
				gasto.setData(data.toString());
				gasto.setDescricao(descricao.toString());
				gasto.setValor(Double.parseDouble(valor.getText().toString()));
				gasto.setCategoria(c);

				// cat.setDescricao(descricao.getEditableText().toString());

				GastoDAO dao = new GastoDAO(FormGasto.this);
				dao.inserir(gasto);
				dao.close();

				finish();

			}
		});

	}
}
