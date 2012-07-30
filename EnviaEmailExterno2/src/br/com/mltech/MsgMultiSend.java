
package br.com.mltech;

import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * MsgMultiSendSample creates a simple multipart/mixed message and sends it.
 * Both body parts are text/plain.
 * 
 * <p>
 * usage: <code>java MsgMultiSendSample 
 * <i>to from smtp true|false</i>
 * 
 * </code> where <i>to</i> and <i>from</i> are the destination and origin email
 * addresses, respectively, and <i>smtp</i> is the hostname of the machine that
 * has smtp server running. The last parameter either turns on or turns off
 * debugging during sending.
 * 
 * @author Max Spivak
 */
public class MsgMultiSend extends javax.mail.Authenticator {

  private String user;

  private String pass;

  //static String msgText1 = "This is a message body.\nHere's line two.";
  static String msgText1 = "This is a message body. Here's line two.";

  static String msgText2 = "This is the text in the message attachment.";

  /**
   * Construtor
   * 
   */
  public MsgMultiSend() {

    // There is something wrong with MailCap, javamail can not find a
    // handler for the multipart/mixed part, so this bit needs to be added.

    MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();

    mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
    mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
    mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
    mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
    mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");

    CommandMap.setDefaultCommandMap(mc);

    System.out.println("==> MsgMultiSendSample()");

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
  public MsgMultiSend(String user, String pass) {

    // executa o construtor inicial (sem parâmetros).
    this();

    // atualiza os usuário e senha
    this.user = user;
    this.pass = pass;

    System.out.println("==> MsgMultiSendSample: " + user + ", " + pass);

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

  public void executa() throws Exception {

    String to = "maurocl@terra.com.br";
    String from = "maurocl@mltech.com.br";

    boolean debug = true;

    // create some properties and get the default Session
    Properties props = new Properties();

    props.put("mail.smtp.host", "smtp.mltech.com.br");
    props.put("mail.protocol", "smtp.mltech.com.br");
    props.put("mail.protocol.user", "maurocl@mltech.com.br");
    props.put("mail.smtp.port", "587");
    props.put("mail.user", "maurocl@mltech.com.br");
    props.put("mail.from", "maurocl@mltech.com.br");
    props.put("mail.smtp.auth", "true");
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.socketFactory.fallback", "false");
    props.put("mail.smtp.quitwait", "false");
    props.put("mail.debug", "true");

    Session session = Session.getInstance(props, this);
    session.setDebug(debug);

    System.out.println("\n==> Session");
    Utils.showSession(session);

    MimeMessage msg = null;

    try {

      // create a message
      msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(from));

      InternetAddress[] address = { new InternetAddress(to) };
      msg.setRecipients(Message.RecipientType.TO, address);

      msg.setSubject("JavaMail APIs Multipart Test");
      msg.setSentDate(new Date());

      // create and fill the first message part
      MimeBodyPart mbp1 = new MimeBodyPart();
      mbp1.setText(msgText1, "us-ascii");
      System.out.println("\n==> MimeBodyPart 1");
      Utils.showBodyPart(mbp1);

      // create and fill the second message part
      MimeBodyPart mbp2 = new MimeBodyPart();
      mbp2.setText(msgText2, "us-ascii");
      System.out.println("\n==> MimeBodyPart 2");
      Utils.showBodyPart(mbp2);

      // create the Multipart and its parts to it      
      Multipart mp = new MimeMultipart();
      mp.addBodyPart(mbp1);
      mp.addBodyPart(mbp2);

      System.out.println("\n==> Multipart");
      Utils.showMultipart(mp);

      // add the Multipart to the message
      msg.setContent(mp);

      System.out.println("\n==> Message");
      Utils.showMessage(msg);

      // send the message
      Transport.send(msg);

    } catch (MessagingException mex) {

      System.out.println(msg.getAllRecipients());

      mex.printStackTrace();

      Exception ex = null;
      if ((ex = mex.getNextException()) != null) {
        ex.printStackTrace();
      }

    }
  }

  /**
   * 
   * @param to
   * @param bcc
   * @param from
   * @param subject
   * @param body
   * @param filename
   * 
   * @throws Exception
   */
  public void send(String to, String bcc, String from, String subject, String body, String filename) throws Exception {

    boolean debug = true;

    // create some properties and get the default Session
    Properties props = new Properties();

    if (debug) {
      props.put("mail.debug", "true");
    } else {
      props.put("mail.debug", "false");
    }

    props.put("mail.smtp.host", "smtp.mltech.com.br");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.auth", "true");

    props.put("mail.smtp.socketFactory.fallback", "false");
    props.put("mail.smtp.quitwait", "false");

    //props.put("mail.protocol", "smtp.mltech.com.br");
    props.put("mail.protocol", "smtp");
    props.put("mail.protocol.user", to);

    props.put("mail.user", "maurocl@mltech.com.br");
    props.put("mail.from", "maurocl@mltech.com.br");

    props.put("mail.transport.protocol", "smtp");

    // cria uma sessão com autenticação
    Session session = Session.getInstance(props, this);

    //session.setDebug(false);

    System.out.println("\n==> Session");
    Utils.showSession(session);

    MimeMessage msg = null;

    try {

      // create a message
      msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(from));

      InternetAddress[] addressTo = { new InternetAddress(to) };
      msg.setRecipients(Message.RecipientType.TO, addressTo);

      InternetAddress[] addressBcc = { new InternetAddress(to) };
      msg.setRecipients(Message.RecipientType.BCC, addressBcc);

      Date date = new Date();
      msg.setSubject(subject + " - " + date);
      msg.setSentDate(date);

      // create and fill the first message part
      MimeBodyPart mbp1 = new MimeBodyPart();
      mbp1.setText(body, "us-ascii");
      System.out.println("\n==> MimeBodyPart 1");
      Utils.showBodyPart(mbp1);

      // create and fill the second message part

      // Cria um BodyPart.
      MimeBodyPart mbp2 = new MimeBodyPart();

      mbp2.setText(msgText2, "us-ascii");

      // Cria um DataSource associado ao arquivo de anexo.
      DataSource source = new FileDataSource(filename);

      // Estabelece o handler para o arquivo.
      mbp2.setDataHandler(new DataHandler(source));

      // Associa o arquivo
      // lança MessagingException
      mbp2.setFileName(filename);

      System.out.println("\n==> MimeBodyPart 2");
      Utils.showBodyPart(mbp2);

      // create the Multipart and its parts to it      
      Multipart mp = new MimeMultipart();
      mp.addBodyPart(mbp1);
      mp.addBodyPart(mbp2);

      System.out.println("\n==> Multipart");
      Utils.showMultipart(mp);

      // add the Multipart to the message
      msg.setContent(mp);

      System.out.println("\n==> Message");
      Utils.showMessage(msg);

      // send the message
      //Transport.send(msg);

      new MyAsyncTask().execute(msg);

    } catch (MessagingException mex) {

      System.out.println(msg.getAllRecipients());

      mex.printStackTrace();

      Exception ex = null;
      if ((ex = mex.getNextException()) != null) {
        ex.printStackTrace();
      }

    }
  }

  /**
   * 
   * 
   *
   */
  private class MyAsyncTask extends AsyncTask<MimeMessage, Void, Boolean>
  {

    Boolean b;

    ProgressDialog mProgressDialog;

    @Override
    protected void onPostExecute(Boolean result) {

      if (mProgressDialog != null) {
        mProgressDialog.dismiss();
      }

    }

    @Override
    protected void onPreExecute() {

      // mProgressDialog = ProgressDialog.show(ActivityName.this, "Loading...", "Data is Loading...");
    }

    @Override
    protected Boolean doInBackground(MimeMessage... params) {

      // your network operation

      // send the message
      //Transport.send(msg);
      try {
        Transport.send(params[0]);
        b = true;
      } catch (MessagingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        b = false;
      }

      return b;
      
    }
    
  }

}
