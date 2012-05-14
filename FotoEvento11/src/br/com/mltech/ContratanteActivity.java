package br.com.mltech;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.com.mltech.modelo.Contratante;

/**
 * ContratanteActivity
 * 
 * Essa tela lê as informações a respeito do contratante
 * 
 * @author maurocl
 * 
 */
public class ContratanteActivity extends Activity {

  private static final String TAG = "ContratanteActivity";

  private Contratante mContratante = null;

  /**
   * onCreate(Bundle savedInstanceState)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.contratante);

    Log.d(TAG, "*** onCreate() ***");

    Intent intent = getIntent();

    if (intent != null) {

      mContratante = (Contratante) intent.getSerializableExtra("br.com.mltech.contratante");

      if (mContratante == null) {
        mContratante = new Contratante(null, null, null);
        Log.d(TAG, "Criando contratante null");
      } else {
        Log.d(TAG, "Contratante carregado com sucesso: " + mContratante);
      }

    }

    final EditText nome = (EditText) findViewById(R.id.nome);
    final EditText email = (EditText) findViewById(R.id.email);
    final EditText telefone = (EditText) findViewById(R.id.telefone);

    // Inicializa os dados do contratante

    if (mContratante.getNome() != null) {
      nome.setText(mContratante.getNome());
    }
    if (mContratante.getEmail() != null) {
      email.setText(mContratante.getEmail());
    }
    if (mContratante.getTelefone() != null) {
      telefone.setText(mContratante.getTelefone());
    }

    final Button btnEnviar = (Button) findViewById(R.id.btnEnviar);
    final Button btnCancelar = (Button) findViewById(R.id.btnCancelar);

    // Tratamento de evento do botão Enviar
    btnEnviar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Log.d(TAG, "Botão enviar selecionado");

        // Lê os valores dos campos da UI
        String nome1 = nome.getText().toString();
        String email1 = email.getText().toString();
        String telefone1 = telefone.getText().toString();

        mContratante.setNome(nome1);
        mContratante.setEmail(email1);
        mContratante.setTelefone(telefone1);

        if (checkCamposObrigatorios(nome, email, telefone) == true) {
          
          // campos obrigatórios foram forncecidos
          // cria uma nova Intent para retorno de informações
          Intent it = new Intent();

          // retorna uma instância de contratante
          it.putExtra("br.com.mltech.contratante", mContratante);

          setResult(RESULT_OK, it);

          finish();
        } else {
          // campos obrigatórios não foram preenchidos
          Toast.makeText(ContratanteActivity.this, "Campos obrigatórios não foram preenchidos", Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, "Contratante: " + mContratante);

      

      }

    });

    // Tratamento de evento do botão Cancelar
    btnCancelar.setOnClickListener(new OnClickListener() {

      /**
       * onClick(View v)
       */
      public void onClick(View v) {

        Log.d(TAG, "Botão cancelar selecionado");

        finish();

      }

    });

  }

  /**
   * checkCamposObrigatorios(final EditText nome, final EditText email, final
   * EditText telefone)
   * 
   * @param nome
   * @param email
   * @param telefone
   * 
   */
  private boolean checkCamposObrigatorios(final EditText nome, final EditText email, final EditText telefone) {

    boolean validado = true;

    
    if( (nome==null) || (email==null) || (telefone==null)) {
      return false;
    }
    
    if(nome!=null && nome.getText().toString().equals("")) {
      validado = false;
    }

    if(email!=null && email.getText().toString().equals("")) {
      validado = false;
    }

    if(telefone!=null && telefone.getText().toString().equals("")) {
      validado = false;
    }

    return validado;

  }

}
