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
import android.widget.TextView;
import br.com.mltech.modelo.Evento;
import br.com.mltech.modelo.Participacao;
import br.com.mltech.modelo.Participante;

/**
 * Participante.java
 * 
 * @author maurocl
 * 
 *         Criação da tela para obter os dados do participante do evento
 * 
 */
public class ParticipanteActivity extends Activity {

  private static int DEBUG = 0;

  private static final String TAG = "ParticipanteActivity";

  // Definição de constantes para o tipo de foto
  private static final int POLAROID = 1;
  private static final int CABINE = 2;

  // Definição de constantes para o efeito de cores
  private static final int CORES = 11;
  private static final int PB = 12;

  private int tipoFoto = -1;

  private int efeitoFoto = -1;

  private Evento mEvento;
  private Participante mParticipante;
  private Participacao mParticipacao;

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

    Intent intent = getIntent();

    //Log.i(TAG, "onCreate() - intent.getActtion()=" + intent.getAction());

    if (intent != null) {

      if (intent.getSerializableExtra("br.com.mltech.evento") != null) {

        mEvento = (Evento) intent.getSerializableExtra("br.com.mltech.evento");

        Log.w(TAG, "onCreate() - mEvento=" + mEvento);

      }

      if (intent.getSerializableExtra("br.com.mltech.participante") != null) {

        mParticipante = (Participante) intent.getSerializableExtra("br.com.mltech.participante");

      }

      if (intent.getSerializableExtra("br.com.mltech.participacao") != null) {

        mParticipacao = (Participacao) intent.getSerializableExtra("br.com.mltech.participacao");

      }

      if (mParticipante == null) {
        mParticipante = new Participante();
        Log.w(TAG, "onCreate() - Criando um novo Participante");
      }
      else {

        Log.d(TAG, "onCreate() - Participante carregado com sucesso: " + mParticipante);

      }

      if (mParticipacao == null) {

        mParticipacao = new Participacao();
        Log.w(TAG, "onCreate() - Criando uma nova Participacao");

      } else {

        Log.d(TAG, "onCreate() - Participação carregada com sucesso: " + mParticipacao);

      }

    }

    // --------------------------------

    final EditText editNome = (EditText) findViewById(R.id.editNome);
    final EditText editEmail = (EditText) findViewById(R.id.editEmail);
    final EditText editTelefone = (EditText) findViewById(R.id.editTelefone);

    /* Parâmetros opcionais */
    // int[] params = new int[] { INVISIVEL, INVISIVEL, INVISIVEL, INVISIVEL, INVISIVEL };
    int[] params = new int[] { android.view.View.GONE, android.view.View.GONE, android.view.View.GONE, android.view.View.GONE,
        android.view.View.GONE };

    if (mEvento != null) {

      if (mEvento.getParametros() != null) {

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

    }

    // Atualiza os parâmetros opcionais
    updateOptionalParamFields(params);

    final RadioGroup groupFormatoFoto = (RadioGroup) findViewById(R.id.group1);
    final RadioGroup groupEfeitoFoto = (RadioGroup) findViewById(R.id.group2);

    final Button btnEnviar = (Button) findViewById(R.id.button1);
    final Button btnCancelar = (Button) findViewById(R.id.button2);

    // =========================================

    if (DEBUG == 1) {
      // Pré inicializa um participante
      editNome.setText("Mauro Cesar Lopes");
      editEmail.setText("maurocl@terra.com.br");
      editTelefone.setText("81438978");

      ((RadioButton) findViewById(R.id.radioCor)).setChecked(true);

      ((RadioButton) findViewById(R.id.radioOp1)).setChecked(true);
    }

    // =========================================

    /**
     * Tratamento do botão Enviar
     */
    btnEnviar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Log.d(TAG, "onCreate() - *** Botão Enviar do Participante foi selecionado ***");

        Log.d(TAG, "onCreate() - groupFormatoFoto.isSelected()=" + groupFormatoFoto.isSelected());

        Log.d(TAG, "onCreate() - groupEfeitoFoto.isSelected()=" + groupEfeitoFoto.isSelected());

        boolean radio1isChecked = ((RadioButton) findViewById(R.id.radioOp1)).isChecked();
        boolean radio2isChecked = ((RadioButton) findViewById(R.id.radioOp2)).isChecked();

        boolean radioCorIsChecked = ((RadioButton) findViewById(R.id.radioCor)).isChecked();
        boolean radioPbIsChecked = ((RadioButton) findViewById(R.id.radioPB)).isChecked();

        
        Log.d(TAG, "onCreate() - radio1isChecked=" + radio1isChecked);
        Log.d(TAG, "onCreate() - radio2isChecked=" + radio2isChecked);
        
        Log.d(TAG, "onCreate() - radioCorIsChecked=" + radioCorIsChecked);
        Log.d(TAG, "onCreate() - radioPbIsChecked=" + radioPbIsChecked);
        
        
        Participante novoParticipante = new Participante(editNome.getText().toString(), editEmail.getText().toString(),
            editTelefone.getText().toString());

        mParticipacao = new Participacao(novoParticipante, tipoFoto, efeitoFoto, null);

        Log.d(TAG, "onCreate() - Participante: " + novoParticipante);
        Log.d(TAG, "onCreate() - Participacao: " + mParticipacao);

        // Criação da Intent com resultado da execução da Activity
        Intent it = new Intent();

        it.putExtra("br.com.mltech.participante", novoParticipante);
        it.putExtra("br.com.mltech.participacao", mParticipacao);

        setResult(RESULT_OK, it);

        finish();

      }
    });

    /**
     * Tratamento do evento de click no botão Cancelar
     */
    btnCancelar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Log.d(TAG, "onCreate() - Botão Cancelar selecionado");

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
        Intent it = new Intent();

        setResult(RESULT_CANCELED, it);

        finish();

      }
    });

    /**
     * Obtem a opção do formato da foto: Polaroid ou Cabine
     */
    groupFormatoFoto.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

      /**
       * 
       */
      public void onCheckedChanged(RadioGroup group, int checkedId) {

        boolean opcaoPolaroid = R.id.radioOp1 == checkedId;
        boolean opcaoCabine = R.id.radioOp2 == checkedId;

        if (opcaoPolaroid) {

          Log.d(TAG, "onCreate() - Opção Polaroid escolhida");
          tipoFoto = POLAROID;

        } else if (opcaoCabine) {

          Log.d(TAG, "onCreate() - Opção Cabine escolhida");
          tipoFoto = CABINE;

        }
                
        //int buttonId = group.getCheckedRadioButtonId();
        //Log.w(TAG,"onCreate() - Polaroid ou Cabine = buttonId="+buttonId);
        
      }
    });

    /**
     * Obtém o efeito de cores desejado: Core ou PB (Preto e Branco)
     * 
     */
    groupEfeitoFoto.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

      /**
       * 
       */
      public void onCheckedChanged(RadioGroup group, int checkedId) {

        boolean cor = R.id.radioCor == checkedId;
        boolean pb = R.id.radioPB == checkedId;

        if (cor) {

          Log.d(TAG, "onCreate() - Foto a cores selecionada");
          efeitoFoto = CORES;

        } else if (pb) {

          Log.d(TAG, "onCreate() - Foto em Preto e Branco selecionada");
          efeitoFoto = PB;

        }
        
        //int buttonId = group.getCheckedRadioButtonId();
        //Log.w(TAG,"onCreate() - cor ou PB = buttonId="+buttonId);
        
        
        
      }

    });

  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.d(TAG, "*** onStart() ***");
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.d(TAG, "*** onResume() ***");
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.d(TAG, "*** onPause() ***");
  }

  @Override
  protected void onStop() {
    super.onStop();
    Log.d(TAG, "*** onStop() ***");
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    Log.d(TAG, "*** onRestart() ***");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "*** onDestroy() ***");
  }

  /**
   * updateOptionalParamFields(int[] params)
   * 
   * Recebe um vetor onde cada elemento indica se o parâmetro será visível ou não
   * 
   * @param params vetor de inteiros contendo a visibilidade de cada parâmetro adicional
   */
  private void updateOptionalParamFields(int[] params) {

    // vetor de EditText (nome dos parâmetros)
    EditText[] editText = new EditText[5];
    
    // vetor TestView (valor dos parâmetros)
    TextView[] textView = new TextView[5];

    textView[0] = (TextView) findViewById(R.id.tvParam1);
    textView[1] = (TextView) findViewById(R.id.tvParam2);
    textView[2] = (TextView) findViewById(R.id.tvParam3);
    textView[3] = (TextView) findViewById(R.id.tvParam4);
    textView[4] = (TextView) findViewById(R.id.tvParam5);

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
