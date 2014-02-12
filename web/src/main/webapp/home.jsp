<%@ page import="com.stormpath.spring.security.provider.StormpathUserDetails" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%--
  ~ Copyright (c) 2014 Stormpath, Inc. and contributors
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~     http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>
<%@ include file="/include.jsp" %>
<%
    //These next constants below would ordinarily be defined in your application's configuration somewhere.  The URLs
    //for your Groups are unique to your Stormpath tenant/application.
%>
<c:set var="GOOD_GUYS" value="https://api.stormpath.com/v1/groups/upXiVIrPQ7yfA5L1G5ZaSQ" scope="application"/>
<c:set var="BAD_GUYS" value="https://api.stormpath.com/v1/groups/01L6Fj7ATwKg8XrcpF1Lww" scope="application"/>
<c:set var="SCHWARTZ_MASTERS" value="https://api.stormpath.com/v1/groups/hyXDGl2oT1GDL8b_B7WG3A" scope="application"/>
<c:set var="subject" value=""/>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="<c:url value="/style.css"/>"/>
    <title>Stormpath + Spring Security Quickstart</title>
</head>
<body>

<h1>Stormpath + Spring Security Quickstart</h1>

<p>Hi <sec:authorize access="!isFullyAuthenticated()">Guest</sec:authorize><sec:authorize access="isFullyAuthenticated()">
    <%
        //This should never be done in a normal page and should exist in a proper MVC controller of some sort, but for this
        //demo, we'll just pull out Stormpath Account data from Spring Security's principal <c:out/> tag next:
        pageContext.setAttribute("account", ((StormpathUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getProperties());

    %>
    <c:out value="${account.givenName}"/></sec:authorize>!
    ( <sec:authorize access="isFullyAuthenticated()"><a href="<c:url value="/logout"/>">Log out</a></sec:authorize>
    <sec:authorize access="!isFullyAuthenticated()"><a href="<c:url value="/spring_security_login"/>">Log in</a> (sample accounts provided)</sec:authorize> )
</p>

<p>Welcome to the Stormpath + Spring Security Quickstart sample application.
    This page represents the home page of any web application.</p>

<sec:authorize access="isFullyAuthenticated()">
<p>Visit your <a href="<c:url value="/account"/>">account page</a>.</p>
<p>Edit your <a href="/account/customData">account's custom data</a>.</p>
</sec:authorize>

<sec:authorize access="!isFullyAuthenticated()"><p>If you want to access the user-only <a href="<c:url value="/account"/>">account page</a> or <a href="<c:url
value="/account/customData"/>">custom data page</a>, you will need to log-in first.</p></sec:authorize>

<h2>Roles</h2>

<p>To show some taglibs, here are the roles you have and don't have. Log out and log back in under different user
    accounts to see different roles.</p>

<h3>Roles you have</h3>

<p>
    <sec:authorize access="hasRole('${GOOD_GUYS}')">Good Guys<br/></sec:authorize>
    <sec:authorize access="hasRole('${BAD_GUYS}')">Bad Guys<br/></sec:authorize>
    <sec:authorize access="hasRole('${SCHWARTZ_MASTERS}')">Schwartz Masters<br/></sec:authorize>
</p>

<h3>Roles you DON'T have</h3>

<p>
    <sec:authorize access="!hasRole('${GOOD_GUYS}')">Good Guys<br/></sec:authorize>
    <sec:authorize access="!hasRole('${BAD_GUYS}')">Bad Guys<br/></sec:authorize>
    <sec:authorize access="!hasRole('${SCHWARTZ_MASTERS}')">Schwartz Masters<br/></sec:authorize>
</p>

<h2>Permissions</h2>

<ul>
    <li>You may <sec:authorize access="!hasPermission('ship:NCC-1701-D', 'command')"><b>NOT</b> </sec:authorize> command the <code>NCC-1701-D</code> Starship!</li>
    <li>You may <sec:authorize access="!hasPermission('${account.username}', 'user', 'edit')"><b>NOT</b> </sec:authorize>  edit the ${account.username} user!</li>
</ul>

</body>
</html>
