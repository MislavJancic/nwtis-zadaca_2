package org.foi.nwtis.mjancic.zadaca_2.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.foi.nwtis.mjancic.vjezba_03.konfiguracije.Konfiguracija;
import org.foi.nwtis.mjancic.zadaca_2.mvc.AerodromiKlijent.JsonIcao;
import org.foi.nwtis.mjancic.zadaca_2.podaci.Problem;
import org.foi.nwtis.podaci.Aerodrom;

import com.google.gson.Gson;

import jakarta.servlet.ServletContext;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

public class ProblemiKlijent {

	Konfiguracija konfig;

	public ProblemiKlijent(ServletContext context) {

		konfig = (Konfiguracija) context.getAttribute("postavke");
	}

	public List<Problem> dohvatiProbleme(String brojStranice) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target(konfig.dajPostavku("adresa.wa_1") + "/problemi")
				.queryParam("str", brojStranice).queryParam("br", konfig.dajPostavku("stranica.brojRedova"));
		System.out.println("ADRESA WA1 " + webResource.toString());
		Response restOdgovor = webResource.request().header("Accept", "application/json").get();
		List<Problem> problemi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			problemi = new ArrayList<>();
			problemi.addAll(Arrays.asList(gson.fromJson(odgovor, Problem[].class)));
		}
	
		return problemi;
	}
	
	public List<Problem> dohvatiProblemeZaIcao(String icao, String brojStranice) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target(konfig.dajPostavku("adresa.wa_1") + "/problemi/"+icao)
				.queryParam("str", brojStranice).queryParam("br", konfig.dajPostavku("stranica.brojRedova"));
		System.out.println("ADRESA WA1 " + webResource.toString());
		Response restOdgovor = webResource.request().header("Accept", "application/json").get();
		List<Problem> problemi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			problemi = new ArrayList<>();
			problemi.addAll(Arrays.asList(gson.fromJson(odgovor, Problem[].class)));
		}
		return problemi;
	}
	
	public boolean obrisiProblemeZaIcao(String icao) {
		if(icao==null) return false;
		if (icao.length() < 2)
			return false;
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target(konfig.dajPostavku("adresa.wa_1") + "/problemi/"+icao);
		Response odg = webResource.request().header("Accept", "application/json").method("DELETE");
		if (odg.getStatus() == 200)
			return true;
		return false;
	}

}
