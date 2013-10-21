package medizin.server.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class SkillLevel {

	private Integer levelNumber;
	
	/*public static List<SkillLevel> getSkillLevelByLevelNumber(int val)
	{
		EntityManager em = entityManager();
    	TypedQuery<SkillLevel> q = em.createQuery("SELECT o FROM SkillLevel o WHERE o.levelNumber = " + val, SkillLevel.class);
    	return q.getResultList();
	}*/
	
}
