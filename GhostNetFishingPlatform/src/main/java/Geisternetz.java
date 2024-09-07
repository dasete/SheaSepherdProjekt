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
    private int groesse;
    private float breitengrad;
    private float laengengrad;
    private Status status;
    @ManyToOne(fetch= FetchType.EAGER, cascade=CascadeType.ALL)
    private Person bergendePerson;
    @ManyToOne(fetch= FetchType.EAGER, cascade=CascadeType.ALL)
    private Person meldendePerson; // notwendig? sollte es nicht gemmeldetVonPerson heissen?


    public Geisternetz()
    {
		this.status = Status.GEMELDET;
		//this.bergendePerson = null;
		//this.meldendePerson = null;
    }

    public Geisternetz(int groesse, float breitengrad, float laengengrad, Status status)
    {
    	this.groesse = groesse;
		this.breitengrad = breitengrad;
		this.laengengrad = laengengrad;
		this.status = status;
    }
    
    public Geisternetz(int groesse, float breitengrad, float laengengrad, Status status, Person bPerson, Person mPerson)
    {
    	this.groesse = groesse;
		this.breitengrad = breitengrad;
		this.laengengrad = laengengrad;
		this.status = status;
		this.bergendePerson = bPerson;
		this.meldendePerson = mPerson;
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
	
	public Person getMeldendePerson() {
		return meldendePerson;
	}
	
	
	public void setMeldendePerson(Person meldendePerson) {
		this.meldendePerson = meldendePerson;
	}
	
}