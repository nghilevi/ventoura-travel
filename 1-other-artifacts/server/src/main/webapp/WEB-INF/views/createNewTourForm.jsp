<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<head>
</head>
<body>
	<form action="/guide/tour/createNewTour" name="createNewTourForm"
		method="post" enctype="multipart/form-data">
		<table>
			<tr>
				<td><spring:message code="label.guideId" /></td>
				<td><input type="text" name=tourGuideId maxLength="30" value="1"
					size="45" /></td>
			</tr>
			<tr>
				<td><spring:message code="label.tourName" /></td>
				<td><input type="text" name="tourName" maxLength="30" size="45" /></td>
			</tr>
			<tr>
				<td><spring:message code="label.tourGallery" /></td>
				<td><input type="file" name="tourGallery" id="file" /><br></td>
			</tr>
			<tr>
				<td><spring:message code="label.tourPrice" /></td>
				<td><input type="text" name="tourPrice" maxLength="30"
					size="45" /></td>
			</tr>
			<tr>
				<td><spring:message code="label.tourStartTime" /></td>
				<td><input type="text" name="tourStartTime" maxLength="30"
					size="45" /></td>
			</tr>
			<tr>
				<td><spring:message code="label.tourEndTime" /></td>
				<td><input type="text" name="tourEndTime" maxLength="30"
					size="45" /></td>
			</tr>
			<tr>
				<td><spring:message code="label.city" /></td>
				<td><select name="city">
						<c:forEach items="${cityList}" var="entry">
							<option value="${entry.id}">${entry.cityName}</option>
						</c:forEach>
				</select></td>
			</tr>
			<tr>
				<td><spring:message code="label.country" /></td>
				<td><select name="country">
						<c:forEach items="${countryList}" var="entry">
							<option value="${entry.id}">${entry.countryName}</option>
						</c:forEach>
				</select></td>
			</tr>
			<tr>
				<td><spring:message code="label.tourDescription" /></td>
				<td><textarea name="tourTextDescription" rows="1" cols="38" /></textarea></td>
			</tr>
			<tr></tr>
			<tr>
				<td align="right"><input type="submit"
					value=<spring:message code="label.confirm"/> /></td>
			</tr>
		</table>

	</form>
</body>