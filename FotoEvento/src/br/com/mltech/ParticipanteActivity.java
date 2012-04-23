package br.com.mltech;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import br.com.mltech.modelo.Participante;

/**
 * Participante.java
 * 
 * @author maurocl
 * 
 *         Criação da activity de cadastro dos participantes do evento
 * 
 */
public class ParticipanteActivity extends Activity {

	private static final String TAG = "Participante";
	
	private Participante usuario;

	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.participante);

		final EditText editNome = (EditText) findViewById(R.id.editNome);
		final EditText editEmail = (EditText) findViewById(R.id.editEmail);
		final EditText editTelefone = (EditText) findViewById(R.id.editTelefone);

		final RadioGroup group = (RadioGroup) findViewById(R.id.group1);

		final RadioGroup group2 = (RadioGroup) findViewById(R.id.group2);

		final Button btnEnviar = (Button) findViewById(R.id.button1);
		final Button btnCancelar = (Button) findViewById(R.id.button2);

		/**
		 * Tratamento do botão Enviar 
		 */
		btnEnviar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Botão Enviar selecionado");

				Log.d(TAG, "Nome: " + editNome.getText().toString());
				Log.d(TAG, "Email: " + editEmail.getText().toString());
				Log.d(TAG, "Telefone: " + editTelefone.getText().toString());
				
				usuario = new Participante(editNome.getText().toString(), editEmail.getText().toString(), editTelefone.getText().toString());

			}
		});

		/**
		 * Tratamento do evento de click no botão Cancelar
		 */
		btnCancelar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.d(TAG, "Botão Cancelar selecionado");

				// retorna todos os campos para seus valores iniciais.

				editNome.setText("");
				editEmail.setText("");
				editTelefone.setText("");

			}
		});

		/**
		 * 
		 */
		group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				boolean opcao1 = R.id.radioOp1 == checkedId;
				boolean opcao2 = R.id.radioOp2 == checkedId;
				if (opcao1) {
					Log.d(TAG, "Opção Polaroid escolhida");
				} else if (opcao2) {
					Log.d(TAG, "Opção Cabine escolhida");

				}
			}
		});

		/**
		 * 
		 */
		group2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				boolean cor = R.id.radioCor == checkedId;
				boolean pb = R.id.radioPB == checkedId;
				if (cor) {
					Log.d(TAG, "Foto a cores");
				} else if (pb) {
					Log.d(TAG, "Foto em Preto e Branco");

				}
			}
		});

	}

}
