<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Test de la collecte</title></head>
<body>

<form action="api/collect/cpu" method="POST">
    <fieldset>
        <legend>Test de la collecte des mesures pour le Green Challenge.</legend>
        <p>Permet de saisir une collecte comme le ferait l'add-on ou l'appli GAE, mais manuellement, à des fins de
            tests.</p>

        <p>
            <label for="challengerID">challengerID</label>
            <input type="text" name="challengerID" id="challengerID"/>
        </p>

        <p>
            <label for="CPUCycles">CPUCycles</label>
            <input type="text" name="CPUCycles" id="CPUCycles"/>
        </p>

        <p><label for="source">source</label>
            <select id="source" name="source">
                <option value="GREEN_FOX" selected="selected">AddOn GreenFox</option>
                <option value="SERVER_APP">Appli Google App Engine</option>
            </select></p>

        <input type="submit" value="Soumettre">
    </fieldset>
</form>
<%
    UserService userService = UserServiceFactory.getUserService();
%>
<p><a href="/api/dump/cpu">Dump all recorded samples (must be admin)</a></p>
<% if (request.getUserPrincipal() == null) { %>
<p><a href="<%=userService.createLoginURL(request.getRequestURI())%>">Login</a></p>
<% } else { %>
<p>Logged as <%=userService.getCurrentUser().getEmail()%></p>
<p><a href="<%=userService.createLogoutURL(request.getRequestURI())%>">Logout</a></p>
<% } %>
</body>
</html>