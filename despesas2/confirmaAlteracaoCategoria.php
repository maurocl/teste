<?php

include_once 'DBConnection.php';
include_once 'Categoria.php';
include_once 'CategoriaDAO.php';

$id        = $_POST['id'];
$descricao = $_POST['descricao'];

$categoria = new Categoria($id, $descricao);

//echo "<p>Despesa=$despesa";

$con = DBConnection::getConnection();

if($con==null) {
 // echo "<p>conn � null";
}
else {
  //echo "<p>conn N�O � null";
}
    
$dao = new CategoriaDAO($con);

// busca a categoria que ser� alterada
$categoria = $dao->alterar($categoria);

header("Location: manutencaoCategoria.php");

?>