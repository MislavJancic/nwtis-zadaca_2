package org.foi.nwtis.mjancic.zadaca_2.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.foi.nwtis.mjancic.vjezba_06.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.Lokacija;

/**
 * Klasa RepozitorijProblemi.
 */
public class RepozitorijProblemi {

	/** instanca. */
	private static RepozitorijProblemi instanca = null;

	/** Postavke. */
	private PostavkeBazaPodataka pbp = null;

	/** url servera. */
	private String url = null;

	/** korisnicko ime. */
	private String username = null;

	/** lozinka. */
	private String lozinka = null;

	/**
	 * Instantiates a new repozitorij problemi.
	 *
	 * @param postavkeBazaPodataka the postavke baza podataka
	 */
	private RepozitorijProblemi(PostavkeBazaPodataka postavkeBazaPodataka) {
		pbp = postavkeBazaPodataka;
		username = pbp.getAdminUsername();
		lozinka = pbp.getAdminPassword();
		url = pbp.getServerDatabase() + pbp.getAdminDatabase();
		try {
			Class.forName(pbp.getDriverDatabase(url));

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Dohvati instancu.
	 *
	 * @return repozitorij problemi
	 */
	public static RepozitorijProblemi dohvatiInstancu() {
		return instanca;
	}

	/**
	 * Dohvati instancu.
	 *
	 * @param postavkeBazaPodataka the postavke baza podataka
	 * @return the repozitorij problemi
	 */
	public static RepozitorijProblemi dohvatiInstancu(PostavkeBazaPodataka postavkeBazaPodataka) {
		if (instanca == null) {
			instanca = new RepozitorijProblemi(postavkeBazaPodataka);
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
	 * Dohvati problem za icao.
	 *
	 * @param icao   icao
	 * @param veza   veza
	 * @param limit  limit
	 * @param offset offset
	 * @return lista problema
	 */
	public List<Problem> dohvatiProblemZaIcao(String icao, Connection veza, int limit, int offset) {
		String upit = "SELECT * FROM AERODROMI_PROBLEMI ap WHERE ap.IDENT  = ?";
		List<Problem> problemi = new ArrayList<Problem>();
		if (veza == null)
			return null;
		try {
			upit += " LIMIT ? OFFSET ?";
			PreparedStatement s = veza.prepareStatement(sqlKonverzija(upit));
			s.setInt(2, limit);
			s.setInt(3, offset);
			s.setString(1, icao);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				String ident = rs.getString("ident");
				String description = rs.getString("description");
				Timestamp stored = rs.getTimestamp("stored");

				problemi.add(new Problem(ident, description, stored));
			}
			rs.close();
			return problemi;
		} catch (SQLException e) {
			Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);

			return null;
		}
	}

	/**
	 * Obrisi probleme za icao.
	 *
	 * @param icao icao
	 * @param veza veza
	 * @return the broj obrisanih redova
	 */
	public int obrisiProblemeZaIcao(String icao, Connection veza) {
		String upit = "DELETE FROM PUBLIC.PUBLIC.AERODROMI_PROBLEMI WHERE IDENT  =  ?";
		if (veza == null)
			return -1;
		PreparedStatement s;
		try {
			s = veza.prepareStatement(sqlKonverzija(upit));
			s.setString(1, icao);

			int r = s.executeUpdate();

			return r;

		} catch (SQLException e) {
			Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);

			return -1;
		}
	}

	/**
	 * Dohvati probleme.
	 *
	 * @param veza   veza
	 * @param limit  limit
	 * @param offset offset
	 * @return lista problema
	 */
	public List<Problem> dohvatiProbleme(Connection veza, int limit, int offset) {
		String upit = "SELECT * FROM AERODROMI_PROBLEMI";
		List<Problem> problemi = new ArrayList<Problem>();
		if (veza == null)
			return null;
		try {
			upit += " LIMIT ? OFFSET ?";
			PreparedStatement s = veza.prepareStatement(sqlKonverzija(upit));
			s.setInt(1, limit);
			s.setInt(2, offset);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				String ident = rs.getString("ident");
				String description = rs.getString("description");
				Timestamp stored = rs.getTimestamp("stored");

				problemi.add(new Problem(ident, description, stored));
			}
			rs.close();
			return problemi;
		} catch (SQLException e) {
			Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);

			return null;
		}
	}

	/**
	 * Sql konverzija za slučaj korištenja MySQL zbog naziva atributa tablica
	 * "STORED" koji je keyword u MySQL-u.
	 *
	 * @param upit upit
	 * @return provjereni ili popravljeni upit
	 */
	private String sqlKonverzija(String upit) {
		String vrati = "";
		if (url.toLowerCase().contains("mysql")) {
			if (upit.contains("stored")) {
				vrati = upit.replaceAll("stored", "`stored`");
			}
			if (upit.contains("STORED")) {
				vrati = upit.replaceAll("STORED", "`STORED`");
			}
		} else {
			vrati = upit;
		}
		System.out.println("DB URL " + url);
		System.out.println("UPIT KONVERT:" + vrati);

		return vrati;
	}
}
