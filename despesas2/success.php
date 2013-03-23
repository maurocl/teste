<?php
// start session
session_start();

// check session for flag

if($_SESSION['authorizedUser']!=1) {

	// if flag is absent
	// the user does not have view privileges
	// print error message
	echo "You are not authorized to view this pages.";

	// terminate processing
	// kick the client out
	exit();

}
else {

	echo "Welcome!";

}

echo "<h1>Success</h1>";

?>