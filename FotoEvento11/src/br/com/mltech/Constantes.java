package br.com.mltech;

/**
 * Constantes.java
 * 
 * @author maurocl
 * 
 */
public interface Constantes {

  // Definições de tipos de fotos
  public static final int TIPO_FOTO_POLAROID = 1;

  public static final int TIPO_FOTO_CABINE = 2;

  // Definição de constantes para o efeito de cores
  public static final int CORES = 11;

  // Definição de constantes para o efeito P&B
  public static final int PB = 12;

  //Definição de nomes de SharedPreferences
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

  // TODO criar um diretório inicial, um lugar para configuração ...
  public static final String PATH_MOLDURAS = "/mnt/sdcard/Pictures/fotoevento/molduras/";

  public static final String PATH_FOTOS = "/mnt/sdcard/Pictures/fotoevento/fotos/";

  // Definição da Activies chamadas a partir de DummyActivity
  public static final int ACTIVITY_TIRA_FOTO_3 = 113;

  public static final int ACTIVITY_CHOOSER = 150;

  public static final int ACTIVITY_PARTICIPANTE = 102;

  //-----------------------------------------------------------------
  // Definição de constantes
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

  // Preferências

  public static final String PREFERENCIAS_ASSUNTO = "preferencias_assunto";

  public static final String PREFERENCIAS_DESCRICAO = "preferencias_descricao";

  public static final String PREFERENCIAS_TEXTO_FACEBOOK = "preferencias_texto_facebook";

  public static final String PREFERENCIAS_TEXTO_TWITTER = "preferencias_texto_twitter";

  public static final String PREFERENCIAS_URL_IMAGEM = "preferencias_url_imagem";

  public static final String PREFERENCIAS_NUM_CAMERA_FRONTAL = "preferencias_num_camera_frontal";

}
