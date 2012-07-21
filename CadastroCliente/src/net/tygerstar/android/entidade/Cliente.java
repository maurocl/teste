package net.tygerstar.android.entidade;

import java.io.Serializable;

public class Cliente implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7896797804512742070L;

	public static final String[] colunas = 
		new String[]{"_id", "nome", "endereco", "bairro", "cidade", "estado"};
	
	private int id = 0;
	private String nome = "";
	private String endereco = "";
	private String bairro = "";
	private String cidade = "";
	private String estado = "";
	
	@Override
	public String toString() {
		return nome +" / "+cidade;
	}

  
  public int getId() {
  
    return id;
  }

  
  public void setId(int id) {
  
    this.id = id;
  }

  
  public String getNome() {
  
    return nome;
  }

  
  public void setNome(String nome) {
  
    this.nome = nome;
  }

  
  public String getEndereco() {
  
    return endereco;
  }

  
  public void setEndereco(String endereco) {
  
    this.endereco = endereco;
  }

  
  public String getBairro() {
  
    return bairro;
  }

  
  public void setBairro(String bairro) {
  
    this.bairro = bairro;
  }

  
  public String getCidade() {
  
    return cidade;
  }

  
  public void setCidade(String cidade) {
  
    this.cidade = cidade;
  }

  
  public String getEstado() {
  
    return estado;
  }

  
  public void setEstado(String estado) {
  
    this.estado = estado;
  }

  
  public static String[] getColunas() {
  
    return colunas;
  }
	
	
	
}
