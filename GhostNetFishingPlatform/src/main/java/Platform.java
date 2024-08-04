import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Named
@ApplicationScoped
public class Platform {

	@Inject
	GeisternetzDAO geisternetzDAO;
	 
	static final List<Geisternetz> netzListe = Arrays.asList(new Geisternetz[]{
			new Geisternetz(1, 10, 100f, 1000f, Status.GEMELDET, new Person("Man", "Fred")),
	    	new Geisternetz(2, 20, 200f, 2000f, Status.GEMELDET, new Person("Al", "Bert", "+491234567")),
	    	new Geisternetz(3, 30, 300f, 3000f, Status.BERGUNG_BEVORSTEHEND, new Person("Hans", "Wurst", "+491234567")),
	    	new Geisternetz(4, 40, 400f, 4000f, Status.GEBORGEN, new Person("Axel", "Schweiß", "+491234567")),
	    	new Geisternetz(5, 50, 500f, 5000f, Status.VERSCHOLLEN, new Person("Clara", "Himmel", "+491234567"))
	         });


    public Platform()
    {
    	
    }

}

