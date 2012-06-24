package br.com.mltech.utils;

/**
 * Transaction
 * 
 * @author maurocl
 *
 */
public interface Transaction {

	/**
	 * executa uma tarefa
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception;
	
	/**
	 * atualiza a view
	 */
	public void updateView();
	
}
