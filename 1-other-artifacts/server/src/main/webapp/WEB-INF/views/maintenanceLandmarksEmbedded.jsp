<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div style="margin: 20px; margin-left: 60px;">
	<div class="maintenance_navigation" id="maintenance_langmark">
		<%
			int i = 1;
		%>
		<div class="resourceTitle">
			<a href="javascript:void(0)"
				onclick="showResourceForm('#uploadLandmark')"><img
				src="i/upload.png" width="30px" height="30px"
				title=<spring:message code="label.add" />
				style="float: right; margin-right: 50px;"></a>
			<h3 class="resourceType"><%=i%>
			</h3>
		</div>
		<div style="display: none;" id="uploadLandmark">
			<hr style="color: #9c0; height: 1px;">
			<form action="uploadLandmark" name="uploadLandmarkForm" method="post"
				enctype="multipart/form-data">
				<table>
					<tr>
						<td><spring:message code="label.name" /></td>
						<td><input type="text" name="landmarkName" maxLength="30"
							size="45" /></td>
					</tr>
					<tr>
						<td><spring:message code="label.picture" /></td>
						<td><input type="file" name="landmarkPicture" id="file"><br></td>
					</tr>
					<tr>
						<td><spring:message code="label.ticketPrice" /></td>
						<td><input type="text" name="landmarkTicketPrice"
							maxLength="30" size="45" /></td>
					</tr>
					<tr>
						<td><spring:message code="label.city" /></td>
						<td><select name="landmarkCity">
								<c:forEach items="${cityList}" var="entry">
									<option value="${entry.id}">${entry.cityName}</option>
								</c:forEach>
						</select></td>
					</tr>
					<tr>
						<td><spring:message code="label.country" /></td>
						<td><select name="landmarkCountry">
								<c:forEach items="${countryList}" var="entry">
									<option value="${entry.id}">${entry.countryName}</option>
								</c:forEach>
						</select></td>
					</tr>
					<tr>
						<td><spring:message code="label.description" /></td>
						<td><textarea name="landmarkDescription" rows="1" cols="38" /></textarea></td>
					</tr>
					<tr></tr>
					<tr>
					<tr>
						<td align="right"><input type="submit"
							value=<spring:message code="label.confirm"/> /></td>
					</tr>
				</table>

			</form>
		</div>
		<c:forEach items="${landmarkList}" var="entry">
			<div class="langmarkInfoDiv">
				<span style="color: green;"><spring:message
						code="label.description" /></span>
				<c:out value="${entry.landmarkIntroduction}" />
				<img src="landmarkImage/${entry.id}" alt="landmarkImg" width="300" height="200"/>
				<hr style="color: #9c0; height: 1px;">
			</div>
			<br />
			<%
				i++;
			%>
		</c:forEach>
	</div>
</div>