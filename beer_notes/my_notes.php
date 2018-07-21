<!doctype html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <title>Beer Notes</title>
  <link rel="stylesheet" href="css/main.css">
</head>

<body>
	<div>
		<a href="index.php">Home</a> > <a href="index.php?page=user_info">My Page</a> > My Notes
		<?php
		if (session_status() == PHP_SESSION_NONE)
			session_start();
		
		$_GET['search_type'] = "beer";
		$_GET['term'] = $_SESSION['user'];
		$_SESSION['my_page_flag'] = "true";
		include_once("doSearch.php");
		?>		
	</div>

</body>
</html>
