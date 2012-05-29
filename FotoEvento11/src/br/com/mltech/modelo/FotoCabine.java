package br.com.mltech.modelo;

import android.graphics.Bitmap;

/**
 * FotoCabine
 * 
 * @author maurocl
 * 
 */
public class FotoCabine {

  // moldura
	private Moldura moldura;
	
	// array de fotos
	private Foto[] fotos = new Foto[3];

	/**
	 * 
	 * @param arquivo
	 * @param file1
	 * @param file2
	 * @param file3
	 */
	public FotoCabine(String arquivo, Bitmap file1, Bitmap file2, Bitmap file3) {
      //fotos[0] = new Foto(file1, file2, file3);
	}

	/**
	 * 
	 * @param moldura
	 * @param file1
	 * @param file2
	 * @param file3
	 */
	public FotoCabine(Bitmap moldura, Bitmap file1, Bitmap file2, Bitmap file3) {
	      //fotos[0] = new Foto(file1, file2, file3);
		}

	
	/**
	 * 
	 * @param i
	 * @return
	 */
	Bitmap getBitmap(int i) {
		return fotos[i].getBitmap();
	}

	/**
	 * 
	 * @return
	 */
	public Foto[] getFotos() {
		return fotos;
	}

	/**
	 * 
	 * @param fotos
	 */
	public void setFotos(Foto[] fotos) {
		this.fotos = fotos;
	}

	/**
	 * 
	 * @return
	 */
	public Moldura getMoldura() {
		return moldura;
	}

	/**
	 * 
	 * @param moldura
	 */
	public void setMoldura(Moldura moldura) {
		this.moldura = moldura;
	}
	
}
