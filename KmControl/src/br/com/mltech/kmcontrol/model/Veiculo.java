package br.com.mltech.kmcontrol.model;

import java.io.Serializable;

import android.database.Cursor;
import br.com.mltech.kmcontrol.data.VeiculoData;

/**
 * Representa um veículo incluindo seu nome, marca, modelo e placa.
 * 
 * 
 * 
 */
public class Veiculo implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private long mId;
	private String mNome;
	private String mMarca;
	private String mModelo;
	private String mPlaca;

	/**
	 * 
	 */
	public Veiculo() {
		this(-1, null, null, null, null);
	}

	/**
	 * 
	 * @param mNome
	 * @param mMarca
	 * @param mModelo
	 * @param mPlaca
	 */
	public Veiculo(String mNome, String mMarca, String mModelo, String mPlaca) {
		this(-1, mNome, mMarca, mModelo, mPlaca);
	}

	/**
	 * 
	 * @param mId
	 * @param mNome
	 * @param mMarca
	 * @param mModelo
	 * @param mPlaca
	 */
	public Veiculo(long mId, String mNome, String mMarca, String mModelo,
			String mPlaca) {
		super();
		this.mId = mId;
		this.mNome = mNome;
		this.mMarca = mMarca;
		this.mModelo = mModelo;
		this.mPlaca = mPlaca;
	}

	public long getId() {
		return mId;
	}

	public void setId(long mId) {
		this.mId = mId;
	}

	public String getNome() {
		return mNome;
	}

	public void setNome(String mNome) {
		this.mNome = mNome;
	}

	public String getMarca() {
		return mMarca;
	}

	public void setMarca(String mMarca) {
		this.mMarca = mMarca;
	}

	public String getModelo() {
		return mModelo;
	}

	public void setModelo(String mModelo) {
		this.mModelo = mModelo;
	}

	public String getPlaca() {
		return mPlaca;
	}

	public void setPlaca(String mPlaca) {
		this.mPlaca = mPlaca;
	}

	/**
	 * Preenche o objeto veiculo com as informações recebidas do cursor
	 * 
	 * @param c Cursor 
	 */
	public void init(Cursor c) {
		
		if (c != null) {
			mId = c.getInt(VeiculoData.VeiculoColumns._ID_IDX);
			mNome = c.getString(VeiculoData.VeiculoColumns.NOME_IDX);
			mMarca = c.getString(VeiculoData.VeiculoColumns.MARCA_IDX);
			mModelo = c.getString(VeiculoData.VeiculoColumns.MODELO_IDX);
			mPlaca = c.getString(VeiculoData.VeiculoColumns.PLACA_IDX);
		}

	}

	/**
	 * Limpa os campos de um objeto veículo permitindo que o objeto seja reaproveitado.
	 */
	public void clearFields() {
		this.setId(-1);
		this.setNome("");
		this.setMarca("");
		this.setModelo("");
		this.setPlaca("");
		
	}

	@Override
	public String toString() {
		return "Veiculo [mId=" + mId + ", mNome=" + mNome + ", mMarca="
				+ mMarca + ", mModelo=" + mModelo + ", mPlaca=" + mPlaca + "]";
	}

}
