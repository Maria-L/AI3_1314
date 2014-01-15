package db;

import java.sql.SQLException;
/*
 * Eine Exception fuer den Fall, dass die Verbindung zur Datenbank fehlschlaegt
 */
public class ConnectionException extends Exception{
	
	public ConnectionException() {
		super("Connection to database has failed.");
	}
	
//	public ConnectionException(SQLException e){
//		super("Connection to database has failed" + e.getMessage().toString());
//	}
	
	public ConnectionException(Exception e){
		//super(e.getMessage().toString());
		super(e.fillInStackTrace());
	}
	

}
