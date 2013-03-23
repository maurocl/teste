<?php

/*
 * teste.php
 *  
 * Exibe o funcionamento de um array.
 * 
 * @var Array $a Array de Strings
 */
$a = array("a", "bb", "ccc");

echo "<pre>";

echo "Nº de elementos do array (\$a): " . count($a) . "\n";

$i=0;

foreach ($a as $value) {
	echo "<br>[$i]: $value\n";
	$i++;
}

echo "<br>\n";
echo "print_r(\$a)<br>\n";
print_r($a);

echo "<br>\n";
echo "var_dump(\$a)<br>\n";
var_dump($a);

echo "</pre>"

?>