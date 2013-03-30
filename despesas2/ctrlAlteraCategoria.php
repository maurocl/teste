<?php

include 'DBConnection.php';
include 'Categoria.php';
include 'CategoriaDAO.php';

// Identificador da categoria
$id = $_GET['id'];

//echo "<p>ctrlAlteraCategoria";

$con = DBConnection::getConnection();

if($con==null) {
  echo "<p>conn � null";
}
else {
  echo "<p>conn N�O � null";
}

$dao = new CategoriaDAO($con);

// busca a consulta que ser� alterada
$categoria = $dao->consultar($id);

//echo "<p>Buscando id categoria: $id";

// exibe informa��es sobre a despesa
//echo "<p>Categoria: " . $categoria ;

$con->close();

?>

<hr>
<h2>Altera Categoria</h2>
<hr>

<form name="frmAlteraCategoria2" method="post" action="confirmaAlteracaoCategoria.php" >

<p>Id: <input type="text" name="id" value="<?php echo $categoria->getId();?>">

<p>Descri��o: <input type="text" name="descricao" value="<?php echo $categoria->getDescricao();?>">

<p>
<input type="submit" name="btnSubmit" value="submit"> 
<input type="reset" name="btnSubmit" value="reset">
</p>

</form>

