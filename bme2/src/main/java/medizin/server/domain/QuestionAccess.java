package medizin.server.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import medizin.client.shared.AccessRights;

import org.hibernate.Query;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.allen_sauer.gwt.log.client.Log;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class QuestionAccess {

    @NotNull
    @Enumerated
    private AccessRights accRights;

    @NotNull
    @ManyToOne
    private Person person;

    @ManyToOne
    private Question question;

    @ManyToOne
    private QuestionEvent questionEvent;
    
    @ManyToOne
    private Institution institution;
    
	   public static long countQuestionEventAccessByPersonNonRoo(java.lang.Long personId) {
	        Person person = Person.findPerson(personId);
	        if (person == null) throw new IllegalArgumentException("The institution argument is required");
	        EntityManager em = QuestionEvent.entityManager();
	        TypedQuery<Long> q = em.createQuery("SELECT count(qaccess) FROM QuestionAccess qaccess " + 
	        		"INNER JOIN qaccess.questionEvent qevent WHERE qaccess.person = :person", Long.class);
	        q.setParameter("person", person);
	        return q.getSingleResult();
	    }

	    public static List<QuestionAccess> findQuestionEventAccessByPersonNonRooNonRoo(java.lang.Long personId, int firstResult, int maxResults) {
	        Person person = Person.findPerson(personId);
	        if (person == null) throw new IllegalArgumentException("The institution argument is required");
	        EntityManager em = QuestionEvent.entityManager();
	        TypedQuery<QuestionAccess> q = em.createQuery("SELECT qaccess FROM QuestionAccess qaccess " + 
	        		"INNER JOIN qaccess.questionEvent qevent WHERE qaccess.person = :person", QuestionAccess.class).setFirstResult(firstResult).setMaxResults(maxResults);
//	        TypedQuery<QuestionEvent> q = em.createQuery("SELECT QuestionEvent FROM QuestionEvent AS questionevent ", QuestionEvent.class).setFirstResult(firstResult).setMaxResults(maxResults);
	        q.setParameter("person", person);
	        return q.getResultList();
	    }
	    
	    
	    public static List<QuestionAccess> findQuestionAccessQuestionByPersonNonRoo(java.lang.Long personId, int firstResult, int maxResults) {
	        Person person = Person.findPerson(personId);
	        if (person == null) throw new IllegalArgumentException("The institution argument is required");
	        EntityManager em = QuestionEvent.entityManager();
	        TypedQuery<QuestionAccess> q = em.createQuery("SELECT qaccess FROM QuestionAccess qaccess " + 
	        		"INNER JOIN qaccess.question question WHERE qaccess.person = :person", QuestionAccess.class).setFirstResult(firstResult).setMaxResults(maxResults);
//	        TypedQuery<QuestionEvent> q = em.createQuery("SELECT QuestionEvent FROM QuestionEvent AS questionevent ", QuestionEvent.class).setFirstResult(firstResult).setMaxResults(maxResults);
	        q.setParameter("person", person);
	        return q.getResultList();
	    }
	    
		   public static long countQuestionAccessQuestionByPersonNonRoo(java.lang.Long personId) {
		        Person person = Person.findPerson(personId);
		        if (person == null) throw new IllegalArgumentException("The institution argument is required");
		        EntityManager em = QuestionEvent.entityManager();
		        TypedQuery<Long> q = em.createQuery("SELECT count(qaccess) FROM QuestionAccess qaccess " + 
		        		"INNER JOIN qaccess.question question WHERE qaccess.person = :person", Long.class);
		        q.setParameter("person", person);
		        return q.getSingleResult();
		    }
		   
		   public static long countInstiuteAccessByPerson(java.lang.Long personId){
			   EntityManager em = QuestionEvent.entityManager();
			   CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			   CriteriaQuery<QuestionAccess> criteriaQuery = criteriaBuilder.createQuery(QuestionAccess.class);
			   Root<QuestionAccess> from = criteriaQuery.from(QuestionAccess.class);
			   
			   Predicate pre1 = criteriaBuilder.equal(from.get("person").get("id"), personId);
			   Predicate pre2 = criteriaBuilder.isNotNull(from.get("institution"));
			   
			   criteriaQuery.where(criteriaBuilder.and(pre1,pre2));
			   
			   TypedQuery<QuestionAccess> query = em.createQuery(criteriaQuery);
			   
			   Log.info("~~QUERY : " + query.unwrap(Query.class).getQueryString());
			   
			   return query.getResultList().size();
		   }
		   
		   public static List<QuestionAccess> findInstiuteAccessByPerson(java.lang.Long personId, int start, int length){
			   EntityManager em = QuestionEvent.entityManager();
			   CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			   CriteriaQuery<QuestionAccess> criteriaQuery = criteriaBuilder.createQuery(QuestionAccess.class);
			   Root<QuestionAccess> from = criteriaQuery.from(QuestionAccess.class);
			   
			   Predicate pre1 = criteriaBuilder.equal(from.get("person").get("id"), personId);
			   Predicate pre2 = criteriaBuilder.isNotNull(from.get("institution"));
			   
			   criteriaQuery.where(criteriaBuilder.and(pre1,pre2));
			   
			   TypedQuery<QuestionAccess> query = em.createQuery(criteriaQuery);
			   
			   return query.getResultList();
		   }
}
