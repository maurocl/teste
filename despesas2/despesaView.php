<?php

// start session
session_start();

echo "session=" . $_SESSION['authorizedUser'] ."<p>";

if($_SESSION['authorizedUser']!=null) {
//if($_SESSION['authorizedUser']!=1) {

	// if flag is absent
	// the user does not have view privileges
	// print error message
	echo "You are not authorized to view this pages.";

	// terminate processing
	// kick the client out
	exit();

}

include_once 'Despesa.php';
include_once 'Categoria.php';



if($_SESSION['despesa']==null) {
  $despesa = new Despesa();

  $despesa->setId("1");
  $despesa->setData("23/03/2013");
  $despesa->setDescricao("Gasolina Zafira");
  $despesa->setValor(95.67);
  $despesa->setCategoria($categoria);
  
}
else {
  
  $despesa = $_SESSION['despesa'];
  
}



$categoria = new Categoria("1","Combustível");


?>

<!DOCTYPE unspecified PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<form name="" method="" action="">

<p>Id:

<input name="id" value="<?php echo $despesa->getId() ?>">

<p>Data:

<input name="data" value="<?php echo $despesa->getData() ?>">

<p>Descrição:

<input name="descricao" value="<?php echo $despesa->getDescricao() ?>">

<p>Valor:

<input name="valor" value="<?php echo $despesa->getValor() ?>">

<select>
<option>Gasolina Zafira
<option>Gasolina Palio
<option>Gasolina 2006
</select>

</form>

<?php 
$despesa->getId();
$despesa->getData();
$despesa->getDescricao();
$despesa->getValor();
$despesa->getCategoria();

?>