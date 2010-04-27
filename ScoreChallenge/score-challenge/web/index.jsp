<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Score Challenge</title>
    <link rel="stylesheet" href="style.css"/>
</head>
<body>
<img src="customLogo.gif" alt="Green Challenge Logo" title="Green Challenge for USI 2010"/>

<h1>Welcome to Score Challenge</h1>
<%
    UserService userService = UserServiceFactory.getUserService();
    if (request.getUserPrincipal() == null) { %>
<p><a href="<%=userService.createLoginURL(request.getRequestURI())%>">Please login using your Google Account</a></p>
<% } else { %>
<p>Logged as <%=userService.getCurrentUser().getEmail()%>
</p>

<h1>My samples</h1>

<div id="samples">
<pre>
<jsp:include page="/api/dump/cpu"/>
</pre>
</div>

<ul>
    <li><a href="index.jsp">Refresh</a></li>
    <li><a href="submit.jsp">Manually submit a sample</a></li>
    <li><a href="<%=userService.createLogoutURL(request.getRequestURI())%>">Logout</a></li>
</ul>
<form action="/api/purge/cpu" method="POST">
    <input type="submit" value="Purge all samples">
</form>
<% } %>
</body>
</html>