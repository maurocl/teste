<?php

include 'DBConnection.php';
include 'Categoria.php';
include 'CategoriaDAO.php';
include 'Despesa.php';
include 'DespesaDAO.php';

$id = $_GET['id'];

echo "<p>ctrlAlteraDespesa";

$con = DBConnection::getConnection();

if($con==null) {
  echo "<p>conn � null";
}
else {
  echo "<p>conn N�O � null";
}
    
$dao = new DespesaDAO($con);

//$despesa = new Despesa($id, $data, $descricao, $valor, $categoria);
$despesa = $dao->consultar($id);

echo "<p>Despesa: " . $despesa ;

$dao->alterar($despesa);

echo "<p>Registro alterado id: " . $despesa->getId();

$con->close();