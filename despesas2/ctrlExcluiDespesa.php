<?php

include 'DBConnection.php';
include 'Categoria.php';
include 'CategoriaDAO.php';
include 'Despesa.php';
include 'DespesaDAO.php';

$id = $_GET['id'];

echo "<p>ctrlExcluiDespesa";

echo "<p>despesa: " . $despesa;

$con = DBConnection::getConnection();

if($con==null) {
  echo "<p>conn é null";
}
else {
  echo "<p>conn NÃO é null";
}
    
$dao = new DespesaDAO($con);

//$despesa = new Despesa($id, $data, $descricao, $valor, $categoria);
$despesa = $dao->consultar($id);



echo "<p>Despesa: " . $despesa ;

$dao->excluir($despesa);

echo "<p>Registro excluido id: " . $despesa->getId();

$con->close();