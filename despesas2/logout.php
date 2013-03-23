<?php
// start a session
session_start();


// register a session variable
$_SESSION['authorizedUser']=null;

// redirect browser to protected resource
header("Location: login.php");

?>