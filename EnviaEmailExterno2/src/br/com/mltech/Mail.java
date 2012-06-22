package br.com.mltech;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Mail
 * 
 * @author maurocl
 * 
 */
public class Mail extends javax.mail.Authenticator {

  public static final String TAG = "Mail";

  /**
   * nome do usu�rio
   */
  private String user;

  /**
   * senha do usu�rio
   */
  private String pass;

  /**
   * array de endere�os de email (to)
   */
  private String[] to;

  /**
   * array de endere�os de email (cc)
   */
  private String[] cc;

  /**
   * array de endere�os de email (bcc)
   */
  private String[] bcc;

  /**
   * email do remetente
   */
  private String from;

  /**
   * responder para
   */
  private String replyTo;

  /**
   * porta do servidor SMTP
   */
  private String port;

  /**
   * default socket factory port
   */
  private String sport;

  /**
   * endere�o do servidor SMTP
   */
  private String host;

  /**
   * Assunto do email
   */
  private String subject;

  /**
   * Corpo do email
   */
  private String body;

  /**
   * Indica se o usu�rio est� autenticado ou n�o
   */
  private boolean auth;

  /**
   * Indica se a rotina � debugavel
   */
  private boolean debuggable;

  /**
   * SSL = Secure Socket Layer
   */
  private boolean ssl;

  /**
   * 
   */
  private Multipart multipart;

  /**
   * Mail()
   * 
   * Construtor
   * 
   */
  public Mail() {

    auth = true; // smtp authentication - default on

    multipart = new MimeMultipart();

    // There is something wrong with MailCap, javamail can not find a
    // handler for the multipart/mixed part, so this bit needs to be added.

    MailcapCommandMap mc = (MailcapCommandMap) CommandMap
        .getDefaultCommandMap();

    mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
    mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
    mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
    mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
    mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");

    CommandMap.setDefaultCommandMap(mc);

  }

  /**
   * Mail(String user, String pass)
   * 
   * @param user
   *          Username
   * 
   * @param pass
   *          Password
   * 
   */
  public Mail(String user, String pass) {

    this();

    this.user = user;
    this.pass = pass;

  }

  /**
   * send()
   * 
   * @return true email enviado com sucesso ou false caso contr�rio
   * 
   * @throws Exception
   */
  public boolean send() throws Exception {

    Properties props = setProperties();

    if (!user.equals("") && !pass.equals("") && to.length > 0
        && !from.equals("") && !subject.equals("")
        && !body.equals("")) {

      Session session = Session.getInstance(props, this);
      //Session session = Session.getInstance(props, null);

      MimeMessage msg = new MimeMessage(session);

      msg.setFrom(new InternetAddress(from));

      //-----------------------------------------------------------------------
      // To:
      //-----------------------------------------------------------------------
      InternetAddress[] addressTo = new InternetAddress[to.length];

      for (int i = 0; i < to.length; i++) {
        addressTo[i] = new InternetAddress(to[i]);
      }

      msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);

      //-----------------------------------------------------------------------
      // Cc:
      //-----------------------------------------------------------------------
      if (cc != null) {
        InternetAddress[] addressCc = new InternetAddress[cc.length];

        for (int i = 0; i < cc.length; i++) {
          addressCc[i] = new InternetAddress(cc[i]);
        }

        msg.setRecipients(MimeMessage.RecipientType.CC, addressCc);

      }

      //-----------------------------------------------------------------------
      // Bcc:
      //-----------------------------------------------------------------------
      if (bcc != null) {
        InternetAddress[] addressBcc = new InternetAddress[bcc.length];

        for (int i = 0; i < bcc.length; i++) {
          addressBcc[i] = new InternetAddress(bcc[i]);
        }

        msg.setRecipients(MimeMessage.RecipientType.BCC, addressBcc);
        //-----------------------------------------------------------------------
      }

      // subject do email
      msg.setSubject(subject);

      // data de envio do email
      msg.setSentDate(new Date());

      // setup message body
      BodyPart messageBodyPart = new MimeBodyPart();

      // adiciona o corpo do email
      messageBodyPart.setText(body);

      multipart.addBodyPart(messageBodyPart);

      // Put parts in message
      msg.setContent(multipart);

      // n�o sei se � necess�rio
      msg.saveChanges();

      // send email      
      Transport.send(msg);

      return true;

    } else {

      return false;

    }

  }

  /**
   * send(String to, String bcc, String from, String subject, String body,
   * String foto)
   * 
   * @param to
   * @param bcc
   * @param from
   * @param subject
   * @param body
   * @param foto
   * 
   * @return true email enviado com sucesso ou false caso contr�rio
   * 
   * @throws Exception
   * 
   */
  public boolean send(String to, String bcc, String from, String subject, String body, String foto) throws Exception {

    Properties props = setProperties();

    if (!user.equals("") && !pass.equals("") && (!to.equals("")) && (!bcc.equals("")) && (!foto.equals(""))
        && !from.equals("") && !subject.equals("")
        && !body.equals("")) {

      Session session = Session.getInstance(props, this);
      //Session session = Session.getInstance(props, null);

      MimeMessage msg = new MimeMessage(session);

      msg.setFrom(new InternetAddress(from));

      //-----------------------------------------------------------------------
      // To:
      //-----------------------------------------------------------------------
      InternetAddress[] addressTo = { new InternetAddress(to) };

      msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);

      //-----------------------------------------------------------------------
      // Bcc:
      //-----------------------------------------------------------------------
      InternetAddress[] addressBcc = { new InternetAddress(bcc) };

      msg.setRecipients(MimeMessage.RecipientType.BCC, addressBcc);

      //-----------------------------------------------------------------------
      // subject do email
      msg.setSubject(subject);

      // data de envio do email
      msg.setSentDate(new Date());

      // setup message body
      BodyPart messageBodyPart = new MimeBodyPart();

      // adiciona o corpo do email
      messageBodyPart.setText(body);

      multipart.addBodyPart(messageBodyPart);

      // Put parts in message
      msg.setContent(multipart);

      // anexa a foto
      File f = new File(foto);
      addAttachment(f.getAbsolutePath());

      // n�o sei se � necess�rio
      msg.saveChanges();

      // send email      
      Transport.send(msg);

      return true;

    } else {

      return false;

    }

  }

  /**
   * addAttachment(String filename)
   * 
   * Adiciona um arquivo como anexo ao email.
   * 
   * @param filename
   *          Nome do arquivo anexado
   * 
   * @throws Exception
   *           lan�ada em caso de erro
   * 
   */
  public void addAttachment(String filename) throws Exception {

    BodyPart messageBodyPart = new MimeBodyPart();

    DataSource source = new FileDataSource(filename);

    messageBodyPart.setDataHandler(new DataHandler(source));

    // lan�a MessagingException
    messageBodyPart.setFileName(filename);

    multipart.addBodyPart(messageBodyPart);

  }

  /**
   * getPasswordAuthentication()
   * 
   * <p>
   * Called when password authentication is needed. Subclasses should override
   * the default implementation, which returns null.
   * 
   * Note that if this method uses a dialog to prompt the user for this
   * information, the dialog needs to block until the user supplies the
   * information.
   * 
   * This method can not simply return after showing the dialog.
   * 
   * Returns: The PasswordAuthentication collected from the user, or null if
   * none is provided.
   * 
   * 
   * 
   */
  @Override
  public PasswordAuthentication getPasswordAuthentication() {

    return new PasswordAuthentication(user, pass);

  }

  /**
   * setProperties()
   * 
   * Estabelece as propriedades do email
   * 
   * @return uma inst�ncia de Properties
   */
  private Properties setProperties() {

    Properties props = new Properties();

    if (debuggable) {
      props.put("mail.debug", "true");
    }
    
    props.setProperty("mail.transport.protocol", "smtp");

    props.put("mail.smtp.host", host);


    if (auth) {
      props.put("mail.smtp.auth", "true");
    }

    props.put("mail.smtp.port", port);

    //props.put("mail.smtp.socketFactory.port", sport);

    //props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");

    props.put("mail.smtp.socketFactory.fallback", "false");

    props.setProperty("mail.smtp.quitwait", "false");

    // retorna as propriedades
    return props;

  }

  /**
   * 
   * @return
   */
  private Properties setProperties2() {

    //host = "smtp.gmail.com"; 
    // default smtp server _port = "465"; 
    // default smtp port _sport = "465"; 
    // default socketfactory port
    // * _user = ""; 
    // username _pass = ""; 
    // password _from = ""; 
    // email sent
    // from _subject = ""; 
    // email subject _body = ""; 
    // email body

    host = "smtp.terra.com.br"; // default smtp server
    port = "587"; // default smtp port
    sport = "587"; // default socket factory port

    user = "maurocl"; // username
    pass = "Mcl16dcjl"; // password

    from = "maurocl@terra.com.br"; // email sent from

    subject = "Teste"; // email subject

    body = "Corpo do email"; // email body

    debuggable = true; // debug mode on or off - default off

    auth = true; // smtp authentication - default on

    multipart = new MimeMultipart();

    return null;

  }

  //---------------------------------------------------------------------------
  // the getters and setters
  //---------------------------------------------------------------------------

  /**
   * Retorna o corpo da mensagem como uma string
   * 
   * @return
   */
  public String getBody() {

    return body;
  }

  /**
   * Atribui um valor ao corpo da mensagem
   * 
   * @param body
   */
  public void setBody(String body) {

    this.body = body;
  }

  //---------------------------------------------------------------------------
  // more of the getters and setters ...
  //---------------------------------------------------------------------------

  /**
   * Atribui uma lista de endere�os
   * 
   * @param toArr
   */
  public void setTo(String[] toArr) {

    this.to = toArr;
  }

  /**
   * Atribui a origem dos emails, isto �, o remetente do email
   * 
   * @param string
   * 
   */
  public void setFrom(String string) {

    this.from = string;
  }

  /**
   * Atribui o subject da mensagem
   * 
   * @param string
   *          assunto da mensagem
   */
  public void setSubject(String string) {

    this.subject = string;

  }

  /**
   * Obtem o usu�rio da conta de email respons�vel pelo envio do email
   * 
   * @return o usu�rio usado para acessar a conta do email
   */
  public String getUser() {

    return user;
  }

  /**
   * Estabelece um usu�rio da conta de email
   * 
   * @param user
   *          usu�rio da conta de email
   */
  public void setUser(String user) {

    this.user = user;
  }

  /**
   * Obt�m o password do usu�rio
   * 
   * @return o password associado a conta de email do usu�rio
   */
  public String getPass() {

    return pass;
  }

  /**
   * Atribui o password do usu�rio
   * 
   * @param pass
   *          password associado a conta de email do usu�rio
   * 
   */
  public void setPass(String pass) {

    this.pass = pass;
  }

  /**
   * obtem a porta do servidor SMTP
   * 
   * @return
   */
  public String getPort() {

    return port;
  }

  /**
   * Atribui a porta do servidor SMTP
   * 
   * @param port
   *          N� da porta
   */
  public void setPort(String port) {

    this.port = port;
  }

  /**
   * 
   * @return
   */
  public String getSport() {

    return sport;
  }

  /**
   * 
   * @param sport
   */
  public void setSport(String sport) {

    this.sport = sport;
  }

  /**
   * Retorna o host onde o servidor SMTP est� executando
   * 
   * @return
   */
  public String getHost() {

    return host;

  }

  /**
   * Atribui o nome do host SMTP
   * 
   * @param host
   *          nome do host SMTP
   */
  public void setHost(String host) {

    this.host = host;

  }

  /**
   * Indica se o usu�rio est� autenticado
   * 
   * @return true se o usu�rio estiver autenticado ou false caso contr�rio.
   * 
   */
  public boolean isAuth() {

    return auth;
  }

  /**
   * Altera a situa��o de autentica��o do usu�rio
   * 
   * @param auth
   *          true ou false
   */
  public void setAuth(boolean auth) {

    this.auth = auth;
  }

  /**
   * Verifdica se a rotina � debug�vel
   * 
   * @return true rotina debug�vel ou false caso contr�rio
   * 
   */
  public boolean isDebuggable() {

    return debuggable;
  }

  /**
   * 
   * @param debuggable
   */
  public void setDebuggable(boolean debuggable) {

    this.debuggable = debuggable;
  }

  /**
   * Obt�m um Multipart
   * 
   * @return
   * 
   */
  public Multipart getMultipart() {

    return multipart;
  }

  /**
   * 
   * @param multipart
   */
  public void setMultipart(Multipart multipart) {

    this.multipart = multipart;
  }

  /**
   * Obt�m um array dos emails que ser�o enviados ao destinat�rio (to:)
   * 
   * @return um array com os email de cada destinat�rio
   */
  public String[] getTo() {

    return to;
  }

  /**
   * Obt�m o email do remetente
   * 
   * @return o email do rementente
   * 
   */
  public String getFrom() {

    return from;
  }

  /**
   * Obt�m o subject do email
   * 
   * @return o assunto do email
   */
  public String getSubject() {

    return subject;
  }

  /**
   * Obt�m o array dos emails que ser�o enviado em c�pia
   * 
   * @return
   */
  public String[] getCc() {

    return cc;
  }

  /**
   * Seta o array dos emails que ser�o enviados em c�pia oculta
   * 
   * @param cc
   */
  public void setCc(String[] cc) {

    this.cc = cc;
  }

  /**
   * Obt�m o array dos emails que ser�o enviados em c�pia oculta
   * 
   * @return
   */
  public String[] getBcc() {

    return bcc;
  }

  /**
   * Seta o endere�o de email de retorno
   * 
   * @return
   * 
   */
  public String getReplyTo() {

    return replyTo;
  }

  /**
   * 
   * @param replyTo
   */
  public void setReplyTo(String replyTo) {

    this.replyTo = replyTo;
  }

  /**
   * Seta o array dos emails que ser�o enviados em c�pia oculta
   * 
   * @param bcc
   *          um array de email dos destinat�rios em c�pia oculta.
   */
  public void setBcc(String[] bcc) {

    this.bcc = bcc;
  }

  /**
   * Obtem o uso do SSL
   * 
   * @return
   */
  public boolean isSsl() {

    return ssl;
  }

  /**
   * Estabelece o uso do SSL
   * 
   * @param ssl
   * 
   */
  public void setSsl(boolean ssl) {

    this.ssl = ssl;
  }

  @Override
  public String toString() {

    return "Mail [user=" + user + ", pass=" + pass + ", to=" + Arrays.toString(to) + ", cc=" + Arrays.toString(cc) + ", bcc="
        + Arrays.toString(bcc) + ", from=" + from + ", port=" + port + ", sport=" + sport + ", host=" + host + ", subject="
        + subject + ", body=" + body + ", auth=" + auth + ", debuggable=" + debuggable + ", multipart=" + multipart + "]";
  }

  //---------------------------------------------------------------------------

}