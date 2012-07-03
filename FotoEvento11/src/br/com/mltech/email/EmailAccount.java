package br.com.mltech.email;

/**
 * EmailAccount
 * 
 * @author maurocl
 *
 */
public class EmailAccount {

  //---------------------------------------------------------------------------
  // Variáveis de instância
  //---------------------------------------------------------------------------

  /**
   * nome do usuário
   */
  private String user;

  /**
   * senha do usuário
   */
  private String pass;

  /**
   * porta do servidor SMTP
   */
  private String port;

  /**
   * default socket factory port
   */
  private String sport;

  /**
   * endereço do servidor SMTP
   */
  private String host;

  /**
   * SSL = Secure Socket Layer
   */
  private boolean ssl;

  //---------------------------------------------------------------------------
  // Construtores
  //---------------------------------------------------------------------------
  public EmailAccount(String user, String pass) {

    this.user = user;
    this.pass = pass;

    port = "587";

    sport = "587";

    host = "";

    ssl = false;

  }

  //---------------------------------------------------------------------------
  // Métodos getters e setters
  //---------------------------------------------------------------------------

  /**
   * Obtém o nome do usuário da conta
   * 
   * @return
   * 
   */
  public String getUser() {

    return user;
  }

  /**
   * Atualiza o nome do usário da conta
   * 
   * @param user
   * 
   */
  public void setUser(String user) {

    this.user = user;
  }

  /**
   * Obtém o password
   * 
   * @return
   * 
   */
  public String getPass() {

    return pass;
  }

  /**
   * Fornece o password
   * 
   * @param pass
   * 
   */
  public void setPass(String pass) {

    this.pass = pass;
  }

  /**
   * Obtem a porta do servidor SMTP
   * 
   * @return
   * 
   */
  public String getPort() {

    return port;
  }

  /**
   * Atualiza a porta do servidor SMTP
   * 
   * @param port
   * 
   */
  public void setPort(String port) {

    this.port = port;
  }

  /**
   * Obtem a porta segura do servidor SMTP
   * 
   * @return uma string com o nº da porta segura
   * 
   */
  public String getSport() {

    return sport;
  }

  /**
   * Seta a porta segura do servidor SMTP
   * 
   * @param sport
   */
  public void setSport(String sport) {

    this.sport = sport;
  }

  /**
   * Retorna o nome do servidor SMTP
   * 
   * @return
   */
  public String getHost() {

    return host;
  }

  /**
   * Estabele o nome do servidor SMTP
   * 
   * @param host
   */
  public void setHost(String host) {

    this.host = host;
  }

  /**
   * Indica o uso de SSL
   * 
   * @return
   * 
   */
  public boolean isSsl() {

    return ssl;
  }

  /**
   * habilita o uso de SSL
   * 
   * @param ssl
   * 
   */
  public void setSsl(boolean ssl) {

    this.ssl = ssl;
  }

}
