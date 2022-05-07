<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dodavanje aerodroma za praćenje</title>
</head>
<body>
	<script type="text/javascript">
		var brStranice=0;
	
		function dodaj(){
			location.href = "${pageContext.servletContext.contextPath}/mvc/aerodromi/dodajAerodrom?icao="+document.getElementById("icao").value;
		}
		
	</script>
	<h1>Dodavanje aerodroma za praćenje</h1>
	<a
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pocetak">
		Početna </a>
	<br>

	<label for="datum">Unesite icao:</label>
	<br/>
	<input type="text" id="icao" name="icao" placeholder="LDZA">
	<br/>
	<br/>
	<button onclick="dodaj()">Dodaj</button>
	
</body>
</html>