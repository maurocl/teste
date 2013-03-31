<?php
/**
 * 
 * Representa uma categoria onde uma despesa
 * é classificada.
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

  // métodos

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