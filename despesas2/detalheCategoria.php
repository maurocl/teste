<?php

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

echo "<h1 class=\"center\">Detalhes da Despesa</h1>";

echo "<p>Exibe despesas da categoria [$idCategoria] no período de [$data1] à [$data2]";

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

echo "<p>Categoria: " . $idCategoria . " - " . $descrCategoria;

echo "<p>";

echo "<table border=1>";

echo "<tr>";

echo "<td class=\"title\">" . "Id" . "</td>";
echo "<td class=\"title\">" . "Data". "</td>";
echo "<td class=\"title\">" . "Descricao". "</td>";
echo "<td class=\"title\">" . "Valor". "</td>";

echo "</tr>";

foreach($lista as $item) {

  $id        = $item[0];
  $data      = $item[1];
  $descricao = $item[2];
  $valor     = $item[3];

  //echo "<p>item=[$item]";

  $total += $valor;

  echo "<tr>";

  echo "<td>" . $id . "</td>";
  echo "<td>" . $data. "</td>";
  echo "<td>" . $descricao. "</td>";
  echo "<td class=\"right\">" . $valor. "</td>";

  echo "</tr>";

}

$vlrTotal = number_format($total,2,",",".");

echo "<tr>";

echo "<td>" . "&nbsp;" . "</td>";
echo "<td>" . "&nbsp;" . "</td>";
echo "<td>" . "&nbsp;" . "</td>";
echo "<td class=\"right\">" . $vlrTotal. "</td>";

echo "</tr>";

echo "</table>";

// Formato o valor total das despesas entre duas datas.
//$vlrTotal = number_format($total,2,",",".");

echo "<p><a href=\"relDespesaTotalByCategoria.php\">voltar</a>";

$x2 = "data1=$data1";
$x3 = "data2=$data2";

$x4 = "\"relDespesaTotalByCategoria.php?";

$x4 = $x4 . $x2 . "&" . $x3 . "\"";

echo "<td><a href=" . $x4 . ">" . "novo teste" . "</a>";

?>