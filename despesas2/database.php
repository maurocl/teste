<?php

/**
 *
 * Enter description here ...
 * @var unknown_type
 */

$host='localhost';
$user='root';
$pass='';
$db='mydb';

/**
 * connect()
 *
 * Enter description here ...
 */
function connect() {
  
  $mysqli = new mysqli($host, $user, $pass, $mydb);
  
  if(mysqli_connect_errno()) {
    die("Error: Cannot connect. " . mysqli_connect_errno());
  }
  
}

/**
 * disconnect()
 *
 * Enter description here ...
 */
function disconnect() {
  $mysqli->close();
}

/**
 * query
 *
 * @param $sql
 */
function query($sql) {
  
  if ($result=$mysqli->query($sql)) {
    
    if ($result->num_rows >0) {
      while($row = $result->fetch_row()) {
        	
      }
    }
    else {
      echo "No records found!";
    }
    
  }
  
}

/**
 * insert
 *
 * @param $sql
 */
function insert($sql) {
  if(!$mysqli->query($sql)) {
    die ("Error: " . $mysqli->error . " (query was $sql)");
  }
}

/**
 * delete
 * 
 * @param $sql
 */
function delete($sql) {
  
  if(!$mysqli->query($sql)) {
    die ("Error: " . $mysqli->error . " (query was $sql)");
  }
  
}

/**
 * update
 * 
 * @param $sql
 */
function update($sql) {
  if(!$mysqli->query($sql)) {
    die ("Error: " . $mysqli->error . " (query was $sql)");
  }
}

?>