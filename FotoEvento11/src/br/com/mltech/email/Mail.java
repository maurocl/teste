package br.com.mltech.email;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
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

import android.util.Log;

/**
 * Classe responsável pelo envio de email usando o JavaMail.
 * 
 * 
 * @author maurocl
 * 
 */
public class Mail extends javax.mail.Authenticator {

  public static final String TAG = "Mail";

  /**
   * nome do usuário da conta de email
   */
  private String user;

  /**
   * senha do usuário da conta de email
   */
  private String pass;

  /**
   * array de endereços de email (to)
   */
  private String[] to;

  /**
   * array de endereços de email (cc)
   */
  private String[] cc;

  /**
   * array de endereços de email (bcc)
   */
  private String[] bcc;

  /**
   * email do remetente
   */
  private String from;

  /**
   * endereço que deve ser usado para responder o email
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
   * endereço do servidor SMTP
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
   * Indica se o usuário está autenticado ou não
   */
  private boolean auth;

  /**
   * Indica se a rotina é debugavel
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
   *          Username nome do usuário da conta de email
   * 
   * @param pass
   *          Password senha do usuário da conta de email
   * 
   */
  public Mail(String user, String pass) {

    // executa o construtor inicial (sem parâmetros).
    this();

    // atualiza os usuário e senha
    this.user = user;
    this.pass = pass;

  }

  /**
   * Envia o email.<br>
   * 
   * Usa os atributos para preencher ...<br>
   * 
   * @return true email enviado com sucesso ou false caso contrário
   * 
   * @throws Exception
   *           se houver algum erro.
   */
  public boolean send() throws Exception {

    /*
     * m.setDebuggable(true); m.setAuth(true);
     * 
     * m.setHost("smtp.mltech.com.br"); m.setPort("587");
     * 
     * m.setFrom("maurocl@mltech.com.br");
     * 
     * m.setSsl(false);
     * 
     * m.setTo(new String[] { "maurocl@terra.com.br" }); m.setBcc(new String[] {
     * "maurocl@mltech.com.br" });
     * 
     * m.setSubject("test02"); m.setBody("test02 - envio de email");
     */

    // cria uma instância das propriedades
    Properties props = setProperties();

    // grava as propriedades em um arquivo
    savePropertiesFile(props);
    
    if((props!=null) && (props.isEmpty())) {
      Log.w(TAG,"send() - propriedades está vazia");     
    }
    
    if (!props.isEmpty()) {

      Log.w(TAG,"send() - nº de entradas: "+props.size());
      
      Enumeration<Object> e = props.keys();
      
      while (e.hasMoreElements()) {
        String key = (String) e.nextElement();
        Log.d(TAG, "send() - Key=" + key + ", Value=" + props.get(key));
      }

    }

    Log.d(TAG,"toString2(): "+toString2());
    
    
    Log.d(TAG,"!user.equals('')   : "+!user.equals(""));
    Log.d(TAG,"!pass.equals('')   : "+!pass.equals("")  );
    Log.d(TAG,"to.length > 0      : "+(to.length > 0) );
    Log.d(TAG,"!from.equals('')   : "+!from.equals("") );
    Log.d(TAG,"!subject.equals(''): "+!subject.equals(""));
    Log.d(TAG,"!body.equals('')   : "+!body.equals(""));
    
    
    
    if (!user.equals("")     && 
        !pass.equals("")     && 
        (to.length > 0)      && 
        !from.equals("")     && 
        !subject.equals("")  && 
        !body.equals("")
    ) {

      Log.d(TAG,"110");
      
      // obtém uma sessão para envio de email
      Session session = Session.getInstance(props, this);

      Log.d(TAG,"120");
      
      // Session session = Session.getInstance(props, null);

      // cria uma mensagem (associada a sessão)
      MimeMessage msg = new MimeMessage(session);

      // atualiza a origem da mensagem
      msg.setFrom(new InternetAddress(from));

      Log.d(TAG,"130");
      
      // -----------------------------------------------------------------------
      // To:
      // -----------------------------------------------------------------------
      InternetAddress[] addressTo = new InternetAddress[to.length];

      for (int i = 0; i < to.length; i++) {
        addressTo[i] = new InternetAddress(to[i]);
      }

      msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);

      Log.d(TAG,"140");
      
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

      Log.d(TAG,"150");
      
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

      Log.d(TAG,"160");
      
      // subject do email
      msg.setSubject(this.getSubject());

      // data de envio do email
      msg.setSentDate(new Date());

      // setup message body
      BodyPart messageBodyPart = new MimeBodyPart();

      Log.d(TAG,"170");
      
      // adiciona o corpo do email
      messageBodyPart.setText(this.getBody());

      Log.d(TAG,"180");
      multipart.addBodyPart(messageBodyPart);

      Log.d(TAG,"183");
      
      // Put parts in message
      msg.setContent(multipart);
            
      
      Log.d(TAG,"185");
      
      // não sei se é necessário
     // msg.saveChanges();

      Log.d(TAG,"190");
      
      // send email
     Transport.send(msg);

      
      Log.d(TAG,"***** retornando true");
      return true;

    } else {
      Log.d(TAG,"***** retornando false");
      return false;

    }

  }

  /**
   * Envia um email usando os parâmetros.
   * 
   * @param to
   *          Endereço do destinatário
   * @param bcc
   *          Endereço do bcc
   * @param from
   *          Endereço do remetente
   * @param subject
   *          Assunto do email
   * @param body
   *          Corpo do email
   * @param foto
   *          Foto
   * 
   * @return true email enviado com sucesso ou false caso contrário
   * 
   * @throws Exception
   *           Lançada em caso de erros.
   * 
   */
  public boolean send(String to, String bcc, String from, String subject, String body, String foto) throws Exception {

    Properties props = setProperties();

    if (!user.equals("") && !pass.equals("") && (!to.equals("")) && (!bcc.equals("")) && (!foto.equals("")) && !from.equals("")
        && !subject.equals("") && !body.equals("")) {

      Session session = Session.getInstance(props, this);
      // Session session = Session.getInstance(props, null);

      MimeMessage msg = new MimeMessage(session);

      msg.setFrom(new InternetAddress(from));

      // -----------------------------------------------------------------------
      // To:
      // -----------------------------------------------------------------------
      InternetAddress[] addressTo = { new InternetAddress(to) };

      msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);

      // -----------------------------------------------------------------------
      // Bcc:
      // -----------------------------------------------------------------------
      InternetAddress[] addressBcc = { new InternetAddress(bcc) };

      msg.setRecipients(MimeMessage.RecipientType.BCC, addressBcc);

      // -----------------------------------------------------------------------
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

      // não sei se é necessário
      msg.saveChanges();

      // send email
      Transport.send(msg);

      return true;

    } else {

      Log.w(TAG, "send() - falha no envio do email");

      return false;

    }

  }

  /**
   * Adiciona um arquivo como anexo ao email.
   * 
   * @param filename
   *          Nome do arquivo anexado
   * 
   * @throws Exception
   *           lançada em caso de erro
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
    // lança MessagingException
    messageBodyPart.setFileName(filename);

    // Adiciona o BodyPart ao multipart.
    multipart.addBodyPart(messageBodyPart);

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
   * @return uma instância de Properties.
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
   * Atribui uma lista de endereços
   * 
   * @param toArr
   */
  public void setTo(String[] toArr) {

    this.to = toArr;
  }

  /**
   * Atribui a origem dos emails, isto é, o remetente do email
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
   * Obtem o usuário da conta de email responsável pelo envio do email
   * 
   * @return o usuário usado para acessar a conta do email
   */
  public String getUser() {

    return user;
  }

  /**
   * Estabelece um usuário da conta de email
   * 
   * @param user
   *          usuário da conta de email
   */
  public void setUser(String user) {

    this.user = user;
  }

  /**
   * Obtém o password do usuário
   * 
   * @return o password associado a conta de email do usuário
   */
  public String getPass() {

    return pass;
  }

  /**
   * Atribui o password do usuário
   * 
   * @param pass
   *          password associado a conta de email do usuário
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
   *          Nº da porta
   */
  public void setPort(String port) {

    this.port = port;
  }

  /**
   * Obtém o nº da porta segura.
   * 
   * @return O n º da porta segura.
   */
  public String getSport() {

    return sport;
  }

  /**
   * Atribui o nº da porta segura;
   * 
   * @param sport
   *          O nº da porta segura.
   * 
   */
  public void setSport(String sport) {

    this.sport = sport;
  }

  /**
   * Retorna o host onde o servidor SMTP está executando
   * 
   * @return O endereço do servidor SMTP usado.
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
   * Indica se o usuário está autenticado.
   * 
   * @return true se o usuário estiver autenticado ou false caso contrário.
   * 
   */
  public boolean isAuth() {

    return auth;
  }

  /**
   * Altera a situação de autenticação do usuário
   * 
   * @param auth
   *          true ou false
   */
  public void setAuth(boolean auth) {

    this.auth = auth;
  }

  /**
   * Verifica se a rotina é debugável.
   * 
   * @return true rotina debugável ou false caso contrário
   * 
   */
  public boolean isDebuggable() {

    return debuggable;
  }

  /**
   * Atribui o use de debug.
   * 
   * @param debuggable
   *          true se debugavel ou false caso contrário.
   */
  public void setDebuggable(boolean debuggable) {

    this.debuggable = debuggable;
  }

  /**
   * Obtém um Multipart.
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
   * Obtém um array dos emails que serão enviados ao destinatário (to:)
   * 
   * @return um array com os email de cada destinatário
   */
  public String[] getTo() {

    return to;
  }

  /**
   * Obtém o email do remetente
   * 
   * @return o email do rementente
   * 
   */
  public String getFrom() {

    return from;
  }

  /**
   * Obtém o subject do email.
   * 
   * @return o assunto do email
   */
  public String getSubject() {

    return subject;
  }

  /**
   * Obtém o array dos emails que serão enviado em cópia
   * 
   * @return
   */
  public String[] getCc() {

    return cc;
  }

  /**
   * Atribui o array dos emails que serão enviados em cópia oculta
   * 
   * @param cc
   *          array de strings onde cada elemento é um endereço de email que
   *          será enviado em CC.
   * 
   */
  public void setCc(String[] cc) {

    this.cc = cc;
  }

  /**
   * Obtém o array dos emails que serão enviados em cópia oculta
   * 
   * @return um
   */
  public String[] getBcc() {

    return bcc;
  }

  /**
   * Atribui o endereço de email de retorno.
   * 
   * @return o endereço de email que deverá ser usado para retornar a mensagem.
   * 
   */
  public String getReplyTo() {

    return replyTo;
  }

  /**
   * Atribui o endereço de email de retorno
   * 
   * @param replyTo
   *          o endereço de email de retorno.
   * 
   */
  public void setReplyTo(String replyTo) {

    this.replyTo = replyTo;
  }

  /**
   * Seta o array dos emails que serão enviados em cópia oculta
   * 
   * @param bcc
   *          um array de email dos destinatários em cópia oculta.
   */
  public void setBcc(String[] bcc) {

    this.bcc = bcc;
  }

  /**
   * Obtem o uso do SSL (Secure Socket Layer).
   * 
   * @return true se estiver usando SSL ou false caso contrário.
   */
  public boolean isSsl() {

    return ssl;
  }

  /**
   * Estabelece o uso do SSL (Secure Socket Layer).
   * 
   * @param ssl
   *          true para usar SSL ou false caso contrário.
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
   * @return Uma instância de properties.
   * 
   */
  private Properties loadPropertiesFile(String filename) throws FileNotFoundException, IOException {

    // o arquivo encontra-se no mesmo diretório
    File file = new File(filename);

    Properties emailProperties = new Properties();

    FileInputStream inputStream = new FileInputStream(file);

    // lê os dados que estão no arquivo
    emailProperties.load(inputStream);

    inputStream.close();

    return emailProperties;

  }

  /**
   * Grava o arquivo de propriedades.<br>
   * 
   * O arquivo padrão de gravação é dado por: "mail2.properties".
   * 
   * @throws FileNotFoundException
   *           Arquivo não encontrado
   * @throws IOException
   *           Erro de I/O
   * 
   */
  private void savePropertiesFile(Properties emailProperties) throws FileNotFoundException, IOException {

    if (emailProperties == null) {
      throw new IllegalArgumentException("Properties é nula.");
    }

    File file = new File("/mnt/sdcard/Pictures/mail2.properties");

    FileOutputStream fos = null;

    fos = new FileOutputStream(file);

    // grava os dados no arquivo
    emailProperties.store(fos, "Configurações do arquivo mail.properties");

    fos.close();

  }

  /**
   * Exibe detalhes sobre uma exceção gerada.
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

    return "Mail ["+
    "\nuser=" + user + 
    ", \npass=" + pass + 
    ", \nto*=" + Arrays.toString(to) + 
    ", \ncc*=" + Arrays.toString(cc) + 
    ", \nbcc*="+ Arrays.toString(bcc) + 
    ", \nfrom=" + from + 
    ", \nreplyTo=" + replyTo + 
    ", \nport=" + port + 
    ", \nsport=" + sport + 
    ", \nhost=" + host + 
    ", \nsubject=" + subject + 
    ", \nbody=" + body + 
    ", \nauth=" + auth + 
    ", \ndebuggable=" + debuggable + 
    ", \nssl=" + ssl    + 
    ", \nmultipart=" + multipart + 
    "]";
  }

  
}
