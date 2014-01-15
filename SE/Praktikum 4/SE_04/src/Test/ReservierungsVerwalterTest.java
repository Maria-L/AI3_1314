package Test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import db.ConnectionException;
import db.DbData;

public class ReservierungsVerwalterTest {

	private static Connection conn = null;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(DbData.CON_STRING, DbData.USER, DbData.PASS);
		} catch (SQLException e) {
			throw new ConnectionException(e);
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		conn.close();
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
