package org.foi.nwtis.mjancic.zadaca_2.mvc;

import java.util.ArrayList;
import java.util.List;

import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

@Controller
@Path("aerodromi")
@RequestScoped
public class PregledAerodroma {
	@Context
	ServletContext context;

	@Inject
	private Models model;

	@GET
	@Path("pocetak")
	@View("index.jsp")
	public void pocetak() {
	}

	@GET
	@Path("pregledSvihAerodroma")
	@View("pregledSvihAerodroma.jsp")
	public void pregledSvihAerodroma(@QueryParam("str") String brojStranice) {
		AerodromiKlijent ak = new AerodromiKlijent(context);
		List<Aerodrom> a = ak.dajSveAerodrome(brojStranice);
		model.put("brojStranice", brojStranice);
		model.put("aerodromi", a);
	}

	@GET
	@Path("pregledAerodroma")
	@View("upisAerodroma.jsp")
	public void pregledAerodroma() {

	}

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Path("PrikaziIcao")
	@View("prikaziIcao.jsp")
	public void prikaziIcao(@FormParam("icao") String icao) {
		if (icao == null)
			icao = "null";
		else if (icao.length() == 0)
			icao = "null";
		AerodromiKlijent ak = new AerodromiKlijent(context);
		Aerodrom a = ak.dajAerodrom(icao);
		List<Aerodrom> ai = new ArrayList<Aerodrom>();
		ai.add(a);
		model.put("aerodrom", ai);
	}

	@GET
	@Path("pregledPracenihAerodroma")
	@View("pregledPracenihAerodroma.jsp")
	public void pregledPracenihAerodroma(@QueryParam("str") String brojStranice) {
		AerodromiKlijent ak = new AerodromiKlijent(context);
		List<Aerodrom> a = ak.dajPraceneAerodrome(brojStranice);
		model.put("brojStranice", brojStranice);
		model.put("aerodromi", a);
	}

	@GET
	@Path("opcijeIcao")
	@View("opcijeIcao.jsp")
	public void opcijeIcao(@QueryParam("icao") String icao) {
		model.put("icao", icao);
	}

	@GET
	@Path("avioniPolasci")
	@View("avioniPolasci.jsp")
	public void avioniPolasci(@QueryParam("icao") String icao, @QueryParam("dan") String dan,
			@QueryParam("str") String brojStranice) {
		AerodromiKlijent ak = new AerodromiKlijent(context);
		List<AvionLeti> a = ak.dajPolaske(icao, dan, brojStranice);
		model.put("brojStranice", brojStranice);
		model.put("dan", dan);
		model.put("icao", icao);
		model.put("polasci", a);
	}

	@GET
	@Path("avioniDolasci")
	@View("avioniDolasci.jsp")
	public void avioniDolasci(@QueryParam("icao") String icao, @QueryParam("dan") String dan,
			@QueryParam("str") String brojStranice) {
		AerodromiKlijent ak = new AerodromiKlijent(context);
		List<AvionLeti> a = ak.dajDolaske(icao, dan, brojStranice);
		model.put("brojStranice", brojStranice);
		model.put("dan", dan);
		model.put("icao", icao);
		model.put("dolasci", a);
	}
	
	@GET
	@Path("dodajAerodromZaPracenje")
	@View("dodajAerodromZaPracenje.jsp")
	public void dodajAerodromZaPracenje() {
		
	}
	@GET
	@Path("dodajAerodrom")
	@View("index.jsp")
	public void dodajAerodrom(@QueryParam("icao") String icao) {
		AerodromiKlijent ak = new AerodromiKlijent(context);
		ak.dodajAerodromZaPracenje(icao);
		
	}

}
