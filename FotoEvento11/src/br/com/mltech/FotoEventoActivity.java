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
 * Activity principal da aplicação
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

  // Definição do contrante, evento e participações
  private Contratante mContratante;
  private Evento mEvento;
  private Participante mParticipante;
  private Participacao mParticipacao;

  // Definição da lista de participação no evento
  private List<Participacao> mListaParticipacao;

  // Nº de participantes até o momento
  private int mNumParticipantes = 0;

  // Indica se o usuário se encontra loggado a aplicação
  private boolean mLogged;

  // indica a necessidade do uso de conta de administrador para acesso ao menu
  // de manutenção. É usado para testes
  // TODO incluir essa configuração nas configurações da aplicação
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
      // obtém as informações salvas (se existirem)
    }

    Log.i(TAG, "*** onCreate() ***");

    ct = new CameraTools();

    // prepara o ambiente para execução da aplicação
    boolean b = preparaAmbiente();

    // Lê a configuração das preferências do sistema
    if (mPreferences == null) {
      lerConfiguracoes("preferencias");
    }

    // lista de participações
    mListaParticipacao = new ArrayList<Participacao>();

    if (DEBUG > 0) {
      mContratante = new Contratante("Joao", "maurocl@mltech.com.br", "9999999");

      mParticipante = new Participante("Mauro Cesar Lopes", "maurocl@terra.com.br", "(19) 8143-8978");

      // 1=POLAROID, 11=COR
      mParticipacao = new Participacao(mParticipante, 1, 11, null);

    }

    // Verifica se existem algum bitmap configurado para exibição na tela
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
      // trata o botão Participante (ação do usuário para participar no evento)
      // ----------------------------
      btnParticipante.setOnClickListener(new OnClickListener() {

        public void onClick(View v) {

          launchActivity(FotoEventoActivity.this, ParticipanteActivity.class, null, ACTIVITY_PARTICIPANTE);

        }

      });

    }

    // ----------------------------
    // trata o botão Participar
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
   * Criação dos Menus
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
    // Menu: Manutenção
    // ---------------------------------------------
    MenuItem manutencao = menu.add(0, 3, 0, "Manutenção");
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
   * Lança uma Activity passando o contexto de execução, o nome da classe que
   * será executada, parâmetros e o requestCode, isto é, o código de requisição
   * de execução da Activity.
   * 
   * @param ctx
   *          Contexto
   * @param cls
   *          Classe que será executada
   * @param params
   *          Parâmetros enviados
   * @param requestCode
   *          Código da solicitação
   * 
   * 
   */
  private void launchActivity(Context ctx, Class<?> cls, Bundle params, int requestCode) {

    // Intent intent = new Intent(FotoEventoActivity.this,CameraActivity.class);
    Intent intent = new Intent(ctx, cls);

    if (params != null) {
      intent.putExtras(params);
    }

    Log.i(TAG, "launchActivity() - Lançando a Activity code: " + requestCode);

    // Inicia a Activity
    startActivityForResult(intent, requestCode);

  }

  /**
   * 
   */
  private void launchActivityocessaClickItemMenuManutencao() {

    if (mAdminAccount) {

      // verifica o login do usuário antes de entrar no menu de manutenção

      if (mLogged) {
        // usuário está logado no sistema com senha de administrador

        launchActivityManutencao();

      } else {
        // usuário não está logado no sistema
        // usuário deverá se loggar para conseguir fazer as
        // manutenções no sistema

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

    // TODO esse método precisa verificação
    int escolha = 2;

    Bundle params = null;

    if (escolha == 1) {

      params = new Bundle();
      params.putString("msg", "Olá");

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

      Log.w(TAG, "onActivityResult() - Erro ... requestCode: " + requestCode + " não pode ser processado");
    }

  }

  /**
   * activityManutencaoResult(int resultCode, Intent data)
   * 
   * Resultado da execução da Activity de Manutenção
   * 
   * @param resultCode
   *          Resultado da execução da activity
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
   * activityParticipanteResult(int resultCode, Intent data)
   * 
   * @param resultCode
   *          Resultado da execução da Activity
   * @param data
   *          É esperado que a Activity participante retorne as informações
   *          sobre o participante e sua participação.
   * 
   */
  private void resultActivityParticipante(int resultCode, Intent data) {

    Log.d(TAG, "resultActivityParticipante() ==> processando ACTIVITY_PARTICIPANTE");

    if (resultCode == RESULT_CANCELED) {
      Log.d(TAG, "resultActivityParticipante() - resultCode=RESULT_CANCELED - Participante cancelou sua participação");
      return;
    } else if (resultCode != RESULT_OK) {
      Log.w(TAG, "resultActivityParticipante() - resultCode não conhecido: " + resultCode);
      return;
    }

    if (data == null) {
      // caso a Intent não retorne nada houve algum problema
      Log.w(TAG, "resultActivityParticipante() - A intent não retornou os dados esperados");
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
   *          Resultado da execução da Activity
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
   * activityCameraResult(int resultCode, Intent data)
   * 
   * @param resultCode
   *          Resultado da execução da Activity
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
   * resultActivityDummy3(int resultCode, Intent data)
   * 
   * Processo o resultado da execução da activity
   * 
   * @param resultCode
   *          Resultado da execução da activity
   * @param data
   *          Intent recebida (se houver)
   * 
   */
  private void resultActivityDummy3(int resultCode, Intent data) {

    String result = null;

    Log.d(TAG, "==> resultActivityDummy3() - Executando o resultado do processamento da ACTIVITY DUMMY3");

    Log.d(TAG, "==> resultActivityDummy3() - resultCode=" + resultCode);

    if (data == null) {
      // Activity não retornou dados
      Log.w(TAG, "resultActivityDummy3() - Activity Dummy3 não retornou dados");

    } else {

      Log.i(TAG, "resultActivityDummy3 - data=" + data);

      // Obtem o resultado da execução da activity
      result = data.getStringExtra("br.com.mltech.result");

      Log.i(TAG, "resultActivityDummy3() - result=" + result);

    }

    // Obtém informações sobre a intent que chamou a activity
    Intent i = getIntent();

    Bundle ss = i.getExtras();

    if (ss != null) {

      Log.w(TAG, "resultActivityDummy3() - bundle ss possui as seguintes informações:");

      Set<String> chaves = ss.keySet();

      for (String chave : chaves) {
        Log.v(TAG, "    chave=" + chave);
      }

    } else {

      Log.w(TAG, "resultActivityDummy3() - bundle ss está vazio");

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
   * lerConfiguracoes(String name)
   * 
   * Lê as configurações gravadas em um arquivo de preferência. Inicia os
   * atributos mContratante e mEvento
   * 
   * @param name
   *          Identificador do sistema de preferências compartilhadas
   * 
   */
  private void lerConfiguracoes(String name) {

    Log.d(TAG, "lerConfiguracoes() - Lendo as preferências compartilhadas: " + name);

    mPreferences = getSharedPreferences(name, MODE_PRIVATE);

    // Lê todas as entradas
    Map<?, ?> chaves = mPreferences.getAll();

    // Obtem o conjunto de todas as entradas
    Set<?> set = chaves.entrySet();

    // Exibe o nº de entradas no arquivo de preferências
    Log.d(TAG, "lerConfiguracoes() - nº de entradas do arquivo de preferências=" + set.size());

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
   * Inicia a Activity Participante que obtém as informações do participante do
   * evento e suas preferências quanto ao tipo e filtro que será aplicado a
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
   * Verifica se as configurações iniciais estão satisfeitas para execução de
   * uma participação no evento.
   * 
   * contratante - ter um contratante configurado evento - ter um evento
   * configurado
   * 
   * @return true se as condições forem satisfeitas e false, caso contrário
   */
  private boolean isCondicoesIniciaisSatisfeitas() {

    /*
     * as condições necessárias para execução da activity é ter: - contratante -
     * evento - participante - participação
     */

    boolean b = true;

    // verifica se existe um contratante
    if (mContratante == null) {
      Log.w(TAG, "isCondicoesIniciaisSatisfeitas() - Contratante não foi configurado");
      Toast.makeText(this, "Contratante não foi configurado", Toast.LENGTH_LONG).show();
      b = false;
    }

    // verifica se existe um evento cadastrado
    if (mEvento == null) {
      Log.w(TAG, "isCondicoesIniciaisSatisfeitas() - Evento não foi configurado");
      Toast.makeText(this, "Evento não foi configurado", Toast.LENGTH_LONG).show();
      b = false;
    }

    // verifica se as bordas das fotos já foram disponibilizadas ao
    // evento

    if (mEvento != null) {
      Log.i(TAG, "getBordaCabine: " + mEvento.getBordaCabine());
      Log.i(TAG, "getBordaPolaroid: " + mEvento.getBordaPolaroid());
    } else {
      Log.w(TAG, "Evento é null");
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
   * updateListaParticipacao()
   * 
   * Insere um novo participante na lista de participação do evento
   * 
   */
  private void updateListaParticipacao() {

    Log.v(TAG, "--------------------------------");
    Log.v(TAG, " updateListaParticipação        ");
    Log.v(TAG, "--------------------------------");

    if (mListaParticipacao != null) {

      // adiciona uma nova participação
      mListaParticipacao.add(mParticipacao);

      // incrementa o contador de participantes do evento
      // TODO o nº de participante poderia aparecer na tela principal
      mNumParticipantes++;
      Log.v(TAG, "updateListaParticipação() - nº de participantes até o momento: " + mNumParticipantes);

    } else {

      Log.w(TAG, "updateListaParticipação() - Lista de participantes é null. Participante não foi adicionado");

    }

  }

  /**
   * preparaAmbiente()
   * 
   * Prepara o ambiente para gravação das fotos
   * 
   */
  private boolean preparaAmbiente() {

    if (ct == null) {
      Log.w(TAG, "preparaAmbiente() - CameraTools não foi instanciado");
      return false;
    }

    boolean b = false;
    if (ct.isExternalStorageMounted()) {

      Log.i(TAG, "preparaAmbiente() - SDCARD está montado");

      File f = null;

      f = ct.getDir2("fotoevento/fotos");
      ct.ShowFileDetails(f, "fotoevento/fotos");

      f = ct.getDir2("fotoevento/molduras");
      ct.ShowFileDetails(f, "fotoevento/molduras");

      f = ct.getDir2("fotoevento/telainicial");
      ct.ShowFileDetails(f, "fotoevento/telainicial");

      b = true;

    } else {

      Log.i(TAG, "preparaAmbiente() - SDCARD não está montado");

      // TODO aqui seria melhor exibir uma caixa de diálogo
      Toast.makeText(this, "SDCARD não está montado !!!", Toast.LENGTH_LONG).show();

    }

    return b;

  }

  /**
   * obtemImagemTelaInicial()
   * 
   * Obtem um bitmap que será exibido na tela inicial da aplicação. Caso
   * nenhuma bitmap seja configurado será usado um bitmap padrão.
   * 
   */
  private Bitmap obtemImagemTelaInicial() {

    Bitmap bitmap = null;

    SharedPreferences preferences = getSharedPreferences(PREF_EMAIL, MODE_PRIVATE);

    if (preferences == null) {
      Log.w(TAG, "obtemImagemTelaInicial() - Não foi possível abrir o arquivo de preferências: " + PREF_EMAIL);
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
        
        // carrega a foto default (padrão)
        // TODO definir o tamanho da imagem inicial

        Log.w(TAG, "obtemImagemTelaInicial() - Arquivo: " + f.getAbsolutePath() + " não foi encontrado.");

      }

    }

    preferences = null;

    return bitmap;

  }

  /**
   * dialogoDesejaSairDaAplicacao()
   * 
   * Exibe uma caixa de diálogo e a mensagem perguntando se o usuário deseja
   * sair da aplicação.
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

}
