package br.com.mltech;

import junit.framework.Assert;

import org.junit.Test;

import br.com.br.Valida;

/**
 * 
 * @author maurocl
 * 
 */
public class TestPatternMatcher {

  //-----------------------------------------------------------------------
  // validaData()
  //-----------------------------------------------------------------------

  @Test
  public void testDataNula() {

    boolean b = Valida.validaData(null);

    Assert.assertEquals(false, b);

  }

  @Test
  public void testDataStringNula() {

    boolean b = Valida.validaData("");

    Assert.assertEquals(false, b);

  }

  @Test
  public void testDataValida() {

    boolean b = Valida.validaData("11/01/2000");

    Assert.assertEquals(true, b);

  }

  @Test
  public void testData4Invalida() {

    boolean b = Valida.validaData("1/1/1");

    Assert.assertEquals(false, b);

  }

  @Test
  public void testData5() {

    boolean b = Valida.validaData("1/1/11");

    Assert.assertEquals(true, b);

  }

  @Test
  public void testData6() {

    boolean b = Valida.validaData("1/1/1111");

    Assert.assertEquals(true, b);

  }

  @Test
  public void testDataFormatoOkValoresInvalidos() {

    boolean b = Valida.validaData("99/99/9999");

    Assert.assertEquals(true, b);

  }

  //-----------------------------------------------------------------------
  // validaCep()
  //-----------------------------------------------------------------------
  
  @Test
  public void testCepNull() {

    boolean b = Valida.validaCep(null);

    Assert.assertEquals(false, b);

  }

  @Test
  public void testCepStringNula() {

    boolean b = Valida.validaCep("");

    Assert.assertEquals(false, b);

  }

  @Test
  public void testCepTamanhoInvalido() {

    boolean b = Valida.validaCep("1");

    Assert.assertEquals(false, b);

  }

  @Test
  public void testCepValido() {

    boolean b = Valida.validaCep("00000000");

    Assert.assertEquals(true, b);

  }

  @Test
  public void testCepValidoComSeparador() {

    boolean b = Valida.validaCep("00000-000");

    Assert.assertEquals(true, b);

  }

  @Test
  public void testCep06() {

    boolean b = Valida.validaCep("00000 - 000");

    Assert.assertEquals(false, b);

  }

  @Test
  public void testCep07() {

    boolean b = Valida.validaCep("00000-00");

    Assert.assertEquals(true, b);

  }

  @Test
  public void testCep08() {

    boolean b = Valida.validaCep("00000-0");

    Assert.assertEquals(true, b);

  }

  @Test
  public void testCep09() {

    boolean b = Valida.validaCep("00000-");

    Assert.assertEquals(true, b);

  }

  @Test
  public void testCep10() {

    String s = "00000000";

    boolean b = (s.length() <= 8);

    Assert.assertEquals(true, b);

  }

  @Test
  public void testCep11() {

    String s = "00000-000";

    boolean b = (s.length() <= 9);

    Assert.assertEquals(true, b);

  }

  //-----------------------------------------------------------------------
  // validaTelefone()
  //-----------------------------------------------------------------------

  
  @Test
  public void testTel01() {

    String s = "8143-8978";

    boolean b = Valida.validaTelefone(s);

    Assert.assertEquals(true, b);

  }

  @Test
  public void testTel02() {

    String s = "19 8143-8978";

    boolean b = Valida.validaTelefone(s);

    Assert.assertEquals(true, b);

  }

  @Test
  public void testTel03() {

    String s = "55 19 8143-8978";

    boolean b = Valida.validaTelefone(s);

    Assert.assertEquals(true, b);

  }

  @Test
  public void testTel04() {

    String s = "+55 (19) 8143-8978";

    boolean b = Valida.validaTelefone(s);

    Assert.assertEquals(true, b);

  }

  @Test
  public void testTel05() {

    String s = "55 (19) 8143-8978";

    boolean b = Valida.validaTelefone(s);

    Assert.assertEquals(true, b);

  }

  @Test
  public void testTel05a() {

    String s = "55(19)8143-8978";

    boolean b = Valida.validaTelefone(s);

    Assert.assertEquals(true, b);

  }

  @Test
  public void testTel05b() {

    String s = "55(19)81438978";

    boolean b = Valida.validaTelefone(s);

    Assert.assertEquals(true, b);

  }

  @Test
  public void testTel05c() {

    String s = "551981438978";

    boolean b = Valida.validaTelefone(s);

    Assert.assertEquals(true, b);

  }

  @Test
  public void testTel06() {

    String s = "(19) 8143-8978";

    boolean b = Valida.validaTelefone(s);

    Assert.assertEquals(true, b);

  }

  //-----------------------------------------------------------------------
  // validaEmail()
  //-----------------------------------------------------------------------
  
  @Test
  public void testEmailNull() {

    String s = null;

    boolean b = Valida.validaEmail(s);

    Assert.assertEquals(false, b);

  }

  @Test
  public void testEmailStringVazia() {

    String s = "";

    boolean b = Valida.validaEmail(s);

    Assert.assertEquals(false, b);

  }

  @Test
  public void testEmailInvalido() {

    String s = "a@b";

    boolean b = Valida.validaEmail(s);

    Assert.assertEquals(false, b);

  }

  @Test
  public void testEmailValido01() {

    String s = "maurocl@mltech.com.br";

    boolean b = Valida.validaEmail(s);

    Assert.assertEquals(true, b);

  }

  @Test
  public void testEmailValido02() {

    String s = "maurocl@gmail.com";

    boolean b = Valida.validaEmail(s);

    Assert.assertEquals(true, b);

  }

  @Test
  public void testEmailInvalido02() {

    String s = "maurocl@gmail.com.";

    boolean b = Valida.validaEmail(s);

    Assert.assertEquals(true, b);

  }

}
