package br.com.mltech;

import android.app.Activity;
import android.content.Intent;
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
 *         Activity para criação de um evento
 * 
 */
public class EventoActivity extends Activity {

  public static final String TAG = "EventoActivity";

  private Contratante mContratante;
  private Evento mEvento;

  /**
   * 
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.evento);

    Log.d(TAG, "*** onCreate() ***");

    // retorna a Intent que iniciou essa Activity
    Intent intent = getIntent();

    if (intent != null) {

      // Lê os parâmetros recebidos pela Intent
      mEvento = (Evento) intent.getSerializableExtra("br.com.mltech.evento");
      mContratante = (Contratante) intent.getSerializableExtra("br.com.mltech.contratante");

      if (mContratante == null) {

        Log.w(TAG, "Contratante não pode ser nulo");
        Toast.makeText(this, "Contratante ainda não foi preenchido", Toast.LENGTH_SHORT);

        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        finish();

      }

      if (mEvento == null) {
        // cria um evento vazio
        mEvento = new Evento();
        Log.d(TAG, "Criando evento vazio");
        // e atribui o contratante
        mEvento.setContratante(mContratante);
      } else {
        // exibe as informações a respeito do evento
        Log.d(TAG, "Evento carregado com sucesso: " + mEvento);
      }

    }

    /*
     * if(mContratante==null) { Log.w(TAG,"Contratante não pode ser nulo");
     * Toast.makeText(this, "Contratante ainda não foi preenchido",
     * Toast.LENGTH_SHORT);
     * 
     * Intent i = new Intent(); setResult(RESULT_CANCELED, i); finish();
     * 
     * }
     */

    // Obtendo os identificadores dos elementos de tela
    final EditText cliente = (EditText) findViewById(R.evento.editCliente);
    final EditText nome = (EditText) findViewById(R.evento.editNome);
    final EditText endereco = (EditText) findViewById(R.evento.endereco);
    final EditText cidade = (EditText) findViewById(R.id.cidade);
    final EditText estado = (EditText) findViewById(R.id.estado);
    final EditText cep = (EditText) findViewById(R.id.cep);
    final EditText data = (EditText) findViewById(R.id.data);
    final EditText telefone = (EditText) findViewById(R.id.telefone);
    final EditText email = (EditText) findViewById(R.id.email);

    final EditText bordaPolaroid = (EditText) findViewById(R.evento.editBordaPolaroid);
    final EditText bordaCabine = (EditText) findViewById(R.evento.editBordaCabine);

    final EditText param1 = (EditText) findViewById(R.evento.editParam1);
    final EditText param2 = (EditText) findViewById(R.evento.editParam2);
    final EditText param3 = (EditText) findViewById(R.evento.editParam3);
    final EditText param4 = (EditText) findViewById(R.evento.editParam4);
    final EditText param5 = (EditText) findViewById(R.evento.editParam5);

    // Checkboxes
    final CheckBox chkFacebook = (CheckBox) findViewById(R.evento.chkFacebook);
    final CheckBox chkTwitter = (CheckBox) findViewById(R.evento.chkTwitter);

    // Botões
    final Button btnGravar = (Button) findViewById(R.evento.btnGravar);
    final Button btnCancelar = (Button) findViewById(R.evento.btnCancelar);

    if (mEvento == null) {

      Log.d(TAG, "mEvento is null");

    } else {

      Log.d(TAG, "mEvento is not null");

      // Atualiza os valores dos componentes

      cliente.setText(mContratante.getNome());

      cliente.setFocusable(false);

      nome.setText(mEvento.getNome());

      nome.requestFocus();

      endereco.setText(mEvento.getEndereco());
      cidade.setText(mEvento.getCidade());
      estado.setText(mEvento.getEstado());
      cep.setText(mEvento.getCep());
      data.setText(mEvento.getData());
      telefone.setText(mEvento.getTelefone());

      bordaPolaroid.setText(mEvento.getBordaPolaroid());
      bordaCabine.setText(mEvento.getBordaCabine());

      chkFacebook.setChecked(mEvento.isEnviaFacebook());
      chkTwitter.setChecked(mEvento.isEnviaTwitter());

      Parametros paramOpcionais = mEvento.getParametros();

      String[] param = null;

      if (paramOpcionais != null) {
        param = paramOpcionais.getParametros();
      } else {
        param = new String[5];
      }

      param1.setText(param[0]);
      param2.setText(param[1]);
      param3.setText(param[2]);
      param4.setText(param[3]);
      param5.setText(param[4]);

    }

    /**
     * Checkbox do Facebook
     */
    chkFacebook.setOnCheckedChangeListener(new OnCheckedChangeListener() {

      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        Log.d(TAG, "checkFacebook: " + isChecked);

      }

    });

    /**
     * Checkbox do Twitter
     */
    chkTwitter.setOnCheckedChangeListener(new OnCheckedChangeListener() {

      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        Log.d(TAG, "checkTwitter: " + isChecked);

      }

    });

    /**
     * Botão Gravar (Evento)
     */
    btnGravar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        /**
         * na hora de gravar os eventos é necessário "ler" todos os valores
         * presentes na interface gráfica e atualizar a variável mEvento
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

        Parametros parametros = new Parametros(paramEvento);

        mEvento.setParametros(parametros);

        Log.d(TAG, "Evento: " + mEvento);

        // Retorna os valores da intent
        Intent intent = new Intent();
        intent.putExtra("br.com.mltech.evento", mEvento);
        setResult(RESULT_OK, intent);
        finish();

      }
    });

    /**
     * Botão Cancelar (Evento)
     */
    btnCancelar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {
        Log.d(TAG, "Botão cancelar selecionado");
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
      }

    });

    Log.d(TAG, "*** passei 140 ***");

  }

  // ------------------------------------
  // Criação de Menu da Activity Evento
  // ------------------------------------
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // Menu Item: Upload (executa o upload das bordas do evento)
    MenuItem upload = menu.add(0, 0, 0, "Upload");
    upload.setIcon(R.drawable.ic_launcher);
    upload.setOnMenuItemClickListener(new OnMenuItemClickListener() {

      public boolean onMenuItemClick(MenuItem item) {
        // TODO Auto-generated method stub
        Toast.makeText(EventoActivity.this, "Upload de Bordas", Toast.LENGTH_SHORT).show();
        return false;
      }
    });

    return super.onCreateOptionsMenu(menu);

  }

}
