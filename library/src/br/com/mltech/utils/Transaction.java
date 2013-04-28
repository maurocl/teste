package br.com.mltech.utils;

/**
 * <p>Define uma transacao.<br>
 * 
 * <p>Essa interface define duas ações:<br>
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
