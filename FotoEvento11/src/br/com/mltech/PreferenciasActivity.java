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
 * <p>Activity responsável pela manutenção das preferências de uso do sistema<br>
 * 
 * Aqui são configuradas as preferências de:<br>
 * 
 * - subjet do email - corpo da mensagem do email<br>
 * 
 * - texto padrão da mensagem do Facebook - texto padrão da mensagem do Twitter<br>
 * 
 * - localização da foto da página inicial da aplicação<br>
 * 
 * - nº da câmera frontal do equipamento<br>
 * 
 * @author maurocl
 * 
 */
public class PreferenciasActivity extends Activity implements Constantes {

  public static final String TAG = "Preferencias";

  public static final String PREF_EMAIL = "pref_email";

  private static SharedPreferences preferences;

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

    final EditText editUsuario = (EditText) findViewById(R.id.editUsuario);

    final EditText editSenha = (EditText) findViewById(R.id.editSenha);
    final EditText editServidorSMTP = (EditText) findViewById(R.id.editServidorSMTP);
    final EditText editServidorSMTPPorta = (EditText) findViewById(R.id.editServidorSMTPPorta);
    final EditText editRemetente = (EditText) findViewById(R.id.editRemetente);

    final Button btnGravarPreferencias = (Button) findViewById(R.id.btnGravarPreferencias);
    final Button btnCancelar = (Button) findViewById(R.id.btnCancelar);

    // carrega as preferências sobre o envio de email
    preferences = getSharedPreferences(PREF_EMAIL, MODE_PRIVATE);

    //---------------------------------------------------------------------------------
    // Atualiza a interface gráfico com os valores recuperados do arquivo de prefências
    //---------------------------------------------------------------------------------
    assunto.setText(preferences.getString(Constantes.PREFERENCIAS_ASSUNTO, ""));
    descricao.setText(preferences.getString(Constantes.PREFERENCIAS_DESCRICAO, ""));

    txtFacebook.setText(preferences.getString(Constantes.PREFERENCIAS_TEXTO_FACEBOOK, ""));
    txtTwitter.setText(preferences.getString(Constantes.PREFERENCIAS_TEXTO_TWITTER, ""));

    urlImagem.setText(preferences.getString(Constantes.PREFERENCIAS_URL_IMAGEM, ""));
    numCameraFrontal.setText(preferences.getString(Constantes.PREFERENCIAS_NUM_CAMERA_FRONTAL, ""));

    editUsuario.setText(preferences.getString(Constantes.PREFERENCIAS_USUARIO_EMAIL, ""));
    editSenha.setText(preferences.getString(Constantes.PREFERENCIAS_SENHA_EMAIL, ""));
    editServidorSMTP.setText(preferences.getString(Constantes.PREFERENCIAS_SERVIDOR_SMTP, ""));
    editServidorSMTPPorta.setText(preferences.getString(Constantes.PREFERENCIAS_SERVIDOR_SMTP_PORTA, ""));
    editRemetente.setText(preferences.getString(Constantes.PREFERENCIAS_REMETENTE_EMAIL, ""));

    //---------------------------
    // processa o botão de Gravar
    //---------------------------
    btnGravarPreferencias.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Editor edit = preferences.edit();

        //------------------------------------------------------------------------------
        // Atualiza o arquivo de prefência com as alterações feitas na interface gráfica
        //------------------------------------------------------------------------------
        edit.putString(Constantes.PREFERENCIAS_ASSUNTO, assunto.getText().toString());
        edit.putString(Constantes.PREFERENCIAS_DESCRICAO, descricao.getText().toString());

        edit.putString(Constantes.PREFERENCIAS_TEXTO_FACEBOOK, txtFacebook.getText().toString());
        edit.putString(Constantes.PREFERENCIAS_TEXTO_TWITTER, txtTwitter.getText().toString());

        edit.putString(Constantes.PREFERENCIAS_URL_IMAGEM, urlImagem.getText().toString());
        edit.putString(Constantes.PREFERENCIAS_NUM_CAMERA_FRONTAL, numCameraFrontal.getText().toString());

        edit.putString(Constantes.PREFERENCIAS_USUARIO_EMAIL, editUsuario.getText().toString());
        edit.putString(Constantes.PREFERENCIAS_SENHA_EMAIL, editSenha.getText().toString());
        edit.putString(Constantes.PREFERENCIAS_SERVIDOR_SMTP, editServidorSMTP.getText().toString());
        edit.putString(Constantes.PREFERENCIAS_SERVIDOR_SMTP_PORTA, editServidorSMTPPorta.getText().toString());
        edit.putString(Constantes.PREFERENCIAS_REMETENTE_EMAIL, editRemetente.getText().toString());

        // grava as preferências
        boolean b = edit.commit();

        if (b == false) {
          Log.d(TAG, "onClick() - Falha na atualização da preferência");
        }
        // Finaliza a execução da activity
        finish();

      }
    });

    // processa o botão Cancelar
    btnCancelar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        // Finaliza a execução da activity
        finish();

      }

    });

  };

}
