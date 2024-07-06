<!DOCTYPE html>
<html>

<head>
    <title>Fotoalbum 📸</title>
    <style>
    body {
        background-color: aliceblue;
    }

    h1 {
        text-transform: uppercase;
        text-align: center;
    }

    table,
    th,
    td {
        border: 1px solid black;
        border-collapse: collapse;
        padding: 15px;
    }

    table {
        margin: auto;
    }

    th {
        font-weight: bold;
        background-color: crimson;
        color: white;
        text-transform: uppercase;
    }

    tr:nth-child(even) {
        background-color: lightgray;
    }

    img {
        width: 300px;
    }
    </style>
</head>

<body>
    <h1>Fotoalbum 📸</h1>
    <table>
        <thead>
            <tr>
                <th>Foto</th>
                <th>Beschreibung</th>
            </tr>
        </thead>
        <tbody>
            <#list model.fotos as foto>
                <tr>
                    <td>
                        <a href="${model.rootUrl}/${foto.fullName}">
                            <img src="${model.rootUrl}/${foto.fullName}" alt="${foto.name}" />
                        </a>
                    </td>
                    <td>
                        ${foto.name}
                    </td>
                </tr>
            </#list>
        </tbody>
    </table>
</body>

</html>