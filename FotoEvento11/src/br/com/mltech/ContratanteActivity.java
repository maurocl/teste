package br.com.mltech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

  private static final int ACTIVITY_CONTRATANTE = 100;
  
  private Contratante mContratante=null; 

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
      
      if(mContratante==null) {
        mContratante = new Contratante("","","");
        Log.d(TAG,"Criando contratante vazio");
      }
      else {
        Log.d(TAG,"Contratante carregado com sucesso: "+mContratante);
      }
      
    }

    final EditText nome = (EditText) findViewById(R.id.nome);
    final EditText email = (EditText) findViewById(R.id.email);
    final EditText telefone = (EditText) findViewById(R.id.telefone);
     
    // Inicializa os dados do contratante

    nome.setText(mContratante.getNome());
    email.setText(mContratante.getEmail());
    telefone.setText(mContratante.getTelefone());
    
    final Button btnEnviar = (Button) findViewById(R.id.btnEnviar);
    final Button btnCancelar = (Button) findViewById(R.id.btnCancelar);

    // Tratamento de evento do botão Enviar
    btnEnviar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {
                
        Log.d(TAG, "Botão enviar selecionado");

        //checkCamposObrigatorios(nome, email); 
        
        String nome1 = nome.getText().toString();
        String email1 = email.getText().toString();
        String telefone1 = telefone.getText().toString();
        
        mContratante.setNome(nome1);
        mContratante.setEmail(email1);
        mContratante.setTelefone(telefone1);
        
        Log.d(TAG, "Contratante: " + mContratante);

        // cria uma nova Intent para retorno de informações
        Intent it = new Intent();

        // retorna uma instância de contratante
        it.putExtra("br.com.mltech.contratante", mContratante);

        setResult(RESULT_OK, it);

        finish();

      }

  
      /**
       * checkCamposObrigatorios(final EditText nome, final EditText email, final EditText telefone)
       * 
       * @param nome
       * @param email
       * @param telefone
       * 
       */
      private void checkCamposObrigatorios(final EditText nome, final EditText email, final EditText telefone) {
        if (nome != null) {
          Log.d(TAG, "nome=" + nome.getText().toString());
        } 

        if (email != null) {
          Log.d(TAG, "email=" + email.getText().toString());
        }
        
        if (telefone != null) {
          Log.d(TAG, "email=" + telefone.getText().toString());
        }

        
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

}
