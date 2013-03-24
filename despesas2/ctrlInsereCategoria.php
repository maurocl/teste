<?php

include 'DBConnection.php';
include 'Categoria.php';
include 'CategoriaDAO.php';


$id = -1;
$descricao = $_POST['descricao'];

echo "<p>ctrlInsereCategoria";

$categoria = new Categoria($id, $descricao);

echo "<p>Categoria: " . $categoria;

$con = DBConnection::getConnection();

if($con==null) {
  echo "<p>conn é null";
}
else {
  echo "<p>conn NÃO é null";
}
    
$dao = new CategoriaDAO($con);

$lista = $dao->inserir($categoria);

echo "<p>Registro inserido com id: " . $categoria->getId();

$con->close();