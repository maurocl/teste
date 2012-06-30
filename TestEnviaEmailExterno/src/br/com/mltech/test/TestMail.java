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
public class TestMail extends AndroidTestCase {

  public static final String TAG = "TestMail";

  
  
  public TestMail() {

    // TODO Auto-generated constructor stub
    Log.d(TAG,"TestMail constructor() ...");
  }
  
  @Override
  protected void setUp() throws Exception {
  
    // TODO Auto-generated method stub
    super.setUp();
    
    Log.d(TAG,"setUp() ...");
    
    Log.d(TAG,"getName(): "+this.getName());
    
    
  }
  
  /**
   * 
   */
  public void test01() {

    Log.w(TAG,"test01() - Start ...");
    
    //Mail m = new Mail("maurocl.lopes@gmail.com", "Mcl16dcjl");
    Mail m = null;

    assertNull("test0() - Mail não é nulo", m);

  }

  /**
   * 
   * @throws Exception
   */
  public void test02()  {

    Log.w(TAG,"test02() - Start ...");
    
    Mail m = new Mail("maurocl@terra.com.br", "Mcl16dcjl");

    m.setDebuggable(true);

    m.setFrom("maurocl@terra.com.br");

    m.setTo(new String[] { "maurocl@terra.com.br" });
    m.setBcc(new String[] { "maurocl@mltech.com.br" });
    m.setSubject("test02");
    m.setBody("test02 - envio de email");

    m.setHost("smtp.terra.com.br");
    m.setPort("587");

    m.setAuth(true);

    boolean enviou = false;
    try {
      enviou = m.send();
    } catch (Exception e) {
      // TODO: handle exception
      Log.w(TAG, "test02() - ", e);
    }

    assertTrue("Erro no envio do email", enviou);
    
    

  }

  public void test03()  {

    Log.w(TAG,"test03() - Start ...");
    
    Mail m = new Mail("maurocl@mltech.com.br", "mcl16d");

    m.setDebuggable(true);

    m.setFrom("maurocl@mltech.com.br");

    m.setTo(new String[] { "maurocl@terra.com.br" });
    m.setBcc(new String[] { "maurocl@mltech.com.br" });
    m.setSubject("test03");
    m.setBody("test03 - envio de email");

    m.setHost("smtp.mltech.com.br");
    m.setPort("587");

    File f = new File("/mnt/sdcard/Pictures/fotoevento/fotos/20120619_104431.jpg");
    try {
      m.addAttachment(f.getAbsolutePath());
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    m.setAuth(true);

    boolean enviou=false;
    try {
      enviou = m.send();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    assertTrue("Erro no envio do email", enviou);

  }

  /**
   * 
   * @throws Exception
   */
  public void test04()  {
    
    Log.w(TAG,"test04() - Start ...");

    Mail m = new Mail("maurocl@mltech.com.br", "mcl16d");

    m.setDebuggable(true);

    m.setHost("smtp.mltech.com.br");

    m.setPort("587");

    m.setAuth(true);

    boolean enviou=false;
    try {
      enviou = m.send("maurocl@terra.com.br", "maurocl@mltech.com.br", "maurocl@mltech.com.br", "test04 - envio de email",
          "corpo", "/mnt/sdcard/Pictures/fotoevento/fotos/20120619_104431.jpg");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    assertTrue("Erro no envio do email", enviou);

  }

  /**
   * 
   */
  public void test05() {

    Log.w(TAG,"test05() - Start ...");
    
    Mail m = new Mail("maurocl@mltech.com.br", "mcl16d");

    m.setDebuggable(true);

    m.setHost("smtp.mltech.com.br");
    m.setPort("587");

    m.setAuth(true);

    boolean enviou = false;

    try {
      enviou = m.send("maurocl@terra.com.br", "maurocl@terra.com.br", "maurocl@terra.com.br", "test05 - envio de email", "corpo",
          "/mnt/sdcard/Pictures/fotoevento/fotos/20120619_104431.jpg");
    } catch (Exception e) {

      Log.w(TAG,"getMessage(): " + e.getMessage());
      Log.w(TAG,"getLocalizedMessage(): " + e.getLocalizedMessage());
      Log.w(TAG,"getCause(): " + e.getCause());
      
      Log.e(TAG, "erro", e);
      assertTrue("Erro no envio do email", enviou);
    }

    assertFalse("Erro no envio do email", enviou);

  }

  /**
   * 
   * @throws Exception
   */
  public void XXX() throws Exception {

    Mail m = new Mail("maurocl.lopes@google.com", "Mcl16dcjl");

    m.setDebuggable(true);

    m.setFrom("maurocl.lopes@gmail.com");

    m.setTo(new String[] { "maurocl@terra.com.br" });
    m.setBcc(new String[] { "maurocl@mltech.com.br" });
    m.setSubject("test04");
    m.setBody("test04 - envio de email");

    m.setHost("smtp.gmail.com");
    m.setPort("465");
    m.setSport("465");

    m.setAuth(true);

    boolean enviou = m.send();

    assertTrue("Erro no envio do email", enviou);

  }

  @Override
  protected void tearDown() throws Exception {
  
    // TODO Auto-generated method stub
    super.tearDown();
    
    Log.d(TAG,"tearDown() ...");
    
    
  }
  
}
