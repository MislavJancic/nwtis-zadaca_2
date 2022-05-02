package org.foi.nwtis.mjancic.zadaca_2.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Icao;

import com.google.gson.Gson;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

public class AerodromiKlijent {
	public List<Aerodrom> dajSveAerodrome() {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = 
				client.target("http://localhost:8080/mjancic-zadaca_2_wa_1/api/aerodromi");
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
	
	public List<Aerodrom> dajAerodrom(String icao) {
		Client client = ClientBuilder.newClient();
		WebTarget webResource = 
				client.target("http://localhost:8080/mjancic-zadaca_2_wa_1/api/aerodromi").path(icao);
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
	
	
}
