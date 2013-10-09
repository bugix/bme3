package medizin.server.domain;

import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Appliance {

	@Size(max = 3)
	private String shortcut;
	
	/*public static List<Appliance> getAppliacneByShortcut(String val)
	{
		EntityManager em = entityManager();
    	TypedQuery<Appliance> q = em.createQuery("SELECT o FROM Appliance o WHERE o.shortcut LIKE :val", Appliance.class);
     	q.setParameter("val", "%" + val + "%");
     	return q.getResultList();
	}*/
}
