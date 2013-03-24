<?php

include 'DBConnection.php';
include 'Categoria.php';
include 'CategoriaDAO.php';
include 'Despesa.php';
include 'DespesaDAO.php';

$id = -1;
$data = $_POST['data'];
$descricao = $_POST['descricao'];
$valor = $_POST['valor'];
//$categoria = $_POST['categoria'];
$categoria = 1;

echo "<p>ctrlInsereDespesa";

$despesa = new Despesa($id, $data, $descricao, $valor, $categoria);

echo "<p>despesa: " . $despesa;

$con = DBConnection::getConnection();

if($con==null) {
  echo "<p>conn é null";
}
else {
  echo "<p>conn NÃO é null";
}
    
$dao = new DespesaDAO($con);

$lista = $dao->inserir($despesa);

echo "<p>Registro inserido com id: " . $despesa->getId();

$con->close();