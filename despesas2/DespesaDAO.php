<?php

class DespesaDAO {
  // propriedades

  // métodos

  private $con; // conexão
  
  /**
   *
   *
   */
  function __construct($con) {
    
  }

  /**
   *
   *
   */
  function __destruct() {
    
  }


  public function save($despesa) {
    
    // insert into despesa values 
   
    
   

  }

  /**
   * (non-PHPdoc)
   * @see InterfaceDAO::update()
   */
  public function update($despesa) {

  }

  /**
   * (non-PHPdoc)
   * @see InterfaceDAO::delete()
   */
  public function delete($despesa) {

  }

  /**
   * (non-PHPdoc)
   * @see InterfaceDAO::query($despesa)
   */
  public function query() {

  }

  /**
   * (non-PHPdoc)
   * @see InterfaceDAO::getAll()
   */
  public function getAll($despesa) {

  }

  public function listAll() {
    // lista todas as despesas compreendidas entre duas datas


    $lista = array();

    //$despesaDAO->

    array_push($lista, $despesa);

    // retorna uma lista de despesas (ordendadas por data)

    return $list;

  }

  public function listByDate($date1,$date2) {
     // retorna uma lista de despesas compreendidas entre duas datas
     
   $lista = array(); 
    return $list;
  }

}

?>