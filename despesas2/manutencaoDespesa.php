<?php

include "valida_sessao.php";

require_once 'Categoria.php';
require_once 'Despesa.php';
require_once 'CategoriaDAO.php';
require_once 'DespesaDAO.php';
require_once 'DBConnection.php';

if(isset($_POST["data1"])) {
  $data1 = $_POST["data1"];
  //echo "<p>Data1: $data1";
}
else {
  $data1="01/01/2013";
}
$_SESSION["data1"] = $data1;

if(isset($_POST["data2"])) {
  $data2 = $_POST["data2"];
  //echo "<p>Data2: $data2";
}
else {
  $data2 = "31/12/2013";
}
$_SESSION["data2"]=$data2;


?>

<!DOCTYPE h1 PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<title>Manutenção de Despesas</title>
<link rel="stylesheet" href="css/padrao.css" type="text/css">
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
echo "<td class=\"title\">ID"  . "</td>";
echo "<td class=\"title\">Data" .  "</td>";
echo "<td class=\"title\">Descrição".  "</td>";
echo "<td class=\"title\">Valor".  "</td>";
echo "<td class=\"title\">Categoria ".  "</td>";
echo "<td class=\"title\">Descr. Categoria ".  "</td>";
echo "<td class=\"title\">Editar".  "</td>";
echo "<td class=\"title\">Excluir".  "</td>";
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


echo "<p>";
foreach($_SESSION as $key=>$value) {
  echo "<br>key=$key, value=$value";
}

$con->close();

?>