package org.foi.nwtis.mjancic.zadaca_2.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.foi.nwtis.mjancic.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.Lokacija;

public class RepozitorijAerodromi {

	private static RepozitorijAerodromi instanca = null;

	private PostavkeBazaPodataka pbp = null;
	private String url = null;
	private String username = null;
	private String lozinka = null;
	private Connection veza = null;

	private RepozitorijAerodromi(PostavkeBazaPodataka postavkeBazaPodataka) {
		pbp = postavkeBazaPodataka;
		username = pbp.getUserUsername();
		lozinka = pbp.getUserPassword();
		url = pbp.getServerDatabase() + pbp.getUserDatabase();
		try {
			Class.forName(pbp.getDriverDatabase(url));

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static RepozitorijAerodromi dohvatiInstancu() {
		return instanca;
	}

	public static RepozitorijAerodromi dohvatiInstancu(PostavkeBazaPodataka postavkeBazaPodataka) {
		if (instanca == null) {
			instanca = new RepozitorijAerodromi(postavkeBazaPodataka);
		}
		return instanca;
	}

	public boolean spoji() {
		try {
			veza = DriverManager.getConnection(url, username, lozinka);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean odspoji() {
		try {
			veza.close();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public List<Aerodrom> dohvatiSveAerodrome() {
		String upit = "SELECT a.IDENT ,a.NAME ,a.ISO_COUNTRY ,a.COORDINATES FROM airports a";
		List<Aerodrom> aerodromi = new ArrayList<Aerodrom>();
		if (veza == null)
			return null;
		try {
			PreparedStatement s = veza.prepareStatement(upit);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				String icao = rs.getString("ident");
				String naziv = rs.getString("name");
				String drzava = rs.getString("iso_country");
				String coordinates = rs.getString("coordinates");
				Lokacija lokacija = null;
				try {
					lokacija = new Lokacija(coordinates.split(",")[0].trim(), coordinates.split(",")[1].trim());
				} catch (ArrayIndexOutOfBoundsException e) {
					lokacija = new Lokacija("null", "null");
				}
				aerodromi.add(new Aerodrom(icao, naziv, drzava, lokacija));
			}
			return aerodromi;
		} catch (SQLException e) {
			Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}

	}

	public Aerodrom dohvatiAerodrom(String icao) {
		String upit = "SELECT a.IDENT ,a.NAME ,a.ISO_COUNTRY ,a.COORDINATES FROM airports a WHERE ident = ?";
		if (veza == null)
			return null;
		PreparedStatement s;
		Aerodrom aerodrom = null;
		try {
			s = veza.prepareStatement(upit);
			s.setString(1, icao);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				String naziv = rs.getString("name");
				String drzava = rs.getString("iso_country");
				String coordinates = rs.getString("coordinates");
				Lokacija lokacija = null;
				try {
					lokacija = new Lokacija(coordinates.split(",")[0].trim(), coordinates.split(",")[1].trim());
				} catch (ArrayIndexOutOfBoundsException e) {
					lokacija = new Lokacija("null", "null");
				}
				aerodrom = new Aerodrom(icao, naziv, drzava, lokacija);
			}
			return aerodrom;

		} catch (SQLException e) {
			Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}

	}

	public List<Aerodrom> dohvatiAerodromePracene() {
		String upit = "SELECT a.IDENT ,a.NAME ,a.ISO_COUNTRY ,a.COORDINATES FROM airports a, aerodromi_praceni ap WHERE a.ident=ap.ident";
		List<Aerodrom> aerodromi = new ArrayList<Aerodrom>();
		if (veza == null)
			return null;
		try {
			PreparedStatement s = veza.prepareStatement(upit);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				String icao = rs.getString("ident");
				String naziv = rs.getString("name");
				String drzava = rs.getString("iso_country");
				String coordinates = rs.getString("coordinates");
				Lokacija lokacija = null;
				try {
					lokacija = new Lokacija(coordinates.split(",")[0].trim(), coordinates.split(",")[1].trim());
				} catch (ArrayIndexOutOfBoundsException e) {
					lokacija = new Lokacija("null", "null");
				}
				aerodromi.add(new Aerodrom(icao, naziv, drzava, lokacija));
			}
			return aerodromi;
		} catch (SQLException e) {
			Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}

	}

}
