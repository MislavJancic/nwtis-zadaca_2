/*
 * 
 */
package org.foi.nwtis.mjancic.vjezba_03.konfiguracije;

import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * Apstraktna klasa za rad s postavkama iz konfiguracijske datoteke Implementira
 * dio mentoda iz sučelja Konfiguracija.
 */
public abstract class KonfiguracijaApstraktna implements Konfiguracija {

	/** naziv datoteke konfiguracije. */
	protected String nazivDatoteke;

	/** kolekcija postavki. */
	public Properties postavke;

	/**
	 * Konstruktor klase.
	 *
	 * @param nazivDatoteke naziv datoteke
	 */
	public KonfiguracijaApstraktna(String nazivDatoteke) {
		this.nazivDatoteke = nazivDatoteke;
		this.postavke = new Properties();
	}

	/**
	 * Učitava konfiguraciju pod danim nazivom datoteke.
	 *
	 * @param nazivDatoteke naziv datoteke
	 * @throws NeispravnaKonfiguracija ako tip nije podržan ili ne postoji datoteka
	 *                                 konfiguracije
	 */
	public abstract void ucitajKonfiguraciju(String nazivDatoteke) throws NeispravnaKonfiguracija;

	/**
	 * Spremi konfiguraciju.
	 *
	 * @param datoteka the datoteka
	 * @throws NeispravnaKonfiguracija ako tip nije podržan ili se javi problem kod
	 *                                 spremanja datoteke konfiguracije
	 */
	public abstract void spremiKonfiguraciju(String datoteka) throws NeispravnaKonfiguracija;

	/**
	 * Sprema konfiguraciju.
	 *
	 * @throws NeispravnaKonfiguracija ako se javi problem kod spremanja datoteke
	 *                                 konfiguracije
	 */
	public void ucitajKonfiguraciju() throws NeispravnaKonfiguracija {
		this.ucitajKonfiguraciju(this.nazivDatoteke);
	}

	/**
	 * Sprema konfiguraciju pod danim nazivom datoteke.
	 *
	 * @throws NeispravnaKonfiguracija ako se javi problem kod spremanja datoteke
	 *                                 konfiguracije
	 */
	public void spremiKonfiguraciju() throws NeispravnaKonfiguracija {
		this.spremiKonfiguraciju(this.nazivDatoteke);
	}

	/**
	 * Kreira objekt konfiguracije i sprema u datoteku pod zadanim nazivom.
	 *
	 * @param nazivDatoteke the naziv datoteke
	 * @return objekt konfiguracije bez postavki
	 * @throws NeispravnaKonfiguracija ako tip konfiguracije nije podržan ili je
	 *                                 došlo do pogreške kod spremanja u datoteku
	 */
	public static Konfiguracija kreirajKonfiguraciju(String nazivDatoteke) throws NeispravnaKonfiguracija {
		Konfiguracija konfig = dajKonfiguraciju(nazivDatoteke);
		konfig.spremiKonfiguraciju();
		return konfig;
	}

	/**
	 * Kreira objekt konfiguraciju i u njega učitava datoteku postavki zadanog
	 * naziva.
	 *
	 * @param nazivDatoteke naziv datoteke
	 * @return objekt konfiguracije s postavkama
	 * @throws NeispravnaKonfiguracija ako tip konfiguracije nije podržan ili
	 *                                 datoteka zadanog naziva ne postoji ili je
	 *                                 došlo do pogreške kod čitanja datoteke
	 */
	public static Konfiguracija preuzmiKonfiguraciju(String nazivDatoteke) throws NeispravnaKonfiguracija {
		Konfiguracija konfig = dajKonfiguraciju(nazivDatoteke);
		konfig.ucitajKonfiguraciju();
		return konfig;
	}

	/**
	 * Vraća objekt konfiguracije.
	 *
	 * @param nazivDatoteke naziv datoteke
	 * @return objekt konfiguracije
	 * @throws NeispravnaKonfiguracija ako tip konfiguracije nije podržan
	 */
	public static Konfiguracija dajKonfiguraciju(String nazivDatoteke) throws NeispravnaKonfiguracija {
		String tip = Konfiguracija.dajTipKonfiguracije(nazivDatoteke);
		return switch (tip) {
		case KonfiguracijaTXT.TIP -> new KonfiguracijaTXT(nazivDatoteke);
		case KonfiguracijaXML.TIP -> new KonfiguracijaXML(nazivDatoteke);
		case KonfiguracijaBIN.TIP -> new KonfiguracijaBIN(nazivDatoteke);
		case KonfiguracijaJSON.TIP -> new KonfiguracijaJSON(nazivDatoteke);
		default -> throw new NeispravnaKonfiguracija("Datoteka: '" + nazivDatoteke + "' nema podržani tip datoteke.");
		};
	}
	
	/**
	 * Daj sve postavke.
	 *
	 * @return the properties
	 */
	public Properties dajSvePostavke() {
		return this.postavke;
	}

	/**
	 * Obrisi sve postavke.
	 *
	 * @return true, if successful
	 */
	public boolean obrisiSvePostavke() {
		if(!this.postavke.isEmpty()) {
			this.postavke.clear();
			return true;
		}
		return false;
	}

	/**
	 * Daj postavku.
	 *
	 * @param kljuc the kljuc
	 * @return the string
	 */
	public String dajPostavku(String kljuc) {
		return this.postavke.getProperty(kljuc);
	}

	/**
	 * Spremi postavku.
	 *
	 * @param kljuc the kljuc
	 * @param vrijednost the vrijednost
	 * @return true, if successful
	 */
	public boolean spremiPostavku(String kljuc, String vrijednost) {
		if(this.postavke.containsKey(kljuc))
			return false;
		else {
			this.postavke.setProperty(kljuc, vrijednost);
			return true;
		}
	}

	/**
	 * Azuriraj postavku.
	 *
	 * @param kljuc the kljuc
	 * @param vrijednost the vrijednost
	 * @return true, if successful
	 */
	public boolean azurirajPostavku(String kljuc, String vrijednost) {
		if(this.postojiPostavka(kljuc)) {
			this.postavke.setProperty(kljuc, vrijednost);
			return true;
		}
		return false;
	}

	/**
	 * Postoji postavka.
	 *
	 * @param kljuc the kljuc
	 * @return true, if successful
	 */
	public boolean postojiPostavka(String kljuc) {
		return this.postavke.containsKey(kljuc);
	}

	/**
	 * Obrisi postavku ako postoji.
	 *
	 * @param kljuc postavke
	 * @return vraća true, ako je postavka obrisana u  protivnom false
	 */
	public boolean obrisiPostavku(String kljuc) {
		if(this.postavke.containsKey(kljuc)) {
			this.postavke.remove(kljuc);
			return true;
		}
		
		return false;
	}
}

