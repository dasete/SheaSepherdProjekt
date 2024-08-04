import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity //Instanzen dieser Klasse werden in DB gespeichert
public class Geisternetz implements Serializable {
	
	@Id
    //@GeneratedValue //Value automatisch vom persistence provider definiert
    private int id; //primary Key für DB
    private int groesse;
    private float breitengrad;
    private float laengengrad;
    private Status status;
    //private Person bergendePerson;


    public Geisternetz()
    {
		this.id = 0;
    	this.groesse = 0;
		this.breitengrad = 0f;
		this.laengengrad = 0f;
		this.status = Status.GEMELDET;
		//this.bergendePerson = null;
    }

    public Geisternetz(int id, int groesse, float breitengrad, float laengengrad, Status status)
    {
    	this.id = id;
    	this.groesse = groesse;
		this.breitengrad = breitengrad;
		this.laengengrad = laengengrad;
		this.status = status;
		//this.bergendePerson = person;
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
	/*
	public Person getBergendePerson() {
		return bergendePerson;
	}
	
	
	public void setBergendePerson(Person bergendePerson) {
		this.bergendePerson = bergendePerson;
	}
	*/
	
}