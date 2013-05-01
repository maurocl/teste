
package br.com.mltech.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.com.mltech.Constantes;
import br.com.mltech.R;
import br.com.mltech.modelo.Contratante;

/**
 * Essa tela gerencia as informa��es a respeito do Contratante do evento.
 * 
 * 
 * 
 */
public class ContratanteActivity extends Activity implements Constantes {

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

      mContratante = (Contratante) intent.getSerializableExtra(Constantes.CONTRATANTE);

      if (mContratante == null) {

        mContratante = new Contratante(null, null, null);

        Log.d(TAG, "onCreate() - Criando contratante null");

      } else {

        Log.d(TAG, "onCreate() - Contratante carregado com sucesso: " + mContratante);

      }

    }

    //
    // identifica os elementos de UI da tela
    //
    final EditText nome = (EditText) findViewById(R.id.nome);
    final EditText email = (EditText) findViewById(R.id.email);
    final EditText telefone = (EditText) findViewById(R.id.telefone);

    // Inicializa os dados do contratante
    // atualiza os componente de UI com seus valores iniciais (se houverem).

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

    /**
     * Tratamento de evento do bot�o Enviar
     */
    btnEnviar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Log.d(TAG, "Bot�o enviar selecionado");

        // L� os valores dos campos da UI
        // atualiza o objeto contratante com os valores obtidos da tela.
        mContratante.setNome(nome.getText().toString());
        
        if(nome.getText().toString().equals("")) {
          // email n�o pode ser vazio
          showToast("Nome n�o pode ser nulo");
          return;
        }
        
        mContratante.setEmail(email.getText().toString());
        
        if(email.getText().toString().equals("")) {
          // email n�o pode ser vazio
          showToast("Email n�o pode ser nulo");
          return;
        }
        
        mContratante.setTelefone(telefone.getText().toString());
        
        if(telefone.getText().toString().equals("")) {
          // email n�o pode ser vazio
          showToast("Telefone n�o pode ser nulo");
          return;
        }

        if (checkCamposObrigatorios(nome, email, telefone) == true) {

          // campos obrigat�rios foram forncecidos
          // cria uma nova Intent para retorno de informa��es
          Intent it = new Intent();

          // retorna uma inst�ncia de contratante
          it.putExtra(Constantes.CONTRATANTE, mContratante);

          setResult(RESULT_OK, it);

          // finaliza a activity
          finish();

        } else {

          // campos obrigat�rios n�o foram preenchidos
          Toast.makeText(ContratanteActivity.this, "Campos obrigat�rios n�o foram preenchidos", Toast.LENGTH_SHORT).show();

        }

        // loga as informa��es do contratante;
        Log.d(TAG, "Contratante: " + mContratante);

      }

    });

    // Tratamento de evento do bot�o Cancelar
    btnCancelar.setOnClickListener(new OnClickListener() {

      /**
       * onClick(View v)
       */
      public void onClick(View v) {

        Log.d(TAG, "Bot�o cancelar selecionado");

        // finaliza a activity
        finish();

      }

    });

    // Hide soft-keyboard:
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    
  }

  /**
   * checkCamposObrigatorios(final EditText nome, final EditText email, final
   * EditText telefone)
   * 
   * @param nome
   *          Nome do contratante
   * @param email
   *          Email do contratante
   * @param telefone
   *          Telefone do contratante
   * 
   */
  private boolean checkCamposObrigatorios(final EditText nome, final EditText email, final EditText telefone) {

    boolean validado = true;

    if ((nome == null) || (email == null) || (telefone == null)) {
      return false;
    }

    if (nome != null && nome.getText().toString().equals("")) {
      validado = false;
    }

    if (email != null && email.getText().toString().equals("")) {
      validado = false;
    }

    if (telefone != null && telefone.getText().toString().equals("")) {
      validado = false;
    }

    return validado;

  }

  /**
   * Exibe um toast na tela
   * 
   * @param msg mensagem exibida pelo toast
   * 
   */
  private void showToast(String msg) {

    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
  }

}