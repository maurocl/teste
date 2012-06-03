package br.com.mltech;

/**
 * Constantes.java
 * 
 * @author maurocl
 * 
 */
public interface Constantes {

  public static final String EVENTO_NOME = "";

  // Definições de tipos de fotos
  public static final int TIPO_FOTO_POLAROID = 1;
  public static final int TIPO_FOTO_CABINE = 2;

  // Definição de constantes para o efeito de cores
  public static final int CORES = 11;

  // Definição de constantes para o efeito P&B
  public static final int PB = 12;

  //

  public static final String CONTRATANTE = "br.com.mltech.contratante";
  public static final String EVENTO = "br.com.mltech.evento";
  public static final String PARTICIPANTE = "br.com.mltech.participante";
  public static final String PARTICIPACAO = "br.com.mltech.participacao";
  public static final String LISTA = "br.com.mltech.lista";

  // TODO criar um diretório inicial, um lugar para configuração ...
  public static final String PATH_MOLDURAS = "/mnt/sdcard/Pictures/fotoevento/molduras/";
  public static final String PATH_FOTOS = "/mnt/sdcard/Pictures/fotoevento/fotos/";

  // Definição da Activies chamadas a partir de DummyActivity
  public static final int ACTIVITY_TIRA_FOTO_3 = 113;
  public static final int ACTIVITY_CHOOSER = 150;
  public static final int ACTIVITY_PARTICIPANTE = 102;

}
