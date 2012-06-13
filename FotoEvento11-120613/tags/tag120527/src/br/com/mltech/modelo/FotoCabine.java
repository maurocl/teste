package br.com.mltech.modelo;

import android.graphics.Bitmap;

/**
 * FotoCabine
 * 
 * @author maurocl
 * 
 */
public class FotoCabine {

	private Moldura moldura;
	private Foto[] fotos = new Foto[3];

	public FotoCabine(String arquivo, Bitmap file1, Bitmap file2, Bitmap file3) {
      //fotos[0] = new Foto(file1, file2, file3);
	}

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

	public Foto[] getFotos() {
		return fotos;
	}

	public void setFotos(Foto[] fotos) {
		this.fotos = fotos;
	}

	public Moldura getMoldura() {
		return moldura;
	}

	public void setMoldura(Moldura moldura) {
		this.moldura = moldura;
	}
	
}
