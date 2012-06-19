package br.com.mltech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import br.com.mltech.modelo.Evento;
import br.com.mltech.modelo.Participacao;
import br.com.mltech.modelo.Participante;

/**
 * @class Participacao.java
 * 
 *        Cria��o da tela para obter informa��es sobre o tipo e o efeito que
 *        ser� aplicado a uma foto.<br />
 * 
 *        Essa activity recebe como par�metros: Evento; Participante;
 *        Participa��o<br />
 * 
 *        O usu�rio poder� Confirmar ou Cancelar a participa��o.
 * 
 * @author maurocl
 * 
 */
public class ParticipacaoActivity extends Activity implements Constantes {

  private static final String TAG = "ParticipacaoActivity";

  private static int DEBUG = 0;

  private static boolean DEBUGON = false;

  // tipo da foto: polaroid ou cabine
  private int tipoFoto = -1;

  // efeito aplicado a foto: colorido ou p&b
  private int efeitoFoto = -1;

  // evento
  private Evento mEvento;

  // participante
  private Participante mParticipante;

  // participacao
  private Participacao mParticipacao;

  // TODO aqui � necess�rio uma inst�ncia da mEvento
  // para que possamos saber quais campos adicionais est�o
  // habilitados

  /**
   * onCreate(Bundle savedInstanceState)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    this.setContentView(R.layout.participacao);

 
    Log.d(TAG, "*** onCreate() ***");

    // obt�m informa��es sobre a intent usada para iniciar a activity
    Intent intent = getIntent();

    // carrea as vari�veis iniciais
    iniciarTela(intent);

    // --------------------------------

    // EditText
    final EditText editNome = (EditText) findViewById(R.id.editNome);
    final EditText editEmail = (EditText) findViewById(R.id.editEmail);
    final EditText editTelefone = (EditText) findViewById(R.id.editTelefone);

    // atualiza a tela com os dados recebidos do participante
    editNome.setText(mParticipante.getNome());
    editEmail.setText(mParticipante.getEmail());
    editTelefone.setText(mParticipante.getTelefone());

    // Radio Groups
    final RadioGroup groupFormatoFoto = (RadioGroup) findViewById(R.id.group1);
    final RadioGroup groupEfeitoFoto = (RadioGroup) findViewById(R.id.group2);

    // Bot�es
    final Button btnEnviar = (Button) findViewById(R.id.button1);
    final Button btnCancelar = (Button) findViewById(R.id.button2);

    //-------------------------------------------------------------------------
    // Tratamento de Evento dos Bot�es
    //-------------------------------------------------------------------------
    
    /**
     * Tratamento do bot�o Enviar
     */
    btnEnviar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        if (DEBUGON) {
          Log.d(TAG, "onCreate(btnEnviar) - *** Bot�o Enviar do Participante foi selecionado ***");

          Log.v(TAG, "onCreate(btnEnviar) - groupFormatoFoto.isSelected()=" + groupFormatoFoto.isSelected());

          Log.v(TAG, "onCreate(btnEnviar) - groupEfeitoFoto.isSelected()=" + groupEfeitoFoto.isSelected());
        }

        // Escolhe o formato da foto (Polaroid e/ou Cabine)
        boolean radio1isChecked = ((RadioButton) findViewById(R.id.radioOp1)).isChecked();
        boolean radio2isChecked = ((RadioButton) findViewById(R.id.radioOp2)).isChecked();

        // Escolhe o efeito de cores
        boolean radioCorIsChecked = ((RadioButton) findViewById(R.id.radioCor)).isChecked();
        boolean radioPbIsChecked = ((RadioButton) findViewById(R.id.radioPB)).isChecked();

        if (DEBUGON) {
          Log.v(TAG, "onCreate(btnEnviar) - radio1isChecked=" + radio1isChecked);
          Log.v(TAG, "onCreate(btnEnviar) - radio2isChecked=" + radio2isChecked);

          Log.v(TAG, "onCreate(btnEnviar) - radioCorIsChecked=" + radioCorIsChecked);
          Log.v(TAG, "onCreate(btnEnviar) - radioPbIsChecked=" + radioPbIsChecked);
        }

        // define o tipo da foto
        tipoFoto = (radio1isChecked ? Constantes.TIPO_FOTO_POLAROID : Constantes.TIPO_FOTO_CABINE);

        // define o efeito da foto
        efeitoFoto = (radioCorIsChecked ? Constantes.CORES : Constantes.PB);

        // cria uma nova inst�ncia da classe Participa��o
        mParticipacao = new Participacao(mParticipante, tipoFoto, efeitoFoto, null);

        if (DEBUGON) {
          Log.d(TAG, "onCreate(btnEnviar) - Participacao: " + mParticipacao);
        }

        // ---------------------------------------------------------
        // Cria��o da Intent com resultado da execu��o da Activity
        // ---------------------------------------------------------
        Intent it = new Intent();

        it.putExtra(Constantes.PARTICIPACAO, mParticipacao);

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
   * iniciarTela(Intent intent)
   * 
   * @param intent
   *          intent recebida da activity chamadora
   * 
   */
  private void iniciarTela(Intent intent) {

    if (intent == null) {
      // retorna caso nenhum par�metro seja recibido.
      return;
    }

    // verifica a inst�ncia do evento
    if (intent.hasExtra(Constantes.EVENTO)) {

      mEvento = (Evento) intent.getSerializableExtra(Constantes.EVENTO);

      //Log.w(TAG, "iniciarTela() - mEvento=" + mEvento);

    }

    // verifica a inst�ncia do participante
    if (intent.hasExtra(Constantes.PARTICIPANTE)) {

      mParticipante = (Participante) intent.getSerializableExtra(Constantes.PARTICIPANTE);

    }

    // verifica a inst�ncia da participacao
    if (intent.hasExtra(Constantes.PARTICIPACAO)) {

      mParticipacao = (Participacao) intent.getSerializableExtra(Constantes.PARTICIPACAO);

    }

    // se a participa��o for nula ent�o cria uma inst�ncia da classe Participacao para ser preenchida.
    if (mParticipacao == null) {

      mParticipacao = new Participacao();

      Log.d(TAG, "iniciarTela() - Criando uma nova Participacao");

    } else {

      Log.d(TAG, "iniciarTela() - Participa��o carregada com sucesso: " + mParticipacao);

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

}
