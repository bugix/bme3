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
public class ClassificationTopic {
	
	@Size(max = 8)
	private String shortcut;
	
	private String description;
	
	@ManyToOne
	private MainClassification mainClassification;
	
	public static List<ClassificationTopic> findClassificationTopicByMainClassification(Long mainClassificationId)
	{
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<ClassificationTopic> criteriaQuery = criteriaBuilder.createQuery(ClassificationTopic.class);
		Root<ClassificationTopic> from = criteriaQuery.from(ClassificationTopic.class);
		
		if (mainClassificationId != null)
			criteriaQuery.where(criteriaBuilder.equal(from.get("mainClassification").get("id"), mainClassificationId));
		// Added this to show classification topic order by description as ASC - Manish.
		criteriaQuery.orderBy(criteriaBuilder.asc(from.get("description")));
		TypedQuery<ClassificationTopic> query = entityManager().createQuery(criteriaQuery);
		return query.getResultList();
	}
}

