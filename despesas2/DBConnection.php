<?php

include "Constantes.php";

/**
 * 
 * 
 * 
 *
 */
class DBConnection {
  
  private static $con=null;

  private function __construct() {}

  /**
   * getConnection()
   *
   * Obtem uma conexão com o banco de dados
   */
  public static function getConnection() {
    
    if ($con==null) {
       $con = new mysqli(SERVER, USER, PASS, DATABASE);
    }
    return $con;

  }
  
}