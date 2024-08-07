import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.Arrays;
import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityTransaction;

@Named
@ApplicationScoped
public class Platform implements Serializable {

	@Inject
	GeisternetzDAO geisternetzDAO;
	 
	static final List<Geisternetz> netzListe = Arrays.asList(new Geisternetz[]{
			new Geisternetz(10, 100f, 1000f, Status.GEMELDET, new Person("Man", "Fred"), new Person("Al", "Bert", "+49123456")),
	    	new Geisternetz(20, 200f, 2000f, Status.GEMELDET),// new Person("Al", "Bert", "+491234567")),
	    	new Geisternetz(30, 600f, 3000f, Status.BERGUNG_BEVORSTEHEND),// new Person("Hans", "Wurst", "+491234567")),
	    	new Geisternetz(50, 400f, 4000f, Status.GEBORGEN),// new Person("Axel", "Schweiß", "+491234567")),
	    	new Geisternetz(40, 500f, 5000f, Status.VERSCHOLLEN), //new Person("Clara", "Himmel", "+491234567"))
	         });


    public Platform()
    {
    	
    }

}