package br.com.mltech;

/**
 * Constantes.java
 * 
 * @author maurocl
 * 
 */
public interface Constantes {

  // Defini��es de tipos de fotos
  public static final int TIPO_FOTO_POLAROID = 1;

  public static final int TIPO_FOTO_CABINE = 2;

  // Defini��o de constantes para o efeito de cores
  public static final int CORES = 11;

  // Defini��o de constantes para o efeito P&B
  public static final int PB = 12;

  //Defini��o de nomes de SharedPreferences
  public static final String PREF_EMAIL = "pref_email";

  //
  public static final String CONTRATANTE = "br.com.mltech.contratante";

  public static final String EVENTO = "br.com.mltech.evento";

  public static final String PARTICIPANTE = "br.com.mltech.participante";

  public static final String PARTICIPACAO = "br.com.mltech.participacao";

  public static final String LISTA = "br.com.mltech.lista";

  public static final String USUARIO = "br.com.mltech.usuario";

  public static final String SENHA = "br.com.mltech.senha";

  public static final String USUARIOVALIDADO = "br.com.mltech.usuarioValidado";

  // TODO criar um diret�rio inicial, um lugar para configura��o ...
  //public static final String PATH_MOLDURAS = "/mnt/sdcard/Pictures/fotoevento/molduras/";

  //public static final String PATH_FOTOS = "/mnt/sdcard/Pictures/fotoevento/fotos/";

  // Defini��o da Activies chamadas a partir de DummyActivity
  public static final int ACTIVITY_TIRA_FOTO_3 = 113;

  public static final int ACTIVITY_CHOOSER = 150;

  public static final int ACTIVITY_PARTICIPANTE = 102;

  public static final int ACTIVITY_PARTICIPACAO = 107;
  
  public static final int ACTIVITY_FACEBOOK = 197;
  public static final int ACTIVITY_TWITTER = 198;

  public static final int TIRA_FOTO = 200;

  public static final int TIRA_FOTO_POLAROID = 201;

  public static final int TIRA_FOTO_CABINE = 202;

  //-----------------------------------------------------------------
  // Defini��o de constantes
  // -----------------------------------------------------------------

  // Contratante
  public static final String CONTRATANTE_NOME = "contratante_nome";

  public static final String CONTRATANTE_EMAIL = "contratante_email";

  public static final String CONTRATANTE_TELEFONE = "contratante_telefone";

  // Evento
  public static final String EVENTO_NOME = "evento_nome";

  public static final String EVENTO_EMAIL = "evento_email";

  public static final String EVENTO_ENDERECO = "evento_endereco";

  public static final String EVENTO_CIDADE = "evento_cidade";

  public static final String EVENTO_ESTADO = "evento_estado";

  public static final String EVENTO_CEP = "evento_cep";

  public static final String EVENTO_DATA = "evento_data";

  public static final String EVENTO_BORDA_POLAROID = "evento_borda_polaroid";

  public static final String EVENTO_BORDA_CABINE = "evento_borda_cabine";

  public static final String EVENTO_TELEFONE = "evento_telefone";

  public static final String EVENTO_ENVIA_FACEBOOK = "evento_envia_facebook";

  public static final String EVENTO_ENVIA_TWITTER = "evento_envia_twitter";

  public static final String EVENTO_PARAM1 = "evento_param1";

  public static final String EVENTO_PARAM2 = "evento_param2";

  public static final String EVENTO_PARAM3 = "evento_param3";

  public static final String EVENTO_PARAM4 = "evento_param4";

  public static final String EVENTO_PARAM5 = "evento_param5";

  // Prefer�ncias

  public static final String PREFERENCIAS_ASSUNTO = "preferencias_assunto";

  public static final String PREFERENCIAS_DESCRICAO = "preferencias_descricao";

  public static final String PREFERENCIAS_TEXTO_FACEBOOK = "preferencias_texto_facebook";

  public static final String PREFERENCIAS_TEXTO_TWITTER = "preferencias_texto_twitter";

  public static final String PREFERENCIAS_URL_IMAGEM = "preferencias_url_imagem";

  public static final String PREFERENCIAS_NUM_CAMERA_FRONTAL = "preferencias_num_camera_frontal";

  public static final String PREFERENCIAS_USUARIO_EMAIL = "preferencias_usuario_email";

  public static final String PREFERENCIAS_SENHA_EMAIL = "preferencias_senha_email";

  public static final String PREFERENCIAS_SERVIDOR_SMTP = "preferencias_servidor_smtp";

  public static final String PREFERENCIAS_SERVIDOR_SMTP_PORTA = "preferencias_servidor_smtp_porta";

  public static final String PREFERENCIAS_REMETENTE_EMAIL = "preferencias_remetente_email";

  public static final String PREFERENCIAS_EMAIL_DEBUG = "preferencias_email_debug";

  public static final String PREFERENCIAS_EMAIL_AUTH = "preferencias_email_auth";

  public static final String PREFERENCIAS_EMAIL_SSL = "preferencias_email_ssl";
  
  //
  // Localiza��o do arquivo de Log da aplica��o
  //
  public static final String APP_LOG="/mnt/sdcard/Pictures/logger2.log";

}