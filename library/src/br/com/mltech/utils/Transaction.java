package br.com.mltech.utils;

/**
 * Define uma transacao.<br>
 * 
 * Essa interface define duas ações:<br>
 * - execução de uma tarefa<br>
 * - atualização de uma visão<br>
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
