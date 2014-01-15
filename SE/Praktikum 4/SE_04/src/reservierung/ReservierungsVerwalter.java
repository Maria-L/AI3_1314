package reservierung;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.ConnectionException;

public class ReservierungsVerwalter {
	
	//Konstruktor
	public Reservierung neueReservierung(int num, int zNr){
		return new Reservierung(num, zNr);
	}
	//Getter Anfang
	public int getReservierungNr(Reservierung res){
		return res.getNr();
	}
	
	public int getReservierungZimmerNr(Reservierung res){
		return res.getZimmerNr();
	}
	
	//Getter Ende

	//Anfang Methode
	/*
	 * Holt alle Reservierungen aus der Datenbank und gibt sie in einer Liste zurück
	 */
	public List<Reservierung> getAllReservierung(Connection conn) throws ConnectionException {
		try {
			List<Reservierung> reservierungListe = new ArrayList<Reservierung>();
			Statement stmt = conn.createStatement();
			String findAllResSQL = "SELECT reservierungID, zimmerNr FROM reservierung";
			ResultSet rset = stmt.executeQuery(findAllResSQL);
			while (rset.next()) {
				Reservierung res = neueReservierung(rset.getInt("reservierungID"), rset.getInt("zimmerNr"));
				reservierungListe.add(res);
			}
			return reservierungListe;
		} catch (Exception e) {
			throw new ConnectionException(e);
		}
	}
		
	/*
	 * Speichert die übergebene Reservierung in die Datenbank und fügt den gastNr Fremdschlüssen hinzu
	 * hierbei wird keinerlei Validierung vorgenommen
	 */
	public void speicherReservierung(Reservierung res, int gastNr,Connection conn) throws ConnectionException {
		
		try {String newResSQL = "INSERT INTO RESERVIERUNG (reservierungID, zimmerNr, fGastNr) VALUES ("
					+ +res.getNr()
					+ ", "
					+ res.getZimmerNr()
					+ ","
					+ gastNr
					+ ")";
	
				PreparedStatement speicherReservierung = conn.prepareStatement(newResSQL);
				speicherReservierung.execute();

		} catch (SQLException e) {
			throw new ConnectionException(e);
		}
	}
		
		/*
		 * Sucht in der Datenbank nach einer Reservierung mit der übergebenen ID
		 */
		public Reservierung sucheReservierungNachNr(int nr, List<Reservierung> resList) {
			Reservierung akku = null;

			for (Reservierung res : resList) {
				if (res.getNr() == nr) {
					akku = res;
					break;
				}
			}
			if (akku == null) {
				throw new IllegalArgumentException("Reservierung nicht gefunden");
			}
			return akku;
		}
		
		/*
		 * Sucht in der Datenbank nach der Reservierung mit der übergebenen Nummer und gibt
		 * die ID des Gastes zurück zu der sie gehört
		 */
		public int getGastRes(int resNr, Connection conn)throws ConnectionException{
			int gastNr = -1;
			try {
				Statement stmt = conn.createStatement();
				
				String findAllResSQL = "SELECT fGastNr FROM Reservierung WHERE reservierungID = "+ resNr;
				ResultSet rset = stmt.executeQuery(findAllResSQL);
				rset.next();
				gastNr = rset.getInt("fGastNr");
				
			} catch (Exception e) {
				throw new ConnectionException(e);
			}
			return gastNr;
		}
		
		/*
		 * Sucht alle Reservierungen heraus die ein Gast getätigt hat und liefert die IDs in einer Liste zurück
		 */
		public List<Integer> getGastReservierungen(int gastNr, Connection conn)throws ConnectionException{
			try {
				List<Integer> resListe = new ArrayList<Integer>();
				Statement stmt = conn.createStatement();
				String findAllResSQL = "SELECT reservierungID FROM Reservierung WHERE fGastNr = "+gastNr;
				ResultSet rset = stmt.executeQuery(findAllResSQL);
				
				while (rset.next()) {
					resListe.add(rset.getInt("reservierungID"));
				}
				return resListe;
				
			} catch (Exception e) {
				throw new ConnectionException(e);
			}
		}
		
		/*
		 * Zählt wie viele Reservierungen ein Gast getätigt hat
		 */
		public int countRes(int gastNr, Connection conn)throws ConnectionException{
			int count = 0;
			
			try{
				Statement stmt = conn.createStatement();
				String newCountSQL = "SELECT COUNT(*) AS amount FROM Reservierung WHERE fgastNr = " + gastNr;
				
				ResultSet countset = stmt.executeQuery(newCountSQL);
				countset.next();
				count = countset.getInt("amount");
				 
			}catch(SQLException e){
				throw new ConnectionException(e);
			}
			return count;
		}
		
	}

