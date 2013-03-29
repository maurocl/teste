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

echo "<h1>Sistema de Controle de Despesas</h1>";

echo "<p><a href=\"manutencaoCategoria.php\">Manuten��o de Categorias</a></p>";
echo "<p><a href=\"manutencaoDespesa.php\">Manuten��o de Despesas</a></p>";
echo "<p><a href=\"manutencaoCategoria.php\">Relat�rio de Categorias</a></p>";
echo "<p><a href=\"relDespesa.php\">Relat�rio de Despesas</a></p>";

echo "<p><a href=\"logout.php\">Logout</a></p>";

?>