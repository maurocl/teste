<?php

include "Constantes.php";

/**
 * Classe responsсvel pela
 * obtenчуo de uma conexуo com
 * o Banco de Dados.
 * Usa os dados definidos no arquivo
 * Constantes.php afim de fazer a conexуo.
 * 
 */
class DBConnection {
  
  private static $con=null;

  private function __construct() {}

  /**
   * getConnection()
   *
   * Obtem uma conexуo com o banco de dados
   */
  public static function getConnection() {
    
    if ($con==null) {
       $con = new mysqli(SERVER, USER, PASS, DATABASE);
    }
    return $con;

  }
  
}