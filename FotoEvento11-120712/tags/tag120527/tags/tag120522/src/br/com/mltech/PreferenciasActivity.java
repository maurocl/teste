package br.com.mltech;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * PreferenciasActivity
 * 
 * Activity responsável pela manutenção das preferências de uso do sistema
 * 
 * Aqui são configuradas as preferências de:
 * 
 * - subjet do email
 * - corpo da mensagem do email
 * 
 * - texto padrão da mensagem do Facebook
 * - texto padrão da mensagem do Twitter
 * 
 * - localização da foto da página inicial da aplicação
 * 
 * - nº da câmera frontal do equipamento
 * 
 * @author maurocl
 * 
 */
public class PreferenciasActivity extends Activity {

  public static final String TAG = "Preferencias";
  
  public static final String PREF_EMAIL = "pref_email";

  private SharedPreferences preferences;

  /**
   * onCreate(android.os.Bundle savedInstanceState)
   */
  protected void onCreate(android.os.Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    this.setContentView(R.layout.preferencias);

    final EditText assunto = (EditText) findViewById(R.id.editAssunto);
    final EditText descricao = (EditText) findViewById(R.id.editDescricao);
        
    final EditText txtFacebook = (EditText) findViewById(R.id.editTextoFacebook);
    final EditText txtTwitter  = (EditText) findViewById(R.id.editTextoTwitter);
    
    final EditText urlImagem = (EditText) findViewById(R.id.editUrlImagem);
    final EditText numCameraFrontal = (EditText) findViewById(R.id.editNumCameraFrontal);

    final Button btnGravarPreferencias = (Button) findViewById(R.id.btnGravarPreferencias);
    final Button btnCancelar = (Button) findViewById(R.id.btnCancelar);

    // carrega as preferências sobre o envio de email
    preferences = getSharedPreferences(PREF_EMAIL, MODE_PRIVATE);

    assunto.setText(preferences.getString("preferencias_assunto", ""));
    descricao.setText(preferences.getString("preferencias_descricao", ""));
    
    txtFacebook.setText(preferences.getString("preferencias_texto_facebook", ""));
    txtTwitter.setText(preferences.getString("preferencias_texto_twitter", ""));
    
    urlImagem.setText(preferences.getString("preferencias_url_imagem", ""));
    numCameraFrontal.setText(preferences.getString("preferencias_num_camera_frontal", ""));

    // processa o botão de Gravar
    btnGravarPreferencias.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Editor edit = preferences.edit();

        edit.putString("preferencias_assunto", assunto.getText().toString());
        edit.putString("preferencias_descricao", descricao.getText().toString());
        
        edit.putString("preferencias_texto_facebook", txtFacebook.getText().toString());
        edit.putString("preferencias_texto_twitter", txtTwitter.getText().toString());
        
        edit.putString("preferencias_url_imagem", urlImagem.getText().toString());
        edit.putString("preferencias_num_camera_frontal", numCameraFrontal.getText().toString());

        // grava as preferências
        boolean b = edit.commit();

        if (b == false) {
          Log.d(TAG, "Falha da atualização da preferência");
        }

        finish();

      }
    });

    // processa o botão Cancelar
    btnCancelar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        finish();

      }
      
    });

  };

}
