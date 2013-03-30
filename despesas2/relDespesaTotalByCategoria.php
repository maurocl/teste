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

require_once 'Categoria.php';
require_once 'Despesa.php';
require_once 'CategoriaDAO.php';
require_once 'DespesaDAO.php';
require_once 'DBConnection.php';

// Data Inicial
$data1=$_POST['data1'];

// Data Final
$data2=$_POST['data2'];

if(!isset($data1)) {
  $data1="01/01/2013";
}

if(!isset($data2)) {
  $data2 = "31/12/2013";
}

?>

<!DOCTYPE h1 PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<title>Manutenção de Despesas</title>
<link rel="stylesheet" href="css/padrao.css" type="text/css">
</head>

<?php

echo "<h1 align=center>Total de Despesas por Categoria</h1>";

echo "<h2 align=center>Período: $data1 até $data2";

echo "<p><a href='relDespesa.php'>voltar</a><br>";

// exibe a lista de todas as despesas

$con = DBConnection::getConnection();

$daoCategoria = new CategoriaDAO($con);

$hash = $daoCategoria->listarCategoriaDescr();

$dao = new DespesaDAO($con);

$lista = $dao->listarTotalByCategoria($data1,$data2);

// obtem o valor total das despesas entre duas datas
$vlrTotalDespesas = $dao->listarVlrTotalByCategoria($data1,$data2);

echo "<p><p>vlrTotalDespesas=". $vlrTotalDespesas . "<br><br>";

echo "<table border=1>";

echo "<tr>";
echo "<td>Categoria"  . "</td>";
echo "<td>Descr. Categoria ".  "</td>";
echo "<td>Valor".  "</td>";
echo "<td>Detalhes".  "</td>";
echo "<td>%".  "</td>";
echo "</tr>";

$total = 0.0;

foreach($lista as $item) {

  $idCategoria = $item[0]; // categoria
  $item1 = $item[1]; // valor total da categoria

  $total += $item1;

  $vlrPercTotal = number_format(($item1/$vlrTotalDespesas*100),2,",",".");
  $vlrTotalCategoria = number_format($item1,2,",",".");

  // obtem a descricao da categoria
  $descrCategoria = $hash[$idCategoria];

  echo "<tr>";
  echo "<td>" . $idCategoria . "</td>";
  echo "<td>" . $descrCategoria. "</td>";
  echo "<td class=\"fmtDireita\">" . $vlrTotalCategoria. "</td>";
  echo "<td><a href=\"ctrlAlteraDespesa.php?id="  . $idCategoria  . "\">detalhes</a>" .  "</td>";
  echo "<td class=\"fmtDireita\">" . $vlrPercTotal . "</td>";

  echo "</tr>";

}

// Formato o valor total das despesas entre duas datas.
$vlrTotal = number_format($total,2,",",".");

echo "<tr>";
echo "<td>" . "&nbsp;"  . "</td>";
echo "<td>" . "&nbsp;" . "</td>";
echo "<td>" . $vlrTotal . "</td>";
echo "<td>" . "&nbsp;" .  "</td>";
echo "<td>" . "&nbsp;" .  "</td>";
echo "</tr>";


echo "</table>";

$con->close();

?>
