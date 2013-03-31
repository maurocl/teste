<?php
include "valida_sessao.php";

include 'DBConnection.php';
include 'Categoria.php';
include 'CategoriaDAO.php';
include 'Despesa.php';
include 'DespesaDAO.php';

$id = -1;

$data = $_POST['data'];
$descricao = $_POST['descricao'];
$valor = $_POST['valor'];
$descrCategoria = $_POST['descrCategoria'];

$pieces = explode("|",$descrCategoria);

$idCategoria = $pieces[1];

$categoria = $idCategoria;

// cria uma nova despesa
$despesa = new Despesa($id, $data, $descricao, $valor, $categoria);

$con = DBConnection::getConnection();
    
$dao = new DespesaDAO($con);

$lista = $dao->inserir($despesa);

$con->close();

header("Location: manutencaoDespesa.php");