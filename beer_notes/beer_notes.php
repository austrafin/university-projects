<!doctype html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <title>Beer Notes</title>
  <link rel="stylesheet" href="css/main.css">
</head>

<body>
	<div>
		<a href="index.php">Home</a> > Beer Notes
		<?php
		$_GET['search_type'] = "beer";
		if( !isset($_POST['term'])) 
			$_GET['term'] = "*";	
		$_SESSION['my_page_flag'] = "false";	
		include_once("doSearch.php");
		?>
		
	</div>

</body>
</html>
