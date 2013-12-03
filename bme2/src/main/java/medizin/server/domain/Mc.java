package medizin.server.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Mc {

    @NotNull
    @Column(unique = true)
    @Size(min = 2, max = 50)
    private String mcName;

	public static Mc findByName(String mcName) {
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
    	CriteriaQuery<Mc> criteriaQuery = criteriaBuilder.createQuery(Mc.class);
    	Root<Mc> from = criteriaQuery.from(Mc.class);
    	Predicate pre1 = criteriaBuilder.equal(from.get("mcName"), mcName);
    	criteriaQuery.where(pre1);
		TypedQuery<Mc> query = entityManager().createQuery(criteriaQuery);
		List<Mc> resultList = query.getResultList();
		if(resultList != null && resultList.size() > 0) {
			return resultList.get(0);
		}
		return null;
	}
}
