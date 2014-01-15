package reservierung;

import java.net.ConnectException;
import java.sql.SQLException;

import db.*;

public class main {
	public static void main(String[] args) throws ConnectionException {
			
			try{
				//EmailTyp mail0 = new EmailTyp("du@haw-ham.urg.de");
				
//				EmailTyp mail = new EmailTyp("Name", "@haw", ".de");
//				EmailTyp mail1 = new EmailTyp("test", "@haw_hamburg.", ".de");
				//Initialisieren
				GastAnwendungsfall gastAn = new GastAnwendungsfall();
				ReservierungAnwendungsfall resAn = new ReservierungAnwendungsfall();
				System.out.println("Initalized");

				//Stammdaten Anlegen
				resAn.erzeugeZusatzleistung("Sauna");
				resAn.erzeugeZusatzleistung("Vollpension");
				resAn.erzeugeZusatzleistung("WLAN");
				
				
				EmailTyp email = new EmailTyp("timzain","@yahoo",".de");
				Gast gast = gastAn.erzeugeGast(1, "Tom Tompson", email);
				System.out.println("Data initalized");
				
				//Reservierung tätigen
				Reservierung ress = resAn.reserviereZimmer(1, 22);
				//Zusatzleistung buchen
				resAn.bucheZusatzleistung(1, 1);
				
				
				//Stammgast
				Reservierung ress1 = resAn.reserviereZimmer(1, 11);
				Reservierung ress2 = resAn.reserviereZimmer(1, 66);
				Reservierung ress3 = resAn.reserviereZimmer(1, 33);
				Reservierung ress4 = resAn.reserviereZimmer(1, 44);
				
				
				//Stammgast über Zusatz
//				resAn.reserviereZimmer(1, 11);
//				resAn.bucheZusatzleistung(2, 2);
//				resAn.reserviereZimmer(1, 66);
//				resAn.bucheZusatzleistung(3, 3);
				
				System.out.println("Unheil angerichtet");
				
				}catch(Exception e){
					throw new ConnectionException(e);
			}
		}
}
