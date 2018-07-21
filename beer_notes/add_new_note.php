<!doctype html>
<html lang="en">

<head>
	<meta charset="utf-8">
	<title>Beer Notes</title>
	<link rel="stylesheet" href="css/main.css">
	<script type="text/Javascript">

	function validateForm (form, event)
	{
		if (isNotEmpty(form.beer_name))
			return true;
		event.preventDefault();
	}

	function isNotEmpty(elem)
	{
	  var str = elem.value;
	  if (str == null || str.length == 0)
	  {
		alert("Name is required.");
		return false;
	  }
	  else
		return true;
	}
	
	function addListeners()
	{
		var txtIn = document.new_note;
		txtIn.addEventListener('submit', function(e){validateForm(txtIn, e)}, false);
    }
	window.addEventListener('load', addListeners, false);
	</script>
</head>

<body>
	<div class="user_info_form">
	<a href="index.php">Home</a> > <a href="index.php?page=user_info">My Page</a> > <a href="index.php?page=my_notes">My Notes</a> > Add New Note
		<form name="new_note" method="post" action="add_beer_file.php" enctype="multipart/form-data">
			<table>
				<tr><td colspan="3"><p>Name:</p></td></tr>
				<tr><td colspan="3"><input type = "text" name = "beer_name"></td></tr>
				<tr><td colspan="3"><p>Brewery:</p></td></tr>
				<tr><td colspan="3"><input type = "text" name = "brewery_name"></td></tr>
				<tr><td colspan="3"><p>Glass:</p></td></tr>
				<tr><td colspan="3"><input type = "text" name = "glass"></td></tr>
				<tr><td colspan="3"><p>Look:</p></td></tr>
				<tr><td colspan="3"><textarea class="beer_note_description" name = "look" placeholder="eg. head, carbonation, colour..."></textarea></td></tr>
				<tr><td colspan="3"><p>Nose:</p></td></tr>
				<tr><td colspan="3"><textarea class="beer_note_description" name = "nose" placeholder="eg. sweetness, citrus..."></textarea></td></tr>
				<tr><td colspan="3"><p>Taste:</p></td></tr>
				<tr><td colspan="3"><textarea class="beer_note_description" name = "taste" placeholder="eg. smokiness, bitterness, fruitiness..."></textarea></td></tr>
				<tr><td colspan="3"><p>Description:</p></td></tr>
				<tr><td colspan="3"><textarea class="beer_note_description" name = "description"></textarea></td></tr>
				<tr><td colspan="3"><p>Final Rating:</p></td></tr>
				<tr>
					<td>
						<select name="rating">
							<option value="5">5</option>
							<option value="4">4</option>
							<option value="3">3</option>
							<option value="2">2</option>
							<option value="1">1</option>
						</select>	
					</td>
				</tr>
				<tr><td><input type="checkbox" name="private" value="yes">Private<br></td></tr>
				<tr><td colspan="3"><p>Image:</p></td></tr>
				<tr><td><input type="file" name="fileToUpload"   /></td></tr>
			</table>
			
			<input type = "submit" value = "Add">
		</form>
		<form action="index.php?page=user_info" method="post"><input type="submit" value="Back"></form>
	</div>
	
</body>
</html>
