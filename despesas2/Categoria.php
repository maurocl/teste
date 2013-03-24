<?php
class Categoria {

  // propriedades

  /**
   *
   * @var unknown_type
   */
  private $id;

  /**
   *
   * @var unknown_type
   */
  private $descricao;

  // métodos



  function __construct($id,$descricao) {
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

  function getId() {
    return $this->id;
  }

  function getDescricao() {
    return $this->descricao;
  }

  function setId($id) {
    $this->id = $id;
  }

  function setDescricao($descricao) {
    $this->descricao = $descricao;
  }

  function __toString() {
    return "Id: " . $this->getId() . ", Descricao: " . $this->getDescricao();
  }

}

?>