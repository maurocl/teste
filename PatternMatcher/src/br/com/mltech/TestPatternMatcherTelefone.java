package br.com.mltech;

import junit.framework.Assert;

import org.junit.Test;

import br.com.br.Valida;

/**
 * 
 * @author maurocl
 * 
 */
public class TestPatternMatcherTelefone {

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
  public void testTelValidoComHifen() {

    String s = "(19) 8143-8978";

    boolean b = Valida.validaTelefone(s);

    Assert.assertEquals(true, b);

  }

  @Test
  public void testTelValidoSemEspacoComHifen() {

    String s = "(19)8143-8978";

    boolean b = Valida.validaTelefone(s);

    Assert.assertEquals(true, b);

  }
  
  @Test
  public void testTelValidoSemHifen() {

    String s = "(19) 81438978";

    boolean b = Valida.validaTelefone(s);

    Assert.assertEquals(true, b);

  }

  @Test
  public void testTelValidoSemEspacoSemHifen() {

    String s = "(19)81438978";

    boolean b = Valida.validaTelefone(s);

    Assert.assertEquals(true, b);

  }

  
  @Test
  public void testTelValidoComEspacoComPonto() {

    String s = "(19) 8143.8978";

    boolean b = Valida.validaTelefone(s);

    Assert.assertEquals(true, b);

  }
  
  @Test
  public void testTelValidoSemEspacoComPonto() {

    String s = "(19)8143.8978";

    boolean b = Valida.validaTelefone(s);

    Assert.assertEquals(true, b);

  }


  @Test
  public void testTelValidoSemPontuacao() {

    String s = "1981438978";

    boolean b = Valida.validaTelefone(s);

    Assert.assertEquals(true, b);

  }
  
  
}
