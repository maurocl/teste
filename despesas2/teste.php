<?php

$a = array("a", "bb", "ccc");

echo "Nº de elementos do array: " . count($a) . "\n";

$i=0;
foreach ($a as $value) {
	echo "<br>$i: $value\n";
	$i++;
}

echo "<br>\n";
print_r($a);

echo "<br>\n";
var_dump($a);

?>