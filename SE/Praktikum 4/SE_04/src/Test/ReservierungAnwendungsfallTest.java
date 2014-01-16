package Test;

import static org.junit.Assert.*;

import gast.EmailTyp;
import gast.Gast;
import gast.GastAnwendungsfall;
import ireservierung.IGastAnwendungsfall;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import mocks.Entity_ID_Mock;
import mocks.GastAnwendungsfallMock;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import reservierung.Reservierung;
import reservierung.ReservierungAnwendungsfall;
import reservierung.ReservierungsVerwalter;
import reservierung.Zusatzleistung;
import reservierung.ZusatzleistungVerwalter;
import db.ConnectionException;
import db.DbData;

public class ReservierungAnwendungsfallTest {
	static Connection conn = null;
	static GastAnwendungsfall gastAn = null;
	static ReservierungAnwendungsfall resAn = null;

	@BeforeClass
	public static void setUpBeforeClass() {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(DbData.CON_STRING, DbData.USER, DbData.PASS);
			gastAn = new GastAnwendungsfall();
			resAn = new ReservierungAnwendungsfall();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Before
	public void setUp() throws ConnectionException {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection conn = DriverManager.getConnection(DbData.CON_STRING, DbData.USER, DbData.PASS);

		} catch (SQLException e) {
			throw new ConnectionException(e);
		}
	}

	@After
	public void tearDown() throws Exception {
		conn.close();
	}

	@Test(expected = GastAnwendungsfallMock.SucessException.class)
	public void testZusatsStammgast() {
		IGastAnwendungsfall gastAn = null;
		ReservierungAnwendungsfall resAn = null;
		try {
			gastAn = new GastAnwendungsfallMock();
			resAn = new ReservierungAnwendungsfall(gastAn, new Entity_ID_Mock(1000),new Entity_ID_Mock(1000));
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		EmailTyp email = new EmailTyp("timzain", "@yahoo", ".de");
		Gast gast = gastAn.erzeugeGast(1, "Tom Tompson", email);

		assertEquals(0, gast.getStammkunde());

		Zusatzleistung sauna = resAn.erzeugeZusatzleistung("Sauna");
		Zusatzleistung vollPension = resAn.erzeugeZusatzleistung("Vollpension");
		Zusatzleistung wlan = resAn.erzeugeZusatzleistung("WLAN");

		Reservierung res = resAn.reserviereZimmer(gast.getNr(), 22);
		resAn.bucheZusatzleistung(res.getNr(), sauna.getNr());
		res = resAn.reserviereZimmer(gast.getNr(), 11);
		resAn.bucheZusatzleistung(res.getNr(), vollPension.getNr());
		res = resAn.reserviereZimmer(gast.getNr(), 66);
		resAn.bucheZusatzleistung(res.getNr(), wlan.getNr());

		gast = gastAn.sucheGastNachName("Tom Tompson");

		assertEquals(1, gast.getStammkunde());
	}

	@Test(expected = GastAnwendungsfallMock.SucessException.class)
	public void testReservierungStammgast() {
		IGastAnwendungsfall gastAn = null;
		ReservierungAnwendungsfall resAn = null;
		try {
			gastAn = new GastAnwendungsfallMock();
			resAn = new ReservierungAnwendungsfall(gastAn, new Entity_ID_Mock(500), new Entity_ID_Mock(500));
		} catch (Exception e) {
			e.printStackTrace();
		}

		EmailTyp email1 = new EmailTyp("paulchen", "@yahoo", ".de");
		Gast gast1 = gastAn.erzeugeGast(2, "Paul Port", email1);
		assertEquals(gast1.getStammkunde(), 0);

		resAn.reserviereZimmer(gast1.getNr(), 22);
		resAn.reserviereZimmer(gast1.getNr(), 11);
		resAn.reserviereZimmer(gast1.getNr(), 66);
		resAn.reserviereZimmer(gast1.getNr(), 33);
		resAn.reserviereZimmer(gast1.getNr(), 44);
	}

	// Integrationstest
	@Test
	public void integrationsTest() {
		// Zusatz in die Datenbank
		Zusatzleistung zusatz = resAn.erzeugeZusatzleistung("Sektempfang");

		// Gast in die Datenbanl
		EmailTyp email1 = new EmailTyp("tonyTob", "@yahoo", ".de");
		Gast gast = gastAn.erzeugeGast(3, "Tony Tobago", email1);

		// Reservierung in die Datenbank
		Reservierung res = resAn.reserviereZimmer(gast.getNr(), 11);
		resAn.bucheZusatzleistung(res.getNr(), zusatz.getNr());

		// Gast richtig in die Datenbank?
		Gast gast1 = gastAn.sucheGastNachName("Tony Tobago");
		assertEquals(3, gast1.getNr());
		assertEquals("Tony Tobago", gast1.getName());
		assertEquals("tonyTob", gast1.getEmail().getName());
		assertEquals("@yahoo", gast1.getEmail().getServer());
		assertEquals(".de", gast1.getEmail().getDomain());
		assertEquals(0, gast1.getStammkunde());

		// Reservierung richtig in die Datenbank?
		try {
			ReservierungsVerwalter resVer = new ReservierungsVerwalter();
			Reservierung res1 = resVer.sucheReservierungNachNr(0, resVer.getAllReservierung(conn));

			assertEquals(0, res1.getNr());
			assertEquals(11, res1.getZimmerNr());

			// Zusatz richtig in die Datenbank?
			ZusatzleistungVerwalter zuVer = new ZusatzleistungVerwalter();
			Zusatzleistung zusatz1 = zuVer.sucheZusatzleistungNachArt("Sektempfang", conn);

			assertEquals(0, zusatz.getNr());
			assertEquals("Sektempfang", zusatz1.getLeistungsArt());

		} catch (Exception e) {
			e.getStackTrace();
		}
		
		//Stammgast 
		resAn.reserviereZimmer(gast.getNr(), 22);
		resAn.reserviereZimmer(gast.getNr(), 66);
		resAn.reserviereZimmer(gast.getNr(), 33);
		resAn.reserviereZimmer(gast.getNr(), 44);
		
		Gast gast2 = gastAn.sucheGastNachName("Tony Tobago");
		assertEquals(1, gast2.getStammkunde());
	}

}
