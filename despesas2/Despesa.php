<?php
class Despesa {
	
	// propriedades
	
	/**
	 * 
	 * Identificador da despesas
	 * @var unknown_type
	 */
	private $id;
	
	/**
	 * 
	 * Data de realiza��o da despesa
	 * @var unknown_type
	 */
	private $data;
	
	/**
	 * 
	 * Desscri��o da despesa
	 * @var String
	 */
	private $descricao;
	
	/**
	 * 
	 * Valor da despesa
	 * 
	 * @var unknown_type
	 */
	private $valor;
	
	/**
	 * 
	 * Categoria a qual a despesa pertence
	 * 
	 * @var unknown_type
	 */
	private $categoria;
	
	// m�todos
	
	/**
	 * 
	 * Enter description here ...
	 */
	function __construct() {
		echo "Executando o construtor ...\n";
	}
	
	/**
	 * 
	 * Enter description here ...
	 */
	function __destruct() {
		echo "Executando o destrutor ...\n";
	}
	
	// m�todos getters e setters
	
}

?>