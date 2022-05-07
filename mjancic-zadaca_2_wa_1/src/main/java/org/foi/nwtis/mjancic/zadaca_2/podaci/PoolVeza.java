package org.foi.nwtis.mjancic.zadaca_2.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * Klasa PoolVeza. Nije implementirano njezino korištenje u zadaći 2 zbog nedostatka vremena.
 * Izvor: https://www.baeldung.com/java-connection-pooling
 */

public class PoolVeza {

	/** url. */
	private String url;

	/** Korisnikr. */
	private String user;

	/** Lozinka. */
	private String password;

	/** pool veza. */
	private List<Connection> poolVeza;

	/** koristene veze. */
	private List<Connection> koristeneVeze = new ArrayList<>();

	/** inicijalni broj veza. */
	private static int INICIJALNI_BROJ_VEZA = 10;

	/**
	 * Kreira.
	 *
	 * @param url      url servera bp
	 * @param user     korisnicko ime bp
	 * @param password lozinka bp
	 * @return the objekt veze
	 * @throws SQLException SQL exception
	 */
	public static PoolVeza create(String url, String user, String password) throws SQLException {

		List<Connection> pool = new ArrayList<>(INICIJALNI_BROJ_VEZA);
		for (int i = 0; i < INICIJALNI_BROJ_VEZA; i++) {
			pool.add(stvoriVezu(url, user, password));
		}
		return new PoolVeza(url, user, password, pool);
	}

	/**
	 * Instancira novi pool veza.
	 *
	 * @param url      url servera bp
	 * @param user     korisnicko ime bp
	 * @param password lozinka bp
	 * @param poolVeza pool veza
	 */
	public PoolVeza(String url, String user, String password, List<Connection> poolVeza) {
		super();
		this.url = url;
		this.user = user;
		this.password = password;
		this.poolVeza = poolVeza;
	}

	/**
	 * Dohvativezu.
	 *
	 * @return the connection
	 */
	public Connection dohvativezu() {
		Connection connection = poolVeza.remove(poolVeza.size() - 1);
		koristeneVeze.add(connection);
		return connection;
	}

	/**
	 * Otpusti vezu.
	 *
	 * @param connection the connection
	 * @return true, if successful
	 */
	public boolean otpustiVezu(Connection connection) {
		poolVeza.add(connection);
		return koristeneVeze.remove(connection);
	}

	/**
	 * Stvori vezu.
	 *
	 * @param url      the url
	 * @param user     the user
	 * @param password the password
	 * @return the connection
	 * @throws SQLException the SQL exception
	 */
	private static Connection stvoriVezu(String url, String user, String password) throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public int getSize() {
		return poolVeza.size() + koristeneVeze.size();
	}

}
