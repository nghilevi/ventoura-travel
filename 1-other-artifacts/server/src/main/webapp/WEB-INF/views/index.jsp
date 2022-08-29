<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title><spring:message code="label.logo" /></title>
<%@include file="headIncludeEmbedded.jsp"%>
</head>

<body>
	<div id="wrapper">
		<div id="container">
			<%@include file="headerEmbedded.jsp"%>
			<%@include file="mainNavigationEmbedded.jsp"%>
			<div id="main">
				<div class="sectionOneColumnDiv">
					<div class="sectionOneColumnTitleDiv">
						<div id="dashboardTitleDiv" class="switchDivLink"
							style="background: white; border-color: white">
							<a href="#"
								onclick="switchIndexDiv('dashboardTitleDiv'); return false;"
								style="text-decoration: none">公告板</a>
						</div>
						<div id="softwareIntroTitleDiv" class="switchDivLink">
							<a href="#"
								onclick="switchIndexDiv('softwareIntroTitleDiv'); return false;"
								style="text-decoration: none">软件介绍</a>
						</div>
					</div>
					<div id="dashboardContentDiv">
						<%@include file="indexDashboardEmbedded.jsp"%>
					</div>
					<div id="softwareIntroContentDiv" style="display:none;">
						<%@include file="indexSoftwareIntroductionEmbedded.jsp"%>
					</div>
				</div>
			</div>
			<br /> <br /> <br /> <br />
			<!-- /columns-wrapper -->
			<%@include file="footerEmbedded.jsp"%>
		</div>
		<!-- /container -->
	</div>
	<!-- /wrapper -->

</body>
</html>
