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
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledPolazakaAerodroma">Pregled
				polazaka s aerodroma</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledOdlazakaAerodroma">Pregled
				odlazaka s aerodroma</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/aerodromi/dodajAerodrom">Dodaj
				aerodrom</a></li>
	</ul>
</body>
</html>