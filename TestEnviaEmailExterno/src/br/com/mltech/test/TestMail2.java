package br.com.mltech.test;

import java.io.File;

import android.test.AndroidTestCase;
import android.util.Log;
import br.com.mltech.Mail;

/**
 * Conjunto de testes de envio de email usando JavaMail adpatado para Android.
 * 
 * @author maurocl
 * 
 */
public class TestMail2 extends AndroidTestCase {

  public static final String TAG = "TestMail2";

  public static int num = 0;
  
  /*
  public TestMail2() {
    Log.d(TAG,"TestMail2 constructor() ...");
  }
  */
  
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

    Log.w(TAG,"test02() - Start ...");
    
    Mail m = new Mail("maurocl@terra.com.br", "Mcl16dcjl");

    m.setDebuggable(false);
    m.setAuth(true);

    m.setHost("smtp.terra.com.br");
    m.setPort("587");
    
    m.setFrom("maurocl@terra.com.br");

    m.setTo(new String[] { "maurocl@terra.com.br" });
    m.setBcc(new String[] { "maurocl@mltech.com.br" });

    m.setSubject("test02");
    m.setBody("test02 - envio de email");

    boolean enviou = false;
    try {
      enviou = m.send();
    } catch (Exception e) {
      Log.w(TAG, "test02() - ", e);
    }

    assertTrue("Erro no envio do email", enviou);

  }

  /**
   * 
   */
  public void test03()  {

    Log.w(TAG,"test03() - Start ...");
    
    Mail m = new Mail("maurocl@terra.com.br", "Mcl16dcjl");

    m.setDebuggable(false);
    //m.setAuth(true);

    //m.setHost("smtp.terra.com.br");
    m.setPort("587");
    
    m.setFrom("maurocl@terra.com.br");

    m.setTo(new String[] { "maurocl@terra.com.br" });
    m.setBcc(new String[] { "maurocl@mltech.com.br" });

    m.setSubject("test32");
    m.setBody("test04 - envio de email");

    boolean enviou = false;
    try {
      enviou = m.send();
    } catch (Exception e) {
      Log.w(TAG,"getMessage(): " + e.getMessage());
      Log.w(TAG,"getLocalizedMessage(): " + e.getLocalizedMessage());
      Log.w(TAG,"getCause(): " + e.getCause());
      Log.w(TAG, "test03() - ", e);
    
    }

    assertTrue("Erro no envio do email", enviou);

  }

  public void test04()  {

    Log.w(TAG,"test04() - Start ...");
    
    Mail m = new Mail("maurocl@terra.com.br", "Mcl16dcjl");

    m.setDebuggable(false);
    //m.setAuth(true);

    m.setHost("smtp.terra.com.br");
    //m.setPort("587");
    
    m.setFrom("maurocl@terra.com.br");

    m.setTo(new String[] { "maurocl@terra.com.br" });
    m.setBcc(new String[] { "maurocl@mltech.com.br" });

    m.setSubject("test32");
    m.setBody("test04 - envio de email");

    boolean enviou = false;
    try {
      enviou = m.send();
    } catch (Exception e) {
      Log.w(TAG,"getMessage(): " + e.getMessage());
      Log.w(TAG,"getLocalizedMessage(): " + e.getLocalizedMessage());
      Log.w(TAG,"getCause(): " + e.getCause());
      Log.w(TAG, "test03() - ", e);
    
    }

    assertTrue("Erro no envio do email", enviou);

  }
  

  public void test05()  {

    Log.w(TAG,"test05() - Start ...");
    
    Mail m = new Mail("maurocl@terra.com.br", "Mcl16dcjl");

    m.setDebuggable(true);
    //m.setAuth(true);

    //m.setHost("smtp.terra.com.br");
    m.setPort("587");
    
    m.setFrom("maurocl@terra.com.br");

    m.setTo(new String[] { "maurocl@terra.com.br" });
    m.setBcc(new String[] { "maurocl@mltech.com.br" });

    m.setSubject("test32");
    m.setBody("test05 - envio de email");

    boolean enviou = false;
    try {
      enviou = m.send();
    } catch (Exception e) {
      Log.w(TAG,"getMessage(): " + e.getMessage());
      Log.w(TAG,"getLocalizedMessage(): " + e.getLocalizedMessage());
      Log.w(TAG,"getCause(): " + e.getCause());
      Log.w(TAG, "test03() - ", e);
    
    }

    assertTrue("Erro no envio do email", enviou);

  }
  
  /**
   * 
   */
  public void test06()  {

    Log.w(TAG,"test06() - Start ...");
    
    Mail m = new Mail("maurocl@terra.com.br", "Mcl16dcjl");


    boolean enviou = false;
    try {
      enviou = m.send();
    } catch (Exception e) {
      Log.w(TAG,"getMessage(): " + e.getMessage());
      Log.w(TAG,"getLocalizedMessage(): " + e.getLocalizedMessage());
      Log.w(TAG,"getCause(): " + e.getCause());
      Log.w(TAG, "test03() - ", e);
    
    }

    assertTrue("Erro no envio do email", enviou);

  }
  
  @Override
  protected void tearDown() throws Exception {
  
    // TODO Auto-generated method stub
    super.tearDown();
    
    Log.d(TAG,"*** tearDown("+num+") ...");
    
    
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
