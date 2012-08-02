package br.com.mltech.modelo;

import java.io.Serializable;
import java.util.ArrayList;

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

  // lista de participantes do evento
  private ArrayList<Participacao> participantes;

  // contratante do evento
  private Contratante contratante;

  private String nome; // nome do evento

  private String data; // data do evento

  private String email; // email principal do evento

  private String endereco; // endereço

  private String cidade; // cidade

  private String estado; // estado

  private String cep; // cep

  private String telefone; // telefone

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

  /**
   * Construtor vazio
   */
  public Evento() {

    this.contratante = null;
    this.parametros = new Parametros();
    this.participantes = new ArrayList<Participacao>();
    
  }

  /**
   * Cria um evento com seu Contratante
   * 
   * @param contratante
   *          Instancia de um contratante
   * 
   */
  public Evento(Contratante contratante) {
    
    this();
    
    this.contratante = contratante;
    
  }

  /**
   * Retorno uma instância do contratante do evento
   * 
   * @return a instância do contratante do evento
   */
  public Contratante getContratante() {

    return contratante;
  }

  /**
   * 
   * @param contratante
   */
  public void setContratante(Contratante contratante) {

    this.contratante = contratante;
  }

  /**
   * 
   * @return
   */
  public String getNome() {

    return nome;
  }

  /**
   * 
   * @param nome
   */
  public void setNome(String nome) {

    this.nome = nome;
  }

  /**
   * 
   * @return
   */
  public String getData() {

    return data;
  }

  /**
   * 
   * @param data
   */
  public void setData(String data) {

    this.data = data;
  }

  /**
   * 
   * @return
   */
  public String getEmail() {

    return email;
  }

  /**
   * 
   * @param email
   */
  public void setEmail(String email) {

    this.email = email;
  }

  /**
   * 
   * @return
   */
  public String getEndereco() {

    return endereco;
  }

  /**
   * 
   * @param endereco
   */
  public void setEndereco(String endereco) {

    this.endereco = endereco;
  }

  /**
   * 
   * @return
   */
  public String getCidade() {

    return cidade;
  }

  /**
   * 
   * @param cidade
   */
  public void setCidade(String cidade) {

    this.cidade = cidade;
  }

  /**
   * 
   * @return
   */
  public String getEstado() {

    return estado;
  }

  /**
   * 
   * @param estado
   */
  public void setEstado(String estado) {

    this.estado = estado;
  }

  /**
   * 
   * @return
   */
  public String getCep() {

    return cep;
  }

  /**
   * 
   * @param cep
   */
  public void setCep(String cep) {

    this.cep = cep;
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

  /**
   * 
   * @return
   */
  public String getContaFacebook() {

    return contaFacebook;
  }

  /**
   * 
   * @param contaFacebook
   */
  public void setContaFacebook(String contaFacebook) {

    this.contaFacebook = contaFacebook;
  }

  /**
   * 
   * @return
   */
  public String getContaTwitter() {

    return contaTwitter;
  }

  /**
   * 
   * @param contaTwitter
   */
  public void setContaTwitter(String contaTwitter) {

    this.contaTwitter = contaTwitter;
  }

  /**
   * 
   * @return
   */
  public boolean isEnviaFacebook() {

    return enviaFacebook;
  }

  /**
   * 
   * @param enviaFacebook
   */
  public void setEnviaFacebook(boolean enviaFacebook) {

    this.enviaFacebook = enviaFacebook;
  }

  /**
   * esse método facilita o trabalho
   * 
   * @param enviaFacebook
   */
  public void setEnviaFacebook(String enviaFacebook) {

    if (enviaFacebook.equalsIgnoreCase("true")) {
      this.enviaFacebook = true;
    }
    else {
      this.enviaFacebook = false;
    }

  }

  /**
   * 
   * @return
   */
  public boolean isEnviaTwitter() {

    return enviaTwitter;
  }

  public void setEnviaTwitter(boolean enviaTwitter) {

    this.enviaTwitter = enviaTwitter;
  }

  /**
   * esse método facilita o trabalho
   * 
   * @param enviaTwitter
   */
  public void setEnviaTwitter(String enviaTwitter) {

    if (enviaTwitter.equalsIgnoreCase("true")) {
      this.enviaTwitter = true;
    }
    else {
      this.enviaTwitter = false;
    }
  }

  /**
   * 
   * @return
   */
  public String getBordaPolaroid() {

    return bordaPolaroid;
  }

  /**
   * 
   * @return
   */
  public String getBordaCabine() {

    return bordaCabine;
  }

  /**
   * Obtem os parâmetros opcionais
   * 
   * @return uma instância da classe Parâmetros
   * 
   */
  public Parametros getParametros() {

    return parametros;
  }

  /**
   * 
   * @param parametros
   */
  public void setParametros(Parametros parametros) {

    this.parametros = parametros;
  }

  /**
   * Altera o nome do arquivo que possui a borda formato polaroid
   * 
   * @param bordaPolaroid
   */
  public void setBordaPolaroid(String bordaPolaroid) {

    this.bordaPolaroid = bordaPolaroid;
  }

  /**
   * Altera o nome do arquivo que possui a borda formato cabine
   * 
   * @param bordaCabine
   */
  public void setBordaCabine(String bordaCabine) {

    this.bordaCabine = bordaCabine;
  }

  /**
   * getParticipantes()
   * 
   * Obtém a lista de participantes do evento
   * 
   * @return Obtém uma referência a lista de participantes do evento
   * 
   */
  public ArrayList<Participacao> getParticipantes() {

    return participantes;
  }

  /**
   * setParticipantes(ArrayList<Participacao> participantes)
   * 
   * Atribui uma lista d e participantes ao evento
   * 
   * @param participantes
   * 
   */
  public void setParticipantes(ArrayList<Participacao> participantes) {

    this.participantes = participantes;
  }

  /**
   * 
   */
  @Override
  public String toString() {

    return "Evento [contratante=" + contratante + ", nome=" + nome + ", data=" + data + ", email=" + email + ", endereco="
        + endereco + ", cidade=" + cidade + ", estado=" + estado + ", cep=" + cep + ", telefone=" + telefone + ", contaFacebook="
        + contaFacebook + ", contaTwitter=" + contaTwitter + ", enviaFacebook=" + enviaFacebook + ", enviaTwitter=" + enviaTwitter
        + ", bordaPolaroid=" + bordaPolaroid + ", bordaCabine=" + bordaCabine + ", parametros=" + parametros + "]";
  }

  
  /**
   * 
   * @return true se o email for "sintaticamente válido" ou false caso não possa ser validado
   * 
   */
  public boolean emailIsValid() {
    return true;
  }
  
}
