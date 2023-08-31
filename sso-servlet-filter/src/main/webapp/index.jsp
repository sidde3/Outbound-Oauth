<%@page contentType="text/html" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <title>Sample Servlet</title>
    <link rel="stylesheet" type="text/css" href="styles.css"/>
</head>
<body>
<jsp:useBean id="controller" class="org.demo.sso.Controller" scope="request"/>
<% controller.handleLogout(request); %>

        <c:set var="isLoggedIn" value="<%=controller.isLoggedIn(request)%>"/>

        <c:if test="${isLoggedIn}">
            <c:set var="accountUri" value="<%=controller.getAccountUri(request)%>"/>
            <c:set var="principalName" value="<%=controller.getPrincipalName(request)%>"/>
            <c:set var="idToken" value="<%=controller.getIDToken(request)%>"/>
            <c:set var="tokenString" value="<%=controller.getTokenString(request)%>"/>
            <c:set var="showToken" value="<%=controller.showToken(request)%>"/>
            <c:set var="invokeRest" value="<%=controller.invokeRest(request)%>"/>
        </c:if>

        <div class="wrapper">
            <c:if test="${isLoggedIn}">
                <p style="color:white;">Welcome, <var>${principalName}</var> </p>
                <div id="authenticated" style="display: block" class="menu">
                    <button name="profileBtn" onclick="location.href = 'index.jsp'">Profile</button>
                    <button name="tokenBtn" onclick="location.href = 'index.jsp?showToken=true'">Token</button>
                    <button name="invokeBtn" onclick="location.href = 'index.jsp?invokeRest=true'">Spring-Boot</button>
                    <button name="accountBtn" onclick="location.href = '${accountUri}'" type="button">Account</button>
                    <button name="logoutBtn" onclick="location.href = 'index.jsp?action=logout'">Logout</button>
                </div>
            </c:if>

                <c:if test="${showToken}">
                    <div class="content">
                        <div id="token-content" class="message">${tokenString}</div>
                    </div>
                </c:if>

                <c:if test="${invokeRest}">
                    <c:set var="showToken" value="true"/>
                    <c:set var="getInfo" value="<%=controller.getInfo(request)%>"/>
                    <div class="content">
                        <div id="token-content" class="message">${getInfo}</div>
                    </div>
                </c:if>

                <c:if test="${!showToken}">
                    <div class="content">
                        <div id="profile-content" class="message">
                            <table cellpadding="0" cellspacing="0">
                                <tr>
                                    <td class="label">First name</td>
                                    <td><span id="firstName">${idToken.givenName}</span></td>
                                </tr>
                                <tr class="even">
                                    <td class="label">Last name</td>
                                    <td><span id="lastName">${idToken.familyName}</span></td>
                                </tr>
                                <tr>
                                    <td class="label">Username</td>
                                    <td><span id="username">${idToken.preferredUsername}</span></td>
                                </tr>
                                <tr class="even">
                                    <td class="label">Email</td>
                                    <td><span id="email">${idToken.email}</span></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </c:if>
        </div>
</body>
</html>