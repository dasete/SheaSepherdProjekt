import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity //Instanzen dieser Klasse werden in DB gespeichert
public class Person implements Serializable {
	@Id
    @GeneratedValue //Value automatisch vom persistence provider definiert
	private int id; //primary key für DB
	private String vorname;
	private String nachname;
	private String telefonnummer;

	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Geisternetz> zuBergendeGeisternetze = new ArrayList<>();
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Geisternetz> gemeldeteGeisternetze = new ArrayList<>();
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Geisternetz> geborgendeGeisternetze = new ArrayList<>();
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Geisternetz> verschollenGemeldeteGeisternetze = new ArrayList<>();
	
	public Person()
	{
		
	}
	
	public Person(String vorname, String nachname)
	{
		this.vorname = vorname;
		this.nachname = nachname;
	}
	
	public Person(String vorname, String nachname, String telefonnummer)
	{
		this.vorname = vorname;
		this.nachname = nachname;
		this.telefonnummer = telefonnummer;
	}

	
	public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
	
    
	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	
	public String getTelefonnummer() {
		return telefonnummer;
	}
	public void setTelefonnummer(String telefonnummer) {
		this.telefonnummer = telefonnummer;
	}
	
	
	public List<Geisternetz> getZuBergendeGeisternetze() {
		return zuBergendeGeisternetze;
	}
	public void setZuBergendeGeisternetze( List<Geisternetz> zuBergendeGeisternetze) {
		this.zuBergendeGeisternetze = zuBergendeGeisternetze;
	}
	
	
	public List<Geisternetz> getGemeldeteGeisternetze() {
		return gemeldeteGeisternetze;
	}
	public void setGemeldeteGeisternetze( List<Geisternetz> gemeldeteGeisternetze) {
		this.gemeldeteGeisternetze = gemeldeteGeisternetze;
	}
	
	
	public List<Geisternetz> getGeborgendeGeisternetze() {
		return geborgendeGeisternetze;
	}
	public void setGeborgendeGeisternetze( List<Geisternetz> geborgendeGeisternetze) {
		this.geborgendeGeisternetze = geborgendeGeisternetze;
	}
	
	
	public List<Geisternetz> getVerschollenGemeldeteGeisternetze() {
		return verschollenGemeldeteGeisternetze;
	}
	public void setVerschollenGemeldeteGeisternetze( List<Geisternetz> verschollenGemeldeteGeisternetze) {
		this.verschollenGemeldeteGeisternetze = verschollenGemeldeteGeisternetze;
	}
}