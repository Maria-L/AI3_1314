package reservierung;
import java.util.List;

import db.ConnectionException;
import db.DBFascade;
import ireservierung.*;

public class ReservierungAnwendungsfall implements IReservierungService {

	//Initialisierung
	static final ReservierungsVerwalter reservierungsVerwalter = new ReservierungsVerwalter();
	static final ZusatzleistungVerwalter zusatzleistungsVerwalter = new ZusatzleistungVerwalter();
	private GastAnwendungsfall gastAnwendungsfall = new GastAnwendungsfall();
	
	private ReservierungID resId = new ReservierungID();
	private ZusatzID zusatzId = new ZusatzID();
	static  DBFascade dbf;
	
	//Konstruktor
	public ReservierungAnwendungsfall() throws ConnectionException, ClassNotFoundException {
		try{
			dbf = new DBFascade();
		}catch(ConnectionException e){
			throw new ConnectionException(e);
		}
	}

	//Methoden Anfang
	@Override
	public Zusatzleistung erzeugeZusatzleistung(String name) {
		Zusatzleistung zusatz = zusatzleistungsVerwalter.erzeugeZusatzleistung(zusatzId.getID(), name);
		try{
			zusatzleistungsVerwalter.speicherZusatzleistung(zusatz, dbf.getConn());
		}catch(ConnectionException e) {
			e.printStackTrace();
		}
		return zusatz;
	}

	@Override
	public Reservierung reserviereZimmer(int gastNr, int zimmerNr){
		Reservierung res = reservierungsVerwalter.neueReservierung(resId.getID(), zimmerNr);
		try{
			reservierungsVerwalter.speicherReservierung(res,gastNr,dbf.getConn()); 
			if( reservierungsVerwalter.countRes(gastNr, dbf.getConn()) >= 5){
				gastAnwendungsfall.markiereGastAlsStammkunden(gastNr);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public void bucheZusatzleistung(int reservierungsNr, int zusatzleistungNr){
		try{
			int gastNr = reservierungsVerwalter.getGastRes(reservierungsNr, dbf.getConn());
			List<Integer> resList = reservierungsVerwalter.getGastReservierungen(gastNr, dbf.getConn());
			
			zusatzleistungsVerwalter.speichereZusatzBuchung(reservierungsNr, zusatzleistungNr, dbf.getConn());
			
			if(zusatzleistungsVerwalter.countZusatz(resList, dbf.getConn()) >= 3){
				gastAnwendungsfall.markiereGastAlsStammkunden(gastNr);
			}
		}catch(ConnectionException e){
			e.printStackTrace();
		}
		
	}

}
