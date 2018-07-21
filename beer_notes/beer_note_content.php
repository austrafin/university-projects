<!doctype html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <title>Beer Notes</title>
  <link rel="stylesheet" href="css/main.css">
</head>

<body>
	<div id="beer_note_content">
		<?php
		
		include_once 'common.inc.php';
		$handle = opendir($fileDir);
		$items = array();
		
		while (($file = readdir($handle)) !== FALSE)
		{
			if (is_dir($fileDir . $file)) continue;			
			if ($file == ($_GET['id'] . ".xml"))
			{

			$xmlItem = simplexml_load_file($fileDir . $file);
			if((string)$xmlItem->note_content->image->source != "")
				$image_source = (string)$xmlItem->note_content->image->source;
			else
				$image_source = "images/no_photo.png";
			echo '<a href="index.php">Home</a> > <a href="index.php?page=beer_notes">Beer Notes</a> > ' . (string)$xmlItem->note_content->name;
			echo '<table><tr><td class="beer_note_content_image"><img class="beer_note_content_image" src="' . $image_source . '" alt="images/no_photo.png"></td>';
			echo '<td id="beer_note_content_details"><p>';
			echo printRating( (int)$xmlItem->note_content->rating);
			echo '<br>Name: ' . (string)$xmlItem->note_content->name;
			echo '<br>Brewery: ' . (string)$xmlItem->note_content->brewery;
			echo '<br>Date Added: ' . (string)$xmlItem->note_content->pubdate;
			echo '<br>By: ' . (string)$xmlItem->note_content->userid;
			echo '<br><br>Glass: ' . (string)$xmlItem->note_content->glass;
			echo '<br>Look: ' . (string)$xmlItem->note_content->look;
			echo '<br>Nose: ' . (string)$xmlItem->note_content->nose;
			echo '<br>Taste: ' . (string)$xmlItem->note_content->taste;
			echo '</p></td></tr></table>';
			echo '<p class="beer_note_content_description">' . (string)$xmlItem->note_content->description . '</p>';
			
			echo '<div>';
			if($xmlItem->note_content->comments->comment != NULL)
			{
				foreach($xmlItem->note_content->comments->comment as $comments)
				{
					echo '<p class="beer_note_content_comment_userid">' . $comments->userid . '<span class="beer_note_content_comment_date">' . $comments->pubdate . '</span></p>';
					echo '<p class="beer_note_content_description">' . $comments->content . '</p>';
					
				}
			}
			echo '</div>';
			
			}
					
		}
		?>
		
	</div>

</body>
</html>
