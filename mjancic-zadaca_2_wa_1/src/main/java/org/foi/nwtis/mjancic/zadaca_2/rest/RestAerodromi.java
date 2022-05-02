package org.foi.nwtis.mjancic.zadaca_2.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.Lokacija;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("aerodromi")
public class RestAerodromi {

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response dajSveAerodrome() {
		Response odgovor = null;

		// TODO ovo mora doći iz baze
		List<Aerodrom> aerodromi = new ArrayList<>();
		Aerodrom ad = new Aerodrom("LDZA", "Airport Zagreb", "HR", new Lokacija("0", "0"));
		aerodromi.add(ad);
		ad = new Aerodrom("LDVA", "Airport Varaždin", "HR", new Lokacija("0", "0"));
		aerodromi.add(ad);
		ad = new Aerodrom("EDDF", "Airport Frankfurt", "DE", new Lokacija("0", "0"));
		aerodromi.add(ad);
		ad = new Aerodrom("EDDB", "Airport Berlin", "DE", new Lokacija("0", "0"));
		aerodromi.add(ad);
		ad = new Aerodrom("LOWW", "Airport Vienna", "AT", new Lokacija("0", "0"));
		aerodromi.add(ad);

		if (aerodromi != null) {
			odgovor = Response.status(Response.Status.OK).entity(aerodromi).build();
		} else {
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema aerodroma.").build();
		}

		return odgovor;
	}

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

		// TODO ovo mora doći iz baze
		List<Aerodrom> aerodromi = new ArrayList<>();
		Aerodrom ad = new Aerodrom("LDZA", "Airport Zagreb", "HR", new Lokacija("0", "0"));
		aerodromi.add(ad);
		ad = new Aerodrom("LDVA", "Airport Varaždin", "HR", new Lokacija("0", "0"));
		aerodromi.add(ad);
		ad = new Aerodrom("EDDF", "Airport Frankfurt", "DE", new Lokacija("0", "0"));
		aerodromi.add(ad);
		ad = new Aerodrom("EDDB", "Airport Berlin", "DE", new Lokacija("0", "0"));
		aerodromi.add(ad);
		ad = new Aerodrom("LOWW", "Airport Vienna", "AT", new Lokacija("0", "0"));
		aerodromi.add(ad);

		for(Aerodrom a : aerodromi) {
			if(a.getIcao().compareTo(icao) == 0) {
				odgovor = Response.status(Response.Status.OK).entity(a).build();
				break;
			}
		}
		
		if (odgovor == null) {
			odgovor = Response.status(Response.Status.NOT_FOUND).entity("Nema aviona: "+icao).build();
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
