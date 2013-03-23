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
	 * Data de realizaчуo da despesa
	 * @var unknown_type
	 */
	private $data;
	
	/**
	 * 
	 * Desscriчуo da despesa
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
	
	// mщtodos
	
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
	
	// mщtodos getters e setters
	
}

?>