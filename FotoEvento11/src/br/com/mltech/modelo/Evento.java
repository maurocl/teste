package br.com.mltech.modelo;

import java.io.Serializable;

/**
 * Evento
 * 
 * @author maurocl
 * 
 */
public class Evento implements Serializable {

  /**
	 * 
	 */
  private static final long serialVersionUID = 3437555496744232446L;

  private Contratante contratante;

  private String nome; // nome do evento
  private String data; // data do evento
  private String email; // email principal do evento

  private String endereco;
  private String cidade;
  private String estado;
  private String cep;
  private String telefone;

  private String contaFacebook;
  private String contaTwitter;

  private boolean enviaFacebook;
  private boolean enviaTwitter;

  private String bordaPolaroid;
  private String bordaCabine;

  private Parametros parametros; // indica se o evento terá parâmetros

  // adicionais para que o participante do
  // evento
  // preencha

  public Contratante getContratante() {
    return contratante;
  }

  public void setContratante(Contratante contratante) {
    this.contratante = contratante;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEndereco() {
    return endereco;
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  public String getCidade() {
    return cidade;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getContaFacebook() {
    return contaFacebook;
  }

  public void setContaFacebook(String contaFacebook) {
    this.contaFacebook = contaFacebook;
  }

  public String getContaTwitter() {
    return contaTwitter;
  }

  public void setContaTwitter(String contaTwitter) {
    this.contaTwitter = contaTwitter;
  }

  public boolean isEnviaFacebook() {
    return enviaFacebook;
  }

  public void setEnviaFacebook(boolean enviaFacebook) {
    this.enviaFacebook = enviaFacebook;
  }

  public boolean isEnviaTwitter() {
    return enviaTwitter;
  }

  public void setEnviaTwitter(boolean enviaTwitter) {
    this.enviaTwitter = enviaTwitter;
  }

  public String getBordaPolaroid() {
    return bordaPolaroid;
  }

  public String getBordaCabine() {
    return bordaCabine;
  }

  public Parametros getParametros() {
    return parametros;
  }

  public void setParametros(Parametros parametros) {
    this.parametros = parametros;
  }

  public void setBordaPolaroid(String bordaPolaroid) {
    this.bordaPolaroid = bordaPolaroid;
  }

  public void setBordaCabine(String bordaCabine) {
    this.bordaCabine = bordaCabine;
  }

  @Override
  public String toString() {
    return "Evento [contratante=" + contratante + ", nome=" + nome + ", data=" + data + ", email=" + email + ", endereco="
        + endereco + ", cidade=" + cidade + ", estado=" + estado + ", cep=" + cep + ", telefone=" + telefone + ", contaFacebook="
        + contaFacebook + ", contaTwitter=" + contaTwitter + ", enviaFacebook=" + enviaFacebook + ", enviaTwitter=" + enviaTwitter
        + ", bordaPolaroid=" + bordaPolaroid + ", bordaCabine=" + bordaCabine + ", parametros=" + parametros + "]";
  }

}
