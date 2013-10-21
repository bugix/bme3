package medizin.server.domain;

import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class MainClassification {
	
	@Size(max = 2)
	private String shortcut;
	
	private String description;
	
	/*public static List<MainClassification> findMainClassificationByShortCut(String val)
	{
		EntityManager em = entityManager();
    	TypedQuery<MainClassification> q = em.createQuery("SELECT o FROM MainClassification o WHERE o.shortcut LIKE :val", MainClassification.class);
     	q.setParameter("val", "%" + val + "%");
     	return q.getResultList();
	}*/
}
