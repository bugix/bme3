package medizin.server.domain;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Appliance {

	private static final Logger log = Logger.getLogger(Appliance.class);
	
	@Size(max = 3)
	private String shortcut;
	
	/*public static List<Appliance> getAppliacneByShortcut(String val)
	{
		EntityManager em = entityManager();
    	TypedQuery<Appliance> q = em.createQuery("SELECT o FROM Appliance o WHERE o.shortcut LIKE :val", Appliance.class);
     	q.setParameter("val", "%" + val + "%");
     	return q.getResultList();
	}*/
	
	public static List<Appliance> findAllAppliancesByShortcutASC(){
    	
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Appliance> criteriaQuery = criteriaBuilder.createQuery(Appliance.class);
		Root<Appliance> from = criteriaQuery.from(Appliance.class);
		criteriaQuery.orderBy(criteriaBuilder.asc(from.get("shortcut")));
		 
		TypedQuery<Appliance> query = entityManager().createQuery(criteriaQuery);
	    log.info("ADVANCED QUERY : " + query.unwrap(Query.class).getQueryString());
	    return query.getResultList();
    	
    }
}
