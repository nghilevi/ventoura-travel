<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title><spring:message code="navigation.maintenance" /></title>
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
						<div id="maintenanceHelpTitleDiv" class="switchDivLink"
							style="background: white; border-color: white">
							<a href="#"
								onclick="switchMaintenanceDiv('maintenanceHelpTitleDiv'); return false;"
								style="text-decoration: none"><spring:message code="label.manual" /></a>
						</div>
						<div id="landmarksTitleDiv" class="switchDivLink">
							<a href="#"
								onclick="switchMaintenanceDiv('landmarksTitleDiv'); return false;"
								style="text-decoration: none"><spring:message code="label.Landmarks" /></a>
						</div>
					</div>
					<div id="landmarksContentDiv">
						<%@include file="maintenanceLandmarksEmbedded.jsp"%>
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
