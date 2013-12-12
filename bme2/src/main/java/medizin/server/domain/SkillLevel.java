package medizin.server.domain;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class SkillLevel {

	private static Logger log = Logger.getLogger(SkillLevel.class);
	
	private Integer levelNumber;
	
	/*public static List<SkillLevel> getSkillLevelByLevelNumber(int val)
	{
		EntityManager em = entityManager();
    	TypedQuery<SkillLevel> q = em.createQuery("SELECT o FROM SkillLevel o WHERE o.levelNumber = " + val, SkillLevel.class);
    	return q.getResultList();
	}*/
	
	public static List<SkillLevel> findAllSkillLevelsByLevelASC(){
    	
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<SkillLevel> criteriaQuery = criteriaBuilder.createQuery(SkillLevel.class);
		Root<SkillLevel> from = criteriaQuery.from(SkillLevel.class);
		criteriaQuery.orderBy(criteriaBuilder.asc(from.get("levelNumber")));
		 
		TypedQuery<SkillLevel> query = entityManager().createQuery(criteriaQuery);
	    log.info("ADVANCED QUERY : " + query.unwrap(Query.class).getQueryString());
	    return query.getResultList();
    	
    }
	
}
