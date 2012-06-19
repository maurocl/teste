package br.com.mltech;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import br.com.mltech.utils.FileUtils;
import br.com.mltech.utils.camera.CameraTools;

/**
 * Activity principal da aplica��o
 * 
 * @author maurocl
 * 
 */
public class FotoEventoActivity extends Activity implements Constantes {

  private static final String TAG = "FotoEventoActivity";

  // indica o uso do DEBUG
  private static int DEBUG = 0;

  /* Identificadores das Activities */

  private static final int ACTIVITY_LOGIN = 103;

  private static final int ACTIVITY_MANUTENCAO = 104;

  private static final int ACTIVITY_DUMMY3 = 120;

  private static final int ACTIVITY_CAMERA = 105;

  // Estabelece a activity respons�vel por tirar as fotos
  private static int ACTIVITY_FOTOS = ACTIVITY_DUMMY3;

  // Estabelece a classe respons�vel por tirar as fotos
  private static Class<DummyActivity3> CLASS_FOTOS = DummyActivity3.class;

  //
  private SharedPreferences mPreferences;

  // Defini��o do contrante, evento e participa��es
  private static Contratante mContratante;

  private static Evento mEvento;

  private static Participante mParticipante;

  private static Participacao mParticipacao;

  // Defini��o da lista de participa��o no evento
  private static List<Participacao> mListaParticipacao;

  // N� de participantes at� o momento
  private int mNumParticipantes = 0;

  // Indica se o usu�rio se encontra loggado a aplica��o
  private boolean mLogged;

  // indica a necessidade do uso de conta de administrador para acesso ao menu
  // de manuten��o. � usado para testes
  // TODO incluir essa configura��o nas configura��es da aplica��o
  //
  private boolean mAdminAccount = false;

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

    // prepara o ambiente para execu��o da aplica��o
    boolean resultado = preparaAmbiente();

    if (resultado == true) {
      Log.d(TAG, "onCreate() - ambiente preparado para execu��o do software");
    }
    else {
      Log.d(TAG, "onCreate() - ambiente n�o est� preparado para execu��o do software");
    }

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

          // launchActivity(FotoEventoActivity.this, ParticipanteActivity.class,
          // null, ACTIVITY_PARTICIPANTE);

        }

      });

    }

    // ----------------------------
    // trata o bot�o Participar
    // ----------------------------

    btn.setText("*** Participar ***");

    btn.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        boolean condicoesSatisfeitas = isCondicoesIniciaisSatisfeitas();

        if (condicoesSatisfeitas) {
          // condi��es iniciais foram satisfeitas

          Bundle bundle = new Bundle();

          bundle.putSerializable(CONTRATANTE, mContratante);
          bundle.putSerializable(EVENTO, mEvento);
          bundle.putSerializable(PARTICIPANTE, mParticipante);
          bundle.putSerializable(PARTICIPACAO, mParticipacao);

          //launchActivity(FotoEventoActivity.this, DummyActivity3.class, bundle, ACTIVITY_DUMMY3);          
          //launchActivity(getBaseContext(), DummyActivity3.class, bundle, ACTIVITY_FOTOS);

          // lan�a uma activity implementada na classe CLASS_FOTOS, com c�digo de retorno ACTIVITY_FOTOS, 
          // passando como par�metros as informa��es sobre o contratante, evento e participante. 
          launchActivity(getBaseContext(), CLASS_FOTOS, bundle, ACTIVITY_FOTOS);

        } else {
          // TODO mensagem abaixo n�o est� muito clara
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

    // cria o item de menu Manuten��o
    criaMenuManutencao(menu);

    return super.onCreateOptionsMenu(menu);
  }

  /**
   * criaMenuManutencao(Menu menu)
   * 
   * @param menu
   *          Inst�ncia da classe Menu
   */
  private void criaMenuManutencao(Menu menu) {

    // ----------------------------------------------
    // Menu: Manuten��o
    // ---------------------------------------------
    MenuItem manutencao = menu.add(0, 3, 0, "Manuten��o");
    manutencao.setIcon(R.drawable.ic_launcher);

    manutencao.setOnMenuItemClickListener(new OnMenuItemClickListener() {

      public boolean onMenuItemClick(MenuItem item) {

        processaClickItemMenuManutencao();
        return false;

      }
      
    });

  }

  /**
   * launchActivity(Context ctx, Class<?> cls, Bundle params, int requestCode)
   * 
   * Lan�a uma Activity passando o contexto de execu��o, o nome da classe que
   * ser� executada, par�metros extras (se houverem) e o requestCode, isto �, o
   * c�digo de requisi��o que � usado para identificar o retorno da activity.
   * 
   * @param ctx
   *          Contexto da aplica��o
   * 
   * @param cls
   *          Classe que ser� executada
   * 
   * @param params
   *          Par�metros extras (se for necess�rio)
   * 
   * @param requestCode
   *          C�digo da solicita��o (da identifica��o da Activity)
   * 
   * 
   */
  private void launchActivity(Context ctx, Class<?> cls, Bundle params, int requestCode) {

    Intent intent = new Intent(ctx, cls);

    if (params != null) {
      intent.putExtras(params);
    }

    Log.i(TAG, "launchActivity() - Lan�ando a Activity code: " + getActivityName(requestCode) + "(" + requestCode + ")");

    // Inicia a activity e precisa saber o valor de retorno 
    startActivityForResult(intent, requestCode);

  }

  /**
   * processaClickItemMenuManutencao()
   * 
   * Executa a a��o do click no menu item Manuten��o
   * 
   */
  private void processaClickItemMenuManutencao() {

    if (mAdminAccount) {

      // verifica o login do usu�rio antes de entrar no menu de manuten��o

      if (mLogged) {
        // usu�rio est� logado no sistema com senha de administrador

        // lan�a a activity ("tela") de Manuten��o
        launchActivityManutencao();

      } else {
        // usu�rio n�o est� logado no sistema
        // usu�rio dever� se loggar para conseguir fazer as
        // manuten��es no sistema

        // lan�a a activity ("tela") de Login
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

      // lan�a a activity ("tela") de Manuten��o
      launchActivityManutencao();

    }

  }

  /**
   * lancaActivityManutencao()
   * 
   * Lan�a a Activity respons�vel pela manuten��o da aplica��o
   * 
   */
  private void launchActivityManutencao() {

    // cria uma "mensagem" (intent)
    Intent intent = new Intent(FotoEventoActivity.this, ManutencaoActivity.class);

    // passa os par�metros para tela de manuten��o
    intent.putExtra(CONTRATANTE, mContratante);
    intent.putExtra(EVENTO, mEvento);
    intent.putExtra(LISTA, (ArrayList<Participacao>) mListaParticipacao);

    // inicia a activity
    startActivityForResult(intent, ACTIVITY_MANUTENCAO);

  }

  /**
   * launchActivityLogin()
   * 
   * Lan�a a tela de login
   * 
   */
  private void launchActivityLogin() {

    launchActivity(FotoEventoActivity.this, LoginActivity.class, null, ACTIVITY_LOGIN);

  }

  /**
   * onActivityResult(int requestCode, int resultCode, Intent data)
   * 
   * Called when an activity you launched exits
   * 
   * @param requestCode
   *          c�digo da requisi��o
   * 
   * @param resultCode
   *          c�digo do resultado da execu��o da activity
   * 
   * @param data
   *          intent de retorno
   * 
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    Log.i(TAG, "onActivityResult(request (" + requestCode + ") " + getActivityName(requestCode) + ", result=" + resultCode
        + ", data " + data + ") ...");

    if (requestCode == ACTIVITY_LOGIN) {
      //
      resultActivityLogin(resultCode, data);

    } else if (requestCode == ACTIVITY_MANUTENCAO) {
      //
      resultActivityManutencao(resultCode, data);

    } else if (requestCode == ACTIVITY_CAMERA) {
      // TODO verificar se esse item pode ser retirado
      resultActivityCamera(resultCode, data);

    } else if (requestCode == ACTIVITY_DUMMY3) {
      //
      resultActivityDummy3(resultCode, data);

    } else {

      Toast.makeText(this, "Resultado inexperado", Toast.LENGTH_SHORT).show();

      Log.w(TAG, "onActivityResult() - Erro ... requestCode: " + requestCode + " n�o pode ser processado");

    }

  }

  /**
   * resultActivityManutencao(int resultCode, Intent data)
   * 
   * Resultado da execu��o da Activity de Manuten��o
   * 
   * @param resultCode
   *          Resultado da execu��o da activity
   * 
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
   * resultActivityLogin(int resultCode, Intent data)
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
   * resultActivityDummy3(int resultCode, Intent data)
   * 
   * Processa o resultado da execu��o da activity respons�vel por obter uma foto
   * 
   * @param resultCode
   *          Resultado da execu��o da activity
   * 
   * @param data
   *          Uma intent que pode conter dados extras como resultado
   * 
   */
  private void resultActivityDummy3(int resultCode, Intent data) {

    String resultado = null;

    Log.d(TAG, "==> resultActivityDummy3() - Processando o resultado da tela ACTIVITY DUMMY3");

    Log.d(TAG, "==> resultActivityDummy3() - resultCode=" + resultCode + ", " + getActivityName(resultCode));

    if (data == null) {

      // Activity n�o retornou dados
      Log.w(TAG, "resultActivityDummy3() - Activity Dummy3 n�o retornou dados");

    } else {

      // exibe os dados retornados pela activity
      Log.i(TAG, "resultActivityDummy3 - data=" + data);

      // Obtem o resultado da execu��o da activity
      resultado = data.getStringExtra("br.com.mltech.result");

      mParticipante = (Participante) data.getSerializableExtra(Constantes.PARTICIPANTE);
      mParticipacao = (Participacao) data.getSerializableExtra(Constantes.PARTICIPACAO);

      // exibe o resultado da execu��o da activity
      Log.i(TAG, "resultActivityDummy3() - resultado=" + resultado);

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
   * resultActivityCamera(int resultCode, Intent data)
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
      Log.v(TAG, "isCondicoesIniciaisSatisfeitas() - getBordaCabine: " + mEvento.getBordaCabine());
      Log.v(TAG, "isCondicoesIniciaisSatisfeitas() - getBordaPolaroid: " + mEvento.getBordaPolaroid());
    } else {
      Log.w(TAG, "isCondicoesIniciaisSatisfeitas() - Evento � nulo");
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
   * preparaAmbiente()
   * 
   * Prepara o sistema de arquivos ambiente para conter as molduras, a tela
   * inicial e para grava��o das fotos. O m�tod retorna false caso o SDCard n�o
   * esteja montado.
   * 
   * @return true caso o ambiente esteja criado com sucesso ou false caso haja
   *         algum erro.
   * 
   */
  private boolean preparaAmbiente() {

    if (!CameraTools.isExternalStorageMounted()) {
      Log.i(TAG, "preparaAmbiente() - SDCARD n�o est� montado");

      // TODO aqui seria melhor exibir uma caixa de di�logo
      Toast.makeText(this, "SDCARD n�o est� montado !!!", Toast.LENGTH_LONG).show();

      return false;

    }

    Log.d(TAG, "preparaAmbiente() - SDCARD est� montado");

    File f = null;

    f = CameraTools.getDir2("fotoevento/fotos");

    if (DEBUG == 1) {
      FileUtils.showFileDetails(f, "fotoevento/fotos");
    }

    f = CameraTools.getDir2("fotoevento/molduras");
    if (DEBUG == 1) {
      FileUtils.showFileDetails(f, "fotoevento/molduras");
    }

    f = CameraTools.getDir2("fotoevento/telainicial");
    if (DEBUG == 1) {
      FileUtils.showFileDetails(f, "fotoevento/telainicial");
    }

    return true;

  }

  /**
   * lerConfiguracoes(String name)
   * 
   * L� as configura��es gravadas em um arquivo de prefer�ncia. Inicia os
   * atributos mContratante e mEvento.
   * 
   * @param name
   *          Identificador do sistema de prefer�ncias compartilhadas
   * 
   */
  private void lerConfiguracoes(String name) {

    Log.d(TAG, "lerConfiguracoes() - Lendo as prefer�ncias compartilhadas: " + name);

    mPreferences = getSharedPreferences(name, MODE_PRIVATE);

    if (mPreferences == null) {
      Log.w(TAG, "lerConfiguracoes() - mPreferences � nulo");
      return;
    }

    // n� de itens de configura��o lidos
    int numItensDeConfiguracao = 0;

    if (mPreferences.getAll() != null) {
      numItensDeConfiguracao = mPreferences.getAll().size();
    }

    // Exibe o n� de entradas no arquivo de prefer�ncias
    Log.d(TAG, "lerConfiguracoes() - n� de entradas do arquivo de prefer�ncias: " + numItensDeConfiguracao);

    // Contratante
    if ((mContratante == null)) {
      // inicializa o contrante

      //
      mContratante = new Contratante();

      mContratante.setNome(mPreferences.getString(Constantes.CONTRATANTE_NOME, ""));
      mContratante.setEmail(mPreferences.getString(Constantes.CONTRATANTE_EMAIL, ""));
      mContratante.setTelefone(mPreferences.getString(Constantes.CONTRATANTE_TELEFONE, ""));

    }

    // Evento
    if (mEvento == null) {

      mEvento = new Evento();

      mEvento.setContratante(mContratante);

      mEvento.setNome(mPreferences.getString(Constantes.EVENTO_NOME, ""));
      mEvento.setEmail(mPreferences.getString(Constantes.EVENTO_EMAIL, ""));
      mEvento.setEndereco(mPreferences.getString(Constantes.EVENTO_ENDERECO, ""));
      mEvento.setCidade(mPreferences.getString(Constantes.EVENTO_CIDADE, ""));
      mEvento.setEstado(mPreferences.getString(Constantes.EVENTO_ESTADO, ""));
      mEvento.setCep(mPreferences.getString(Constantes.EVENTO_CEP, ""));
      mEvento.setData(mPreferences.getString(Constantes.EVENTO_DATA, ""));
      mEvento.setTelefone(mPreferences.getString(Constantes.EVENTO_TELEFONE, ""));
      mEvento.setBordaPolaroid(mPreferences.getString(Constantes.EVENTO_BORDA_POLAROID, ""));
      mEvento.setBordaCabine(mPreferences.getString(Constantes.EVENTO_BORDA_CABINE, ""));

      mEvento.setEnviaFacebook(mPreferences.getString(Constantes.EVENTO_ENVIA_FACEBOOK, "false"));
      mEvento.setEnviaTwitter(mPreferences.getString(Constantes.EVENTO_ENVIA_TWITTER, "false"));

      String[] paramEventos = new String[5];

      paramEventos[0] = mPreferences.getString(Constantes.EVENTO_PARAM1, "");
      paramEventos[1] = mPreferences.getString(Constantes.EVENTO_PARAM2, "");
      paramEventos[2] = mPreferences.getString(Constantes.EVENTO_PARAM3, "");
      paramEventos[3] = mPreferences.getString(Constantes.EVENTO_PARAM4, "");
      paramEventos[4] = mPreferences.getString(Constantes.EVENTO_PARAM5, "");

      Parametros parametros = new Parametros(paramEventos);

      mEvento.setParametros(parametros);

    }

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

    if (mParticipacao == null) {
      Log.w(TAG, "updateListaParticipa��o() - participacao � nula, portanto n�o foi adicionada");
      return;
    }

    if (mListaParticipacao == null) {
      Log.w(TAG, "updateListaParticipa��o() - Lista de participantes � nula. Participante n�o foi adicionado");
      return;
    }

    // adiciona uma nova participa��o
    mListaParticipacao.add(mParticipacao);

    // incrementa o contador de participantes do evento
    // TODO o n� de participante poderia aparecer na tela principal
    mNumParticipantes++;

    Log.v(TAG, "updateListaParticipa��o() - n� de participantes at� o momento: " + mNumParticipantes);

    Toast.makeText(this, "Obrigado pela participa��o !", Toast.LENGTH_SHORT).show();

  }

  /**
   * obtemImagemTelaInicial()
   * 
   * Obtem um bitmap que ser� exibido na tela inicial da aplica��o. Caso nenhum
   * bitmap seja configurado ser� usado um bitmap padr�o.
   * 
   * @return uma inst�ncia do objeto Bitmap contendo a imagem da tela inicial se
   *         configurada ou null caso haja algum erro.
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
      Log.w(TAG, "obtemImagemTelaInicial() - N�o foi poss�vel encontrar a tela inicial. Usando a tela padr�o");
      return null;
    }

    File f = new File(urlImagem);

    if (f != null) {

      if (f.exists()) {

        // arquivo existe
        // carrega (decodifica o arquivo e cria um objeto bitmap)
        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

      } else {

        // arquivo n�o existe (ou n�o foi configurado)

        // carrega a foto default (padr�o)
        // TODO definir o tamanho da imagem inicial

        Log.w(TAG, "obtemImagemTelaInicial() - Arquivo: " + f.getAbsolutePath() + " n�o foi encontrado.");

      }

    }

    preferences = null;

    // retorna o bitmap
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

  /**
   * getActivityName(int i)
   * 
   * @param requestCode
   *          inteiro originalmente fornecido na chamada ao m�todo
   *          startActivityForResult() permitindo quye seja identificado quem
   *          recebeu o resultado.
   * 
   * @return o nome associado ao c�digo usado na execu��o da Activity.
   */
  private String getActivityName(int requestCode) {

    String nome = null;

    switch (requestCode) {
      case ACTIVITY_LOGIN:
        nome = "ACTIVITY_LOGIN";
        break;

      case ACTIVITY_MANUTENCAO:
        nome = "ACTIVITY_MANUTENCAO";
        break;

      case ACTIVITY_DUMMY3:
        nome = "ACTIVITY_DUMMY3";
        break;

      case ACTIVITY_CAMERA:
        nome = "ACTIVITY_CAMERA";
        break;

      default:
        nome = "Activity n�o encontrada.";
        break;
    }

    return nome;

  }

}
