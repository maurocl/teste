<?php
// start a session
session_start();

// Elimina as vari�veis de sess�o
$_SESSION = array();

// Elimina todos os dados de uma sess�o
session_destroy();

// redirect browser to protected resource
header("Location: login.php");

?>