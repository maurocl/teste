<?php

include 'DBConnection.php';
include 'Categoria.php';
include 'CategoriaDAO.php';

// Identificador da categoria
$id = $_GET['id'];

//echo "<p>ctrlAlteraCategoria";

$con = DBConnection::getConnection();

$dao = new CategoriaDAO($con);

// busca a consulta que será alterada
$categoria = $dao->consultar($id);

$con->close();

?>

<head>
<title>Altera Categoria</title>
<link rel="stylesheet" href="css/padrao.css" type="text/css">
</head>

<hr>
<h1 class="center" >Altera Categoria</h2>
<hr>

<form name="frmAlteraCategoria2" method="post" action="confirmaAlteracaoCategoria.php" >

<p>Id: <input type="text"  size="5"  readonly="readonly" name="id" value="<?php echo $categoria->getId();?>">

<p>Descrição: <input type="text" name="descricao" value="<?php echo $categoria->getDescricao();?>">

<p>
<div class="center">
<input type="submit" name="btnSubmit" value="Alterar"> 
<input type="reset" name="btnSubmit" value="Limpar">
</div>
</p>

</form>

