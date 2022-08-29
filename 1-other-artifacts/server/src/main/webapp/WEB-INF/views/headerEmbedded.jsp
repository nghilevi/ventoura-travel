<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="header">
	<div id="logo"
		style="background: url(i/logo.jpg); width: 800px; height: 96px; color: #9c0;">
		<c:if test="${empty user}">
			<p>
				<a onclick="login()">登陆</a> or <a href="/studentRegister"><span
					style="color: #9c0;">注册</span></a>
			</p>
		</c:if>
		<c:if test="${not empty user}">
			<p>欢迎你--${user.name}</p>
		</c:if>
	</div>
	<div id="loginDialogDiv"></div>
</div>
<br />
<!-- /header -->