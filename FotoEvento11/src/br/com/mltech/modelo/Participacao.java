
package br.com.mltech.modelo;

import java.io.Serializable;

import br.com.mltech.Constantes;

/**
 * Participacao<br>
 * 
 * Essa entidade representa uma participação no evento.<br>
 * A participação consiste de um participante que fornece seu nome, email e telefone e tira uma foto
 * de um tipo (cabine ou polaroid) e tem sua foto armazenada em um arquivo.
 * 
 * @author maurocl
 * 
 */
public class Participacao implements Serializable, Constantes {

  /**
	 * 
	 */
  private static final long serialVersionUID = 3182925558872819278L;

  public static final String _ID = "_id";

  public static final String TIPO = "tipoFoto";

  public static final String EFEITO = "tipoEfeito";

  public static final String ARQUIVO = "nomeArqFoto";

  // identificador da participação
  private long id;

  // informações sobre um participante do evento
  private Participante participante;

  // formato da foto
  private int tipoFoto;

  // filtro (efeito) usado na foto
  // color effect ou efeito de cores
  private int efeitoFoto;

  // nome do arquivo que contém a foto
  private String nomeArqFoto;

  /**
   * Cria uma participação nula
   */
  public Participacao() {

    this.participante = null;
  }

  /**
   * Construtor
   * 
   * @param participante
   *          Objeto participante
   * @param tipoFoto
   *          Tipo da foto
   * @param efeitoFoto
   *          Tipo do efeito que será aplicado a foto
   * @param nomeArqFoto
   *          Arquivo com a foto
   * 
   */
  public Participacao(Participante participante, int tipoFoto, int efeitoFoto, String nomeArqFoto) {

    this.participante = participante;
    this.tipoFoto = tipoFoto;
    this.efeitoFoto = efeitoFoto;
    this.nomeArqFoto = nomeArqFoto;

  }

  
  /**
   * Construtor usando quando não houver parâmetros adicionaos no participante
   * 
   * @param id
   * @param nome
   * @param email
   * @param telefone
   * @param tipoFoto
   * @param efeitoFoto
   * @param nomeArqFoto
   * 
   */
  public Participacao(long id, String nome, String email, String telefone, int tipoFoto, int efeitoFoto, String nomeArqFoto) {

    // Cria um novo participante
    Participante participante = new Participante(nome, email, telefone);

    Parametros parametroAdicional = new Parametros(null);
    
    participante.setParametros(parametroAdicional);
  
    this.id = id;
    this.participante = participante;
    this.tipoFoto = tipoFoto;
    this.efeitoFoto = efeitoFoto;
    this.nomeArqFoto = nomeArqFoto;
    
  }
  
  /**
   * Construtor
   * 
   * @param id
   * @param nome
   * @param email
   * @param telefone
   * @param tipoFoto
   * @param efeitoFoto
   * @param nomeArqFoto
   * @param param1
   * @param param2
   * @param param3
   * @param param4
   * @param param5
   */
  public Participacao(long id, String nome, String email, String telefone, int tipoFoto, int efeitoFoto, String nomeArqFoto, String param1, String param2, String param3, String param4, String param5) {

    // Cria um novo participante
    Participante participante = new Participante(nome, email, telefone);

    Parametros parametroAdicional = new Parametros(new String[] { param1, param2, param3, param4, param5 });
    
    participante.setParametros(parametroAdicional);
  
    this.id = id;
    this.participante = participante;
    this.tipoFoto = tipoFoto;
    this.efeitoFoto = efeitoFoto;
    this.nomeArqFoto = nomeArqFoto;
    
  }
  
  
  /**
   * Obtém o identificador da participacao
   * 
   * @return
   */
  public long getId() {

    return id;
  }

  /**
   * Seta o id da participacao
   * 
   * @param id
   *          Identificador da participacao
   * 
   */
  public void setId(long id) {

    this.id = id;
  }

  /**
   * Obtem o participante do evento.
   * 
   * @return Objeto da classe participante
   */
  public Participante getParticipante() {

    return participante;
  }

  /**
   * Seta o participante do evento
   * 
   * @param participante
   */
  public void setParticipante(Participante participante) {

    this.participante = participante;
  }

  /**
   * Retorna o tipo (formato) da foto: Polaroid, Cabine.
   * 
   * @return tipo da foto
   * 
   */
  public int getTipoFoto() {

    return tipoFoto;
  }

  /**
   * Atualiza o tipo da foto
   * 
   * @param tipoFoto
   * 
   */
  public void setTipoFoto(int tipoFoto) {

    this.tipoFoto = tipoFoto;
  }

  /**
   * Obtem o efeito de cores aplicado a foto
   * 
   * @return O identificador do efeito de cores aplicado
   * 
   */
  public int getEfeitoFoto() {

    return efeitoFoto;
  }

  /**
   * Estabelece o efeito de cores que será usado.
   * 
   * @param efeitoFoto
   */
  public void setEfeitoFoto(int efeitoFoto) {

    this.efeitoFoto = efeitoFoto;
  }

  /**
   * Obtem o nome do arquivo onde a foto está armzenada
   * 
   * @return O nome completo do arquivo
   * 
   */
  public String getNomeArqFoto() {

    return nomeArqFoto;
  }

  /**
   * Atualiza o nome do arquivo onde a foto foi armazenada
   * 
   * @param nomeArqFoto
   *          Nome completo do arquivo
   * 
   */
  public void setNomeArqFoto(String nomeArqFoto) {

    this.nomeArqFoto = nomeArqFoto;
  }

  /**
   * Retorna o tipo da foto selecionada
   * 
   * @return o tipo da foto
   */
  public String getStrTipoFoto() {

    String sTipoFoto = null;

    if (getTipoFoto() == TIPO_FOTO_POLAROID) {
      sTipoFoto = "Polaroid";
    } else if (getTipoFoto() == TIPO_FOTO_CABINE) {
      sTipoFoto = "Cabine";
    } else {
      sTipoFoto = "Indefinido";
    }

    return sTipoFoto;

  }

  /**
   * Retorna o nome do efeito de cores aplicado a foto
   * 
   * @return o nome do efeito de cores aplicado a foto
   */
  public String getStrEfeitoFoto() {

    String sEfeitoFoto = null;

    if (getEfeitoFoto() == Constantes.CORES) {
      sEfeitoFoto = "Cores";
    } else if (getEfeitoFoto() == Constantes.PB) {
      sEfeitoFoto = "P&B";
    } else {
      sEfeitoFoto = "Indefinido";
    }

    return sEfeitoFoto;

  }

  @Override
  public String toString() {

    return "Participacao [id=" + id + ", participante=" + participante + ", tipoFoto=" + tipoFoto + ", efeitoFoto=" + efeitoFoto
        + ", nomeArqFoto=" + nomeArqFoto + "]";
  }

}
