<?php 
	include_once('common.inc.php');
	$fileName = $fileDir . $_GET['file'] . ".xml";
	unlink($fileName);
	header('Location: index.php?page=my_notes');
?>
