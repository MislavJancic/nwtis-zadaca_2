<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pregled svih aerodroma</title>
</head>
<body>
	<script type="text/javascript">
		var brStranice = "${requestScope.brojStranice}";
		
		
		function klikDalje(){
			//alert(${requestScope.brojStranice} );
			brStranice++;
			//alert("${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledSvihAerodroma"+"?str="+brStranice)
			location.href = "${pageContext.servletContext.contextPath}/mvc/problemi/prikazProblemaIcao"+"?str="+brStranice+"&icao=${requestScope.icao}";
		}

		function klikPrije() {
			if (brStranice >0 ){
				brStranice--;
				location.href = "${pageContext.servletContext.contextPath}/mvc/problemi/prikazProblemaIcao"+"?str="+brStranice+"&icao=${requestScope.icao}";
			}
			
		}

	</script>

	<h1>Pregled problema za ${requestScope.icao}</h1>
	<a
		href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pocetak">
		Početna </a>
	<br>
	<button onclick="klikPrije()">Prije</button>
	<button onclick="klikDalje()">Dalje</button>


	<table>
		<tr>
			<th>ICAO</th>
			<th>Opis problema</th>
			<th>Timestamp</th>
		</tr>
		<c:forEach var="a" items="${requestScope.problemi}">
			<tr>
				<td>${a.ident}</td>
				<td>${a.description}</td>
				<td>${a.stored}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>