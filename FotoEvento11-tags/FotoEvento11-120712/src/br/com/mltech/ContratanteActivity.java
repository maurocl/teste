package br.com.mltech;

import android.app.Activity;
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
 * Essa tela gerencia as informações a respeito do Contratante do evento.
 * 
 * @author maurocl
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

    // identifica os elementos de UI da tela
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

    // Tratamento de evento do botão Enviar
    btnEnviar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Log.d(TAG, "Botão enviar selecionado");

        // Lê os valores dos campos da UI
        String nome1 = nome.getText().toString();
        String email1 = email.getText().toString();
        String telefone1 = telefone.getText().toString();

        // atualiza o objeto contratante com os valores obtidos da tela.
        mContratante.setNome(nome1);
        mContratante.setEmail(email1);
        mContratante.setTelefone(telefone1);

        if (checkCamposObrigatorios(nome, email, telefone) == true) {

          // campos obrigatórios foram forncecidos
          // cria uma nova Intent para retorno de informações
          Intent it = new Intent();

          // retorna uma instância de contratante
          it.putExtra(Constantes.CONTRATANTE, mContratante);

          setResult(RESULT_OK, it);

          // finaliza a activity
          finish();

        } else {

          // campos obrigatórios não foram preenchidos
          Toast.makeText(ContratanteActivity.this, "Campos obrigatórios não foram preenchidos", Toast.LENGTH_SHORT).show();

        }

        // loga as informações do contratante;
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

        // finaliza a activity
        finish();

      }

    });

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

}
