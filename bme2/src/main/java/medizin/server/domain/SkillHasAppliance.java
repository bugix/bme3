package medizin.server.domain;

import javax.persistence.ManyToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class SkillHasAppliance {

	@ManyToOne
	private Skill skill;
	
	@ManyToOne
	private Appliance appliance;
	
	/*public static List<SkillHasAppliance> findSkillHasApplianceBySkillAndAppliance(Skill val, Appliance appl)
	{
		EntityManager em = entityManager();
    	TypedQuery<SkillHasAppliance> q = em.createQuery("SELECT o FROM SkillHasAppliance o WHERE o.skill LIKE :val AND o.appliance LIKE :appl", SkillHasAppliance.class);
     	q.setParameter("val", val);
     	q.setParameter("appl", appl);
     	return q.getResultList();
	}*/
}
