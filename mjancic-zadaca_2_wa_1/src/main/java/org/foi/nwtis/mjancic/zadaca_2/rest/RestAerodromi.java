package org.foi.nwtis.mjancic.zadaca_2.rest;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.foi.nwtis.mjancic.zadaca_2.podaci.JsonIcao;
import org.foi.nwtis.mjancic.zadaca_2.podaci.RepozitorijAerodromi;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("aerodromi")
public class RestAerodromi {

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response dajSveAerodrome(@Context HttpServletRequest rq, @QueryParam("str") String str,
			@QueryParam("br") String br) {
		Connection veza = RepozitorijAerodromi.dohvatiInstancu().spoji();
		Response odgovor = null;

		int brojStranice = 0;
		int limit = 0;
		if (str != null && br != null) {
			brojStranice = Integer.parseInt(str);
			limit = Integer.parseInt(br);
		}
		if (rq.getParameter("preuzimanje") != null) {
			return dajAerodromeZaPratiti(brojStranice, limit);
		}
		RepozitorijAerodromi ra = RepozitorijAerodromi.dohvatiInstancu();
		// ra.spoji();
		System.out.println("LIMIT " + limit + " OFFSET " + brojStranice * limit);
		List<Aerodrom> aerodromi = ra.dohvatiSveAerodrome(veza, limit, brojStranice * limit);

		if (aerodromi != null) {
			odgovor = Response.status(Response.Status.OK).entity(aerodromi).build();
		} else {
			odgovor = Response.status(Response.Status.NO_CONTENT).entity("Nema odgovora.").build();
		}
		try {
			veza.close();
		} catch (SQLException | NullPointerException e) {
			odgovor = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greška kod rada s bazom.").build();
		}
		return odgovor;
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response dodajAerodromZaPratiti(JsonIcao icao) {
		Connection veza = RepozitorijAerodromi.dohvatiInstancu().spoji();
		System.out.println("ICAO ZA DODATI JE: " + icao.icao);
		Response odgovor = null;
		RepozitorijAerodromi ra = RepozitorijAerodromi.dohvatiInstancu();
		boolean uspjeh = ra.dodajAerodromZaPratiti(icao.icao, veza);
		if (uspjeh) {
			odgovor = Response.status(Response.Status.OK).entity("Uspjeh.").build();
		} else {
			odgovor = Response.status(Response.Status.NOT_MODIFIED).entity("Unos nije uspio.").build();
		}
		try {
			veza.close();
		} catch (SQLException | NullPointerException e) {
			odgovor = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greška kod rada s bazom.").build();
		}
		return odgovor;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{icao}")
	public Response dajAerodrom(@PathParam("icao") String icao) {
		Connection veza = RepozitorijAerodromi.dohvatiInstancu().spoji();
		Response odgovor = null;

		RepozitorijAerodromi ra = RepozitorijAerodromi.dohvatiInstancu();
		Aerodrom ad = ra.dohvatiAerodrom(icao, veza);

		odgovor = Response.status(Response.Status.OK).entity(ad).build();

		if (odgovor == null) {
			odgovor = Response.status(Response.Status.NO_CONTENT).entity("Nema aerodroma: " + icao).build();
		}
		try {
			veza.close();
		} catch (SQLException | NullPointerException e) {
			odgovor = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greška kod rada s bazom.").build();
		}
		return odgovor;
	}

	public Response dajAerodromeZaPratiti(int brojStranice, int limit) {
		Connection veza = RepozitorijAerodromi.dohvatiInstancu().spoji();
		Response odgovor = null;
		RepozitorijAerodromi ra = RepozitorijAerodromi.dohvatiInstancu();
		List<Aerodrom> aerodromi = ra.dohvatiAerodromePracene(veza, limit, brojStranice * limit);

		if (aerodromi != null) {
			odgovor = Response.status(Response.Status.OK).entity(aerodromi).build();

		} else {
			odgovor = Response.status(Response.Status.NO_CONTENT).entity("Nema aerodroma.").build();
		}
		try {
			veza.close();
		} catch (SQLException | NullPointerException e) {
			odgovor = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greška kod rada s bazom.").build();
		}
		return odgovor;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{icao}/polasci")
	public Response dajPolaskeAerodoma(@PathParam("icao") String icao, @QueryParam("dan") String dan,
			@QueryParam("str") String str, @QueryParam("br") String br) {
		Connection veza = RepozitorijAerodromi.dohvatiInstancu().spoji();
		int brojStranice = 0;
		int limit = 0;
		if (str != null && br != null) {
			brojStranice = Integer.parseInt(str);
			limit = Integer.parseInt(br);
		}
		System.out.println("ICAO POLASCI: " + icao);
		List<AvionLeti> letovi = null;
		Response odgovor = null;
		DateFormat formatDatuma = new SimpleDateFormat("dd.MM.yyyy");
		Date datum;
		RepozitorijAerodromi ra = RepozitorijAerodromi.dohvatiInstancu();
		if (dan != null) {

			System.out.println("DAN " + dan);
			try {
				datum = formatDatuma.parse(dan);
				letovi = ra.dohvatiIcaoPolaske(icao, datum.getTime() / 1000, veza, limit, brojStranice * limit);
				System.out.println("USPIO PARSE");
			} catch (ParseException e) {
				System.out.println("EXCEPTION APRSE");

			}

		} else {
			letovi = ra.dohvatiIcaoPolaske(icao, null, veza);
		}

		if (letovi != null) {
			odgovor = Response.status(Response.Status.OK).entity(letovi).build();

		} else {
			System.out.println("EXCEPTION APRSE");
			odgovor = Response.status(Response.Status.NO_CONTENT).entity("Nema polazaka.").build();
		}
		try {
			veza.close();
		} catch (SQLException | NullPointerException e) {
			odgovor = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greška kod rada s bazom.").build();
		}
		return odgovor;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{icao}/dolasci")
	public Response dajDolaskeAerodoma(@PathParam("icao") String icao, @QueryParam("dan") String dan,
			@QueryParam("str") String str, @QueryParam("br") String br) {
		Connection veza = RepozitorijAerodromi.dohvatiInstancu().spoji();
		int brojStranice = 0;
		int limit = 0;
		if (str != null && br != null) {
			brojStranice = Integer.parseInt(str);
			limit = Integer.parseInt(br);
		}
		System.out.println("ICAO DOLASCI: " + icao);
		List<AvionLeti> letovi = null;
		Response odgovor = null;
		DateFormat formatDatuma = new SimpleDateFormat("dd.MM.yyyy");
		Date datum;
		RepozitorijAerodromi ra = RepozitorijAerodromi.dohvatiInstancu();
		if (dan != null) {

			System.out.println("DAN " + dan);
			try {
				datum = formatDatuma.parse(dan);
				letovi = ra.dohvatiIcaoDolaske(icao, datum.getTime() / 1000, veza,limit, brojStranice * limit);
				System.out.println("USPIO PARSE");
			} catch (ParseException e) {
				System.out.println("EXCEPTION APRSE");

			}

		} else {
			letovi = ra.dohvatiIcaoDolaske(icao, null, veza);
		}

		if (letovi != null) {
			odgovor = Response.status(Response.Status.OK).entity(letovi).build();

		} else {
			odgovor = Response.status(Response.Status.NO_CONTENT).entity("Nema dolazaka.").build();
		}
		try {
			veza.close();
		} catch (SQLException | NullPointerException e) {
			odgovor = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greška kod rada s bazom.").build();
		}
		return odgovor;
	}

}
