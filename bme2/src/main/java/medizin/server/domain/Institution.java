package medizin.server.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Institution {

    @NotNull
    @Column(unique = true)
    @Size(min = 3, max = 60)
    private String institutionName;
    
    public void mySetCurrentInstitution(){
    	HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		session.setAttribute("institutionId", this.getId());
    }
    
    public static void fillCurrentInstitutionNull()
    {
    	HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		session.setAttribute("institutionId", null);
    }

    public static Institution myGetInstitutionToWorkWith(){
    	HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		Long instId = (Long) session.getAttribute("institutionId");
		return Institution.findInstitution(instId);
    }
    
    public static List<Institution> findInstitutionByName(String text, int start, int length)
    {
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
 		CriteriaQuery<Institution> criteriaQuery = criteriaBuilder.createQuery(Institution.class);
 		Root<Institution> from = criteriaQuery.from(Institution.class);
 		Expression<String> exp = from.get("institutionName");
 		criteriaQuery.where(criteriaBuilder.like(exp, "%" + text +"%"));
 		
 		TypedQuery<Institution> query = entityManager().createQuery(criteriaQuery);
 		query.setFirstResult(start);
 		query.setMaxResults(length);
 		
 		return query.getResultList();
    }
    
    public static Long countInstitutionByName(String text)
    {
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
 		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
 		Root<Institution> from = criteriaQuery.from(Institution.class);
 		criteriaQuery.select(criteriaBuilder.count(from)); 		
 		Expression<String> exp = from.get("institutionName");
 		criteriaQuery.where(criteriaBuilder.like(exp, "%" + text +"%"));
 		
 		TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);
 		
 		return query.getSingleResult();
    }
}
