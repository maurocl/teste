<?php

include_once 'DBConnection.php';
include_once 'Categoria.php';
include_once 'CategoriaDAO.php';

$id        = $_POST['id'];
$descricao = $_POST['descricao'];

$categoria = new Categoria($id, $descricao);

$con = DBConnection::getConnection();
    
$dao = new CategoriaDAO($con);

// busca a categoria que será alterada
$categoria = $dao->alterar($categoria);

header("Location: manutencaoCategoria.php");

?>