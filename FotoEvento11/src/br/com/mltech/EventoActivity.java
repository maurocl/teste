package br.com.mltech;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import br.com.mltech.modelo.Contratante;
import br.com.mltech.modelo.Evento;
import br.com.mltech.modelo.Parametros;

/**
 * EventoActivity.java
 * 
 * @author maurocl
 * 
 *         Activity para cria��o de um evento
 * 
 */
// public class EventoActivity extends Activity implements OnClickListener {
public class EventoActivity extends Activity implements Constantes {

  public static final String TAG = "EventoActivity";

  private Contratante mContratante;

  private Evento mEvento;

  private EditText cliente;

  private EditText nome;

  private EditText endereco;

  private EditText cidade;

  private EditText estado;

  private EditText cep;

  private EditText data;

  private EditText telefone;

  private EditText email;

  private EditText bordaPolaroid;

  private EditText bordaCabine;

  private EditText param1;

  private EditText param2;

  private EditText param3;

  private EditText param4;

  private EditText param5;

  // Checkboxes
  private CheckBox chkFacebook;

  private CheckBox chkTwitter;

  // Buttons
  private Button btnGravar;

  private Button btnCancelar;

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.evento);

    Log.d(TAG, "*** onCreate() ***");

    // retorna a Intent que iniciou essa Activity
    Intent intent = getIntent();

    // verifica se a intent possui dados
    testaIntentNula(intent);

    // Verifica se h� um contratante
    testaContratanteNulo();

    // Verifica se h� um evento
    testaEventoNulo();

    // configura os identificadores dos componentes da tela
    atualizaIdentificadoresTela();

    // Atualiza os componentes da interface gr�fica com os valores do objeto
    // mEvento
    atualizaValorComponentes();

    /**
     * Checkbox do Facebook
     */
    chkFacebook.setOnCheckedChangeListener(new OnCheckedChangeListener() {

      public void onCheckedChanged(CompoundButton buttonView,
          boolean isChecked) {

        Log.d(TAG, "checkFacebook: " + isChecked);
        
      }

    });

    /**
     * Checkbox do Twitter
     */
    chkTwitter.setOnCheckedChangeListener(new OnCheckedChangeListener() {

      public void onCheckedChanged(CompoundButton buttonView,
          boolean isChecked) {

        Log.d(TAG, "checkTwitter: " + isChecked);

      }

    });

    btnGravar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Log.d(TAG, "btnGravar");
        processaBtnGravar();
        
      }

    });

    btnCancelar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Log.d(TAG, "btnCancelar");
        processaBtnCancelar();
      }

    });

  }

  /**
   * onClick(View v)
   */
  public void onClick(View v) {

    int elemento = v.getId();

    if (v == btnGravar) {
      Log.d(TAG, "btnGravar");
    } else if (v == btnCancelar) {
      Log.d(TAG, "btnCancelar");
    }

    Log.d(TAG, "elemento=" + elemento + ", v=" + v);

    switch (elemento) {
      case R.evento.btnGravar:
        processaBtnGravar();
        break;
      case R.evento.btnCancelar:
        processaBtnCancelar();
        break;
      case R.evento.chkFacebook:
        Log.d(TAG, "chkFacebook");
        break;
      case R.evento.chkTwitter:
        Log.d(TAG, "chkTwitter");
        break;
    }

  }

  /**
   * testaIntentNula(Intent intent)
   * 
   * se a intent n�o for nula ent�o l� as informa��es sobre o Contratante e o
   * Evento e guarda essas informa��es nos objetos correspondentes
   * 
   * @param intent
   * 
   */
  private void testaIntentNula(Intent intent) {

    if (intent == null) {
      // intent recebida � null
      Log.w(TAG, "testaIntentNula() - Intent � nula !!!");
      return;
    }

    // L� os par�metros recebidos pela Intent

    if (intent.getSerializableExtra(CONTRATANTE) != null) {
      // recebe informa��es sobre o contratante
      mContratante = (Contratante) intent
          .getSerializableExtra(CONTRATANTE);
    } else {
      // par�metro n�o encontrado
      Log.d(TAG,
          "testaIntentNula() - Par�metro br.com.mltech.contratante n�o encontrado");
    }

    if (intent.getSerializableExtra(EVENTO) != null) {
      // recebe informa��es sobre o evento
      mEvento = (Evento) intent.getSerializableExtra(EVENTO);
    } else {
      // par�metro n�o encontrado
      Log.d(TAG,
          "testaIntentNula() - Par�metro br.com.mltech.evento n�o encontrado");
    }

  }

  /**
   * testaContratanteNulo()
   * 
   * se o contratante for nulo ent�o o evento n�o poder� ser preenchido dessa
   * forma envia uma mensagem ao usu�rio indicando que o contratante n�o deve
   * ser nulo. � criada uma Intent indicando o resultado da execu��o da activity
   * 
   */
  private void testaContratanteNulo() {

    if (mContratante == null) {

      Log.w(TAG, "testaContratanteNulo() - Contratante n�o pode ser nulo");
      Toast.makeText(this, "Contratante ainda n�o foi preenchido",
          Toast.LENGTH_SHORT).show();

      // cria intent de retorno e finaliza a activity
      Intent i = new Intent();
      setResult(RESULT_CANCELED, i);
      finish();

    } else {
      Log.d(TAG, "testaContratanteNulo() - Contratante recuperado");
    }

  }

  /**
   * testaEventoNulo()
   * 
   * testa se o evento � nulo
   * 
   * Testa se j� existe algum evento cadastrado. Se ainda n�o houver um evento,
   * ent�o cria-o.
   * 
   */
  private void testaEventoNulo() {

    if (mEvento == null) {
      // nenhum evento foi encontrado
      // cria um objeto Evento
      mEvento = new Evento();
      Log.d(TAG, "testaEventoNulo() - cria um novo evento");
      // e atribui o contratante
      mEvento.setContratante(mContratante);

    } else {

      // exibe as informa��es a respeito do evento
      Log.d(TAG, "testaEventoNulo() - Evento carregado com sucesso: "
          + mEvento);

    }

  }

  /**
   * atualizaIdentificadoresTela()
   * 
   * Obtem o identificador de cada componente gr�fico da tela
   * 
   */
  void atualizaIdentificadoresTela() {

    Log.d(TAG, "atualizaIdentificadoresTela() - inicio");

    // Obtendo os identificadores dos elementos de tela
    cliente = (EditText) findViewById(R.evento.editCliente);
    nome = (EditText) findViewById(R.evento.editNome);
    endereco = (EditText) findViewById(R.evento.endereco);
    cidade = (EditText) findViewById(R.id.cidade);
    estado = (EditText) findViewById(R.id.estado);
    cep = (EditText) findViewById(R.id.cep);
    data = (EditText) findViewById(R.id.data);
    telefone = (EditText) findViewById(R.id.telefone);
    email = (EditText) findViewById(R.id.email);

    // Bordas
    bordaPolaroid = (EditText) findViewById(R.evento.editBordaPolaroid);
    bordaCabine = (EditText) findViewById(R.evento.editBordaCabine);

    // Par�metros opcionais
    param1 = (EditText) findViewById(R.evento.editParam1);
    param2 = (EditText) findViewById(R.evento.editParam2);
    param3 = (EditText) findViewById(R.evento.editParam3);
    param4 = (EditText) findViewById(R.evento.editParam4);
    param5 = (EditText) findViewById(R.evento.editParam5);

    // Checkboxes
    chkFacebook = (CheckBox) findViewById(R.evento.chkFacebook);
    chkTwitter = (CheckBox) findViewById(R.evento.chkTwitter);

    // Bot�es
    btnGravar = (Button) findViewById(R.evento.btnGravar);
    btnCancelar = (Button) findViewById(R.evento.btnCancelar);

    Log.d(TAG, "atualizaIdentificadoresTela() - fim");

  }

  /**
   * atualizaValorComponentes()]
   * 
   * Preenche os componentes visuais com os dados do objeto Evento Usa os
   * objetos mContratante e mEvento para preencher os dados nos elementos
   * gr�ficos visuais
   * 
   */
  private void atualizaValorComponentes() {

    if (mEvento == null) {

      Log.d(TAG,
          "atualizaValorComponentes() - atualizaValorComponentes() - mEvento is null");

    } else {

      Log.d(TAG, "atualizaValorComponentes() - mEvento is not null");

      // Atualiza os valores dos componentes

      cliente.setText(mContratante.getNome());

      // estabelece que o componente n�o ser� foc�vel
      // ele ser� apenas exibido por�m n�o poder� ser
      // modificado
      cliente.setFocusable(false);
      cliente.setBackgroundColor(Color.LTGRAY);

      // nome do evento
      nome.setText(mEvento.getNome());

      // estabelece o foco nesse componente
      nome.requestFocus();

      endereco.setText(mEvento.getEndereco());
      cidade.setText(mEvento.getCidade());
      estado.setText(mEvento.getEstado());
      cep.setText(mEvento.getCep());
      data.setText(mEvento.getData());
      telefone.setText(mEvento.getTelefone());

      bordaPolaroid.setText(mEvento.getBordaPolaroid());
      bordaCabine.setText(mEvento.getBordaCabine());

      // Atualiza os checkboxes
      chkFacebook.setChecked(mEvento.isEnviaFacebook());
      chkTwitter.setChecked(mEvento.isEnviaTwitter());

      // Obt�m um objeto Parametros do Evento
      Parametros paramOpcionais = mEvento.getParametros();

      //
      String[] param = null;

      if (paramOpcionais != null) {
        param = paramOpcionais.getParametros();
      } else {
        // cria um array de Strings para armazenar os par�metros
        // opcionais
        param = new String[5];
      }

      // Atualiza cada elemento da tela com o valor de um par�mero
      // opcional
      param1.setText(param[0]);
      param2.setText(param[1]);
      param3.setText(param[2]);
      param4.setText(param[3]);
      param5.setText(param[4]);

    }

  }

  /**
   * processaBtnGravar()
   * 
   * Pega os valores dos componentes gr�ficos e preenche o objeto mEvento
   * 
   */
  void processaBtnGravar() {

    Log.d(TAG, "processaBtnGravar()");

    /**
     * na hora de gravar os eventos � necess�rio "ler" todos os valores
     * presentes na interface gr�fica e atualizar a vari�vel mEvento
     */

    if (nome != null) {
      mEvento.setNome(nome.getText().toString());
    }

    if (email != null) {
      mEvento.setEmail(email.getText().toString());
    }

    if (endereco != null) {
      mEvento.setEndereco(endereco.getText().toString());
    }

    if (cidade != null) {
      mEvento.setCidade(cidade.getText().toString());
    }

    if (estado != null) {
      mEvento.setEstado(estado.getText().toString());
    }

    if (cep != null) {
      mEvento.setCep(cep.getText().toString());
    }

    if (data != null) {
      mEvento.setData(data.getText().toString());
    }

    if (telefone != null) {
      mEvento.setTelefone(telefone.getText().toString());
    }

    //

    if (chkFacebook != null) {
      mEvento.setEnviaFacebook(chkFacebook.isChecked());
    }

    if (chkTwitter != null) {
      mEvento.setEnviaTwitter(chkTwitter.isChecked());
    }

    // ---
    if (bordaPolaroid != null) {
      mEvento.setBordaPolaroid(bordaPolaroid.getText().toString());
    }

    if (bordaCabine != null) {
      mEvento.setBordaCabine(bordaCabine.getText().toString());
    }

    // Vetor de Strings para armazenamento do nome dos elementos opcionais
    String[] paramEvento = new String[5];

    if (param1 != null) {
      paramEvento[0] = param1.getText().toString();
    }

    if (param2 != null) {
      paramEvento[1] = param2.getText().toString();
    }

    if (param3 != null) {
      paramEvento[2] = param3.getText().toString();
    }

    if (param4 != null) {
      paramEvento[3] = param4.getText().toString();
    }

    if (param5 != null) {
      paramEvento[4] = param5.getText().toString();
    }

    // Cria uma inst�ncia da classe Parametros para armazenar os par�metros
    // opcionais
    Parametros parametros = new Parametros(paramEvento);

    // Atualiza os par�metros opcionais no evento
    mEvento.setParametros(parametros);

    Log.d(TAG, "Evento: " + mEvento);

    // Retorna os valores da intent
    Intent intent = new Intent();
    intent.putExtra(EVENTO, mEvento);
    setResult(RESULT_OK, intent);
    finish();

  }

  /**
   * processaBtnCancelar()
   */
  void processaBtnCancelar() {

    Log.d(TAG, "processaBtnCancelar() - Bot�o cancelar selecionado");
    Intent intent = new Intent();
    setResult(RESULT_CANCELED, intent);
    finish();
  }

  // ------------------------------------
  // Cria��o de Menu da Activity Evento
  // ------------------------------------
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // Menu Item: Upload (executa o upload das bordas do evento)
    MenuItem upload = menu.add(0, 0, 0, "Upload");
    upload.setIcon(R.drawable.ic_launcher);
    upload.setOnMenuItemClickListener(new OnMenuItemClickListener() {

      public boolean onMenuItemClick(MenuItem item) {

        // TODO Auto-generated method stub
        Toast.makeText(EventoActivity.this, "Upload de Bordas",
            Toast.LENGTH_SHORT).show();
        return false;
      }
    });

    return super.onCreateOptionsMenu(menu);

  }

}
