import java.io.Serializable;
import java.util.List;
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
	private Person aktuellePerson;
	private Geisternetz ausgewähltesGeisternetz;
	
	@Inject
    GeisternetzDAO geisternetzDAO;
	
	//für Anzeige auf Startseite
	public List<Geisternetz> getGeisternetze() {
        return geisternetzDAO.getGeisternetze();
    }
	
	
	// von Button auf Startseite in Tabelle ausgeführt
	public String weiterleitenZuNetzMelden() {
		erstelleAktuellePerson();
		erstelleNeuesGeisternetz();
		return "netzMelden.xhtml?faces-redirect=true";
	}
	
	
	// von Button auf Startseite in Tabelle ausgeführt
	public String weiterleitenZuBergungsabsichtEintragen(int geisternetzId) {
        this.ausgewähltesGeisternetz = geisternetzDAO.findeGeisternetzMitId(geisternetzId);
        erstelleAktuellePerson();
        return "bergungBeabsichtigen.xhtml?faces-redirect=true";
    }
	
	
	// in netzMelden.xthml von Button aufgerufen
	public String geisternetzSpeichern() {
		if (neuesGeisternetz == null || aktuellePerson == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
		            "Fehlende Daten", "Fehler bei Verarbeitung von aktueller Person oder Geisternetz"));
			return null;
		}
		
		if (istGeisternetzmitKoordinatenBekannt()) {
            // Wenn bereits ein Geisternetz an diesen Koordinaten existiert, wird die Speicherung nicht durchgeführt
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Geisternetz bereits bekannt", "Ein Geisternetz an diesen Koordinaten wurde bereits gemeldet. Vielen Dank"));
            return null; // Keine Weiterleitung
        }
        
	    Person existierendePerson = geisternetzDAO.findePersonMitDaten(
	            aktuellePerson.getVorname(),
	            aktuellePerson.getNachname(),
	            aktuellePerson.getTelefonnummer()
	    );
	    if (existierendePerson != null) {
	        // Falls Person existiert, zuweisen
	        neuesGeisternetz.setGemeldetVon(existierendePerson);
	    } else {
	        // Falls Person nicht existiert, neu anlegen
	        neuesGeisternetz.setGemeldetVon(this.aktuellePerson);
	    }
		
		
        geisternetzDAO.persistGeisternetz(this.neuesGeisternetz);
		this.aktuellePerson = null;
        this.neuesGeisternetz = null;
        return "index.xhtml?faces-redirect=true";
	}
	
	// von Button in bergungBeabsichtigen ausgeführt
	public String bergendePersonEintragen() {
		if (ausgewähltesGeisternetz == null || aktuellePerson == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
		            "Fehlende Daten", "Fehler bei Verarbeitung von aktueller Person oder Geisternetz"));
			return null;
		}
		
		Person existierendePerson = geisternetzDAO.findePersonMitDaten(
	            aktuellePerson.getVorname(),
	            aktuellePerson.getNachname(),
	            aktuellePerson.getTelefonnummer()
	    );
	    if (existierendePerson != null) {
	        // Falls Person existiert, zuweisen
	    	ausgewähltesGeisternetz.setBergendePerson(existierendePerson);
	    } else {
	        // Falls Person nicht existiert, neu anlegen
	    	ausgewähltesGeisternetz.setBergendePerson(this.aktuellePerson);
	    }
			
        ausgewähltesGeisternetz.setStatus(Status.BERGUNG_BEVORSTEHEND);
        geisternetzDAO.mergeGeisternetz(this.ausgewähltesGeisternetz);
    	return "index.xhtml?faces-redirect=true";
	}
	

	//von Button auf Startseite in Tabelle ausgeführt
	public boolean istStatusBergungBevorstehend(int geisternetzId) {
        Geisternetz geisternetz = geisternetzDAO.findeGeisternetzMitId(geisternetzId);
        if (geisternetz != null) {
            return geisternetz.getStatus() == Status.BERGUNG_BEVORSTEHEND;
        }
        return false;
    }
	
	
	public void pruefeName(FacesContext ctx, UIComponent cmp, Object value) throws ValidatorException {
	    // Definiere den regulären Ausdruck für gültige Namen
	    // Muss mindestens zwei Buchstaben enthalten und erlaubt die Sonderzeichen '-' und Leerzeichen
	    final String NAME_REGEX = "^(?=.*[A-Za-z].*[A-Za-z])[A-Za-z- ]+$";

	    Pattern pattern = Pattern.compile(NAME_REGEX);
	    Matcher matcher = pattern.matcher(value.toString());

	    // Prüfe, ob der Wert dem regulären Ausdruck entspricht
	    if (!matcher.matches()) {
	        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                "Fehlerhafter Name", 
	                "Name muss mindestens zwei Buchstaben enthalten und darf nur Buchstaben, Leerzeichen oder '-' enthalten.");
	        throw new ValidatorException(msg);
	    }
	}
	
	
	public void pruefeTelefonnummer(FacesContext context, UIComponent component, Object value) throws ValidatorException {
	    // Regulären Ausdruck als Konstante definieren
	    final String NUMMER_REGEX = "^(\\+{0,1}\\d{1,3} ?)?\\d{3,15}$";

	    Pattern pattern = Pattern.compile(NUMMER_REGEX);
	    Matcher matcher = pattern.matcher(value.toString());

	    // Wenn die Telefonnummer nicht dem Muster entspricht, eine Fehlermeldung auslösen
	    if (!matcher.matches()) {
	        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                "Fehlerhafte Telefonnummer", 
	                "Bitte geben Sie eine gültige Telefonnummer ein. Erlaubte Formate: +49 123456 oder 0123456789.");
	        throw new ValidatorException(msg);
	    }
	}
	
	
	private boolean istGeisternetzmitKoordinatenBekannt() {
	    float breitengrad = neuesGeisternetz.getBreitengrad();
	    float laengengrad = neuesGeisternetz.getLaengengrad();
	    Geisternetz vorhandenesNetz = geisternetzDAO.findeGeisternetzMitKoordinaten(breitengrad, laengengrad);
	    if (vorhandenesNetz != null) {
	        return true; // Geisternetz bereits bekannt
	    }
	    return false; // Geisternetz ist neu
	}
	
	
	private void erstelleNeuesGeisternetz() {
		if (this.neuesGeisternetz != null) {
			this.neuesGeisternetz = null;
		}
		this.neuesGeisternetz = new Geisternetz();
	}
	

	private void erstelleAktuellePerson() {
		if (this.aktuellePerson != null) {
			this.aktuellePerson = null;
		}
		this.aktuellePerson = new Person();
	}
	
	
	public Geisternetz getNeuesGeisternetz() {
		return neuesGeisternetz;
	}

	public void setNeuesGeisternetz(Geisternetz neuesGeisternetz) {
		this.neuesGeisternetz = neuesGeisternetz;
	}
	
	public Person getAktuellePerson() {
		return aktuellePerson;
	}

	public void setAktuellePerson(Person aktuellePerson) {
		this.aktuellePerson = aktuellePerson;
	}

    public Geisternetz getAusgewähltesGeisternetz() {
        return ausgewähltesGeisternetz;
    }
    
    public void setAusgewähltesGeisternetz(Geisternetz geisternetz) {
        this.ausgewähltesGeisternetz = geisternetz;
    }
	
}