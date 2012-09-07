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
 * Activity responsável por pegar as informações de login e senha para acesso a telas
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
   * Trata os eventos dos botões
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
   * Processa ação do botão login
   * 
   */
  private void processaLogin() {

    Log.d(TAG, "onClick() - Botão Login clicado");

    final String sUsuario = usuario.getText().toString().toLowerCase();

    final String sSenha = senha.getText().toString().toLowerCase();

    Log.d(TAG, "onClick() ==> usuario: " + sUsuario);
    Log.d(TAG, "onClick() ==> senha: " + sSenha);

    mUsuarioValidado = verificaUsuarioSenha(sUsuario, sSenha);

    Intent it = new Intent();

    if (mUsuarioValidado) {
      it.putExtra(USUARIOVALIDADO, "OK");
      Log.d(TAG, "onClick() - Usuário validado");
    } else {
      it.putExtra(USUARIOVALIDADO, "FALHOU");
      Log.d(TAG, "onClick() - Usuário NÃO validado");
    }

    it.putExtra(USUARIO, usuario.getText().toString());
    it.putExtra(SENHA, senha.getText().toString());

    setResult(RESULT_OK, it);

    finish();

  }

  /**
   * processaCancelar()
   * 
   * Processa ação do botão cancelar
   * 
   */
  public void processaCancelar() {

    Log.d(TAG, "onClick() - Botão Cancelar clicado");
    Intent it = new Intent();
    setResult(RESULT_CANCELED, it);
    finish();

  }

  /**
   * verificaUsuarioSenha(String usuario, String senha)
   * 
   * @param usuario nome do usuário
   * @param senha password do usuário
   * 
   * @return true caso o usuário e senha estejam corretas ou false caso
   *         contrário.
   */
  private boolean verificaUsuarioSenha(String usuario, String senha) {

    boolean usuarioValidado;

    if (USER.equals(usuario) && PASS.equals(senha)) {
      // usário validado
      Log.d(TAG, "verificaUsuarioSenha() - usuário validado com sucesso !");
      usuarioValidado = true;
    } else {
      // usuário não é válido
      Log.w(TAG, "verificaUsuarioSenha() - usuário não válido !");
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
   * @return true caso o usuário e senha estejam corretas ou false caso
   *         contrário.
   */
  /*
   * private boolean verificaUsuarioSenha(String usuario, String senha) {
   * 
   * boolean b;
   * 
   * if ("root".equals(usuario) && "root".equals(senha)) { // usário validado
   * Log.d(TAG, "Usuário validado com sucesso !"); b = true; } else { // usuário
   * não é válido Log.w(TAG, "Usuário não válido !"); b = false; }
   * 
   * return b; }
   */

}
