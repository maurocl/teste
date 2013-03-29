<?php
/**
 * Session
 * 
 * Enter description here ...
 * @author maurocl
 *
 * @version 1.0
 * @access public
 * @todo
 */
class Session {
	
	/**
	 * Session ID
	 * 
	 * Enter description here ...
	 * @var number
	 */
	private $id;
	
	/**
	 * 
	 * Enter description here ...
	 * @param $id
	 */
	 function __construct($id) {
		$this->id = $id;
	}
	
	/**
	 * getId()
	 * 
	 * Enter description here ...
	 */
	public function getId() {
		return $this->id;
	}
	
	/**
	 * setId($id)
	 * 
	 * Enter description here ...
	 * @param $id
	 */
	public function setId($id) {
		$this->id=$id;
	}
	
}

$s = new Session(1);

echo "<br>id: " . $s->getId();

$s->setId(5);

echo "<br>id: " . $s->getId();
