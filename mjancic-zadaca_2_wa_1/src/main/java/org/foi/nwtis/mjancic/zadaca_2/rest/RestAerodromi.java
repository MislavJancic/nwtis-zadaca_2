package org.foi.nwtis.mjancic.zadaca_2.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	// @Consumes({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public Response dodajAerodromZaPratiti(@FormParam("icao") String icao) {
		System.out.println("ICAO ZA DODATI JE: " + icao);
		Response odgovor = null;
		RepozitorijAerodromi ra = RepozitorijAerodromi.dohvatiInstancu();
		boolean uspjeh = ra.dodajAerodromZaPratiti(icao);
		if (uspjeh) {
			odgovor = Response.status(Response.Status.OK).entity("Uspjeh.").build();
		} else {
			odgovor = Response.status(Response.Status.NOT_MODIFIED).entity("Unos nije uspio.").build();
		}
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
	public Response dajPolaskeAerodoma(@PathParam("icao") String icao) {
		System.out.println("ICAO POLASCI: " + icao);
		Response odgovor = null;
		RepozitorijAerodromi ra = RepozitorijAerodromi.dohvatiInstancu();
		List<AvionLeti> avioniPolasci = ra.dohvatiIcaoPolaske(icao);

		if (avioniPolasci != null) {
			odgovor = Response.status(Response.Status.OK).entity(avioniPolasci).build();

		} else {
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema polazaka.").build();
		}
		return odgovor;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{icao}/dolasci")
	public Response dajDolaskeAerodoma(@PathParam("icao") String icao) {
		System.out.println("ICAO DOLASCI: " + icao);
		Response odgovor = null;
		RepozitorijAerodromi ra = RepozitorijAerodromi.dohvatiInstancu();
		List<AvionLeti> avioniDolasci = ra.dohvatiIcaoDolaske(icao);

		if (avioniDolasci != null) {
			odgovor = Response.status(Response.Status.OK).entity(avioniDolasci).build();

		} else {
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema polazaka.").build();
		}
		return odgovor;
	}

}
