package br.com.mltech.modelo;

import java.io.File;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * 
 * @author maurocl
 * 
 */
public class Foto {

	private Dimensao dimensao;

	// nome do arquivo
	private String arquivo;

	// bitmap contendo a imagem (foto)
	private Bitmap imagem;

	private byte[] dados;

	private Uri uri;

	/**
	 * 
	 * @param arquivo
	 */
	public Foto(String arquivo) {
		this.arquivo = arquivo;
	}

	public Foto(String arquivo, byte[] dados) {
		this.arquivo = arquivo;
		this.dados = dados;
	}

	public Foto(String arquivo, Bitmap bm) {
		this.arquivo = arquivo;
		this.imagem = bm;
	}
	
	public Foto(Uri uri) {
		this.uri = uri;
	}
	
	public Foto(Uri uri, byte[] dados) {
		this.uri = uri;
		this.dados = dados;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isLandscape() {
		if (getDimensao() != null) {
			return (getDimensao().getLargura() > getDimensao().getAltura());
		} else {
			return false;
		}

	}

	/**
	 * 
	 * @return
	 */
	public boolean isPortrait() {
		if (getDimensao() != null) {
			return (getDimensao().getLargura() <= getDimensao().getAltura());
		} else {
			return false;
		}

	}

	public Foto redimensiona(int largura, int altura) {
		return null;
	}
	
	/**
	 * Grava uma foto em um arquivo
	 * 
	 * @return
	 */
	public boolean gravar() {

		// a foto armazenada em imagem será salva
		if (getImagem() == null) {
			return false;
		}

		if (getFilename() == null) {
			return false;
		}

		return false;
	}

	/**
	 * Lê uma foto armazenada em um arquivo
	 * 
	 * @return
	 * 
	 */
	public boolean ler() {
		// a foto será lida do filename e armazenada no bitmap imagem

		if (getFilename() == null) {
			return false;
		}

		if (!getFilename().exists()) {
			// arquivo não existe
		}

		return false;

	}

	private File getFilename() {
		File f = new File(getArquivo());
		if ((f != null) && (f.exists())) {
			return f;
		}
		return null;
	}

	//
	public Bitmap getImagem() {
		return imagem;
	}

	public void setImagem(Bitmap imagem) {
		this.imagem = imagem;
	}

	public Bitmap getBitmap() {
		return null;
	}

	public String tamanho() {
		return null;
	}

	public Dimensao getDimensao() {
		return dimensao;
	}

	public void setDimensao(Dimensao dimensao) {
		this.dimensao = dimensao;
	}

	public String getArquivo() {
		return arquivo;
	}

	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "Foto [dimensao=" + dimensao + ", arquivo=" + arquivo
				+ ", imagem=" + imagem + "]";
	}

}
