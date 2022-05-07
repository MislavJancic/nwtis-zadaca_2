package org.foi.nwtis.mjancic.zadaca_2.slusaci;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.foi.nwtis.mjancic.vjezba_03.konfiguracije.KonfiguracijaBIN;
import org.foi.nwtis.mjancic.vjezba_03.konfiguracije.KonfiguracijaJSON;
import org.foi.nwtis.mjancic.vjezba_03.konfiguracije.KonfiguracijaTXT;
import org.foi.nwtis.mjancic.vjezba_03.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.mjancic.vjezba_06.konfiguracije.bazaPodataka.KonfiguracijaBP;
import org.foi.nwtis.mjancic.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.mjancic.zadaca_2.dretve.PreuzimanjeRasporedaAerodroma;
import org.foi.nwtis.mjancic.zadaca_2.podaci.RepozitorijAerodromi;
import org.foi.nwtis.mjancic.zadaca_2.podaci.RepozitorijProblemi;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Klasa SlusacAplikacije.
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

	/** naziv datoteke dnevnika. */
	String nazivDatotekeDnevnika = "";
	
	/** vrijeme pocetka rada. */
	long vrijemePocetkaRada;
	
	/** vrijeme kraja rada. */
	long vrijemeKrajaRada;
	
	/** pra. */
	PreuzimanjeRasporedaAerodroma pra;

	/**
	 * Instancira novi slusac aplikacije.
	 */
	public SlusacAplikacije() {

	}

	/**
	 * Context inicijaliziran.
	 *
	 * @param sce the sce
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		String nazivDatoteke = context.getInitParameter("konfiguracija");
		String putanja = context.getRealPath("/WEB-INF") + File.separator;
		nazivDatoteke = putanja + nazivDatoteke;

		System.out.println(nazivDatoteke);

		KonfiguracijaBP konfig = new PostavkeBazaPodataka(nazivDatoteke);
		try {
			konfig.ucitajKonfiguraciju();
		} catch (NeispravnaKonfiguracija e) {
			e.printStackTrace();
			return;
		}

		KonfiguracijaBIN kb = new KonfiguracijaBIN("NWTiS.db.config_2.bin");

		try {
			this.nazivDatotekeDnevnika = konfig.dajPostavku("dnevnik.datoteka");
			nazivDatotekeDnevnika.hashCode();
		} catch (NullPointerException e) {
			System.out.println("Greška kod učitavanja postavke naziva datoteke dnevnika. Zadani naziv je \"NWTiS_dnevnik.txt\"");
			nazivDatotekeDnevnika = "NWTiS_dnevnik.txt";
		}

    	try {
    		kb.postavke = konfig.dajSvePostavke();
			kb.spremiKonfiguraciju();
			KonfiguracijaTXT kt = new KonfiguracijaTXT("NWTiS.db.config_2.txt");
	    	kt.postavke = konfig.dajSvePostavke();
	    	kt.spremiKonfiguraciju();
	    	KonfiguracijaJSON kj = new KonfiguracijaJSON("NWTiS.db.config_2.json");
	    	kj.postavke = konfig.dajSvePostavke();
	    	kj.spremiKonfiguraciju();
		} catch (NeispravnaKonfiguracija e) {
			e.printStackTrace();
		}

		context.setAttribute("postavke", konfig);
		RepozitorijAerodromi ra = RepozitorijAerodromi.dohvatiInstancu((PostavkeBazaPodataka) konfig);
		RepozitorijProblemi rp = RepozitorijProblemi.dohvatiInstancu((PostavkeBazaPodataka) konfig);
		System.out.println("Postavke učitane!");

		pra = new PreuzimanjeRasporedaAerodroma(konfig);
		pra.start();
		vrijemePocetkaRada = System.currentTimeMillis();
		ServletContextListener.super.contextInitialized(sce);
	}

	/**
	 * Context uništen.
	 *
	 * @param sce servlet context event
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		pra.interrupt();
		vrijemeKrajaRada = System.currentTimeMillis();
		
		PrintWriter pw;
		try {
			pw = new PrintWriter(nazivDatotekeDnevnika, "UTF-8");
			pw.println("Server radio od "+vrijemePocetkaRada+" do "+ vrijemeKrajaRada);
			pw.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.out.println("Doslo je do greske prilikom pisanja datoteke dnevnika. Poruka: "+e.getMessage());
		}
		
		
		ServletContext context = sce.getServletContext();
		context.removeAttribute("postavke");
		System.out.println("Postavke obrisane!");
		ServletContextListener.super.contextDestroyed(sce);
	}

}
