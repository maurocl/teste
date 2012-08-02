
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
 * Activity principal da aplicação
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

  // Estabelece a activity responsável por tirar as fotos
  private static int ACTIVITY_FOTOS = ACTIVITY_DUMMY3;

  // Estabelece a classe responsável por tirar as fotos
  private static Class<DummyActivity3> CLASS_FOTOS = DummyActivity3.class;

  // Arquivo de preferências da aplicação
  private SharedPreferences mPreferences;

  // Definição do contrante, evento e participações
  private static Contratante mContratante;

  private static Evento mEvento;

  private static Participante mParticipante;

  private static Participacao mParticipacao;

  // Definição da lista de participação no evento
  private static List<Participacao> mListaParticipacao;

  // Nº de participantes até o momento
  private int mNumParticipantes = 0;

  // Indica se o usuário se encontra loggado na aplicação
  private boolean mLogged;

  // indica a necessidade do uso de conta de administrador para acesso ao menu
  // de manutenção. É usado para testes
  // TODO incluir essa configuração nas configurações da aplicação
  //
  private boolean mAdminAccount = false;

  // Repositório - Participacao
  static RepositorioParticipacao repositorio;

  /**
   * Called when the activity is first created.
   * 
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);

    // Exibe as informações sobre o dispositivo executando a aplicação.
    exibeInformacaoSobreDispositivo();

    // Verifica se existe rede disponível no momento
    if (!AndroidUtils.isNetworkingAvailable(this)) {
      Log.w(TAG, "Não há rede disponível no momento !!!");
    }

    int funcao = 1;

    // TODO verificar qual repositório é necessário criar aqui !!!

    if (funcao == 1) {
      Log.d(TAG, "Abrindo E CRIANDO o repositório ...");
      repositorio = new RepositorioParticipacaoScript(this);

      Log.d(TAG, "Localização da base de dados: " + repositorio.getDbPath());

      File f = new File(repositorio.getDbPath());

      if (f.exists() && f.canRead()) {

        Log.d(TAG, "Tamanho do arquivo: " + f.length());

      }

    }
    else {
      Log.d(TAG, "Abrindo o repositório ...");
      repositorio = new RepositorioParticipacao(this);
    }

    if (savedInstanceState != null) {
      // obtém as informações salvas (se existirem)
    }

    Log.i(TAG, "*** onCreate() ***");

    int orientacaoAtual = this.getResources().getConfiguration().orientation;

    // exibe informações sobre a configuração atual da tela
    if (orientacaoAtual == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
      Log.d(TAG, "onCreate() - Orientação atual da tela: " + orientacaoAtual + "(PORTRAIT)");
    } else if (orientacaoAtual == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
      Log.d(TAG, "onCreate() - Orientação atual da tela: " + orientacaoAtual + "(LANDSCAPE)");
    } else {
      Log.d(TAG, "onCreate() - Orientação atual da tela: " + orientacaoAtual + "(não suportado)");
    }

    // prepara o ambiente para execução da aplicação
    boolean resultado = preparaAmbiente();

    if (resultado == true) {
      Log.d(TAG, "onCreate() - ambiente preparado para execução do software");
    } else {
      Log.d(TAG, "onCreate() - ambiente não está preparado para execução do software");
    }

    // Lê a configuração das preferências do sistema
    if (mPreferences == null) {
      lerConfiguracoes("preferencias");
    }

    // lista de participações
    mListaParticipacao = new ArrayList<Participacao>();

    // Verifica se existem algum bitmap configurado para exibição na tela inicial
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
      // trata o botão Participante (ação do usuário para participar no evento)
      // ----------------------------
      btnParticipante.setOnClickListener(new OnClickListener() {

        public void onClick(View v) {

          // launchActivity(FotoEventoActivity.this, ParticipanteActivity.class,
          // null, ACTIVITY_PARTICIPANTE);

        }

      });

    }

    // ----------------------------
    // trata o botão Participar
    // ----------------------------

    btn.setText("Participar");

    btn.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        boolean condicoesSatisfeitas = isCondicoesIniciaisSatisfeitas();

        if (condicoesSatisfeitas) {
          // condições iniciais foram satisfeitas

          Bundle bundle = new Bundle();

          bundle.putSerializable(CONTRATANTE, mContratante);
          bundle.putSerializable(EVENTO, mEvento);
          bundle.putSerializable(PARTICIPANTE, mParticipante);
          bundle.putSerializable(PARTICIPACAO, mParticipacao);

          // launchActivity(FotoEventoActivity.this, DummyActivity3.class,
          // bundle, ACTIVITY_DUMMY3);
          // launchActivity(getBaseContext(), DummyActivity3.class, bundle,
          // ACTIVITY_FOTOS);

          // lança uma activity implementada na classe CLASS_FOTOS, com código
          // de retorno ACTIVITY_FOTOS,
          // passando como parâmetros as informações sobre o contratante, evento
          // e participante.
          launchActivity(getBaseContext(), CLASS_FOTOS, bundle, ACTIVITY_FOTOS);

        } else {
          // TODO mensagem abaixo não está muito clara
          Toast.makeText(FotoEventoActivity.this, "Falta configuração !!!", Toast.LENGTH_LONG).show();
          Log.w(TAG, "mParticipante ou mParticipacao ou mContratante ou mEvento é null");
        }

      }

    });

    // -----------------------------------
    // Tratamento do evento do botão Sair
    // -----------------------------------
    btnSair.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        // executa o diálogo verificando se o usuário deseja realmente sair da
        // aplicação.
        dialogoDesejaSairDaAplicacao();

      }

    });

  }

  /**
   * Exibe informações sobre o dispositivo executando a aplicação.
   * 
   */
  private void exibeInformacaoSobreDispositivo() {

    Log.d(TAG,"Informações sobre o dispositivo executando a aplicação: ");
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
      Log.w(TAG, "Repositório fechado com sucesso");

    } else {

      Log.w(TAG, "Repositório está vazio");

    }

    super.onDestroy();
    Log.d(TAG, "*** onDestroy() ***");
    
  }

  /**
   * Criação dos Menus
   * 
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // cria o item de menu Manutenção
    criaMenuManutencao(menu);

    return super.onCreateOptionsMenu(menu);
  }

  /**
   * Cria o Menu Manutencao.
   * 
   * @param menu
   *          Instância da classe Menu
   */
  private void criaMenuManutencao(Menu menu) {

    // ----------------------------------------------
    // Menu: Manutenção
    // ---------------------------------------------
    MenuItem manutencao = menu.add(0, 3, 0, "Manutenção");
    manutencao.setIcon(R.drawable.ic_launcher);

    manutencao.setOnMenuItemClickListener(new OnMenuItemClickListener() {

      public boolean onMenuItemClick(MenuItem item) {

        processaClickItemMenuManutencao();

        return false;

      }

    });

  }

  /**
   * Lança uma Activity passando o contexto de execução, o nome da classe que
   * será executada, parâmetros extras (se houverem) e o requestCode, isto é, o
   * código de requisição que é usado para identificar o retorno da activity.
   * 
   * @param ctx
   *          Contexto da aplicação
   * 
   * @param cls
   *          Classe que será executada
   * 
   * @param params
   *          Parâmetros extras (se for necessário)
   * 
   * @param requestCode
   *          Código da solicitação (da identificação da Activity)
   * 
   * 
   */
  private void launchActivity(Context ctx, Class<?> cls, Bundle params, int requestCode) {

    Intent intent = new Intent(ctx, cls);

    if (params != null) {
      intent.putExtras(params);
    }

    Log.i(TAG, "launchActivity() - Lançando a Activity code: " + getActivityName(requestCode) + "(" + requestCode + ")");

    // Inicia a activity e precisa saber o valor de retorno
    startActivityForResult(intent, requestCode);

  }

  /**
   * Executa a ação do click no menu item Manutenção
   * 
   */
  private void processaClickItemMenuManutencao() {

    if (mAdminAccount) {

      // verifica o login do usuário antes de entrar no menu de manutenção

      if (mLogged) {
        // usuário está logado no sistema com senha de administrador

        // lança a activity ("tela") de Manutenção
        launchActivityManutencao();

      } else {
        // usuário não está logado no sistema
        // usuário deverá se loggar para conseguir fazer as
        // manutenções no sistema

        // lança a activity ("tela") de Login
        launchActivityLogin();

        //
        // TODO se o resultado da execução do login for válido
        // então devemos chamar automaticamente a tela de manutenção
        //
        // launchActivityManutencao();
        //
      }

    } else {

      // não verifica o login do usuário antes de entrar no menu de manutenção
      mLogged = true;

      // lança a activity ("tela") de Manutenção
      launchActivityManutencao();

    }

  }

  /**
   * Lança a Activity responsável pela manutenção da aplicação.<br>
   * 
   */
  private void launchActivityManutencao() {

    // cria uma "mensagem" (intent)
    Intent intent = new Intent(this, ManutencaoActivity.class);

    // passa os parâmetros para tela de manutenção
    intent.putExtra(CONTRATANTE, mContratante);
    intent.putExtra(EVENTO, mEvento);
    intent.putExtra(LISTA, (ArrayList<Participacao>) mListaParticipacao);

    // inicia a activity
    startActivityForResult(intent, ACTIVITY_MANUTENCAO);

  }

  /**
   * Lança a tela (activity) responsável pelo autenticação na aplicação
   * 
   */
  private void launchActivityLogin() {

    launchActivity(this, LoginActivity.class, null, ACTIVITY_LOGIN);

  }

  /**
   * Método de callback chamado após execução de uma Activity.<br>
   * 
   * @param requestCode
   *          código da requisição (da execução da activity)
   * 
   * @param resultCode
   *          resultado da execução da activity
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

      Log.w(TAG, "onActivityResult() - Erro ... requestCode: " + requestCode + " não pode ser processado");

    }

  }

  /**
   * Processa o resultado da execução da Activity de Manutenção.
   * 
   * @param resultCode
   *          Resultado da execução da activity
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
      Log.d(TAG, "resultActivityManutencao() - Erro ... resultCode não conhecido: " + resultCode);
    }

  }

  /**
   * Processa o resultado da execução da Activity de Login.
   * 
   * @param resultCode
   *          Resultado da execução da Activity
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

            // o parâmetro foi encontrado
            if ("OK".equals(resultado)) {

              mLogged = true;
              Log.d(TAG, "activityLoginResult() - Login efetuado com sucesso");

              // Lança automaticamente a atividade de manutenção
              launchActivityManutencao();

            } else {

              mLogged = false;
              Log.d(TAG, "activityLoginResult() - Falha no Login");

            }
          } else {
            // o parâmetro não foi encontrado
          }
        } else {
          // params is null
        }

      } else {
        // data is null
      }

    } else if (resultCode == RESULT_CANCELED) {

      Log.d(TAG, "activityLoginResult() - Login cancelado pelo usuário - RESULT_CANCELED");

    } else {
      Log.d(TAG, "activityLoginResult() - Erro ...");
    }

  }

  /**
   * Processa o resultado da execução da activity responsável por executar um
   * ciclo completo da aplicação que é:<br>
   * <ul>
   * <li>Obter informações sobre o participante do evento (nome, email,
   * telefone, etc);
   * <li>Obter informações sobre o tipo da foto (polaroid/cabine) e o efeito de
   * cores aplicado (cores/p&b)
   * <li>Obter a foto no formato e efeito desejado mais a inclusão da moldura.
   * </ul>
   * 
   * @param resultCode
   *          Resultado da execução da activity
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

      // Activity não retornou dados
      Log.w(TAG, "resultActivityDummy3() - Activity Dummy3 não retornou dados");

    } else {

      // exibe os dados retornados pela activity
      Log.i(TAG, "resultActivityDummy3 - data=" + data);

      // Obtem o resultado da execução da activity
      resultado = data.getStringExtra("br.com.mltech.result");

      mParticipante = (Participante) data.getSerializableExtra(Constantes.PARTICIPANTE);
      mParticipacao = (Participacao) data.getSerializableExtra(Constantes.PARTICIPACAO);

      int numFotosPolaroid = data.getIntExtra("br.com.mltech.numFotosPolaroid", 0);
      int numFotosCabine = data.getIntExtra("br.com.mltech.numFotosCabine", 0);

      // exibe o resultado da execução da activity
      Log.i(TAG, "resultActivityDummy3() - resultado=" + resultado);
      Log.i(TAG, "resultActivityDummy3() - numFotosPolaroid=" + numFotosPolaroid);
      Log.i(TAG, "resultActivityDummy3() - numFotosCabine=" + numFotosCabine);

    }

    if (resultCode == RESULT_OK) {

      Log.i(TAG, "resultActivityDummy3() - executada com sucesso");

      // atualiza a lista de participantes
      updateListaParticipacao();

    } else {

      // operação cancelada
      Log.w(TAG, "resultActivityDummy3() - Operação ocorreu com erros");

    }

  }

  /**
   * Processa o resultado da activity camera;
   * 
   * @param resultCode
   *          Resultado da execução da Activity
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

      Log.d(TAG, "resultActivityCamera() - activityprocessada com sucesso: RESULT_OK");

      if (data != null) {

        Bundle params = data.getExtras();

        if (params != null) {

          resultado = params.getString("br.com.mltech.usuarioValidado");

          if (resultado != null) {
            // o parâmetro foi encontrado
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
      Log.d(TAG, "resultActivityCamera() - valor retornado " + resultCode + " é desconhecido.");
    }

  }

  /**
   * Verifica se as configurações iniciais estão satisfeitas para execução de
   * uma participação no evento.<br>
   * <p>
   * Condições:<br>
   * <br>
   * contratante - ter um contratante configurado<br>
   * evento - ter um evento configurado<br>
   * 
   * @return true se as condições forem satisfeitas e false, caso contrário
   */
  private boolean isCondicoesIniciaisSatisfeitas() {

    boolean b = true;

    // verifica se existe um contratante
    if (mContratante == null) {

      Log.w(TAG, "isCondicoesIniciaisSatisfeitas() - Contratante não foi configurado");
      Toast.makeText(this, "Contratante não foi configurado", Toast.LENGTH_LONG).show();

      b = false;

    } else {

      if (mContratante.getNome().equals("")) {
        Log.w(TAG, "isCondicoesIniciaisSatisfeitas() - O nome do contratante está vazio");
        Toast.makeText(this, "Contratante não foi configurado", Toast.LENGTH_LONG).show();
        b = false;
      }

    }

    // verifica se existe um evento cadastrado
    if (mEvento == null) {
      Log.w(TAG, "isCondicoesIniciaisSatisfeitas() - Evento não foi configurado");
      Toast.makeText(this, "Evento não foi configurado", Toast.LENGTH_LONG).show();
      b = false;
    } else {
      if (mEvento.getNome().equals("")) {
        Log.w(TAG, "isCondicoesIniciaisSatisfeitas() - Nome do evento não foi configurado.");
        Toast.makeText(this, "Evento não foi configurado", Toast.LENGTH_LONG).show();
        b = false;
      }
    }

    // verifica se as bordas das fotos já foram disponibilizadas ao
    // evento

    if (mEvento != null) {
      Log.v(TAG, "isCondicoesIniciaisSatisfeitas() - getBordaCabine: " + mEvento.getBordaCabine());
      Log.v(TAG, "isCondicoesIniciaisSatisfeitas() - getBordaPolaroid: " + mEvento.getBordaPolaroid());
    } else {
      Log.w(TAG, "isCondicoesIniciaisSatisfeitas() - Evento é nulo");
    }

    if ((mEvento != null) && ((mEvento.getBordaCabine() == null) || (mEvento.getBordaPolaroid() == null))) {
      // TODO alterar essa condição
      Log.w(TAG, "isCondicoesIniciaisSatisfeitas() - Bordas não foram configuradas");
      Toast.makeText(this, "Bordas não foram configuradas", Toast.LENGTH_LONG).show();
      b = false;
    }

    return b;

  }

  /**
   * <p>
   * Prepara o sistema de arquivos para execução da aplicação. ambiente para
   * conter as molduras, a tela inicial e para gravação das fotos. <br>
   * 
   * <pre>
   *   +--+ /fotoevento
   *      |--- /fotos
   *      |--- /molduras
   *      |--- /telainicial
   * 
   * </pre>
   * 
   * O método retorna false caso o SDCard não esteja montado.<br>
   * 
   * @return true caso o ambiente seja criado com sucesso ou false caso haja
   *         algum erro.
   * 
   */
  private boolean preparaAmbiente() {

    if (!CameraTools.isExternalStorageMounted()) {
      Log.i(TAG, "preparaAmbiente() - SDCARD não está montado");

      // TODO aqui seria melhor exibir uma caixa de diálogo
      Toast.makeText(this, "SDCARD não está montado !!!", Toast.LENGTH_LONG).show();

      return false;

    }

    Log.d(TAG, "preparaAmbiente() - SDCARD está montado");

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
   * Lê as configurações gravadas em um arquivo de preferência e inicializa os
   * atributos contratante e evento.<br>
   * 
   * @param name
   *          Identificador do sistema de preferências compartilhadas
   * 
   */
  private void lerConfiguracoes(String name) {

    Log.d(TAG, "lerConfiguracoes() - Lendo as preferências compartilhadas: " + name);

    mPreferences = getSharedPreferences(name, MODE_PRIVATE);

    if (mPreferences == null) {
      Log.w(TAG, "lerConfiguracoes() - mPreferences é nulo");
      return;
    }

    // nº de itens de configuração lidos
    int numItensDeConfiguracao = 0;

    if (mPreferences.getAll() != null) {
      numItensDeConfiguracao = mPreferences.getAll().size();
    }

    // Exibe o nº de entradas no arquivo de preferências
    Log.d(TAG, "lerConfiguracoes() - nº de entradas do arquivo de preferências: " + numItensDeConfiguracao);

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
   * Insere um novo participante na lista de participação do evento.
   * 
   */
  private void updateListaParticipacao() {

    // TODO como persistir os dados da lista de participação entre uma execução
    // e outra ???
    // quando a lista deverá ser reiniciada ?
    // devemos reinicar a lista quando dermos um 'reset' na aplicação ?

    Log.v(TAG, "--------------------------------");
    Log.v(TAG, " updateListaParticipação        ");
    Log.v(TAG, "--------------------------------");

    if (mParticipacao == null) {
      Log.w(TAG, "updateListaParticipação() - participacao é nula, portanto não foi adicionada");
      return;
    }

    if (mListaParticipacao == null) {
      Log.w(TAG, "updateListaParticipação() - Lista de participantes é nula. Participante não foi adicionado");
      return;
    }

    // adiciona uma nova participação
    //mListaParticipacao.add(mParticipacao);

    // Inclui uma nova participação

    if (repositorio != null) {

      long myId = repositorio.inserir(mParticipacao);

      if (myId != -1) {

        Log.d(TAG, "Participação inserida com sucesso - ID: " + myId);

        mNumParticipantes++;

        // incrementa o contador de participantes do evento
        // TODO o nº de participante poderia aparecer na tela principal

        Log.v(TAG, "updateListaParticipação() - nº de participantes até o momento: " + mNumParticipantes);

      }
      else {
        Log.w(TAG, "Falha na inserção da Participação");

      }

      // atualiza o identificador da participação
      mParticipacao.setId(myId);

    } else {

      Log.w(TAG, "Repositório está vazio");

    }

    Toast.makeText(this, mParticipacao.getParticipante().getNome() + ", obrigado pela participação !", Toast.LENGTH_SHORT).show();

  }

  /**
   * Obtem um bitmap que será exibido na tela inicial da aplicação. Caso nenhum
   * bitmap seja configurado será usado um bitmap padrão.
   * 
   * @return uma instância do objeto Bitmap contendo a imagem da tela inicial se
   *         configurada ou null caso haja algum erro.
   */
  private Bitmap obtemImagemTelaInicial() {

    Bitmap bitmap = null;

    SharedPreferences preferences = getSharedPreferences(PREF_EMAIL, MODE_PRIVATE);

    if (preferences == null) {
      Log.w(TAG, "obtemImagemTelaInicial() - Não foi possível abrir o arquivo de preferências: " + PREF_EMAIL);
      return null;
    }

    String urlImagem = preferences.getString("preferencias_url_imagem", null);

    preferences = null;

    if (urlImagem == null) {
      Log.w(TAG,
          "obtemImagemTelaInicial() - Não foi possível encontrar o parâmetro de configuração 'preferencias_url_imagem'. Usando a tela padrão");
      return null;
    }

    File f = new File(urlImagem);

    if (f.exists()) {

      // Arquivo existente. Carrega (decodifica o arquivo e cria um objeto bitmap)
      bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

    } else {

      // Arquivo não existe (ou não foi configurado)
      // Carrega a foto default (padrão)
      // TODO definir o tamanho da imagem inicial

      Log.w(TAG, "obtemImagemTelaInicial() - Arquivo: " + f.getAbsolutePath() + " não foi encontrado.");

    }

    // retorna o bitmap
    return bitmap;

  }

  /**
   * Exibe uma caixa de diálogo e a mensagem perguntando se o usuário deseja
   * sair da aplicação.<br>
   * 
   * Caso o usuário pressione "Sim" a aplicação será encerrada. Caso contrário a
   * caixa de diálogo irá desaparecer e a aplicação continua´ra em execução.
   * 
   */
  private void dialogoDesejaSairDaAplicacao() {

    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setMessage("Você tem certeza que deseja sair ?").setCancelable(false)
        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {

          public void onClick(DialogInterface dialog, int id) {

            Log.i(TAG, "dialogoDesejaSairDaAplicacao() - Finalizando a aplicação.");
            FotoEventoActivity.this.finish();
          }

        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {

          public void onClick(DialogInterface dialog, int id) {

            Log.i(TAG, "dialogoDesejaSairDaAplicacao() - o usuário cancelou o pedido de saída.");
            dialog.cancel();
          }

        });

    AlertDialog alert = builder.create();
    alert.show();

  }

  /**
   * Obtém o nome associado a activity dado de requestCode.
   * 
   * @param requestCode
   *          inteiro originalmente fornecido na chamada ao método
   *          startActivityForResult() permitindo que seja identificado quem
   *          recebeu o resultado.
   * 
   * @return o nome associado ao código usado na execução da Activity.
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
        nome = "Activity não encontrada.";
        break;

    }

    return nome;

  }

}
