package medizin.server.domain;

import java.util.List;

import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class MainQuestionSkill {
	
	private static Logger log = Logger.getLogger(MainQuestionSkill.class);
	
	@ManyToOne
	private Question question;
	
	@ManyToOne 
	private Skill skill;
	
	public static Integer countMainQuestionSkillByQuestionId(Long questionId)
	{
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<MainQuestionSkill> from = criteriaQuery.from(MainQuestionSkill.class);
		criteriaQuery.distinct(true);
		criteriaQuery.select(criteriaBuilder.count(from.get("id")));
		criteriaQuery.where(criteriaBuilder.equal(from.get("question").get("id"), questionId));
		TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);
		
		if (query.getSingleResult() != null)
			return query.getSingleResult().intValue();
		
		return 0;
	}
	
	public static List<MainQuestionSkill> findMainQuestionSkillByQuestionId(Long questionId, int start, int length)
	{
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<MainQuestionSkill> criteriaQuery = criteriaBuilder.createQuery(MainQuestionSkill.class);
		Root<MainQuestionSkill> from = criteriaQuery.from(MainQuestionSkill.class);
		criteriaQuery.distinct(true);
		criteriaQuery.where(criteriaBuilder.equal(from.get("question").get("id"), questionId));
		TypedQuery<MainQuestionSkill> query = entityManager().createQuery(criteriaQuery);
		query.setFirstResult(start);
		query.setMaxResults(length);
		return query.getResultList();
	}
}
