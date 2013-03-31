<?php
include "valida_sessao.php";

require_once 'Categoria.php';
require_once 'Despesa.php';
require_once 'CategoriaDAO.php';
require_once 'DespesaDAO.php';
require_once 'DBConnection.php';

?>
<!DOCTYPE h1 PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<title>Manutenção de Categorias</title>
<link rel="stylesheet" href="css/padrao.css" type="text/css" >
</head>

<hr>
<h1 class="center">Manutenção Categoria</h1>
<hr>

<p><a href="success.php">voltar</a></p>

<br />

<?php 

// exibe a lista de todas as categorias

$con = DBConnection::getConnection();

$dao = new CategoriaDAO($con);

$lista = $dao->listarTodos();

echo "<div class=\"center\">";

echo "<table border=1>";

echo "<tr>";
echo "<td class=\"title\">ID"  . "</td>";
echo "<td class=\"title\">Descrição".  "</td>";
echo "<td class=\"title\">Editar".  "</td>";
echo "<td class=\"title\">Excluir".  "</td>";
echo "</tr>";

foreach($lista as $item) {
  
  echo "<tr>";
  
  echo "<td>" . $item->getId() . "</td>";
  echo "<td>" . $item->getDescricao(). "</td>";
  echo "<td><a href=\"ctrlAlteraCategoria.php?id="  . $item->getId() . "\">editar</a>" .  "</td>";
  echo "<td><a href=\"ctrlExcluiCategoria.php?id="  . $item->getId() . "\">excluir</a>" . "</td>";

  echo "</tr>";

}

echo "</table>";

echo "</div>";

// adiciona um botão para permitir a inserção de uma nova categoria
echo "<p><a href=\"insereCategoria.php\">Insere nova categoria</a>";

$con->close();

?>