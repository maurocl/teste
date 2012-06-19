package br.com.mltech;

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
  private String mailhost = "smtp.dialhost.com.br";

  // usário
  private String user; // usuário

  // password
  private String password; // senha

  // sessão
  private Session session; // sessão

  // inicialização estática do provedor de segurança
  static {
    // Security.addProvider(new com.provider.JSSEProvider());
    Security.addProvider(new br.com.mltech.JSSEProvider());
  }

  /**
   * MLTechlSender(String user, String password)
   * 
   * Envia um email usando MLTech
   * 
   * @param user
   *          Usuário
   *          
   * @param password
   *          Senha
   * 
   */
  public MLTechSender(String user, String password) {

    this.user = user;
    this.password = password;

    Properties props = new Properties();

    props.setProperty("mail.transport.protocol", "smtp");
    props.setProperty("mail.host", mailhost);

    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.port", "465");
    props.put("mail.smtp.socketFactory.port", "465");
    props.put("mail.smtp.socketFactory.class",
        "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.socketFactory.fallback", "false");

    props.setProperty("mail.smtp.quitwait", "false");

    session = Session.getDefaultInstance(props, this);

  }

  /**
   * getPasswordAuthentication()
   */
  protected PasswordAuthentication getPasswordAuthentication() {

    return new PasswordAuthentication(user, password);
    
  }

  /**
   * sendMail(String subject, String body, String sender, String recipients)
   * 
   * Envia um email
   * 
   * @param subject
   *          Assunto
   * @param body
   *          Corpo do email
   * @param sender
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
     * Tipo
     */
    private String type;

    /**
     * ByteArrayDataSource(byte[] data, String type)
     * 
     * Construtor
     * 
     * @param data
     *          Um array de bytes
     * @param type
     *          Tipo do array de bytes
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
     */
    public ByteArrayDataSource(byte[] data) {

      super();
      this.data = data;
    }

    /**
     * setType(String type)
     * 
     * Configura o tipo do ...
     * 
     * @param type
     *          Tipo do ...
     */
    public void setType(String type) {

      this.type = type;
    }

    /**
     * getContentType()
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
     * @return
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