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

	private Categoria cat = new Categoria();

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
				// TODO Auto-generated method stub
				Log.i("Categoria", v.toString());

				Toast.makeText(FormCategoria.this, "Voce clicou no item ",
						Toast.LENGTH_LONG).show();

				EditText descricao = (EditText) findViewById(R.id.descricao);

				cat.setDescricao(descricao.getEditableText().toString());

				CategoriaDAO dao = new CategoriaDAO(FormCategoria.this);
				dao.inserir(cat);
				dao.close();

				finish();

			}
		});

	}

}
