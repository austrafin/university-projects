<?php
include_once 'common.inc.php';
if(isset($_GET['term']))
	$term = $_GET['term'];
else
	$term = $_POST['term'];
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Search Results</title>
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" href="xmlcms.css" type="text/css" />
</head>
<body>

<div id="mainContent">
	<?php
	if (session_status() == PHP_SESSION_NONE)
		session_start();
	$handle = opendir($fileDir);
	$items = array();
	$contentType = "Beer Note|pages";
	$searchType = "";
	if(!isset($_SESSION['user']))
		$_SESSION['user'] = "";
	
	while (($file = readdir($handle)) !== FALSE)
	{
		if (is_dir($fileDir . $file)) continue;
		if(isset($_GET['search_type']))
		{
			if($_GET['search_type'] == "beer")
			{
				$searchType = "beer";
				$contentType = "Beer Note";
			}
		}
		
		if (!preg_match("/^($contentType).*\.xml$/", $file)) continue;

		$xmlItem = simplexml_load_file($fileDir . $file);
		$item = array();
		
		// Searching for pages
		if($file === "pages.xml")
		{
			foreach($xmlItem->page as $page)
			{
				if ( ((stripos($page->name, $term) !== FALSE || stripos($page->name, $term) !== FALSE)
					and (string)$page->status == 'live') || $term == "*")
				{	
					$item = array();
					$item['id'] = "Page";
					$item['name'] = (string)$page->name;
					$item['url'] = (string)$page->url;
					$items[] = $item;
				}
				foreach($page as $subPage)
				{
					if ( ((stripos($subPage->name, $term) !== FALSE || stripos($subPage->name, $term) !== FALSE)
					and (string)$subPage->status == 'live') || $term == "*")
				{	
					$item = array();
					$item['id'] = "Page";
					$item['name'] = (string)$subPage->name;
					$item['url'] = (string)$subPage->url;
					$items[] = $item;
				}
				}
			}
		}
		if( $_SESSION['my_page_flag'] == "true" )
		{
			if( stripos($xmlItem->note_content->userid, $term) !== FALSE || stripos($xmlItem->note_content->name, $term) !== FALSE || stripos($xmlItem->note_content->name, $term) !== FALSE || $term == "*" and stripos($xmlItem->note_content->userid, $_SESSION['user']) !== FALSE and (string)$xmlItem->status == 'live')
				$items[] = getBeer($xmlItem);
		}
		else if ( ( (stripos($xmlItem->note_content->name, $term) !== FALSE || stripos($xmlItem->note_content->name, $term) !== FALSE || stripos($xmlItem->note_content->userid, $term) !== FALSE) || $term == "*") and (string)$xmlItem->status == 'live' )
		{
			if((string)$xmlItem->private != 'yes' || (string)$xmlItem->note_content->userid == $_SESSION['user'])
				$items[] = getBeer($xmlItem);
		}
		
	}

	
	if (count($items) > 0)
	{
		if($term != "*" && $_GET['page'] == "my_notes")
			echo '<h1 class="beer_search">My Notes</h1><a href="index.php?page=add_new_note"><img id="button_add_new" src="images/button_add_new.png"alt="No Photo"/></a>';
		else if( $_GET['page'] == "beer_notes" )
			echo '<h1>Beer Notes</h1>';
		else
			echo '<h1>Search Results for ' . htmlentities($term) . '</h1>';
		
		
		if($searchType == "beer")
		{
			include("search.inc.php"); // If the user is on beer notes page, show search bar
			
			echo '<table class="beer_search">';
			//echo '<table class="beer_search">';
			foreach ($items as $item)
			{	
				echo '<tr><td class="beer_search_picture"><a href="index.php?id=' . $item['id'] . '"><img src="' . htmlentities($item['image']) . '"alt="No Photo"/></a></td>';
				echo '<td><table><tr><td>';
				echo printRating($item['rating']);
				echo '</td>';
				echo '</tr>';
				echo '<tr><td class="beer_name"><a href="index.php?id=' . $item['id'] . '">' . htmlentities($item['name']) . '</a></td></tr>';
				echo '<tr><td>' . htmlentities($item['brewery']) . '</td></tr>';
				echo '<tr><td>By: ' . htmlentities($item['user']) . '</td></tr>';
				
				echo '</table></td>';
				
				if( $_SESSION['my_page_flag'] == "true" )
					echo '<td><table><tr><td><a href="delete_xml.php?file=' . $item['id'] . '"><img class="delete_cross" src="images/cross.png"alt="No Photo"/></a></td></tr></table></td>';
				
				echo'</tr>';
			}
			echo '</table>';
		}
		else
		{			
			echo '<table border="1" cellspacing="0" cellpadding="3" width="85%">';
			echo '<tr valign="top"><th>Content Item</th><th>Content Type</th></tr>';
			foreach ($items as $item)
			{
				echo '<tr valign="top"><td><a href="' . $item['url'] . '">';
				echo htmlentities($item['name']) . '</a></td>';
				echo '<td>';
				echo preg_replace("/[0-9]/", '', $item['id']);
				echo '</td></tr>';
			}
			echo '</table>';
		}
	}
	
	else {
		echo '<h1>Sorry!</h1>';
		echo '<p>No files found with the search term ' . htmlentities($term) . '</p>';
	}
	?>
</div>
</body>
</html>
