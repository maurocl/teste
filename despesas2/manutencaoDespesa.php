<?php

// start session
session_start();

// check session for flag

if($_SESSION['authorizedUser']!=1) {

  // if flag is absent
  // the user does not have view privileges
  // print error message
  echo "You are not authorized to view this pages.";

  // terminate processing
  // kick the client out
  exit();

}
else {

  //echo "Welcome!";

}

require_once 'Categoria.php';
require_once 'Despesa.php';
require_once 'CategoriaDAO.php';
require_once 'DespesaDAO.php';
require_once 'DBConnection.php';

// Data Inicial
$data1=$_POST['data1'];

// Data Final
$data2=$_POST['data2'];

if($data1==null) {
  $data1="01/01/2013";
}

if($data2==null) {
  $data2 = "31/12/2013";
}

?>

<!DOCTYPE h1 PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<title>Manutenção de Despesas</title>
<link rel="stylesheet" href="css/padrao.css" type="text/css" >
</head>

<?php 

echo "<h1 class=\"center\">Manutenção Despesa</h1>";

echo "<h2 align=center>Período: $data1 até $data2";

echo "<p><a href='success.php'>voltar</a><br>";

// exibe a lista de todas as despesas

$con = DBConnection::getConnection();

$daoCategoria = new CategoriaDAO($con);

$hash = $daoCategoria->listarCategoriaDescr();

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
echo "<td>Descr. Categoria ".  "</td>";
echo "<td>Editar".  "</td>";
echo "<td>Excluir".  "</td>";
echo "</tr>";

// valor total das despesas
$total = 0.00;

foreach($lista as $item) {

  //echo "<p><b>item</b>=$item";

  $df = date("d/m/Y", strtotime($item->getData()));
  //$df= $item->getData();
  
  $valor = number_format($item->getValor(),2,",",".");
  
  $descrCategoria = $hash[$item->getCategoria()];
  
  echo "<tr>";
  echo "<td>" . $item->getId() . "</td>";
  echo "<td>" . $df . "</td>";
  echo "<td class=\"fmtEsquerda\">" . $item->getDescricao(). "</td>";
  echo "<td class=\"fmtDireita\">" . $valor. "</td>";
  echo "<td>" . $item->getCategoria(). "</td>";
  echo "<td>" . $descrCategoria. "</td>";
  echo "<td><a href=\"ctrlAlteraDespesa.php?id="  . $item->getId()  . "\">editar</a>" .  "</td>";
  echo "<td><a href=\"ctrlExcluiDespesa.php?id=" . $item->getId() . "\">excluir</a>" . "</td>";

  $total += $item->getValor();
  
  echo "</tr>";

}

$valorTotalFormatado = number_format($total,2,",",".");

echo "<tr>";
echo "<td>&nbsp;"  . "</td>";
echo "<td>&nbsp;" .  "</td>";
echo "<td>&nbsp;".  "</td>";
echo "<td>" . $valorTotalFormatado . "</td>";
echo "<td>&nbsp;".  "</td>";
echo "<td>&nbsp;".  "</td>";
echo "<td>&nbsp;".  "</td>";
echo "<td>&nbsp;".  "</td>";
echo "</tr>";

echo "</table>";

// permite adicionar uma nova despesa
echo "<p align=\"left\"><a href=\"insereDespesa.php\">Insere nova despesa</a>";

$con->close();



?>