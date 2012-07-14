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
	// color effect ou efeito de cores
	private int efeitoFoto;

	// nome do arquivo que contém a foto
	private String nomeArqFoto;

	/**
	 * Construtor
	 */
	public Participacao() {
		this.participante = null;
	}

	/** 
	 * Construtor
	 * 
	 * @param participante Objeto participante
	 * @param tipoFoto Tipo da foto
	 * @param efeitoFoto Tipo do efeito que será aplicado a foto 
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
	 * @param nomeArqFoto Nome completo do arquivo
	 * 
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
