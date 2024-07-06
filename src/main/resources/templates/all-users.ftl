<!DOCTYPE html>
<html>

<head>
    <title>All Users</title>
</head>

<body>
    <h1>All Users</h1>
    <ul>
        <#list allUsers as user>
            <li>
                ${user.id} - ${user.name}
            </li>
        </#list>
    </ul>
</body>

</html>