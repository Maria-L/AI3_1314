package reservierung;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.ConnectionException;

public class ZusatzleistungVerwalter {

	//Konstruktor
	public Zusatzleistung erzeugeZusatzleistung(int nr, String art){
		return new Zusatzleistung(nr,art);
	}
	
	//Getter
	public int getNr(Zusatzleistung zuLe){
		return zuLe.getNr();
	}
	
	public String getleistungsName(Zusatzleistung zuLe){
		return zuLe.getLeistungsArt();
	}
	
	//Anfang Methoden
	
	/*
	 * Holt alle Zusatzdienstleistungen aus der Datenbank und liefert sie in einer Liste zurück
	 */
	public List<Zusatzleistung> getAllZusatzleistung(Connection conn) throws ConnectionException {
		try {
			List<Zusatzleistung> zusatzListe = new ArrayList<Zusatzleistung>();
			Statement stmt = conn.createStatement();
			String findAllZuSQL = "SELECT zusatzNummer, leistungsArt FROM zusatzleistung";
			ResultSet rset = stmt.executeQuery(findAllZuSQL);
			while (rset.next()) {
				Zusatzleistung zusatz = erzeugeZusatzleistung(rset.getInt("zusatzNummer"), rset.getString("leistungsArt"));
				zusatzListe.add(zusatz);
			}
			return zusatzListe;
		} catch (Exception e) {
			throw new ConnectionException(e);
		}
	}
	/*
	 * Speichert die übergebene Zusatzdienstleistung in die Datenbank
	 * Dabei wird keine Datenvalidierung vorgenommen
	 */
	public void speicherZusatzleistung(Zusatzleistung zusatz, Connection conn)throws ConnectionException {
		try {
			String newZusatzSQL = "INSERT INTO ZUSATZLEISTUNG (zusatzID, leistungsArt) VALUES ("
					+ +zusatz.getNr()
					+ ", '"
					+ zusatz.getLeistungsArt()
					+ "')";
			
			PreparedStatement speicherZusatzleistung = conn.prepareStatement(newZusatzSQL);
			speicherZusatzleistung.execute();

		} catch (SQLException e) {
			throw new ConnectionException(e);
		}
	}
	
	/*
	 * Sucht in der Datenbank nach einer Zusatzdienstleistung mit dem übergebenen Namen
	 */
	public Zusatzleistung sucheZusatzleistungNachArt(String art, Connection conn)throws ConnectionException {
		Zusatzleistung akku = null;
		try {
			List<Zusatzleistung> zusatzList = getAllZusatzleistung(conn);

			for (Zusatzleistung zu : zusatzList) {
				if (zu.getLeistungsArt() == art) {
					akku = zu;
					break;
				}
			}
			if (akku == null) {
				throw new IllegalArgumentException(
						"Zusatzleistung nicht gefunden");
			}
		} catch (ConnectionException e) {
			throw new ConnectionException(e);
		}
		return akku;
	}
	
	/*
	 * Speichert eine Zusatzbuchung in der Datenbank
	 */
	public void speichereZusatzBuchung(int resNr, int zusatzNr, Connection conn) throws ConnectionException{
		try {
			String newZusatzBuchSQL = "INSERT INTO ZUSATZLEISTZUNG_RESERVIERUNG (reservierungID, zusatzID) VALUES ("
					+ resNr
					+ ", "
					+ zusatzNr
					+ ")";
			
			PreparedStatement speicherZusatzBuchung = conn.prepareStatement(newZusatzBuchSQL);
			speicherZusatzBuchung.execute();
			
		} catch (SQLException e) {
			throw new ConnectionException(e);
		}
	}
	
	/*
	 * zählt wie viele der übergebenen Reservierungen eine Zusatzdienstleistung gebucht haben
	 */
	public int countZusatz(List<Integer> resList, Connection conn)throws ConnectionException{	
		int count = 0;
		
		try{
			List<Integer> zusatzResListe = new ArrayList<Integer>();
			
			Statement stmt = conn.createStatement();
			String newCountSQL = "SELECT * FROM ZUSATZLEISTZUNG_RESERVIERUNG";
			
			ResultSet countset = stmt.executeQuery(newCountSQL);
			while(countset.next()){
				zusatzResListe.add(countset.getInt("reservierungID"));
				countset.next();
			}
			for(Integer resNr: zusatzResListe){
				if(resList.contains(resNr)){
					count ++;
				}
			}
			 
		}catch(SQLException e){
			throw new ConnectionException(e);
		}
		return count;
	}

}
