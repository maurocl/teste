<?php

class GenericDAO implements InterfaceDAO {
	
	// propriedades
	
	// métodos
	
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
	
	/**
	 * (non-PHPdoc)
	 * @see InterfaceDAO::save()
	 */
    public function save() {
		
	}
	
	/**
	 * (non-PHPdoc)
	 * @see InterfaceDAO::update()
	 */
	public function update() {
		
	}
	
	/**
	 * (non-PHPdoc)
	 * @see InterfaceDAO::delete()
	 */
	public function delete() {
		
	}
	
	/**
	 * (non-PHPdoc)
	 * @see InterfaceDAO::query()
	 */
	public function query() {
		
	}
	
	/**
	 * (non-PHPdoc)
	 * @see InterfaceDAO::getAll()
	 */
	public function getAll() {
		
	}
	
}

?>