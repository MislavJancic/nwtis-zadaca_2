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
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;


/**
 * Klasa RepozitorijAerodromi koja služi kao posrednik za rad s bazom podataka.
 */
public class RepozitorijAerodromi {

	/** The instanca. */
	private static RepozitorijAerodromi instanca = null;

	/** The pbp. */
	private PostavkeBazaPodataka pbp = null;
	
	/** The url. */
	private String url = null;
	
	/** The username. */
	private String username = null;
	
	/** The lozinka. */
	private String lozinka = null;

	/**
	 * Instancira novi repozitorij aerodromi.
	 *
	 * @param postavkeBazaPodataka postavke baza podataka
	 */
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

	/**
	 * Dohvati instancu.
	 *
	 * @return the repozitorij aerodromi
	 */
	public static RepozitorijAerodromi dohvatiInstancu() {
		return instanca;
	}

	/**
	 * Dohvati instancu.
	 *
	 * @param postavkeBazaPodataka the postavke baza podataka
	 * @return the repozitorij aerodromi
	 */
	public static RepozitorijAerodromi dohvatiInstancu(PostavkeBazaPodataka postavkeBazaPodataka) {
		if (instanca == null) {
			instanca = new RepozitorijAerodromi(postavkeBazaPodataka);
		}
		return instanca;
	}

	/**
	 * Spoji.
	 *
	 * @return veza na bazu
	 */
	public Connection spoji() {
		try {
			Connection veza = DriverManager.getConnection(url, username, lozinka);
			return veza;
		} catch (SQLException e) {
			return null;
		}
	}

	/**
	 * Dohvati sve aerodrome.
	 *
	 * @param veza  veza
	 * @param limit  limit
	 * @param offset  offset
	 * @return  lista aerodroma
	 */
	public List<Aerodrom> dohvatiSveAerodrome(Connection veza, int limit, int offset) {
		String upit = "SELECT a.IDENT ,a.NAME ,a.ISO_COUNTRY ,a.COORDINATES FROM airports a";
		List<Aerodrom> aerodromi = new ArrayList<Aerodrom>();
		if (veza == null)
			return null;
		try {
			upit+=" LIMIT ? OFFSET ?";
			PreparedStatement s = veza.prepareStatement(sqlKonverzija(upit));
			s.setInt(1, limit);
			s.setInt(2, offset);
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

	/**
	 * Dohvati aerodrom.
	 *
	 * @param icao  icao
	 * @param veza  veza
	 * @return aerodrom
	 */
	public Aerodrom dohvatiAerodrom(String icao, Connection veza) {
		String upit = "SELECT a.IDENT ,a.NAME ,a.ISO_COUNTRY ,a.COORDINATES FROM airports a WHERE ident = ?";
		if (veza == null)
			return null;
		PreparedStatement s;
		Aerodrom aerodrom = null;
		try {
			s = veza.prepareStatement(sqlKonverzija(upit));
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

	/**
	 * Dohvati aerodrome pracene.
	 *
	 * @param veza  veza
	 * @param limit limit
	 * @param offset offset
	 * @return lista aerodroma
	 */
	public List<Aerodrom> dohvatiAerodromePracene(Connection veza, int limit, int offset) {
		String upit = "SELECT a.IDENT ,a.NAME ,a.ISO_COUNTRY ,a.COORDINATES FROM airports a, AERODROMI_PRACENI ap WHERE a.ident=ap.ident";
		List<Aerodrom> aerodromi = new ArrayList<Aerodrom>();
		if (veza == null)
			spoji();
		try {
			upit+=" LIMIT ? OFFSET ?";
			PreparedStatement s = veza.prepareStatement(sqlKonverzija(upit));
			s.setInt(1, limit);
			s.setInt(2, offset);
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
	
	/**
	 * Dohvati sve aerodrome pracene.
	 *
	 * @param veza veza
	 * @return  lista aerodroma
	 */
	public List<Aerodrom> dohvatiSveAerodromePracene(Connection veza) {
		String upit = "SELECT a.IDENT ,a.NAME ,a.ISO_COUNTRY ,a.COORDINATES FROM airports a, AERODROMI_PRACENI ap WHERE a.ident=ap.ident";
		List<Aerodrom> aerodromi = new ArrayList<Aerodrom>();
		if (veza == null)
			spoji();
		try {

			PreparedStatement s = veza.prepareStatement(sqlKonverzija(upit));
		
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

	/**
	 * Dodaj aerodrom za pratiti.
	 *
	 * @param icao icao
	 * @param veza veza
	 * @return true, kad uspije
	 */
	public boolean dodajAerodromZaPratiti(String icao, Connection veza) {
		if (provjeriPostojanjeIcaoZaPratiti(icao, veza))
			return false;
		String upit = "INSERT INTO AERODROMI_PRACENI (IDENT, STORED) VALUES (?, NOW())";
		if (veza == null)
			return false;
		PreparedStatement s;
		try {
			s = veza.prepareStatement(sqlKonverzija(upit));
			s.setString(1, icao);
			int r = s.executeUpdate();

			return (true ? r > 0 : false);

		} catch (SQLException e) {
			Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);

			return false;
		}
	}

	/**
	 * Provjeri postojanje icao za pratiti.
	 *
	 * @param icao icao
	 * @param veza veza
	 * @return true, ako uspije
	 */
	private boolean provjeriPostojanjeIcaoZaPratiti(String icao, Connection veza) {
		String upit = "SELECT a.IDENT FROM airports a, AERODROMI_PRACENI ap WHERE a.ident=ap.ident";
		if (veza == null)
			return false;
		try {
			PreparedStatement s = veza.prepareStatement(sqlKonverzija(upit));
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

	/**
	 * Dohvati icao polaske.
	 *
	 * @param icao icao
	 * @param vrijeme vrijeme
	 * @param veza veza
	 * @param limit limit
	 * @param offset offset
	 * @return lista letova
	 */
	public List<AvionLeti> dohvatiIcaoPolaske(String icao, Long vrijeme, Connection veza, int limit, int offset) {
		String upit = "SELECT * FROM  AERODROMI_POLASCI ap WHERE ESTDEPARTUREAIRPORT = ?";
		List<AvionLeti> avioniLete = new ArrayList<AvionLeti>();
		if (veza == null)
			return null;
		ResultSet rs = null;
		try {
			if (vrijeme != null) {
				upit += " AND ap.FIRSTSEEN BETWEEN ? AND ?+86400";
				System.out.println("dodani upit " + vrijeme);
			}
			upit+=" LIMIT ? OFFSET ?";
			PreparedStatement s = veza.prepareStatement(sqlKonverzija(upit));
			if (vrijeme != null) {
				s.setLong(2, vrijeme);
				s.setLong(3, vrijeme);
				
				s.setInt(4, limit);
				s.setInt(5, offset);
			} else {
				s.setInt(2, limit);
				s.setInt(3, offset);
			}
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
						estArrivalAirportHorizDistance, estArrivalAirportVertDistance, departureAirportCandidatesCount,
						arrivalAirportCandidatesCount));
			}
			rs.close();
			return avioniLete;
		} catch (SQLException e) {
			Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);

			return null;
		}
	}

	/**
	 * Dohvati icao dolaske.
	 *
	 * @param icao icao
	 * @param vrijeme vrijeme
	 * @param veza veza
	 * @param limit limit
	 * @param offset offset
	 * @return lista letova
	 */
	public List<AvionLeti> dohvatiIcaoDolaske(String icao, Long vrijeme, Connection veza, int limit, int offset) {
		String upit = "SELECT * FROM  AERODROMI_DOLASCI ad WHERE ESTARRIVALAIRPORT = ? ";
		List<AvionLeti> avioniLete = new ArrayList<AvionLeti>();
		if (veza == null)
			return null;
		ResultSet rs = null;
		try {
			if (vrijeme != null) {
				upit += " AND ad.LASTSEEN BETWEEN ? AND ?+86400";
				System.out.println("dodani upit " + vrijeme);
			}
			upit+=" LIMIT ? OFFSET ?";
			PreparedStatement s = veza.prepareStatement(sqlKonverzija(upit));
			if (vrijeme != null) {
				s.setLong(2, vrijeme);
				s.setLong(3, vrijeme);
				
				s.setInt(4, limit);
				s.setInt(5, offset);
			} else {
				s.setInt(2, limit);
				s.setInt(3, offset);
			}
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
						estArrivalAirportHorizDistance, estArrivalAirportVertDistance, departureAirportCandidatesCount,
						arrivalAirportCandidatesCount));
			}
			rs.close();
			return avioniLete;
		} catch (SQLException e) {
			Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);

			return null;
		}
	}

	/**
	 * Spremi polaske.
	 *
	 * @param letovi letovi
	 * @param veza veza
	 * @return true, ako uspije
	 */
	public boolean spremiPolaske(List<AvionLeti> letovi, Connection veza) {
		String upit = "INSERT  INTO AERODROMI_POLASCI (\n" + "ICAO24 ,\n" + "FIRSTSEEN,\n" + "ESTDEPARTUREAIRPORT,\n"
				+ "LASTSEEN,\n" + "ESTARRIVALAIRPORT,\n" + "CALLSIGN,\n" + "ESTDEPARTUREAIRPORTHORIZDISTANCE,\n"
				+ "ESTDEPARTUREAIRPORTVERTDISTANCE,\n" + "ESTARRIVALAIRPORTHORIZDISTANCE,\n"
				+ "ESTARRIVALAIRPORTVERTDISTANCE,\n" + "DEPARTUREAIRPORTCANDIDATESCOUNT,\n"
				+ "ARRIVALAIRPORTCANDIDATESCOUNT,\n" + "STORED \n" + ") VALUES\n" + "(?,?,?,?,?,?,?,?,?,?,?,?,NOW());";
		if (veza == null)
			return false;
		PreparedStatement s;

		int r = 0;

		for (AvionLeti al : letovi) {
			try {
				if (al.getEstArrivalAirport() != null) {
					s = veza.prepareStatement(sqlKonverzija(upit));
					s.setString(1, al.getIcao24());
					s.setInt(2, al.getFirstSeen());
					s.setString(3, al.getEstDepartureAirport());
					s.setInt(4, al.getLastSeen());
					s.setString(5, al.getEstArrivalAirport());
					s.setString(6, al.getCallsign());
					s.setInt(7, al.getEstDepartureAirportHorizDistance());
					s.setInt(8, al.getEstDepartureAirportVertDistance());
					s.setInt(9, al.getEstArrivalAirportHorizDistance());
					s.setInt(10, al.getEstArrivalAirportVertDistance());
					s.setInt(11, al.getDepartureAirportCandidatesCount());
					s.setInt(12, al.getArrivalAirportCandidatesCount());
					r = s.executeUpdate();
				}
			} catch (SQLException e) {
				Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return true ? r > 0 : false;

	}

	/**
	 * Spremi dolaske.
	 *
	 * @param letovi letovi
	 * @param veza  veza
	 * @return true, ako uspije
	 */
	public boolean spremiDolaske(List<AvionLeti> letovi, Connection veza) {
		String upit = "INSERT INTO AERODROMI_DOLASCI (\n" + "ICAO24,\n" + "FIRSTSEEN,\n" + "ESTDEPARTUREAIRPORT,\n"
				+ "LASTSEEN,\n" + "ESTARRIVALAIRPORT,\n" + "CALLSIGN,\n" + "ESTDEPARTUREAIRPORTHORIZDISTANCE,\n"
				+ "ESTDEPARTUREAIRPORTVERTDISTANCE,\n" + "ESTARRIVALAIRPORTHORIZDISTANCE,\n"
				+ "ESTARRIVALAIRPORTVERTDISTANCE,\n" + "DEPARTUREAIRPORTCANDIDATESCOUNT,\n"
				+ "ARRIVALAIRPORTCANDIDATESCOUNT,\n" + "STORED\n" + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,NOW())";
		if (veza == null)
			return false;
		PreparedStatement s;

		int r = 0;

		for (AvionLeti al : letovi) {
			try {
				if (al.getEstDepartureAirport() != null) {
					s = veza.prepareStatement(sqlKonverzija(upit));
					s.setString(1, al.getIcao24());
					s.setInt(2, al.getFirstSeen());
					s.setString(3, al.getEstDepartureAirport());
					s.setInt(4, al.getLastSeen());
					s.setString(5, al.getEstArrivalAirport());
					s.setString(6, al.getCallsign());
					s.setInt(7, al.getEstDepartureAirportHorizDistance());
					s.setInt(8, al.getEstDepartureAirportVertDistance());
					s.setInt(9, al.getEstArrivalAirportHorizDistance());
					s.setInt(10, al.getEstArrivalAirportVertDistance());
					s.setInt(11, al.getDepartureAirportCandidatesCount());
					s.setInt(12, al.getArrivalAirportCandidatesCount());
					r = s.executeUpdate();
				}
			} catch (SQLException e) {
				Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return true ? r > 0 : false;

	}

	/**
	 * Spremi problem.
	 *
	 * @param problem problem
	 * @param veza veza
	 * @return true, ako uspije
	 */
	public boolean spremiProblem(Problem problem, Connection veza) {
		String upit = "INSERT INTO AERODROMI_PROBLEMI (IDENT,DESCRIPTION , STORED) VALUES (?,? ,NOW())";
		if (veza == null)
			return false;
		PreparedStatement s;
		try {
			s = veza.prepareStatement(sqlKonverzija(upit));
			s.setString(1, problem.ident);
			s.setString(2, problem.description);
			int r = s.executeUpdate();

			return true ? r > 0 : false;

		} catch (SQLException e) {
			Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);

			return false;
		}
	}
	
	/**
	 * Sql konverzija za slučaj korištenja MySQL zbog naziva atributa tablica "STORED" koji je keyword u MySQL-u.
	 *
	 * @param upit upit
	 * @return provjereni ili popravljeni upit
	 */
	private String sqlKonverzija(String upit) {
		String vrati="";
		if(url.toLowerCase().contains("mysql")) {
			if(upit.contains("stored")) {
				vrati=upit.replaceAll("stored", "`stored`");
			}
			if(upit.contains("STORED")) {
				vrati=upit.replaceAll("STORED", "`STORED`");
			}
		}else {
			vrati = upit;
		}
		System.out.println("DB URL "+url);
		System.out.println("UPIT KONVERT:"+vrati);
		
		return vrati;
	}

}
