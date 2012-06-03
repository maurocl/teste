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
 * Activity respons�vel pela manuten��o das prefer�ncias de uso do sistema
 * 
 * Aqui s�o configuradas as prefer�ncias de:
 * 
 * - subjet do email - corpo da mensagem do email
 * 
 * - texto padr�o da mensagem do Facebook - texto padr�o da mensagem do Twitter
 * 
 * - localiza��o da foto da p�gina inicial da aplica��o
 * 
 * - n� da c�mera frontal do equipamento
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
    final EditText txtTwitter = (EditText) findViewById(R.id.editTextoTwitter);

    final EditText urlImagem = (EditText) findViewById(R.id.editUrlImagem);
    final EditText numCameraFrontal = (EditText) findViewById(R.id.editNumCameraFrontal);

    final Button btnGravarPreferencias = (Button) findViewById(R.id.btnGravarPreferencias);
    final Button btnCancelar = (Button) findViewById(R.id.btnCancelar);

    // carrega as prefer�ncias sobre o envio de email
    preferences = getSharedPreferences(PREF_EMAIL, MODE_PRIVATE);

    //---------------------------------------------------------------------------------
    // Atualiza a interface gr�fico com os valores recuperados do arquivo de pref�ncias
    //---------------------------------------------------------------------------------
    assunto.setText(preferences.getString("preferencias_assunto", ""));
    descricao.setText(preferences.getString("preferencias_descricao", ""));

    txtFacebook.setText(preferences.getString("preferencias_texto_facebook", ""));
    txtTwitter.setText(preferences.getString("preferencias_texto_twitter", ""));

    urlImagem.setText(preferences.getString("preferencias_url_imagem", ""));
    numCameraFrontal.setText(preferences.getString("preferencias_num_camera_frontal", ""));

    //---------------------------
    // processa o bot�o de Gravar
    //---------------------------
    btnGravarPreferencias.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Editor edit = preferences.edit();

        //------------------------------------------------------------------------------
        // Atualiza o arquivo de pref�ncia com as altera��es feitas na interface gr�fica
        //------------------------------------------------------------------------------
        edit.putString("preferencias_assunto", assunto.getText().toString());
        edit.putString("preferencias_descricao", descricao.getText().toString());

        edit.putString("preferencias_texto_facebook", txtFacebook.getText().toString());
        edit.putString("preferencias_texto_twitter", txtTwitter.getText().toString());

        edit.putString("preferencias_url_imagem", urlImagem.getText().toString());
        edit.putString("preferencias_num_camera_frontal", numCameraFrontal.getText().toString());

        // grava as prefer�ncias
        boolean b = edit.commit();

        if (b == false) {
          Log.d(TAG, "onClick() - Falha na atualiza��o da prefer�ncia");
        }
        // Finaliza a execu��o da activity
        finish();

      }
    });

    // processa o bot�o Cancelar
    btnCancelar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {
        // Finaliza a execu��o da activity
        finish();

      }

    });

  };

}