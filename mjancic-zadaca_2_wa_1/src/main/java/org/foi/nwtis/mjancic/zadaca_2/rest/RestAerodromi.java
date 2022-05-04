package org.foi.nwtis.mjancic.zadaca_2.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.foi.nwtis.mjancic.zadaca_2.podaci.RepozitorijAerodromi;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.Lokacija;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
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
	public Response dajSveAerodrome(@Context HttpServletRequest rq) {
		Response odgovor = null;
		if (rq.getParameter("preuzimanje") != null) {
			return dajAerodromeZaPratiti();
		}
		RepozitorijAerodromi ra = RepozitorijAerodromi.dohvatiInstancu();
		// ra.spoji();
		List<Aerodrom> aerodromi = ra.dohvatiSveAerodrome();

		if (aerodromi != null) {
			odgovor = Response.status(Response.Status.OK).entity(aerodromi).build();
		} else {
			odgovor = Response.status(Response.Status.NO_CONTENT).entity("Nema odgovora.").build();
		}

		return odgovor;
	}

	// TODO
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response dodajAerodromZaPratiti(String icao) {
		Response odgovor = null;
		return odgovor;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{icao}")
	public Response dajAerodrom(@PathParam("icao") String icao) {
		Response odgovor = null;

		RepozitorijAerodromi ra = RepozitorijAerodromi.dohvatiInstancu();
		Aerodrom ad = ra.dohvatiAerodrom(icao);

		odgovor = Response.status(Response.Status.OK).entity(ad).build();

		if (odgovor == null) {
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema aerodroma: " + icao).build();
		}

		return odgovor;
	}

	public Response dajAerodromeZaPratiti() {
		Response odgovor = null;
		RepozitorijAerodromi ra = RepozitorijAerodromi.dohvatiInstancu();
		List<Aerodrom> aerodromi = ra.dohvatiAerodromePracene();

		if (aerodromi != null) {
			odgovor = Response.status(Response.Status.OK).entity(aerodromi).build();
			
		} else {
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema aerodroma.").build();
		}
		return odgovor;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{icao}/polasci")
	public Response dajPolaskeAerodoma(String icao) {
		Response odgovor = null;
		return odgovor;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{icao}/odlasci")
	public Response dajDolaskeAerodoma(String icao) {
		Response odgovor = null;
		return odgovor;
	}

}
