package br.com.mltech;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage.RecipientType;

/**
 * 
 * @author maurocl
 *
 */
public class Utils {

  /**
   * Exibe as informações sobre Session.
   * 
   * @param s
   *          Instância de Session
   * 
   */
  public static void showSession(Session s) {

    s.getDebug();

    s.getDebugOut();
    s.getProviders();

    Properties p = s.getProperties();

    Enumeration e = p.elements();

    int i = 0;

    Set<Entry<Object, Object>> conjunto = p.entrySet();

    for (Entry elem : conjunto) {
      System.out.println("showSession(): " + i + " - Key=" + elem.getKey() + ", Value=" + elem.getValue());
      i++;
    }

  }

  /**
   * 
   * @param enderecos
   *          Array de endereços de email.
   */
  public static void showAddress(Address[] enderecos) {

    int i = 0;
    for (Address emailAddress : enderecos) {
      System.out.println("showAddress(" + i + ") - " + emailAddress.toString());
    }

  }

  /**
   * 
   * @param enderecos
   * @return
   */
  public static String toAddress(Address[] enderecos) {

    StringBuffer sb = new StringBuffer();

    for (Address emailAddress : enderecos) {
      sb.append(emailAddress.toString());
    }

    return sb.toString();

  }

  /**
   * 
   * @param mp
   * @throws IOException
   */
  public static void showMultipart(Multipart mp) throws IOException {

    MimeMultipart mmp;

    if (mp instanceof MimeMultipart) {

      mmp = (MimeMultipart) mp;

      System.out.println("showMultipart() - toString: " + mmp.toString());
      System.out.println("showMultipart() - getContentType: " + mmp.getContentType());

      try {

        System.out.println("showMultipart() - getCount():  " + mp.getCount());

        mmp.getCount();

        for (int i = 0; i < mmp.getCount(); i++) {

          System.out.println(" ==> showMultipart(): showBodyPart ==> " + i);

          System.out.println();
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
  public static void showBodyPart(BodyPart bp) throws MessagingException, IOException {

    MimeBodyPart mbp = null;

    if (bp instanceof MimeBodyPart) {

      mbp = (MimeBodyPart) bp;

      System.out.println("showBodyPart(): getContentType: " + mbp.getContentType());
      System.out.println("showBodyPart(): getContent: " + mbp.getContent());

      System.out.println("showBodyPart(): toString(): " + mbp.toString());
      System.out.println("showBodyPart(): getContentID: " + mbp.getContentID());
      System.out.println("showBodyPart(): getDescription: " + mbp.getDescription());
      System.out.println("showBodyPart(): getFileName: " + mbp.getFileName());

      System.out.println("showBodyPart(): getLineCount: " + mbp.getLineCount());

      System.out.println("showBodyPart(): getEncoding: " + mbp.getEncoding());
      System.out.println("showBodyPart(): getSize: " + mbp.getSize());

    }

  }

  /**
   * showMessage(Message msg)
   * 
   * @param msg
   * @throws Exception
   */
  public static void showMessage(Message msg) throws Exception {

    if (msg instanceof MimeMessage) {

      MimeMessage mm = (MimeMessage) msg;

      System.out.println("showMessage() - MimeMessage.getFrom: " + mm.getFrom() + " - " + toAddress(mm.getFrom()));
      System.out.println("showMessage() - MimeMessage.getRecipients(TO): " + mm.getRecipients(RecipientType.TO) + " - "
          + toAddress(mm.getRecipients(RecipientType.TO)));
      System.out.println("showMessage() - MimeMessage.getSubject(): " + mm.getSubject());
      System.out.println("showMessage() - MimeMessage.getSentDate: " + mm.getSentDate());

      System.out.println("showMessage() - MimeMessage.getMessageNumber: " + mm.getMessageNumber());
      System.out.println("showMessage() - MimeMessage.getContentType: " + mm.getContentType());
      System.out.println("showMessage() - MimeMessage.getSender: " + mm.getSender());
      System.out.println("showMessage() - MimeMessage.getEncoding: " + mm.getEncoding());

      int i = 0;
      Enumeration e = mm.getAllHeaderLines();
      for (; e.hasMoreElements();) {
        System.out.println("showMessage() - enum - " + i + " - " + e.nextElement());
        i++;
      }

      System.out.println();

      i = 0;
      Enumeration e2 = msg.getAllHeaders();
      while (e2.hasMoreElements()) {
        Header h = (Header) e2.nextElement();
        System.out.println("showMessage() - enum - " + i + " - " + h + " - " + showHeader(h));
        i++;
      }

    }

    try {

      System.out.println("showMessage() - getContentType: " + msg.getContentType());
      System.out.println("showMessage() - getContent: " + msg.getContent());
      System.out.println("showMessage() - getSubject: " + msg.getSubject());

      System.out.println("showMessage() - getDescription: " + msg.getDescription());
      System.out.println("showMessage() - getFileName: " + msg.getFileName());
      System.out.println("showMessage() - getLineCount: " + msg.getLineCount());

      System.out.println("showMessage() - getLineCount: " + msg.getLineCount());
      System.out.println("showMessage() - getMessageNumber: " + msg.getMessageNumber());

      System.out.println("showMessage() - getSentDate: " + msg.getSentDate());

      System.out.println("showMessage() - getFrom: ");
      showAddress(msg.getFrom());

      System.out.println("showMessage() - getReplyTo: ");
      showAddress(msg.getReplyTo());

      System.out.println("showMessage() - getSize: " + msg.getSize());

    } catch (IOException e) {
      System.out.println("showMessage() - " + e);
    } catch (MessagingException e) {
      System.out.println("showMessage() - " + e);
    }

    System.out.println();

  }

  /**
   * 
   * @param h
   * @return
   */
  public static String showHeader(Header h) {

    String name = h.getName();
    String value = h.getValue();

    return "name=[" + name + "], value=[" + value + "]";

  }

  /**
   * 
   * @param addresses
   * @return
   * @throws AddressException
   */
  public static InternetAddress[] xxx(String[] addresses) throws AddressException {

    if (addresses == null) {
      throw new IllegalArgumentException("Array de endereços está vazio");
    }

    InternetAddress[] internetAddress = new InternetAddress[addresses.length];

    for (int i = 0; i < addresses.length; i++) {

      internetAddress[i] = new InternetAddress(addresses[i]);

    }

    return internetAddress;

  }

}
