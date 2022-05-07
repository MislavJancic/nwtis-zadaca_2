<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aerodromi</title>
</head>
<body>
	<h1>Zadaća 2</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledSvihAerodroma?str=0">Pregled
				svih aerodroma</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledPracenihAerodroma?str=0">Pregled
				praćenih aerodroma</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledAerodroma">Pregled
				jednog aerodroma</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi/dodajAerodromZaPracenje">Dodaj
				aerodrom</a></li>

		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/problemi/prikazProblema?str=0">Prikaz
				svih problema </a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/problemi/upisiIcaoZaProbleme">Prikaz
				svih problema za ICAO </a></li>
				<li><a
			href="${pageContext.servletContext.contextPath}/mvc/problemi/obrisiProblemeZaIcao">Brisanje
				svih problema za ICAO </a></li>
	</ul>
</body>
</html>

