package br.com.mltech.modelo;

import java.io.Serializable;

/**
 * Participacao
 * 
 * @author maurocl
 *
 */
public class Participacao implements Serializable {

	/**
   * 
   */
  private static final long serialVersionUID = 3182925558872819278L;
  
	// informações sobre um participante do evento
	private Participante participante;

	// formato da foto
	private int tipoFoto;

	// filtro (efeito) usado na foto
	private int efeitoFoto;

	// nome do arquivo que contém a foto
	private String nomeArqFoto;

	public Participacao() {
		this.participante = null;
	}

	/**
	 * Construtor
	 * 
	 * @param participante
	 * @param tipoFoto
	 * @param efeitoFoto
	 * @param nomeArqFoto
	 * 
	 */
	public Participacao(Participante participante, int tipoFoto,
			int efeitoFoto, String nomeArqFoto) {

		this.participante = participante;
		this.tipoFoto = tipoFoto;
		this.efeitoFoto = efeitoFoto;
		this.nomeArqFoto = nomeArqFoto;
	}

	/**
	 * 
	 * @return
	 */
	public Participante getParticipante() {
		return participante;
	}

	/**
	 * 
	 * @param participante
	 */
	public void setParticipante(Participante participante) {
		this.participante = participante;
	}

	/**
	 * 
	 * @return
	 */
	public int getTipoFoto() {
		return tipoFoto;
	}

	/**
	 * 
	 * @param tipoFoto
	 */
	public void setTipoFoto(int tipoFoto) {
		this.tipoFoto = tipoFoto;
	}

	/**
	 * 
	 * @return
	 */
	public int getEfeitoFoto() {
		return efeitoFoto;
	}

	/**
	 * 
	 * @param efeitoFoto
	 */
	public void setEfeitoFoto(int efeitoFoto) {
		this.efeitoFoto = efeitoFoto;
	}

	/**
	 * 
	 * @return
	 */
	public String getNomeArqFoto() {
		return nomeArqFoto;
	}

	/**
	 * 
	 * @param nomeArqFoto
	 */
	public void setNomeArqFoto(String nomeArqFoto) {
		this.nomeArqFoto = nomeArqFoto;
	}

	@Override
	public String toString() {
		return "Participacao [participante=" + participante + ", tipoFoto="
				+ tipoFoto + ", efeitoFoto=" + efeitoFoto + ", nomeArqFoto="
				+ nomeArqFoto + "]";
	}

}
