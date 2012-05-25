package br.com.mltech;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.mltech.modelo.Contratante;
import br.com.mltech.modelo.Evento;
import br.com.mltech.modelo.Parametros;
import br.com.mltech.modelo.Participacao;
import br.com.mltech.modelo.Participante;
import br.com.mltech.utils.camera.CameraTools;

/**
 * Activity principal da aplica��o
 * 
 * @author maurocl
 * 
 */
public class FotoEventoActivity extends Activity {

  private static final String TAG = "FotoEventoActivity";

  public static final String PREF_EMAIL = "pref_email";

  // indica o uso do DEBUG
  private static int DEBUG = 0;

  /* Identificadores das Activities */

  private static final int ACTIVITY_LOGIN = 103;
  private static final int ACTIVITY_MANUTENCAO = 104;
  private static final int ACTIVITY_PARTICIPANTE = 102;
  private static final int ACTIVITY_DUMMY3 = 120;

  private static final int ACTIVITY_CAMERA = 105;

  //
  private SharedPreferences mPreferences;

  // Defini��o do contrante, evento e participa��es
  private Contratante mContratante;
  private Evento mEvento;
  private Participante mParticipante;
  private Participacao mParticipacao;

  // Defini��o da lista de participa��o no evento
  private List<Participacao> mListaParticipacao;

  // N� de participantes at� o momento
  private int mNumParticipantes = 0;

  // Indica se o usu�rio se encontra loggado a aplica��o
  private boolean mLogged;

  // indica a necessidade do uso de conta de administrador para acesso ao menu
  // de manuten��o. � usado para testes
  // TODO incluir essa configura��o nas configura��es da aplica��o
  //
  private boolean mAdminAccount = false;

  private CameraTools ct;

  /**
   * onCreate(Bundle savedInstanceState)
   * 
   * Called when the activity is first created.
   * 
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);

    if (savedInstanceState != null) {
      // obt�m as informa��es salvas (se existirem)
    }

    Log.i(TAG, "*** onCreate() ***");

    ct = new CameraTools();

    // prepara o ambiente para execu��o da aplica��o
    boolean b = preparaAmbiente();

    // L� a configura��o das prefer�ncias do sistema
    if (mPreferences == null) {
      lerConfiguracoes("preferencias");
    }

    // lista de participa��es
    mListaParticipacao = new ArrayList<Participacao>();

    if (DEBUG > 0) {
      mContratante = new Contratante("Joao", "maurocl@mltech.com.br", "9999999");

      mParticipante = new Participante("Mauro Cesar Lopes", "maurocl@terra.com.br", "(19) 8143-8978");

      // 1=POLAROID, 11=COR
      mParticipacao = new Participacao(mParticipante, 1, 11, null);

    }

    // Verifica se existem algum bitmap configurado para exibi��o na tela
    // inicial
    Bitmap bitmapTelaInicial = obtemImagemTelaInicial();

    if (bitmapTelaInicial != null) {

      ImageView imageViewTelaInicial = (ImageView) findViewById(R.id.imageView1);

      imageViewTelaInicial.setImageBitmap(bitmapTelaInicial);

    }

    Button btnParticipante = (Button) findViewById(R.id.btnParticipante);

    btnParticipante.setVisibility(android.view.View.GONE);

    Button btn = (Button) findViewById(R.id.btnBotao);

    Button btnSair = (Button) findViewById(R.id.btnSair);

    if (DEBUG == 1) {

      // ----------------------------
      // trata o bot�o Participante (a��o do usu�rio para participar no evento)
      // ----------------------------
      btnParticipante.setOnClickListener(new OnClickListener() {

        public void onClick(View v) {

          launchActivity(FotoEventoActivity.this, ParticipanteActivity.class, null, ACTIVITY_PARTICIPANTE);

        }

      });

    }

    // ----------------------------
    // trata o bot�o Participar
    // ----------------------------

    btn.setText("*** Participar ***");

    btn.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        boolean b = isCondicoesIniciaisSatisfeitas();

        if (b) {

          Bundle bundle = new Bundle();

          bundle.putSerializable("br.com.mltech.contratante", mContratante);
          bundle.putSerializable("br.com.mltech.evento", mEvento);
          bundle.putSerializable("br.com.mltech.participante", mParticipante);
          bundle.putSerializable("br.com.mltech.participacao", mParticipacao);

          launchActivity(FotoEventoActivity.this, DummyActivity3.class, bundle, ACTIVITY_DUMMY3);

        } else {
          Toast.makeText(FotoEventoActivity.this, "Falta configura��o !!!", Toast.LENGTH_LONG).show();
          Log.w(TAG, "mParticipante ou mParticipacao ou mContratante ou mEvento � null");
        }

      }

    });

    // -----------------------------------
    // Tratamento do evento do bot�o Sair
    // -----------------------------------
    btnSair.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        dialogoDesejaSairDaAplicacao();

        // finish();

      }

    });

  }

  /**
   * onPause()
   */
  @Override
  protected void onPause() {

    super.onPause();

    Log.d(TAG, "*** onPause() ***");

  }

  /**
   * onDestroy()
   */
  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "*** onDestroy() ***");
  }

  /**
   * Cria��o dos Menus
   * 
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // criaMenuParticipante(menu);

    criaMenuManutencao(menu);

    // criaMenuCamera(menu);

    return super.onCreateOptionsMenu(menu);
  }

  /**
   * criaMenuManutencao(Menu menu)
   * 
   * @param menu
   */
  private void criaMenuManutencao(Menu menu) {
    // ----------------------------------------------
    // Menu: Manuten��o
    // ---------------------------------------------
    MenuItem manutencao = menu.add(0, 3, 0, "Manuten��o");
    manutencao.setIcon(R.drawable.ic_launcher);

    manutencao.setOnMenuItemClickListener(new OnMenuItemClickListener() {

      public boolean onMenuItemClick(MenuItem item) {
        launchActivityocessaClickItemMenuManutencao();
        return false;
      }
    });

  }

  /**
   * launchActivity(Context ctx, Class<?> cls, Bundle params, int requestCode)
   * 
   * Lan�a uma Activity passando o contexto de execu��o, o nome da classe que
   * ser� executada, par�metros e o requestCode, isto �, o c�digo de requisi��o
   * de execu��o da Activity.
   * 
   * @param ctx
   *          Contexto
   * @param cls
   *          Classe que ser� executada
   * @param params
   *          Par�metros enviados
   * @param requestCode
   *          C�digo da solicita��o
   * 
   * 
   */
  private void launchActivity(Context ctx, Class<?> cls, Bundle params, int requestCode) {

    // Intent intent = new Intent(FotoEventoActivity.this,CameraActivity.class);
    Intent intent = new Intent(ctx, cls);

    if (params != null) {
      intent.putExtras(params);
    }

    Log.i(TAG, "launchActivity() - Lan�ando a Activity code: " + requestCode);

    // Inicia a Activity
    startActivityForResult(intent, requestCode);

  }

  /**
   * 
   */
  private void launchActivityocessaClickItemMenuManutencao() {

    if (mAdminAccount) {

      // verifica o login do usu�rio antes de entrar no menu de manuten��o

      if (mLogged) {
        // usu�rio est� logado no sistema com senha de administrador

        launchActivityManutencao();

      } else {
        // usu�rio n�o est� logado no sistema
        // usu�rio dever� se loggar para conseguir fazer as
        // manuten��es no sistema

        launchActivityLogin();

        //
        // TODO se o resultado da execu��o do login for v�lido
        // ent�o devemos chamar automaticamente a tela de manuten��o
        //
        // launchActivityManutencao();
        //
      }

    } else {
      // n�o verifica o login do usu�rio antes de entrar no menu de manuten��o
      mLogged = true;
      launchActivityManutencao();
    }

  }

  /**
   * lancaActivityManutencao()
   */
  private void launchActivityManutencao() {

    Intent intent = new Intent(FotoEventoActivity.this, ManutencaoActivity.class);

    intent.putExtra("br.com.mltech.contratante", mContratante);
    intent.putExtra("br.com.mltech.evento", mEvento);
    intent.putExtra("br.com.mltech.lista", (ArrayList<Participacao>) mListaParticipacao);

    startActivityForResult(intent, ACTIVITY_MANUTENCAO);

  }

  /**
   * launchActivityLogin()
   */
  private void launchActivityLogin() {

    /*
     * Intent intent = new Intent(FotoEventoActivity.this, LoginActivity.class);
     * 
     * startActivityForResult(intent, ACTIVITY_LOGIN);
     */

    launchActivity(FotoEventoActivity.this, LoginActivity.class, null, ACTIVITY_LOGIN);

  }

  /**
   * launchActivityCamera()
   */
  private void launchActivityCamera() {

    // TODO esse m�todo precisa verifica��o
    int escolha = 2;

    Bundle params = null;

    if (escolha == 1) {

      params = new Bundle();
      params.putString("msg", "Ol�");

      launchActivity(FotoEventoActivity.this, CameraActivity.class, params, ACTIVITY_CAMERA);

    } else if (escolha == 2) {

    }

  }

  /**
   * onActivityResult(int requestCode, int resultCode, Intent data)
   * 
   * Called when an activity you launched exits
   * 
   * @param requestCode
   * @param resultCode
   * @param data
   * 
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    Log.i(TAG, "onActivityResult(request " + requestCode + ", result=" + resultCode + ", data " + data + ") ...");

    if (requestCode == ACTIVITY_PARTICIPANTE) {
      resultActivityParticipante(resultCode, data);
    } else if (requestCode == ACTIVITY_LOGIN) {
      resultActivityLogin(resultCode, data);
    } else if (requestCode == ACTIVITY_MANUTENCAO) {
      resultActivityManutencao(resultCode, data);
    } else if (requestCode == ACTIVITY_CAMERA) {
      resultActivityCamera(resultCode, data);
    } else if (requestCode == ACTIVITY_DUMMY3) {
      resultActivityDummy3(resultCode, data);
    } else {
      Toast.makeText(this, "Resultado inexperado", Toast.LENGTH_SHORT).show();

      Log.w(TAG, "onActivityResult() - Erro ... requestCode: " + requestCode + " n�o pode ser processado");
    }

  }

  /**
   * activityManutencaoResult(int resultCode, Intent data)
   * 
   * Resultado da execu��o da Activity de Manuten��o
   * 
   * @param resultCode
   *          Resultado da execu��o da activity
   * @param data
   *          Intent contendo o resultado (se houver algum)
   * 
   */
  private void resultActivityManutencao(int resultCode, Intent data) {

    Log.d(TAG, "resultActivityManutencao()");

    if (resultCode == RESULT_OK) {

      Log.d(TAG, "resultActivityManutencao() - processando ACTIVITY MANUTENCAO - RESULT_OK");

    } else if (resultCode == RESULT_CANCELED) {
      Log.d(TAG, "resultActivityManutencao() - processando ACTIVITY MANUTENCAO - RESULT_CANCELED");

    } else {
      Log.d(TAG, "resultActivityManutencao() - Erro ... resultCode n�o conhecido: " + resultCode);
    }

  }

  /**
   * activityParticipanteResult(int resultCode, Intent data)
   * 
   * @param resultCode
   *          Resultado da execu��o da Activity
   * @param data
   *          � esperado que a Activity participante retorne as informa��es
   *          sobre o participante e sua participa��o.
   * 
   */
  private void resultActivityParticipante(int resultCode, Intent data) {

    Log.d(TAG, "resultActivityParticipante() ==> processando ACTIVITY_PARTICIPANTE");

    if (resultCode == RESULT_CANCELED) {
      Log.d(TAG, "resultActivityParticipante() - resultCode=RESULT_CANCELED - Participante cancelou sua participa��o");
      return;
    } else if (resultCode != RESULT_OK) {
      Log.w(TAG, "resultActivityParticipante() - resultCode n�o conhecido: " + resultCode);
      return;
    }

    if (data == null) {
      // caso a Intent n�o retorne nada houve algum problema
      Log.w(TAG, "resultActivityParticipante() - A intent n�o retornou os dados esperados");
      return;
    }

    mParticipante = (Participante) data.getSerializableExtra("br.com.mltech.participante");
    mParticipacao = (Participacao) data.getSerializableExtra("br.com.mltech.participacao");

    Log.d(TAG, "resultActivityParticipante() - mParticipante=" + mParticipante);
    Log.d(TAG, "resultActivityParticipante() - mParticipacao=" + mParticipacao);

  }

  /**
   * activityLoginResult(int resultCode, Intent data)
   * 
   * @param resultCode
   *          Resultado da execu��o da Activity
   * @param data
   *          Intent de retorno
   * 
   */
  private void resultActivityLogin(int resultCode, Intent data) {

    String resultado;

    mLogged = false;

    Log.d(TAG, "activityLoginResult()");

    if (resultCode == RESULT_OK) {

      if (data != null) {

        Bundle params = data.getExtras();

        if (params != null) {

          resultado = params.getString("br.com.mltech.usuarioValidado");

          if (resultado != null) {

            // o par�metro foi encontrado
            if ("OK".equals(resultado)) {

              mLogged = true;
              Log.d(TAG, "activityLoginResult() - Login efetuado com sucesso");

              // Lan�a automaticamente a atividade de manuten��o
              launchActivityManutencao();

            } else {

              mLogged = false;
              Log.d(TAG, "activityLoginResult() - Falha no Login");

            }
          } else {
            // o par�metro n�o foi encontrado
          }
        } else {
          // params is null
        }

      } else {
        // data is null
      }

    } else if (resultCode == RESULT_CANCELED) {

      Log.d(TAG, "activityLoginResult() - Login cancelado pelo usu�rio - RESULT_CANCELED");

    } else {
      Log.d(TAG, "activityLoginResult() - Erro ...");
    }

  }

  /**
   * activityCameraResult(int resultCode, Intent data)
   * 
   * @param resultCode
   *          Resultado da execu��o da Activity
   * @param data
   *          Intent contendo os dados de retorno (se houverem)
   * 
   */
  private void resultActivityCamera(int resultCode, Intent data) {

    String resultado;
    String filename;

    byte[] dados;

    Log.d(TAG, "resultActivityCamera() - processando ACTIVITY_CAMERA");

    if (resultCode == RESULT_OK) {

      Log.d(TAG, "resultActivityCamera() - activityprocessada com sucesso: RESULT_OK");

      if (data != null) {

        Bundle params = data.getExtras();

        if (params != null) {

          resultado = params.getString("br.com.mltech.usuarioValidado");

          if (resultado != null) {
            // o par�metro foi encontrado
          }

          dados = params.getByteArray("br.com.mltech.dados");
          filename = params.getString("br.com.mltech.image.filename");

          Log.d(TAG, "resultActivityCamera() - Filename=" + filename);
          if (dados != null) {
            Log.d(TAG, "resultActivityCamera() - Dados=" + dados.length);
          }

        }
      }

    } else if (resultCode == RESULT_CANCELED) {
      Log.d(TAG, "resultActivityCamera() - RESULT_CANCELED");
    } else {
      Log.d(TAG, "resultActivityCamera() - valor retornado " + resultCode + " � desconhecido.");
    }

  }

  /**
   * resultActivityDummy3(int resultCode, Intent data)
   * 
   * Processo o resultado da execu��o da activity
   * 
   * @param resultCode
   *          Resultado da execu��o da activity
   * @param data
   *          Intent recebida (se houver)
   * 
   */
  private void resultActivityDummy3(int resultCode, Intent data) {

    String result = null;

    Log.d(TAG, "==> resultActivityDummy3() - Executando o resultado do processamento da ACTIVITY DUMMY3");

    Log.d(TAG, "==> resultActivityDummy3() - resultCode=" + resultCode);

    if (data == null) {
      // Activity n�o retornou dados
      Log.w(TAG, "resultActivityDummy3() - Activity Dummy3 n�o retornou dados");

    } else {

      Log.i(TAG, "resultActivityDummy3 - data=" + data);

      // Obtem o resultado da execu��o da activity
      result = data.getStringExtra("br.com.mltech.result");

      Log.i(TAG, "resultActivityDummy3() - result=" + result);

    }

    // Obt�m informa��es sobre a intent que chamou a activity
    Intent i = getIntent();

    Bundle ss = i.getExtras();

    if (ss != null) {

      Log.w(TAG, "resultActivityDummy3() - bundle ss possui as seguintes informa��es:");

      Set<String> chaves = ss.keySet();

      for (String chave : chaves) {
        Log.v(TAG, "    chave=" + chave);
      }

    } else {

      Log.w(TAG, "resultActivityDummy3() - bundle ss est� vazio");

    }

    if (resultCode == RESULT_OK) {

      Log.i(TAG, "resultActivityDummy3() - executada com sucesso");

      // atualiza a lista de participantes
      updateListaParticipacao();

    } else {

      // opera��o cancelada
      Log.w(TAG, "resultActivityDummy3() - Opera��o ocorreu com erros");

    }

  }

  /**
   * lerConfiguracoes(String name)
   * 
   * L� as configura��es gravadas em um arquivo de prefer�ncia. Inicia os
   * atributos mContratante e mEvento
   * 
   * @param name
   *          Identificador do sistema de prefer�ncias compartilhadas
   * 
   */
  private void lerConfiguracoes(String name) {

    Log.d(TAG, "lerConfiguracoes() - Lendo as prefer�ncias compartilhadas: " + name);

    mPreferences = getSharedPreferences(name, MODE_PRIVATE);

    // L� todas as entradas
    Map<?, ?> chaves = mPreferences.getAll();

    // Obtem o conjunto de todas as entradas
    Set<?> set = chaves.entrySet();

    // Exibe o n� de entradas no arquivo de prefer�ncias
    Log.d(TAG, "lerConfiguracoes() - n� de entradas do arquivo de prefer�ncias=" + set.size());

    Iterator<?> i = set.iterator();

    int j = 0;

    // Exibe os elementos
    while (i.hasNext()) {

      j++;

      Entry<?, ?> me = (Entry<?, ?>) i.next();

      if (DEBUG == 1) {
        Log.d(TAG, "  " + j + ") " + me.getKey() + "=" + me.getValue());
      }

    }

    // Contratante
    if ((mContratante == null) && (set.size() > 0)) {
      // inicializa o contrante

      //
      mContratante = new Contratante();

      mContratante.setNome((String) chaves.get("contratante_nome"));
      mContratante.setEmail((String) chaves.get("contratante_email"));
      mContratante.setTelefone((String) chaves.get("contratante_telefone"));

    }

    // Evento
    if ((mEvento == null) && (set.size() > 0)) {

      mEvento = new Evento();

      mEvento.setContratante(mContratante);

      mEvento.setBordaPolaroid((String) chaves.get("evento_borda_polaroid"));
      mEvento.setBordaCabine((String) chaves.get("evento_borda_cabine"));

      // TODO
      // edit.putString("evento_envia_facebook", mEvento.isEnviaFacebook() ==
      // true ? "true" : "false");
      // edit.putString("evento_envia_twitter", mEvento.isEnviaTwitter() == true
      // ? "true" : "false");

      //

      mEvento.setNome((String) chaves.get("evento_nome"));

      mEvento.setEmail((String) chaves.get("evento_email"));

      mEvento.setEndereco((String) chaves.get("evento_endereco"));
      mEvento.setCidade((String) chaves.get("evento_cidade"));
      mEvento.setEstado((String) chaves.get("evento_estado"));
      mEvento.setCep((String) chaves.get("evento_cep"));

      mEvento.setData((String) chaves.get("evento_data"));

      mEvento.setTelefone((String) chaves.get("evento_telefone"));

      /*
       * 
       * boolean b1, b2;
       * 
       * b1 = ((String) chaves.get("evento_envia_facebook")).equals("true") ?
       * true : false; b2 = ((String)
       * chaves.get("evento_envia_twitter")).equals("true") ? true : false;
       * 
       * mEvento.setEnviaFacebook(b1); mEvento.setEnviaTwitter(b2);
       */

      String[] paramEventos = new String[5];

      paramEventos[0] = (String) chaves.get("evento_param1");
      paramEventos[1] = (String) chaves.get("evento_param2");
      paramEventos[2] = (String) chaves.get("evento_param3");
      paramEventos[3] = (String) chaves.get("evento_param4");
      paramEventos[4] = (String) chaves.get("evento_param5");

      Parametros parametros = new Parametros(paramEventos);

      mEvento.setParametros(parametros);

    }

  }

  /**
   * startActivityParticipante(View v)
   * 
   * Inicia a Activity Participante que obt�m as informa��es do participante do
   * evento e suas prefer�ncias quanto ao tipo e filtro que ser� aplicado a
   * foto.
   * 
   * @param v
   *          View
   */
  private void startActivityParticipante(View v) {

    Log.d(TAG, "startActivityParticipante() - inicio");
    launchActivity(FotoEventoActivity.this, ParticipanteActivity.class, null, ACTIVITY_PARTICIPANTE);
    Log.d(TAG, "startActivityParticipante() - fim");

  }

  /**
   * isCondicoesIniciaisSatisfeitas()
   * 
   * Verifica se as configura��es iniciais est�o satisfeitas para execu��o de
   * uma participa��o no evento.
   * 
   * contratante - ter um contratante configurado evento - ter um evento
   * configurado
   * 
   * @return true se as condi��es forem satisfeitas e false, caso contr�rio
   */
  private boolean isCondicoesIniciaisSatisfeitas() {

    /*
     * as condi��es necess�rias para execu��o da activity � ter: - contratante -
     * evento - participante - participa��o
     */

    boolean b = true;

    // verifica se existe um contratante
    if (mContratante == null) {
      Log.w(TAG, "isCondicoesIniciaisSatisfeitas() - Contratante n�o foi configurado");
      Toast.makeText(this, "Contratante n�o foi configurado", Toast.LENGTH_LONG).show();
      b = false;
    }

    // verifica se existe um evento cadastrado
    if (mEvento == null) {
      Log.w(TAG, "isCondicoesIniciaisSatisfeitas() - Evento n�o foi configurado");
      Toast.makeText(this, "Evento n�o foi configurado", Toast.LENGTH_LONG).show();
      b = false;
    }

    // verifica se as bordas das fotos j� foram disponibilizadas ao
    // evento

    if (mEvento != null) {
      Log.i(TAG, "getBordaCabine: " + mEvento.getBordaCabine());
      Log.i(TAG, "getBordaPolaroid: " + mEvento.getBordaPolaroid());
    } else {
      Log.w(TAG, "Evento � null");
    }

    if ((mEvento != null) && ((mEvento.getBordaCabine() == null) || (mEvento.getBordaPolaroid() == null))) {
      // TODO alterar essa condi��o
      Log.w(TAG, "isCondicoesIniciaisSatisfeitas() - Bordas n�o foram configuradas");
      Toast.makeText(this, "Bordas n�o foram configuradas", Toast.LENGTH_LONG).show();
      b = false;
    }

    return b;

  }

  /**
   * updateListaParticipacao()
   * 
   * Insere um novo participante na lista de participa��o do evento
   * 
   */
  private void updateListaParticipacao() {

    Log.v(TAG, "--------------------------------");
    Log.v(TAG, " updateListaParticipa��o        ");
    Log.v(TAG, "--------------------------------");

    if (mListaParticipacao != null) {

      // adiciona uma nova participa��o
      mListaParticipacao.add(mParticipacao);

      // incrementa o contador de participantes do evento
      // TODO o n� de participante poderia aparecer na tela principal
      mNumParticipantes++;
      Log.v(TAG, "updateListaParticipa��o() - n� de participantes at� o momento: " + mNumParticipantes);

    } else {

      Log.w(TAG, "updateListaParticipa��o() - Lista de participantes � null. Participante n�o foi adicionado");

    }

  }

  /**
   * preparaAmbiente()
   * 
   * Prepara o ambiente para grava��o das fotos
   * 
   */
  private boolean preparaAmbiente() {

    if (ct == null) {
      Log.w(TAG, "preparaAmbiente() - CameraTools n�o foi instanciado");
      return false;
    }

    boolean b = false;
    if (ct.isExternalStorageMounted()) {

      Log.i(TAG, "preparaAmbiente() - SDCARD est� montado");

      File f = null;

      f = ct.getDir2("fotoevento/fotos");
      ct.ShowFileDetails(f, "fotoevento/fotos");

      f = ct.getDir2("fotoevento/molduras");
      ct.ShowFileDetails(f, "fotoevento/molduras");

      f = ct.getDir2("fotoevento/telainicial");
      ct.ShowFileDetails(f, "fotoevento/telainicial");

      b = true;

    } else {

      Log.i(TAG, "preparaAmbiente() - SDCARD n�o est� montado");

      // TODO aqui seria melhor exibir uma caixa de di�logo
      Toast.makeText(this, "SDCARD n�o est� montado !!!", Toast.LENGTH_LONG).show();

    }

    return b;

  }

  /**
   * obtemImagemTelaInicial()
   * 
   * Obtem um bitmap que ser� exibido na tela inicial da aplica��o. Caso
   * nenhuma bitmap seja configurado ser� usado um bitmap padr�o.
   * 
   */
  private Bitmap obtemImagemTelaInicial() {

    Bitmap bitmap = null;

    SharedPreferences preferences = getSharedPreferences(PREF_EMAIL, MODE_PRIVATE);

    if (preferences == null) {
      Log.w(TAG, "obtemImagemTelaInicial() - N�o foi poss�vel abrir o arquivo de prefer�ncias: " + PREF_EMAIL);
      return null;
    }

    String urlImagem = preferences.getString("preferencias_url_imagem", "");

    if ((urlImagem == null) || (urlImagem.equals(""))) {
      return null;
    }

    File f = new File(urlImagem);

    if (f != null) {

      if (f.exists()) {
        
        // arquivo existe
        // carrega o arquivo e exibe a foto
        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
        
      } else {
        
        // carrega a foto default (padr�o)
        // TODO definir o tamanho da imagem inicial

        Log.w(TAG, "obtemImagemTelaInicial() - Arquivo: " + f.getAbsolutePath() + " n�o foi encontrado.");

      }

    }

    preferences = null;

    return bitmap;

  }

  /**
   * dialogoDesejaSairDaAplicacao()
   * 
   * Exibe uma caixa de di�logo e a mensagem perguntando se o usu�rio deseja
   * sair da aplica��o.
   * 
   * Caso o usu�rio pressione "Sim" a aplica��o ser� encerrada. Caso contr�rio a
   * caixa de di�logo ir� desaparecer e a aplica��o continua�ra em execu��o.
   * 
   */
  private void dialogoDesejaSairDaAplicacao() {

    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setMessage("Voc� tem certeza que deseja sair ?").setCancelable(false)
        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {

          public void onClick(DialogInterface dialog, int id) {
            Log.i(TAG, "dialogoDesejaSairDaAplicacao() - Finalizando a aplica��o.");
            FotoEventoActivity.this.finish();
          }

        }).setNegativeButton("N�o", new DialogInterface.OnClickListener() {

          public void onClick(DialogInterface dialog, int id) {
            Log.i(TAG, "dialogoDesejaSairDaAplicacao() - o usu�rio cancelou o pedido de sa�da.");
            dialog.cancel();
          }

        });

    AlertDialog alert = builder.create();
    alert.show();

  }

}
