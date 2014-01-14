package reservierung;

import ireservierung.*;
import db.DBFascade;
import db.ConnectionException;


public final class GastAnwendungsfall implements IGastServices, IGastServicesFuerReservierung{

	//Initialisierung
	static final GastVerwalter verwalter = new GastVerwalter();
	static  DBFascade dbf;
	
	//Konstrukor
	public GastAnwendungsfall() throws ConnectionException, ClassNotFoundException {
		try{
			dbf = new DBFascade();
		}catch(ConnectionException e){
			throw new ConnectionException(e);
		}
	}

	//Methoden Anfang
	@Override
	public Gast erzeugeGast(int nr, String name, EmailTyp email) {
		Gast gast = verwalter.neuerGast(nr, name, email);
		try{
			verwalter.speichereGast(gast, dbf.getConn());
		}catch(ConnectionException e) {
			e.printStackTrace();
		}
		return gast;
	}

	@Override
	public Gast sucheGastNachName(String name) {
		try {
			return verwalter.sucheGastNachName(name, verwalter.getAllGast(dbf.getConn()));
		}catch (ConnectionException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void markiereGastAlsStammkunden(int nr) {
		try {
			verwalter.markiereGastStammkunde(nr,dbf.getConn());
		}catch (ConnectionException e) {
			e.printStackTrace();
		}
	}

}
