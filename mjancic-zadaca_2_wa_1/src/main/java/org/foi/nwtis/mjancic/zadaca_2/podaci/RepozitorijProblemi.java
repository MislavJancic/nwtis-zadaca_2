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

public class RepozitorijProblemi {
	private static RepozitorijProblemi instanca = null;

	private PostavkeBazaPodataka pbp = null;
	private String url = null;
	private String username = null;
	private String lozinka = null;

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

	public static RepozitorijProblemi dohvatiInstancu() {
		return instanca;
	}

	public static RepozitorijProblemi dohvatiInstancu(PostavkeBazaPodataka postavkeBazaPodataka) {
		if (instanca == null) {
			instanca = new RepozitorijProblemi(postavkeBazaPodataka);
		}
		return instanca;
	}

	public Connection spoji() {
		try {
			Connection veza = DriverManager.getConnection(url, username, lozinka);
			return veza;
		} catch (SQLException e) {
			return null;
		}
	}

	public List<Problem> dohvatiProblemZaIcao(String icao, Connection veza) {
		String upit = "SELECT * FROM AERODROMI_PROBLEMI ap WHERE ap.IDENT  = ?";
		List<Problem> problemi = new ArrayList<Problem>();
		if (veza == null)
			return null;
		try {
			PreparedStatement s = veza.prepareStatement(upit);
			s.setString(1, icao);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				String ident = rs.getString("ident");
				String description = rs.getString("description");
				Timestamp stored= rs.getTimestamp("stored");
				
				problemi.add(new Problem(ident, description, stored));
			}
			rs.close();
			return problemi;
		} catch (SQLException e) {
			Logger.getLogger(RepozitorijAerodromi.class.getName()).log(Level.SEVERE, null, e);

			return null;
		}
	}
}
