package br.com.mltech.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import br.com.mltech.Constantes;
import br.com.mltech.R;

/**
 * Activity responsável pela manutenção das preferências de uso do sistema<br>
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

    // Preferências de envio de email
    final EditText editUsuario = (EditText) findViewById(R.id.editUsuario);
    final EditText editSenha = (EditText) findViewById(R.id.editSenha);
    final EditText editServidorSMTP = (EditText) findViewById(R.id.editServidorSMTP);
    final EditText editServidorSMTPPorta = (EditText) findViewById(R.id.editServidorSMTPPorta);
    final EditText editRemetente = (EditText) findViewById(R.id.editRemetente);

    final CheckBox chkDebugavel = (CheckBox) findViewById(R.id.chkDebugavel);
    final CheckBox chkAuth = (CheckBox) findViewById(R.id.chkAuth);
    final CheckBox chkSsl = (CheckBox) findViewById(R.id.chkSsl);

    // Botões 
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

    boolean checked;
    String s;
    
    s = preferences.getString(Constantes.PREFERENCIAS_EMAIL_DEBUG, "");
    if (s.equalsIgnoreCase("true")) {
      checked = true;
    }
    else {
      checked = false;
    }
    chkDebugavel.setChecked(checked);

    
    s = preferences.getString(Constantes.PREFERENCIAS_EMAIL_AUTH, "");
    if (s.equalsIgnoreCase("true")) {
      checked = true;
    }
    else {
      checked = false;
    }
    chkAuth.setChecked(checked);


    s = preferences.getString(Constantes.PREFERENCIAS_EMAIL_SSL, "");    
    checked = convertStrToBoolean(s); 
    chkSsl.setChecked(checked);

    // Hide soft-keyboard:
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
        
        edit.putString(Constantes.PREFERENCIAS_EMAIL_DEBUG, ((chkDebugavel.isChecked()==true)?"true":"false"));
        edit.putString(Constantes.PREFERENCIAS_EMAIL_AUTH, ((chkAuth.isChecked()==true)?"true":"false"));
        edit.putString(Constantes.PREFERENCIAS_EMAIL_SSL, ((chkSsl.isChecked()==true)?"true":"false"));

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

  /**
   * Converte um valor string em um valor booleano
   * 
   * @param str um string contendo os valores true ou false
   * 
   * @return true caso a string contenha o valor true ou false caso contrário
   */
  private static boolean convertStrToBoolean(String str) {
    
    boolean b;
    
    b = ((str.equalsIgnoreCase("true"))) ? true:false;
        
    return b;
    
  }
  
}
