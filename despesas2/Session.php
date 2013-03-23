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
	 * @var number
	 */
	private $id;
	
	/**
	 * 
	 * @param $id
	 */
	 function __construct($id) {
		$this->id = $id;
	}
	
	/**
	 * 
	 */
	public function getId() {
		return $this->id;
	}
	
	/**
	 * 
	 * @param $id
	 */
	public function setIt($id) {
		$this->id=$id;
	}
	
}

$s = new Session(1);

echo $s->getId();

$s->setIt(5);

echo $s->getId();
