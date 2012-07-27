package br.com.mltech;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import br.com.mltech.utils.FileUtils;

/**
 * ManutencaoActivity
 * 
 * Esta activity é responsável pela Manutenção do Sistema.<br>
 * 
 * É a partir dela que podemos fazer a manutenção do Contratante, Evento,
 * Configurações, Relatórios, Backup e Restore de configuração do sistema a
 * partir de um arquivo de configuração.
 * 
 * @author maurocl
 * 
 */
public class ManutencaoActivity extends Activity implements Constantes {

  private static final String TAG = "ManutecaoActivity";

  /* Identificadores das Activities */

  private static final int ACTIVITY_CONTRATANTE = 100;

  private static final int ACTIVITY_EVENTO = 101;

  private static final int ACTIVITY_PREFERENCIAS = 1002;

  private static final int ACTIVITY_RELATORIOS = 1003;

  private static SharedPreferences mPreferences;

  private static SharedPreferences mPreferencesEmail;

  private static Contratante mContratante;

  private static Evento mEvento;

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
      Log.d(TAG, "onCreate() - Erro na leitura do arquivo de preferências.");
    }

    Button btnContratante = (Button) findViewById(R.id.btnContratante);
    Button btnEvento = (Button) findViewById(R.id.btnEvento);
    Button btnPreferencias = (Button) findViewById(R.id.btnPrefencias);
    Button btnRelatorios = (Button) findViewById(R.id.btnRelatorios);
    Button btnBackup = (Button) findViewById(R.id.btnBackup);
    Button btnRestauracao = (Button) findViewById(R.id.btnRestauracao);
    Button btnConfiguracaoInicial = (Button) findViewById(R.id.btnConfiguracaoInicial);

    // Desabilita botão
    btnConfiguracaoInicial.setEnabled(true);

    /* Tratamento do click no botão Contratante */
    btnContratante.setOnClickListener(new OnClickListener() {

      /** botão contratante */
      public void onClick(View v) {

        Intent intent = new Intent(ManutencaoActivity.this, ContratanteActivity.class);

        intent.putExtra(Constantes.CONTRATANTE, mContratante);

        mContratante = (Contratante) intent.getSerializableExtra(Constantes.CONTRATANTE);

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

        if (mContratante == null) {
          Toast.makeText(ManutencaoActivity.this, "Contratante ainda não foi preenchido", Toast.LENGTH_SHORT).show();
          Log.w(TAG, "É necessário cadastrar um contratante antes de preencher os dados do evento");

          return;
        }

        Intent intent = new Intent(ManutencaoActivity.this, EventoActivity.class);

        intent.putExtra(Constantes.EVENTO, mEvento);
        intent.putExtra(Constantes.CONTRATANTE, mContratante);

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
       * Botão relatórios
       */
      public void onClick(View v) {

        Intent i = getIntent();

        List<Participacao> lista = (ArrayList<Participacao>) i.getSerializableExtra(Constantes.LISTA);

        Intent intent = new Intent(ManutencaoActivity.this, RelatorioActivity.class);

        Bundle params = new Bundle();

        int num = (lista != null) ? lista.size() : 0;

        params.putString("numParticipantes", String.valueOf(num));

        intent.putExtra(Constantes.LISTA, (ArrayList<Participacao>) lista);

        intent.putExtra(Constantes.EVENTO, mEvento);

        intent.putExtras(params);

        startActivity(intent);

      }
    });

    /* Tratamento do click no Botão Backup */
    btnBackup.setOnClickListener(new OnClickListener() {

      /**
       * 
       */
      public void onClick(View v) {

        Log.d(TAG, "O botão de backup foi pressionado");

        Toast.makeText(getBaseContext(), "Cria backup da configuração em arquivo", Toast.LENGTH_SHORT).show();

        String arquivo = FileUtils.getBaseDirectory() + File.separator + "config_backup.txt";

        boolean gravou = gravaArquivoConfiguracao(arquivo);

        if (gravou) {
          Toast.makeText(getBaseContext(), "Arquivo de backup: " + arquivo + " gravado com sucesso", Toast.LENGTH_SHORT).show();
        }
        else {
          Toast.makeText(getBaseContext(), "Falha na gravação do arquivo: " + arquivo, Toast.LENGTH_SHORT).show();
        }

      }
    });

    /* Tratamento do click no Botão Restauração (Restore) */
    btnRestauracao.setOnClickListener(new OnClickListener() {

      /**
       * onClick(View v)
       */
      public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ManutencaoActivity.this);

        /*
         * builder.setMessage(
         * "Você tem certeza que apagar todos os dados e restaurar do arquivo ?"
         * ).setCancelable(false) .setPositiveButton("Sim", new
         * DialogInterface.OnClickListener() {
         * 
         * public void onClick(DialogInterface dialog, int id) {
         * 
         * String arquivo = FileUtils.getBaseDirectory() + File.separator +
         * "config.txt";
         * 
         * HashMap<String, String> hash = null;
         * 
         * // Lê o arquivo de configuração try {
         * 
         * hash = leArquivoConfiguracao(arquivo);
         * 
         * } catch (FileNotFoundException e) { Log.w(TAG, "onClick(restore) - "
         * + arquivo + " não foi encontrado", e);
         * Toast.makeText(getBaseContext(), "Falha na abertura do arquivo: " +
         * arquivo, Toast.LENGTH_SHORT).show();
         * 
         * } catch (IOException e) { Log.w(TAG,
         * "onClick(restore) - Erro na abertura do arquivo: " + arquivo, e);
         * 
         * }
         * 
         * // Atualiza as variáveis de preferência if (hash != null) { if
         * (saveMapPreferences(hash)) { Toast.makeText(getBaseContext(),
         * "Restauração feita com sucesso do arquivo: " + arquivo,
         * Toast.LENGTH_SHORT) .show(); } else {
         * Toast.makeText(getBaseContext(),
         * "Falha na restauração da configuração", Toast.LENGTH_SHORT).show(); }
         * } else { Log.w(TAG,
         * "onClick(restore) - Erro na restauração do arquivo: " + arquivo); }
         * 
         * }
         * 
         * }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
         * 
         * public void onClick(DialogInterface dialog, int id) {
         * 
         * dialog.cancel(); }
         * 
         * });
         */

        builder.setMessage("Você tem certeza que apagar todos os dados e restaurar do arquivo ?");

        builder.setCancelable(false);

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

          public void onClick(DialogInterface dialog, int id) {

            String arquivo = FileUtils.getBaseDirectory() + File.separator + "config.txt";

            HashMap<String, String> hash = null;

            // Lê o arquivo de configuração 
            try {

              hash = leArquivoConfiguracao(arquivo);

            } catch (FileNotFoundException e) {
              Log.w(TAG, "onClick(restore) - " + arquivo + " não foi encontrado", e);
              Toast.makeText(getBaseContext(), "Falha na abertura do arquivo: " + arquivo, Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
              Log.w(TAG, "onClick(restore) - Erro na abertura do arquivo: " + arquivo, e);

            }

            // Atualiza as variáveis de preferência
            if (hash != null) {

              if (saveMapPreferences(hash)) {
                Toast.makeText(getBaseContext(), "Restauração feita com sucesso do arquivo: " + arquivo, Toast.LENGTH_SHORT)
                    .show();
              }
              else {
                Toast.makeText(getBaseContext(), "Falha na restauração da configuração", Toast.LENGTH_SHORT).show();
              }

            }
            else {
              Log.w(TAG, "onClick(restore) - Erro na restauração do arquivo: " + arquivo);
            }

          }

        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {

          public void onClick(DialogInterface dialog, int id) {

            dialog.cancel();
          }

        });

        AlertDialog alert = builder.create();
        alert.show();

      }
    });

    /* Tratamento do click no botão Configuração Inicial */
    btnConfiguracaoInicial.setOnClickListener(new OnClickListener() {

      /**
       * 
       */
      public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ManutencaoActivity.this);

        builder.setMessage("Você tem certeza que apagar todos os dados ?").setCancelable(false)
            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int id) {

                boolean b = limpaConfiguracoes();

                if (b) {
                  Toast.makeText(ManutencaoActivity.this, "Configuração inicial restaurada com sucesso", Toast.LENGTH_SHORT).show();

                  mContratante = null;
                  mEvento = null;

                } else {
                  Toast.makeText(ManutencaoActivity.this, "Falha na reinicialização da onfiguração inicial", Toast.LENGTH_SHORT)
                      .show();
                }

              }

            }).setNegativeButton("Não", new DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
              }

            });

        AlertDialog alert = builder.create();
        alert.show();

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

    Log.i(TAG, "onActivityResult(request " + requestCode + ", result=" + resultCode + ", data " + data + ") ...");

    if (requestCode == ACTIVITY_CONTRATANTE) {
      processActivityContratante(resultCode, data);
    } else if (requestCode == ACTIVITY_EVENTO) {
      processActivityEvento(resultCode, data);
    } else if (requestCode == ACTIVITY_PREFERENCIAS) {
      processActivityPreferencias(resultCode, data);
    } else if (requestCode == ACTIVITY_RELATORIOS) {
      processActivityRelatorios(resultCode, data);
    } else {
      Log.w(TAG, "onActivityResult() - requestCode: " + requestCode + " desconhecido");
    }

  }

  /**
   * Processa o resultado da execução da Activity de manutenção de contrante.
   * 
   * @param resultCode
   *          Resultado da execução da intent
   * @param data
   *          Intent
   */
  private void processActivityContratante(int resultCode, Intent data) {

    Log.d(TAG, "processActivityContratante() - retorno da ACTIVITY_CONTRATANTE:");

    if (resultCode == RESULT_OK) {

      Log.d(TAG, "processActivityContratante() - processando RESULT_OK");

      if (data != null) {
        // Atualiza os dados do contratante
        mContratante = (Contratante) data.getSerializableExtra(Constantes.CONTRATANTE);
        Log.d(TAG, "processando contratante=" + mContratante);
      }

      // atualiza o arquivo de preferências
      boolean b = gravarPreferencias();
      if (b) {
        Log.d(TAG, "processActivityContratante() - Arquivo de preferências foi gravado com sucesso após alteração de contratante");
      } else {

      }

    } else if (resultCode == RESULT_CANCELED) {

      Log.d(TAG, "processActivityContratante() - O usuário cancelou a atividade Contratante");

    } else {
      Log.d(TAG, "processActivityContratante() - Erro ...");
    }

  }

  /**
   * Processa o resultado da execução da Activity de manutenção de evento.
   * 
   * @param resultCode
   * 
   * @param data
   * 
   */
  private void processActivityEvento(int resultCode, Intent data) {

    Log.d(TAG, "processActivityEvento() - processando ACTIVITY_EVENTO");

    if (resultCode == RESULT_OK) {

      Log.d(TAG, "processActivityEvento() - processando RESULT_OK");

      if (data != null) {
        mEvento = (Evento) data.getSerializableExtra(Constantes.EVENTO);
        Log.d(TAG, "processActivityEvento() - processando evento=" + mEvento);
      }

      // grava as preferências
      boolean b = gravarPreferencias();
      if (b) {
        Log.d(TAG, "processActivityEvento() - Arquivo de preferências foi gravado com sucesso após alteração do evento");
      } else {

      }

    } else if (resultCode == RESULT_CANCELED) {
      Log.d(TAG, "processActivityEvento() - Usuário cancelou a tela de eventos");

    } else {
      Log.d(TAG, "processActivityEvento() - Erro ...");

    }

  }

  /**
   * processActivityPreferencias(int resultCode, Intent data)
   * 
   * @param resultCode
   * @param data
   */
  private void processActivityPreferencias(int resultCode, Intent data) {

    Log.d(TAG, "processActivityPreferencias() - processando ACTIVITY_PREFERENCIAS");

    if (resultCode == RESULT_OK) {

      Log.d(TAG, "processActivityPreferencias() - processando RESULT_OK");

    } else if (resultCode == RESULT_CANCELED) {
      Log.d(TAG, "processActivityPreferencias() - Usuário cancelou a tela de preferências");

    } else {

      Log.d(TAG, "processActivityPreferencias() - Erro ...");

    }
  }

  /**
   * 
   * @param resultCode
   * @param data
   */
  private void processActivityRelatorios(int resultCode, Intent data) {

    Log.d(TAG, "processActivityRelatorios() - retorno ACTIVITY_RELATORIOS");

    if (resultCode == RESULT_OK) {

      Log.d(TAG, "processActivityRelatorios() - processando RESULT_OK na tela de relatórios");

    } else if (resultCode == RESULT_CANCELED) {

      Log.d(TAG, "processActivityRelatorios() - O usuário cancelou a tela de relatórios");

    } else {
      Log.d(TAG, "processActivityRelatorios() - Erro ...");
    }

  }

  /**
   * Verifica se a aplicação está configurada para uso.<br>
   * 
   * Testa se o Contrante está preenchido.<br>
   * Testa se o evento está preenchido<br>
   * Testa se as bordas estão configuradas para o evento.<br>
   * 
   * @return Retorna true em caso de sucesso e false no caso de haver algum
   *         problema
   * 
   */
  private boolean isConfiguracaoCompleta() {

    boolean configuracaoCompleta = true;

    // verifica se existe um Contratante configurado
    if (mContratante == null) {
      configuracaoCompleta = false;
      Log.w(TAG, "isConfiguracaoCompleta() - Contratante não foi configurado");
    }

    // verifica se existe um Evento cadastrado
    if (mEvento == null) {
      configuracaoCompleta = false;
      Log.w(TAG, "isConfiguracaoCompleta() - Evento não foi configurado");
    }

    // verifica se as bordas das fotos já foram disponibilizadas ao
    // evento
    if (mEvento != null && ((mEvento.getBordaCabine() == null) || (mEvento.getBordaPolaroid() == null))) {
      Log.w(TAG, "isConfiguracaoCompleta() - Bordas não foram configuradas");

      configuracaoCompleta = false;

    }

    return configuracaoCompleta;

  }

  /**
   * Grava a configuração do Contratante e do Evento na estrutura de
   * preferências (Preferences).
   * 
   * @return true caso a gravação ocorra com sucesso ou false caso haja algum
   *         problema.
   */
  private boolean gravarPreferencias() {

    boolean commitDone;

    if (mPreferences == null) {
      Log.w(TAG, "gravarPreferencias() - mPreferences é nula");
      return false;
    }

    Editor edit = mPreferences.edit();

    if (edit != null) {

      Log.d(TAG, "gravarPreferencias() - Gravando as preferências de contratante e evento");

      /* Contratante */
      if (mContratante != null) {

        edit.putString(Constantes.CONTRATANTE_NOME, mContratante.getNome());
        edit.putString(Constantes.CONTRATANTE_EMAIL, mContratante.getEmail());
        edit.putString(Constantes.CONTRATANTE_TELEFONE, mContratante.getTelefone());

      }

      /* Evento */
      if (mEvento != null) {

        edit.putString(Constantes.EVENTO_NOME, mEvento.getNome());
        edit.putString(Constantes.EVENTO_EMAIL, mEvento.getEmail());
        edit.putString(Constantes.EVENTO_ENDERECO, mEvento.getEndereco());
        edit.putString(Constantes.EVENTO_CIDADE, mEvento.getCidade());
        edit.putString(Constantes.EVENTO_ESTADO, mEvento.getEstado());
        edit.putString(Constantes.EVENTO_CEP, mEvento.getCep());
        edit.putString(Constantes.EVENTO_DATA, mEvento.getData());
        edit.putString(Constantes.EVENTO_TELEFONE, mEvento.getTelefone());

        edit.putString(Constantes.EVENTO_ENVIA_FACEBOOK, mEvento.isEnviaFacebook() == true ? "true" : "false");
        edit.putString(Constantes.EVENTO_ENVIA_TWITTER, mEvento.isEnviaTwitter() == true ? "true" : "false");

        edit.putString(Constantes.EVENTO_BORDA_POLAROID, mEvento.getBordaPolaroid());
        edit.putString(Constantes.EVENTO_BORDA_CABINE, mEvento.getBordaCabine());

        edit.putString(Constantes.EVENTO_PARAM1, mEvento.getParametros().getParametro(0));
        edit.putString(Constantes.EVENTO_PARAM2, mEvento.getParametros().getParametro(1));
        edit.putString(Constantes.EVENTO_PARAM3, mEvento.getParametros().getParametro(2));
        edit.putString(Constantes.EVENTO_PARAM4, mEvento.getParametros().getParametro(3));
        edit.putString(Constantes.EVENTO_PARAM5, mEvento.getParametros().getParametro(4));

      }

      // grava as preferências
      commitDone = edit.commit();

      if (commitDone) {
        Toast.makeText(this, "Preferências foram salvas com sucesso", Toast.LENGTH_LONG).show();
      }

      return commitDone;

    } else {
      Log.w(TAG, "gravarPreferencias() - Erro na gravação das preferências compartilhadas");
      return false;
    }

  }

  /**
   * Grava arquivo de preferências.
   * 
   * @param hash
   *          Estrutura com os parâmetros de configuração.
   * 
   * @return true se o arquivo foi salvo com sucesso ou false caso contrário.
   * 
   */
  private boolean gravarPreferencias(HashMap<String, String> hash) {

    boolean commitDone;

    if (mPreferences == null) {
      Log.w(TAG, "gravarPreferencias() - mPreferences is null");
      return false;
    }

    if (hash == null) {
      Log.w(TAG, "gravarPreferencias() - hash é null");
      return false;
    }

    Editor edit = mPreferences.edit();

    if (edit != null) {

      Log.d(TAG, "gravarPreferencias() - Gravando as preferências de contratante e evento");

      for (String chave : hash.keySet()) {
        edit.putString(chave, hash.get(chave));
      }

      // grava as preferências
      commitDone = edit.commit();

      if (commitDone) {
        Toast.makeText(this, "Preferências foram salvas com sucesso", Toast.LENGTH_LONG).show();
      }

      return commitDone;

    } else {
      Log.w(TAG, "gravarPreferencias() - Erro na gravação das preferências compartilhadas");
      return false;
    }

  }

  /**
   * Lê a configuração do Contratante e do Evento armazenada na estrutura de
   * preferências (Preferences).<br>
   * 
   * Inicializa as variáveis mContratante e mEvento com os valores lidos ou
   * apenas cria uma instância delas.<br>
   * 
   * @return true em caso de sucesso ou false no caso de falhas
   * 
   */
  private boolean lerPreferencias() {

    Log.d(TAG, "lerPreferencias() - Lendo as preferências compartilhadas ...");

    mPreferences = getSharedPreferences("preferencias", MODE_PRIVATE);

    if (mPreferences == null) {
      // TODO não seria melhor fazer mContratante e mEvento igual a null ?
      Log.w(TAG, "lerPreferencias() - mPreferences is null. Falha na execução do comandos getSharedPreferences()");
      return false;
    }

    if (mContratante == null) {

      // Contratante não foi inicializado
      // Cria um novo Contratante vazio
      mContratante = new Contratante();

    }

    mContratante.setNome(mPreferences.getString(Constantes.CONTRATANTE_NOME, ""));
    mContratante.setEmail(mPreferences.getString(Constantes.CONTRATANTE_EMAIL, ""));
    mContratante.setTelefone(mPreferences.getString(Constantes.CONTRATANTE_TELEFONE, ""));

    if (mEvento == null) {

      // Evento não foi inicializado
      // Cria um novo Evento vazio
      mEvento = new Evento();

      mEvento.setContratante(mContratante);

    }

    // ------------------------------------

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

    mEvento.setEnviaFacebook(mPreferences.getString(Constantes.EVENTO_ENVIA_FACEBOOK,
        "false").equalsIgnoreCase("true"));

    mEvento.setEnviaTwitter(mPreferences.getString(Constantes.EVENTO_ENVIA_TWITTER,
        "false").equalsIgnoreCase("true")
        );

    // --------------------------------------

    String[] paramEventos = new String[5];

    paramEventos[0] = mPreferences.getString(Constantes.EVENTO_PARAM1, "");
    paramEventos[1] = mPreferences.getString(Constantes.EVENTO_PARAM2, "");
    paramEventos[2] = mPreferences.getString(Constantes.EVENTO_PARAM3, "");
    paramEventos[3] = mPreferences.getString(Constantes.EVENTO_PARAM4, "");
    paramEventos[4] = mPreferences.getString(Constantes.EVENTO_PARAM5, "");

    Parametros parametros = new Parametros(paramEventos);

    mEvento.setParametros(parametros);

    return true;

  }

  /**
   * Reinicia o SharedPreferences("preferencias").<br>
   * Limpa a configuração de Contratante e Evento.<br>
   * 
   * @return true se sucesso ou false caso haja algum erro.
   * 
   *         Usa a variável membro mPreferences.
   * 
   */
  private boolean limpaConfiguracoes() {

    mPreferences = getSharedPreferences("preferencias", MODE_PRIVATE);

    Editor edit = mPreferences.edit();

    // Mark in the editor to remove all values from the preferences.
    // Once commit is called, the only remaining preferences will be any that
    // you have defined in this editor.
    edit.clear();

    // grava as preferências
    boolean commitDone = edit.commit();

    if (commitDone) {
      Log.d(TAG, "limpaConfiguracoes() - Gravando as preferências compartilhadas ...");
      mPreferences = getSharedPreferences("preferencias", MODE_PRIVATE);
    } else {
      Log.w(TAG, "limpaConfiguracoes() - Falha na atualização das preferências compartilhadas.");
    }

    return commitDone;

  }

  /**
   * leArquivoConfiguracao(String filename)
   * 
   * Lê um arquivo com as configurações. O nome das chaves deve ser colocado em
   * letras maiúsculas.<br>
   * 
   * @param filename
   *          nome do arquivo de configuração
   * 
   * @return um HashMap<String,String> com as associações entre chave e valor
   * 
   * @throws FileNotFoundException
   * @throws IOException
   * 
   */
  private HashMap<String, String> leArquivoConfiguracao(String filename) throws FileNotFoundException, IOException {

    BufferedReader buf = null;

    HashMap<String, String> hash = new HashMap<String, String>();

    // número da linha do arquivo
    int linha = 0;

    buf = new BufferedReader(new FileReader(filename));

    String line = null;

    while ((line = buf.readLine()) != null) {

      linha++;

      if (line.startsWith("#")) {
        // ignora linhas começas por '#'
        continue;
      }

      Log.d(TAG, linha + ": " + line);

      String[] atribuicao = line.split("=");

      // Log.v(TAG, "Tamanho=" + atribuicao.length);

      String chave = null;
      String valor = null;

      if (atribuicao.length > 1) {

        chave = atribuicao[0];
        valor = atribuicao[1];

        Log.v(TAG, "Tamanho=" + atribuicao.length + ", Chave=" + chave + ", Valor=" + valor);

      }
      else if (atribuicao.length == 1) {
        chave = atribuicao[0];

        Log.v(TAG, "Chave=" + chave + ", Valor=" + valor);
      }
      else {
        Log.w(TAG, "Erro de atribuição na linha: " + linha);
      }

      hash.put(chave.toLowerCase(), valor);

    }

    if (buf != null) {
      try {
        buf.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // a estrutura de chaves e valores usadas na configuração.
    return hash;

  }

  /**
   * Grava nos arquivos de SharedPreferences os valores mapeados em um HashMap.
   * 
   * @param hash
   *          HashMap contendo a relação de chaves e valores.
   * 
   */
  private boolean saveMapPreferences(HashMap<String, String> hash) {

    if (mPreferences != null) {
      mPreferences = null;
    }

    if (mPreferencesEmail != null) {
      mPreferencesEmail = null;
    }

    SharedPreferences mPreferences = getSharedPreferences("preferencias", MODE_PRIVATE);

    if (mPreferences == null) {
      Log.w(TAG, "saveMapPreferences() - mPreferences é nulo");
      return false;
    }

    SharedPreferences mPreferencesEmail = getSharedPreferences(Constantes.PREF_EMAIL, MODE_PRIVATE);

    if (mPreferencesEmail == null) {
      Log.w(TAG, "saveMapPreferences() - mPreferencesEmail é nulo");
      return false;
    }

    Editor edit1 = mPreferences.edit();
    Editor edit2 = mPreferencesEmail.edit();

    for (String chave : hash.keySet()) {
      if (chave.toUpperCase().startsWith("PREFERENCIA")) {
        edit2.putString(chave, hash.get(chave));
      }
      else {
        edit1.putString(chave, hash.get(chave));
      }
    }

    // grava as preferências
    boolean commitDone1 = edit1.commit();
    boolean commitDone2 = edit2.commit();

    if (commitDone1 && commitDone2) {
      Log.i(TAG, "saveMapPreferences() - Gravação realizada com sucesso");
    }
    else {

      if (commitDone1 == true) {
        Log.i(TAG, "saveMapPreferences() - Gravação 1 realizada com sucesso");
      }
      else {
        Log.w(TAG, "saveMapPreferences() - Gravação 1 realizada com fracasso");
      }

      if (commitDone2 == true) {
        Log.i(TAG, "saveMapPreferences() - Gravação 2 realizada com sucesso");
      }
      else {
        Log.w(TAG, "saveMapPreferences() - Gravação 2 realizada com fracasso");
      }

    }

    //------------------------------------------------------------------------
    // Returns a map containing a list of pairs key/value representing the preferences.
    // Retorna um mapa contendo a lista de pares chave/valor representando as preferências.
    //------------------------------------------------------------------------
    Map<String, ?> mapa = mPreferences.getAll();

    Log.d(TAG, "saveMapPreferences() - mapa possui: " + mapa.size());

    // exibe a lista de chaves e valores
    for (String chave : mapa.keySet()) {
      Log.v(TAG, "saveMapPreferences() - mapa: " + chave + ", valor: " + mapa.get(chave));
    }

    //------------------------------------------------------------------------
    // Returns a map containing a list of pairs key/value representing the preferences.
    // Retorna um mapa contendo a lista de pares chave/valor representando as preferências.
    //------------------------------------------------------------------------
    Map<String, ?> mapa2 = mPreferencesEmail.getAll();

    Log.d(TAG, "saveMapPreferences() - mapa possui: " + mapa2.size());

    for (String chave : mapa2.keySet()) {
      Log.v(TAG, "saveMapPreferences() - mapa: " + chave + ", valor: " + mapa2.get(chave));
    }

    //------------------------------------------------------------------------
    //
    //------------------------------------------------------------------------    

    mPreferencesEmail = null;
    mPreferences = null;

    boolean b = lerPreferencias();

    Log.i(TAG, "saveMapPreferences() - b=" + b + ", ler preferencias");

    return (commitDone1 && commitDone2);

  }

  /**
   * Lê os valores armazenados no arquivo de preferência e cria um HashMap com
   * os mapeamentos de chaves e valores.
   * 
   * @return um HashMap com o mapeamento entra chave e valores ou null caso haja
   *         algum problema
   */
  private HashMap<String, String> readPreferencesMap() {

    HashMap<String, String> hash = new HashMap<String, String>();

    if (mPreferences == null) {
      mPreferences = getSharedPreferences("preferencias", MODE_PRIVATE);
      Log.w(TAG, "obtemValores() - mPreferences é nulo");
    }

    if (mPreferencesEmail == null) {
      mPreferencesEmail = getSharedPreferences(Constantes.PREF_EMAIL, MODE_PRIVATE);
      Log.w(TAG, "obtemValores() - " + Constantes.PREF_EMAIL + " é nulo");
    }

    // obtém o conjunto de chaves da estrutura SharedPreference
    Set<String> set = mPreferences.getAll().keySet();

    for (String chave : set) {

      // cria um novo elemento para cada chave do conjunto
      hash.put(chave.toUpperCase(), mPreferences.getString(chave, ""));
      Log.v(TAG, chave.toUpperCase() + "=" + mPreferences.getString(chave, ""));

    }

    // obtém o conjunto de chaves da estrutura SharedPreference
    Set<String> set2 = mPreferencesEmail.getAll().keySet();

    for (String chave : set2) {

      // cria um novo elemento para cada chave do conjunto
      hash.put(chave.toUpperCase(), mPreferencesEmail.getString(chave, ""));

      Log.v(TAG, chave.toUpperCase() + "=" + mPreferencesEmail.getString(chave, ""));

    }

    // o HashMap de elementos
    return hash;

  }

  /**
   * Grava um arquivo de configuração das preferências da aplicação.<br>
   * 
   * @param filename
   *          nome do arquivo onde as preferências serão salvas.
   * 
   * @return true em caso de sucesso ou false caso contrário
   */
  private boolean gravaArquivoConfiguracao(String filename) {

    BufferedWriter buf = null;

    boolean gravou = false;

    // inicia o HashMap a partir da leitura das preferências
    HashMap<String, String> hash = readPreferencesMap();

    if (hash == null) {
      Log.w(TAG, "gravaArquivoConfiguracao() - conjunto de elementos está vazio");
      return false;
    }

    try {

      buf = new BufferedWriter(new FileWriter(filename));

      for (String chave : hash.keySet()) {
        buf.write(chave.toUpperCase() + "=" + hash.get(chave));
        buf.newLine();
      }

      buf.flush();

    } catch (FileNotFoundException e) {
      //
      Log.w(TAG, "gravaArquivoConfiguracao() - arquivo: " + filename + " não foi encontrado", e);

    } catch (IOException e) {
      //
      Log.w(TAG, "gravaArquivoConfiguracao() - IOException", e);

    } finally {
      if (buf != null) {
        try {
          buf.close();
          gravou = true;
        } catch (IOException e) {
          Log.w(TAG, "gravaArquivoConfiguracao() - erro ao fechar o arquivo", e);
        }
      }
      else {
        gravou = false;
      }
    }

    return gravou;

  }

}
