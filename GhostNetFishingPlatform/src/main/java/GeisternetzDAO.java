import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;


@Named
@ApplicationScoped
public class GeisternetzDAO {

	EntityManager entityManager;
    CriteriaBuilder cb;
    EntityTransaction trans;

    public GeisternetzDAO() {
    	try {
            entityManager = Persistence.createEntityManagerFactory("ghostnetfishing").createEntityManager();
            cb = entityManager.getCriteriaBuilder();

            long count = getGeisternetzCount();
            System.err.println(count+ " Geisternetze erfasst.");

            if(count == 0) {
                System.err.println("Initialisierung der Daten.");
                trans = getAndBeginTransaction();
                for(Geisternetz netz : Platform.netzListe) {
                    persist(netz);
                }
                trans.commit();	//Datenbank Transaktion commiten
            }

        } catch (Exception e) {
            
            if (trans != null && trans.isActive()) {
                trans.rollback(); // Im Fehlerfall trans zurücksetzen
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    //Löschen?
    public long getGeisternetzCount() {
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(Geisternetz.class)));
        return entityManager.createQuery(cq).getSingleResult();
    }

    //Löschen?
    public Geisternetz getGeisternetzAtIndex(int idx) {
        CriteriaQuery<Geisternetz> cq = cb.createQuery(Geisternetz.class);
        Root<Geisternetz> variableRoot = cq.from(Geisternetz.class);
        return entityManager.createQuery(cq).setMaxResults(1).setFirstResult(idx).getSingleResult();
    }
   
   
    public List<Geisternetz> findeGeisternetzMitKoordinaten(float breitengrad, float laengengrad) {
        CriteriaQuery<Geisternetz> cq = cb.createQuery(Geisternetz.class);
        Root<Geisternetz> root = cq.from(Geisternetz.class);

        // Abfrage, Geisternetze mit gleichen Koordinaten zu finden
        cq.select(root).where(
            cb.and(
                cb.equal(root.get("breitengrad"), breitengrad),
                cb.equal(root.get("laengengrad"), laengengrad)
            )
        );

        return entityManager.createQuery(cq).getResultList();
    }
    
    
    public Person findePersonMitDaten(String vorname, String nachname, String telefonnummer) {
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> root = cq.from(Person.class);

        cq.select(root).where(
            cb.and(
                cb.equal(root.get("vorname"), vorname),
                cb.equal(root.get("nachname"), nachname),
                cb.equal(root.get("telefonnummer"), telefonnummer)
            )
        );

        List<Person> resultList = entityManager.createQuery(cq).getResultList();

        if (!resultList.isEmpty()) {
            return resultList.get(0); // Gibt die erste gefundene Person zurück
        } else {
            return null; // Keine Person gefunden
        }
    }
    
    
    public Geisternetz findeGeisternetzMitId(int id) {
        return entityManager.find(Geisternetz.class, id);
    }
    
 
    public List<Geisternetz> getGeisternetze() {
        CriteriaQuery<Geisternetz> cq = cb.createQuery(Geisternetz.class);
        Root<Geisternetz> variableRoot = cq.from(Geisternetz.class);
        return entityManager.createQuery(cq).getResultList();
    }

    //Datenbank Transaktion Beginnen
    public EntityTransaction getAndBeginTransaction() {
        EntityTransaction trans = entityManager.getTransaction();
        trans.begin();
        return trans;
    }
    
    // Datenbank Transaktion bekommen
    public EntityTransaction getTransaction() {
    	EntityTransaction trans = entityManager.getTransaction();
    	return trans;
    }
    
    public void persistGeisternetz(Geisternetz netz) {
    	try {
	        EntityTransaction trans = getAndBeginTransaction();
	        persist(netz);
	        trans.commit();
	        
	    } catch (Exception e) {
	        e.printStackTrace();

	        // Transaktion rollback, falls trans bereits begonnen wurde
	        EntityTransaction trans = getTransaction();
	        if (trans != null && trans.isActive()) {
	            trans.rollback();
	        }
	    }
    }
    
    
    public void mergeGeisternetz(Geisternetz netz) {
    	try {
            EntityTransaction trans = getAndBeginTransaction();
            merge(netz);
            trans.commit();
             
        } catch (Exception e) {
        	EntityTransaction trans = getTransaction();
        	if (trans != null && trans.isActive()) {
            	trans.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    //speichert Objekt in Datenbank
    public void persist(Geisternetz netz) {
        entityManager.persist(netz);
    }
    
    public void merge(Geisternetz netz) {
        entityManager.merge(netz);
    }


    public static void main(String[] args) {
    	GeisternetzDAO dao = new GeisternetzDAO();
        System.err.println(dao.getGeisternetzCount() + " Geisternetze erfasst.");
    }
	
}