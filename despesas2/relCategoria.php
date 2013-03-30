<?php 
require_once 'Categoria.php';
require_once 'Despesa.php';
require_once 'CategoriaDAO.php';
require_once 'DespesaDAO.php';
require_once 'DBConnection.php';

echo "<hr>";
echo "<h1 align=center>Relatório de Categorias</h1>";
echo "<hr>";

echo "<p><a href='success.php'>voltar</a><p><br>";

$con = DBConnection::getConnection();

// exibe a lista de todas as categorias
$dao = new CategoriaDAO($con);

$lista = $dao->listarTodos();

echo "<table border=1>";

echo "<tr>";
echo "<td>ID"  . "</td>";
echo "<td>Descrição".  "</td>";
echo "<td>Editar".  "</td>";
echo "<td>Excluir".  "</td>";
echo "</tr>";

foreach($lista as $item) {
  
  echo "<tr>";
  echo "<td>" . $item->getId() . "</td>";
  echo "<td>" . $item->getDescricao(). "</td>";
  echo "<td><a href=\"ctrlAlteraCategoria.php?id=" . $item->getId()  . "\">editar</a>" .  "</td>";
  echo "<td><a href=\"ctrlExcluiCategoria.php?id=" . $item->getId() . "\">excluir</a>" . "</td>";

  echo "</tr>";

}

echo "</table>";

// permite adicionar uma nova categoria
echo "<p><a href=\"insereCategoria.php\">Insere nova categoria</a>";

$con->close();

?>