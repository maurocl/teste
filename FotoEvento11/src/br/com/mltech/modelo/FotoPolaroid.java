package br.com.mltech.modelo;


/**
 * FotoPolaroid
 * 
 * É uma foto no formato Polaroid
 * 
 * @author maurocl
 *
 */
public class FotoPolaroid {

  // Foto
	private Foto foto;
	
	// Moldura
	private Moldura moldura;

	/**
	 * 
	 * @param arquivo
	 */
	public FotoPolaroid(String arquivo) {

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

	/**
	 * 
	 * @return
	 */
	public Foto getFoto() {
		return foto;
	}

	/**
	 * 
	 * @param foto
	 */
	public void setFoto(Foto foto) {
		this.foto = foto;
	}

}
