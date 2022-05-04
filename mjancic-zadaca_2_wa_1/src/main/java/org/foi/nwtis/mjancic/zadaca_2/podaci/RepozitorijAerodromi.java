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
import org.foi.nwtis.podaci.OdgovorAerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;
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
			rs.close();
			return aerodrom;

		} catch (SQLException e) {
			Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}

	}

	public List<Aerodrom> dohvatiAerodromePracene() {
		String upit = "SELECT a.IDENT ,a.NAME ,a.ISO_COUNTRY ,a.COORDINATES FROM airports a, AERODROMI_PRACENI ap WHERE a.ident=ap.ident";
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
			rs.close();
			return aerodromi;
		} catch (SQLException e) {
			Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}

	}

	public boolean dodajAerodromZaPratiti(String icao) {
		if (provjeriPostojanjeIcaoZaPratiti(icao))
			return false;
		String upit = "INSERT INTO AERODROMI_PRACENI (IDENT, STORED) VALUES (?, NOW())";
		if (veza == null)
			return false;
		PreparedStatement s;
		try {
			s = veza.prepareStatement(upit);
			s.setString(1, icao);
			int r = s.executeUpdate();

			return true ? r > 0 : false;

		} catch (SQLException e) {
			Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);
			return false;
		}
	}

	private boolean provjeriPostojanjeIcaoZaPratiti(String icao) {
		String upit = "SELECT a.IDENT FROM airports a, AERODROMI_PRACENI ap WHERE a.ident=ap.ident";
		if (veza == null)
			spoji();
		try {
			PreparedStatement s = veza.prepareStatement(upit);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				String ident = rs.getString("ident");

				if (ident.equals(icao)) {
					rs.close();
					return true;
				}
			}

		} catch (SQLException e) {
			Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);

		}
		return false;
	}

	public List<AvionLeti> dohvatiIcaoPolaske(String icao) {
		String upit = "SELECT * FROM  AERODROMI_POLASCI ap WHERE ICAO24 = ?";
		List<AvionLeti> avioniLete = new ArrayList<AvionLeti>();
		if (veza == null)
			spoji();
		ResultSet rs = null;
		try {
			PreparedStatement s = veza.prepareStatement(upit);
			s.setString(1, icao);
			rs = s.executeQuery();
			while (rs.next()) {
				String icao24 = rs.getString("icao24");
				int firstSeen = rs.getInt("firstseen");
				String estDepartureAirport = rs.getString("estdepartureairport");
				int lastSeen = rs.getInt("lastseen");
				String estArrivalAirport = rs.getString("estArrivalAirport");
				String callsign = rs.getString("callsign");
				int estDepartureAirportHorizDistance = rs.getInt("estDepartureAirportHorizDistance");
				int estDepartureAirportVertDistance = rs.getInt("estDepartureAirportVertDistance");
				int estArrivalAirportHorizDistance = rs.getInt("estArrivalAirportHorizDistance");
				int estArrivalAirportVertDistance = rs.getInt("estArrivalAirportVertDistance");
				int departureAirportCandidatesCount = rs.getInt("departureAirportCandidatesCount");
				int arrivalAirportCandidatesCount = rs.getInt("arrivalAirportCandidatesCount");
				avioniLete.add(new AvionLeti(icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, 
						callsign, estDepartureAirportHorizDistance, estDepartureAirportVertDistance, 
						estArrivalAirportHorizDistance, estArrivalAirportVertDistance, 
						departureAirportCandidatesCount, arrivalAirportCandidatesCount));
			}
			rs.close();
			return avioniLete;
		} catch (SQLException e) {
			Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}
	}
	public List<AvionLeti> dohvatiIcaoDolaske(String icao){
		String upit = "SELECT * FROM  AERODROMI_DOLASCI ad WHERE ICAO24 = ?";
		List<AvionLeti> avioniLete = new ArrayList<AvionLeti>();
		if (veza == null)
			spoji();
		ResultSet rs = null;
		try {
			PreparedStatement s = veza.prepareStatement(upit);
			s.setString(1, icao);
			rs = s.executeQuery();
			while (rs.next()) {
				String icao24 = rs.getString("icao24");
				int firstSeen = rs.getInt("firstseen");
				String estDepartureAirport = rs.getString("estdepartureairport");
				int lastSeen = rs.getInt("lastseen");
				String estArrivalAirport = rs.getString("estArrivalAirport");
				String callsign = rs.getString("callsign");
				int estDepartureAirportHorizDistance = rs.getInt("estDepartureAirportHorizDistance");
				int estDepartureAirportVertDistance = rs.getInt("estDepartureAirportVertDistance");
				int estArrivalAirportHorizDistance = rs.getInt("estArrivalAirportHorizDistance");
				int estArrivalAirportVertDistance = rs.getInt("estArrivalAirportVertDistance");
				int departureAirportCandidatesCount = rs.getInt("departureAirportCandidatesCount");
				int arrivalAirportCandidatesCount = rs.getInt("arrivalAirportCandidatesCount");
				avioniLete.add(new AvionLeti(icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, 
						callsign, estDepartureAirportHorizDistance, estDepartureAirportVertDistance, 
						estArrivalAirportHorizDistance, estArrivalAirportVertDistance, 
						departureAirportCandidatesCount, arrivalAirportCandidatesCount));
			}
			rs.close();
			return avioniLete;
		} catch (SQLException e) {
			Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}
	}

}
