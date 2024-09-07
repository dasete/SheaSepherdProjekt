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

    public GeisternetzDAO() {
        try {
            entityManager = Persistence.createEntityManagerFactory("ghostnetfishing").createEntityManager();
            criteriaBuilder = entityManager.getCriteriaBuilder();

            long count = getGeisternetzCount();
            System.err.println(count+ " Geisternetze erfasst.");

            if(count == 0) {
                System.err.println("Initialisierung der Daten.");
                EntityTransaction trans = getAndBeginTransaction();
                for(Geisternetz netz : Platform.netzListe) {
                    persist(netz);
                }
                trans.commit();	//Datenbank Transaktion commiten
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public long getGeisternetzCount() {
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        cq.select(criteriaBuilder.count(cq.from(Geisternetz.class)));
        return entityManager.createQuery(cq).getSingleResult();
    }

    
    public Geisternetz getGeisternetzAtIndex(int idx) {
        CriteriaQuery<Geisternetz> cq = criteriaBuilder.createQuery(Geisternetz.class);
        Root<Geisternetz> variableRoot = cq.from(Geisternetz.class);
        return entityManager.createQuery(cq).setMaxResults(1).setFirstResult(idx).getSingleResult();
    }
   
    
    public List<Geisternetz> getGeisternetz() {
        CriteriaQuery<Geisternetz> cq = criteriaBuilder.createQuery(Geisternetz.class);
        Root<Geisternetz> variableRoot = cq.from(Geisternetz.class);
        return entityManager.createQuery(cq).getResultList();
    }

    //Datenbank Transaktion Beginnen
    public EntityTransaction getAndBeginTransaction() {
        EntityTransaction tran = entityManager.getTransaction();
        tran.begin();
        return tran;
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