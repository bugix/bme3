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
public class MainClassification {
	
	private static Logger log = Logger.getLogger(Doctor.class);
	
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
	
	 public static List<MainClassification> findAllMainClassificationByDescASC(){
	    	
	    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
			CriteriaQuery<MainClassification> criteriaQuery = criteriaBuilder.createQuery(MainClassification.class);
			Root<MainClassification> from = criteriaQuery.from(MainClassification.class);
			criteriaQuery.orderBy(criteriaBuilder.asc(from.get("description")));
			 
			TypedQuery<MainClassification> query = entityManager().createQuery(criteriaQuery);
		    log.info("ADVANCED QUERY : " + query.unwrap(Query.class).getQueryString());
		    return query.getResultList();
	    	
	    }
}
