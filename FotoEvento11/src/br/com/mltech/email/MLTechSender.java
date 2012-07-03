package br.com.mltech.email;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * MLTechlSender
 * 
 * É uma classe Authenticator.
 * 
 * @author maurocl
 * 
 */
public class MLTechSender extends javax.mail.Authenticator {

  // host responsável pelo envio do email
  private String mailhost = "smtp.mltech.com.br";

  // usuário
  private String user;

  // password
  private String password;

  // sessão
  private Session session;

  // inicialização estática do provedor de segurança
  static {
    // Security.addProvider(new com.provider.JSSEProvider());
    Security.addProvider(new JSSEProvider());
  }

  /**
   * MLTechlSender(String user, String password)
   * 
   * Envia um email usando MLTech
   * 
   * @param user
   *          Usuário nome do usuário
   * 
   * @param password
   *          Senha do usuário
   * 
   */
  public MLTechSender(String user, String password) {

    this.user = user;
    this.password = password;

    // guarda as prooriedades de configuração do envio do email
    Properties props = new Properties();

    props.setProperty("mail.transport.protocol", "smtp");
    props.setProperty("mail.host", mailhost);

    props.put("mail.smtp.auth", "true");

    props.put("mail.smtp.port", "587");

    //props.put("mail.smtp.socketFactory.port", "587");

    props.setProperty("mail.smtp.quitwait", "false");

    // cria uma sessão a patir das propriedades
    session = Session.getDefaultInstance(props, this);

  }

  /**
   * getPasswordAuthentication()
   * 
   * Autentica o usuário e senha
   * 
   */
  protected PasswordAuthentication getPasswordAuthentication() {

    return new PasswordAuthentication(user, password);

  }

  /**
   * sendMail(String subject, String body, String sender, String recipients)
   * 
   * Envia um email ao recipiente(s) versando sobre o assunto.
   * 
   * @param subject
   *          Assunto
   * 
   * @param body
   *          Corpo do email
   * 
   * @param sender
   *          Remetente do email
   * 
   * @param recipients
   *          Recipientes para onde o email será enviado
   * 
   * @throws Exception
   *           Lança uma Exception
   * 
   */
  public synchronized void sendMail(String subject, String body,
      String sender, String recipients) throws Exception {

    try {

      MimeMessage message = new MimeMessage(session);

      DataHandler handler = new DataHandler(new ByteArrayDataSource(
          body.getBytes(), "text/plain"));

      message.setSender(new InternetAddress(sender));

      // adiciona o subjet
      message.setSubject(subject);

      // adiciona o tratador de dados
      message.setDataHandler(handler);

      if (recipients.indexOf(',') > 0) {

        message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(recipients));

      } else {

        message.setRecipient(Message.RecipientType.TO,
            new InternetAddress(recipients));

      }

      // envia a mensagem
      Transport.send(message);

    } catch (Exception e) {
      // ignora se houver erros
    }

  }

  /**
   * ByteArrayDataSource
   * 
   * Cria um DataSource a partir de um array de bytes
   * 
   * @author maurocl
   * 
   */
  public class ByteArrayDataSource implements DataSource {

    /**
     * Array de bytes
     */
    private byte[] data;

    /**
     * Tipo de dados. Ex: text/plain; text/html
     */
    private String type;

    /**
     * ByteArrayDataSource(byte[] data, String type)
     * 
     * Construtor
     * 
     * @param data
     *          Um array de bytes
     * 
     * @param type
     *          Tipo do dado armazenado no array de bytes
     * 
     */
    public ByteArrayDataSource(byte[] data, String type) {

      super();

      this.data = data;
      this.type = type;

    }

    /**
     * ByteArrayDataSource(byte[] data)
     * 
     * @param data
     *          Um array de bytes
     */
    public ByteArrayDataSource(byte[] data) {

      super();
      this.data = data;
    }

    /**
     * setType(String type)
     * 
     * Configura o tipo do dado armazenado (MIME type do dado)
     * 
     * @param type
     *          MIME type do conteúdo
     */
    public void setType(String type) {

      this.type = type;
    }

    /**
     * getContentType()
     * 
     * Retorna o tipo de dado armazenado no array. O tipo padrão é:
     * application/octet-stream
     * 
     * @return String
     */
    public String getContentType() {

      if (type == null) {
        return "application/octet-stream";
      } else {
        return type;
      }

    }

    /**
     * getInputStream()
     * 
     * Obtém um input stream
     * 
     * @return um input stream
     * 
     * @throws IOException
     * 
     */
    public InputStream getInputStream() throws IOException {

      return new ByteArrayInputStream(data);

    }

    /**
     * getName()
     * 
     * @return String com o nome do DataSource
     */
    public String getName() {

      return "ByteArrayDataSource";
    }

    /**
     * getOutputStream()
     * 
     * @return OutputStream
     * 
     * @throws IOException
     * 
     */
    public OutputStream getOutputStream() throws IOException {

      throw new IOException("Not Supported");

    }

  }

}