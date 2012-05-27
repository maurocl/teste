package br.com.mltech.modelo;

import java.io.Serializable;

/**
 * Contratante
 * 
 * @author maurocl
 * 
 */
public class Contratante implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 675769010412659758L;

	private String nome;
	private String email;
	private String telefone;

	/**
	 * Construtor
	 * 
	 * @param nome
	 * @param email
	 * @param telefone
	 */
	public Contratante(String nome, String email, String telefone) {
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
	}

	/**
	 * Contratante
	 */
	public Contratante() {
		this.nome = null;
		this.email = null;
		this.telefone = null;
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
	 * 
	 * @return
	 */
	public String getTelefone() {
		return telefone;
	}

	/**
	 * 
	 * @param telefone
	 */
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@Override
	public String toString() {
		return "Contratante [nome=" + nome + ", email=" + email + ", telefone="
				+ telefone + "]";
	}

	/**
	 * isValido()
	 * 
	 * O contratante deve ter todos os campos não nulos
	 * 
	 * @return
	 */
	public boolean isValido() {
		if ((!getNome().equals("")) && (!getEmail().equals(""))
				&& (!getTelefone().equals(""))) {
			return true;
		}
		return false;
	}

}
