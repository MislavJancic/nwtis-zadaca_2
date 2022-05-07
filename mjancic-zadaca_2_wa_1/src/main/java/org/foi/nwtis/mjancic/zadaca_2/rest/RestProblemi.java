package org.foi.nwtis.mjancic.zadaca_2.rest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.foi.nwtis.mjancic.zadaca_2.podaci.Problem;
import org.foi.nwtis.mjancic.zadaca_2.podaci.RepozitorijProblemi;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("problemi")
public class RestProblemi {

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{icao}")
	public Response dajProblemeAerodroma(@PathParam("icao") String icao, @QueryParam("str") String str,
			@QueryParam("br") String br) {
		Connection veza = RepozitorijProblemi.dohvatiInstancu().spoji();
		Response odgovor = null;
		int brojStranice = 0;
		int limit = 0;
		if (str != null && br != null) {
			brojStranice = Integer.parseInt(str);
			limit = Integer.parseInt(br);
		}
		RepozitorijProblemi rp = RepozitorijProblemi.dohvatiInstancu();
		List<Problem> problemi = rp.dohvatiProblemZaIcao(icao, veza, limit, limit*brojStranice);

		odgovor = Response.status(Response.Status.OK).entity(problemi).build();

		if (odgovor == null) {
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema aerodroma: " + icao).build();
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
	public Response dajProbleme(@PathParam("icao") String icao, @QueryParam("str") String str,
			@QueryParam("br") String br) {
		Connection veza = RepozitorijProblemi.dohvatiInstancu().spoji();
		Response odgovor = null;
		int brojStranice = 0;
		int limit = 0;
		if (str != null && br != null) {
			brojStranice = Integer.parseInt(str);
			limit = Integer.parseInt(br);
		}
		RepozitorijProblemi rp = RepozitorijProblemi.dohvatiInstancu();
		List<Problem> problemi = rp.dohvatiProbleme(veza, limit, limit*brojStranice);

		odgovor = Response.status(Response.Status.OK).entity(problemi).build();

		if (odgovor == null) {
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema aerodroma: " + icao).build();
		}
		try {
			veza.close();
		} catch (SQLException | NullPointerException e) {
			odgovor = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greška kod rada s bazom.").build();
		}
		return odgovor;
	}

	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{icao}")
	public Response obrisiProblemeZaIcao(@PathParam("icao") String icao) {
		Connection veza = RepozitorijProblemi.dohvatiInstancu().spoji();
		Response odgovor = null;

		RepozitorijProblemi rp = RepozitorijProblemi.dohvatiInstancu();
		int uspjeh = rp.obrisiProblemeZaIcao(icao, veza);

		if (uspjeh > 0) {
			odgovor = Response.status(Response.Status.OK).entity("Obrisano " + uspjeh + " unosa.").build();
		} else {
			odgovor = Response.status(Response.Status.EXPECTATION_FAILED).entity("Ništa nije obrisano za: " + icao)
					.build();
		}

		try {
			veza.close();
		} catch (SQLException | NullPointerException e) {
			odgovor = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greška kod rada s bazom.").build();
		}
		return odgovor;
	}
}
