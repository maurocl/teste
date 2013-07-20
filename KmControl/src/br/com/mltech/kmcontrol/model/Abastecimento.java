package br.com.mltech.kmcontrol.model;

import java.io.Serializable;

import android.database.Cursor;
import br.com.mltech.kmcontrol.data.AbastecimentoData;

/**
 * Representa um veículo incluindo seu nome, marca, modelo e placa.
 * 
 * 
 * 
 */
public class Abastecimento implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private long mId;
	private String mData;
	private int mKmTotal;
	private double mValor;
	private double mLitros;
	private int mKmParcial;
	private double mMedia;

	/**
	 * 
	 */
	public Abastecimento() {
		this(-1, "", 0, 0.0, 0.0, 0, 0.0);
	}

	/**
	 * 
	 * @param mData
	 * @param mKmTotal
	 * @param mValor
	 * @param mLitros
	 * @param mKmParcial
	 * @param mMedia
	 */
	public Abastecimento(String mData, int mKmTotal, double mValor,
			double mLitros, int mKmParcial, double mMedia) {
		this(-1, mData, mKmTotal, mValor, mLitros, mKmParcial, mMedia);
	}

	/**
	 * 
	 * @param mId
	 * @param mData
	 * @param mKmTotal
	 * @param mValor
	 * @param mLitros
	 * @param mKmParcial
	 * @param mMedia
	 */
	public Abastecimento(long mId, String mData, int mKmTotal, double mValor,
			double mLitros, int mKmParcial, double mMedia) {
		super();
		this.mId = mId;
		this.mData = mData;
		this.mKmTotal = mKmTotal;
		this.mValor = mValor;
		this.mLitros = mLitros;
		this.mKmParcial = mKmParcial;
		this.mMedia = mMedia;
	}

	
	
	
	

	/**
	 * @return the mId
	 */
	public long getId() {
		return mId;
	}

	/**
	 * @param mId the mId to set
	 */
	public void setId(long mId) {
		this.mId = mId;
	}

	/**
	 * @return the mData
	 */
	public String getData() {
		return mData;
	}

	/**
	 * @param mData the mData to set
	 */
	public void setData(String mData) {
		this.mData = mData;
	}

	/**
	 * @return the mKmTotal
	 */
	public int getKmTotal() {
		return mKmTotal;
	}

	/**
	 * @param mKmTotal the mKmTotal to set
	 */
	public void setKmTotal(int mKmTotal) {
		this.mKmTotal = mKmTotal;
	}

	/**
	 * @return the mValor
	 */
	public double getValor() {
		return mValor;
	}

	/**
	 * @param mValor the mValor to set
	 */
	public void setValor(double mValor) {
		this.mValor = mValor;
	}

	/**
	 * @return the mLitros
	 */
	public double getLitros() {
		return mLitros;
	}

	/**
	 * @param mLitros the mLitros to set
	 */
	public void setLitros(double mLitros) {
		this.mLitros = mLitros;
	}

	/**
	 * @return the mKmParcial
	 */
	public int getKmParcial() {
		return mKmParcial;
	}

	/**
	 * @param mKmParcial the mKmParcial to set
	 */
	public void setKmParcial(int mKmParcial) {
		this.mKmParcial = mKmParcial;
	}

	/**
	 * @return the mMedia
	 */
	public double getMedia() {
		return mMedia;
	}

	/**
	 * @param mMedia the mMedia to set
	 */
	public void setMedia(double mMedia) {
		this.mMedia = mMedia;
	}

	/**
	 * Preenche o objeto abastecimento com as informações recebidas do cursor (uma linha)
	 * 
	 * @param c
	 *            Cursor
	 */
	public void init(Cursor c) {

		if (c != null) {
			mId = c.getInt(AbastecimentoData.AbastecimentoColumns._ID_IDX);
			mData = c.getString(AbastecimentoData.AbastecimentoColumns.DATA_IDX);			
			mKmTotal= c.getInt(AbastecimentoData.AbastecimentoColumns.KMTOTAL_IDX);
			mValor= c.getDouble(AbastecimentoData.AbastecimentoColumns.VALOR_IDX);
			mLitros= c.getDouble(AbastecimentoData.AbastecimentoColumns.LITROS_IDX);
			mKmParcial= c.getInt(AbastecimentoData.AbastecimentoColumns.KMPARCIAL_IDX);
			mMedia= c.getDouble(AbastecimentoData.AbastecimentoColumns.MEDIA_IDX);
		}

	}

	/**
	 * Limpa os campos de um objeto veículo permitindo que o objeto seja
	 * reaproveitado.
	 */
	public void clearFields() {
		this.setId(-1);
		setData("");
		setKmTotal(0);
		setValor(0);
		setLitros(0.0);
		setKmParcial(0);
		setMedia(0.0);

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Abastecimento [mId=" + mId + ", mData=" + mData + ", mKmTotal="
				+ mKmTotal + ", mValor=" + mValor + ", mLitros=" + mLitros
				+ ", mKmParcial=" + mKmParcial + ", mMedia=" + mMedia + "]";
	}

	
	
}
