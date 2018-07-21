<?php 
if(isset($_GET['search_type']))
{
	if($_GET['search_type'] == "beer")
	{
echo <<<END
		<form class="beer_search" method = "post" action="index.php?page=beer_notes" >
			Search Beers:
			<input name="term" type="text" />
			<input type="submit" value="Search" />
	
			
		</form>
END;
	}
}

else
	{
echo <<<END
		<form method="post" action="index.php?page=search">
			Search Site:
			<input name="term" type="text" id="term" />
			<input name="search" type="submit" id="search" value="Search" />
		</form>
END;
	}
?>

