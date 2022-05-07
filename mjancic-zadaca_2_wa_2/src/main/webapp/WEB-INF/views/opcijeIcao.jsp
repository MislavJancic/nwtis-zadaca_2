<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Prikaz aerodroma</title>
</head>
<body>
	<script type="text/javascript">
		var brStranice=0;
	
		function prikaziDolaske(){
			location.href = "${pageContext.servletContext.contextPath}/mvc/aerodromi/avioniDolasci"+
			"?str="+brStranice+"&icao=${requestScope.icao}&str=0&dan="+document.getElementById("datum").value;
		}
		function prikaziPolaske(){
			location.href = "${pageContext.servletContext.contextPath}/mvc/aerodromi/avioniPolasci"+
			"?str="+brStranice+"&icao=${requestScope.icao}&str=0&dan="+document.getElementById("datum").value;
		}
	</script>
	<h1>Prikaz aerodroma</h1>
	<a
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pocetak">
		Početna </a>
	<br>

	<h2>ICAO: ${requestScope.icao}</h2>
	<label for="datum">Unesite datum:</label>
	<br/>
	<br/>
	<input type="text" id="datum" name="datum" placeholder="dd.MM.yyyy">
	<br/>
	<br/>
	<button onclick="prikaziPolaske()">Prikaži polaske</button>
	<br/>
	<br/>
	<button onclick="prikaziDolaske()">Prikaži dolaske</button>
</body>
</html>