<?php
include "valida_sessao.php";

require_once 'Categoria.php';
require_once 'Despesa.php';
require_once 'CategoriaDAO.php';
require_once 'DespesaDAO.php';
require_once 'DBConnection.php';


  if(isset($_SESSION['data1'])) {
    $data1=$_SESSION['data1'];
  }
  else {
    $data1="01/01/2013";
  }

  if(isset($_SESSION['data2'])) {
    $data2=$_SESSION['data2'];
  }
  else {
    $data2="31/12/2013";
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

//echo "<p><p>vlrTotalDespesas=". $vlrTotalDespesas . "<br><br>";

echo "<p>";

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
  
  $x1 = "id=$idCategoria";
  $x2 = "data1=$data1";
  $x3 = "data2=$data2";

  $x4 = "\"detalheCategoria.php?";
  
  $x4 = $x4 .   $x1 . "&" . $x2 . "&" . $x3 . "\"";
  
  //echo "x4=[$x4]";
  
  echo "<tr>";
  echo "<td>" . $idCategoria . "</td>";
  echo "<td>" . $descrCategoria. "</td>";
  echo "<td class=\"fmtDireita\">" . $vlrTotalCategoria. "</td>";
  echo "<td><a href=" . $x4 . ">" . "detalhes" . "</a>";
  echo "<td class=\"fmtDireita\">" . $vlrPercTotal . "</td>";

  echo "</tr>";

  //" . "id=$idCategoria" . "&data1=01" . "&data2=02" .">detalhes</a>" .  "</td>";
  
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

echo "<p>";

//echo  "id=$idCategoria" . "&data1=\"$data1\"" . "&data2=\"$data2\"" ; 

$con->close();

?>