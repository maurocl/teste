<?php

######################
# Limpa a autorização
#
######################

// start a session
session_start();

// register a session variable
$_SESSION['authorizedUser']=null;

// redirect browser to protected resource
header("Location: success.php");

?>