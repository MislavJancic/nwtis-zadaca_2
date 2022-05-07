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
	<h1>Unesi aerodrom</h1>
	<a
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pocetak">
		Početna </a>
	<br>
	<form action="${pageContext.servletContext.contextPath}/mvc/aerodromi/PrikaziIcao" method="POST">
		<label for="korime">ICAO ident</label>
		<input type="text" id="icao" name="icao" />

		<input type="submit" value="Potvrdi" />
	</form>

</body>
</html>