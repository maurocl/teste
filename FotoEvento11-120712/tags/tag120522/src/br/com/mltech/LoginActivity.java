package br.com.mltech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * Activity Login
 * 
 * Respons�vel por pegar as informa��es de login e senha para acesso a telas
 * protegidas.
 * 
 * @author maurocl
 * 
 */
public class LoginActivity extends Activity {

  private static final String TAG = "LoginActivity";

  private static final String USER = "root";
  private static final String PASS = "root";

  private boolean mUsuarioValidado;

  /**
   * onCreate(Bundle savedInstanceState)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.login);

    final EditText usuario = (EditText) findViewById(R.id.usuario);
    final EditText senha = (EditText) findViewById(R.id.senha);

    final Button btnLogin = (Button) findViewById(R.id.btnLogin);
    final Button btnCancelar = (Button) findViewById(R.id.btnCancelar);

    // Trata o click do bot�o login
    btnLogin.setOnClickListener(new OnClickListener() {

      /**
       * 
       */
      public void onClick(View v) {
       
        Log.d(TAG, "Bot�o Login clicado");

        final String sUsuario = usuario.getText().toString().toLowerCase();

        final String sSenha = senha.getText().toString().toLowerCase();

        Log.d(TAG, "==> usuario: " + sUsuario);
        Log.d(TAG, "==> senha: " + sSenha);

        mUsuarioValidado = verificaUsuarioSenha(sUsuario, sSenha);

        Intent it = new Intent();

        if (mUsuarioValidado) {
          it.putExtra("br.com.mltech.usuarioValidado", "OK");
          Log.d(TAG, "Usu�rio validado");
        } else {
          it.putExtra("br.com.mltech.usuarioValidado", "FALHOU");
          Log.d(TAG, "Usu�rio N�O validado");
        }

        it.putExtra("br.com.mltech.usuario", usuario.getText().toString());
        it.putExtra("br.com.mltech.senha", senha.getText().toString());

        setResult(RESULT_OK, it);

        finish();

      }

      // TODO verificar o que fazer - remover
      /**
       * verificaUsuarioSenha
       * 
       * @param usuario
       * @param senha
       * 
       * @return true caso o usu�rio e senha estejam corretas ou false caso
       *         contr�rio.
       */
      /*
       * private boolean verificaUsuarioSenha(String usuario, String senha) {
       * 
       * boolean b;
       * 
       * if ("root".equals(usuario) && "root".equals(senha)) { // us�rio
       * validado Log.d(TAG, "Usu�rio validado com sucesso !"); b = true; } else
       * { // usu�rio n�o � v�lido Log.w(TAG, "Usu�rio n�o v�lido !"); b =
       * false; }
       * 
       * return b; }
       */

    });

    // Trata o click do bot�o cancelar
    btnCancelar.setOnClickListener(new OnClickListener() {

      /**
       * 
       */
      public void onClick(View v) {

        Log.d(TAG, "Bot�o Cancelar clicado");
        Intent it = new Intent();
        setResult(RESULT_CANCELED, it);
        finish();

      }
    });

  }

  /**
   * verificaUsuarioSenha
   * 
   * @param usuario
   * @param senha
   * 
   * @return true caso o usu�rio e senha estejam corretas ou false caso
   *         contr�rio.
   */
  private boolean verificaUsuarioSenha(String usuario, String senha) {

    boolean b;

    if (USER.equals(usuario) && PASS.equals(senha)) {
      // us�rio validado
      Log.d(TAG, "Usu�rio validado com sucesso !");
      b = true;
    } else {
      // usu�rio n�o � v�lido
      Log.w(TAG, "Usu�rio n�o v�lido !");
      b = false;
    }

    return b;

  }

}
