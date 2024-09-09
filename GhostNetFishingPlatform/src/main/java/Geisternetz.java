import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity //Instanzen dieser Klasse werden in DB gespeichert
public class Geisternetz implements Serializable {
	
	@Id
    @GeneratedValue //Value automatisch vom persistence provider definiert
    private int id; //primary Key für DB
    private float breitengrad;
    private float laengengrad;
    private int groesse;
    private Status status;
    @ManyToOne(fetch= FetchType.EAGER, cascade=CascadeType.ALL)
    private Person bergendePerson;
    @ManyToOne(fetch= FetchType.EAGER, cascade=CascadeType.ALL)
    private Person gemeldetVon; // notwendig? sollte es nicht gemmeldetVonPerson heissen?
    @ManyToOne(fetch= FetchType.EAGER, cascade=CascadeType.ALL)
    private Person verschollenGemeldetVon;


    public Geisternetz()
    {
		this.status = Status.GEMELDET;
		//this.bergendePerson = null;
		//this.meldendePerson = null;
    }

    public Geisternetz(float breitengrad, float laengengrad, int groesse, Status status, Person mPerson)
    {
		this.breitengrad = breitengrad;
		this.laengengrad = laengengrad;
		this.groesse = groesse;
		this.status = status;
		this.gemeldetVon = mPerson;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getGroesse()
    {
        return groesse;
    }

    public void setGroesse(int groesse)
    {
        this.groesse = groesse;
    }
    
    public float getBreitengrad()
    {
        return breitengrad;
    }

    public void setBreitengrad(float breitengrad)
    {
        this.breitengrad = breitengrad;
    }
    
    public float getLaengengrad()
    {
        return laengengrad;
    }

    public void setLaengengrad(float laengengrad)
    {
        this.laengengrad = laengengrad;
    }
    
    public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public Person getBergendePerson() {
		return bergendePerson;
	}
	
	
	public void setBergendePerson(Person bergendePerson) {
		this.bergendePerson = bergendePerson;
	}
	
	public Person getGemeldetVon() {
		return gemeldetVon;
	}
	
	
	public void setGemeldetVon(Person gemeldetVon) {
		this.gemeldetVon = gemeldetVon;
	}
	
	public Person getVerschollenGemeldetVon() {
		return verschollenGemeldetVon;
	}
	
	
	public void setVerschollenGemeldetVon(Person verschollenGemeldetVon) {
		this.verschollenGemeldetVon = verschollenGemeldetVon;
	}
	
}