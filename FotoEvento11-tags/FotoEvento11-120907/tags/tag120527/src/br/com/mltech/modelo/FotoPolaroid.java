package br.com.mltech.modelo;

import java.io.File;

/**
 * 
 * @author maurocl
 *
 */
public class FotoPolaroid {

	private Foto foto;
	private Moldura moldura;

	public FotoPolaroid(String arquivo) {

	}

	public Moldura getMoldura() {
		return moldura;
	}

	public void setMoldura(Moldura moldura) {
		this.moldura = moldura;
	}

	public Foto getFoto() {
		return foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}

}
