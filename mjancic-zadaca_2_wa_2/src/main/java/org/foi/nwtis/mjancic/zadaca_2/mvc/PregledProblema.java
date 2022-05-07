package org.foi.nwtis.mjancic.zadaca_2.mvc;

import java.util.List;

import org.foi.nwtis.mjancic.zadaca_2.podaci.Problem;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.websocket.server.PathParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;

/**
 * Klasa PregledProblema.
 */
@Controller
@Path("problemi")
@RequestScoped
public class PregledProblema {
	
	/**   context. */
	@Context
	ServletContext context;

	/**  model. */
	@Inject
	private Models model;
	
	/**
	 * Prikaz problema.
	 *
	 * @param brojStranice   broj stranice
	 */
	@GET
	@Path("prikazProblema")
	@View("prikazProblema.jsp")
	public void prikazProblema(@QueryParam("str") String brojStranice) {
		ProblemiKlijent pk = new ProblemiKlijent(context);
		List<Problem> problemi = pk.dohvatiProbleme(brojStranice);
		model.put("problemi", problemi);
		model.put("brojStranice",brojStranice);
	}
	
	/**
	 * Prikaz problema icao.
	 *
	 * @param brojStranice   broj stranice
	 * @param icao  icao
	 */
	@GET
	@Path("prikazProblemaIcao")
	@View("prikazProblemaIcao.jsp")
	public void prikazProblemaIcao(@QueryParam("str") String brojStranice, @QueryParam("icao") String icao) {
		ProblemiKlijent pk = new ProblemiKlijent(context);
		List<Problem> problemi = pk.dohvatiProblemeZaIcao(icao,brojStranice);
		model.put("problemi", problemi);
		model.put("brojStranice",brojStranice);
		model.put("icao", icao);
	}
	
	/**
	 * Prikaz problema icao.
	 */
	@GET
	@Path("upisiIcaoZaProbleme")
	@View("upisiIcaoZaProbleme.jsp")
	public void prikazProblemaIcao() {
		
	}
	
	/**
	 * Obrisi probleme za icao.
	 */
	@GET
	@Path("obrisiProblemeZaIcao")
	@View("obrisiProblemeZaIcao.jsp")
	public void obrisiProblemeZaIcao() {
		
	}
	
	/**
	 * Obrisi probleme.
	 *
	 * @param icao  icao
	 */
	@GET
	@Path("obrisiProbleme")
	@View("index.jsp")
	public void obrisiProbleme(@QueryParam("icao") String icao) {
		ProblemiKlijent pk = new ProblemiKlijent(context);
		boolean uspjeh = pk.obrisiProblemeZaIcao(icao);
		
	}
	
}
