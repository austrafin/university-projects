<?php
if (session_status() == PHP_SESSION_NONE)
    session_start();
$fileDir = $_SERVER['DOCUMENT_ROOT'] . '/syrj0001/xml/';

function printRating($rating)
{
	for($i = 1; $i <= 5; $i++)
	{
		if($i <= $rating)
			echo '<img class="rating" src="images/star_red.gif" alt="No Photo"/>';
		else
			echo '<img class="rating" src="images/star_grey.gif"alt="No Photo"/>';
	}
}

function getBeer($xmlItem)
{
	$item['id'] = (string)$xmlItem['id'];
	$item['rating'] = (int)$xmlItem->note_content->rating;
	$item['name'] = (string)$xmlItem->note_content->name;
	$item['brewery'] = (string)$xmlItem->note_content->brewery;
	$item['user'] = (string)$xmlItem->note_content->userid;
	
	if((string)$xmlItem->note_content->image->source != "")
		$item['image'] = (string)$xmlItem->note_content->image->source;
	else
		$item['image'] = "images/no_photo.png";
		$item['url'] = "index.php?id=" . $item['id'];
	
	return $item;
}


?>
