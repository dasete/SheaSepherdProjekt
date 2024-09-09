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
		erstelleGeisternetz();
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
		try {
	        if (istGeisternetzmitKoordinatenBekannt()) {
	            // Wenn bereits ein Geisternetz an diesen Koordinaten existiert, wird die Speicherung nicht durchgeführt
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                "Geisternetz bereits bekannt", "Ein Geisternetz an diesen Koordinaten wurde bereits gemeldet. Vielen Dank"));
	            return null; // Keine Weiterleitung
	        }

	        neuesGeisternetz.setGemeldetVon(this.aktuellePerson);
	        
	        EntityTransaction trans = geisternetzDAO.getAndBeginTransaction();
	        geisternetzDAO.persist(this.neuesGeisternetz);
	        trans.commit();
	        
	        this.aktuellePerson = null;
	        this.neuesGeisternetz = null;
	        return "index.xhtml?faces-redirect=true";
	        
	    } catch (Exception e) {
	        e.printStackTrace();

	        // Transaktion rollback, falls trans bereits begonnen wurde
	        EntityTransaction trans = geisternetzDAO.getTransaction();
	        if (trans != null && trans.isActive()) {
	            trans.rollback();
	        }
	        return null;  // Keine Weiterleitung
	    }
	}
	
		
	
	
	
	public boolean istGeisternetzmitKoordinatenBekannt() {
	    float breitengrad = neuesGeisternetz.getBreitengrad();
	    float laengengrad = neuesGeisternetz.getLaengengrad();

	    // Suche nach existierenden Geisternetzen mit den gleichen Koordinaten
	    Geisternetz vorhandenesNetz = geisternetzDAO.findeGeisternetzMitKoordinaten(breitengrad, laengengrad);

	    if (vorhandenesNetz != null) {
	        return true; // Geisternetz bereits bekannt
	    }
	    return false; // Geisternetz ist neu
	}
	
	
	// von Button in bergungBeabsichtigen ausgeführt
	public String bergendePersonEintragen() {
	    if (ausgewähltesGeisternetz != null && aktuellePerson != null) { 
	    	try {
	    		ausgewähltesGeisternetz.setBergendePerson(aktuellePerson);
	            ausgewähltesGeisternetz.setStatus(Status.BERGUNG_BEVORSTEHEND);
	            
	            EntityTransaction trans = geisternetzDAO.getAndBeginTransaction();
	            geisternetzDAO.merge(ausgewähltesGeisternetz);
	            trans.commit();
	            
	            return "index.xhtml?faces-redirect=true";
	        } catch (Exception e) {
	        	EntityTransaction trans = geisternetzDAO.getTransaction();
	        	if (trans != null && trans.isActive()) {
	            	trans.rollback();
	            }
	            e.printStackTrace();
	            throw new RuntimeException(e);
	        }
	    } else { // Fehleranzeige in Growl
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
	            "Fehlende Daten", "Fehler bei Verarbeitung von aktueller Person und Geisternetz"));
	    }
	    return null;
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
	    // Überprüfen, ob der Wert null ist (optional, da `required="true"` gesetzt ist)
	    if (value == null) {
	        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                "Fehlender Name", "Name darf nicht leer sein");
	        throw new ValidatorException(msg);
	    }

	    // Definiere den regulären Ausdruck für gültige Namen
	    // Muss mindestens zwei Buchstaben enthalten und erlaubt die Sonderzeichen '-' und Leerzeichen
	    final String NAME_REGEX = "^(?=.*[A-Za-z].*[A-Za-z])[A-Za-z- ]+$";

	    // Erstelle das Pattern einmalig für bessere Übersicht
	    Pattern pattern = Pattern.compile(NAME_REGEX);
	    Matcher matcher = pattern.matcher(value.toString());

	    // Prüfe, ob der Wert dem regulären Ausdruck entspricht
	    if (!matcher.matches()) {
	        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                "Fehlerhafter Name", "Name muss mindestens zwei Buchstaben enthalten und darf nur Buchstaben, Leerzeichen oder '-' enthalten.");
	        throw new ValidatorException(msg);
	    }
	}
	
	
	public void pruefeTelefonnummer(FacesContext context, UIComponent component, Object value) throws ValidatorException {
	    // Regulären Ausdruck als Konstante definieren
	    final String PHONE_NUMBER_REGEX = "^(\\+{0,1}\\d{1,3} ?)?\\d{3,15}$";

	    // Pattern wird kompiliert
	    Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX);
	    Matcher matcher = pattern.matcher(value.toString());

	    // Wenn die Telefonnummer nicht dem Muster entspricht, eine Fehlermeldung auslösen
	    if (!matcher.matches()) {
	        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                "Fehlerhafte Telefonnummer", 
	                "Bitte geben Sie eine gültige Telefonnummer ein. Erlaubte Formate: +49 123456 oder 0123456789.");
	        throw new ValidatorException(msg);
	    }
	}
	
	
	private void erstelleGeisternetz() {
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