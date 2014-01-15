package reservierung;

public class Gast{

	private int nr;
	private String name;
	private EmailTyp email;
	private int stammkunde = 0;
	
	public Gast(int num,String name, EmailTyp mail) {
		this.nr = num;
		this.name = name;
		this.email = mail;
	}
	public Gast(int num,String name, EmailTyp mail, int istStammKunde) {
		this.nr = num;
		this.name = name;
		this.email = mail;
		this.stammkunde = istStammKunde;
	}
	
	public int getNr(){
		return nr;
	}
	
	public String getName(){
		return name;
	}
	
	public EmailTyp getEmail(){
		return email;
	}
	
	public void setStammkunde(){
		stammkunde = 1;
	}
	public int getStammkunde(){
		return stammkunde;
	}

}
