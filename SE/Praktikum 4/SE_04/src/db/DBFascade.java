package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBFascade {

	private Connection conn;
	
	public DBFascade()throws ConnectionException {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(DbData.CON_STRING, DbData.USER, DbData.PASS);
		} catch (SQLException e) {
			throw new ConnectionException(e);
		}
	}
	
	public Connection getConn() {
		return conn;
	}

}
