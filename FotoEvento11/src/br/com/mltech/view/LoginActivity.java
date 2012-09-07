package br.com.mltech.view;

import br.com.mltech.Constantes;
import br.com.mltech.R;
import br.com.mltech.R.id;
import br.com.mltech.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/** 
 * Activity respons�vel por pegar as informa��es de login e senha para acesso a telas
 * protegidas.
 * 
 * @author maurocl
 * 
 */
public class LoginActivity extends Activity implements OnClickListener, Constantes {

  private static final String TAG = "LoginActivity";

  private static final String USER = "root";

  private static final String PASS = "root";

  private static boolean mUsuarioValidado;

  Button btnLogin;

  Button btnCancelar;

  EditText usuario;

  EditText senha;

  /**
   * onCreate(Bundle savedInstanceState)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.login);

    usuario = (EditText) findViewById(R.id.usuario);
    senha = (EditText) findViewById(R.id.senha);

    Button btnLogin = (Button) findViewById(R.id.btnLogin);
    btnLogin.setOnClickListener(this);

    Button btnCancelar = (Button) findViewById(R.id.btnCancelar);
    btnCancelar.setOnClickListener(this);

  }

  /**
   * onClick(View v)
   * 
   * Trata os eventos dos bot�es
   * 
   * @param v
   *          View
   */
  public void onClick(View v) {

    if (v == btnLogin) {
      processaLogin();
    } else if (v == btnCancelar) {
      processaCancelar();
    }

  }

  /**
   * processaLogin()
   * 
   * Processa a��o do bot�o login
   * 
   */
  private void processaLogin() {

    Log.d(TAG, "onClick() - Bot�o Login clicado");

    final String sUsuario = usuario.getText().toString().toLowerCase();

    final String sSenha = senha.getText().toString().toLowerCase();

    Log.d(TAG, "onClick() ==> usuario: " + sUsuario);
    Log.d(TAG, "onClick() ==> senha: " + sSenha);

    mUsuarioValidado = verificaUsuarioSenha(sUsuario, sSenha);

    Intent it = new Intent();

    if (mUsuarioValidado) {
      it.putExtra(USUARIOVALIDADO, "OK");
      Log.d(TAG, "onClick() - Usu�rio validado");
    } else {
      it.putExtra(USUARIOVALIDADO, "FALHOU");
      Log.d(TAG, "onClick() - Usu�rio N�O validado");
    }

    it.putExtra(USUARIO, usuario.getText().toString());
    it.putExtra(SENHA, senha.getText().toString());

    setResult(RESULT_OK, it);

    finish();

  }

  /**
   * processaCancelar()
   * 
   * Processa a��o do bot�o cancelar
   * 
   */
  public void processaCancelar() {

    Log.d(TAG, "onClick() - Bot�o Cancelar clicado");
    Intent it = new Intent();
    setResult(RESULT_CANCELED, it);
    finish();

  }

  /**
   * verificaUsuarioSenha(String usuario, String senha)
   * 
   * @param usuario nome do usu�rio
   * @param senha password do usu�rio
   * 
   * @return true caso o usu�rio e senha estejam corretas ou false caso
   *         contr�rio.
   */
  private boolean verificaUsuarioSenha(String usuario, String senha) {

    boolean usuarioValidado;

    if (USER.equals(usuario) && PASS.equals(senha)) {
      // us�rio validado
      Log.d(TAG, "verificaUsuarioSenha() - usu�rio validado com sucesso !");
      usuarioValidado = true;
    } else {
      // usu�rio n�o � v�lido
      Log.w(TAG, "verificaUsuarioSenha() - usu�rio n�o v�lido !");
      usuarioValidado = false;
    }

    return usuarioValidado;

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
   * if ("root".equals(usuario) && "root".equals(senha)) { // us�rio validado
   * Log.d(TAG, "Usu�rio validado com sucesso !"); b = true; } else { // usu�rio
   * n�o � v�lido Log.w(TAG, "Usu�rio n�o v�lido !"); b = false; }
   * 
   * return b; }
   */

}
