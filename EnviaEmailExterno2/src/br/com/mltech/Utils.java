
package br.com.mltech;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

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
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import android.util.Log;

/**
 * Classe de métodos utilitários
 * 
 * @author maurocl
 * 
 */
public class Utils {

  public static final String TAG = "Utils";

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
      Log.d(TAG, "showSession(): " + i + " - Key=" + elem.getKey() + ", Value=" + elem.getValue());
      i++;
    }

  }

  /**
   * Exibe no log um array de endereços IP
   * 
   * @param enderecos
   *          Array de endereços de email.
   */
  public static void showAddress(Address[] enderecos) {

    int i = 0;
    for (Address emailAddress : enderecos) {
      Log.d(TAG, "showAddress(" + i + ") - " + emailAddress.toString());
    }

  }

  /**
   * 
   * @param enderecos
   * 
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
   * 
   * @throws IOException
   */
  public static void showMultipart(Multipart mp) throws IOException {

    MimeMultipart mmp;

    if (mp instanceof MimeMultipart) {

      mmp = (MimeMultipart) mp;

      Log.d(TAG, "showMultipart() - toString: " + mmp.toString());
      Log.d(TAG, "showMultipart() - getContentType: " + mmp.getContentType());

      try {

        Log.d(TAG, "showMultipart() - getCount():  " + mp.getCount());

        mmp.getCount();

        for (int i = 0; i < mmp.getCount(); i++) {

          Log.d(TAG, " ==> showMultipart(): showBodyPart ==> " + i);

          Log.d(TAG, "");
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
   * showMessage(Message msg)
   * 
   * @param msg
   * @throws Exception
   */
  public static void showMessage(Message msg) throws Exception {

    if (msg instanceof MimeMessage) {

      MimeMessage mm = (MimeMessage) msg;

      Log.d(TAG, "showMessage() - MimeMessage.getFrom: " + mm.getFrom() + " - " + toAddress(mm.getFrom()));
      Log.d(TAG, "showMessage() - MimeMessage.getRecipients(TO): " + mm.getRecipients(RecipientType.TO) + " - "
          + toAddress(mm.getRecipients(RecipientType.TO)));
      Log.d(TAG, "showMessage() - MimeMessage.getSubject(): " + mm.getSubject());
      Log.d(TAG, "showMessage() - MimeMessage.getSentDate: " + mm.getSentDate());

      Log.d(TAG, "showMessage() - MimeMessage.getMessageNumber: " + mm.getMessageNumber());
      Log.d(TAG, "showMessage() - MimeMessage.getContentType: " + mm.getContentType());
      Log.d(TAG, "showMessage() - MimeMessage.getSender: " + mm.getSender());
      Log.d(TAG, "showMessage() - MimeMessage.getEncoding: " + mm.getEncoding());

      int i = 0;
      Enumeration e = mm.getAllHeaderLines();
      for (; e.hasMoreElements();) {
        Log.d(TAG, "showMessage() - enum - " + i + " - " + e.nextElement());
        i++;
      }

      Log.d(TAG, "");

      i = 0;
      Enumeration e2 = msg.getAllHeaders();
      while (e2.hasMoreElements()) {
        Header h = (Header) e2.nextElement();
        Log.d(TAG, "showMessage() - enum - " + i + " - " + h + " - " + showHeader(h));
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
      Log.d(TAG, "showMessage() - " + e);
    } catch (MessagingException e) {
      Log.d(TAG, "showMessage() - " + e);
    }

    Log.d(TAG, "");

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
