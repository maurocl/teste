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
 * Criação da tela para obter os dados do participante do evento.<br>
 * 
 * Essa activity recebe como parâmetros:
 * <ul>
 * <li>Evento;
 * <li>Participante;
 * </ul>
 * 
 * O usuário poderá Confirmar ou Cancelar a participação.<br>
 * 
 * @author maurocl
 * 
 */
public class ParticipanteActivity extends Activity implements Constantes {

  private static int DEBUG = 1;

  private static final String TAG = "ParticipanteActivity";

  private Evento mEvento;

  private Participante mParticipante;

  // TODO aqui é necessário uma instância da mEvento
  // para que possamos saber quais campos adicionais estão
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

    // obtém informações sobre a intent usada para iniciar a activity
    Intent intent = getIntent();

    // valida os parâmetros recebidos para execução da intent
    validaParametros(intent);

    // --------------------------------

    // EditText
    final EditText editNome = (EditText) findViewById(R.id.editNome);
    final EditText editEmail = (EditText) findViewById(R.id.editEmail);
    final EditText editTelefone = (EditText) findViewById(R.id.editTelefone);

    // Botões
    final Button btnEnviar = (Button) findViewById(R.id.button1);
    final Button btnCancelar = (Button) findViewById(R.id.button2);

    // ------------------------
    // Parâmetros opcionais
    // ------------------------

    // int[] params = new int[] { INVISIVEL, INVISIVEL, INVISIVEL, INVISIVEL,
    // INVISIVEL };
    int[] params = new int[] { android.view.View.GONE, android.view.View.GONE, android.view.View.GONE, android.view.View.GONE,
        android.view.View.GONE };

    if ((mEvento != null) && (mEvento.getParametros() != null)) {

      // exibe os botões referentes aos parâmetros opcionais (se eles estiverem configurados)
      exibeBotoesOpcionais(params);

    }

    // Atualiza os parâmetros opcionais
    updateOptionalParamFields(params);

    // =========================================

    if (DEBUG == 1) {
      // Pré inicializa um participante
      editNome.setText("Mauro Cesar Lopes");
      editEmail.setText("maurocl@terra.com.br");
      editTelefone.setText("81438978");
    }

    // =========================================

    /**
     * Tratamento do botão Enviar
     */
    btnEnviar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Log.d(TAG, "onCreate(btnEnviar) - *** Botão Enviar do Participante foi selecionado ***");

        // cria uma nova instância da classe Participante
        Participante novoParticipante = new Participante(editNome.getText().toString(), editEmail.getText().toString(),
            editTelefone.getText().toString());

        Log.d(TAG, "onCreate(btnEnviar) - Participante: " + novoParticipante);

        // ---------------------------------------------------------
        // Criação da Intent com resultado da execução da Activity
        // ---------------------------------------------------------
        Intent it = new Intent();

        it.putExtra(Constantes.PARTICIPANTE, novoParticipante);

        setResult(RESULT_OK, it);

        finish();

      }
    });

    /**
     * Tratamento do evento de click no botão Cancelar
     */
    btnCancelar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Log.d(TAG, "onCreate(btnCancelar) - Botão Cancelar selecionado");

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

        Log.d(TAG, "onCreate() - Botão cancelar selecionado");

        // ---------------------------------------------------------
        // Criação da Intent com resultado da execução da Activity
        // ---------------------------------------------------------
        Intent it = new Intent();

        setResult(RESULT_CANCELED, it);

        finish();

      }
    });

  }

  /**
   * Valida os parâmetros recebido da intent "chamadora"
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
   * Um evento poderá contar com até cinco campos extras (opcionais).
   * 
   * @param params
   *          Um array onde cada posição indica se o botão correspondente deverá
   *          estar visível ou oculto.
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

    // Parâmetro 1
    if (mEvento.getParametros().getParametro(0) != null) {
      params[0] = android.view.View.VISIBLE;
    } else {
      params[0] = android.view.View.GONE;
    }

    // Parâmetro 2
    if (mEvento.getParametros().getParametro(1) != null) {
      params[1] = android.view.View.VISIBLE;
    } else {
      params[1] = android.view.View.GONE;
    }

    // Parâmetro 3
    if (mEvento.getParametros().getParametro(2) != null) {
      params[2] = android.view.View.VISIBLE;
    } else {
      params[2] = android.view.View.GONE;
    }

    // Parâmetro 4
    if (mEvento.getParametros().getParametro(3) != null) {
      params[3] = android.view.View.VISIBLE;
    } else {
      params[3] = android.view.View.GONE;
    }

    // Parâmetro 5
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
   * Recebe um vetor onde cada elemento indica se o parâmetro será visível ou
   * não
   * 
   * @param params
   *          vetor de inteiros contendo a visibilidade de cada parâmetro
   *          adicional
   */
  private void updateOptionalParamFields(int[] params) {

    // vetor de EditText (nome dos parâmetros)
    EditText[] editText = new EditText[5];

    // vetor TestView (valor dos parâmetros)
    TextView[] textView = new TextView[5];

    // TextView indicando o nome do parâmetro
    textView[0] = (TextView) findViewById(R.id.tvParam1);
    textView[1] = (TextView) findViewById(R.id.tvParam2);
    textView[2] = (TextView) findViewById(R.id.tvParam3);
    textView[3] = (TextView) findViewById(R.id.tvParam4);
    textView[4] = (TextView) findViewById(R.id.tvParam5);

    // EditText (conterão o nome dos parâmetros)
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
