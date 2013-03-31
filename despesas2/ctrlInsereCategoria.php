<?php
include "valida_sessao.php";

include 'DBConnection.php';
include 'Categoria.php';
include 'CategoriaDAO.php';

$id = -1;
$descricao = $_POST['descricao'];

$categoria = new Categoria($id, $descricao);

$con = DBConnection::getConnection();
    
$dao = new CategoriaDAO($con);

$lista = $dao->inserir($categoria);

$con->close();

header("Location: relCategoria.php");