<?php

include 'DBConnection.php';
include 'Categoria.php';
include 'CategoriaDAO.php';

$id = $_GET['id'];

//echo "<p>ctrlExcluiCategoria";

//echo "<p>categoria: " . $categoria;

$con = DBConnection::getConnection();

if($con==null) {
  //echo "<p>conn � null";
}
else {
  //echo "<p>conn N�O � null";
}
    
$dao = new categoriaDAO($con);

$categoria = $dao->consultar($id);

//echo "<p>Categoria: " . $categoria ;

$dao->excluir($categoria);

//echo "<p>Registro excluido id: " . $categoria->getId();

$con->close();


header("Location: relCategoria.php");