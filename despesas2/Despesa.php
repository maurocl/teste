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

  // mщtodos getters e setters

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
   * @param $descricao
   */
  function setDescricao($descricao) {
    $this->descricao = $descricao;
  }

  /**
   * 
   * Enter description here ...
   */
  function getData() {
    return $this->data;
  }

  /**
   * 
   * Enter description here ...
   * @param $data
   */
  function setData($data) {
    $this->data = $data;
  }

  /**
   * 
   * Enter description here ...
   */
  function getValor() {
    return $this->valor;
  }

  /**
   * 
   * Enter description here ...
   * @param $valor
   */
  function setValor($valor) {
    $this->valor = $valor;
  }

  /**
   * 
   * Enter description here ...
   */
  function getCategoria() {
    return $this->categoria;
  }

  /**
   * 
   * Enter description here ...
   * @param $categoria
   */
  function setCategoria($categoria) {
    $this->categoria = $categoria;
  }

  /**
   * 
   * Enter description here ...
   */
  function __toString() {
    return "Id: " . $this->getId() . ", data: " .
    $this->getData(). ", descriчуo: " .
    $this->getDescricao() .", valor: " .
    $this->getValor() .", categoria: " .
    $this->getCategoria();
  }

}

?>