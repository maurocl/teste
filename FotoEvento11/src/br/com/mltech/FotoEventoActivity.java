package br.com.mltech;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
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

  // indica o uso do DEBUG
  private static final int DEBUG = 1;

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

    ct = new CameraTools();

    // prepara o ambiente para execu��o da aplica��o
    preparaAmbiente();

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
    // trata o bot�o
    // ----------------------------

    btn.setText("*** Participar ***");

    btn.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        Bundle bundle = new Bundle();

        bundle.putSerializable("br.com.mltech.contratante", mContratante);
        bundle.putSerializable("br.com.mltech.evento", mEvento);
        bundle.putSerializable("br.com.mltech.participante", mParticipante);
        bundle.putSerializable("br.com.mltech.participacao", mParticipacao);

        boolean b = isCondicoesIniciaisSatisfeitas();

        if (b) {
          launchActivity(FotoEventoActivity.this, DummyActivity3.class, bundle, ACTIVITY_DUMMY3);
        } else {
          Log.w(TAG, "mParticipante ou mParticipacao ou mContratante ou mEvento � null");
        }

      }

    });

    // -----------------------------------
    // Tratamento do evento do bot�o Sair
    // -----------------------------------
    btnSair.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {
        // TODO aqui deve ter um ALERT solicitando sim/n�o

        /*
         * AlertDialog.Builder builder = new
         * AlertDialog.Builder(FotoEventoActivity.this);
         * builder.setMessage("Sai da aplica��o ?").setNeutralButton("Fechar",
         * null); AlertDialog alert = builder.create(); alert.show();
         */

        finish();
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
   * criaMenuParticipante(Menu menu)
   * 
   * @param menu
   */
  private void criaMenuParticipante(Menu menu) {
    // ---------------------------------------------
    // Menu: Cadastro de Participantes
    // ----------------------------------------------
    MenuItem participante = menu.add(0, 0, 0, "Participante");
    participante.setIcon(R.drawable.ic_launcher);

    participante.setOnMenuItemClickListener(new OnMenuItemClickListener() {

      public boolean onMenuItemClick(MenuItem item) {
        launchActivity(FotoEventoActivity.this, ParticipanteActivity.class, null, ACTIVITY_PARTICIPANTE);
        return false;
      }
    }

    );

  }

  /**
   * criaMenuCamera(Menu menu)
   * 
   * @param menu
   */
  private void criaMenuCamera(Menu menu) {

    MenuItem participante = menu.add(0, 8, 0, "Camera");
    participante.setIcon(R.drawable.ic_launcher);

    participante.setOnMenuItemClickListener(new OnMenuItemClickListener() {

      public boolean onMenuItemClick(MenuItem item) {

        launchActivityCamera();

        return false;

      }
    });

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
   * launchActivity(Context ctx, Class<?> cls, Bundle params, int requestCode)
   * 
   * Cria e lan�a uma Intent
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

    Log.i(TAG, "Lan�ando a Activity code: " + requestCode);

    // Inicia a Activity
    startActivityForResult(intent, requestCode);

  }

  /**
   * onActivityResult(int requestCode, int resultCode, Intent data)
   * 
   * Called when an activity you launched exits
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

      Log.w(TAG, "Erro ... requestCode: " + requestCode + " n�o pode ser processado");
    }

  }

  /**
   * activityManutencaoResult(int resultCode, Intent data)
   * 
   * @param resultCode
   * @param data
   */
  private void resultActivityManutencao(int resultCode, Intent data) {

    Log.d(TAG, "processando ACTIVITY MANUTENCAO");

    if (resultCode == RESULT_OK) {

      Log.d(TAG, "processando ACTIVITY MANUTENCAO - RESULT_OK");

    } else if (resultCode == RESULT_CANCELED) {
      Log.d(TAG, "processando ACTIVITY MANUTENCAO - RESULT_CANCELED");

    } else {
      Log.d(TAG, "Erro ... resultCode n�o conhecido: " + resultCode);
    }

  }

  /**
   * activityParticipanteResult(int resultCode, Intent data)
   * 
   * @param resultCode
   *          Resultado da execu��o da Intent
   * @param data
   * 
   *          � esperado que a Activity participante retorne ...
   * 
   */
  private void resultActivityParticipante(int resultCode, Intent data) {

    Log.d(TAG, "==> processando ACTIVITY_PARTICIPANTE");

    if (resultCode == RESULT_CANCELED) {
      Log.d(TAG, "resultCode=RESULT_CANCELED - Participante cancelou sua participa��o");
      return;
    } else if (resultCode != RESULT_OK) {
      Log.w(TAG, "resultCode n�o conhecido: " + resultCode);
      return;
    }

    if (data == null) {
      // caso a Intent n�o retorne nada houve algum problema
      Log.w(TAG, "A intent n�o retornou os dados esperados");
      return;
    }

    mParticipante = (Participante) data.getSerializableExtra("br.com.mltech.participante");
    mParticipacao = (Participacao) data.getSerializableExtra("br.com.mltech.participacao");

    Log.d(TAG, "mParticipante=" + mParticipante);
    Log.d(TAG, "mParticipacao=" + mParticipacao);

  }

  /**
   * activityLoginResult(int resultCode, Intent data)
   * 
   * @param resultCode
   * @param data
   * 
   */
  private void resultActivityLogin(int resultCode, Intent data) {

    String resultado;

    mLogged = false;

    Log.d(TAG, "processActivityLogin()");

    if (resultCode == RESULT_OK) {

      if (data != null) {

        Bundle params = data.getExtras();

        if (params != null) {

          resultado = params.getString("br.com.mltech.usuarioValidado");

          if (resultado != null) {

            // o par�metro foi encontrado
            if ("OK".equals(resultado)) {

              mLogged = true;
              Log.d(TAG, "Login efetuado com sucesso");

              // Lan�a automaticamente a atividade de manuten��o
              launchActivityManutencao();

            } else {

              mLogged = false;
              Log.d(TAG, "Falha no Login");

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

      Log.d(TAG, "Login cancelado pelo usu�rio - RESULT_CANCELED");

    } else {
      Log.d(TAG, "Erro ...");
    }

  }

  /**
   * activityCameraResult(int resultCode, Intent data)
   * 
   * @param resultCode
   * @param data
   * 
   */
  private void resultActivityCamera(int resultCode, Intent data) {

    String resultado;
    String filename;

    byte[] dados;

    Log.d(TAG, "processando ACTIVITY_CAMERA");

    if (resultCode == RESULT_OK) {

      Log.d(TAG, "ActivityCamera processada com sucesso: RESULT_OK");

      if (data != null) {

        Bundle params = data.getExtras();

        if (params != null) {

          resultado = params.getString("br.com.mltech.usuarioValidado");

          if (resultado != null) {
            // o par�metro foi encontrado
          }

          dados = params.getByteArray("br.com.mltech.dados");
          filename = params.getString("br.com.mltech.image.filename");

          Log.d(TAG, "Filename=" + filename);
          if (dados != null) {
            Log.d(TAG, "Dados=" + dados.length);
          }

        }
      }

    } else if (resultCode == RESULT_CANCELED) {
      Log.d(TAG, "ActivityCamera retornou: RESULT_CANCELED");
    } else {
      Log.d(TAG, "Erro ...");
    }

  }

  /**
   * resultActivityDummy3(int resultCode, Intent data)
   * 
   * @param resultCode
   * @param data
   * 
   */
  private void resultActivityDummy3(int resultCode, Intent data) {

    String result = null;

    Log.d(TAG, "==> resultActivityDummy3 - Executando o resultado do processamento da ACTIVITY DUMMY3");

    Log.d(TAG, "==> resultCode=" + resultCode);

    Intent i = getIntent();

    Bundle ss = i.getBundleExtra("br.com.mltech.result");
    if (ss != null) {

      Log.w(TAG, "Bundle ss possui informa��es");

      Set<String> chaves = ss.keySet();

      for (String chave : chaves) {
        Log.v(TAG, "    chave=" + chave);
      }

    } else {

      Log.w(TAG, "Bundle ss est� vazio - nenhum resultado retornou");

    }

    if (data == null) {

      Log.w(TAG, "data is null");

    } else {

      Log.i(TAG, "data possui informa��es");
      Log.i(TAG,"data="+data);
      result = data.getStringExtra("br.com.mltech.result");
      Log.d(TAG, "result=" + result);

    }

    if (result != null) {

      if (resultCode == RESULT_OK) {
        Log.i(TAG, "resultActivityDummy3 executada com sucesso");

        // atualiza a lista de participantes
        updateListaParticipacao();

      } else {

        // opera��o cancelada
        Log.w(TAG, "resultActivityDummy3() - Opera��o ocorreu com erros");

      }

    } else {
      Log.w(TAG, "Result == null - problemas !!!");
    }

  }

  /**
   * carregaImagem(File f)
   * 
   * @param f
   */
  public Bitmap carregaImagem(File f) {

    Bitmap bm = null;

    if (f != null) {

      bm = BitmapFactory.decodeFile(f.getName());

      if (bm != null) {

        bm = Bitmap.createScaledBitmap(bm, 100, 100, true);
        ImageView button = (ImageView) findViewById(R.id.imageView1);

        button.setImageBitmap(bm);

      }

    }

    return bm;

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

    Log.d(TAG, "Lendo as prefer�ncias compartilhadas: " + name);

    mPreferences = getSharedPreferences(name, MODE_PRIVATE);

    // L� todas as entradas
    Map<?, ?> chaves = mPreferences.getAll();

    // Obtem o conjunto de todas as entradas
    Set<?> set = chaves.entrySet();

    // Exibe o n� de entradas no arquivo de prefer�ncias
    Log.d(TAG, "N� de entradas do arquivo de prefer�ncias=" + set.size());

    Iterator<?> i = set.iterator();

    int j = 0;

    // Exibe os elementos
    while (i.hasNext()) {

      j++;

      Entry<?, ?> me = (Entry<?, ?>) i.next();

      if (DEBUG == 1) {
        Log.d(TAG, j + ") " + me.getKey() + "=" + me.getValue());
      }

    }

    // Contratante
    if (mContratante == null) {

      //
      mContratante = new Contratante();

    }

    mContratante.setNome((String) chaves.get("contratante_nome"));
    mContratante.setEmail((String) chaves.get("contratante_email"));
    mContratante.setTelefone((String) chaves.get("contratante_telefone"));

    // Evento
    if (mEvento == null) {

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

    /*
     * 
     * boolean b1, b2;
     * 
     * b1 = ((String) chaves.get("evento_envia_facebook")).equals("true") ? true
     * : false; b2 = ((String)
     * chaves.get("evento_envia_twitter")).equals("true") ? true : false;
     * 
     * mEvento.setEnviaFacebook(b1); mEvento.setEnviaTwitter(b2);
     */

    String[] paramEventos = new String[5];

    Log.d(TAG,"*** passo 10 ***");
    
    paramEventos[0] = (String) chaves.get("evento_param1");
    paramEventos[1] = (String) chaves.get("evento_param2");
    paramEventos[2] = (String) chaves.get("evento_param3");
    paramEventos[3] = (String) chaves.get("evento_param4");
    paramEventos[4] = (String) chaves.get("evento_param5");

    Parametros parametros = new Parametros(paramEventos);

    Log.d(TAG,"*** passo 20 ***");
    
    mEvento.setParametros(parametros);
    
    Log.d(TAG,"*** passo 30 ***");

  }

  /**
   * startActivityParticipante(View v)
   * 
   * @param v
   *          View
   */
  public void startActivityParticipante(View v) {

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
      Log.w(TAG, "Contratante n�o foi configurado");
      b = false;
    }

    // verifica se existe um evento cadastrado
    if (mEvento == null) {
      Log.w(TAG, "Evento n�o foi configurado");
      b = false;
    }

    // verifica se as bordas das fotos j� foram disponibilizadas ao
    // evento
    if (mEvento != null && ((mEvento.getBordaCabine() == null) || (mEvento.getBordaPolaroid() == null))) {
      // TODO alterar essa condi��o
      Log.w(TAG, "Bordas n�o foram configuradas");
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

    Log.v(TAG, "--> updateListaParticipa��o <---");

    if (mListaParticipacao != null) {

      // adiciona uma nova participa��o
      mListaParticipacao.add(mParticipacao);

      // incrementa o contador de participantes do evento
      // TODO o n� de participante poderia aparecer na tela principal
      mNumParticipantes++;
      Log.v(TAG, "updateListaParticipa��o: N� de participantes at� o momento: " + mNumParticipantes);

    } else {

      Log.w(TAG, "Lista de participantes � null. Participante n�o foi adicionado");

    }

  }

  /**
   * 
   */
  public void preparaAmbiente() {

    if (ct != null) {

      if (ct.isExternalStorageMounted()) {

        Log.i(TAG, "SDCARD est� montado");

        // xxx(publicDirectory);

        // File f = ct.getStorageDir("fotoevento2");
        File f = null;

        f = ct.getDir2("fotoevento/fotos");
        ct.ShowFileDetails(f, "fotoevento/fotos");

        f = ct.getDir2("fotoevento/molduras");
        ct.ShowFileDetails(f, "fotoevento/molduras");

        f = ct.getDir2("fotoevento/telainicial");
        ct.ShowFileDetails(f, "fotoevento/telainicial");

      } else {

        Log.i(TAG, "SDCARD n�o est� montado");

      }

    }

  }

}
