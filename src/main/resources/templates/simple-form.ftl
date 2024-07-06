<!DOCTYPE html>
<html>

<head>
    <title>Simple HTML Form</title>
</head>

<body>
    <h1>Simple HTML Form</h1>
    <form action="/simple-form-action" method="post">
        <label for="fname">First name:</label><br>
        <input type="text" id="fname" name="fname" value="John"><br>
        <label for="lname">Last name:</label><br>
        <input type="text" id="lname" name="lname" value="Doe"><br><br>
        <input type="submit" value="Submit">
    </form>
    <p>If you click the "Submit" button, the form-data will be sent to a page called "/action".</p>
</body>

</html>