package br.com.mltech.utils;

/**
 * <p>Define uma transacao.<br>
 * 
 * <p>Essa interface define duas a��es:<br>
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
