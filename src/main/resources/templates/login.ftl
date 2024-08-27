<html lang="de">

<head>
    <style>

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

        form {
            background-color: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .logo-container {
            text-align: center;
            margin-bottom: 20px;
        }

        .logo-container img {
            width: 235px; /* 50% from the originalsize */
            height: auto; /* The height is proportional to the width */
        }

        h2 {
            margin: 10px 0;
            font-size: 24px;
            color: #333;
        }

        p {
            margin: 10px 0;
            display: flex;
            flex-direction: column;
        }

        label {
            margin-bottom: 5px;
            font-weight: bold;
        }

        input[type="text"],
        input[type="password"] {
            padding: 10px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 5px;
            width: 250px;
        }

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

        input[type="submit"]:hover {
            background-color: #4CAF50; /* Old: #0056b3 */
        }

        .status-blue {
            color: blue;
        }

        .status-red {
            color: red;
        }
    </style>
    <title></title>
</head>

<body>
<form action="${login.postUrl}" enctype="application/x-www-form-urlencoded" method="post">
    <div class="logo-container">
        <img src="/images/HeimAG_Logo.jpg" alt="HeimAG Logo">
    </div>
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
    <br>
    <p>
        Status:
        <#if login.databaseStatus == "Datenbank vorhanden">
            <span class="status-blue">${login.databaseStatus}</span>
        <#else>
            <span class="status-red">${login.databaseStatus}</span>
        </#if>
    </p>
</form>
</body>
</html>
