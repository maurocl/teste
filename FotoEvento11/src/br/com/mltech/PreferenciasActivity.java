package br.com.mltech;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * PreferenciasActivity
 * 
 * Activity respons�vel pelo cadastramento de prefer�ncias de uso do sistema
 * 
 * @author maurocl
 * 
 */
public class PreferenciasActivity extends Activity {

  private SharedPreferences preferences;

  /**
   * onCreate(android.os.Bundle savedInstanceState)
   */
  protected void onCreate(android.os.Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    this.setContentView(R.layout.preferencias);

    final EditText assunto = (EditText) findViewById(R.id.editAssunto);    
    final EditText descricao = (EditText) findViewById(R.id.editDescricao);
    
    final EditText urlImagem = (EditText) findViewById(R.id.editUrlImagem);
    
    final Button btnGravarPreferencias = (Button) findViewById(R.id.btnGravarPreferencias);
    final Button btnCancelar = (Button) findViewById(R.id.btnCancelar);
        
    // carrega as prefer�ncias sobre o envio de email
    preferences = getSharedPreferences("pref_email", MODE_PRIVATE);
    
    assunto.setText(preferences.getString("preferencias_assunto", ""));
    descricao.setText(preferences.getString("preferencias_descricao", ""));
    urlImagem.setText(preferences.getString("preferencias_url_imagem", ""));
    

    // processa o bot�o de Gravar
    btnGravarPreferencias.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Editor edit = preferences.edit();
        
        edit.putString("preferencias_assunto", assunto.getText().toString());
        edit.putString("preferencias_descricao", descricao.getText().toString());
        edit.putString("preferencias_url_imagem", urlImagem.getText().toString());

        // grava as prefer�ncias
        edit.commit();

        finish();

      }
    });

    // processa o bot�o Cancelar
    btnCancelar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        finish();

      }
    });

    
  };

}
