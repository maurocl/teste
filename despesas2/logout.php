<?php
// start a session
session_start();

// Elimina as variáveis de sessão
$_SESSION = array();

// Elimina todos os dados de uma sessão
session_destroy();

// redirect browser to protected resource
header("Location: login.php");

?>