<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<head>
</head>
<body>
	<form action="/user/createNewGuide" name="createNewGuideForm"
		method="post" enctype="multipart/form-data">
		<table>
			<tr>
				<td><spring:message code="label.guide.guideName" /></td>
				<td><input type="text" name=travallerName maxLength="30"
					value="1" size="45" /></td>
			</tr>
			<tr>
				<td><spring:message code="label.guide.facebookAccountName" /></td>
				<td><input type="text" name=facebookAccountName maxLength="30"
					value="1" size="45" /></td>
			</tr>
			<tr>
				<td><spring:message code="label.guide.male" /></td>
				<td><input type="radio" name="gender" value="male"></td>
			</tr>
			<tr>
				<td><spring:message code="label.guide.female" /></td>
				<td><input type="radio" name="gender" value="female"></td>
			</tr>

			<tr>
				<td><spring:message code="label.guide.dateOfBirth" /></td>
				<td><input type="date" name=dateOfBirth /></td>
			</tr>
			
			<tr>
				<td><spring:message code="label.guide.payByArrival" /></td>
				<td><input type="radio" name="paymentMethod" value="0"></td>
			</tr>
			<tr>
				<td><spring:message code="label.guide.prepay" /></td>
				<td><input type="radio" name="paymentMethod" value="1"></td>
			</tr>
			<tr>
				<td><spring:message code="label.guide.bothpay" /></td>
				<td><input type="radio" name="paymentMethod" value="2"></td>
			</tr>
			
			<tr>
				<td><spring:message code="label.guide.email" /></td>
				<td><input type="email" name=email /></td>
			</tr>
			<tr>
				<td><spring:message code="label.guide.address" /></td>
				<td><input type="text" name=address /></td>
			</tr>
			<tr>
				<td><spring:message code="label.guide.contactNo" /></td>
				<td><input type="text" name=contactNo /></td>
			</tr>

			<tr>
				<td><spring:message code="label.guide.photo" /></td>
				<td><input type="file" name="photo" id="file" /><br></td>
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
				<td><spring:message code="label.guide.textBiography" /></td>
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