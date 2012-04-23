package br.com.mltech.modelo;


/**
 * 
 * @author maurocl
 *
 */
public class Contratante {

	String nome;
	String email;
		
	/**
	 * 
	 * @param nome
	 * @param email
	 */
	public Contratante(String nome, String email) {
		this.nome = nome;
		this.email = email;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}
	
	/**
	 * @param nome the nome to set
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
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
