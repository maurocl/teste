package br.com.mltech.test;

import javax.mail.internet.InternetAddress;

import android.test.AndroidTestCase;
import android.util.Log;
import br.com.mltech.MsgMultiSend;
import br.com.mltech.Utils;

/**
 * Conjunto de testes de envio de email usando JavaMail adpatado para Android.
 * 
 * @author maurocl
 * 
 */
public class TestMail5 extends AndroidTestCase {

  private static String USER = "maurocl@mltech.com.br";

  private static String PASS = "mcl16d";

  public static final String TAG = "TestMail5";

  public static int num = 0;

  @Override
  protected void setUp() throws Exception {

    super.setUp();

    num++;

    Log.d(TAG, "*** setUp(" + num + ") ...");

    Log.d(TAG, "getName(): " + this.getName());

  }

  /**
   * 
   * @throws Exception
   */
  public void test02() throws Exception {

    MsgMultiSend m = new MsgMultiSend(USER, PASS);

    //
    String to = "maurocl@terra.com.br";
    String bcc = "maurocl@mltech.com.br";
    String from = "maurocl@mltech.com.br";
    String subject = "Assunto";
    String body = "Esse eh o corpo da mensagem";
    String filename = "/mnt/sdcard/Pictures/fotoevento/molduras/cabine_132_568_red.png";

    m.send(to, bcc, from, subject, body, filename);

    String[] nomes = { "maurocl@mltech.com.br", "maurocl@terra.com.br", "danielej@terra.com.br" };
    //String[] nomes = null;

    InternetAddress[] enderecos = Utils.xxx(nomes);

    Utils.showAddress(enderecos);

    boolean enviou = true;
    
    
    
    assertTrue("Erro no envio do email", enviou);

  }

  /**
   * Exibe detalhes sobre uma exceção gerada.
   * 
   * @param e
   *          Exception
   * 
   */
  public void showException(Exception e) {

    Log.w(TAG, "getMessage(): " + e.getMessage());
    Log.w(TAG, "getLocalizedMessage(): " + e.getLocalizedMessage());
    Log.w(TAG, "getCause(): " + e.getCause());
    Log.w(TAG, "test03() - ", e);
  }

}
