package mocks;

import db.ConnectionException;
import db.DBFascade;
import gast.EmailTyp;
import gast.Gast;
import gast.GastVerwalter;
import ireservierung.IGastAnwendungsfall;

public class GastAnwendungsfallMock implements IGastAnwendungsfall {

	//Initialisierung
		static final GastVerwalter verwalter = new GastVerwalter();
		static  DBFascade dbf;
		
		//Konstrukor
		public GastAnwendungsfallMock() throws ConnectionException, ClassNotFoundException {
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
		throw new SucessException();

	}
	
	
	public class SucessException extends Error{

		public SucessException() {
			super();
			// TODO Auto-generated constructor stub
		}

		public SucessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
			// TODO Auto-generated constructor stub
		}

		public SucessException(String message, Throwable cause) {
			super(message, cause);
			// TODO Auto-generated constructor stub
		}

		public SucessException(String message) {
			super(message);
			// TODO Auto-generated constructor stub
		}

		public SucessException(Throwable cause) {
			super(cause);
			// TODO Auto-generated constructor stub
		}
		
		
	}

}
