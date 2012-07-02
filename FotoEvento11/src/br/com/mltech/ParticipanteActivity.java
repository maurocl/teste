package br.com.mltech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import br.com.mltech.modelo.Evento;
import br.com.mltech.modelo.Participante;

/**
 * Participante.java
 * 
 * <p>
 * Cria��o da tela para obter os dados do participante do evento.<br>
 * 
 * Essa activity recebe como par�metros:
 * <ul>
 * <li>Evento;
 * <li>Participante;
 * </ul>
 * 
 * O usu�rio poder� Confirmar ou Cancelar a participa��o.<br>
 * 
 * @author maurocl
 * 
 */
public class ParticipanteActivity extends Activity implements Constantes {

  private static int DEBUG = 1;

  private static final String TAG = "ParticipanteActivity";

  private Evento mEvento;

  private Participante mParticipante;

  // TODO aqui � necess�rio uma inst�ncia da mEvento
  // para que possamos saber quais campos adicionais est�o
  // habilitados

  /**
   * onCreate(Bundle savedInstanceState)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    this.setContentView(R.layout.participante);

    /**
     * 
     */
    Log.d(TAG, "*** onCreate() ***");

    // obt�m informa��es sobre a intent usada para iniciar a activity
    Intent intent = getIntent();

    // valida os par�metros recebidos para execu��o da intent
    validaParametros(intent);

    // --------------------------------

    // EditText
    final EditText editNome = (EditText) findViewById(R.id.editNome);
    final EditText editEmail = (EditText) findViewById(R.id.editEmail);
    final EditText editTelefone = (EditText) findViewById(R.id.editTelefone);

    // Bot�es
    final Button btnEnviar = (Button) findViewById(R.id.button1);
    final Button btnCancelar = (Button) findViewById(R.id.button2);

    // ------------------------
    // Par�metros opcionais
    // ------------------------

    // int[] params = new int[] { INVISIVEL, INVISIVEL, INVISIVEL, INVISIVEL,
    // INVISIVEL };
    int[] params = new int[] { android.view.View.GONE, android.view.View.GONE, android.view.View.GONE, android.view.View.GONE,
        android.view.View.GONE };

    if ((mEvento != null) && (mEvento.getParametros() != null)) {

      // exibe os bot�es referentes aos par�metros opcionais (se eles estiverem configurados)
      exibeBotoesOpcionais(params);

    }

    // Atualiza os par�metros opcionais
    updateOptionalParamFields(params);

    // =========================================

    if (DEBUG == 1) {
      // Pr� inicializa um participante
      editNome.setText("Mauro Cesar Lopes");
      editEmail.setText("maurocl@terra.com.br");
      editTelefone.setText("81438978");
    }

    // =========================================

    /**
     * Tratamento do bot�o Enviar
     */
    btnEnviar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Log.d(TAG, "onCreate(btnEnviar) - *** Bot�o Enviar do Participante foi selecionado ***");

        // cria uma nova inst�ncia da classe Participante
        Participante novoParticipante = new Participante(editNome.getText().toString(), editEmail.getText().toString(),
            editTelefone.getText().toString());

        Log.d(TAG, "onCreate(btnEnviar) - Participante: " + novoParticipante);

        // ---------------------------------------------------------
        // Cria��o da Intent com resultado da execu��o da Activity
        // ---------------------------------------------------------
        Intent it = new Intent();

        it.putExtra(Constantes.PARTICIPANTE, novoParticipante);

        setResult(RESULT_OK, it);

        finish();

      }
    });

    /**
     * Tratamento do evento de click no bot�o Cancelar
     */
    btnCancelar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Log.d(TAG, "onCreate(btnCancelar) - Bot�o Cancelar selecionado");

        // retorna todos os campos para seus valores iniciais.

        editNome.setText("");
        editEmail.setText("");
        editTelefone.setText("");

        /*
         * editParam1.setText(""); editParam2.setText("");
         * editParam3.setText(""); editParam4.setText("");
         * editParam5.setText("");
         */

        // TODO falta limpar os checkboxes

        Log.d(TAG, "onCreate() - Bot�o cancelar selecionado");

        // ---------------------------------------------------------
        // Cria��o da Intent com resultado da execu��o da Activity
        // ---------------------------------------------------------
        Intent it = new Intent();

        setResult(RESULT_CANCELED, it);

        finish();

      }
    });

  }

  /**
   * Valida os par�metros recebido da intent "chamadora"
   * 
   * @param intent
   *          Intent chamadora da activity.
   * 
   */
  private void validaParametros(Intent intent) {

    if (intent == null) {
      return;
    }

    // evento
    if (intent.hasExtra(Constantes.EVENTO)) {

      mEvento = (Evento) intent.getSerializableExtra(Constantes.EVENTO);

      Log.w(TAG, "validaParametros() - mEvento=" + mEvento);

    }

    // participante
    if (intent.hasExtra(Constantes.PARTICIPANTE)) {

      mParticipante = (Participante) intent.getSerializableExtra(Constantes.PARTICIPANTE);

    }

    if (mParticipante == null) {

      mParticipante = new Participante();
      Log.d(TAG, "validaParametros() - Criando um novo Participante");

    } else {

      Log.d(TAG, "validaParametros() - Participante carregado com sucesso: " + mParticipante);

    }

  }

  /**
   * Um evento poder� contar com at� cinco campos extras (opcionais).
   * 
   * @param params
   *          Um array onde cada posi��o indica se o bot�o correspondente dever�
   *          estar vis�vel ou oculto.
   * 
   */
  private void exibeBotoesOpcionais(int[] params) {

    /*
     * for(int i =0; i<5;i++) {
     * 
     * if (mEvento.getParametros().getParametro(i) != null) { params[i] =
     * android.view.View.VISIBLE; } else { params[i] = android.view.View.GONE; }
     * 
     * }
     */

    // Par�metro 1
    if (mEvento.getParametros().getParametro(0) != null) {
      params[0] = android.view.View.VISIBLE;
    } else {
      params[0] = android.view.View.GONE;
    }

    // Par�metro 2
    if (mEvento.getParametros().getParametro(1) != null) {
      params[1] = android.view.View.VISIBLE;
    } else {
      params[1] = android.view.View.GONE;
    }

    // Par�metro 3
    if (mEvento.getParametros().getParametro(2) != null) {
      params[2] = android.view.View.VISIBLE;
    } else {
      params[2] = android.view.View.GONE;
    }

    // Par�metro 4
    if (mEvento.getParametros().getParametro(3) != null) {
      params[3] = android.view.View.VISIBLE;
    } else {
      params[3] = android.view.View.GONE;
    }

    // Par�metro 5
    if (mEvento.getParametros().getParametro(4) != null) {
      params[4] = android.view.View.VISIBLE;
    } else {
      params[4] = android.view.View.GONE;
    }
  }

  /**
   * 
   */
  @Override
  protected void onStart() {

    super.onStart();
    Log.d(TAG, "*** onStart() ***");
  }

  /**
   * 
   */
  @Override
  protected void onResume() {

    super.onResume();
    Log.d(TAG, "*** onResume() ***");
  }

  /**
   * 
   */
  @Override
  protected void onPause() {

    super.onPause();
    Log.d(TAG, "*** onPause() ***");
  }

  /**
   * 
   */
  @Override
  protected void onStop() {

    super.onStop();
    Log.d(TAG, "*** onStop() ***");
  }

  /**
   * 
   */
  @Override
  protected void onRestart() {

    super.onRestart();
    Log.d(TAG, "*** onRestart() ***");
  }

  /**
   * 
   */
  @Override
  protected void onDestroy() {

    super.onDestroy();
    Log.d(TAG, "*** onDestroy() ***");
  }

  /**
   * Recebe um vetor onde cada elemento indica se o par�metro ser� vis�vel ou
   * n�o
   * 
   * @param params
   *          vetor de inteiros contendo a visibilidade de cada par�metro
   *          adicional
   */
  private void updateOptionalParamFields(int[] params) {

    // vetor de EditText (nome dos par�metros)
    EditText[] editText = new EditText[5];

    // vetor TestView (valor dos par�metros)
    TextView[] textView = new TextView[5];

    // TextView indicando o nome do par�metro
    textView[0] = (TextView) findViewById(R.id.tvParam1);
    textView[1] = (TextView) findViewById(R.id.tvParam2);
    textView[2] = (TextView) findViewById(R.id.tvParam3);
    textView[3] = (TextView) findViewById(R.id.tvParam4);
    textView[4] = (TextView) findViewById(R.id.tvParam5);

    // EditText (conter�o o nome dos par�metros)
    editText[0] = (EditText) findViewById(R.id.editParam1);
    editText[1] = (EditText) findViewById(R.id.editParam2);
    editText[2] = (EditText) findViewById(R.id.editParam3);
    editText[3] = (EditText) findViewById(R.id.editParam4);
    editText[4] = (EditText) findViewById(R.id.editParam5);

    for (int i = 0; i < params.length; i++) {

      if (mEvento.getParametros() != null) {

        if ((mEvento.getParametros().getParametro(i) != null) && (!mEvento.getParametros().getParametro(i).equals(""))) {

          textView[i].setText(mEvento.getParametros().getParametro(i));
          textView[i].setVisibility(android.view.View.VISIBLE);
          editText[i].setVisibility(android.view.View.VISIBLE);

        } else {

          textView[i].setVisibility(android.view.View.GONE);
          editText[i].setVisibility(android.view.View.GONE);

        }

      }

    }

  }

}
