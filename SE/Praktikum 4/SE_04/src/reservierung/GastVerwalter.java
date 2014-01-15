package reservierung;

import db.ConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GastVerwalter {

	//Konstruktor
	public GastVerwalter() {

	}
	
//	public GastVerwalter getGastVerwalter() {
//		return new GastVerwalter();
//	}

	/*
	 * Der Konstrukor der nach oben weitergegeben wird ohne Stammkunde
	 */
	public Gast neuerGast(int num, String name, EmailTyp mail) {
		return new Gast(num, name, mail);
	}
	/*
	 * Der Konstruktor für die Datenbank mit Stammkunde
	 */
	public Gast neuerGast(int num, String name, EmailTyp mail, int istStammKunde) {
		return new Gast(num, name, mail, istStammKunde);
	}

	//Getter Anfang
	public String getName(Gast gast) {
		return gast.getName();
	}

	public void setStammkunde(Gast gast) {
		gast.setStammkunde();
	}
	//Getter Ende
	
	//Methoden Anfang
	/*
	 * Holt eine Liste aller Gäste aus der Datenbank
	 */
	public List<Gast> getAllGast(Connection conn) throws ConnectionException {
		try {
			List<Gast> gastListe = new ArrayList<Gast>();
			Statement stmt = conn.createStatement();
			String findAllGastSQL = "SELECT gastID, gastName, emailName, emailServer, emaildomain, istStammKunde FROM gast";
			ResultSet rset = stmt.executeQuery(findAllGastSQL);
			while (rset.next()) {
				EmailTyp email = new EmailTyp(rset.getString("emailName"),
						rset.getString("emailServer"),
						rset.getString("emailDomain"));
				Gast gast = neuerGast(rset.getInt("gastID"),
						rset.getString("gastName"), email,
						rset.getInt("istStammKunde"));
				gastListe.add(gast);
			}
			return gastListe;
		} catch (Exception e) {
			throw new ConnectionException(e);
		}
	}

	/*
	 * Speichert den übergebenen Gast in die Datenbank
	 * Hier erfolgt keinerlei Verifizierung
	 */
	public void speichereGast(Gast gast, Connection conn)throws ConnectionException {
		try {
			String newGastSQL = "INSERT INTO GAST (gastID, gastName, emailName, emailServer, emailDomain, istStammkunde) VALUES ("
					+ +gast.getNr()
					+ ", '"
					+ gast.getName()
					+ "', '"
					+ gast.getEmail().getName()
					+ "', '"
					+ gast.getEmail().getServer()
					+ "', '"
					+ gast.getEmail().getDomain()
					+ "', "
					+ gast.getStammkunde() 
					+ ")";

			PreparedStatement speichereGast = conn.prepareStatement(newGastSQL);
			speichereGast.execute();

		} catch (SQLException e) {
			throw new ConnectionException(e);
		}
	}

	/*
	 * Sucht in der Datenbank nach dem Gast mit dem übergebenen Namen
	 * und liefert ihn zurück
	 * 
	 * Bei nicht finden wird ein Fehler geworfen
	 */
	public Gast sucheGastNachName(String name, List<Gast> gastList) {
		Gast akku = null;

		for (Gast g : gastList) {
			if (g.getName().equals(name)) {
				akku = g;
				break;
			}
		}
		if (akku == null) {
			throw new IllegalArgumentException("Gast nicht gefunden");
		}
		return akku;
	}

	/*
	 * Sucht in der Datenbank nach einem Gast mit der übergebenen ID
	 * 
	 * Bei nicht finden wird ein Fehler geworfen
	 */
	public Gast sucheGastNachNr(int nr, List<Gast> gastList) {
		Gast akku = null;

		for (Gast g : gastList) {
			if (g.getNr() == nr) {
				akku = g;
				break;
			}
		}
		if (akku == null) {
			throw new IllegalArgumentException("Gast nicht gefunden");
		}
		return akku;
	}

	/*
	 * Sucht in der Datenbank nach dem Gast mit der übergebenen ID
	 * setzt ihn auf Stammkunde und updated den Datenbank eintrag auf Stammkunde
	 */
	public void markiereGastStammkunde(int nr, Connection conn) throws ConnectionException {
		try{
		Gast gast = sucheGastNachNr(nr, getAllGast(conn));
		gast.setStammkunde();
		String updateGastSQL = "UPDATE GAST SET istStammkunde = "+gast.getStammkunde()+"  WHERE gastID = " +nr;

		PreparedStatement speichereGast = conn.prepareStatement(updateGastSQL);
		speichereGast.execute();

		} catch (SQLException e) {
			throw new ConnectionException(e);
		}
	}
}
