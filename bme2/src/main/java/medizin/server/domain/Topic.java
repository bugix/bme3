package medizin.server.domain;

import java.util.List;

import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Topic {

	@Size(max = 1024)
	private String topicDesc;
	
	@ManyToOne
	private ClassificationTopic classificationTopic;
	
	public static List<Topic> findTopicByClassificationTopicId(Long classificationTopicId)
	{
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Topic> criteriaQuery = criteriaBuilder.createQuery(Topic.class);
		Root<Topic> from = criteriaQuery.from(Topic.class);
		
		if (classificationTopicId != null)
			criteriaQuery.where(criteriaBuilder.equal(from.get("classificationTopic").get("id"), classificationTopicId));
		
		// Added this to show topic order by description as ASC - Manish.
		criteriaQuery.orderBy(criteriaBuilder.asc(from.get("topicDesc")));
		TypedQuery<Topic> query = entityManager().createQuery(criteriaQuery);
		return query.getResultList();
	}
}
