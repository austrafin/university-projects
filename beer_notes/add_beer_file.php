<?php
	if (session_status() == PHP_SESSION_NONE)
		session_start();
	
	$target_dir = "images/";
	$target_file = $target_dir . basename($_FILES['fileToUpload']['name']);
	
	
	if(move_uploaded_file($_FILES['fileToUpload']['tmp_name'], $target_file))
	{
		echo "The file ".  basename( $_FILES['fileToUpload']['name']). " has been uploaded";
	}
	else
	{
		echo "There was an error uploading the file, please try again!";
	}
	
	$xml = new SimpleXMLElement("<?xml version=\"1.0\" encoding=\"utf-8\" ?><beer_note></beer_note>");
	$id = "Beer Note" . date("Ymdhis");
	$xml->addAttribute("id", $id);
	$note_content = $xml->addChild('note_content');
	$image_source = "images/" . $_FILES['fileToUpload']['name'];
	$image = $note_content->addChild('image');
	$image->addChild('source', $image_source);
	$note_content->addChild('name', $_POST['beer_name']);
	$note_content->addChild('brewery', $_POST['brewery_name']);
	$note_content->addChild('glass', $_POST['glass']);
	$note_content->addChild('pubdate', date("d F Y"));
	$note_content->addChild('userid', $_SESSION['user']);
	$note_content->addChild('look', $_POST['look']);
	$note_content->addChild('nose', $_POST['nose']);
	$note_content->addChild('taste', $_POST['taste']);
	$note_content->addChild('rating', $_POST['rating']);
	$note_content->addChild('description', $_POST['description']);
/*	
	$comments = $note_content->addChild('comments');
	$comment = $comments->addChild('comment');
	$comment->addChild('userid');
	$comment->addChild('pubdate');
	$comment->addChild('content');
*/	
	$xml->addChild('keywords');
	$xml->addChild('private', $_POST['private']);
	$xml->addChild('status', 'live');
	
	$fileName = "xml/" . $id . ".xml";
	$xml->saveXML($fileName);
	
	header('Location: index.php?page=my_notes');
	
?>

