<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    UserService userService = UserServiceFactory.getUserService();
    String curUser = userService.getCurrentUser().getEmail();
%>
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
            <input type="text" name="challengerID" id="challengerID" value="<%= curUser %>"/>
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
<p><a href="index.jsp">Home</a></p>
</body>
</html>