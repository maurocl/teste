<?php

include 'DBConnection.php';
include 'Categoria.php';
include 'CategoriaDAO.php';

$id = $_GET['id'];

$con = DBConnection::getConnection();
    
$dao = new categoriaDAO($con);

$categoria = $dao->consultar($id);

$dao->excluir($categoria);

$con->close();

header("Location: relCategoria.php");