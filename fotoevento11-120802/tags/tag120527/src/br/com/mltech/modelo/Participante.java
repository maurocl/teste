package br.com.mltech.modelo;

import java.io.Serializable;

/**
 * Participante
 * 
 * @author maurocl
 * 
 */
public class Participante implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3719854749249823592L;

	private String nome; // Nome do participante do evento
	private String email; // email do participante do evento
	private String telefone; 
	private Parametros parametros; // Parâmetros opcionais (se houver)

	/**
	 * Participante()
	 */
	public Participante() {
		// construtor vazio
	}

	/**
	 * Participante(String nome, String email, String telefone)
	 * 
	 * @param nome
	 * @param email
	 * @param telefone
	 */
	public Participante(String nome, String email, String telefone) {
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
		this.parametros = null;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the telefone
	 */
	public String getTelefone() {
		return telefone;
	}

	/**
	 * @param telefone
	 *            the telefone to set
	 */
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	/**
	 * @return the parametros
	 */
	public Parametros getParametros() {
		return parametros;
	}

	/**
	 * @param parametros
	 *            the parametros to set
	 */
	public void setParametros(Parametros parametros) {
		this.parametros = parametros;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Participante [nome=" + nome + ", email=" + email
				+ ", telefone=" + telefone + "]";
	}

	/**
	 * 
	 * @return
	 */
	public boolean isValido() {
		return true;
	}
	
}
