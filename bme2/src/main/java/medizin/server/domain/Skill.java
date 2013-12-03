package medizin.server.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Skill {

	private Integer shortcut;
	
	@Size(max = 1024)
	private String description;
	
	@ManyToOne
	private Topic topic;
	
	@ManyToOne
	private SkillLevel skillLevel;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "skill",fetch=FetchType.LAZY)
	private Set<SkillHasAppliance> skillHasAppliances = new HashSet<SkillHasAppliance>();
	
	public static Integer countSkillBySearchCriteria(Long mainClassificationId, Long classificationTopicId, Long topicId, Long skillLevlId, Long applianceId)
	{
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Skill> from = criteriaQuery.from(Skill.class);
		criteriaQuery.select(criteriaBuilder.count(from.get("id")));
		
		Predicate mainPredicate = null;
		
		if (mainClassificationId != null)
		{
			Predicate pre1 = criteriaBuilder.equal(from.get("topic").get("classificationTopic").get("mainClassification").get("id"), mainClassificationId);
			mainPredicate = pre1;
		}
		
		if (classificationTopicId != null)
		{
			Predicate pre2 = criteriaBuilder.equal(from.get("topic").get("classificationTopic").get("id"), classificationTopicId);
			if (mainPredicate != null)
				mainPredicate = criteriaBuilder.and(mainPredicate, pre2);
			else
				mainPredicate = pre2;
		}
		
		if (topicId != null)
		{
			Predicate pre3 = criteriaBuilder.equal(from.get("topic").get("id"), topicId);
			if (mainPredicate != null)
				mainPredicate = criteriaBuilder.and(mainPredicate, pre3);
			else
				mainPredicate = pre3;
		}
		
		if (skillLevlId != null)
		{
			Predicate pre4 = criteriaBuilder.equal(from.get("skillLevel"), skillLevlId);
			if (mainPredicate != null)
				mainPredicate = criteriaBuilder.and(mainPredicate, pre4);
			else
				mainPredicate = pre4;
		}
		
		if (applianceId != null)
		{
			Subquery<SkillHasAppliance> subQuery = criteriaQuery.subquery(SkillHasAppliance.class);
			Root skillHasApplianceFrom = subQuery.from(SkillHasAppliance.class);
			subQuery.select(skillHasApplianceFrom.get("skill").get("id")).where(criteriaBuilder.equal(skillHasApplianceFrom.get("appliance").get("id"), applianceId));
			Predicate pre5 = criteriaBuilder.in(from.get("id")).value(subQuery);
			
			if (mainPredicate != null)
				mainPredicate = criteriaBuilder.and(mainPredicate, pre5);
			else
				mainPredicate = pre5; 
		}
		
		if (mainPredicate != null)
			criteriaQuery.where(mainPredicate);
		
		TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);
		
		if (query.getSingleResult() != null)
			return query.getSingleResult().intValue();
		
		return 0;
	}
	
	public static List<Skill> findSkillBySearchCriteria(int start, int length, Long mainClassificationId, Long classificationTopicId, Long topicId, Long skillLevlId, Long applianceId)
	{
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Skill> criteriaQuery = criteriaBuilder.createQuery(Skill.class);
		Root<Skill> from = criteriaQuery.from(Skill.class);
		
		Predicate mainPredicate = null;
		
		if (mainClassificationId != null)
		{
			Predicate pre1 = criteriaBuilder.equal(from.get("topic").get("classificationTopic").get("mainClassification").get("id"), mainClassificationId);
			mainPredicate = pre1;
		}
		
		if (classificationTopicId != null)
		{
			Predicate pre2 = criteriaBuilder.equal(from.get("topic").get("classificationTopic").get("id"), classificationTopicId);
			if (mainPredicate != null)
				mainPredicate = criteriaBuilder.and(mainPredicate, pre2);
			else
				mainPredicate = pre2;
		}
		
		if (topicId != null)
		{
			Predicate pre3 = criteriaBuilder.equal(from.get("topic").get("id"), topicId);
			if (mainPredicate != null)
				mainPredicate = criteriaBuilder.and(mainPredicate, pre3);
			else
				mainPredicate = pre3;
		}
		
		if (skillLevlId != null)
		{
			Predicate pre4 = criteriaBuilder.equal(from.get("skillLevel"), skillLevlId);
			if (mainPredicate != null)
				mainPredicate = criteriaBuilder.and(mainPredicate, pre4);
			else
				mainPredicate = pre4;
		}
		
		if (applianceId != null)
		{
			Subquery<SkillHasAppliance> subQuery = criteriaQuery.subquery(SkillHasAppliance.class);
			Root skillHasApplianceFrom = subQuery.from(SkillHasAppliance.class);
			subQuery.select(skillHasApplianceFrom.get("skill").get("id")).where(criteriaBuilder.equal(skillHasApplianceFrom.get("appliance").get("id"), applianceId));
			Predicate pre5 = criteriaBuilder.in(from.get("id")).value(subQuery);
			
			if (mainPredicate != null)
				mainPredicate = criteriaBuilder.and(mainPredicate, pre5);
			else
				mainPredicate = pre5; 
		}
		
		if (mainPredicate != null)
			criteriaQuery.where(mainPredicate);
		
		TypedQuery<Skill> query = entityManager().createQuery(criteriaQuery);
		query.setFirstResult(start);
		query.setMaxResults(length);
		
		return query.getResultList();
	}
}
