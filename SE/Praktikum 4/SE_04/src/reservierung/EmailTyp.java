package reservierung;

public class EmailTyp {

	private String name;
	private String server;
	private String domain;
	
	public EmailTyp(String name, String server, String domain) {
		this.name = name;
		this.server = server;
		this.domain = domain;
	}
	
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
