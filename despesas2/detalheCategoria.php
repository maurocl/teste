<?php
include "valida_sessao.php";

require_once 'Categoria.php';
require_once 'Despesa.php';
require_once 'CategoriaDAO.php';
require_once 'DespesaDAO.php';
require_once 'DBConnection.php';

$idCategoria = $_GET['id'];

$data1 = $_GET['data1'];
$data2 = $_GET['data2'];

?>

<!DOCTYPE h1 PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<title>Detalhe Categoria</title>
<link rel="stylesheet" href="css/padrao.css" type="text/css">
</head>

<?php

// conecta no banco de dados
$con = DBConnection::getConnection();

$daoCategoria = new CategoriaDAO($con);

// Chave do hash é dada pelo código da categoria e o
// valor associado é a descrição da categoria
$hash = $daoCategoria->listarCategoriaDescr();

$dao = new DespesaDAO($con);

// exibe a lista de todas as despesas
$lista = $dao->listarByCategoriaBetweenData($idCategoria, $data1, $data2);

//echo "<pre>";
//echo "print_r($lista)";
//echo "</pre>";

$total = 0.0;

// obtem a descricao da categoria
$descrCategoria = $hash[$idCategoria];

echo "<h1 class=\"center\">Detalhes da Despesa</h1>";

echo "<p class=\"center\">Categoria: " . $idCategoria . " - " . $descrCategoria;

echo "<p class=\"center\">Período de [$data1] à [$data2]";

echo "<p>";

echo "<div class=\"center\">";

echo "<table border=1>";

echo "<tr>";

echo "<td class=\"title\">" . "Id" . "</td>";
echo "<td class=\"title\">" . "Data". "</td>";
echo "<td class=\"title\">" . "Descricao". "</td>";
echo "<td class=\"title\">" . "Valor". "</td>";

echo "</tr>";

foreach($lista as $item) {

  //echo "<p>$item[3]</p>";
  
  $id        = $item[0];
  $data      = $item[1];
  $descricao = $item[2];
  $valor     = $item[3];

  // valor formatado da despesa
  $vlrFormatado = number_format($valor,2,",",".");

  // totaliza o valor
  $total += $valor;

  echo "<tr>";

  echo "<td>" . $id . "</td>";
  echo "<td>" . $data. "</td>";
  echo "<td>" . $descricao. "</td>";
  echo "<td class=\"right\">" . $vlrFormatado . "</td>";

  echo "</tr>";

}

// formata o valor total do intervalo
$vlrTotal = number_format($total,2,",",".");

echo "<tr>";

echo "<td>" . "&nbsp;" . "</td>";
echo "<td>" . "&nbsp;" . "</td>";
echo "<td>" . "&nbsp;" . "</td>";
echo "<td class=\"right\">" . $vlrTotal. "</td>";

echo "</tr>";

echo "</table>";

echo "</div>";

$x2 = "data1=$data1";
$x3 = "data2=$data2";

$x4 = "\"relDespesaTotalByCategoria.php?";

$x4 = $x4 . $x2 . "&" . $x3 . "\"";

echo "<td><a href=" . $x4 . ">" . "Voltar" . "</a>";

?>