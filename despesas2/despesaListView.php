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



if($_SESSION['listaDespesas']==null) {
  
  $despesa = new Despesa();

  $despesa->setId("1");
  $despesa->setData("23/03/2013");
  $despesa->setDescricao("Gasolina Zafira");
  $despesa->setValor(95.67);
  $despesa->setCategoria($categoria);

  $despesa2 = new Despesa();

  $despesa2->setId("2");
  $despesa2->setData("30/03/2013");
  $despesa2->setDescricao("Alcool Palio");
  $despesa2->setValor(66.67);
  $despesa2->setCategoria($categoria);
  

  $despesa3 = new Despesa();

  $despesa3->setId("3");
  $despesa3->setData("20/03/2013");
  $despesa3->setDescricao("Alcool 206");
  $despesa3->setValor(70);
  $despesa3->setCategoria($categoria);
  
  
  $listaDespesas = array($despesa,$despesa2,$despesa3);
  

  
}
else {
  
  $listaDespesas = $_SESSION['listaDespesas'];
  
}

foreach ($listaDespesas as $despesa) {
  //echo "<p>" .$despesa->getId() . "";
  //echo "<p>" .$despesa->getData() . "";
  //echo "<p>" .$despesa->getDescricao() . "";
  //echo "<p>" .$despesa->getValor() . "";
}

$categoria = new Categoria("1","Combustível");

$total = 0;

echo "<table border=1>";

foreach ($listaDespesas as $despesa) {
  
  echo "<tr>";
  
  echo "<td>" .$despesa->getId() . "</td>";
  echo "<td>" .$despesa->getData() . "</td>";
  echo "<td>" .$despesa->getDescricao() . "</td>";
  echo "<td align=right>" .$despesa->getValor() . "</td>";
  
  $total += $despesa->getValor();
  
  echo "</tr>";
}

  echo "<tr>";
  
  echo "<td>&nbsp;</td>";
  echo "<td>&nbsp;</td>";
  echo "<td>&nbsp;</td>";
  echo "<td>" .$total . "</td>";

  echo "</tr>";
  

echo "</table>";

?>


<?php 

?>