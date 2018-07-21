<!doctype html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <title>Beer Notes</title>
  <link rel="stylesheet" href="css/main.css">
</head>

<body>
	<div id="banner_top">
		<a href="index.php"> <img src="images/logo.png" alt="Beer Notes"/></a>
		
		<?php
		if (session_status() == PHP_SESSION_NONE) // if session not started, starting it
			session_start();
		
		if ( !isset($_SESSION['log']) ) // if not logged in, showing the login form
			printForm();
		else if($_SESSION['log'] == 1) // else, showing the user name and sign out link
			printLogin();
		else							// else, checking the password
		{
			if(isset($_POST['login']))
			{
				$login = $_POST['login'];
				$passwd = $_POST['passwd'];
				if (checkPasswd($login, $passwd))
				{
					$_SESSION['log'] = 1; //If the username and password are correct, setting the session flag to true
					$_SESSION['user'] = $login; // storing the user name in session
					printLogin(); // showing the user name and sig out link
					header('Location: index.php?page=user_info'); // redirecting user to their page
					
				}
				else // if login not successful (wrong password or username), showin the login form
				{
					printForm();
					echo '<script type="text/Javascript">document.getElementById(\'log\').style.backgroundColor=\'#FF3030\';</script>';
					echo '<script type="text/Javascript">document.getElementById(\'pw\').style.backgroundColor=\'#FF3030\';</script>';
					echo '<script type="text/Javascript">document.getElementById(\'login_label\').innerHTML = "Wrong User Name or Password";</script>';
					echo '<script type="text/Javascript">document.getElementById(\'login_label\').style.color = "#CF0000";</script>';
					//echo $_GET['page'];
				}
			}
			else
				printForm();
		}



		  
		?>		
	</div>

	<ul id="nav_top">
		<li><a href="index.php">Home</a></li>
		<li><a href="index.php?page=about_beer">About Beer</a>
			<ul class="dropdown">
				<li><a href="index.php?page=beer_types">Beer Types</a></li>
				<li><a href="index.php?page=equipment">Equipment</a></li>
				<li><a href="index.php?page=glassware">Glassware</a></li>
				<li><a href="index.php?page=useful_links">Useful Links</a></li>
			</ul>
		</li>
		<li>
			<a href="index.php?page=beer_notes">Beer Notes</a>
		</li>
		<li><a href="index.php?page=feedback">Feedback</a></li>
		<li><a href="index.php?page=sitemap">Site Map</a></li>
		
		<?php
			if($_SESSION['log'] == 1)
			{

echo <<<END
				<li id="user_info"><a href="index.php?page=user_info">My Page</a>
					<ul class="dropdown">
						<li><a href="index.php?page=user_info">User Info</a></li>
						<li><a href="index.php?page=my_notes">My Notes</a></li>
				
					</ul>
				</li>
END;

			}
		?>
		<li class="search"><?php include("search.inc.php"); ?></li>
	</ul>

	<div id="sidebar_left">
		<?php
			if(isset($_GET['page']))
			{
				if($_GET['page'] == "user_info" || $_GET['page'] == "my_notes")
				{
					echo "<h2>My Page</h2>";
					echo "<a href=\"index.php?page=user_info\">User Info</a><br>";
					echo "<a href=\"index.php?page=my_notes\">My Notes</a>";
				}
				else if($_GET['page'] == "about_beer" || $_GET['page'] == "beer_types" || $_GET['page'] == "glassware" || $_GET['page'] == "equipment" || $_GET['page'] == "useful_links")
				{
					echo "<h2>About Beer</h2>";
					echo "<a href=\"index.php?page=beer_types\">Beer Types</a><br>";
					echo "<a href=\"index.php?page=equipment\">Equipment</a><br>";
					echo "<a href=\"index.php?page=glassware\">Glassware</a><br>";
					echo "<a href=\"index.php?page=useful_links\">Useful Links</a>";
				}
			}
		?>
	</div>

	<div id="sidebar_right">
		<a href="index.php"> <img src="images/ad_placeholder.png" alt="Advertisement"></a>
		<a href="index.php"> <img src="images/ad_placeholder.png" alt="Advertisement"></a>
	</div>
		
	<div id="content_main">
	<?php
		if(isset($_GET['page']))
		{
			if($_GET['page'] == "feedback")
				include("feedback.html");
			else if($_GET['page'] == "sitemap")
				include("sitemap.php");
			else if($_GET['page'] == "search")
				include("doSearch.php");
			else if($_GET['page'] == "user_info")
				include("user_info.html");
			else if($_GET['page'] == "my_notes")
				include("my_notes.php");
			else if($_GET['page'] == "update_user_info")
				include("update_user_info.php");
			else if($_GET['page'] == "beer_notes")
				include("beer_notes.php");
			else if($_GET['page'] == "add_new_note")
				include("add_new_note.php");
			else if($_GET['page'] == "about_beer")
				include("about_beer.html");
			else if($_GET['page'] == "glassware")
				include("glassware.html");
			else if($_GET['page'] == "equipment")
				include("equipment.html");
			else if($_GET['page'] == "beer_types")
				include("beer_types.html");
			else if($_GET['page'] == "feedback_received")
				include("feedback_received.html");
			else if($_GET['page'] == "useful_links")
				include("useful_links.html");
		}
		else if( isset($_GET['id']) )
			include("beer_note_content.php");
		else
			include("home.php");
		// functions
		function printForm()
		{
			require("definitions.php");
			$_SESSION['log'] = 0;
			
echo <<<END
		 <form id="login" name = "login" action="$self" method = "post">
			  <table class="back">
				<tr><td id="login_label">Sign In:</td>
					<td><input type = "text" name = "login" id="log" placeholder="User"> </td>
				</tr>
				<tr>
				  <td></td><td><input type = "password" name = "passwd" id="pw" placeholder="Password"> </td>
				</tr>
				<tr>
				  <td></td><td><input type = "submit" value = "Sign In"> <a href="#">Sign Up</a></td>
				</tr>
			  </table>
			</form>
END;
		}
		
		
		function printLogin()
		{		
			echo '<div id="login_form"><p><a href="index.php?page=user_info">' . $_SESSION['user'] . '</a><br><a href="logout.php">Sign Out</a></p></div>';
		}
		
		function checkPasswd($login, $passwd)
		{
			include_once('common.inc.php');
			$handle = opendir($fileDir);
					
			while ( ($file = readdir($handle)) !== FALSE)
			{
		
				if (is_dir($fileDir . $file)) continue;
				if (!preg_match("/^(user).*\.xml$/", $file)) continue;
				$xmlItem = simplexml_load_file($fileDir . $file);
			
				if( (string)$xmlItem->userid == $login && (string)$xmlItem->password == md5($passwd) )
					return true;
			}
			return false;
		}		
	?>
	</div>

	<footer>
			Copyright	
	</footer>

</body>
</html>
