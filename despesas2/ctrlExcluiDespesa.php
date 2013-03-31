<?php
include "valida_sessao.php";

include 'DBConnection.php';
include 'Categoria.php';
include 'CategoriaDAO.php';
include 'Despesa.php';
include 'DespesaDAO.php';

$id = $_GET['id'];

$con = DBConnection::getConnection();

$dao = new DespesaDAO($con);

//$despesa = new Despesa($id, $data, $descricao, $valor, $categoria);
$despesa = $dao->consultar($id);

$dao->excluir($despesa);

$con->close();

header("Location: manutencaoDespesa.php");