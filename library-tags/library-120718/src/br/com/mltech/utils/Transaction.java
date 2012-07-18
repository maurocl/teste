package br.com.mltech.utils;

/**
 * Define uma transacao.<br>
 * 
 * Essa interface define duas a��es:<br>
 * - execu��o de uma tarefa<br>
 * - atualiza��o de uma vis�o<br>
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
