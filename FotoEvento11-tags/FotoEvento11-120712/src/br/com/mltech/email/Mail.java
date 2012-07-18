package br.com.mltech.email;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import android.util.Log;

/**
 * Classe respons�vel pelo envio de email usando o JavaMail.
 * 
 * 
 * @author maurocl
 * 
 */
public class Mail extends javax.mail.Authenticator {

  public static final String TAG = "Mail";

  /**
   * nome do usu�rio da conta de email
   */
  private String user;

  /**
   * senha do usu�rio da conta de email
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
   * endere�o que deve ser usado para responder o email
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

  private String filename;

  /**
   * Construtor
   * 
   */
  public Mail() {

    this.auth = true; // smtp authentication - default on

    this.multipart = new MimeMultipart();

    // There is something wrong with MailCap, javamail can not find a
    // handler for the multipart/mixed part, so this bit needs to be added.

    MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();

    mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
    mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
    mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
    mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
    mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");

    CommandMap.setDefaultCommandMap(mc);

  }

  /**
   * Construtor
   * 
   * @param user
   *          Username nome do usu�rio da conta de email
   * 
   * @param pass
   *          Password senha do usu�rio da conta de email
   * 
   */
  public Mail(String user, String pass) {

    // executa o construtor inicial (sem par�metros).
    this();

    // atualiza os usu�rio e senha
    this.user = user;
    this.pass = pass;

  }

  /**
   * Envia o email.<br>
   * 
   * Usa os atributos para preencher ...<br>
   * 
   * @return true email enviado com sucesso ou false caso contr�rio
   * 
   * @throws Exception
   *           se houver algum erro.
   */
  public boolean send() throws Exception {

    // m.setDebuggable(true); 
    // m.setAuth(true);
    // m.setHost("smtp.mltech.com.br"); 
    // m.setPort("587");
    // m.setFrom("maurocl@mltech.com.br");
    // m.setSsl(false);      
    // m.setTo(new String[] { "maurocl@terra.com.br" }); 
    // m.setBcc(new String[] {"maurocl@mltech.com.br" });     
    // m.setSubject("test02"); m.setBody("test02 - envio de email");

    // cria uma inst�ncia das propriedades de configura��o do email.
    Properties props = setProperties();

    // grava as propriedades num arquivo padr�o
    savePropertiesFile(props);

    if ((props != null) && (props.isEmpty())) {
      Log.w(TAG, "send() - propriedades est� vazia");
    }

    if (!props.isEmpty()) {

      Log.w(TAG, "send() - n� de entradas: " + props.size());

      // obt�m uma enumera��o com o nome das propriedades
      Enumeration<Object> e = props.keys();

      while (e.hasMoreElements()) {
        String key = (String) e.nextElement();
        // exibe a chave e seu valor associado
        Log.d(TAG, "send() - Key=" + key + ", Value=" + props.get(key));
      }

    }

    Log.d(TAG, "toString2(): " + toString2());

    Log.d(TAG, "!user.equals('')   : " + !user.equals(""));
    Log.d(TAG, "!pass.equals('')   : " + !pass.equals(""));
    Log.d(TAG, "to.length > 0      : " + (to.length > 0));
    Log.d(TAG, "!from.equals('')   : " + !from.equals(""));
    Log.d(TAG, "!subject.equals(''): " + !subject.equals(""));
    Log.d(TAG, "!body.equals('')   : " + !body.equals(""));

    if (!user.equals("") &&
        !pass.equals("") &&
        (to.length > 0) &&
        !from.equals("") &&
        !subject.equals("") &&
        !body.equals("")) {

      // obt�m uma sess�o para envio de email
      Session session = Session.getInstance(props, this);

      session.setDebug(true);

      // Session session = Session.getInstance(props, null);

      // cria uma mensagem (associada a sess�o)
      MimeMessage msg = new MimeMessage(session);

      Log.d(TAG, "-------------------------------------------------------------------");
      Log.d(TAG, "--- SESSION");
      Log.d(TAG, "-------------------------------------------------------------------");

      showSession(session);

      assert (from != null);

      // atualiza a origem da mensagem
      msg.setFrom(new InternetAddress(from));

      // -----------------------------------------------------------------------
      // To:
      // -----------------------------------------------------------------------
      InternetAddress[] addressTo = new InternetAddress[to.length];

      for (int i = 0; i < to.length; i++) {
        addressTo[i] = new InternetAddress(to[i]);
      }

      msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);

      // -----------------------------------------------------------------------
      // Cc:
      // -----------------------------------------------------------------------
      if (cc != null) {
        InternetAddress[] addressCc = new InternetAddress[cc.length];

        for (int i = 0; i < cc.length; i++) {
          addressCc[i] = new InternetAddress(cc[i]);
        }

        msg.setRecipients(MimeMessage.RecipientType.CC, addressCc);

      }

      // -----------------------------------------------------------------------
      // Bcc:
      // -----------------------------------------------------------------------
      if (bcc != null) {
        InternetAddress[] addressBcc = new InternetAddress[bcc.length];

        for (int i = 0; i < bcc.length; i++) {
          addressBcc[i] = new InternetAddress(bcc[i]);
        }

        msg.setRecipients(MimeMessage.RecipientType.BCC, addressBcc);
        // -----------------------------------------------------------------------
      }

      //
      // ReplyTo:
      //
      InternetAddress[] x = new InternetAddress[1];
      x[0] = new InternetAddress(from);
      msg.setReplyTo(x);

      // subject do email
      msg.setSubject(this.getSubject());

      // data de envio do email
      msg.setSentDate(new Date());

      //------------------------
      // cria��o do 1� BodyPart
      //------------------------

      // setup message body
      BodyPart messageBodyPart = new MimeBodyPart();

      // adiciona o corpo do email
      messageBodyPart.setText(this.getBody());

      // acrescenta o primeiro BodyPart - o corpo (texto) do email
      multipart.addBodyPart(messageBodyPart);

      Log.d(TAG, "send() - multipart.getCount(): " + multipart.getCount());

      // 
      Log.d(TAG, "send() - getFilename=" + this.getFilename());

      // adiciona um anexo ao email
      // o anexo � adicionado como um novo BodyPart
      this.addAttachment(this.getFilename());

      Log.d(TAG, "send() - multipart.getCount(): " + multipart.getCount());

      // Put parts in message
      // Especifica que o conte�do da mensagem � um multipart.
      msg.setContent(multipart);

      Log.d(TAG, "-------------------------------------------------------------------");
      Log.d(TAG, "--- MULTIPART");
      Log.d(TAG, "-------------------------------------------------------------------");
      showMultipart(multipart);

      Log.d(TAG, "-------------------------------------------------------------------");
      Log.d(TAG, "--- MESSAGE");
      Log.d(TAG, "-------------------------------------------------------------------");
      showMessage(msg);

      // send email
      Transport.send(msg);

      Log.d(TAG, "***** retornando true");
      return true;

    } else {
      Log.d(TAG, "***** retornando false");
      return false;

    }

  }

  public boolean send2() throws Exception {

    // m.setDebuggable(true); 
    // m.setAuth(true);
    // m.setHost("smtp.mltech.com.br"); 
    // m.setPort("587");
    // m.setFrom("maurocl@mltech.com.br");
    // m.setSsl(false);      
    // m.setTo(new String[] { "maurocl@terra.com.br" }); 
    // m.setBcc(new String[] {"maurocl@mltech.com.br" });     
    // m.setSubject("test02"); m.setBody("test02 - envio de email");

    setDebuggable(true); 
    setAuth(true);
    setHost("smtp.mltech.com.br"); 
    setPort("587");
    setFrom("maurocl@mltech.com.br");
    setSsl(false);      
    setTo(new String[] { "maurocl@terra.com.br" }); 
    setBcc(new String[] {"maurocl@mltech.com.br" });     
    setSubject("test02");
    setBody("test02 - envio de email");
    
    Properties props = setProperties();

    // obt�m uma sess�o para envio de email
    Session session = Session.getInstance(props, this);

    session.setDebug(true);

    // cria uma mensagem (associada a sess�o)
    MimeMessage msg = new MimeMessage(session);

    // atualiza a origem da mensagem
    msg.setFrom(new InternetAddress(from));

    InternetAddress[] addressTo = new InternetAddress[1];
    addressTo[0] = new InternetAddress(to[0]);
    msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);

    // -----------------------------------------------------------------------

    InternetAddress[] x = new InternetAddress[1];
    x[0] = new InternetAddress(getFrom());
    msg.setReplyTo(x);

    // subject do email
    msg.setSubject(this.getSubject());

    // data de envio do email
    msg.setSentDate(new Date());

    //------------------------
    // cria��o do 1� BodyPart
    //------------------------

    // setup message body
    BodyPart mbp1 = new MimeBodyPart();

    // adiciona o corpo do email
    mbp1.setText(this.getBody());

    // acrescenta o primeiro BodyPart - o corpo (texto) do email
    multipart.addBodyPart(mbp1);

    //------------------------
    // cria��o do 2� BodyPart
    //------------------------

    // Cria um BodyPart.
    BodyPart mbp2 = new MimeBodyPart();

    // Cria um DataSource associado ao arquivo de anexo.
    DataSource source = new FileDataSource(filename);

    // Estabelece o handler para o arquivo.
    mbp2.setDataHandler(new DataHandler(source));

    // Associa o arquivo
    // lan�a MessagingException
    mbp2.setFileName(filename);

    // Adiciona o BodyPart ao multipart.
    multipart.addBodyPart(mbp2);
    
    // Put parts in message
    // Especifica que o conte�do da mensagem � um multipart.
    msg.setContent(multipart);

    Log.d(TAG, "-------------------------------------------------------------------");
    Log.d(TAG, "--- MULTIPART");
    Log.d(TAG, "-------------------------------------------------------------------");
    showMultipart(multipart);

    Log.d(TAG, "-------------------------------------------------------------------");
    Log.d(TAG, "--- MESSAGE");
    Log.d(TAG, "-------------------------------------------------------------------");
    showMessage(msg);

    // send email
    Transport.send(msg);

    return true;

  }

  /**
   * Exibe as informa��es sobre Session.
   * 
   * @param s
   *          Inst�ncia de Session
   * 
   */
  public void showSession(Session s) {

    s.getDebug();

    s.getDebugOut();
    s.getProviders();

    Properties p = s.getProperties();

    Enumeration e = p.elements();

    int i = 0;

    Set<Entry<Object, Object>> conjunto = p.entrySet();

    for (Entry elem : conjunto) {
      Log.d(TAG, "showSession(): " + i + " - Key=" + elem.getKey() + ", Value=" + elem.getValue());
      i++;
    }

  }

  /**
   * showMessage(Message msg)
   * 
   * @param msg
   * @throws Exception
   */
  public void showMessage(Message msg) throws Exception {

    if (msg instanceof MimeMessage) {
      MimeMessage mm = (MimeMessage) msg;

      Log.d(TAG, "showMessage() - mm.getFrom: " + mm.getFrom());
      Log.d(TAG, "showMessage() - mm.getRecipients(TO): " + mm.getRecipients(RecipientType.TO));
      Log.d(TAG, "showMessage() - mm.getRecipients(CC): " + mm.getRecipients(RecipientType.CC));
      Log.d(TAG, "showMessage() - mm.getRecipients(BCC): " + mm.getRecipients(RecipientType.BCC));
      Log.d(TAG, "showMessage() - mm.getSubject(): " + mm.getSubject());
      Log.d(TAG, "showMessage() - mm.getSentDate: " + mm.getSentDate());

      Log.d(TAG, "showMessage() - mm.getMessageNumber: " + mm.getMessageNumber());
      Log.d(TAG, "showMessage() - mm.getContentType: " + mm.getContentType());
      Log.d(TAG, "showMessage() - mm.getSender: " + mm.getSender());
      Log.d(TAG, "showMessage() - mm.getEncoding: " + mm.getEncoding());

      int i = 0;
      Enumeration e = mm.getAllHeaderLines();
      for (; e.hasMoreElements();) {
        Log.d(TAG, "showMessage() - enum - " + i + " - " + e.nextElement());
        i++;
      }

    }

    try {

      Log.d(TAG, "showMessage() - getContentType: " + msg.getContentType());
      Log.d(TAG, "showMessage() - getContent: " + msg.getContent());
      Log.d(TAG, "showMessage() - getSubject: " + msg.getSubject());

      Log.d(TAG, "showMessage() - getDescription: " + msg.getDescription());
      Log.d(TAG, "showMessage() - getFileName: " + msg.getFileName());
      Log.d(TAG, "showMessage() - getLineCount: " + msg.getLineCount());

      Log.d(TAG, "showMessage() - getLineCount: " + msg.getLineCount());
      Log.d(TAG, "showMessage() - getMessageNumber: " + msg.getMessageNumber());

      Log.d(TAG, "showMessage() - getSentDate: " + msg.getSentDate());

      Log.d(TAG, "showMessage() - getFrom: ");
      showAddress(msg.getFrom());

      Log.d(TAG, "showMessage() - getReplyTo: ");
      showAddress(msg.getReplyTo());

      Log.d(TAG, "showMessage() - getSize: " + msg.getSize());

    } catch (IOException e) {
      Log.w(TAG, "showMessage() - ", e);
    } catch (MessagingException e) {
      Log.w(TAG, "showMessage() - ", e);
    }

  }

  /**
   * 
   * @param a
   */
  public void showAddress(Address[] a) {

    for (Address x : a) {
      Log.d(TAG, "showAddress() - " + x.toString());
    }
  }

  public String toAddress(Address[] a) {

    StringBuffer sb = new StringBuffer();

    for (Address x : a) {
      sb.append(x.toString());
    }

    return sb.toString();

  }

  /**
   * 
   * @param mp
   * @throws IOException
   */
  public void showMultipart(Multipart mp) throws IOException {

    MimeMultipart mmp;

    if (mp instanceof MimeMultipart) {

      mmp = (MimeMultipart) mp;

      Log.d(TAG, "showMultipart() - toString: " + mmp.toString());
      Log.d(TAG, "showMultipart() - getContentType: " + mmp.getContentType());

      try {

        Log.d(TAG, "showMultipart() - getCount():  " + mp.getCount());

        mmp.getCount();

        for (int i = 0; i < mmp.getCount(); i++) {

          Log.d(TAG, "showMultipart(): showBodyPart ==> " + i);

          showBodyPart(mmp.getBodyPart(i));

        }

      } catch (MessagingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }

    mp.getContentType();

  }

  /**
   * 
   * @param bp
   * @throws MessagingException
   * @throws IOException
   */
  void showBodyPart(BodyPart bp) throws MessagingException, IOException {

    MimeBodyPart mbp = null;

    if (bp instanceof MimeBodyPart) {

      mbp = (MimeBodyPart) bp;

      Log.d(TAG, "showBodyPart(): getContentType: " + mbp.getContentType());
      Log.d(TAG, "showBodyPart(): getContent: " + mbp.getContent());

      Log.d(TAG, "showBodyPart(): toString(): " + mbp.toString());
      Log.d(TAG, "showBodyPart(): getContentID: " + mbp.getContentID());
      Log.d(TAG, "showBodyPart(): getDescription: " + mbp.getDescription());
      Log.d(TAG, "showBodyPart(): getFileName: " + mbp.getFileName());

      Log.d(TAG, "showBodyPart(): getLineCount: " + mbp.getLineCount());

      Log.d(TAG, "showBodyPart(): getEncoding: " + mbp.getEncoding());
      Log.d(TAG, "showBodyPart(): getSize: " + mbp.getSize());

    }

  }

  /**
   * Obt�m o nome do arquivo que ser� anexado
   * 
   * @return O nome do arquivo
   */
  public String getFilename() {

    return filename;
  }

  /**
   * Estabelece o nome do arquivo do anexo
   * 
   * @param filename
   *          Nome completo do arquivo
   * 
   */
  public void setFilename(String filename) {

    this.filename = filename;
  }

  /**
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

    // Cria um BodyPart.
    BodyPart messageBodyPart = new MimeBodyPart();

    // Cria um DataSource associado ao arquivo de anexo.
    DataSource source = new FileDataSource(filename);

    // Estabelece o handler para o arquivo.
    messageBodyPart.setDataHandler(new DataHandler(source));

    // Associa o arquivo
    // lan�a MessagingException
    messageBodyPart.setFileName(filename);

    // Adiciona o BodyPart ao multipart.
    this.multipart.addBodyPart(messageBodyPart);

  }

  /**
   * Called when password authentication is needed. Subclasses should override
   * the default implementation, which returns null.<br>
   * 
   * Note that if this method uses a dialog to prompt the user for this
   * information, the dialog needs to block until the user supplies the
   * information.<br>
   * 
   * This method can not simply return after showing the dialog.<br>
   * 
   * Returns: The PasswordAuthentication collected from the user, or null if
   * none is provided.<br>
   * 
   */
  @Override
  public PasswordAuthentication getPasswordAuthentication() {

    return new PasswordAuthentication(user, pass);

  }

  /**
   * Estabelece as propriedades do email.
   * 
   * @return uma inst�ncia de Properties.
   * 
   */
  private Properties setProperties() {

    Properties props = new Properties();

    if (this.isDebuggable()) {
      props.put("mail.debug", this.isDebuggable() ? "true" : "false");
    }

    if (this.isAuth()) {
      props.put("mail.smtp.auth", this.isAuth() ? "true" : "false");
    }

    props.put("mail.transport.protocol", "smtp");

    props.put("mail.smtp.host", this.getHost());

    props.put("mail.smtp.port", this.getPort());

    if (this.isSsl()) {

      props.put("mail.smtp.socketFactory.port", this.getSport());

      props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

    }

    props.put("mail.smtp.socketFactory.fallback", "false");

    props.put("mail.smtp.quitwait", "false");

    Enumeration<Object> e = props.elements();

    while (e.hasMoreElements()) {
      Log.d(TAG, "setProperties(): " + e.nextElement());

    }

    // retorna as propriedades
    return props;

  }

  // ---------------------------------------------------------------------------
  // the getters and setters
  // ---------------------------------------------------------------------------

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

  // ---------------------------------------------------------------------------
  // more of the getters and setters ...
  // ---------------------------------------------------------------------------

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
   * Obt�m o n� da porta segura.
   * 
   * @return O n � da porta segura.
   */
  public String getSport() {

    return sport;
  }

  /**
   * Atribui o n� da porta segura;
   * 
   * @param sport
   *          O n� da porta segura.
   * 
   */
  public void setSport(String sport) {

    this.sport = sport;
  }

  /**
   * Retorna o host onde o servidor SMTP est� executando
   * 
   * @return O endere�o do servidor SMTP usado.
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
   * Indica se o usu�rio est� autenticado.
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
   * Verifica se a rotina � debug�vel.
   * 
   * @return true rotina debug�vel ou false caso contr�rio
   * 
   */
  public boolean isDebuggable() {

    return debuggable;
  }

  /**
   * Atribui o use de debug.
   * 
   * @param debuggable
   *          true se debugavel ou false caso contr�rio.
   */
  public void setDebuggable(boolean debuggable) {

    this.debuggable = debuggable;
  }

  /**
   * Obt�m um Multipart.
   * 
   * @return Um multipart.
   * 
   */
  public Multipart getMultipart() {

    return multipart;
  }

  /**
   * Atribui um multipart
   * 
   * @param multipart
   * 
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
   * Obt�m o subject do email.
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
   * Atribui o array dos emails que ser�o enviados em c�pia oculta
   * 
   * @param cc
   *          array de strings onde cada elemento � um endere�o de email que
   *          ser� enviado em CC.
   * 
   */
  public void setCc(String[] cc) {

    this.cc = cc;
  }

  /**
   * Obt�m o array dos emails que ser�o enviados em c�pia oculta
   * 
   * @return um
   */
  public String[] getBcc() {

    return bcc;
  }

  /**
   * Atribui o endere�o de email de retorno.
   * 
   * @return o endere�o de email que dever� ser usado para retornar a mensagem.
   * 
   */
  public String getReplyTo() {

    return replyTo;
  }

  /**
   * Atribui o endere�o de email de retorno
   * 
   * @param replyTo
   *          o endere�o de email de retorno.
   * 
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
   * Obtem o uso do SSL (Secure Socket Layer).
   * 
   * @return true se estiver usando SSL ou false caso contr�rio.
   */
  public boolean isSsl() {

    return ssl;
  }

  /**
   * Estabelece o uso do SSL (Secure Socket Layer).
   * 
   * @param ssl
   *          true para usar SSL ou false caso contr�rio.
   * 
   */
  public void setSsl(boolean ssl) {

    this.ssl = ssl;
  }

  // ---------------------------------------------------------------------------

  /**
   * Carrega o arquivo de propriedades localizado no arquivo fornecido.
   * 
   * @param filename
   *          Nome do arquivo de propriedades
   * 
   * @throws FileNotFoundException
   * 
   * @throws IOException
   * 
   * @return Uma inst�ncia de properties.
   * 
   */
  private Properties loadPropertiesFile(String filename) throws FileNotFoundException, IOException {

    // o arquivo encontra-se no mesmo diret�rio
    File file = new File(filename);

    Properties emailProperties = new Properties();

    FileInputStream inputStream = new FileInputStream(file);

    // l� os dados que est�o no arquivo
    emailProperties.load(inputStream);

    inputStream.close();

    return emailProperties;

  }

  /**
   * Grava o arquivo de propriedades.<br>
   * 
   * O arquivo padr�o de grava��o � dado por: "mail2.properties".
   * 
   * @throws FileNotFoundException
   *           Arquivo n�o encontrado
   * @throws IOException
   *           Erro de I/O
   * 
   */
  private void savePropertiesFile(Properties emailProperties) throws FileNotFoundException, IOException {

    if (emailProperties == null) {
      throw new IllegalArgumentException("Properties � nula.");
    }

    File file = new File("/mnt/sdcard/Pictures/mail2.properties");

    FileOutputStream fos = null;

    fos = new FileOutputStream(file);

    // grava os dados no arquivo
    emailProperties.store(fos, "Configura��es do arquivo mail.properties");

    fos.close();

  }

  /**
   * Exibe detalhes sobre uma exce��o gerada.
   * 
   * @param e
   *          Exception
   * 
   */
  public void showException(Exception e) {

    Log.w(TAG, "showException() - getMessage(): " + e.getMessage());
    Log.w(TAG, "showException() - getLocalizedMessage(): " + e.getLocalizedMessage());
    Log.w(TAG, "showException() - getCause(): " + e.getCause());
    Log.w(TAG, "showException() - Exception - ", e);
  }

  @Override
  public String toString() {

    return "Mail [user=" + user + ", pass=" + pass + ", to=" + Arrays.toString(to) + ", cc=" + Arrays.toString(cc) + ", bcc="
        + Arrays.toString(bcc) + ", from=" + from + ", replyTo=" + replyTo + ", port=" + port + ", sport=" + sport + ", host="
        + host + ", subject=" + subject + ", body=" + body + ", auth=" + auth + ", debuggable=" + debuggable + ", ssl=" + ssl
        + ", multipart=" + multipart + "]";
  }

  public String toString2() {

    return "Mail [" +
        "\nuser=" + user +
        ", \n  pass=" + pass +
        ", \n  to*=" + Arrays.toString(to) +
        ", \n  cc*=" + Arrays.toString(cc) +
        ", \n  bcc*=" + Arrays.toString(bcc) +
        ", \n  from=" + from +
        ", \n  replyTo=" + replyTo +
        ", \n  port=" + port +
        ", \n  sport=" + sport +
        ", \n  host=" + host +
        ", \n  subject=" + subject +
        ", \n  body=" + body +
        ", \n  auth=" + auth +
        ", \n  debuggable=" + debuggable +
        ", \n  ssl=" + ssl +
        ", \n  multipart=" + multipart +
        "]";
  }

  
  
  
}
