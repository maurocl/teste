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

?>

<!DOCTYPE h1 PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>Sistema de Controle de Despesas</title>
<link rel="stylesheet" href="css/padrao.css" type="text/css">
</head>

<body>

<hr>
<h1 class="center">Sistema de Controle de Despesas</h1>
<hr>

<div class="center">

<p><a href="manutencaoCategoria.php">Manutenção de Categorias</a></p>
<p><a href="manutencaoDespesa.php">Manutenção de Despesas</a></p>
<p><a href="manutencaoCategoria.php">Relatório de Categorias</a></p>
<p><a href="relDespesa.php">Relatório de Despesas</a></p>
<p><a href="relDespesaByCategoria.php">Relatório de Despesas por Categoria</a></p>
<p><a href="relDespesaTotalByCategoria.php">Relatório de total de Despesas por Categoria</a></p>

<p><a href="logout.php">Logout</a></p>

</div>

<hr>

</body>
</html>