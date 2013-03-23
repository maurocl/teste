<?php

/**
 * isValidSession()
 * 
 * Enter description here ...
 */
function isValidSession() {

	if($_SESSION['authorizedUser']==1) {

		return TRUE;

	}
	else {

		return FALSE;

	}

}

?>

