package org.foi.nwtis.mjancic.zadaca_2.mvc;

import java.util.ArrayList;
import java.util.List;

import org.foi.nwtis.podaci.Aerodrom;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;

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
	public void pregledSvihAerodroma() {
		AerodromiKlijent ak = new AerodromiKlijent(context);
		List<Aerodrom> a =  ak.dajSveAerodrome();
		model.put("aerodromi", a);
	}
	@GET
	@Path("pregledAerodroma")
	@View("pregledAerodroma.jsp")
	public void pregledAerodroma() {
		AerodromiKlijent ak = new AerodromiKlijent(context);
		Aerodrom a =  ak.dajAerodrom("LOWW");
		List<Aerodrom> ai = new ArrayList<Aerodrom>();
		ai.add(a);
		model.put("aerodrom", ai);
	}
	
}
