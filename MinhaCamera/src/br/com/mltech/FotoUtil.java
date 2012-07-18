package br.com.mltech;

import java.util.ArrayList;
import java.util.List;

public class FotoUtil {

  List<Foto> fotos;

  public FotoUtil() {

    fotos = new ArrayList<Foto>();
  }

  public void put(Foto f) {

    fotos.add(f);
  }

  public void add(Foto foto) {

    fotos.add(foto);
  }

  // Retorna o número de elementos da lista.
  public int size() {

    return fotos.size();
  }

  @Override
  public String toString() {

    return "FotoUtil [fotos=" + fotos + "]";
  }

}
