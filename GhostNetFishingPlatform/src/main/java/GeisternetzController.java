import java.io.Serializable;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityTransaction;

@Named
@ApplicationScoped
public class GeisternetzController{
	
	private Geisternetz neuesGeisternetz;
	private Person meldendePerson;
	
	@Inject
    GeisternetzDAO geisternetzDAO;
	
	public String netzMelden() {
		erstelleMeldendePerson();
		erstelleGeisternetz();
		return "netzMelden.xhtml?faces-redirect=true";
	}
	
	public String meldeInformationenSpeichern() {
		neuesGeisternetz.setMeldendePerson(this.meldendePerson);
		
		System.out.println(neuesGeisternetz.getGroesse());
		System.out.println(neuesGeisternetz.getBreitengrad());
		System.out.println(neuesGeisternetz.getLaengengrad());
		
        EntityTransaction trans = geisternetzDAO.getAndBeginTransaction();
        geisternetzDAO.persist(this.neuesGeisternetz);
        trans.commit();
     
        this.meldendePerson = null;
        this.neuesGeisternetz = null;
		return "index.xhtml?faces-redirect=true";
	}
	
	private void erstelleGeisternetz() {
		if (this.neuesGeisternetz != null) {
			this.neuesGeisternetz = null;
		}
		this.neuesGeisternetz = new Geisternetz();
	}
	
	
	private void erstelleMeldendePerson() {
		if (this.meldendePerson != null) {
			this.meldendePerson = null;
		}
		this.meldendePerson = new Person();
	}
	
	
	public Geisternetz getNeuesGeisternetz() {
		return neuesGeisternetz;
	}

	public void setNeuesGeisternetz(Geisternetz neuesGeisternetz) {
		this.neuesGeisternetz = neuesGeisternetz;
	}
	
	
	public Person getMeldendePerson() {
		return meldendePerson;
	}

	public void setMeldendePerson(Person meldendePerson) {
		this.meldendePerson = meldendePerson;
	}
	
}