<html lang="de">
<head>
    <style>
        /* Grundstil für das gesamte Dokument */
        body {
            margin: 0;
            padding: 0;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            font-family: Arial, sans-serif;
            background-color: #f0f0f0;
        }

        /* Stil für das Formular */
        form {
            background-color: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        /* Stil für das Bild - Verkleinerung */
        img {
            width: 118px; /* 50% der Originalbreite */
            height: auto; /* Höhe proportional zur Breite */
            margin-bottom: 20px;
        }

        /* Stil für den Übertitel */
        h2 {
            margin: 10px 0;
            font-size: 24px;
            color: #333;
        }

        /* Stil für die Absätze (Labels und Inputs) */
        p {
            margin: 10px 0;
            display: flex;
            flex-direction: column;
        }

        /* Stil für die Labels */
        label {
            margin-bottom: 5px;
            font-weight: bold;
        }

        /* Stil für die Eingabefelder */
        input[type="text"],
        input[type="password"] {
            padding: 10px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 5px;
            width: 250px;
        }

        /* Stil für den Submit-Button */
        input[type="submit"] {
            padding: 10px;
            font-size: 16px;
            border: none;
            border-radius: 5px;
            background-color: #007bff;
            color: white;
            cursor: pointer;
            width: 100%;
            margin-top: 10px;
        }

        /* Hover-Effekt für den Button */
        input[type="submit"]:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<form action="${login.postUrl}" enctype="application/x-www-form-urlencoded" method="post">
   <h2>Datenanalysetool</h2>
    <br>
    <p>
        <label for="username">Username:</label>
        <input id="username" type="text" name="${login.userParamName}" />
    </p>
    <p>
        <label for="password">Password:</label>
        <input id="password" type="password" name="${login.passwordParamName}" />
    </p>
    <p>
        <input type="submit" value="Login" />
    </p>
</form>
</body>
</html>
