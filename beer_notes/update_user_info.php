<!doctype html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <title>Beer Notes</title>
  <link rel="stylesheet" href="css/main.css">
</head>

<body>
	<div class="user_info_form">
	<a href="index.php">Home</a> > <a href="index.php?page=user_info">My Page</a> > <a href="index.php?page=user_info">User Info</a> > Update User Information
		<form>
			<table>
				<tr><td colspan="3"><p>First Name:</p></td></tr>
				<tr><td colspan="3"><input type = "text" name = "first_name"></td></tr>
				<tr><td colspan="3"><p>Last Name:</p></td></tr>
				<tr><td colspan="3"><input type = "text" name = "last_name"></td></tr>
				<tr><td colspan="3"><p>Date of Birth:</p></td></tr>
				<tr>
					<td>
						<select>
							<option value="">Date</option>							
							<?php
								for($i = 1; $i <= 31; $i++)
									echo"<option value=\"$i\">$i</option>";
							?>
						</select>	
					</td>
					
					<td>
						<select>
							<option value="">Month</option>
							<option value="01">January</option>
							<option value="02">February</option>
							<option value="03">March</option>
							<option value="04">April</option>
							<option value="05">May</option>
							<option value="06">June</option>
							<option value="07">July</option>
							<option value="08">August</option>
							<option value="09">September</option>
							<option value="10">October</option>
							<option value="11">November</option>
							<option value="12">December</option>
						</select>
					</td>
					
					<td>
						<select>
							<option value="">Year</option>
							<?php
								for($i = date("Y"); $i >= 1900; $i--)
									echo"<option value=\"$i\">$i</option>";
							?>
						</select>
					</td>
				</tr>
					<tr><td colspan="3"><p>City:</p></td></tr>
					<tr><td colspan="3"><input type = "text" name = "city"></td></tr>
					<tr><td colspan="3"><p>Country:</p></td></tr>
					<tr><td colspan="3"><input type = "text" name = "country"></td></tr>
					<tr><td colspan="3"><p>Email:</p></td></tr>
					<tr><td colspan="3"><input type = "text" name = "email"></td></tr>
					<tr><td colspan="3"><p>Password:</p></td></tr>
					<tr><td colspan="3"><input type = "password" name = "password"></td></tr>
					<tr><td colspan="3"><p>Confirm Password:</p></td></tr>
					<tr><td colspan="3"><input type = "password" name = "password_confirm"></td></tr>
			</table>
			
			<input type = "submit" value = "Update">
		</form>
		<form action="index.php?page=user_info" method="post"><input type="submit" value="Back"></form>
	</div>
	
</body>
</html>
