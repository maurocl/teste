<?php
/**
 * 
 * Representa uma categoria onde uma despesa
 * � classificada.
 * 
 * @author maurocl
 *
 */
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

  // m�todos

  /**
   * 
   * 
   * @param $id
   * @param $descricao
   */
  function __construct($id, $descricao) {
    $this->id        = $id;
    $this->descricao = $descricao;
  }

  /**
   *
   * 
   */
  function __destruct() {

  }

  // m�todos getters e setters

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
   * 
   * @param unknown_type $id
   */
  function setId($id) {
    $this->id = $id;
  }

  /**
   * 
   * 
   * @param unknown_type $descricao
   */
  function setDescricao($descricao) {
    $this->descricao = $descricao;
  }

  /**
   * 
   * 
   */
  function __toString() {
    return "Id: " . $this->getId() . ", Descricao: " . $this->getDescricao();
  }

}

?>