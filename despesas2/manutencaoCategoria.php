<?php


echo "<a href=''>voltar</a>";

// exibe uma lista de categorias

// adiciona um botão para permitir a inserção de uma nova categoria


require_once 'Categoria.php';
require_once 'Despesa.php';
require_once 'CategoriaDAO.php';
require_once 'DespesaDAO.php';
require_once 'DBConnection.php';

echo "<h1>Manutenção Categoria</h1>";

// exibe a lista de todas as categorias

// exibe uma lista das despesas

// permite adicionar uma nova categoria

$con = DBConnection::getConnection();

$dao = new CategoriaDAO($con);

//$lista = $dao->listarTodos();
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
  echo "<td><a href=\"ctrlAlteraCategoria.php?id="  . $item->getId()  . "\">editar</a>" .  "</td>";
  echo "<td><a href=\"ctrlExcluiCategoria.php?id=" . $item->getId() . "\">excluir</a>" . "</td>";

  echo "</tr>";

}

echo "</table>";

echo "<a href=\"insereCategoria.php\">Insere nova categoria</a>";

$con->close();



?>