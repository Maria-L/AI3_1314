package gast;

import java.util.regex.Pattern;

public class EmailTyp {

	private String name;
	private String server;
	private String domain;
	
	//Konstruktor für Strings
	public EmailTyp(String email){
		int name_char = email.indexOf('@');
		int domain_char = email.lastIndexOf('.');
		System.out.println("@" + name_char + "." + domain_char);
		System.out.println(email.substring(0, name_char));
		System.out.println(email.substring(name_char, domain_char));
		System.out.println(email.substring(domain_char, email.length()));
		
		new EmailTyp(email.substring(0, name_char), email.substring(name_char, domain_char), email.substring(domain_char, email.length()));
	}
	//Konstruktor mit drei Feldern für die Datenbank. Umständliche Implementierung 
	public EmailTyp(String name, String server, String domain) {
		if(name.length() <= 60 && name.length() >= 1 && (Pattern.matches( "[\\w._-]*", name))){
			this.name = name;
		}else throw new IllegalArgumentException("Der Name ist zu lang");
		if(Pattern.matches( "@[\\w_.-]*", server) && server.length() <= 20 &&  server.length() >= 1){
			this.server = server;
		}else throw new IllegalArgumentException("@ Zeichen fehlt oder Server Name lang");
		
		if(Pattern.matches( ".\\w.*", domain) && domain.length() <= 20 &&  domain.length() >= 1){
			this.domain = domain;
		}else throw new IllegalArgumentException(". Zeichen fehlt oder Domain Name lang");
	}
	//Getter
	public EmailTyp neueEmail(String name, String server, String domain){
		return new EmailTyp(name, server, domain);
	}
	
	public String getName(){
		return name;
	}
	public String getServer(){
		return server;
	}
	public String getDomain(){
		return domain;
	}
	


}
