<!DOCTYPE html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="10">
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl">

  <div class="body">

    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl">

    <#if currentUser??>
    <h2>Players Online</h2>
    <#if numberusers == 1>
        <li>
        There are no other players available to play at this time.
        </li>
    <#else>
        <form action="./" method="POST" name="postHome">
        <#list foes as x>
        <#if x.name != currentUser.name>
            <li>
            <a href="#" onclick="postHome.submit();">${x.name}<br></button>
            <input type="hidden" name="opponent" value=${x.name}>
            </li>
        </#if>
        </#list>
        </form>
    </#if>





    <#else>
    <h2>Currently Online: ${numberusers}</h2>

  </#if>



    <!-- TODO: future content on the Home:
            to start games,
            spectating active games,
            or replay archived games
    -->

  </div>

</div>
</body>

</html>
