package br.com.mltech.modelo;

public class Participante {

	private String nome;
	private String email;
	private String telefone;
	private Parametros parametros;

	public Participante(String nome, String email, String telefone) {
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome
	 *          the nome to set
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
	 *          the email to set
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
	 *          the telefone to set
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
	 *          the parametros to set
	 */
	public void setParametros(Parametros parametros) {
		this.parametros = parametros;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Participante [nome=" + nome + ", email=" + email + ", telefone=" + telefone + "]";
	}

	
	
}
