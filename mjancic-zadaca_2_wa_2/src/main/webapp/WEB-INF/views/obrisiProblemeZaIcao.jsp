<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Obrisi probleme za icao</title>
</head>
<body>
	<script type="text/javascript">

		function obrisi() {
			location.href = "${pageContext.servletContext.contextPath}/mvc/problemi/obrisiProbleme?icao="
					+ document.getElementById("icao").value;
		}
	</script>
	<h1>Obrisi probleme za icao</h1>
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
	<button onclick="obrisi()">Obriši</button>

</body>
</html>