
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
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import br.com.mltech.modelo.Contratante;
import br.com.mltech.modelo.Evento;
import br.com.mltech.modelo.Parametros;

/**
 * EventoActivity.java<br>
 * 
 * Esta activity � respons�vel pela manutena��o de um Evento.
 * 
 */
// public class EventoActivity extends Activity implements OnClickListener {
public class EventoActivity extends Activity implements Constantes {

  public static final String TAG = "EventoActivity";

  public static final int MY_ACTION_PICK = 255;

  private String[] estados = new String[] { "SP", "MG", "RS", "ES", "DF", "RJ" };

  private Contratante mContratante;

  private Evento mEvento;

  private EditText cliente;

  private EditText nome;

  private EditText endereco;

  private EditText cidade;

  private EditText estado;

  private Spinner estado1;

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

    //------------------------------
    ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, estados);

    adaptador.setDropDownViewResource(android.R.layout.simple_spinner_item);

    estado1.setAdapter(adaptador);

    estado1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> parent, View view, int posicao, long id) {

        Toast.makeText(EventoActivity.this, "Item " + posicao + " selecionado", Toast.LENGTH_LONG).show();

        Log.d(TAG, "onCreate() - Item " + posicao + " selecionado");

        estado.setText((String) estado1.getSelectedItem());

      }

      public void onNothingSelected(AdapterView<?> parent) {

        Toast.makeText(EventoActivity.this, "Nenhum item foi selecionado", Toast.LENGTH_LONG).show();
      }

    });

    cep.setOnFocusChangeListener(new OnFocusChangeListener() {

      public void onFocusChange(View v, boolean hasFocus) {

        EditText x = (EditText) v;
        
        if(x.getText().length()<8 || x.getText().length()>9) {
          Toast.makeText(EventoActivity.this, "O Cep n�o est� correto !", Toast.LENGTH_LONG).show();
        }
        
        

      }
    });

    //------------------------------

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
   * Testa se as informa��es sobre o Contratante e o Evento s�o v�lidas.
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
      mContratante = (Contratante) intent.getSerializableExtra(CONTRATANTE);
    } else {
      // par�metro n�o encontrado
      Log.d(TAG, "testaIntentNula() - Par�metro br.com.mltech.contratante n�o encontrado");
    }

    if (intent.getSerializableExtra(EVENTO) != null) {
      // recebe informa��es sobre o evento
      mEvento = (Evento) intent.getSerializableExtra(EVENTO);
    } else {
      // par�metro n�o encontrado
      Log.d(TAG, "testaIntentNula() - Par�metro br.com.mltech.evento n�o encontrado");
    }

  }

  /**
   * Verifica se o contratante j� foi definido.
   * 
   * Se o contratante for nulo ent�o o evento n�o poder� ser preenchido.<br>
   * Dessa forma envia uma mensagem ao usu�rio indicando que o contratante n�o
   * deve ser nulo.<br>
   * � criada uma Intent indicando o resultado da execu��o da activity.
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
      Log.d(TAG, "testaEventoNulo() - Evento carregado com sucesso: " + mEvento);

    }

  }

  /**
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
    estado1 = (Spinner) findViewById(R.id.spinner1);
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
   * Preenche os componentes visuais com os dados do objeto Evento. Usa os
   * objetos mContratante e mEvento para preencher os dados nos elementos de UI.
   */
  private void atualizaValorComponentes() {

    if (mEvento == null) {

      Log.d(TAG, "atualizaValorComponentes() - mEvento � nulo");

    } else {

      Log.d(TAG, "atualizaValorComponentes() - mEvento n�o � nulo");

      // Atualiza os valores dos componentes

      cliente.setText(mContratante.getNome());

      // estabelece que o componente n�o ser� foc�vel ele ser� apenas exibido por�m n�o poder� ser
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

      int indice = busca(mEvento.getEstado());
      if (indice != -1) {
        estado1.setSelection(indice);
      }

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
   * Executa o processo de grava��o dos elementos de UI no objeto Evento.
   */
  void processaBtnGravar() {

    Log.d(TAG, "processaBtnGravar()");

    /**
     * na hora de gravar os eventos � necess�rio "ler" todos os valores
     * presentes na interface gr�fica e atualizar a vari�vel mEvento
     */

    if (nome != null) {
      mEvento.setNome(nome.getText().toString());
      
      
      if(nome.getText().toString().equals("")) {
        // nome n�o pode ser vazio
        showToast("Nome n�o pode ser nulo");
        return;
      }
      
    }

    if (email != null) {
      mEvento.setEmail(email.getText().toString());
      
      if(email.getText().toString().equals("")) {
        // email n�o pode ser vazio
        showToast("Email n�o pode ser nulo");
        return;
      }
      
      
    }

    if (endereco != null) {
      mEvento.setEndereco(endereco.getText().toString());
      
      if(endereco.getText().toString().equals("")) {
        // email n�o pode ser vazio
        showToast("Endere�o n�o pode ser nulo");
        return;
      }
      
    }

    if (cidade != null) {
      mEvento.setCidade(cidade.getText().toString());
      if(cidade.getText().toString().equals("")) {
        // email n�o pode ser vazio
        showToast("Cidade n�o pode ser nulo");
        return;
      }
    }

    if (estado != null) {
      mEvento.setEstado(estado.getText().toString());
      
      if(estado.getText().toString().equals("")) {
        // email n�o pode ser vazio
        showToast("Estado n�o pode ser nulo");
        return;
      }
      
    }

    if (cep != null) {
      mEvento.setCep(cep.getText().toString());
      
      if(cep.getText().toString().equals("")) {
        // email n�o pode ser vazio
        showToast("Cep n�o pode ser nulo");
        return;
      }
      
      
    }

    if (data != null) {
      mEvento.setData(data.getText().toString());
      
      if(data.getText().toString().equals("")) {
        // email n�o pode ser vazio
        showToast("Data n�o pode ser nulo");
        return;
      }
      
    }

    if (telefone != null) {
      mEvento.setTelefone(telefone.getText().toString());
      
      if(telefone.getText().toString().equals("")) {
        // email n�o pode ser vazio
        showToast("Telefone n�o pode ser nulo");
        return;
      }
      
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
      
      if(bordaPolaroid.getText().toString().equals("")) {
        // email n�o pode ser vazio
        showToast("Borda Polaroid n�o pode ser nula");
        return;
      }
      
    }

    if (bordaCabine != null) {
      mEvento.setBordaCabine(bordaCabine.getText().toString());
      
      if(bordaCabine.getText().toString().equals("")) {
        // email n�o pode ser vazio
        showToast("Borda Cabine n�o pode ser nulo");
        return;
      }
      
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

    // Finaliza a Activity
    finish();

  }
  
  
  /**
   * 
   * @param msg
   */
  public void showToast(String msg) {
    Toast.makeText(EventoActivity.this, msg, Toast.LENGTH_LONG).show();
  }

  /**
   * onActivityResult(int requestCode, int resultCode, Intent data)
   * 
   * Trata o resultado da chamada das Activities
   * 
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    Log.i(TAG, "onActivityResult(request " + requestCode + ", result=" + resultCode + ", data " + data + ") ...");

    if (requestCode == MY_ACTION_PICK) {

      Intent i = getIntent();

      if (data != null) {
        Log.d(TAG, "data=" + data);
      }

    }

  }

  /**
   * Executa o processo de cancelamento de modifica��es e retorno da activity.
   */
  void processaBtnCancelar() {

    Log.d(TAG, "processaBtnCancelar() - Bot�o cancelar selecionado");

    // cria uma intent de resposta indicando o resultado de execu��o da activity.
    Intent intent = new Intent();

    // seta o resultado
    setResult(RESULT_CANCELED, intent);

    // finaliza a activity
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

        Intent y = new Intent(Intent.ACTION_PICK);

        y.setType("image/*");

        startActivityForResult(y, MY_ACTION_PICK);

        return false;
      }

    });

    return super.onCreateOptionsMenu(menu);

  }

  /**
   * Busca por uma string em um array de strings n�o ordenado.<br>
   * Retorna o �ndice da string no array ou -1 caso a string n�o seja encontrada.
   * 
   * @param s String procurada
   * 
   * @return O indice do array onde a string se localiza ou -1 caso a string n�o seja encontrada.
   */
  public int busca(String s) {

    int achei = -1;
    for (int i = 0; i < estados.length; i++) {
      if (s.equals(estados[i])) {
        achei = i;
        break;
      }
    }
    return achei;
  }

}
