<?php
//$texto = "<img scr=http://www.niederauer.com.br/figuras/interbase.jpg>";

$texto = "<b>mauro &  \" \' < >";

$novo_texto = htmlspecialchars($texto);

echo $texto . "<br>";
echo $novo_texto;
?>
