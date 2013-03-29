<?php

//include_once 'Categoria.php';
include_once 'Despesa.php';
//include_once 'CategoriaDAO.php';
include_once 'DespesaDAO.php';
include_once 'DBConnection.php';

$id        = $_POST['id'];
$data      = $_POST['data'];
$descricao = $_POST['descricao'];
$valor     = $_POST['valor'];
$categoria = $_POST['categoria'];

$despesa = new Despesa($id, $data, $descricao, $valor, $categoria);

#echo "<p>Despesa=$despesa";

$con = DBConnection::getConnection();

if($con==null) {
 # echo "<p>conn é null";
}
else {
  #echo "<p>conn NÃO é null";
}
    
$dao = new DespesaDAO($con);

// busca a consulta que será alterada
$despesa = $dao->alterar($despesa);

header("Location: manutencaoDespesa.php");

?>