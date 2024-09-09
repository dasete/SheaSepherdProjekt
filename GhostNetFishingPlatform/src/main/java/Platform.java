import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

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
			new Geisternetz(0f, 0f, 1, Status.GEMELDET, new Person("Test", "Person", "+49123456")),
		});


    public Platform()
    {
    	
    }
    
    public Collection<Geisternetz> getNetzListe()
    {
        return netzListe;
    }

}