package br.com.mltech.modelo;

import java.io.Serializable;

/**
 * Participante do Evento.
 * 
 * <p>O participante do evento possui os seguintes abtributos:
 * <ul>
 * <li>nome
 * <li>email
 * <li>telefone
 * </ul>
 * <p>Há também espaço para criação de cinco campos adicionais representados por:
 * param1, ..., param5.
 * 
 * @author maurocl
 * 
 */
public class Participante implements Serializable {

	public static final String NOME = "nome";
	public static final String EMAIL = "email";
	public static final String TELEFONE = "telefone";

	public static final String PARAM1 = "param1";
	public static final String PARAM2 = "param2";
	public static final String PARAM3 = "param3";
	public static final String PARAM4 = "param4";
	public static final String PARAM5 = "param5";

	/**
	 * 
	 */
	private static final long serialVersionUID = -3719854749249823592L;

	private String nome; // Nome do participante do evento
	private String email; // Email do participante do evento
	private String telefone; // Telefone do participante do evento
	private Parametros parametros; // Parâmetros opcionais (se houver)

	/**
	 * Cria um participante nulo
	 * 
	 */
	public Participante() {
		// construtor vazio
		this(null, null, null, null);
	}

	/**
	 * Constroi um novo participante (sem parâmetros adicionais)
	 * 
	 * @param nome
	 *          Nome do participante
	 * @param email
	 *          Email do participante
	 * @param telefone
	 *          Telefone do participante
	 * 
	 */
	public Participante(String nome, String email, String telefone) {
		this(nome, email, telefone, null);
	}

	/**
	 * Constroi um novo participante
	 * 
	 * @param nome Nome 
	 * @param email Email
	 * @param telefone Telefone
	 * @param parametros 
	 */
	public Participante(String nome, String email, String telefone, Parametros parametros) {
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
		this.parametros = parametros;
	}

	/**
	 * @return o nome do participante
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


	@Override
  public String toString() {

    return "Participante [nome=" + nome + ", email=" + email + ", telefone=" + telefone + ", parametros=" + parametros + "]";
  }

  /**
	 * Verifica se o participante preencheu todos os campos obrigatórios.
	 * 
	 * @return Retorna true se o participante preencheu todos os campos
	 *         obrigatórios e false caso contrário.
	 */
	public boolean isValido() {
		if (((nome != null) && (!nome.equals(""))) && ((email != null) && (!email.equals("")))
				&& ((telefone != null) && (!telefone.equals("")))) {
			return true;
		}
		return false;
	}

}
