package reservierung;
import java.util.List;

import db.ConnectionException;
import db.DBFascade;
import gast.GastAnwendungsfall;
import ireservierung.*;

public class ReservierungAnwendungsfall implements IReservierungService {

	//Initialisierung
	static final ReservierungsVerwalter reservierungsVerwalter = new ReservierungsVerwalter();
	static final ZusatzleistungVerwalter zusatzleistungsVerwalter = new ZusatzleistungVerwalter();
	private IGastAnwendungsfall gastAnwendungsfall;
	
	private Entity_ID resId;
	private Entity_ID zusatzId;
	static  DBFascade dbf;
	
	//Konstruktor
	public ReservierungAnwendungsfall() throws ConnectionException, ClassNotFoundException {
		this(new GastAnwendungsfall(), new ReservierungID(), new ZusatzID());
	}
	
	
	public ReservierungAnwendungsfall(IGastAnwendungsfall gastAnwendungsfall, Entity_ID resId, Entity_ID zusatzId) throws ConnectionException, ClassNotFoundException {
		super();
		this.gastAnwendungsfall = gastAnwendungsfall;
		this.resId = resId;
		this.zusatzId = zusatzId;
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
			zusatzleistungsVerwalter.speichereZusatzBuchung(reservierungsNr, zusatzleistungNr, dbf.getConn());
			int gastNr = reservierungsVerwalter.getGastRes(reservierungsNr, dbf.getConn());
			List<Integer> resList = reservierungsVerwalter.getGastReservierungen(gastNr, dbf.getConn());
			
			if(zusatzleistungsVerwalter.countZusatz(resList, dbf.getConn()) >= 3){
				gastAnwendungsfall.markiereGastAlsStammkunden(gastNr);
			}
		}catch(ConnectionException e){
			e.printStackTrace();
		}
		
	}

}
