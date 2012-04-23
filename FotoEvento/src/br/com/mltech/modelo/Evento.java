package br.com.mltech.modelo;

import android.graphics.Bitmap;


public class Evento {

	Contratante contratante;
	
	String nome; // nome do evento
	String data; // data do evento
	String email; // email principal do evento
	
	String endereco;
	String cidade;
	String estado;
	String cep;
	String telefone;
	
	String contaFacebook;
	String contaTwitter;
	
	boolean enviaFacebook;
	boolean enviaTwitter;

	Bitmap bordaPolaroid;
	Bitmap bordaCabine;
	
	Parametros parametros; // indica se o evento terá parâmetros adicionais para que o participante do evento preencha


	//-----------------------------------------
	
	/**
	 * @return the contratante
	 */
	public Contratante getContratante() {
		return contratante;
	}

	
	/**
	 * @param contratante the contratante to set
	 */
	public void setContratante(Contratante contratante) {
		this.contratante = contratante;
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
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
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

	
	/**
	 * @return the endereco
	 */
	public String getEndereco() {
		return endereco;
	}

	
	/**
	 * @param endereco the endereco to set
	 */
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	
	/**
	 * @return the cidade
	 */
	public String getCidade() {
		return cidade;
	}

	
	/**
	 * @param cidade the cidade to set
	 */
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	
	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	
	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	
	/**
	 * @return the cep
	 */
	public String getCep() {
		return cep;
	}

	
	/**
	 * @param cep the cep to set
	 */
	public void setCep(String cep) {
		this.cep = cep;
	}

	
	/**
	 * @return the telefone
	 */
	public String getTelefone() {
		return telefone;
	}

	
	/**
	 * @param telefone the telefone to set
	 */
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	
	/**
	 * @return the contaFacebook
	 */
	public String getContaFacebook() {
		return contaFacebook;
	}

	
	/**
	 * @param contaFacebook the contaFacebook to set
	 */
	public void setContaFacebook(String contaFacebook) {
		this.contaFacebook = contaFacebook;
	}

	
	/**
	 * @return the contaTwitter
	 */
	public String getContaTwitter() {
		return contaTwitter;
	}

	
	/**
	 * @param contaTwitter the contaTwitter to set
	 */
	public void setContaTwitter(String contaTwitter) {
		this.contaTwitter = contaTwitter;
	}

	
	/**
	 * @return the enviaFacebook
	 */
	public boolean isEnviaFacebook() {
		return enviaFacebook;
	}

	
	/**
	 * @param enviaFacebook the enviaFacebook to set
	 */
	public void setEnviaFacebook(boolean enviaFacebook) {
		this.enviaFacebook = enviaFacebook;
	}

	
	/**
	 * @return the enviaTwitter
	 */
	public boolean isEnviaTwitter() {
		return enviaTwitter;
	}

	
	/**
	 * @param enviaTwitter the enviaTwitter to set
	 */
	public void setEnviaTwitter(boolean enviaTwitter) {
		this.enviaTwitter = enviaTwitter;
	}

	
	/**
	 * @return the bordaPolaroid
	 */
	public Bitmap getBordaPolaroid() {
		return bordaPolaroid;
	}

	
	/**
	 * @param bordaPolaroid the bordaPolaroid to set
	 */
	public void setBordaPolaroid(Bitmap bordaPolaroid) {
		this.bordaPolaroid = bordaPolaroid;
	}

	
	/**
	 * @return the bordaCabine
	 */
	public Bitmap getBordaCabine() {
		return bordaCabine;
	}

	
	/**
	 * @param bordaCabine the bordaCabine to set
	 */
	public void setBordaCabine(Bitmap bordaCabine) {
		this.bordaCabine = bordaCabine;
	}

	
	/**
	 * @return the parametros
	 */
	public Parametros getParametros() {
		return parametros;
	}

	
	/**
	 * @param parametros the parametros to set
	 */
	public void setParametros(Parametros parametros) {
		this.parametros = parametros;
	}
	

	
	
	
	//-----------------------------------------
	
	
}
