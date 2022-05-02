package org.foi.nwtis.mjancic.zadaca_2.dretve;

import java.util.ArrayList;
import java.util.List;

import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;

public class PreuzimanjeRasporedaAerodroma extends Thread {

	private int preuzimanjeOd;
	private int preuzimanjeDo;
	private int preuzimanjeVrijeme;
	private int vrijemePauza;
	private int vrijemeCiklusa;
	private int vrijemeObrade;
	private String korime;
	private String lozinka;
	private OSKlijent osKlijent;

	@Override
	public synchronized void start() {
		// TODO preuzmi podatke iz konf datoteke
		this.preuzimanjeOd = 1648764000;
		this.preuzimanjeDo = 1648850400;
		this.preuzimanjeVrijeme = 6 * 60 * 60;
		this.vrijemePauza = 20;
		this.vrijemeCiklusa = 300 * 1000;
		this.vrijemeObrade = this.preuzimanjeOd;

		this.korime = "XXXX";
		this.lozinka = "yyyy";

		this.osKlijent = new OSKlijent(korime, lozinka);

		super.start();
	}

	@Override
	public void run() {
		List<Aerodrom> aerodromi = new ArrayList<>();
		// TODO dohvatiti iz baze podatka tablica AERODROMI_PRACENI
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

		while (this.vrijemeObrade < this.preuzimanjeDo) {
			for (Aerodrom a : aerodromi) {
				System.out.println("Polasci s aerodroma: " + a.getIcao());
				List<AvionLeti> avioniPolasci;
				try {
					avioniPolasci = osKlijent.getDepartures(a.getIcao(), 
													this.preuzimanjeOd, this.preuzimanjeDo);
					if (avioniPolasci != null) {
						System.out.println("Broj letova: " + avioniPolasci.size());
						for (AvionLeti avion : avioniPolasci) {
							//TODO spremi u bazu AERODROMI_POLASCI
							System.out.println("Avion: " + avion.getIcao24() 
											+ " Odredište: " + avion.getEstArrivalAirport());
						}
					}
				} catch (NwtisRestIznimka e) {
					e.printStackTrace();
				}
				System.out.println("Dolasci na aerodrom: " + a.getIcao());
				List<AvionLeti> avioniDolasci;
				try {
					avioniDolasci = osKlijent.getArrivals(a.getIcao(), 
							this.preuzimanjeOd, this.preuzimanjeDo);
					if (avioniDolasci != null) {
						System.out.println("Broj letova: " + avioniDolasci.size());
						for (AvionLeti avion : avioniDolasci) {
							//TODO spremi u bazu AERODROMI_DOLASCI
							System.out.println("Avion: " + avion.getIcao24() 
											+ " Odredište: " + avion.getEstDepartureAirport());
						}
					}
				} catch (NwtisRestIznimka e) {
					e.printStackTrace();
				}
				//TODO mala pauza
			}
			//TODO onaj cijeli izračun za vrijeme spavanja
			long korekcija = 0;
			long vrijemeSpavanja = vrijemeCiklusa-korekcija;
			try {
				sleep(vrijemeSpavanja);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void interrupt() {
		super.interrupt();
	}

}
