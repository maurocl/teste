package br.com.br;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Valida {

  private static final boolean DEBUG = true;

  /**
   * 
   * @param data
   * 
   * @return
   * 
   */
  public static boolean validaData(String data) {

    if (data == null) {
      return false;
    }

    boolean dataValida = false;

    try {
      Pattern p = Pattern.compile("(\\d{1,})/(\\d{1,})/(\\d{2,})");

      Matcher m = p.matcher(data);

      String[] a = new String[9];

      if (m.matches()) {

        if (DEBUG) {
          System.out.println("\n==> m.group: " + m.group());
          System.out.println("==> m.groupCount(): " + m.groupCount());
        }

        for (int i = 0; i <= m.groupCount(); i++) {
          a[i] = m.group(i);
          if (DEBUG) {
            System.out.println("grupo: " + i + ": " + a[i]);
          }
        }

        int dia = 0, mes = 0, ano = 0;

        if (m.groupCount() == 3) {
          dia = Integer.valueOf(a[1]);
          mes = Integer.valueOf(a[2]);
          ano = Integer.valueOf(a[3]);
        }

        dataValida = true;

        if (dia < 0 || dia > 31) {
          dataValida = false;
        }

        if (mes < 1 || mes > 12) {
          dataValida = false;
        }

        if (ano < 0) {
          dataValida = false;
        }

      } else {
        if (DEBUG) {
          System.out.println("\n[" + data + "] inválidada.");
        }
      }

    } catch (PatternSyntaxException ex) {

    }

    return dataValida;

  }

  /**
   * 
   * @param cep
   * 
   * @return
   */
  public static boolean validaCep(String cep) {

    if (cep == null) {
      return false;
    }

    boolean dataValida = false;

    try {
      Pattern p = Pattern.compile("(\\d{5})-{0,1}(\\d){3}");

      Matcher m = p.matcher(cep);

      String[] a = new String[9];

      if (m.matches()) {

        if (DEBUG) {
          System.out.println("\n==> m.group: " + m.group());
          System.out.println("==> m.groupCount(): " + m.groupCount());
        }

        for (int i = 0; i <= m.groupCount(); i++) {
          a[i] = m.group(i);
          if (DEBUG) {
            System.out.println("grupo: " + i + ": " + a[i]);
          }
        }

        dataValida = true;

      } else {
        if (DEBUG) {
          System.out.println("\n[" + cep + "] inválidada.");
        }
      }

    } catch (PatternSyntaxException ex) {

    }

    return dataValida;

  }

  /**
   * 
   * @param tel
   * 
   * @return
   */
  public static boolean validaTelefone(String tel) {

    if (tel == null) {
      return false;
    }

    try {


      //Pattern p = Pattern.compile("(\\({0,1}(\\d){2}\\){0,1}){0,1}\\s{0,1}(\\d){3,5}-{0,1}(\\d){4}");
      Pattern p = Pattern.compile("(\\({0,1}.*?\\){0,1})(\\s){0,1}(\\d{3,5})([-|\\.]{0,1})(\\d{4})");


      Matcher m = p.matcher(tel);

      if (m.matches()) {

        if (DEBUG) {
          System.out.println("\nm.groupCount(): " + m.groupCount());
          System.out.println("m.group(): " + m.group());

          for (int i = 0; i <= m.groupCount(); i++) {
            System.out.println("grupo: " + i + ": " + m.group(i));
          }

        }

        return true;

      } else {
        if (DEBUG) {
          System.out.println("\n[" + tel + "] inválidada");
        }
        return false;
      }

    } catch (PatternSyntaxException ex) {

    }

    return false;

  }

  public static boolean validaTelefoneCompleto(String tel) {

    if (tel == null) {
      return false;
    }

    try {

      //Pattern p = Pattern.compile("\\+??(\\d\\d)(\\s){0,1}\\((\\d\\d)\\)(\\s){0,1}(\\d{3,5})(-{0,1})(\\d{4})(.){0,}");
      Pattern p = Pattern
          .compile("((\\+??){0,1}(\\d\\d){0,1}){0,1}(\\s){0,1}\\((\\d\\d)\\)(\\s){0,1}(\\d{3,5})(-{0,1})(\\d{4})(.){0,}");
      //Pattern p = Pattern.compile("((\\+??){0,1}(\\d\\d){0,1})(\\s){0,1}((\\()(\\d\\d)\\){0,1}(\\s){0,1}(\\d{3,5})(-{0,1})(\\d{4})");

      Matcher m = p.matcher(tel);

      if (m.matches()) {

        if (DEBUG) {
          System.out.println("\nm.grooup: " + m.group());

          for (int i = 0; i <= m.groupCount(); i++) {
            System.out.println("grupo: " + i + ": " + m.group(i));
          }

        }

        return true;

      } else {
        if (DEBUG) {
          System.out.println("\n[" + tel + "] inválidada");
        }
        return false;
      }

    } catch (PatternSyntaxException ex) {

    }

    return false;

  }

  
  /**
   * 
   * @param email
   * 
   * @return
   */
  public static boolean validaEmail(String email) {

    if (email == null) {
      return false;
    }

    try {

      //Pattern p = Pattern.compile("((\\+??){0,1}(\\d\\d){0,1}){0,1}(\\s){0,1}\\((\\d\\d)\\)(\\s){0,1}(\\d{3,5})(-{0,1})(\\d{4})(.){0,}");

      Pattern p = Pattern.compile("(.){1,}@(.){1,}\\.{1}(.){1,}");

      Matcher m = p.matcher(email);

      if (m.matches()) {

        if (DEBUG) {
          System.out.println("\nm.grooup: " + m.group());
        }

        if (DEBUG) {
          for (int i = 0; i <= m.groupCount(); i++) {
            System.out.println("grupo: " + i + ": " + m.group(i));
          }
        }

        return true;

      } else {
        if (DEBUG) {
          System.out.println("\n[" + email + "] inválidada");
        }
        return false;
      }

    } catch (PatternSyntaxException ex) {

    }

    return false;

  }

}
