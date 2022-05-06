package org.foi.nwtis.mjancic.zadaca_2.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//https://www.baeldung.com/java-connection-pooling
public class PoolVeza {
	private String url;
	private String user;
	private String password;
	private List<Connection> poolVeza;
	private List<Connection> koristeneVeze = new ArrayList<>();
	private static int INICIJALNI_BROJ_VEZA = 10;

	public static PoolVeza create(String url, String user, String password) throws SQLException {

		List<Connection> pool = new ArrayList<>(INICIJALNI_BROJ_VEZA);
		for (int i = 0; i < INICIJALNI_BROJ_VEZA; i++) {
			pool.add(stvoriVezu(url, user, password));
		}
		return new PoolVeza(url, user, password, pool);
	}

	public PoolVeza(String url, String user, String password, List<Connection> poolVeza) {
		super();
		this.url = url;
		this.user = user;
		this.password = password;
		this.poolVeza = poolVeza;
	}

	public Connection dohvativezu() {
		Connection connection = poolVeza.remove(poolVeza.size() - 1);
		koristeneVeze.add(connection);
		return connection;
	}

	public boolean otpustiVezu(Connection connection) {
		poolVeza.add(connection);
		return koristeneVeze.remove(connection);
	}

	private static Connection stvoriVezu(String url, String user, String password) throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	public int getSize() {
		return poolVeza.size() + koristeneVeze.size();
	}

}
