<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dodavanje aerodroma za pracenje</title>
</head>
<body>
	<h1>Dodavanje aerodroma za pracenje</h1>
	<a
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pocetak">
		Početna </a>
	<br>
	<div style="color: red; font-weight: bold;">${requestScope.greska}</div>
	<form action="${pageContext.servletContext.contextPath}/DodajIcao" method="POST">
		<label for="korime">ICAO identifikacija</label>
		<input type="text" name="icao" /><br>
		<input type="submit" value="Registriraj" />
	</form>
</body>
</html>