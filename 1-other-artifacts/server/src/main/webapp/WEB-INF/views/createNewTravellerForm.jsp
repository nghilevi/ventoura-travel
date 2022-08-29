<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<head>
</head>
<body>
	<form action="/user/createNewTravller" name="createNewUserForm"
		method="post" enctype="multipart/form-data">
		<table>
			<tr>
				<td><spring:message code="label.traveller.travallerName" /></td>
				<td><input type="text" name=travallerName maxLength="30" value="1"
					size="45" /></td>
			</tr>
			<tr>
				<td><spring:message code="label.traveller.facebookAccountName" /></td>
				<td><input type="text" name=facebookAccountName maxLength="30" value="1"
					size="45" /></td>
			</tr>
			<tr>
				<td><spring:message code="label.traveller.male" /></td>
				<td><input type="radio" name="gender" value="male"></td>
			</tr>
			<tr>
				<td><spring:message code="label.traveller.female" /></td>
				<td><input type="radio" name="gender" value="female"></td>
			</tr>

			<tr>
				<td><spring:message code="label.traveller.dateOfBirth" /></td>
				<td><input type="date" name=dateOfBirth /></td>
			</tr>
			
			<tr>
				<td><spring:message code="label.traveller.photo" /></td>
				<td><input type="file" name="photo" id="file" /><br></td>
			</tr>
			
			<tr>
				<td><spring:message code="label.traveller.textBiography" /></td>
				<td><textarea name="textBiography" rows="1" cols="38" /></textarea></td>
			</tr>
			<tr></tr>
			<tr>
				<td align="right"><input type="submit"
					value=<spring:message code="label.confirm"/> /></td>
			</tr>
		</table>

	</form>
</body>