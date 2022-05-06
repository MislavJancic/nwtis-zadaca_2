package org.foi.nwtis.mjancic.zadaca_2.dretve;

import java.sql.Connection;
import java.sql.SQLException;
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
	private boolean radi = true;
	private Connection veza = null;

	private Konfiguracija konfig;

	public PreuzimanjeRasporedaAerodroma(Konfiguracija konfig) {
		this.konfig = konfig;
		veza = RepozitorijAerodromi.dohvatiInstancu().spoji();
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

		try {
			String datumKonfig = konfig.dajPostavku("preuzimanje.od");
			Date datumOd = df.parse(datumKonfig);
			datumKonfig = konfig.dajPostavku("preuzimanje.do");
			Date datumDo = df.parse(datumKonfig);
			this.preuzimanjeOd = datumOd.getTime()/1000;
			this.preuzimanjeDo = datumDo.getTime()/1000;
			//this.preuzimanjeOd = 1648764000;
			//this.preuzimanjeDo = 1648850400;
			this.preuzimanjeVrijeme = Integer.parseInt(konfig.dajPostavku("preuzimanje.vrijeme")) * 6 * 60;
			this.vrijemePauza = Integer.parseInt(konfig.dajPostavku("preuzimanje.pauza"));
			this.vrijemeCiklusa = Integer.parseInt(konfig.dajPostavku("ciklus.vrijeme")) * 1000;
			this.ciklusKorekcija = Integer.parseInt(konfig.dajPostavku("ciklus.korekcija"));
			this.korime = konfig.dajPostavku("OpenSkyNetwork.korisnik");
			this.lozinka = konfig.dajPostavku("OpenSkyNetwork.lozinka");
			
			System.out.println("VRIJEME OD "+preuzimanjeOd);
			System.out.println("VRIJEME DO "+preuzimanjeDo);
			System.out.println("USERNAME "+korime);
			System.out.println("LOZINKA "+lozinka);
			//System.out.println("DATUM "+datumOd.toLocaleString());
			
			

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
		List<Aerodrom> aerodromi = RepozitorijAerodromi.dohvatiInstancu().dohvatiAerodromePracene(veza);
		System.out.println("VRIJEME OD "+this.preuzimanjeOd);
		System.out.println("VRIJEME DO "+this.preuzimanjeDo);
		while ((this.vrijemeObrade < this.preuzimanjeDo) && radi) {
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
			avioniDolasci = osKlijent.getArrivals(a.getIcao().trim(), this.preuzimanjeOd, this.preuzimanjeDo);
			if (avioniDolasci != null) {
				System.out.println("Broj letova: " + avioniDolasci.size());
				boolean uspjeh = RepozitorijAerodromi.dohvatiInstancu().spremiDolaske(avioniDolasci,veza);
				for (AvionLeti avion : avioniDolasci) {
					
					System.out.println("Avion: " + avion.getIcao24() + " Odredište: " + avion.getEstDepartureAirport());
				}
			}
		} catch (NwtisRestIznimka e) {
			Problem problem = new Problem(a.getIcao(),e.getMessage());
			RepozitorijAerodromi.dohvatiInstancu().spremiProblem(problem,veza);
		}
	}

	private void obradiPolaske(Aerodrom a) {
		System.out.println("Polasci s aerodroma: " + a.getIcao());
		List<AvionLeti> avioniPolasci;
		try {
			avioniPolasci = osKlijent.getDepartures(a.getIcao(), this.preuzimanjeOd, this.preuzimanjeDo);
			if (avioniPolasci != null) {
				System.out.println("Broj letova: " + avioniPolasci.size());
				boolean uspjeh = RepozitorijAerodromi.dohvatiInstancu().spremiPolaske(avioniPolasci,veza);
				for (AvionLeti avion : avioniPolasci) {
					System.out.println("Avion: " + avion.getIcao24() + " Odredište: " + avion.getEstArrivalAirport());
				}
			}
		} catch (NwtisRestIznimka e) {
			Problem problem = new Problem(a.getIcao(),e.getMessage());
			RepozitorijAerodromi.dohvatiInstancu().spremiProblem(problem,veza);
			
		}
	}

	@Override
	public void interrupt() {
		try {
			veza.close();
		} catch (SQLException e) {
			
		}
		radi = false;
		super.interrupt();
	}

}
