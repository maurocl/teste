<?php

include "Constantes.php";

/**
 * Classe respons�vel pela
 * obten��o de uma conex�o com
 * o Banco de Dados.
 * Usa os dados definidos no arquivo
 * Constantes.php afim de fazer a conex�o.
 * 
 */
class DBConnection {
  
  private static $con=null;

  private function __construct() {}

  /**
   * getConnection()
   *
   * Obtem uma conex�o com o banco de dados
   */
  public static function getConnection() {
    
    if ($con==null) {
       $con = new mysqli(SERVER, USER, PASS, DATABASE);
    }
    return $con;

  }
  
}