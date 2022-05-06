package org.foi.nwtis.mjancic.zadaca_2.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.foi.nwtis.mjancic.vjezba_03.konfiguracije.Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Icao;

import com.google.gson.Gson;

import jakarta.servlet.ServletContext;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;


public class AerodromiKlijent {
	
	private ServletContext context;
	Konfiguracija konfig;
	public AerodromiKlijent(ServletContext context) {
		this.context = context;
		konfig = (Konfiguracija)context.getAttribute("postavke");
	}
	
	public List<Aerodrom> dajSveAerodrome() {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = 
				client.target(konfig.dajPostavku("adresa.wa_1")+"/aerodromi");
		System.out.println("ADRESA WA1 "+webResource.toString());
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json")
				.get();
		List<Aerodrom> aerodromi = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
			aerodromi = new ArrayList<>();
			aerodromi.addAll(Arrays.asList(gson.fromJson(odgovor, Aerodrom[].class)));
		}
		return aerodromi;
	}
	
	public Aerodrom dajAerodrom(String icao) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = 
				client.target(konfig.dajPostavku("adresa.wa_1")+"/aerodromi").path(icao);
		System.out.println("ADRESA WA1 "+webResource.toString());
		Response restOdgovor = webResource.request()
				.header("Accept", "application/json")
				.get();
		Aerodrom a = null;
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			Gson gson = new Gson();
		
			a = gson.fromJson(odgovor, Aerodrom.class);
		}
		return a;
	}
	
	
}
