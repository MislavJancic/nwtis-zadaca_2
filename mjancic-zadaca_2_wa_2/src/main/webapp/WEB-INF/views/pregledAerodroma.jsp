<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pregled  aerodroma</title>
</head>
<body>
	<h1>Pregled  aerodroma</h1>
	<a
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pocetak">
		Početna </a>
	<br>
	<input type="text" id="icao" name="icao" />
	<table>
		<tr>
			<th>ICAO</th>
			<th>Naziv</th>
			<th>Država</th>
		</tr>
		<c:forEach var="a" items="${requestScope.aerodrom}">
			<tr>
				<td>${a.icao}</td>
				<td>${a.naziv}</td>
				<td>${a.drzava}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>