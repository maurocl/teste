
package br.com.mltech.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
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
import br.com.mltech.Constantes;
import br.com.mltech.R;
import br.com.mltech.modelo.Contratante;
import br.com.mltech.modelo.Evento;
import br.com.mltech.modelo.Parametros;
import br.com.mltech.modelo.Participacao;
import br.com.mltech.modelo.Participante;
import br.com.mltech.modelo.RepositorioParticipacao;
import br.com.mltech.modelo.RepositorioParticipacaoScript;
import br.com.mltech.utils.AndroidUtils;
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

  // Arquivo de prefer�ncias da aplica��o
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

  // Indica se o usu�rio se encontra loggado na aplica��o
  private boolean mLogged;

  // indica a necessidade do uso de conta de administrador para acesso ao menu
  // de manuten��o. � usado para testes
  // TODO incluir essa configura��o nas configura��es da aplica��o
  //
  private boolean mAdminAccount = false;

  // Reposit�rio - Participacao
  static RepositorioParticipacao repositorio;

  /**
   * Called when the activity is first created.
   * 
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);

    // Exibe as informa��es sobre o dispositivo executando a aplica��o.
    exibeInformacaoSobreDispositivo();

    // Verifica se existe rede dispon�vel no momento
    if (!AndroidUtils.isNetworkingAvailable(this)) {
      Log.w(TAG, "N�o h� rede dispon�vel no momento !!!");
    }

    int funcao = 1;

    // TODO verificar qual reposit�rio � necess�rio criar aqui !!!

    if (funcao == 1) {
      
      Log.d(TAG, "Abrindo E CRIANDO o reposit�rio ...");
      repositorio = new RepositorioParticipacaoScript(this);

      Log.d(TAG, "Localiza��o da base de dados: " + repositorio.getDbPath());

      File f = new File(repositorio.getDbPath());

      if (f.exists() && f.canRead()) {

        Log.d(TAG, "Tamanho do arquivo: " + f.length());

      }

    }
    else {
      Log.d(TAG, "Abrindo o reposit�rio ...");
      repositorio = new RepositorioParticipacao(this);
    }

    if (savedInstanceState != null) {
      // obt�m as informa��es salvas (se existirem)
    }

    Log.i(TAG, "*** onCreate() ***");

    int orientacaoAtual = this.getResources().getConfiguration().orientation;

    // exibe informa��es sobre a configura��o atual da tela
    if (orientacaoAtual == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
      Log.d(TAG, "onCreate() - Orienta��o atual da tela: " + orientacaoAtual + "(PORTRAIT)");
    } else if (orientacaoAtual == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
      Log.d(TAG, "onCreate() - Orienta��o atual da tela: " + orientacaoAtual + "(LANDSCAPE)");
    } else {
      Log.d(TAG, "onCreate() - Orienta��o atual da tela: " + orientacaoAtual + "(n�o suportado)");
    }

    // prepara o ambiente para execu��o da aplica��o
    boolean resultado = preparaAmbiente();

    if (resultado == true) {
      Log.d(TAG, "onCreate() - ambiente preparado para execu��o do software");
    } else {
      Log.d(TAG, "onCreate() - ambiente n�o est� preparado para execu��o do software");
    }

    // L� a configura��o das prefer�ncias do sistema
    if (mPreferences == null) {
      lerConfiguracoes("preferencias");
    }

    // Instancia a lista de participa��es no evento
    mListaParticipacao = new ArrayList<Participacao>();

    // Verifica se existem algum bitmap configurado para exibi��o na tela inicial
    Bitmap bitmapTelaInicial = obtemImagemTelaInicial();

    if (bitmapTelaInicial != null) {

      ImageView imageViewTelaInicial = (ImageView) findViewById(R.id.imageView1);

      imageViewTelaInicial.setImageBitmap(bitmapTelaInicial);

    }

    Button btnParticipar = (Button) findViewById(R.id.btnBotao);
    btnParticipar.setText("Participar");

    Button btnSair = (Button) findViewById(R.id.btnSair);

    // ----------------------------
    // trata o bot�o Participar
    // ---------------------------
    btnParticipar.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        boolean condicoesSatisfeitas = isCondicoesIniciaisSatisfeitas();

        if (condicoesSatisfeitas) {
          
          // condi��es iniciais foram satisfeitas

          Bundle bundle = new Bundle();

          bundle.putSerializable(CONTRATANTE, mContratante);
          bundle.putSerializable(EVENTO, mEvento);
          bundle.putSerializable(PARTICIPANTE, mParticipante);
          bundle.putSerializable(PARTICIPACAO, mParticipacao);        

          // lan�a uma activity implementada na classe CLASS_FOTOS, com c�digo
          // de retorno ACTIVITY_FOTOS,
          // passando como par�metros as informa��es sobre o contratante, evento
          // e participante.
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

        // executa o di�logo verificando se o usu�rio deseja realmente sair da
        // aplica��o.
        dialogoDesejaSairDaAplicacao();

      }

    });
    
  }

  /**
   * Exibe informa��es sobre o dispositivo executando a aplica��o.
   * 
   */
  private void exibeInformacaoSobreDispositivo() {

    Log.d(TAG, "Informa��es sobre o dispositivo executando a aplica��o: ");
    Log.d(TAG, "DEVICE=" + Build.DEVICE);
    Log.d(TAG, "ID=" + Build.ID);
    Log.d(TAG, "MANUFACTURER=" + Build.MANUFACTURER);
    Log.d(TAG, "MODEL=" + Build.MODEL);
    Log.d(TAG, "USER=" + Build.USER);
  }

  @Override
  protected void onStart() {

    super.onStart();
  }

  @Override
  protected void onResume() {

    super.onResume();

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

    if (repositorio != null) {

      repositorio.fechar();
      Log.w(TAG, "Reposit�rio fechado com sucesso");

    } else {

      Log.w(TAG, "Reposit�rio est� vazio");

    }

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
   * Cria o Menu Manutencao.
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
   * Lan�a a Activity respons�vel pela manuten��o da aplica��o.<br>
   * 
   * <p>S�o passados como par�metros os valores: mContratante, mEvento e lListaParticipacao.
   * 
   */
  private void launchActivityManutencao() {

    // cria uma "mensagem" (intent)
    Intent intent = new Intent(this, ManutencaoActivity.class);

    // passa os par�metros para tela de manuten��o
    intent.putExtra(CONTRATANTE, mContratante);
    intent.putExtra(EVENTO, mEvento);
    intent.putExtra(LISTA, (ArrayList<Participacao>) mListaParticipacao);

    // inicia a activity
    startActivityForResult(intent, ACTIVITY_MANUTENCAO);

  }

  /**
   * Lan�a a tela (activity) respons�vel pelo autentica��o na aplica��o
   * 
   */
  private void launchActivityLogin() {

    launchActivity(this, LoginActivity.class, null, ACTIVITY_LOGIN);

  }

  /**
   * M�todo de callback chamado ap�s execu��o de uma Activity.<br>
   * 
   * @param requestCode
   *          c�digo da requisi��o (da execu��o da activity)
   * 
   * @param resultCode
   *          resultado da execu��o da activity
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
   * Processa o resultado da execu��o da Activity de Manuten��o.
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
   * Processa o resultado da execu��o da Activity de Login.
   * 
   * @param resultCode
   *          Resultado da execu��o da Activity
   * 
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
   * Processa o resultado da execu��o da activity respons�vel por executar um
   * ciclo completo da aplica��o que �:<br>
   * 
   * <ul>
   * 
   * <li>Obter informa��es sobre o participante do evento (nome, email,
   * telefone, etc);
   * 
   * <li>Obter informa��es sobre o tipo da foto (polaroid/cabine) e o efeito de
   * cores aplicado (cores/p&b)
   * 
   * <li>Obter a foto no formato e efeito desejado mais a inclus�o da moldura.
   * 
   * </ul>
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

      int numFotosPolaroid = data.getIntExtra("br.com.mltech.numFotosPolaroid", 0);
      int numFotosCabine = data.getIntExtra("br.com.mltech.numFotosCabine", 0);

      // exibe o resultado da execu��o da activity
      Log.i(TAG, "resultActivityDummy3() - resultado=" + resultado);
      Log.i(TAG, "resultActivityDummy3() - numFotosPolaroid=" + numFotosPolaroid);
      Log.i(TAG, "resultActivityDummy3() - numFotosCabine=" + numFotosCabine);

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
   * Processa o resultado da activity camera;
   * 
   * @param resultCode
   *          Resultado da execu��o da Activity
   * 
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

      Log.d(TAG, "resultActivityCamera() - activity processada com sucesso: RESULT_OK");

      if (data != null) {

        Bundle params = data.getExtras();

        if (params != null) {

          resultado = params.getString("br.com.mltech.usuarioValidado");

          if (resultado != null) {
            // o par�metro foi encontrado
          }

          dados    = params.getByteArray("br.com.mltech.dados");
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
   * Verifica se as configura��es iniciais est�o satisfeitas para execu��o de
   * uma participa��o no evento.<br>
   * 
   * <p>
   * Condi��es:<br>
   * <br>
   * contratante - ter um contratante configurado<br>
   * evento - ter um evento configurado<br>
   * 
   * @return true se as condi��es forem satisfeitas e false, caso contr�rio
   */
  private boolean isCondicoesIniciaisSatisfeitas() {

    boolean b = true;

    // verifica se existe um contratante
    if (mContratante == null) {

      Log.w(TAG, "isCondicoesIniciaisSatisfeitas() - Contratante n�o foi configurado");
      Toast.makeText(this, "Contratante n�o foi configurado", Toast.LENGTH_LONG).show();

      b = false;

    } else {

      if (mContratante.getNome().equals("")) {
        Log.w(TAG, "isCondicoesIniciaisSatisfeitas() - O nome do contratante est� vazio");
        Toast.makeText(this, "Contratante n�o foi configurado", Toast.LENGTH_LONG).show();
        b = false;
      }

    }

    // verifica se existe um evento cadastrado
    if (mEvento == null) {
      Log.w(TAG, "isCondicoesIniciaisSatisfeitas() - Evento n�o foi configurado");
      Toast.makeText(this, "Evento n�o foi configurado", Toast.LENGTH_LONG).show();
      b = false;
    } else {
      if (mEvento.getNome().equals("")) {
        Log.w(TAG, "isCondicoesIniciaisSatisfeitas() - Nome do evento n�o foi configurado.");
        Toast.makeText(this, "Evento n�o foi configurado", Toast.LENGTH_LONG).show();
        b = false;
      }
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
   * <p>
   * Prepara o sistema de arquivos para execu��o da aplica��o. ambiente para
   * conter as molduras, a tela inicial e para grava��o das fotos. <br>
   * 
   * <pre>
   *   +--+ /fotoevento
   *      |--- /fotos
   *      |--- /molduras
   *      |--- /telainicial
   * 
   * </pre>
   * 
   * O m�todo retorna false caso o SDCard n�o esteja montado.<br>
   * 
   * @return true caso o ambiente seja criado com sucesso ou false caso haja
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

    // f = CameraTools.getDir2("fotoevento/fotos");
    f = CameraTools.getDir2(FileUtils.BASE_DIR + File.separator + FileUtils.PHOTO_DIR);

    if (DEBUG == 1) {
      FileUtils.showFileDetails(f, "fotoevento/fotos");
    }

    // f = CameraTools.getDir2("fotoevento/molduras");
    f = CameraTools.getDir2(FileUtils.BASE_DIR + File.separator + FileUtils.MOLDURA_DIR);
    if (DEBUG == 1) {
      FileUtils.showFileDetails(f, "fotoevento/molduras");
    }

    // f = CameraTools.getDir2("fotoevento/telainicial");
    f = CameraTools.getDir2(FileUtils.BASE_DIR + File.separator + FileUtils.TELAINICIAL_DIR);
    if (DEBUG == 1) {
      FileUtils.showFileDetails(f, "fotoevento/telainicial");
    }

    return true;

  }

  /**
   * L� as configura��es gravadas em um arquivo de prefer�ncia e inicializa os
   * atributos contratante e evento.<br>
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

      // cria um novo Contratante
      mContratante = new Contratante();

      mContratante.setNome(mPreferences.getString(Constantes.CONTRATANTE_NOME, ""));
      mContratante.setEmail(mPreferences.getString(Constantes.CONTRATANTE_EMAIL, ""));
      mContratante.setTelefone(mPreferences.getString(Constantes.CONTRATANTE_TELEFONE, ""));

    }

    // Evento
    if (mEvento == null) {

      // cria um novo Evento
      mEvento = new Evento();

      // associa o contratante ao evento
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
   * Insere um novo participante na lista de participa��o do evento.
   * 
   */
  private void updateListaParticipacao() {

    // TODO como persistir os dados da lista de participa��o entre uma execu��o
    // e outra ???
    // quando a lista dever� ser reiniciada ?
    // devemos reinicar a lista quando dermos um 'reset' na aplica��o ?

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
    //mListaParticipacao.add(mParticipacao);

    // Inclui uma nova participa��o

    if (repositorio != null) {

      long myId = repositorio.inserir(mParticipacao);

      if (myId != -1) {

        Log.d(TAG, "Participa��o inserida com sucesso - ID: " + myId);

        mNumParticipantes++;

        // incrementa o contador de participantes do evento
        // TODO o n� de participante poderia aparecer na tela principal

        Log.v(TAG, "updateListaParticipa��o() - n� de participantes at� o momento: " + mNumParticipantes);

      }
      else {
        Log.w(TAG, "Falha na inser��o da Participa��o");

      }

      // atualiza o identificador da participa��o
      mParticipacao.setId(myId);

    } else {

      Log.w(TAG, "Reposit�rio est� vazio");

    }

    Toast.makeText(this, mParticipacao.getParticipante().getNome() + ", obrigado pela participa��o !", Toast.LENGTH_SHORT).show();

  }

  /**
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

    String urlImagem = preferences.getString("preferencias_url_imagem", null);

    preferences = null;

    if (urlImagem == null) {
      Log.w(TAG,
          "obtemImagemTelaInicial() - N�o foi poss�vel encontrar o par�metro de configura��o 'preferencias_url_imagem'. Usando a tela padr�o");
      return null;
    }

    File f = new File(urlImagem);

    if (f.exists()) {

      // Arquivo existente. Carrega (decodifica o arquivo e cria um objeto bitmap)
      bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

    } else {

      // Arquivo n�o existe (ou n�o foi configurado)
      // Carrega a foto default (padr�o)
      // TODO definir o tamanho da imagem inicial

      Log.w(TAG, "obtemImagemTelaInicial() - Arquivo: " + f.getAbsolutePath() + " n�o foi encontrado.");

    }

    // retorna o bitmap
    return bitmap;

  }

  /**
   * Exibe uma caixa de di�logo e a mensagem perguntando se o usu�rio deseja
   * sair da aplica��o.<br>
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
   * Obt�m o nome associado a activity dado seu requestCode.
   * 
   * @param requestCode
   *          inteiro originalmente fornecido na chamada ao m�todo
   *          startActivityForResult() permitindo que seja identificado quem
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
  
  // AsyncTask<Params, Progress, Result>
  private class BuscaBitmapTask extends AsyncTask<String, Void, Bitmap> {
  	
  	private ProgressDialog progress;
  	private Context ctx;
  	
  	private Bitmap bitmap = null;
  	
  	@Override
  	protected void onPreExecute() {

  		super.onPreExecute();
  		
  		progress = ProgressDialog.show(ctx, "title", "message");
  		
  	}
  	
  	@Override
  	protected Bitmap doInBackground(String... params) {
  		// 

      SharedPreferences preferences = getSharedPreferences(PREF_EMAIL, MODE_PRIVATE);

      if (preferences == null) {
        Log.w(TAG, "obtemImagemTelaInicial() - N�o foi poss�vel abrir o arquivo de prefer�ncias: " + PREF_EMAIL);
        return null;
      }

      String urlImagem = preferences.getString("preferencias_url_imagem", null);

      preferences = null;

      if (urlImagem == null) {
        Log.w(TAG,
            "obtemImagemTelaInicial() - N�o foi poss�vel encontrar o par�metro de configura��o 'preferencias_url_imagem'. Usando a tela padr�o");
        return null;
      }

      File f = new File(urlImagem);

      if (f.exists()) {

        // Arquivo existente. Carrega (decodifica o arquivo e cria um objeto bitmap)
        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

      } else {

        // Arquivo n�o existe (ou n�o foi configurado)
        // Carrega a foto default (padr�o)
        // TODO definir o tamanho da imagem inicial

        Log.w(TAG, "obtemImagemTelaInicial() - Arquivo: " + f.getAbsolutePath() + " n�o foi encontrado.");

      }  		
      
  		return bitmap;
  		
  	}
  	
  	@Override
  	protected void onPostExecute(Bitmap result) {
  		
  		super.onPostExecute(result);
  		
  		if(progress!=null) {
  			progress.dismiss();
  		}
  		
  	}
  	
  }

}
