<?php

require_once 'Categoria.php';
require_once 'Despesa.php';
require_once 'CategoriaDAO.php';
require_once 'DespesaDAO.php';
require_once 'DBConnection.php';

$data1=$_POST['data1'];
$data2=$_POST['data2'];

if($data1==null) {
  $data1="20/03/2013";
}

if($data2==null) {
  $data2 = "31/03/2013";
}

echo "<h1>Manutenção Despesa</h1>";

echo "<h2>Período: $data1 até $data2";

echo "<p><a href='relDespesa.php'>voltar</a>";

// exibe a lista de todas as despesas

// exibe uma lista das despesas

// permite adicionar uma nova despesa

$con = DBConnection::getConnection();

$dao = new DespesaDAO($con);

//$lista = $dao->listarTodos();
$lista = $dao->listarTodosByData($data1,$data2);

echo "<table border=1>";

echo "<tr>";
echo "<td>ID"  . "</td>";
echo "<td>Data" .  "</td>";
echo "<td>Descrição".  "</td>";
echo "<td>Valor".  "</td>";
echo "<td>Categoria ".  "</td>";
echo "<td>Editar".  "</td>";
echo "<td>Excluir".  "</td>";
echo "</tr>";

foreach($lista as $item) {

  //echo "<p><b>item</b>=$item";

  $df = date("d/m/Y", strtotime($item->getData()));
  //$df= $item->getData();
  
  echo "<tr>";
  echo "<td>" . $item->getId() . "</td>";
  echo "<td>" . $df . "</td>";
  echo "<td>" . $item->getDescricao(). "</td>";
  echo "<td>" . $item->getValor(). "</td>";
  echo "<td>" . $item->getCategoria(). "</td>";
  echo "<td><a href=\"ctrlAlteraDespesa.php?id="  . $item->getId()  . "\">editar</a>" .  "</td>";
  echo "<td><a href=\"ctrlExcluiDespesa.php?id=" . $item->getId() . "\">excluir</a>" . "</td>";

  echo "</tr>";

}

echo "</table>";

echo "<a href=\"insereDespesa.php\">Insere nova despesa</a>";

$con->close();



?>