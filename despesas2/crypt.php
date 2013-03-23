<?php

$password = "mcl16d";

$salt = "2a0c40";

$crypt = crypt($password);
$size = strlen($crypt);

echo "$crypt, $size<br>";

$crypt = crypt($password,$salt);
$size = strlen($crypt);

echo "$crypt, $size<br>";


$hash1 = md5($password);
$size1 = strlen($hash1);

$hash2 = sha1($password);
$size2 = strlen($hash2);

echo "MD5 $hash1, $size1<br>";
echo "SHA1 $hash2, $size2<br>";

?>