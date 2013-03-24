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

  function __construct($id, $data, $descricao, $valor, $categoria) {
    $this->id        = $id;
    $this->data      = $data;
    $this->descricao = $descricao;
    $this->valor     = $valor;
    $this->categoria = $categoria;
  }


  /**
   *
   * Enter description here ...
   */
  function __destruct() {

  }

  // m�todos getters e setters

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

  function getData() {
    return $this->data;
  }

  function setData($data) {
    $this->data = $data;
  }

  function getValor() {
    return $this->valor;
  }

  function setValor($valor) {
    $this->valor = $valor;
  }

  function getCategoria() {
    return $this->categoria;
  }

  function setCategoria($categoria) {
    $this->categoria = $categoria;
  }

  function __toString() {
    return "Id: " . $this->getId() . ", data: " .
    $this->getData(). ", descri��o: " .
    $this->getDescricao() .", valor: " .
    $this->getValor() .", categoria: " .
    $this->getCategoria();
  }

}

?>