package org.foi.nwtis.mjancic.zadaca_2.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.foi.nwtis.mjancic.vjezba_03.konfiguracije.Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Icao;
import org.foi.nwtis.rest.podaci.AvionLeti;

import com.google.gson.Gson;

import jakarta.servlet.ServletContext;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Klasa AerodromiKlijent.
 */
public class AerodromiKlijent {

	/** context. */
	private ServletContext context;
	
	/** konfig. */
	Konfiguracija konfig;

	/**
	 * Instancira novi aerodromi klijent.
	 *
	 * @param context  context
	 */
	public AerodromiKlijent(ServletContext context) {
		this.context = context;
		konfig = (Konfiguracija) context.getAttribute("postavke");
	}

	/**
	 * Daj pracene aerodrome.
	 *
	 * @param brojStranice broj stranice
	 * @return lista aerodroma
	 */
	public List<Aerodrom> dajPraceneAerodrome(String brojStranice) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target(konfig.dajPostavku("adresa.wa_1") + "/aerodromi")
				.queryParam("str", brojStranice).queryParam("preuzimanje", 1)
				.queryParam("br", konfig.dajPostavku("stranica.brojRedova"));
		System.out.println("ADRESA WA1 " + webResource.toString());
		Response restOdgovor = webResource.request().header("Accept", "application/json").get();
		List<Aerodrom> aerodromi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			aerodromi = new ArrayList<>();
			aerodromi.addAll(Arrays.asList(gson.fromJson(odgovor, Aerodrom[].class)));
		}
		return aerodromi;
	}

	/**
	 * Daj sve aerodrome.
	 *
	 * @param brojStranice broj stranice
	 * @return lista aerodroma
	 */
	public List<Aerodrom> dajSveAerodrome(String brojStranice) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target(konfig.dajPostavku("adresa.wa_1") + "/aerodromi")
				.queryParam("str", brojStranice).queryParam("br", konfig.dajPostavku("stranica.brojRedova"));
		System.out.println("ADRESA WA1 " + webResource.toString());
		Response restOdgovor = webResource.request().header("Accept", "application/json").get();
		List<Aerodrom> aerodromi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			aerodromi = new ArrayList<>();
			aerodromi.addAll(Arrays.asList(gson.fromJson(odgovor, Aerodrom[].class)));
		}
		return aerodromi;
	}

	/**
	 * Daj aerodrom.
	 *
	 * @param icao icao
	 * @return aerodrom
	 */
	public Aerodrom dajAerodrom(String icao) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target(konfig.dajPostavku("adresa.wa_1") + "/aerodromi").path(icao);
		System.out.println("ADRESA WA1 " + webResource.toString());
		Response restOdgovor = webResource.request().header("Accept", "application/json").get();
		Aerodrom a = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			System.out.println("ODGOVOR DAJAERODROM: " + odgovor);
			Gson gson = new Gson();

			a = gson.fromJson(odgovor, Aerodrom.class);
		}
		return a;
	}

	/**
	 * Daj dolaske.
	 *
	 * @param icao icao
	 * @param dan dan
	 * @param brojStranice broj stranice
	 * @return lista letova
	 */
	public List<AvionLeti> dajDolaske(String icao, String dan, String brojStranice) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target(konfig.dajPostavku("adresa.wa_1") + "/aerodromi/" + icao + "/dolasci")
				.queryParam("dan", dan).queryParam("str", brojStranice)
				.queryParam("br", konfig.dajPostavku("stranica.brojRedova"));
		System.out.println("ADRESA WA1 " + webResource.toString());
		Response restOdgovor = webResource.request().header("Accept", "application/json").get();
		List<AvionLeti> letovi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			letovi = new ArrayList<>();
			letovi.addAll(Arrays.asList(gson.fromJson(odgovor, AvionLeti[].class)));
		}
		return letovi;
	}

	/**
	 * Daj polaske.
	 *
	 * @param icao icao
	 * @param dan dan
	 * @param brojStranice broj stranice
	 * @return lista letova
	 */
	public List<AvionLeti> dajPolaske(String icao, String dan, String brojStranice) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target(konfig.dajPostavku("adresa.wa_1") + "/aerodromi/" + icao + "/polasci")
				.queryParam("dan", dan).queryParam("str", brojStranice)
				.queryParam("br", konfig.dajPostavku("stranica.brojRedova"));
		System.out.println("ADRESA WA1 " + webResource.toString());
		Response restOdgovor = webResource.request().header("Accept", "application/json").get();
		List<AvionLeti> letovi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			letovi = new ArrayList<>();
			letovi.addAll(Arrays.asList(gson.fromJson(odgovor, AvionLeti[].class)));
		}
		return letovi;
	}

	/**
	 * Dodaj aerodrom za pracenje.
	 *
	 * @param icao icao
	 * @return true, ako uspije
	 */
	public boolean dodajAerodromZaPracenje(String icao) {
		if(icao==null) return false;
		if (icao.length() < 2)
			return false;
		JsonIcao jsonIcao = new JsonIcao(icao);
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target(konfig.dajPostavku("adresa.wa_1") + "/aerodromi/");
		Response odg = webResource.request().header("Accept", "application/json").method("POST", Entity.json(jsonIcao));
		if (odg.getStatus() == 200)
			return true;
		return false;
	}

	/**
	 * klasa JsonIcao za JSON.
	 */
	@XmlRootElement
	public class JsonIcao {
		
		/** icao. */
		@XmlElement
		public String icao;

		/**
		 * Instancira novi json icao.
		 *
		 * @param icao icao
		 */
		public JsonIcao(String icao) {
			this.icao = icao;
		}

		/**
		 * dohvaca icao.
		 *
		 * @return icao
		 */
		public String getIcao() {
			return icao;
		}

		/**
		 * Sets icao.
		 *
		 * @param icao  icao
		 */
		public void setIcao(String icao) {
			this.icao = icao;
		}
		
	}

}
