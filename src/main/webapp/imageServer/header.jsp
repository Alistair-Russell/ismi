<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="de.mpiwg.itgroup.diva.jsp.JSPDigitalization"%>

<jsp:useBean id="contextBean" class="de.mpiwg.itgroup.diva.jsp.JSPContext" scope="session" />
<jsp:setProperty name="contextBean" property="request" value="${pageContext.request}" />
<jsp:setProperty name="contextBean" property="response" value="${pageContext.response}" />

<link href="../resources/css/ismi-db/default.css" type="text/css" rel="stylesheet" />

		<div id="header">

			<div id="icon">
				<div id="iconContent">
					<img src="http://www.mpiwg-berlin.mpg.de/en/images/logo.png"></img>
				</div>
			</div>
			<div id="headerContent">
				<h1>
					<a href="${contextBean.getAppBean().getRoot()}">ISMI</a>
				</h1>
				<p>Islamic Scientific Manuscripts Initiative</p>
			</div>
		</div>


		<div id="login">
			<div id="loginContent">
				
			</div>
		</div>