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
		var brStranice = 0;

		function prikazi() {
			location.href = "${pageContext.servletContext.contextPath}/mvc/problemi/prikazProblemaIcao?icao="
					+ document.getElementById("icao").value + "&str=" + brStranice;
		}
	</script>
	<h1>Prikaz aerodroma</h1>
	<a
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pocetak">
		Početna </a>
	<br>

	<h2>ICAO: ${requestScope.icao}</h2>
	<label for="datum">Unesite ICAO:</label>
	<br />
	<br />
	<input type="text" id="icao" name="icao" placeholder="LDZA">
	<br />
	<br />
	<button onclick="prikazi()">Prikaži</button>

</body>
</html>