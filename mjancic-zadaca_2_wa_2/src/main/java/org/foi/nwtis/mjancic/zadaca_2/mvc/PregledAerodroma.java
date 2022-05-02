package org.foi.nwtis.mjancic.zadaca_2.mvc;

import java.util.List;

import org.foi.nwtis.podaci.Aerodrom;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Controller
@Path("aerodromi")
@RequestScoped
public class PregledAerodroma {
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
		AerodromiKlijent ak = new AerodromiKlijent();
		List<Aerodrom> a =  ak.dajSveAerodrome();
		model.put("aerodromi", a);
	}
}
