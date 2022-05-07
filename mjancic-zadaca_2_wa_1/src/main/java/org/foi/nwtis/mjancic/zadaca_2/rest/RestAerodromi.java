package org.foi.nwtis.mjancic.zadaca_2.rest;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.foi.nwtis.mjancic.zadaca_2.podaci.JsonIcao;
import org.foi.nwtis.mjancic.zadaca_2.podaci.RepozitorijAerodromi;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestAerodromi.
 */
@Path("aerodromi")
public class RestAerodromi {

	/**
	 * Daj sve aerodrome.
	 *
	 * @param rq request context
	 * @param str stranica
	 * @param br broj podataka
	 * @return odgovor
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response dajSveAerodrome(@Context HttpServletRequest rq, @QueryParam("str") String str,
			@QueryParam("br") String br) {
		Connection veza = RepozitorijAerodromi.dohvatiInstancu().spoji();
		Response odgovor = null;
		RepozitorijAerodromi ra = RepozitorijAerodromi.dohvatiInstancu();
		int brojStranice = 0;
		int limit = 0;
		List<Aerodrom> aerodromi;
		if (str != null && br != null) {
			brojStranice = Integer.parseInt(str);
			limit = Integer.parseInt(br);

		}
		if (rq.getParameter("preuzimanje") != null) {
			return dajAerodromeZaPratiti(brojStranice, limit);
		}
		aerodromi = ra.dohvatiSveAerodrome(veza, limit, brojStranice * limit);
		System.out.println("LIMIT " + limit + " OFFSET " + brojStranice * limit);

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

	/**
	 * Dodaj aerodrom za pratiti.
	 *
	 * @param icao icao
	 * @return response
	 */
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

	/**
	 * Daj aerodrom.
	 *
	 * @param icao the icao
	 * @return the response
	 */
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

	/**
	 * Daj aerodrome za pratiti.
	 *
	 * @param brojStranice  broj stranice
	 * @param limit  limit
	 * @return response
	 */
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

	/**
	 * Daj polaske aerodoma.
	 *
	 * @param icao icao
	 * @param dan dan (datum dd.MM.yyyy)
	 * @param str stranica
	 * @param br broj podataka po stranici
	 * @return response
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{icao}/polasci")
	public Response dajPolaskeAerodoma(@PathParam("icao") String icao, @QueryParam("dan") String dan,
			@QueryParam("str") String str, @QueryParam("br") String br) {
		Connection veza = RepozitorijAerodromi.dohvatiInstancu().spoji();
		int brojStranice = 0;
		int limit = 0;
		RepozitorijAerodromi ra = RepozitorijAerodromi.dohvatiInstancu();
		List<AvionLeti> letovi = null;
		if (str != null && br != null) {
			brojStranice = Integer.parseInt(str);
			limit = Integer.parseInt(br);
			letovi = dajPolaskeExtracted(icao, dan, veza, brojStranice, limit, ra, letovi);
		} else {
			letovi = dajPolaskeExtracted(icao, dan, veza, 0, 0, ra, letovi);
		}
		System.out.println("ICAO POLASCI: " + icao);

		Response odgovor = null;

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

	/**
	 * Daj polaske extracted.
	 *
	 * @param icao icao
	 * @param dan dan
	 * @param veza veza
	 * @param brojStranice broj stranice
	 * @param limit limit
	 * @param ra repozitorij aerodroma
	 * @param letovi letovi
	 * @return list
	 */
	private List<AvionLeti> dajPolaskeExtracted(String icao, String dan, Connection veza, int brojStranice, int limit,
			RepozitorijAerodromi ra, List<AvionLeti> letovi) {
		DateFormat formatDatuma = new SimpleDateFormat("dd.MM.yyyy");
		Date datum;

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
			letovi = ra.dohvatiIcaoPolaske(icao, null, veza, limit, brojStranice * limit);
		}
		return letovi;
	}

	/**
	 * Daj dolaske aerodoma.
	 *
	 * @param icao icao
	 * @param dan dan
	 * @param str stranica
	 * @param br broj podataka po stranici
	 * @return response
	 */
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
		letovi = dohvatiIcaoDolaskeExtracted(icao, dan, veza, brojStranice, limit, letovi);

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

	/**
	 * Dohvati icao dolaske refaktorirano.
	 *
	 * @param icao icao
	 * @param dan dan
	 * @param veza veza
	 * @param brojStranice broj stranice
	 * @param limit limit
	 * @param letovi letovi
	 * @return lista letova
	 */
	private List<AvionLeti> dohvatiIcaoDolaskeExtracted(String icao, String dan, Connection veza, int brojStranice,
			int limit, List<AvionLeti> letovi) {
		DateFormat formatDatuma = new SimpleDateFormat("dd.MM.yyyy");
		Date datum;
		RepozitorijAerodromi ra = RepozitorijAerodromi.dohvatiInstancu();
		if (dan != null) {

			System.out.println("DAN " + dan);
			try {
				datum = formatDatuma.parse(dan);
				letovi = ra.dohvatiIcaoDolaske(icao, datum.getTime() / 1000, veza, limit, brojStranice * limit);
				System.out.println("USPIO PARSE");
			} catch (ParseException e) {
				System.out.println("EXCEPTION APRSE");

			}

		} else {
			letovi = ra.dohvatiIcaoDolaske(icao, null, veza, limit, brojStranice * limit);
		}
		return letovi;
	}

}
