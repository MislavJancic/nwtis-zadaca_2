package org.foi.nwtis.mjancic.zadaca_2.dretve;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.foi.nwtis.mjancic.vjezba_03.konfiguracije.Konfiguracija;
import org.foi.nwtis.mjancic.zadaca_2.podaci.Problem;
import org.foi.nwtis.mjancic.zadaca_2.podaci.RepozitorijAerodromi;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;

public class PreuzimanjeRasporedaAerodroma extends Thread {

	private long preuzimanjeOd;
	private long preuzimanjeDo;
	private long preuzimanjeVrijeme;
	private int vrijemePauza;
	private int vrijemeCiklusa;
	private long vrijemeObrade;
	private long ciklusKorekcija;
	private String korime;
	private String lozinka;
	private OSKlijent osKlijent;

	private Konfiguracija konfig;

	public PreuzimanjeRasporedaAerodroma(Konfiguracija konfig) {
		this.konfig = konfig;
		DateFormat df = new SimpleDateFormat("dd.mm.yyyy");

		try {
			String datumKonfig = konfig.dajPostavku("preuzimanje.od");
			Date datumOd = df.parse(datumKonfig);
			datumKonfig = konfig.dajPostavku("preuzimanje.do");
			Date datumDo = df.parse(datumKonfig);
			this.preuzimanjeOd = datumOd.getTime();
			this.preuzimanjeDo = datumDo.getTime();
			this.preuzimanjeVrijeme = Integer.parseInt(konfig.dajPostavku("preuzimanje.vrijeme")) * 6 * 60;
			this.vrijemePauza = Integer.parseInt(konfig.dajPostavku("preuzimanje.pauza"));
			this.vrijemeCiklusa = Integer.parseInt(konfig.dajPostavku("ciklus.vrijeme")) * 1000;
			this.ciklusKorekcija = Integer.parseInt(konfig.dajPostavku("ciklus.korekcija"));

		} catch (ParseException | NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public synchronized void start() {
		this.vrijemeObrade = this.preuzimanjeOd;
		this.osKlijent = new OSKlijent(korime, lozinka);

		super.start();
	}

	@Override
	public void run() {
		List<Aerodrom> aerodromi = RepozitorijAerodromi.dohvatiInstancu().dohvatiAerodromePracene();

		while (this.vrijemeObrade < this.preuzimanjeDo) {
			for (Aerodrom a : aerodromi) {
				obradiPolaske(a);
				obradiDolaske(a);
				// TODO mala pauza
			}
			// TODO onaj cijeli izračun za vrijeme spavanja
			long korekcija = 0;
			long vrijemeSpavanja = vrijemeCiklusa - korekcija;
			try {
				sleep(vrijemeSpavanja);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void obradiDolaske(Aerodrom a) {
		System.out.println("Dolasci na aerodrom: " + a.getIcao());
		List<AvionLeti> avioniDolasci;
		try {
			avioniDolasci = osKlijent.getArrivals(a.getIcao(), this.preuzimanjeOd, this.preuzimanjeDo);
			if (avioniDolasci != null) {
				System.out.println("Broj letova: " + avioniDolasci.size());
				for (AvionLeti avion : avioniDolasci) {
					if (avion.getEstDepartureAirport().equals("null")) {
						// TODO spremi probleme
					}
					System.out.println("Avion: " + avion.getIcao24() + " Odredište: " + avion.getEstDepartureAirport());
				}
			}
		} catch (NwtisRestIznimka e) {
			Problem problem = new Problem(a.getIcao(),e.getMessage());
			RepozitorijAerodromi.dohvatiInstancu().spremiProblem(problem);
		}
	}

	private void obradiPolaske(Aerodrom a) {
		System.out.println("Polasci s aerodroma: " + a.getIcao());
		List<AvionLeti> avioniPolasci;
		try {
			avioniPolasci = osKlijent.getDepartures(a.getIcao(), this.preuzimanjeOd, this.preuzimanjeDo);
			if (avioniPolasci != null) {
				System.out.println("Broj letova: " + avioniPolasci.size());
				boolean uspjeh = RepozitorijAerodromi.dohvatiInstancu().spremiPolaske(avioniPolasci);
				for (AvionLeti avion : avioniPolasci) {
					System.out.println("Avion: " + avion.getIcao24() + " Odredište: " + avion.getEstArrivalAirport());
				}
			}
		} catch (NwtisRestIznimka e) {
			Problem problem = new Problem(a.getIcao(),e.getMessage());
			RepozitorijAerodromi.dohvatiInstancu().spremiProblem(problem);
			
		}
	}

	@Override
	public void interrupt() {
		super.interrupt();
	}

}
