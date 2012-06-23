package br.com.mltech.test;

import android.test.AndroidTestCase;
import android.util.Log;
import br.com.mltech.GMailSender;
import br.com.mltech.MLTechSender;
import br.com.mltech.Mail;

public class TestEnviaEmailExterno extends AndroidTestCase {

  public static final String TAG = "EnviaEmailExternoActivity";

  public void test10() {

    //Mail m = new Mail("maurocl.lopes@gmail.com", "Mcl16dcjl");
    Mail m = null;

    assertNull("test0() - Mail não é nulo", m);

  }

  public void test10a() {

    Mail m = new Mail();

    assertNotNull("test1a() - ", m);

  }

  public void test11() {

    //Mail m = new Mail("maurocl.lopes@gmail.com", "Mcl16dcjl");
    Mail m = new Mail("maurocl@mltech.com.br", "mcl16d");

    assertNotNull("test1() - Foto é vazia", m);

  }

  /**
   * 
   */
  public void xx10() {

    Log.d(TAG, "test10");

    Mail m = new Mail("maurocl@mltech.com.br", "mcl16d");

    String[] toArr = { "maurocl@terra.com.br",
        "maurocl.lopes@gmail.com" };

    m.setTo(toArr);
    m.setFrom("maurocl@mltech.com.br");
    m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device.");
    m.setBody("Email body.");

    try {

      if (m.send()) {
        Log.v(TAG, "Email was sent successfully.");

      } else {

        Log.v(TAG, "Email was not sent.");

      }
    } catch (Exception e) {

      Log.e("MailApp", "Could not send email", e);

    }

    assertNotNull("test1() - Foto é vazia", m);

  }

  public void test12() throws Exception {

    Log.d(TAG, "test12");

    Mail m = new Mail("maurocl@mltech.com.br", "mcl16d");

    String[] toArr = { "maurocl@terra.com.br",
        "maurocl.lopes@gmail.com" };

    m.setTo(toArr);
    m.setFrom("maurocl@mltech.com.br");
    m.setSubject("assunto");
    m.setBody("corpo.");

    m.setHost("smtp.mltech.com.br");
    m.setFrom("maurocl@mltech.com.br");

    m.setAuth(true);

    m.send();

    assertNotNull("test1() - Foto é vazia", m.getBody());

  }

  public void test13() throws Exception {

    Log.d(TAG, "test13");

    Mail m = new Mail("maurocl.lopes@gmail.com", "Mcl16dcjl");

    String[] toArr = { "maurocl@terra.com.br",
        "maurocl.lopes@gmail.com" };

    m.setTo(toArr);
    m.setFrom("maurocl.lopes@gmail.com");
    m.setSubject("assunto");
    m.setBody("corpo.");

    m.setHost("smtp.gmail.com");

    m.send();

    assertNotNull("test1() - Foto é vazia", m.getBody());

  }

  public void test14() throws Exception {

    Log.d(TAG, "test14");

    GMailSender m = new GMailSender("maurocl.lopes@gmail.com", "Mcl16dcjl");

    m.sendMail("a", "b", "maurocl.lopes@gmail.com", "maurocl@terra.com.br,maurocl@mltech.com.br");

  }

  public void test15() throws Exception {

    Log.d(TAG, "test15");

    MLTechSender m = new MLTechSender("maurocl@mltech.com.br", "mcl16d");

    m.sendMail("c", "d", "maurocl@mltech.com.br", "maurocl@terra.com.br,maurocl@mltech.com.br");

  }

}
