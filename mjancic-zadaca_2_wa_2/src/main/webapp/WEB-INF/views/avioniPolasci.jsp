<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pregled dolazaka</title>
</head>
<body>
	<script type="text/javascript">
		var brStranice = "${requestScope.brojStranice}";

		function klikDalje() {
			//alert(${requestScope.brojStranice} );
			brStranice++;
			//alert("${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledSvihAerodroma"+"?str="+brStranice)
			location.href = "${pageContext.servletContext.contextPath}/mvc/aerodromi/avioniPolasci"
					+ "?str="
					+ brStranice
					+ "&icao=${requestScope.icao}&dan=${requestScope.dan}";
		}

		function klikPrije() {
			if (brStranice > 0) {
				brStranice--;
				location.href = "${pageContext.servletContext.contextPath}/mvc/aerodromi/avioniPolasci"
						+ "?str="
						+ brStranice
						+ "&icao=${requestScope.icao}&dan=${requestScope.dan}";
			}

		}
	</script>
	<h1>Pregled dolazaka</h1>
	<a
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pocetak">
		Početna </a>
	<br>
	<button onclick="klikPrije()">Prije</button>
	<button onclick="klikDalje()">Dalje</button>
	<table>
		<tr>
			<th>Callsign</th>
			<th>Icao24</th>
			<th>EstArrivalAirport</th>
			<th>EstDepartureAirport</th>
			<th>FirstSeen</th>
			<th>LastSeen</th>
		</tr>
		<c:forEach var="a" items="${requestScope.polasci}">
			<tr>
				<td>${a.callsign}</td>
				<td>${a.icao24}</td>
				<td>${a.estArrivalAirport}</td>
				<td>${a.estDepartureAirport}</td>
				<td>${a.firstSeen}</td>
				<td>${a.lastSeen}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>