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
    CriteriaBuilder criteriaBuilder;
    EntityTransaction trans;

    public GeisternetzDAO() {
    	try {
            entityManager = Persistence.createEntityManagerFactory("ghostnetfishing").createEntityManager();
            criteriaBuilder = entityManager.getCriteriaBuilder();

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
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        cq.select(criteriaBuilder.count(cq.from(Geisternetz.class)));
        return entityManager.createQuery(cq).getSingleResult();
    }

    //Löschen?
    public Geisternetz getGeisternetzAtIndex(int idx) {
        CriteriaQuery<Geisternetz> cq = criteriaBuilder.createQuery(Geisternetz.class);
        Root<Geisternetz> variableRoot = cq.from(Geisternetz.class);
        return entityManager.createQuery(cq).setMaxResults(1).setFirstResult(idx).getSingleResult();
    }
   
   
    public Geisternetz findeGeisternetzMitKoordinaten(float breitengrad, float laengengrad) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Geisternetz> cq = cb.createQuery(Geisternetz.class);
        Root<Geisternetz> root = cq.from(Geisternetz.class);

        // Abfrage, Geisternetze mit gleichen Koordinaten und dem Status != VERSCHOLLEN und != GEBORGEN zu finden
        cq.select(root).where(
            cb.and(
                cb.equal(root.get("breitengrad"), breitengrad),
                cb.equal(root.get("laengengrad"), laengengrad),
                cb.not(root.get("status").in(Status.VERSCHOLLEN, Status.GEBORGEN))
            )
        );

        try {
            return entityManager.createQuery(cq).getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            // Kein Geisternetz gefunden
            return null;
        }
    }
    
    public Geisternetz findeGeisternetzMitId(int id) {
        return entityManager.find(Geisternetz.class, id);
    }
    
    
    public List<Geisternetz> getGeisternetze() {
        CriteriaQuery<Geisternetz> cq = criteriaBuilder.createQuery(Geisternetz.class);
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

    public void merge(Geisternetz netz) {
        entityManager.merge(netz);
    }
    
    //speichert Objekt in Datenbank
    public void persist(Geisternetz netz) {
        entityManager.persist(netz);
    }

    public static void main(String[] args) {
    	GeisternetzDAO dao = new GeisternetzDAO();
        System.err.println(dao.getGeisternetzCount() + " Geisternetze erfasst.");
    }
	
}