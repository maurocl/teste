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
	 * Participacao(Participante participante, int tipoFoto,
			int efeitoFoto, String nomeArqFoto)
	 * 
	 * Construtor
	 * 
	 * @param participante Objeto participante
	 * @param tipoFoto Tipo da foto
	 * @param efeitoFoto 
	 * @param nomeArqFoto Arquivo com a foto
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
	 * getParticipante()
	 * 
	 * @return Objeto da classe participante
	 */
	public Participante getParticipante() {
		return participante;
	}

	/**
	 * setParticipante(Participante participante)
	 * 
	 * @param participante
	 */
	public void setParticipante(Participante participante) {
		this.participante = participante;
	}

	/**
	 * getTipoFoto()
	 * 
	 * @return tipo da foto
	 * 
	 */
	public int getTipoFoto() {
		return tipoFoto;
	}

	/**
	 * setTipoFoto(int tipoFoto)
	 * @param tipoFoto
	 */
	public void setTipoFoto(int tipoFoto) {
		this.tipoFoto = tipoFoto;
	}

	/**
	 * getEfeitoFoto()
	 * @return
	 */
	public int getEfeitoFoto() {
		return efeitoFoto;
	}

	/**
	 * setEfeitoFoto(int efeitoFoto)
	 * @param efeitoFoto
	 */
	public void setEfeitoFoto(int efeitoFoto) {
		this.efeitoFoto = efeitoFoto;
	}

	/**
	 * getNomeArqFoto()
	 * @return
	 */
	public String getNomeArqFoto() {
		return nomeArqFoto;
	}

	/**
	 * setNomeArqFoto(String nomeArqFoto)
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
