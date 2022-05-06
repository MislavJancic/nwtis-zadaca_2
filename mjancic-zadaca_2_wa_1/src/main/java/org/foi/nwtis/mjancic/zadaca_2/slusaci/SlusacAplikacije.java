package org.foi.nwtis.mjancic.zadaca_2.slusaci;

import java.io.File;

import org.foi.nwtis.mjancic.vjezba_03.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.mjancic.vjezba_06.konfiguracije.bazaPodataka.KonfiguracijaBP;
import org.foi.nwtis.mjancic.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.mjancic.zadaca_2.dretve.PreuzimanjeRasporedaAerodroma;
import org.foi.nwtis.mjancic.zadaca_2.podaci.RepozitorijAerodromi;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class SlusacAplikacije implements ServletContextListener {

	String nazivDatotekeDnevnika = "";
	long vrijemePocetkaRada;
	long vrijemeKrajaRada;
    public SlusacAplikacije() {
       
    }

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
    	
    	context.setAttribute("postavke", konfig);
    	RepozitorijAerodromi ra = RepozitorijAerodromi.dohvatiInstancu((PostavkeBazaPodataka) konfig);
    	System.out.println("Postavke učitane!");
    	
    	PreuzimanjeRasporedaAerodroma pra = new PreuzimanjeRasporedaAerodroma(konfig);
    	pra.start();
    	vrijemePocetkaRada = System.currentTimeMillis();
		ServletContextListener.super.contextInitialized(sce);
	}
    
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		vrijemeKrajaRada = System.currentTimeMillis();
		//TODO pisanje u datoteku denvnika
		ServletContext context = sce.getServletContext();
		context.removeAttribute("postavke");
		System.out.println("Postavke obrisane!");
		RepozitorijAerodromi ra = RepozitorijAerodromi.dohvatiInstancu();
		try {
			//ra.odspoji();
		} catch (NullPointerException e) {
			
		}
		ServletContextListener.super.contextDestroyed(sce);
	}
    
}
