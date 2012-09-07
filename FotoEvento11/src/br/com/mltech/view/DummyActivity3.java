package br.com.mltech.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.mail.internet.InternetAddress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import br.com.mltech.CameraActivity;
import br.com.mltech.Constantes;
import br.com.mltech.FacebookActivity2;
import br.com.mltech.Mail;
import br.com.mltech.R;
import br.com.mltech.R.id;
import br.com.mltech.R.layout;
import br.com.mltech.modelo.Contratante;
import br.com.mltech.modelo.Evento;
import br.com.mltech.modelo.Foto;
import br.com.mltech.modelo.FotoCabine;
import br.com.mltech.modelo.Moldura;
import br.com.mltech.modelo.Participacao;
import br.com.mltech.modelo.Participante;
import br.com.mltech.utils.AndroidUtils;
import br.com.mltech.utils.FileUtils;
import br.com.mltech.utils.ManipulaImagem;
import br.com.mltech.utils.camera.CameraTools;

/**
 * DummyActivity3
 * 
 * Activity responsável pela obtenção das informações de um participante, tirar
 * fotos e enviar email.
 * 
 * @author maurocl
 * 
 */
public class DummyActivity3 extends Activity implements Constantes {

	// ------------------
	// constantes
	// ------------------

	private static final String TAG = "DummyActivity3";

	// -------------------
	// variáveis da classe
	// -------------------

	// instância de um arquivo
	private static File file;

	// URI onde o será colocada a foto
	private static Uri outputFileUri;

	// Lista de todas as fotos tiradas
	private static List<Foto> listaFotos;

	// Foto
	private static Foto foto;

	// FotoCabine
	private static FotoCabine fotoCabine;

	// Definição dos atributos da aplicação
	private static Contratante mContratante;

	// Um evento
	private static Evento mEvento;

	// Um participante
	private static Participante mParticipante;

	// uma participação
	private static Participacao mParticipacao;

	private static SharedPreferences mPreferences;

	// Estado atual da máquina de estado da aplicação
	private static int mEstado = -1;

	// Nº de vezes que a activity é criada
	private static int mContador = 0;

	// Nº de câmeras do dispositivo
	private static int numCameras = -1;

	// Nº da câmera corrente em uso (se houver)
	private static int currentCamera = -1;

	// Bitmaps contendo as molduras
	private static Bitmap mBitmapMolduraPolaroid;

	private static Bitmap mBitmapMolduraCabine;

	// Molduras
	private static Moldura molduraPolaroid;

	private static Moldura molduraCabine;

	// Contador do nº de fotos cabine
	// private static int mNumFotosCabine = 0;

	// Contador geral (iniciado em onCreate())
	public static int contador = 0;

	public static int i = 0;

	// nº de vezes que o método onCreate() é chamado
	public static int numCreate = 0;

	// nº de vezes que o método onRestart() é chamado
	public static int numRestart = 0;

	// número de fotos carregas
	public static int numFotosCarregadas = 0;

	// nº de fotos efetivamente tiradas
	public static int numFotosTiradas = 0;

	// nº de fotos tiradas no formato Polaroid
	private static int numFotosPolaroid = 0;

	// nº de fotos tiradas no formato Cabine
	private static int numFotosCabine = 0;

	// servidor de email
	private static Mail mailServer = null;

	// modo da tela (portrait / landscape)
	private static int modoTela;

	// instância do Logger 2
	private static Logger logger2 = Logger.getLogger("br.com.mltech");

	// Indica o tipo do disparo da câmera (1=Manual,2=Automático)
	private int tipoDisparo = 2;

	// nome do arquivo onde se encontra a foto
	private String mFilename;

	// ---------------------------------------------
	// área de inicialização de variáveis estáticas
	// ---------------------------------------------
	static {
		// cria uma lista de fotos
		listaFotos = new ArrayList<Foto>();
		fotoCabine = new FotoCabine();
	}

	/**
	 * onCreate(Bundle savedInstanceState)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// Inicia o log da activity
		iniciaLogger();

		Log.d(TAG, "*** onCreate() ***");
		logger2.finest("*** onCreate() ***");

		setContentView(R.layout.dummy);

		if (savedInstanceState != null) {
			// FileUtils.showBundle(savedInstanceState);
		}

		// guarda a orientação inicial da tela
		modoTela = this.getResources().getConfiguration().orientation;
		Log.d(TAG, "onCreate() - Orientação inicial da tela: " + getScreenOrientation(modoTela) + "(" + modoTela + ")");
		logger2.info("onCreate() - Orientação inicial da tela: " + getScreenOrientation(modoTela) + "(" + modoTela + ")");

		// Lê as configurações a respeito da conta de email
		try {

			initEmailConfig();

		} catch (IllegalArgumentException e) {

			Log.w(TAG, "onCreate() - email não está configurado adequadamente");
			logger2.info("onCreate() - email não está configurado adequadamente");

		}

		// Carrega as molduras para fotos Polaroid e Cabine

		try {
			carregaMolduras();
		} catch (IllegalArgumentException e) {
			Log.w(TAG, "onCreate() - molduras não foram configuradas adequadamente.");
			logger2.info("onCreate() - molduras não foram configuradas adequadamente.");
		}

		// Obtem o identificado da câmera que será usada para tirar as fotos
		currentCamera = obtemIdentificadorCamera();

		// ---------------------------------------------------------------------

		// incrementa o nº de vezes que a activity foi reiniciada
		mContador++;

		contador++;
		numCreate++;
		i++;

		Button btnIniciar = (Button) findViewById(R.id.btn0);

		btnIniciar.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				iniciaMaquinaDeEstados();

			}

		});

	}

	/**
	 * onStart() - inicio da activity.
	 */
	@Override
	protected void onStart() {

		super.onStart();
		Log.d(TAG, "*** onStart() ***");
		logger2.finest("*** onStart() ***");
		showVariables();

	}

	/**
	 * onResume()
	 */
	@Override
	protected void onResume() {

		super.onResume();

		// cria o sistema de log em disco
		/*
		 * try { logger = new
		 * br.com.mltech.utils.Logger("/mnt/sdcard/Pictures/logger.log", true); }
		 * catch (IOException e1) { Log.w(TAG,
		 * "onCreate() - falha na abertura do sistema de logs", e1);
		 * 
		 * }
		 */

		Log.d(TAG, "*** onResume() ***");
		logger2.finest("*** onResume() ***");
		showVariables();

	}

	/**
	 * onSaveInstanceState(Bundle outState)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
		Log.d(TAG, "*** onSaveInstanceState() ***");

		logger2.info("*** onSaveInstanceState() ***");

		outState.putSerializable(PARTICIPANTE, mParticipante);
		outState.putSerializable(PARTICIPACAO, mParticipacao);

		showClassVariables();

		// FileUtils.showBundle(outState);

	}

	/**
	 * onPause()
	 */
	@Override
	protected void onPause() {

		super.onPause();
		Log.d(TAG, "*** onPause() ***");
		logger2.info("*** onPause() ***");
		showVariables();

		// logger.close();

	}

	/**
	 * onStop()
	 */
	@Override
	protected void onStop() {

		super.onStop();
		Log.d(TAG, "*** onStop() ***");

		logger2.info("*** onStop() ***");

		showVariables();

		// logger.close();

	}

	/**
	 * Aplicação foi reinicializada.<br>
	 * 
	 * Por qual motivo a aplicação foi reinicializada ???
	 * 
	 */
	@Override
	protected void onRestart() {

		super.onRestart();

		contador++;
		i++;

		numRestart++;

		showClassVariables();

		showVariables();

		Log.i(TAG, "*");
		Log.i(TAG, "*********************************************************************");
		Log.i(TAG, "*** onRestart() - A aplicação foi restartada (" + numRestart + ") ...");
		Log.i(TAG, "*********************************************************************");
		Log.i(TAG, "*");

		logger2.warning("*** onRestart() - A aplicação foi restartada (" + numRestart + ") ...");

	}

	/**
	 * onRestoreInstanceState(Bundle savedInstanceState)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);

		logger2.info("*** onRestoreInstanceState() ***");

		Log.d(TAG, "*********************************");
		Log.d(TAG, "*** onRestoreInstanceState() ***");
		Log.d(TAG, "*********************************");

		if (savedInstanceState == null) {
			Log.w(TAG, "onRestoreInstanceState() - savedInstaceState é nulo");
		}

		if (savedInstanceState.containsKey(PARTICIPANTE)) {
			mParticipante = (Participante) savedInstanceState.getSerializable(PARTICIPANTE);
		}

		if (savedInstanceState.containsKey(PARTICIPACAO)) {
			mParticipacao = (Participacao) savedInstanceState.getSerializable(PARTICIPACAO);
		}

		// FileUtils.showBundle(savedInstanceState);

		showClassVariables();

	}

	/**
	 * Inicia o sistema de log da activity.<br>
	 * 
	 * Usa o atributo de classe logger2.
	 * 
	 */
	private void iniciaLogger() {

		FileHandler fh = null;

		try {
			// cria um arquivo onde será armazenado os logs de execução da aplicação
			fh = new FileHandler(APP_LOG, true);
		} catch (IOException e1) {
			Log.w(TAG, "onCreate() - não foi possível criar o arquivo de log.", e1);
		}

		if (fh != null) {
			fh.setFormatter(new SimpleFormatter());
		}

		// associa um FileHandler ao logger.
		logger2.addHandler(fh);

		Handler[] handlers = logger2.getHandlers();

		if (handlers != null) {
			Log.d(TAG, "iniciarLogger() - nº de handlers: " + handlers.length);
			for (int i = 0; i < handlers.length; i++) {
				Handler h = handlers[i];
				Log.d(TAG, "iniciarLogger() - i=" + i + ", h=" + h);
			}
		}

		// Estabelece o nível do log
		logger2.setLevel(Level.FINEST);

		logger2.info("");

	}

	/**
	 * Inicializa as variáveis (da classe) que vão conter o bitmap das molduras:<br>
	 * 
	 * Carrega as molduras configuradas no arquivo de configuração.
	 * 
	 * mBitmapMolduraPolaroid.<br>
	 * mBitmapMolduraCabine.<br>
	 * <br>
	 * molduraPolaroid<br>
	 * molduraCabine<br>
	 * 
	 */
	private void carregaMolduras() throws IllegalArgumentException {

		mPreferences = getSharedPreferences("preferencias", MODE_PRIVATE);

		if (mPreferences == null) {

			Log.w(TAG, "carregaMolduras() - mPreferences é nula. Falha na execução do comandos getSharedPreferences()");
			throw new IllegalArgumentException();

		}

		// o arquivo de configuração possui a informação da localização das
		// molduras

		// Obtém o arquivo contendo o bitmap da moldura formato polaroid
		String arquivoMolduraPolaroid = mPreferences.getString(Constantes.EVENTO_BORDA_POLAROID, null);

		// Obtém o arquivo contendo o bitmap da moldura formato cabine
		String arquivoMolduraCabine = mPreferences.getString(Constantes.EVENTO_BORDA_CABINE, null);

		if (arquivoMolduraPolaroid == null) {
			// não foi possível encontrar a variável Constantes.EVENTO_BORDA_POLAROID
			// no arquivo de configuração
			Log.w(TAG, "carregaMolduras() - moldura formato Polaroid não foi configurada.");
			logger2.warning("carregaMolduras() - moldura formato Polaroid não foi configurada.");
			return;
		}

		if (arquivoMolduraCabine == null) {
			// não foi possível encontrar a variável Constantes.EVENTO_BORDA_CABINE no
			// arquivo de configuração
			Log.w(TAG, "carregaMolduras() - moldura formato Cabine não foi configurada.");
			logger2.warning("carregaMolduras() - moldura formato Cabine não foi configurada.");
			throw new IllegalArgumentException();
		}

		// ----------------------------------------------------------
		// o arquivo contendo a moldura é lido no inicio da activity
		// ----------------------------------------------------------
		Log.d(TAG, "carregaMolduras() - lendo arquivo contendo moldura formato Polaroid");
		mBitmapMolduraPolaroid = leArquivoMoldura(arquivoMolduraPolaroid);
		Log.d(TAG, "carregaMolduras() - mBitmapMolduraPolaroid =" + mBitmapMolduraPolaroid);

		Log.d(TAG, "carregaMolduras() - Lendo arquivo contendo moldura formato Cabine");
		mBitmapMolduraCabine = leArquivoMoldura(arquivoMolduraCabine);
		Log.d(TAG, "carregaMolduras() - mBitmapMolduraCabine = " + mBitmapMolduraCabine);

		// cria uma instância da classe Moldura - Polaroid
		molduraPolaroid = new Moldura(arquivoMolduraPolaroid, "Moldura Polaroid 1");
		molduraPolaroid.setImagem(mBitmapMolduraPolaroid);

		// cria uma instância da classe Moldura - Cabine
		molduraCabine = new Moldura(arquivoMolduraCabine, "Moldura Cabine 1");
		molduraCabine.setImagem(mBitmapMolduraCabine);

	}

	/**
	 * Verifica se existe uma configuração explícita para usar um determinado
	 * identificador de câmera (se o sistema possuir mais de uma câmera).<br>
	 * Esse recurso é usado quando existe mais de uma câmera suportada pelo
	 * dispositivo e desejamos, por exemplo,usar câmera frontal ao invés da câmera
	 * traseira.
	 * 
	 * @return o identificador da câmera frontal ou 0 caso esse parâmetro não
	 *         esteja cadastrado
	 * 
	 */
	private int obtemIdentificadorCamera() {

		// -----------------------------------------------
		// obtem o identificador da câmera usada para tirar as fotos
		// -----------------------------------------------
		String sCameraId = getSharedPreference(Constantes.PREF_EMAIL, Constantes.PREFERENCIAS_NUM_CAMERA_FRONTAL);

		Log.i(TAG, "obtemIdentificadorCamera() - Id da câmera frontal=" + sCameraId);

		// identificador (Id) da câmera
		int id = 0;

		if ((sCameraId != null) && (!sCameraId.equals(""))) {

			id = Integer.valueOf(sCameraId);

		} else {

			id = 0;
			Log.w(TAG, "obtemIdentificadorCamera() - Identificador da câmera frontal não foi definido. Assumindo o valor 0");

		}

		// o identificador da câmera
		return id;

	}

	/**
	 * Lê as configurações para envio de email usando uma conta externa.<br>
	 * 
	 * <br>
	 * <u>IMPORTANTE</u>: Usa a variável membro: <b>mailServer.</b><br>
	 * <br>
	 * 
	 * usuario, senha, host, porta, remetente
	 * 
	 */
	private void initEmailConfig() throws IllegalArgumentException {

		Log.d(TAG, "initEmailConfig() - inicio");
		logger2.finer("initEmailConfig() - início");

		String mEmail = getSharedPreference(Constantes.PREF_EMAIL, Constantes.PREFERENCIAS_USUARIO_EMAIL);
		String mSenha = getSharedPreference(Constantes.PREF_EMAIL, Constantes.PREFERENCIAS_SENHA_EMAIL);

		String mHost = getSharedPreference(Constantes.PREF_EMAIL, Constantes.PREFERENCIAS_SERVIDOR_SMTP);
		String mPort = getSharedPreference(Constantes.PREF_EMAIL, Constantes.PREFERENCIAS_SERVIDOR_SMTP_PORTA);

		String mRemetente = getSharedPreference(Constantes.PREF_EMAIL, Constantes.PREFERENCIAS_REMETENTE_EMAIL);

		String mEmailDebug = getSharedPreference(Constantes.PREF_EMAIL, Constantes.PREFERENCIAS_EMAIL_DEBUG);
		String mEmailAuth = getSharedPreference(Constantes.PREF_EMAIL, Constantes.PREFERENCIAS_EMAIL_AUTH);
		String mEmailSsl = getSharedPreference(Constantes.PREF_EMAIL, Constantes.PREFERENCIAS_EMAIL_SSL);

		// cria a instância do sistema de email
		mailServer = new Mail(mEmail, mSenha);

		Log.d(TAG, "initEmailConfig() - cria a instância de mailServer");
		logger2.finer("initEmailConfig() - cria a instância de mailServer");

		mailServer.setHost(mHost);
		mailServer.setPort(mPort);
		mailServer.setFrom(mRemetente);
		mailServer.setDebuggable((mEmailDebug.equalsIgnoreCase("true")) ? true : false);
		mailServer.setAuth((mEmailAuth.equalsIgnoreCase("true")) ? true : false);
		mailServer.setSsl((mEmailSsl.equalsIgnoreCase("true")) ? true : false);

		Log.d(TAG, "initEmailConfig() - SMTP host: " + mailServer.getHost());
		Log.d(TAG, "initEmailConfig() - SMTP port: " + mailServer.getPort());
		Log.d(TAG, "initEmailConfig() - from: " + mailServer.getFrom());
		Log.d(TAG, "initEmailConfig() - debuggable: " + mailServer.isDebuggable());
		Log.d(TAG, "initEmailConfig() - authorization: " + mailServer.isAuth());
		Log.d(TAG, "initEmailConfig() - SSL: " + mailServer.isSsl());

		// valida os argumentos obrigatórios do email
		validaArgumentosEmail(mEmail, mSenha, mailServer.getHost(), mailServer.getPort(), mailServer.getFrom());

		Log.d(TAG, "initEmailConfig() - fim");
		logger2.finer("initEmailConfig() - fim");

	}

	/**
	 * Inicia o processo do participante do evento.
	 */
	private void iniciaMaquinaDeEstados() {

		boolean sucesso = iniciaVariaveis();

		if (!sucesso) {
			Log.w(TAG, "iniciaProcesso() - Não foi possível inicializar as variáveis");
			estadoFinal();
		}

	}

	/**
	 * Inicia a execução da máquina de estados da activity.<br>
	 * 
	 * @return true se as operações iniciais foram executas com sucesso ou false
	 *         em caso de erro
	 * 
	 */
	private boolean iniciaVariaveis() {

		// obtem o nº de câmeras disponíveis pelo dispositivo onde a aplicação
		// está em execução
		numCameras = android.hardware.Camera.getNumberOfCameras();

		Log.d(TAG, "iniciaVariaveis() - Número de Câmeras disponíveis no hardware: " + numCameras);

		// verifica se a câmera fotogrática está em operação
		if (CameraTools.isCameraWorking(currentCamera)) {

			Log.i(TAG, "iniciaVariaveis() - Camera: [" + currentCamera + "] está em funcionamento...");

		} else {

			Log.w(TAG, "iniciaVariaveis() - Camera: [" + currentCamera + "] não está em funcionamento");

			Toast.makeText(this, "Camera não está disponível", Toast.LENGTH_SHORT);

			return false;

		}

		// Obtem a Intent que iniciou esta Activity
		Intent i = this.getIntent();

		// indicador de erro de configuração
		int erro = 0;

		// Obtém informações sobre o Contratante
		if (i.hasExtra(CONTRATANTE)) {
			mContratante = (Contratante) i.getSerializableExtra(CONTRATANTE);
		} else {
			Log.w(TAG, "iniciaVariaveis() - Contratante não pode ser nulo.");
			Toast.makeText(this, "Contratante não pode ser nulo", Toast.LENGTH_SHORT).show();
			erro = 1;
		}

		// Obtem informações sobre o Evento
		if (i.hasExtra(EVENTO)) {
			mEvento = (Evento) i.getSerializableExtra(EVENTO);
		} else {
			Log.w(TAG, "iniciaVariaveis() - Evento não pode ser nulo.");
			Toast.makeText(this, "Evento não pode ser nulo", Toast.LENGTH_SHORT).show();
			erro += 2;
		}

		if (erro > 0) {

			Log.w(TAG, "iniciaVariaveis() - Informações insuficientes para execução (erro=" + erro + ")");

			showAlert("Verifique a configuração da aplicação");

			return false;

		}

		Log.v(TAG, "iniciaVariaveis() - mContratante=" + mContratante);
		Log.v(TAG, "iniciaVariaveis() - mEvento=" + mEvento);

		// Altera o estado atual
		setEstado(0);

		// Inicia a obtenção dos Participantes
		startActivityParticipante();

		return true;

	}

	/**
	 * Obtém informações sobre o participante do evento<br>
	 * 
	 * Inicia a Activity Participante. Passa como parâmetro as informações sobre o
	 * Evento.<br>
	 * 
	 * Inicia a activity para obter as informações a respeito de um participante.
	 * 
	 * Aguarda as informações sobre o participante do evento.
	 * 
	 */
	private void startActivityParticipante() {

		// Cria uma nova Intent para chamar a Activity Participante
		Intent intentParticipante = new Intent(this, ParticipanteActivity.class);

		// Inclui o parâmetro mEvento (com as informações sobre o evento em
		// curso)
		intentParticipante.putExtra(EVENTO, mEvento);

		// Inicia a Activity
		startActivityForResult(intentParticipante, ACTIVITY_PARTICIPANTE);

	}

	/**
	 * Obtém uma instância de um Participante como retorno da execução da
	 * activity.<br>
	 * 
	 * @param resultCode
	 *          resultado da execução da activity Participante
	 * 
	 * @param data
	 *          Intent com os resultados (se houverem)
	 * 
	 */
	private void resultActivityParticipante(int resultCode, Intent data) {

		Log.d(TAG, "resultActivityParticipante() ==> processando resultado da ACTIVITY PARTICIPANTE");

		if (resultCode == RESULT_CANCELED) {

			// operação cancelada
			Log.d(TAG, "resultActivityParticipante() - resultCode=RESULT_CANCELED - Participante cancelou sua participação");

			// Limpa as informações do participante
			mParticipante = null;
			mParticipacao = null;

			return;

		} else if (resultCode != RESULT_OK) {

			// o resultado execução da activity não é conhecido
			Log.w(TAG, "resultActivityParticipante() - resultCode não conhecido: " + resultCode);
			return;

		}

		if (data == null) {
			// caso a Intent não retorne nada houve algum problema
			Log.w(TAG, "resultActivityParticipante() - A intent não retornou nenhuma informação");
			return;
		}

		// exibe a lista de valores retornados
		FileUtils.showBundle(data.getExtras());

		// atualiza a variável com os dados do participante
		if (data.hasExtra(PARTICIPANTE)) {
			mParticipante = (Participante) data.getSerializableExtra(PARTICIPANTE);
			// Exibe as informações sobre o participante e sua participação
			Log.d(TAG, "resultActivityParticipante() - mParticipante=" + mParticipante);

		}

		// inicia a activity Participação
		startActivityParticipacao();

		// Atualiza o estado da máquina de estados
		setEstado(1);

	}

	/**
	 * A activity participação pega as informações de um participante e escolhe o
	 * tipo e efeito da foto.
	 * 
	 */
	private void startActivityParticipacao() {

		// Cria uma nova Intent para chamar a Activity Participacao
		Intent intentParticipacao = new Intent(this, ParticipacaoActivity.class);

		// Inclui o parâmetro mEvento (com as informações sobre o evento em
		// curso)
		intentParticipacao.putExtra(EVENTO, mEvento);
		intentParticipacao.putExtra(PARTICIPANTE, mParticipante);

		// Inicia a Activity
		startActivityForResult(intentParticipacao, ACTIVITY_PARTICIPACAO);

	}

	/**
	 * resultActivityParticipacao(int resultCode, Intent data)
	 * 
	 * Processa o resultado da execução da activity Participação.
	 * 
	 * @param resultCode
	 *          Código de retorno da execução da activity
	 * 
	 * @param data
	 *          intent com os dados retornados
	 * 
	 */
	private void resultActivityParticipacao(int resultCode, Intent data) {

		Log.d(TAG, "resultActivityParticipacao() ==> processando resultado da ACTIVITY PARTICIPACAO");

		if (resultCode == RESULT_CANCELED) {

			// operação cancelada

			Log.d(TAG, "resultCode=RESULT_CANCELED - Participação foi cancelada.");

			// Limpa as variáveis
			mParticipacao = null;

			return;

		} else if (resultCode != RESULT_OK) {

			// o resultado execução da activity não é conhecido
			Log.w(TAG, "resultActivityParticipacao() - resultCode não conhecido: " + resultCode);
			return;

		}

		if (data == null) {
			// caso a Intent não retorne nada houve algum problema
			Log.w(TAG, "resultActivityParticipacao() - A intent não retornou nenhuma informação");
			return;
		}

		// exibe a lista de valores retornados
		FileUtils.showBundle(data.getExtras());

		// atualiza a participacao
		if (data.hasExtra(PARTICIPACAO)) {
			mParticipacao = (Participacao) data.getSerializableExtra(PARTICIPACAO);
			// Exibe as informações sobre o participante e sua participação
			Log.d(TAG, "resultActivityParticipacao() - mParticipacao=" + mParticipacao);
		}

		// Atualiza o estado da máquina de estados
		setEstado(1);

		// Processa o próximo estado
		tirarFotos();

	}

	/**
	 * Tira as fotos de acordo com a solicitação do participante.<br>
	 * 
	 * Nesse ponto o usuário já forneceu suas informações pessoais e agora é
	 * necessário tirar a(s) foto(s).<br>
	 * 
	 * Esse método depende dos valores das variáveis:<br>
	 * mParticipante, mParticipação, mEvento.<br>
	 * 
	 * A aplicação prepara-se para tirar as fotos de acordo com a escolha do
	 * participante. Poderá ser do tipo Polaroid ou Cabine.<br>
	 * 
	 * Cada escolha irá disparar uma nova activity encarregada pela obtenção das
	 * fotos.
	 * 
	 */
	private void tirarFotos() {

		logger2.finest("tirarFotos() - inicio");

		// valida o participante
		if (mParticipante == null) {
			Log.d(TAG, "tirarFotos() - não é possível obter as informações do participante");
			estadoFinal();
		}

		// valida a participação
		if (mParticipacao == null) {
			Log.d(TAG, "tirarFotos() - não é possível obter as informações da participação");
			estadoFinal();
		}

		// valida o evento
		if (mEvento == null) {
			Log.d(TAG, "tirarFotos() - não é possível obter as informações sobre o evento");
			estadoFinal();
		}

		// obtém o tipo da foto (se o formato da foto é Polaroid ou Cabine)
		int tipoFoto = mParticipacao.getTipoFoto();

		Log.i(TAG, "------------------------------------------------------------");
		Log.i(TAG, "==> tirarFotos() - tipoFoto: " + tipoFoto);
		Log.i(TAG, "------------------------------------------------------------");

		logger2.finest("tirarFotos() - tipoFoto: " + tipoFoto);

		if (tipoFoto == TIPO_FOTO_POLAROID) {

			// --------------------
			// TIPO_FOTO_POLAROID
			// --------------------

			// gera um nome para o arquivo onde a foto será armazenada
			String arquivo = FileUtils.obtemNomeArquivo(".png").getAbsolutePath();

			Log.i(TAG, "tirarFotos() - tipoFoto: POLAROID, arquivo: " + arquivo);
			logger2.info("tirarFotos() - tipoFoto: POLAROID, arquivo: " + arquivo);

			// executa a activity responsável por tirar uma foto e formatá-la como
			// Polaroid.
			executaActivityTiraFotoPolaroid(arquivo);

		} else if (tipoFoto == TIPO_FOTO_CABINE) {

			// --------------------
			// TIPO_FOTO_CABINE
			// --------------------

			Log.i(TAG, "tirarFotos() - tipoFoto: CABINE");
			logger2.info("tirarFotos() - tipoFoto: CABINE");

			// executa a activity responsável por tirar uma três foto e formatá-las
			// como Cabine.
			executaActivityTiraFotoCabine();

		} else {

			Log.w(TAG, "tirarFotos() - tipo de foto: " + tipoFoto + " não suportado.");
			estadoFinal();

		}

	}

	/**
	 * Cria uma intent para solicitar uma foto a uma activity.<br>
	 * 
	 * <br>
	 * Observe que a variável FLAG é usada para "escolher" a activity que será
	 * executada e que irá retornar a foto.
	 * 
	 * @return uma Intent (mensagem) para execução da activity desejada.
	 * 
	 */
	private Intent getIntentTirarFoto() {

		Intent intent = null;

		// tira uma foto verdadeira usando a activity CameraActivity
		intent = new Intent(this, CameraActivity.class);

		intent.putExtra(Constantes.PARTICIPACAO, mParticipacao);

		intent.putExtra("br.com.mltech.cameraId", currentCamera);
		intent.putExtra("br.com.mltech.outputFileUri", outputFileUri);

		Log.d(TAG, "getIntentTirarFoto() -  " + intent);
		Log.d(TAG, "getIntentTirarFoto() - outputFileUri: " + outputFileUri);

		return intent;

	}

	/**
	 * Inicia a activity responsável por tirar uma foto e formatá-la como
	 * polaroid.<br>
	 * 
	 * @param arquivo
	 *          nome do arquivo onde a foto original tirada pela câmera será
	 *          armazenada.
	 * 
	 */
	private void executaActivityTiraFotoPolaroid(String arquivo) {

		logger2.info("executaActivityTiraFotoPolaroid() - arquivo=" + arquivo);

		Log.d(TAG, "executaActivityTiraFotoPolaroid() - arquivo=" + arquivo);

		file = new File(arquivo);

		outputFileUri = Uri.fromFile(file);

		// obtém a intent responsável pela obtenção da foto
		Intent intent = getIntentTirarFoto();

		intent.putExtra("NUM_FOTOS", 1);
		intent.putExtra("TIPO_DISPARO", tipoDisparo);

		// inicia a activity selecionada
		startActivityForResult(intent, TIRA_FOTO_POLAROID);

	}

	/**
	 * Processa o resultado da execução da activity responsável por fornecer uma
	 * foto.
	 * 
	 * Pega a foto retornada e formata-a no formato Polaroid.<br>
	 * 
	 * @param resultCode
	 *          resultado da execução da activity
	 * 
	 * @param data
	 *          intent contendo os dados retornados (se houver)
	 * 
	 *          Use a variável de classe: file
	 * 
	 */
	private String processaActivityResultPolaroid(int resultCode, Intent data) {

		Toast.makeText(this, "Processando foto Polaroid ...", Toast.LENGTH_LONG).show();

		logger2.info("Processando foto Polaroid ...");

		String meuArquivo2 = null;

		/**
     * 
     */
		if (resultCode == RESULT_CANCELED) {

			// operação cancelada
			Log.w(TAG, "processaActivityResultPolaroid() - Operação cancelada pelo usuário");
			estadoFinal();

		} else if (resultCode != RESULT_OK) {

			Log.w(TAG, "processaActivityResultPolaroid() - Operação não conhecida");
			estadoFinal();

		}

		//
		// RESULT_OK ==> activity retornou com sucesso
		//
		if (data != null) {

			Log.d(TAG, "processaActivityResultPolaroid() - data.getData()= " + data.getData());

			String[] fotoPolaroid = null;

			if (data.hasExtra("br.com.mltech.fotos")) {
				fotoPolaroid = (String[]) data.getSerializableExtra("br.com.mltech.fotos");
			}

			if (fotoPolaroid != null) {
				// cria um arquivo com o nome da foto
				File temp1 = new File(fotoPolaroid[0]);
				// retorna a Uri da foto
				outputFileUri = Uri.fromFile(temp1);
			} else {
				Log.w(TAG, "processaActivityResultPolaroid() - nenhum dado retornou");
			}

			// Loga a uri da foto
			Log.d(TAG, "processaActivityResultPolaroid() - outputFileUri= " + outputFileUri);

		} else {

			// não houve retorno de dados
			Log.w(TAG, "processaActivityResultPolaroid - data (Intent) é vazia");

			outputFileUri = null;

		}

		// exibe a URI conteúdo a foto
		Log.i(TAG, "processaActivityResultPolaroid() - outputFileUri: " + outputFileUri);

		// grava o bitmap (foto original)
		boolean gravouFotoOriginal = ManipulaImagem.gravaBitmapArquivo3(outputFileUri);

		if (!gravouFotoOriginal) {
			Log.w(TAG, "processaActivityResultPolaroid() - arquivo: " + outputFileUri + " não pode ser gravado");
			return null;
		}

		// String meuArquivo = FileUtils.getFilename(file);
		String meuArquivo = outputFileUri.getPath();

		meuArquivo = FileUtils.getFilename(outputFileUri);

		// cria um novo arquivo para armazenar a foto com a moldura
		meuArquivo2 = FileUtils.getBasePhotoDirectory() + File.separator + meuArquivo + ".jpg";

		// meuArquivo2 = FileUtils.obtemNomeArquivo("");

		Log.d(TAG, "meuArquivo=" + meuArquivo);
		Log.d(TAG, "meuArquivo2=" + meuArquivo2);

		// -------------------------------------------------------------------------------
		Log.d(TAG, "carregaImagem()");

		// atualiza o nº de fotos carregadas
		numFotosCarregadas++;

		//
		// Adiciona a nova foto a lista de fotos
		//
		listaFotos.add(foto);

		Log.i(TAG, "carregaImagem() ===> numFotosTiradas: " + numFotosTiradas + ", numFotosCarregadas: " + numFotosCarregadas);

		// -------------------------------------------------------------------------------

		// Obtém uma foto formato polaroid
		Bitmap fp = formatarPolaroid(outputFileUri);

		if (fp != null) {

			// bitmap obtido com sucesso
			boolean gravouFotoComMoldura = ManipulaImagem.gravaBitmapArquivo(fp, meuArquivo2);

			if (!gravouFotoComMoldura) {
				// foto não foi gravada
				Log.w(TAG, "processaActivityResultPolaroid() - arquivo: " + meuArquivo2 + " não pode ser gravado");
				return null;
			}

		}

		Log.w(TAG, "processaActivityResultPolaroid - FIM");
		logger2.info("processaActivityResultPolaroid - FIM");

		// o nome do arquivo onde a foto Polaroid foi gravada.
		return meuArquivo2;

	}

	/**
	 * Activity responsável por obter as três fotos que irão compor a foto no
	 * formato cabine.
	 * 
	 */
	private void executaActivityTiraFotoCabine() {

		Log.i(TAG, "executaActivityTiraFotoCabine()");

		// TODO melhorar a construção abaixo
		// gera o nome de um arquivo onde será armazenada a foto
		String arquivo = (FileUtils.obtemNomeArquivo(".png")).getAbsolutePath();

		// cria um File usando o nome do arquivo gerado
		file = new File(arquivo);

		Log.d(TAG, "==>");
		Log.d(TAG, "==> executaActivityTiraFotoCabine() - arquivo=" + file.getAbsolutePath());
		Log.d(TAG, "==> ");

		// especifica a Uri do arquivo onde a foto deve ser armazenada
		outputFileUri = Uri.fromFile(file);

		// cria uma intent para "tirar a foto"
		Intent intent = getIntentTirarFoto();

		intent.putExtra("NUM_FOTOS", 3);
		intent.putExtra("TIPO_DISPARO", tipoDisparo);

		// exibe informações sobre a intent criada
		Log.d(TAG, "executaActivityTiraFotoCabine() - intent: " + intent);

		// TODO - verificar a linha abaixo
		Log.d(TAG, "executaActivityTiraFotoCabine() - " + intent.getParcelableExtra("br.com.mltech.outputFileUri"));

		// inicia a activity responsável por obter a foto usando a intent criada.
		startActivityForResult(intent, TIRA_FOTO_CABINE);

	}

	/**
	 * Esta rotina é responsável por obter três foto da câmera.<br>
	 * A variável contadorCabine controla o nº de fotos (varia de 0 a 2).<br>
	 * 
	 * @param resultCode
	 *          resultado da execução da activity
	 * 
	 * @param data
	 *          dados retornados da execução da activity
	 * 
	 */
	private String processaActivityResultCabine(int resultCode, Intent data) {

		Log.i(TAG, "processaActivityResultCabine() - Inicio do método");

		Bitmap meuBitmap = null;

		if (resultCode == RESULT_OK) {

			if (data != null) {

				outputFileUri = data.getData();

				Log.d(TAG, "processaActivityResultPolaroid() - data.getData()= " + data.getData());

				String[] fotosCabine = null;

				if (data.hasExtra("br.com.mltech.fotos")) {
					fotosCabine = (String[]) data.getSerializableExtra("br.com.mltech.fotos");
				}

				for (int i = 0; i < fotosCabine.length; i++) {

					// gera o bitmap com a foto
					Bitmap b = ManipulaImagem.criaBitmap(fotosCabine[i]);

					Foto foto = new Foto(file.getAbsolutePath(), b);

					// armazena a foto no índice dado pela variável indiceFoto no array de
					// fotos do objeto FotoCabine
					fotoCabine.setFoto(i, foto);

				}

			}

			// monta uma foto do tipo cabine
			meuBitmap = montaFotoCabine(fotoCabine);

			if (meuBitmap == null) {
				Log.w(TAG, "processaActivityResultCabine() - bitmap retornado é nulo");
			}

		} else if (resultCode == RESULT_CANCELED) {

			// operação cancelada
			Log.w(TAG, "processaActivityResultCabine() - Operação cancelada pelo usuário.");
			// TODO aqui deveremos "cancelar" a fotoCabine fazendo-a null (entre
			// outras coisas)
			meuBitmap = null;

		} else {

			Log.w(TAG, "processaActivityResultCabine() - Operação não suportada pelo usuário");

		}

		// cria um novo arquivo para guardar a foto processada
		String arquivo = (FileUtils.obtemNomeArquivo(".png")).getAbsolutePath();

		// cria uma instância de Foto para guardar a foto processada, isto é, a foto
		// no formato cabine já com a moldura.
		Foto fotoProcessada = new Foto(arquivo, meuBitmap);

		boolean gravouFotoProcessada = false;

		try {

			// grava a foto
			gravouFotoProcessada = fotoProcessada.gravar();

			if (gravouFotoProcessada == false) {
				Log.w(TAG, "processaActivityResultCabine() - fotoProcessada não foi gravada !!!");
			}

		} catch (FileNotFoundException e) {
			Log.w(TAG, "processaActivityResultCabine() - arquivo não foi encontrado", e);

		} catch (IOException e) {
			Log.w(TAG, "processaActivityResultCabine() - erro de I/O", e);

		}

		String s = null;

		// retorna o nome do arquivo onde a foto foi armazenada
		if ((fotoProcessada != null) && (gravouFotoProcessada == true)) {
			s = fotoProcessada.getArquivo();
		}

		// retorna o nome do arquivo contendo a foto processada ou null em caso de
		// erro.
		return s;

	}

	/**
	 * Cria uma foto com moldura cabine de tamanho 3.5 x 15.0 cm a partir de três
	 * fotos 3x4 e uma moldura.<br>
	 * 
	 * @param fotoCabine
	 *          Instância da classe FotoCabine
	 * 
	 * @return Bitmap contendo a foto formatada ou null em caso de erro.
	 * 
	 * @throws IOException
	 * 
	 * @throws FileNotFoundException
	 * 
	 */
	private Bitmap montaFotoCabine(FotoCabine fotoCabine) {

		if (fotoCabine == null) {
			// fotoCabine não existe.
			Log.w(TAG, "montaFotoCabine() - fotoCabine está vazia");
			return null;
		}

		// cria um array contendo referências a três imagens (três fotos que serão
		// combinadas).
		Bitmap[] fotosRedimesionadas = new Bitmap[3];

		// o array fotos possui uma posição para cada foto
		// preenche o array com as fotos armazenadas na fotoCabine
		Foto[] fotos = fotoCabine.getFotos();

		//
		// processa cada uma das fotos e faz o redimensionamento
		// para o tamanho 3cm x 4cm.
		//
		for (int i = 0; i < fotos.length; i++) {

			Log.i(TAG, "montaFotoCabine() - Processando foto[" + i + "] = " + fotos[i]);

			// transforma cada foto em 3x4
			// gera um bitmap com a imagem redimensionada em 3x4
			// fotosRedimesionadas[i] =
			// ManipulaImagem.getScaledBitmap2(fotos[i].getImagem(), 151, 131);
			fotosRedimesionadas[i] = ManipulaImagem.getScaledBitmap2(fotos[i].getImagem(), 131, 151);

		}

		// Inverte as todas asfotos
		fotosRedimesionadas[0] = getReflectedBitmapAxisY(fotosRedimesionadas[0]);
		fotosRedimesionadas[1] = getReflectedBitmapAxisY(fotosRedimesionadas[1]);
		fotosRedimesionadas[2] = getReflectedBitmapAxisY(fotosRedimesionadas[2]);

		//
		// gera um único bitmap a partir das três foto 3x4 e inclui a moldura
		//
		Bitmap bitmap = processaFotoFormatoCabine3(fotosRedimesionadas[0], fotosRedimesionadas[1], fotosRedimesionadas[2],
				mBitmapMolduraCabine);

		if (bitmap != null) {
			Log.v(TAG, "montaFotoCabine() - bitmap gerado");
		} else {
			Log.w(TAG, "montaFotoCabine() - bitmap não foi gerado (retornou nulo)");
		}

		// retorna um bitmap ou null em caso de erro
		return bitmap;

	}

	/**
	 * Resultado da execução da ActivityFacebook
	 * 
	 * @param resultCode
	 * @param data
	 */
	private void resultActivityFacebook(int resultCode, Intent data) {

		Log.d(TAG, "***** resultActivityFacebook() ==> processando resultado da ACTIVITY FACEBOOK");

		if (resultCode == RESULT_CANCELED) {

			// operação cancelada

			Log.d(TAG, "resultActivityFacebook() - resultCode=RESULT_CANCELED - FACEBOOK foi cancelada.");

			return;

		} else if (resultCode != RESULT_OK) {

			// o resultado execução da activity não é conhecido
			Log.w(TAG, "resultActivityFacebook() - resultCode não conhecido: " + resultCode);
			return;

		}

		if (data == null) {
			// caso a Intent não retorne nada houve algum problema
			Log.w(TAG, "resultActivityFacebook() - A intent não retornou nenhuma informação");
			return;
		}

		Log.d(TAG, "resultActivityFacebook() - finalizado com sucesso");

	}

	/**
	 * Resultado da execução da ActivityTwitter
	 * 
	 * @param resultCode
	 * @param data
	 * 
	 */
	private void resultActivityTwitter(int resultCode, Intent data) {

		Log.d(TAG, "***** resultActivityTwitter() ==> processando resultado da ACTIVITY TWITTER");

		if (resultCode == RESULT_CANCELED) {

			// operação cancelada

			Log.d(TAG, "resultActivityTwitter() - resultCode=RESULT_CANCELED - Twitter foi cancelada.");

			return;

		} else if (resultCode != RESULT_OK) {

			// o resultado execução da activity não é conhecido
			Log.w(TAG, "resultActivityTwitter() - resultCode não conhecido: " + resultCode);
			return;

		}

		if (data == null) {
			// caso a Intent não retorne nada houve algum problema
			Log.w(TAG, "resultActivityTwitter() - A intent não retornou nenhuma informação");
			return;
		}

		Log.d(TAG, "resultActivityTwitter() - finalizado com sucesso");

	}

	/**
	 * Recebe o nome do arquivo da foto já processado.
	 * 
	 * @param requestCode
	 *          códido da requisição
	 * 
	 * @param meuArquivo
	 *          nome do arquivo contendo a foto processada
	 * 
	 */
	private void meuMetodo(int requestCode, String meuArquivo) {

		// TODO alterar o nome do método

		Log.d(TAG, "meuMetodo() - requestCode=" + requestCode);

		logger2.info("meuMetodo() - requestCode=" + requestCode);

		if (meuArquivo != null) {
			Log.d(TAG, "meuMetodo() - meuArquivo=" + meuArquivo);
			logger2.info("meuMetodo() - meuArquivo=" + meuArquivo);
		} else {
			Log.d(TAG, "meuMetodo() - meuArquivo retornou nulo");
		}

		// --------------------------------------------------------------
		// executa a activity que exibe a foto
		// --------------------------------------------------------------
		if (meuArquivo != null) {
			executaActivityExibeFoto(meuArquivo);
		}

		// TODO aqui é necessário a criação de uma thread pois a proxima operação é
		// muito demorada !!!
		// showAlert(msg);

		// Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

		File fileMeuArquivoPadrao = null;

		if (meuArquivo != null) {

			fileMeuArquivoPadrao = new File(meuArquivo);

		} else {
			// TODO: remover
			String meuArquivoPadrao = "/mnt/sdcard/Pictures/fotoevento/fotos/casa_320x240.png";
			fileMeuArquivoPadrao = new File(meuArquivoPadrao);

		}

		logger2.info("meuMetodo() - antes do envio do email");

		// Há uma foto com moldura que será enviada por email
		setEstado(2);

		// chama enviaEmail passando a Uri onde se encontra a foto

		Log.i(TAG, "meuMetodo() - Enviando email com foto em: " + Uri.fromFile(fileMeuArquivoPadrao));
		logger2.info("meuMetodo() - Enviando email com foto em: " + Uri.fromFile(fileMeuArquivoPadrao));

		try {

			// Envia o email com a foto
			enviaEmail(Uri.fromFile(fileMeuArquivoPadrao));

			// atualiza o nome do arquivo que possui a foto enviada por email
			mFilename = fileMeuArquivoPadrao.getAbsolutePath();

			Log.i(TAG, "mFilename=" + mFilename);

			// Envia "email" a redes sociais previamente cadastradas
			sendEmailRedesSociais(mFilename);

		} catch (Exception e) {

			Log.w(TAG, "meuMetodo() - Erro no envio do email", e);
			// logger.println("meuMetodo() - Erro no envio do email");
			logger2.info("meuMetodo() - Erro no envio do email");

		}

		Log.w(TAG, "meuMetodo() - fim");

	}

	/**
	 * Inicia o processo de envio de email.<br>
	 * 
	 * Verifica inicialmente se todas as condições necessárias estão satisfeitas
	 * para o envio do email com a foto anexada.<br>
	 * 
	 * Usa: mParticipante e mContratante
	 * 
	 * @param lastUri
	 *          Uri onde a foto está armazenada
	 * 
	 * @throws Exception
	 * 
	 */
	private void enviaEmail(Uri lastUri) throws Exception {

		Log.d(TAG, "*** enviaEmail() - lastUri=" + lastUri);

		// valida as informações para envio do email
		validaDadosEmail(lastUri);

		// carrega as preferências sobre o envio de email
		SharedPreferences emailPreferences = getSharedPreferences(Constantes.PREF_EMAIL, MODE_PRIVATE);

		if (emailPreferences == null) {
			Log.w(TAG, "enviaEmail() - SharedPreferences não foi encontrada.");
		}

		/**
		 * Assunto do email
		 * 
		 * Recupera o "subject" do email do arquivo de preferências
		 */
		String subject = emailPreferences.getString(Constantes.PREFERENCIAS_ASSUNTO, "Evento Inicial");

		/**
		 * Corpo do email
		 * 
		 * Recupera o "corpo" do email do arquivo de preferências
		 * 
		 */
		String body = emailPreferences.getString(Constantes.PREFERENCIAS_DESCRICAO, "Segue as informações sobre o evento");

		emailPreferences = null;

		// obtém o email do participante do evento
		String to = mParticipante.getEmail();

		// obtém o email do contratante do evento
		// ele será copiado em BCC no email enviado
		String bcc = mContratante.getEmail();

		// envia email usando JavaMail ao invés de uma intent

		Log.d(TAG, "to: " + to + ", bcc=" + bcc + ", subject=" + subject + ", body=" + body + ", lastUri=" + lastUri);

		boolean enviadoComSucesso = false;

		// Verifica se existe rede disponível no momento
		if (AndroidUtils.isNetworkingAvailable(this)) {

			enviadoComSucesso = sendEmailExternal(to, bcc, subject, body, lastUri);
			// boolean enviadoComSucesso = sendEmailExternal2(to, bcc, subject,
			// body,
			// lastUri);

			Log.w(TAG, "enviaEmail() - EMAIL ENVIADO COM SUCESSO: " + enviadoComSucesso);

		}

		if (enviadoComSucesso) {
			// processa o resultado do envio do email com sucesso
			aposEnviarEmail();
		} else {
			// vai para o estado final
			estadoFinal();
		}

	}

	/**
	 * Valida se existe informações suficientes para envio do email.<br>
	 * <br>
	 * 
	 * As seguintes condições são verificadas :<br>
	 * <ul>
	 * <li>Existência de um Contratante;
	 * <li>Existência de um Participante;
	 * <li>Existência de uma Foto;
	 * </ul>
	 * 
	 * @param lastUri
	 *          URI da foto
	 */
	private void validaDadosEmail(Uri lastUri) {

		Log.d(TAG, "validaDadosEmail() - lastUri=" + lastUri);

		if (getEstado() < 2) {
			Log.w(TAG, "validaDadosEmail() - Foto não foi tirada");

		}

		if (mParticipante == null) {
			Log.w(TAG, "validaDadosEmail() - Não há informações sobre o participante");

		}

		if (mContratante == null) {
			Log.w(TAG, "validaDadosEmail() - Não há informações sobre o contratante");

		}

		if (lastUri == null) {
			Log.w(TAG, "validaDadosEmail() - Não há foto");

		}

		Log.d(TAG, "validaDadosEmail() - fim");

	}

	/**
	 * Envia email usando a biblioteca externa (JavaMail) de envio de email.<br>
	 * 
	 * O contratante do evento sempre receberá (em cópia invisível ao
	 * destinatário) o email enviado ao participante do evento.<br>
	 * 
	 * Obs: usa a variável membro mailServer.<br>
	 * 
	 * @param emailParticipante
	 *          Email do participante do evento
	 * 
	 * @param emailContratante
	 *          Email do contratante do evento
	 * 
	 * @param subject
	 *          Assunto tratado pelo email
	 * 
	 * @param text
	 *          Corpo do email (mensagem)
	 * 
	 * @param imageUri
	 *          Uri da foto tirada
	 * 
	 * @return true se email enviado com sucesso ou false em caso de erro.
	 * 
	 * @throws Exception
	 *           erro no envio do email
	 */
	private boolean sendEmailExternal(String emailParticipante, String emailContratante, String subject, String text, Uri imageUri)
			throws Exception {

		Log.d(TAG, "sendEmailExternal() - inicio");

		// cria um file que será adicionado (anexado) ao email
		File f = new File(imageUri.getPath());

		// TODO object mailServer começa ser configurado em initEmailConfig()
		// host, port, from, debugabble, auth, ssl

		try {

			if (mailServer != null) {

				// anexa o arquivo ao email
				// mailServer.addAttachment(f.getAbsolutePath());
				mailServer.setFilename(f.getAbsolutePath());

				Log.d(TAG, "sendEmailExternal() - foto anexada ao email.");

			} else {
				Log.w(TAG, "sendEmailExternal() - mailServer está nulo.");
			}

		} catch (Exception e) {

			Log.w(TAG, "sendEmailExternal() - não foi encontrado o anexo contendo a foto", e);

			logger2.info("sendEmailExternal() - não foi encontrado o anexo contendo a foto");

			return false;

		}

		// From:
		mailServer.setFrom(emailContratante);

		// To:
		mailServer.setTo(new String[] { emailParticipante });

		// Bcc:
		mailServer.setBcc(new String[] { emailContratante });

		// Subject
		mailServer.setSubject(subject);

		// Body
		mailServer.setBody(text);

		Log.d(TAG, "sendEmailExternal() - " + mailServer);

		boolean enviou = false;

		try {

			// envia o email
			enviou = mailServer.send();

			Log.d(TAG, "sendEmailExternal() - enviou: " + enviou);

		} catch (Exception e) {

			enviou = false;

			Log.w(TAG, "sendEmailExternal() - falha no envio do email", e);
			Log.w(TAG, "sendEmailExternal() - getMessage(): " + e.getMessage());

		}

		// resultado do envio do email
		return enviou;

	}

	/**
	 * Verifica se as mensagens devem ser postadas nas redes sociais<br>
	 * 
	 * Usa mEvento (informações sobre o evento).
	 * 
	 */
	private void sendEmailRedesSociais(String filename) {

		Log.w(TAG, "sendEmailRedesSociais() - Inicio do método ...");

		if (mEvento == null) {
			Log.w(TAG, "sendEmailRedesSociais() - Não foi possível obter os dados do evento.");
			return;
		}

		// TODO talvez pudesse ser feito após o envio do email ???

		Log.d(TAG, "sendEmailRedesSociais() - mEvento.isEnviaFacebook()=" + mEvento.isEnviaFacebook());
		Log.d(TAG, "sendEmailRedesSociais() - mEvento.isEnviaTwitter()=" + mEvento.isEnviaTwitter());

		if (filename == null || filename.equals("")) {
			Log.w(TAG, "O nome do arquivo é nulo ou está vazio !!!");
		}

		if (mEvento.isEnviaFacebook()) {

			// enviar foto ao Facebook
			// TODO qual texto ???

			Log.i(TAG, "sendEmailRedesSociais() - Envia foto ao Facebook ...");
			sendMsgFacebook(filename);

		}

		if (mEvento.isEnviaTwitter()) {

			Log.i(TAG, "sendEmailRedesSociais() - Envia foto ao Twitter ...");

			String text = mPreferences.getString(Constantes.PREFERENCIAS_TEXTO_TWITTER, "");

			sendMsgTwitter(filename, text);

		}

	}

	/**
	 * sendMsgFacebook(String filename)
	 * 
	 * @param filename
	 *          nome do arquivo onde se encontra a foto
	 * 
	 */
	private void sendMsgFacebook(String filename) {

		Log.i(TAG, "-------------------------------------------------");
		Log.i(TAG, "sendMsgFacebook() - filename=" + filename);
		Log.i(TAG, "-------------------------------------------------");

		Intent intent = new Intent(this, FacebookActivity2.class);

		intent.putExtra("br.com.mltech.filename", filename);

		startActivityForResult(intent, ACTIVITY_FACEBOOK);

	}

	/**
	 * sendMsgTwitter(String filename, String text)
	 * 
	 * @param filename
	 *          nome do arquivo
	 * @param text
	 *          texto que acompanha a foto
	 * 
	 */
	private void sendMsgTwitter(String filename, String text) {

		Log.i(TAG, "-------------------------------------------------");
		Log.i(TAG, "sendMsgTwitter() - filename=" + filename);
		Log.i(TAG, "-------------------------------------------------");

		Intent intent = new Intent(this, TwitterActivity.class);

		Log.d(TAG, "sendMsgTwitter() - filename: " + filename);
		Log.d(TAG, "sendMsgTwitter() - text: " + text);

		intent.putExtra("br.com.mltech.filename", filename);
		intent.putExtra("br.com.mltech.text", text);

		startActivityForResult(intent, ACTIVITY_TWITTER);

	}

	
	/**
	 * Executa ações após o envio do email com sucesso.<br>
	 * 
	 * Exibe a mensagem de envio de email com sucesso.<br>
	 * 
	 * Atualiza o estado da aplicação.<br>
	 * 
	 * Finaliza a ação. <br>
	 * 
	 */
	private void aposEnviarEmail() {

		Log.d(TAG, "aposEnviarEmail() - email enviado com sucesso");

		logger2.info("aposEnviarEmail() - email enviado com sucesso");

		// mensagem exibida após envio de email
		Toast.makeText(this, "Email enviado com sucesso", Toast.LENGTH_LONG).show();

		// Atualiza o estado da máquina de estado
		setEstado(3);

		// Processa o próximo estado
		estadoFinal();

	}

	/**
	 * Executa a activity que exibe uma foto.<br>
	 * 
	 * Envia uma intent com a URI da foto que será exibida.
	 * 
	 * @param arquivo
	 *          nome completo do arquivo onde a foto está localizada.
	 * 
	 */
	private void executaActivityExibeFoto(String arquivo) {

		File f = new File(arquivo);
		Uri uri = Uri.fromFile(f);

		Intent intent = new Intent(this, ExibeFotoActivity.class);
		intent.setData(uri);
		startActivity(intent);

	}

	/**
	 * É o ponto final da activity onde é definido o resultado final da execução
	 * da activity.<br>
	 * 
	 * Representa o estado final da máquina de estado.<br>
	 * 
	 * É também o responsável pela finalização da activity, estabelecendo seu
	 * resultado.<br>
	 * 
	 * Se o estado final foi atingido então o processo correu segundo esperado.<br>
	 * 
	 */
	private void estadoFinal() {

		// Obtem as informações sobre a Intent "chamadora"
		Intent intent = getIntent();

		// Obtém o estado corrente da máquina de estado
		int estadoCorrente = getEstado();

		if (estadoCorrente == 3) {

			// estado final atingido com sucesso
			Log.i(TAG, "estadoFinal() - final do processamento");

			// retorna as informações sobre o participante e sobre sua participação
			intent.putExtra(PARTICIPANTE, mParticipante);
			intent.putExtra(PARTICIPACAO, mParticipacao);
			intent.putExtra("br.com.mltech.numFotosPolaroid", numFotosPolaroid);
			intent.putExtra("br.com.mltech.numFotosCabine", numFotosCabine);

			intent.putExtra("br.com.mltech.result", "OK");

			// estabelece o resultado da execução da Activity
			setResult(RESULT_OK, intent);

		} else {

			Toast.makeText(this, "Falha no processo. Estado atual: " + estadoCorrente, Toast.LENGTH_SHORT).show();

			// estado final atingido porém houve falha
			Log.w(TAG, "estadoFinal() - não foi possível chegar ao final do processamento.");
			// logger.println("estadoFinal() - não foi possível chegar ao final do processamento.");
			logger2.info("estadoFinal() - não foi possível chegar ao final do processamento.");

			intent.putExtra("br.com.mltech.result", "NOT_OK");

			// estabelece o resultado da execução da Activity
			setResult(RESULT_CANCELED, intent);

		}

		// TODO verificar se sempre é necessário retornar para o modo Portrait
		// atualiza o modo da tela
		// atualizaModoTela(Configuration.ORIENTATION_PORTRAIT);
		atualizaModoTela(modoTela);

		// Termina a execução da Activity responsável por tirar e enviar uma
		// foto
		finish();

	}

	/**
	 * Atualiza o estado da uma máquina de estados.
	 * 
	 * @param e
	 *          novo estado (próximo estado)
	 */
	private void setEstado(int e) {

		Log.i(TAG, "----------------------------------------------------------");
		Log.i(TAG, "Transição do estado: " + mEstado + " para o estado: " + e);
		Log.i(TAG, "----------------------------------------------------------");

		mEstado = e;

	}

	/**
	 * Obtém o estado atual da máquina de estados.
	 * 
	 * @return Um inteiro contendo o estado da state machine.
	 * 
	 */
	private int getEstado() {

		return mEstado;
	}

	/**
	 * Trata o resultado da execução das Activities.<br>
	 * 
	 * Processa o resultado da execução das Activities.<br>
	 * 
	 * É chamado quando a activity lançada retorna, dando a você o requestCode com
	 * o qual você iniciou, o resultCode retornado e qualquer dado adicional
	 * resultado do processamento da activity. O resultCode será RESULT_CANCELED
	 * se a activity retornar explicitamente esse valor, não retornar nenhum valor
	 * ou haver algum crash dureante a operação.<br>
	 * 
	 * Esse método será chamado imediatamente antes da execução do onResume()
	 * quando sua activity é reinicializada.<br>
	 * 
	 * Called when an activity you launched exits, giving you the requestCode you
	 * started it with, the resultCode it returned, and any additional data from
	 * it. The resultCode will be RESULT_CANCELED if the activity explicitly
	 * returned that, didn't return any result, or crashed during its operation.<br>
	 * 
	 * You will receive this call immediately before onResume() when your activity
	 * is re-starting.<br>
	 * 
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		Log.i(TAG, "------------------------------------------------------------------------------------------------------------");
		Log.i(TAG, "onActivityResult(request (" + requestCode + ") " + getActivityName(requestCode) + ", result=" + resultCode
				+ ", data " + data + ") ...");
		Log.i(TAG, "------------------------------------------------------------------------------------------------------------");

		String sNomeArquivo = null;

		switch (requestCode) {

		case ACTIVITY_PARTICIPANTE:

			resultActivityParticipante(resultCode, data);
			break;

		case ACTIVITY_PARTICIPACAO:

			resultActivityParticipacao(resultCode, data);
			break;

		case TIRA_FOTO_POLAROID:

			numFotosTiradas++;

			sNomeArquivo = processaActivityResultPolaroid(resultCode, data);

			// atualiza a informação sobre a localização da foto
			mParticipacao.setNomeArqFoto(sNomeArquivo);
			numFotosPolaroid++;

			meuMetodo(requestCode, sNomeArquivo);
			break;

		case TIRA_FOTO_CABINE:

			numFotosTiradas++;

			sNomeArquivo = processaActivityResultCabine(resultCode, data);

			if (sNomeArquivo != null) {
				// atualiza a informação sobre a localização da foto
				mParticipacao.setNomeArqFoto(sNomeArquivo);
				numFotosCabine++;

				meuMetodo(requestCode, sNomeArquivo);

			} else {
				Log.w(TAG, "onActivityResult() - foto cabine retornou nula");
			}

			break;

		case ACTIVITY_FACEBOOK:
			resultActivityFacebook(resultCode, data);
			break;

		case ACTIVITY_TWITTER:
			resultActivityTwitter(resultCode, data);
			break;

		default:
			Log.w(TAG, "onActivityResult() - Erro ... requestCode: " + requestCode + " não pode ser processado");
			break;

		}

	}

	/**
	 * Exibe uma mensagem numa caixa de diálogo. Use o botão para fechar a janela.
	 * 
	 * @param msg
	 *          Mensagem que será exibida na caixa de diálogo exibida.
	 * 
	 */
	private Dialog showAlert(String msg) {

		if (msg == null) {
			// retorna se a mensagem for nula
			return null;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(msg).setNeutralButton("Fechar", null);

		// Cria uma janela a partir dos dados solicitados no builder
		AlertDialog alert = builder.create();

		// exibe a janela
		alert.show();

		// retorna a instância da ciaxa de diálogos criada (para que possa ser
		// destruída).
		return alert;

	}

	/**
	 * Carrega arquivo de moldura.<br>
	 * 
	 * @param arquivoMoldura
	 *          Nome do arquivo onde se encontra o bitmap com a moldura.
	 * 
	 * @return um bitmap com o arquivo contendo a moldura ou null no caso de algum
	 *         problema.
	 * 
	 */
	private Bitmap leArquivoMoldura(String arquivoMoldura) {

		Log.d(TAG, "leArquivoMoldura() - arquivoMoldura: [" + arquivoMoldura + "].");

		if ((arquivoMoldura != null) && (arquivoMoldura.equals(""))) {
			Log.w(TAG, "leArquivoMoldura() - o nome do arquivo está vazio.");
			return null;
		}

		File moldura = new File(arquivoMoldura);

		if (!moldura.exists()) {
			Log.w(TAG, "leArquivoMoldura() - arquivoMoldura: [" + arquivoMoldura + "] não existe.");
			return null;
		}

		// lê o bitmap contendo a moldura do arquivo.
		Bitmap bmMoldura = ManipulaImagem.getBitmapFromFile(moldura);

		if (bmMoldura == null) {

			Log.w(TAG, "leArquivoMoldura() - arquivo contento a moldura está vazio.");
			return null;

		} else {
			Log.v(TAG, "leArquivoMoldura() - largura x altura da moldura: " + ManipulaImagem.getStringBitmapSize(bmMoldura));

			return bmMoldura;

		}

	}

	/**
	 * Obtém um parâmetro da lista de preferências.
	 * 
	 * @param sharedPreferencesName
	 *          nome do arquivo de preferências
	 * @param attribute
	 *          nome do atributo guardado no arquivo
	 * 
	 * @return o valor do atributo solicitado ou uma string vazia caso haja algum
	 *         erro (o parâmetro não foi encontrado)
	 * 
	 */
	private String getSharedPreference(String sharedPreferencesName, String attribute) {

		// TODO o problema desse método é o acesso ao disco toda veze que buscamos
		// um parâmetro.

		SharedPreferences preferences = getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);

		String sValue = preferences.getString(attribute, "");

		preferences = null;

		return sValue;

	}

	/**
	 * Exibe o valor de alguns atributos da classe (se forem diferente de null).<br>
	 * 
	 * Apenas os atributos não nulos serão exibidos.
	 * 
	 */
	private void showVariables() {

		Log.v(TAG, "=================================");
		Log.v(TAG, "showVariables() - Variáveis de classe:");

		logger2.info("showVariables()");

		if (mContratante != null) {
			// Log.v(TAG, "  mContratante=" + mContratante);
		}

		if (mEvento != null) {
			// Log.v(TAG, "  mEvento=" + mEvento);
		}

		if (mParticipante != null) {
			// Log.v(TAG, "  mParticipante=" + mParticipante);
		}

		if (mParticipacao != null) {
			// Log.v(TAG, "  mParticipacao=" + mParticipacao);
		}

		// if (xUri != null) {
		// Log.v(TAG, "  xUri=" + xUri);
		// }

		Log.v(TAG, "  mEstado=" + mEstado + ", mContador=" + mContador);
		Log.v(TAG, "  mCurrentCamera=" + currentCamera);

		showClassVariables();

		Log.v(TAG, "=================================");

	}

	/**
	 * Exibe o valor de algumas variáveis de classe previamente selecionadas no
	 * log da aplicação.
	 * 
	 */
	private void showClassVariables() {

		Log.v(TAG, "    showClassVariables() - file: " + file);
		Log.v(TAG, "    showClassVariables() - outputFileUri: " + outputFileUri);
		Log.v(TAG, "    showClassVariables() - Contador: " + contador + ", i=" + i);
		Log.v(TAG, "    showClassVariables() - numCreate: " + numCreate + ", numRestart: " + numRestart);

	}

	// ---------------------------------------------------------------------------
	//
	//
	// ---------------------------------------------------------------------------

	/**
	 * Formata uma foto no formato Polaroid.<br>
	 * 
	 * Recebe o endereço de uma foto;<br>
	 * Redimensiona a foto;<br>
	 * Insere a moldura e transforma a foto no tamanho 9x11<br>
	 * 
	 * @param uriFotoOriginal
	 *          Uri da foto original
	 * 
	 * @return Um bitmap contendo a foto com a moldura ou null em caso de algum
	 *         erro.
	 * 
	 */
	private Bitmap formatarPolaroid(Uri uriFotoOriginal) {

		/**
		 * TODO Esse método gera dois arquivos contendo fotos intermediários que
		 * poderiam ser descartadas
		 * 
		 */

		Log.d(TAG, "formatarPolaroid() - uriFotoOriginal: " + uriFotoOriginal);

		// Cria um bitmap a partir da Uri da foto.
		Bitmap bmFotoOriginal = ManipulaImagem.criaBitmap(uriFotoOriginal);

		if (bmFotoOriginal == null) {
			Log.w(TAG, "formatarPolaroid() - Não foi possível criar o bitmap com a foto original: " + bmFotoOriginal);
			return null;
		}

		bmFotoOriginal = getReflectedBitmapAxisY(bmFotoOriginal);

		Log.d(TAG,
				"formatarPolaroid() -  tamanho da foto original - w=" + bmFotoOriginal.getWidth() + ", h=" + bmFotoOriginal.getHeight());

		if (ManipulaImagem.isLandscape(bmFotoOriginal)) {
			// foto possui a largura maior que a altura
			Log.d(TAG, "formatarPolaroid() - Foto landscape");
		} else {
			// foto possui a altura maior que a largura
			Log.d(TAG, "formatarPolaroid() - Foto portrait");
		}

		// redimensiona a foto original para 9x12 para manter a proporção 3:4
		// Bitmap bmFoto9x12 = ManipulaImagem.getScaledBitmap2(bmFotoOriginal, 340,
		// 454);
		Bitmap bmFoto9x12 = ManipulaImagem.getScaledBitmap2(bmFotoOriginal, 454, 340);

		if (bmFoto9x12 == null) {
			Log.d(TAG, "formatarPolaroid() - falha no redimensionamento da foto - bmFoto9x12: " + bmFoto9x12);
			return null;
		}

		// Define o nome da foto redimensionada
		String nomeArquivo = FileUtils.getBasePhotoDirectory() + File.separator + FileUtils.getFilename(uriFotoOriginal) + "_9x12.png";

		// Grava a foto redimensionada em um arquivo "intermediário"
		boolean gravou = ManipulaImagem.gravaBitmapArquivo(bmFoto9x12, nomeArquivo);

		if (!gravou) {
			// foto não pode ser gravada. Retorna.
			Log.d(TAG, "formatarPolaroid() - foto não pode ser gravada");
			return null;
		}

		//
		// redimensiona a foto 9x12 para 8x8, isto é, copia uma "janela" 8x8 da foto
		//
		// Create a default Options object, which if left unchanged will give the
		// same result
		// from the decoder as if null were passed.
		//
		Options options = new Options();

		// TODO verificar qual será a "janela" da foto
		Rect rect = new Rect(0, 0, 312, 312);

		// Obtem um bitmap com a foto redimensionada para 8x8
		Bitmap bmFoto8x8 = ManipulaImagem.getBitmapRegion(nomeArquivo, rect, options);

		nomeArquivo = FileUtils.getBasePhotoDirectory() + FileUtils.getFilename(uriFotoOriginal) + "_8x8.png";

		// Aplica a moldura a foto
		Bitmap bmFotoComMoldura = ManipulaImagem.overlay4(bmFoto8x8, mBitmapMolduraPolaroid);

		// o bitmap contendo a foto com a moldura aplicada
		return bmFotoComMoldura;

	}

	/**
	 * Cria uma foto a partir da combição de três outras fotos. As fotos são
	 * colocadas na vertical, isto é, uma abaixo da outra.
	 * 
	 * <pre>
	 * |--------|
	 * |        |
	 * | foto1  |
	 * |        |
	 * |        |
	 * |--------|
	 * |        |
	 * | foto2  |
	 * |        |
	 * |        |
	 * |--------|
	 * |        |
	 * | foto3  |
	 * |        |
	 * |        |
	 * |--------|
	 * </pre>
	 * 
	 * 
	 * @param bmFoto1
	 *          primeira foto
	 * 
	 * @param bmFoto2
	 *          segunda foto
	 * 
	 * @param bmFoto3
	 *          terceira foto
	 * 
	 * @param bmMoldura
	 *          bitmap contendo a moldura
	 * 
	 * @return uma nova foto (bitmap) contendo as três fotos e a moldura ou null
	 *         em caso de erro.
	 */
	private static Bitmap processaFotoFormatoCabine3(Bitmap bmFoto1, Bitmap bmFoto2, Bitmap bmFoto3, Bitmap bmMoldura) {

		if (bmMoldura == null) {
			Log.w(TAG, "processaFotoFormatoCabine3() - Moldura formato Cabine está vazia !");
			return null;
		}

		// executa o merge das três fotos
		Bitmap bmImgJoin = mergeFotosCabine(bmFoto1, bmFoto2, bmFoto3);

		if (bmImgJoin == null) {
			Log.w(TAG, "processaFotoFormatoCabine3() - não foi possível fazer o merge das três fotos !");
			return null;
		}

		// Obtém uma imagem redimensionada
		Bitmap scaledBitmap = ManipulaImagem.getScaledBitmap2(bmImgJoin, 113, 453);

		if (scaledBitmap == null) {
			//
			Log.w(TAG, "processaFotoFormatoCabine3() - não foi possível redimensionar a foto");
			return null;
		}

		// combina a foto com a moldura
		Bitmap fotoComMoldura = ManipulaImagem.aplicaMolduraFoto(scaledBitmap, bmMoldura);

		return fotoComMoldura;

	}

	/**
	 * Cria uma nova foto (bitmap) a partir de três fotos recebidas.<br>
	 * 
	 * <pre>
	 * |foto1|
	 * |foto2|
	 * |foto3|
	 * </pre>
	 * 
	 * @param bmFoto1
	 *          primeira foto
	 * 
	 * @param bmFoto2
	 *          segunda foto
	 * 
	 * @param bmFoto3
	 *          terceira foto
	 * 
	 * @return o bitmap contendo as três fotos ou null em caso de algum erro
	 */
	private static Bitmap mergeFotosCabine(Bitmap bmFoto1, Bitmap bmFoto2, Bitmap bmFoto3) {

		if (bmFoto1 == null) {
			Log.w(TAG, "mergeFotosCabine() - Foto1 está vazia !");
			throw new IllegalArgumentException("mergeFotosCabine() - Foto1 está vazia !");
		}

		if (bmFoto2 == null) {
			Log.w(TAG, "mergeFotosCabine() - Foto2 está vazia !");
			throw new IllegalArgumentException("mergeFotosCabine() - Foto2 está vazia !");
		}

		if (bmFoto3 == null) {
			Log.w(TAG, "mergeFotosCabine() - Foto3 está vazia !");
			throw new IllegalArgumentException("mergeFotosCabine() - Foto3 está vazia !");
		}

		// =========================================================================
		// Cria um novo bitmap a partir da composição das três fotos.
		// As fotos serão organizadas na vertical, isto é, uma nova foto
		// será colocada abaixo da outra.
		// =========================================================================
		Bitmap bmImgJoin = ManipulaImagem.verticalJoin(bmFoto1, bmFoto2, bmFoto3);

		if (bmImgJoin != null) {

			Log.i(TAG, "mergeFotosCabine() - Imagens foram juntadas com sucesso");
			Log.v(TAG, "mergeFotosCabine() -  ==> Tamanho da foto após join: " + ManipulaImagem.getStringBitmapSize(bmImgJoin));

		} else {

			Log.w(TAG, "mergeFotosCabine() - Erro no merge das três fotos");
			return null;

		}

		// bitmap contendo uma única foto criada a partir de três fotos.
		return bmImgJoin;

	}

	// ---------------------------------------------------------------------------
	// Métodos não usados
	// TODO avaliar a remoção dos mesmos
	// ---------------------------------------------------------------------------

	/**
	 * Retorna o nome da Activity dado seu requestCode.<br>
	 * 
	 * @param requestCode
	 *          código que identifica o retorna da Activity
	 * 
	 * @return Retorna o nome da Activity
	 */
	private String getActivityName(int requestCode) {

		String nome = null;

		switch (requestCode) {
		case TIRA_FOTO:
			nome = "TIRA_FOTO";
			break;

		case TIRA_FOTO_POLAROID:
			nome = "TIRA_FOTO_POLAROID";
			break;

		case TIRA_FOTO_CABINE:
			nome = "TIRA_FOTO_CABINE";
			break;

		case ACTIVITY_PARTICIPANTE:
			nome = "ACTIVITY_PARTICIPANTE";
			break;

		case ACTIVITY_PARTICIPACAO:
			nome = "ACTIVITY_PARTICIPACAO";
			break;

		case ACTIVITY_FACEBOOK:
			nome = "ACTIVITY_FACEBOOK";
			break;

		case ACTIVITY_TWITTER:
			nome = "ACTIVITY_TWITTER";
			break;

		default:
			nome = "Activity não encontrada.";
			break;
		}

		return nome;

	}

	/**
	 * Verifica se todos os argumentos enviado ao email são não nulos.<br>
	 * 
	 * @param mEmail
	 *          identificador da conta de email
	 * 
	 * @param mSenha
	 *          senha da conta de email
	 * 
	 * @param mHost
	 *          endereço do servidor SMTP
	 * 
	 * @param mPort
	 *          porta do servidor SMTP
	 * 
	 * @param mRemetente
	 *          remetente do email
	 * 
	 */
	private void validaArgumentosEmail(String mEmail, String mSenha, String mHost, String mPort, String mRemetente) {

		if ((mEmail != null) && (mEmail.equals(""))) {
			throw new IllegalArgumentException("Usuário da conta de email não foi fornecido");
		}

		if ((mSenha != null) && (mSenha.equals(""))) {
			throw new IllegalArgumentException("Senha da conta de email não foi fornecida");
		}

		if ((mHost != null) && (mHost.equals(""))) {
			throw new IllegalArgumentException("Endereço do servidor de email (SMTP) não foi fornecido");
		}

		if ((mPort != null) && (mPort.equals(""))) {
			throw new IllegalArgumentException("Porta do servidor de email (SMTP) não foi fornecida");
		}

		// TODO validar se a porta é um nº válido entre 0 e 1023 ???

		if ((mRemetente != null) && (mRemetente.equals(""))) {
			throw new IllegalArgumentException("Endereço do remetente do email não foi fornecido");
		} else if (!isValidInternetAddress(mRemetente, "Remetente")) {
			throw new IllegalArgumentException("Endereço do remetente do email não é válido");
		}

	}

	/**
	 * Verifica se o endereço de email fornecido é válido (sintaticamente).
	 * 
	 * @param emailAddress
	 *          Endereço do email do remetente
	 * 
	 * @param mMsg
	 *          nome do campo
	 * 
	 * @return true se o endereço de email fornecido for válido ou false caso
	 *         contrário.
	 * 
	 */
	@SuppressWarnings("unused")
	private boolean isValidInternetAddress(String emailAddress, String mMsg) {

		try {
			InternetAddress address = new InternetAddress(emailAddress);
		} catch (javax.mail.internet.AddressException e) {

			String mensagem = mMsg + " não é válido (posição: " + e.getPos() + ", ref: " + e.getRef() + ")";

			Log.w(TAG, mensagem, e);

			return false;

		}
		return true;
	}

	/**
	 * Atualiza a orientação atual da tela para outro modo (se necessário).<br>
	 * 
	 * Configuration.ORIENTATION_LANDSCAPE ou Configuration.ORIENTATION_PORTRAIT
	 * 
	 * @param novaOrientacao
	 *          nova orientação
	 */
	private void atualizaModoTela(int novaOrientacao) {

		// obtém a orientação atual da tela
		int orientacaoAtual = this.getResources().getConfiguration().orientation;

		// exibe qual é a orientação atual da tela
		Log.d(TAG, "atualizaModoTela() - Orientação atual: " + orientacaoAtual + " - " + getScreenOrientation(orientacaoAtual));

		logger2.info("atualizaModoTela() - Orientação atual: " + orientacaoAtual + " - " + getScreenOrientation(orientacaoAtual));

		if (novaOrientacao != orientacaoAtual) {
			// muda a orientação
			this.setRequestedOrientation(novaOrientacao);
			// exibe a nova orientação
			Log.d(TAG, "atualizaModoTela() - nova orientação: " + novaOrientacao + " - " + getScreenOrientation(novaOrientacao));
			logger2.info("atualizaModoTela() - nova orientação: " + novaOrientacao + " - " + getScreenOrientation(novaOrientacao));
		}

	}

	/**
	 * Obtém o nome correspondente a orientação da tela: Landscape ou Portrait<br>
	 * 
	 * @param orientacao
	 *          Configuration.ORIENTATION_LANDSCAPE ou
	 *          Configuration.ORIENTATION_PORTRAIT
	 * 
	 * @return uma string com o nome da orientação da tela ou "Não suportado"
	 *         indicando que a orientação não é suportada.
	 * 
	 */
	private static String getScreenOrientation(int orientacao) {

		String s = null;

		if (orientacao == Configuration.ORIENTATION_LANDSCAPE) {
			s = "Landscape";
		} else if (orientacao == Configuration.ORIENTATION_PORTRAIT) {
			s = "Portrait";
		} else {
			s = "Não suportado";
		}

		return s;

	}

	/**
	 * Cria um bitmap refletido em relação ao eixo y
	 * 
	 * @param bitmap
	 *          bitmap original
	 * 
	 * @return bitmap invertido com relação ao eixo y
	 */
	public Bitmap getReflectedBitmapAxisY(Bitmap bitmap) {

		Matrix matrix = new Matrix();

		matrix.reset();

		matrix.setValues(new float[] { -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f });

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

		return resizedBitmap;

	}

}
