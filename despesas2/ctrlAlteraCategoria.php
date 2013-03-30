<?php

include 'DBConnection.php';
include 'Categoria.php';
include 'CategoriaDAO.php';

// Identificador da categoria
$id = $_GET['id'];

//echo "<p>ctrlAlteraCategoria";

$con = DBConnection::getConnection();

if($con==null) {
  echo "<p>conn é null";
}
else {
  echo "<p>conn NÃO é null";
}

$dao = new CategoriaDAO($con);

// busca a consulta que será alterada
$categoria = $dao->consultar($id);

//echo "<p>Buscando id categoria: $id";

// exibe informações sobre a despesa
//echo "<p>Categoria: " . $categoria ;

$con->close();

?>

<hr>
<h2>Altera Categoria</h2>
<hr>

<form name="frmAlteraCategoria2" method="post" action="confirmaAlteracaoCategoria.php" >

<p>Id: <input type="text" name="id" value="<?php echo $categoria->getId();?>">

<p>Descrição: <input type="text" name="descricao" value="<?php echo $categoria->getDescricao();?>">

<p>
<input type="submit" name="btnSubmit" value="submit"> 
<input type="reset" name="btnSubmit" value="reset">
</p>

</form>

