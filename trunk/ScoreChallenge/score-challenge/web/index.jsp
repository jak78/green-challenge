<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Score Challenge</title></head>
<body>
<img src="customLogo.gif" alt="Green Challenge Logo" title="Green Challenge for USI 2010"/>
<h1>Welcome to Score Challenge</h1>
<%
    UserService userService = UserServiceFactory.getUserService();
    if (request.getUserPrincipal() == null) { %>
<p><a href="<%=userService.createLoginURL(request.getRequestURI())%>">Please login using your Google Account</a></p>
<% } else { %>
<p><a href="submit.jsp">Manually submit a sample</a> </p>
<p>Logged as <%=userService.getCurrentUser().getEmail()%></p>
<p><a href="/api/dump/cpu">Dump all recorded samples</a></p>
<p><a href="<%=userService.createLogoutURL(request.getRequestURI())%>">Logout</a></p>
<% } %>
</body>
</html>