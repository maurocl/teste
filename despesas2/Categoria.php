<?php
class Categoria {

  // propriedades

  /**
   *
   * @var int
   */
  private $id;

  /**
   *
   * @var string
   */
  private $descricao;

  // métodos

  function __construct($id, $descricao) {
    $this->id        = $id;
    $this->descricao = $descricao;
  }

  /**
   *
   * Enter description here ...
   */
  function __destruct() {

  }

  // métodos getters e setters

  /**
   * 
   * Enter description here ...
   */
  function getId() {
    return $this->id;
  }

  /**
   * 
   * Enter description here ...
   */
  function getDescricao() {
    return $this->descricao;
  }

  /**
   * 
   * Enter description here ...
   * @param unknown_type $id
   */
  function setId($id) {
    $this->id = $id;
  }

  /**
   * 
   * Enter description here ...
   * @param unknown_type $descricao
   */
  function setDescricao($descricao) {
    $this->descricao = $descricao;
  }

  /**
   * 
   * Enter description here ...
   */
  function __toString() {
    return "Id: " . $this->getId() . ", Descricao: " . $this->getDescricao();
  }

}

?>