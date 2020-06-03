<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>Web Checkers | ${title}</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
    <div class = "page">
        <h1>Web Checkers | ${title}</h1>
        
        <div class = "body">
            <#include "message.ftl">

            <form action="./signin" method="POST">
                <label for="username"><b>Username</b></label>
                <input type="text" placeholder="Enter Username" name="username" required>
                <button type = "submit">Submit</button>
                <param name="test", value="value">
            </form>
        </div>
    </div>

</body>
</html>
