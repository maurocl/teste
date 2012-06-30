package br.com.mltech.test;

import android.test.AndroidTestCase;
import android.util.Log;
import br.com.mltech.Mail;

/**
 * Conjunto de testes de envio de email usando JavaMail adpatado para Android.
 * 
 * @author maurocl
 * 
 */
public class TestMail3 extends AndroidTestCase {
  
  private static String USER = "maurocl@mltech.com.br";
  private static String PASS = "mcl16d";
  

  public static final String TAG = "TestMail3";

  public static int num = 0;
  
  @Override
  protected void setUp() throws Exception {
  
    super.setUp();
    
    num++;
    
    Log.d(TAG,"*** setUp("+num+") ...");
    
    Log.d(TAG,"getName(): "+this.getName());
    
    
  }
  


  /**
   * 
   * @throws Exception
   */
  public void test02()  {
    
    Mail m = new Mail(USER, PASS);

    m.setDebuggable(true);
    m.setAuth(true);

    m.setHost("smtp.mltech.com.br");
    m.setPort("587");
    
    m.setFrom("maurocl@mltech.com.br");

    m.setSsl(false);
    
    m.setTo(new String[] { "maurocl@terra.com.br" });
    m.setBcc(new String[] { "maurocl@mltech.com.br" });

    m.setSubject("test02");
    m.setBody("test02 - envio de email");

    boolean enviou = false;
    try {
      enviou = m.send();
    } catch (Exception e) {
      showException(e);
    }

    assertTrue("Erro no envio do email", enviou);

  }

  /**
   * Exibe detalhes sobre uma exceção gerada.
   * 
   * @param e Exception
   * 
   */
  public void showException(Exception e) {
    Log.w(TAG,"getMessage(): " + e.getMessage());
    Log.w(TAG,"getLocalizedMessage(): " + e.getLocalizedMessage());
    Log.w(TAG,"getCause(): " + e.getCause());
    Log.w(TAG, "test03() - ", e);
  }
  
}
