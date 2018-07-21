<!doctype html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <title>Beer Notes</title>
  <link rel="stylesheet" href="css/main.css">
</head>

<body>
	<div id="home">
		<h1>Welcome to Beer Notes!</h1>

		<div id="news">
			<h2>News</h2>
			<p>
				Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec sit amet mauris et ligula euismod egestas. Duis condimentum gravida enim semper rutrum. Fusce rhoncus magna vitae sollicitudin accumsan. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Vestibulum semper sollicitudin metus sit amet porttitor. Duis efficitur, mauris id ultricies faucibus, odio augue condimentum tortor, non luctus neque tellus non tellus. Integer ut augue sed sapien imperdiet rutrum. Nullam id lorem lacus. Duis blandit quam eget lectus molestie tempor. Aliquam dictum elit at massa scelerisque accumsan. Mauris lorem risus, iaculis et accumsan et, dignissim et felis. Fusce in urna lacinia, convallis nisl ut, vehicula quam. Aenean dignissim velit cursus gravida semper. Nam blandit, felis nec gravida imperdiet, nisl justo pulvinar risus, vel aliquet felis turpis id mauris. Etiam in dictum dui, quis consequat magna. Pellentesque rhoncus, urna sit amet mattis fringilla, purus purus venenatis turpis, vitae ultrices turpis justo ac nisl. Cras a purus non tellus aliquet tempus dictum id dui. Praesent eu lorem viverra est porttitor ultricies. Quisque vel ornare tellus, vel lacinia sapien. Pellentesque gravida porttitor lorem, ac faucibus diam vestibulum nec. Fusce sodales cursus tristique. Nam in viverra enim. Sed ligula augue, sagittis quis mattis id, consequat sit amet orci. Mauris sit amet ante eu lacus aliquet tincidunt in eu odio. Aliquam eu mauris turpis. Sed sed dictum metus, non condimentum augue. Nullam luctus nisi at enim viverra volutpat. Nunc tincidunt tincidunt nibh vitae tincidunt. Fusce volutpat aliquet nunc, at porttitor dui semper vel. Nulla tempus suscipit sem at viverra. Aenean vulputate, dolor ultrices commodo bibendum, velit nisl faucibus magna, nec tempus elit dui at tellus. Sed pulvinar massa finibus tincidunt pretium. Nullam et nibh et diam posuere consectetur. Nunc at egestas risus, vel lacinia dui. Nunc suscipit scelerisque diam, ac convallis sem tincidunt vitae. Cras eu massa magna. Fusce ornare erat eu ligula aliquet, ut viverra massa consectetur. Quisque sollicitudin cursus ex ac luctus. In aliquet tortor et orci aliquam volutpat. Vivamus augue nunc, posuere id velit ut, laoreet porta arcu. Nam eleifend, nibh nec accumsan imperdiet, ipsum eros scelerisque orci, non ullamcorper libero est at risus. Sed augue mi, egestas vitae tellus quis, ullamcorper accumsan eros. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Morbi faucibus massa vel orci sodales, vitae pellentesque risus elementum. Phasellus at condimentum odio. Aenean vehicula, nisi in scelerisque condimentum, dolor lectus fermentum mauris, sit amet ullamcorper orci turpis ac elit. Mauris non mauris sed nibh consequat faucibus. Praesent aliquam consequat nisl tincidunt auctor. Vivamus ut velit vitae nunc egestas sodales. Sed pretium feugiat scelerisque. Aliquam vel enim mi.
			</p>
		
		</div>
		
		<div id="recently_added">
			<h2>Recently Added</h2>
			<?php
				include_once ('common.inc.php');
				$fileDir = $_SERVER['DOCUMENT_ROOT'] . '/syrj0001/xml/';
				$handle = opendir($fileDir);
				$items = array();
				while (($file = readdir($handle)) !== FALSE)
				{
					if (is_dir($fileDir . $file)) continue;
					if (!preg_match("/^(Beer Note).*\.xml$/", $file)) continue;

					$xmlItem = simplexml_load_file($fileDir . $file);
					$item = array();
					
					 if ( (string)$xmlItem->status == 'live' and (string)$xmlItem->private != 'yes')
						$items[] = getBeer($xmlItem);
				}
				
				$counter = 0;
				

				echo '<table class="beer_search">';

				while ($counter < count($items) and $counter < 3)
				{	
					echo '<tr><td class="beer_search_picture"><a href="index.php?id=' . $items[$counter]['id'] . '"><img src="' . htmlentities($items[$counter]['image']) . '"alt="No Photo"/></a></td>';
					echo '<td><table><tr><td>';
					echo printRating($items[$counter]['rating']);
					echo '</td>';
					echo '</tr>';
					echo '<tr><td class="beer_name"><a href="index.php?id=' . $items[$counter]['id'] . '">' . htmlentities($items[$counter]['name']) . '</a></td></tr>';
					echo '<tr><td>' . htmlentities($items[$counter]['brewery']) . '</td></tr>';
					echo '<tr><td>By: ' . htmlentities($items[$counter]['user']) . '</td></tr></table></td></tr>';		
					$counter++;
				}
				echo '</table>';
			?>
		</div>
	</div>

</body>
</html>
