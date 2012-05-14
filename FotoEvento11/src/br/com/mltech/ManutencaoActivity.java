package br.com.mltech;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import br.com.mltech.modelo.Contratante;
import br.com.mltech.modelo.Evento;
import br.com.mltech.modelo.Parametros;
import br.com.mltech.modelo.Participacao;

/**
 * ManutencaoActivity
 * 
 * @author maurocl
 * 
 */
public class ManutencaoActivity extends Activity {

  private static final String TAG = "ManutecaoActivity";

  /* Identificadores das Activities */

  private static final int ACTIVITY_CONTRATANTE = 100;
  private static final int ACTIVITY_EVENTO = 101;
  private static final int ACTIVITY_PREFERENCIAS = 1002;
  private static final int ACTIVITY_RELATORIOS = 1003;

  private SharedPreferences mPreferences;

  private Contratante mContratante;
  private Evento mEvento;

  /**
   * onCreate(Bundle savedInstanceState)
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.manutencao);

    if (savedInstanceState != null) {
      // obtém as informações salvas (se existirem)
    }

    // Obtém as preferências
    boolean b = lerPreferencias();
    if (!b) {
      Log.d(TAG, "Erro na leitura do arquivo de preferências.");
    }

    Button btnContratante = (Button) findViewById(R.id.btnContratante);
    Button btnEvento = (Button) findViewById(R.id.btnEvento);
    Button btnPreferencias = (Button) findViewById(R.id.btnPrefencias);
    Button btnRelatorios = (Button) findViewById(R.id.btnRelatorios);
    Button btnConfiguracaoInicial = (Button) findViewById(R.id.btnConfiguracaoInicial);
    
    // Desabilita botão
    btnConfiguracaoInicial.setEnabled(false);

    /* Tratamento do click no botão Contratante */
    btnContratante.setOnClickListener(new OnClickListener() {

      /** botão contratante */
      public void onClick(View v) {

        Intent intent = new Intent(ManutencaoActivity.this, ContratanteActivity.class);

        intent.putExtra("br.com.mltech.contratante", mContratante);

        mContratante = (Contratante) intent.getSerializableExtra("br.com.mltech.contratante");

        Log.d(TAG, "Chamando ACTIVITY_CONTRATANTE");

        startActivityForResult(intent, ACTIVITY_CONTRATANTE);

        Log.d(TAG, "Após Chamando da ACTIVITY_CONTRATANTE");

      }
    });

    /* Tratamento do click no botão Evento */
    btnEvento.setOnClickListener(new OnClickListener() {

      /**
       * Trata o click no botão Evento
       */
      public void onClick(View v) {

        if(mContratante==null) {
          Toast.makeText(ManutencaoActivity.this, "Contratante ainda não foi preenchido", Toast.LENGTH_SHORT).show();
          Log.w(TAG,"É necessário cadastrar um contratante antes de preencher os dados do evento");
          
          return;
        }
        
        Intent intent = new Intent(ManutencaoActivity.this, EventoActivity.class);

        intent.putExtra("br.com.mltech.evento", mEvento);
        intent.putExtra("br.com.mltech.contratante", mContratante);

        // mEvento = (Evento)
        // intent.getSerializableExtra("br.com.mltech.evento");

        Log.d(TAG, "Chamando ACTIVITY_EVENTO");

        startActivityForResult(intent, ACTIVITY_EVENTO);

        Log.d(TAG, "Após Chamando da ACTIVITY_EVENTO");

      }
    });

    /* Tratamento do click no botão Preferências */
    btnPreferencias.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Intent intent = new Intent(ManutencaoActivity.this, PreferenciasActivity.class);

        Bundle params = new Bundle();

        intent.putExtras(params);

        startActivity(intent);

        // Toast.makeText(ManutencaoActivity.this, "Preferências ...",
        // Toast.LENGTH_LONG).show();
      }
    });

    /* Tratamento do click no botão Relatórios */
    btnRelatorios.setOnClickListener(new OnClickListener() {

      /**
       * 
       */
      public void onClick(View v) {

        Intent i = getIntent();

        List<Participacao> lista = (List<Participacao>) i.getSerializableExtra("br.com.mltech.lista");

        Intent intent = new Intent(ManutencaoActivity.this, RelatorioActivity.class);

        Bundle params = new Bundle();

        int num = (lista != null) ? lista.size() : 0;

        params.putString("numParticipantes", String.valueOf(num));

        intent.putExtra("br.com.mltech.lista", (ArrayList<Participacao>) lista);

        intent.putExtras(params);

        startActivity(intent);

      }
    });

    /* Tratamento do click no botão Configuração Inicial */
    btnConfiguracaoInicial.setOnClickListener(new OnClickListener() {

      /**
       * 
       */
      public void onClick(View v) {
        
        boolean b = limpaConfiguracoes();

        if (b) {
          Toast.makeText(ManutencaoActivity.this, "Configuração inicial restaurada com sucesso", Toast.LENGTH_SHORT).show();
          
          mContratante = null;
          mEvento = null;
          
          
        } else {
          Toast.makeText(ManutencaoActivity.this, "Falha na reinicialização da onfiguração inicial", Toast.LENGTH_SHORT).show();
        }

      }
    });

  }

 

  /**
   * onPause()
   */
  @Override
  protected void onPause() {
    Log.d(TAG, "*** onPause() ***");
    super.onPause();
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

    if (requestCode == ACTIVITY_CONTRATANTE) {
      processActivityContratante(resultCode, data);
    } else if (requestCode == ACTIVITY_EVENTO) {
      processActivityEvento(resultCode, data);
    } else if (requestCode == ACTIVITY_PREFERENCIAS) {
      processActivityPreferencias(resultCode, data);
    } else if (requestCode == ACTIVITY_RELATORIOS) {
      processActivityRelatorios(resultCode, data);
    } else {
      Log.d(TAG, "Error ... requestCode: " + requestCode + " não pode ser processado");
    }

  }

  /**
   * processActivityContratante(int resultCode, Intent data)
   * 
   * @param resultCode
   *          Resultado da execução da intent
   * @param data
   *          Intent
   */
  private void processActivityContratante(int resultCode, Intent data) {

    Log.d(TAG, "Retorno da ACTIVITY_CONTRATANTE:");

    if (resultCode == RESULT_OK) {

      Log.d(TAG, "processando RESULT_OK");

      if (data != null) {
        // Atualiza os dados do contratante
        mContratante = (Contratante) data.getSerializableExtra("br.com.mltech.contratante");
        Log.d(TAG, "processando contratante=" + mContratante);
      }

      boolean b = gravarPreferencias();
      if (b) {
        Log.d(TAG, "Arquivo de preferências foi gravado com sucesso após alteração de contratante");
      } else {

      }

    } else if (resultCode == RESULT_CANCELED) {

      Log.d(TAG, "O usuário cancelou a atividade Contratante");

    } else {
      Log.d(TAG, "Erro ...");
    }

  }

  /**
   * processActivityEvento(int resultCode, Intent data)
   * 
   * @param resultCode
   * @param data
   */
  private void processActivityEvento(int resultCode, Intent data) {

    Log.d(TAG, "processando ACTIVITY_EVENTO");

    if (resultCode == RESULT_OK) {

      Log.d(TAG, "processando RESULT_OK");

      if (data != null) {
        mEvento = (Evento) data.getSerializableExtra("br.com.mltech.evento");
        Log.d(TAG, "processando evento=" + mEvento);
      }

      boolean b = gravarPreferencias();
      if (b) {
        Log.d(TAG, "Arquivo de preferências foi gravado com sucesso após alteração do evento");
      } else {

      }

    } else if (resultCode == RESULT_CANCELED) {
      Log.d(TAG, "Usuário cancelou a tela de eventos");
    } else {
      Log.d(TAG, "Erro ...");
    }
  }

  /**
   * processActivityPreferencias(int resultCode, Intent data)
   * 
   * @param resultCode
   * @param data
   */
  private void processActivityPreferencias(int resultCode, Intent data) {
    Log.d(TAG, "processando ACTIVITY_PREFERENCIAS");

    if (resultCode == RESULT_OK) {

      Log.d(TAG, "processando RESULT_OK");

      /*
       * if (data != null) { evento = (Evento)
       * data.getSerializableExtra("br.com.mltech.evento"); }
       */

      // Log.d(TAG, "processando evento=" + mEvento);

    } else if (resultCode == RESULT_CANCELED) {
      Log.d(TAG, "Usuário cancelou a tela de preferências");
    } else {
      Log.d(TAG, "Erro ...");
    }
  }

  /**
   * processActivityRelatorios(int resultCode, Intent data)
   * 
   * @param resultCode
   * @param data
   */
  private void processActivityRelatorios(int resultCode, Intent data) {

    Log.d(TAG, "Retorno ACTIVITY_RELATORIOS");

    if (resultCode == RESULT_OK) {

      Log.d(TAG, "processando RESULT_OK na tela de preferencias");

      /*
       * if (data != null) { evento = (Evento)
       * data.getSerializableExtra("br.com.mltech.evento"); }
       */

      // Log.d(TAG, "processando evento=" + mEvento);

    } else if (resultCode == RESULT_CANCELED) {

      Log.d(TAG, "O usuário cancelou a tela de Preferências");

    } else {
      Log.d(TAG, "Erro ...");
    }

  }

  /**
   * isConfiguracaoCompleta()
   * 
   * verifica se a aplicação está configurada para uso
   * 
   */
  private boolean isConfiguracaoCompleta() {

    boolean b = true;

    // verifica se existe um contratante configurado
    if (mContratante == null) {
      b = false;
      Log.d(TAG, "Contratante não foi configurado");
    }

    // verifica se existe um evento cadastrado
    if (mEvento == null) {
      b = false;
      Log.d(TAG, "Evento não foi configurado");
    }

    // verifica se as bordas das fotos já foram disponibilizadas ao
    // evento
    if (mEvento != null && ((mEvento.getBordaCabine() == null) || (mEvento.getBordaPolaroid() == null))) {
      Log.d(TAG, "Bordas não foram configuradas");

      b = false;

    }

    return b;

  }


  /**
   * gravarPreferencias()
   * 
   * Garava a configuração do Contratante e do Evento na estrutura de
   * preferências (Preferences)
   * 
   * @return true caso a gravação ocorra com sucesso ou false caso haja algum
   *         problema.
   */
  private boolean gravarPreferencias() {

    boolean commitDone;

    Editor edit = mPreferences.edit();

    Log.d(TAG, "Gravando as preferências compartilhadas ...");

    /* Contratante */
    edit.putString("contratante_nome", mContratante.getNome());
    edit.putString("contratante_email", mContratante.getEmail());
    edit.putString("contratante_telefone", mContratante.getTelefone());

    /* Evento */
    edit.putString("evento_nome", mEvento.getNome());
    edit.putString("evento_email", mEvento.getEmail());
    edit.putString("evento_endereco", mEvento.getEndereco());
    edit.putString("evento_cidade", mEvento.getCidade());
    edit.putString("evento_estado", mEvento.getEstado());
    edit.putString("evento_cep", mEvento.getCep());
    edit.putString("evento_data", mEvento.getData());
    edit.putString("evento_telefone", mEvento.getTelefone());
    edit.putString("evento_envia_facebook", mEvento.isEnviaFacebook() == true ? "true" : "false");
    edit.putString("evento_envia_twitter", mEvento.isEnviaTwitter() == true ? "true" : "false");

    edit.putString("evento_borda_polaroid", mEvento.getBordaPolaroid());
    edit.putString("evento_borda_cabine", mEvento.getBordaCabine());

    edit.putString("evento_param1", mEvento.getParametros().getParametro(0));
    edit.putString("evento_param2", mEvento.getParametros().getParametro(1));
    edit.putString("evento_param3", mEvento.getParametros().getParametro(2));
    edit.putString("evento_param4", mEvento.getParametros().getParametro(3));
    edit.putString("evento_param5", mEvento.getParametros().getParametro(4));

    // grava as preferências
    commitDone = edit.commit();

    return commitDone;

  }

  /**
   * lerPreferencias()
   * 
   * Lê a configuração do Contratante e do Evento armazenada na estrutura de
   * preferências (Preferences).
   * 
   * Inicializa as variáveis mContratante e mEvento com os valores lidos ou
   * apenas cria uma instância delas.
   * 
   * @return true em caso de sucesso ou false no caso de falhas
   * 
   */
  private boolean lerPreferencias() {

    Log.d(TAG, "Lendo as preferências compartilhadas ...");

    mPreferences = getSharedPreferences("preferencias", MODE_PRIVATE);

    if (mPreferences == null) {
      Log.w(TAG, "mPreferences is null. Falha na execução do comandos getSharedPreferences()");
      return false;
    }

    // Retrieve all values from the preferences.
    // Recupera todos os valores do mecanismo de preferências
    // Returns a map containing a list of pairs key/value representing the
    // preferences.
    // Retorna um mapa contendo uma lista de pares chave/valor representando
    // as preferências
    Map<?, ?> chaves = mPreferences.getAll();

    if (chaves != null) {

      Set<?> set = chaves.entrySet();

      Log.d(TAG, "Nº de chaves lidas: " + set.size());

      Iterator<?> i = set.iterator();

      // Display elements
      while (i.hasNext()) {

        Entry<?, ?> me = (Entry<?, ?>) i.next();

        Log.v(TAG, me.getKey() + "=" + me.getValue());

      }
    } else {
      Log.v(TAG, "Nenhum par de chave/valor foi encontrado.");
    }

    if (mContratante == null) {

      // Contratante não foi inicializado
      // Cria um novo contratante vazio
      mContratante = new Contratante();

    }

    mContratante.setNome((String) chaves.get("contratante_nome"));
    mContratante.setEmail((String) chaves.get("contratante_email"));
    mContratante.setTelefone((String) chaves.get("contratante_telefone"));

    if (mEvento == null) {

      // Evento não foi inicializado
      // Cria um novo Evento vazio
      mEvento = new Evento();

      mEvento.setContratante(mContratante);

    }

    mEvento.setBordaCabine(null);
    mEvento.setBordaPolaroid(null);

    mEvento.setNome((String) chaves.get("evento_nome"));

    mEvento.setEmail((String) chaves.get("evento_email"));

    mEvento.setEndereco((String) chaves.get("evento_endereco"));
    mEvento.setCidade((String) chaves.get("evento_cidade"));
    mEvento.setEstado((String) chaves.get("evento_estado"));
    mEvento.setCep((String) chaves.get("evento_cep"));

    mEvento.setData((String) chaves.get("evento_data"));

    mEvento.setTelefone((String) chaves.get("evento_telefone"));

    mEvento.setBordaPolaroid((String) chaves.get("evento_borda_polaroid"));

    mEvento.setBordaCabine((String) chaves.get("evento_borda_cabine"));

    String facebookSelecionado = null;
    String twitterSelecionado = null;

    facebookSelecionado = ((String) chaves.get("evento_envia_facebook"));

    boolean b1 = false, b2 = false;

    if (facebookSelecionado != null) {

      b1 = (facebookSelecionado.equals("true") ? true : false);

    }

    twitterSelecionado = ((String) chaves.get("evento_envia_twitter"));

    if (twitterSelecionado != null) {

      b2 = (twitterSelecionado.equals("true") ? true : false);
    }

    mEvento.setEnviaFacebook(b1);
    mEvento.setEnviaTwitter(b2);

    String[] paramEventos = new String[5];

    paramEventos[0] = (String) chaves.get("evento_param1");
    paramEventos[1] = (String) chaves.get("evento_param2");
    paramEventos[2] = (String) chaves.get("evento_param3");
    paramEventos[3] = (String) chaves.get("evento_param4");
    paramEventos[4] = (String) chaves.get("evento_param5");

    Parametros parametros = new Parametros(paramEventos);

    mEvento.setParametros(parametros);

    return true;

  }

  
  /**
   * limpaConfiguracoes()
   * 
   * Reinicia o arquivo de onfiguração de contratante e evento
   * 
   * SharedPreferences("preferencias")
   * 
   * @return true se sucesso ou false caso haja algum erro.
   */
  private boolean limpaConfiguracoes() {

    boolean commitDone;

    mPreferences = getSharedPreferences("preferencias", MODE_PRIVATE);

    Editor edit = mPreferences.edit();

    // Mark in the editor to remove all values from the preferences.
    // Once commit is called, the only remaining preferences will be any that you have defined in this editor. 
    edit.clear();
    
    // grava as preferências
    commitDone = edit.commit();

    Log.d(TAG, "Gravando as preferências compartilhadas ...");

    mPreferences = null;

    return commitDone;

  }
  
}
