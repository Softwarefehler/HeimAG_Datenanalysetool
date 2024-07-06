<!DOCTYPE html>
<html>

<head>
    <title>Multipart HTML Form Data</title>
</head>

<body>
    <h1>Multipart HTML Form Data</h1>
    <form action="/multipart-form-data-action" method="post">
        <label for="fname">First name:</label>
        <input type="text" id="fname" name="fname"><br><br>
        <label for="myfile">Select a file:</label>
        <input type="file" id="myfile" name="myfile">
        
        <input type="submit" formenctype="multipart/form-data" value="Submit as Multipart/form-data">
    </form>
</body>

</html>