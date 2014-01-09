package medizin.server.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class QuestionSumPerPerson {

    @NotNull
    @ManyToOne
    private Person responsiblePerson;

    @NotNull
    @ManyToOne
    private QuestionEvent questionEvent;

    @NotNull
    @ManyToOne
    private Assesment assesment;

    @NotNull
    private Integer questionSum;
    
    @NotNull
    private Integer sort_order;
    
    public static long countQuestionSumPerPersonByAssesmentNonRoo(Long assesmentId) {
   	 Assesment assesment = Assesment.findAssesment(assesmentId);
	        if (assesment == null) throw new IllegalArgumentException("The assesment argument is required");
	        EntityManager em = QuestionSumPerPerson.entityManager();
	        TypedQuery<Long> q = em.createQuery("SELECT count(qsum) FROM QuestionSumPerPerson qsum " + 
	        		" WHERE qsum.assesment = :assesment", Long.class);
	        q.setParameter("assesment", assesment);
	        
	        return q.getSingleResult();
   }
   
   public static List<QuestionSumPerPerson> findQuestionSumPerPersonByAssesmentNonRoo(Long assesmentId, int start, int max) {
  	 Assesment assesment = Assesment.findAssesment(assesmentId);
	        if (assesment == null) throw new IllegalArgumentException("The assesment argument is required");
	        EntityManager em = QuestionSumPerPerson.entityManager();
	        TypedQuery<QuestionSumPerPerson> q = em.createQuery("SELECT qsum FROM QuestionSumPerPerson qsum " + 
	        		" WHERE qsum.assesment = :assesment ORDER BY qsum.sort_order", QuestionSumPerPerson.class).setFirstResult(start).setMaxResults(max);
	        q.setParameter("assesment", assesment);
	        
	        return q.getResultList();
  }
   
   public void moveUp(){
   	  if (this.entityManager == null) this.entityManager = entityManager();
   	  QuestionSumPerPerson questionSumPerPerson = findQuestionSumPerPersonByAssesmentAndSortorde(this.assesment, this.sort_order+1);
   	  if (questionSumPerPerson==null) return;
   	  questionSumPerPerson.setSort_order(sort_order);
   	  questionSumPerPerson.persist();
   	  setSort_order(sort_order+1);
   	  this.persist();
        // this.entityManager.persist(this);
   }

   public void moveDown(){
 	  if (this.entityManager == null) this.entityManager = entityManager();
 	  QuestionSumPerPerson questionSumPerPerson = findQuestionSumPerPersonByAssesmentAndSortorde(this.assesment, this.sort_order-1);
 	  if (questionSumPerPerson==null) return;
 	  questionSumPerPerson.setSort_order(sort_order);
 	  questionSumPerPerson.persist();
 	  setSort_order(sort_order-1);
 	  this.persist();
      // this.entityManager.persist(this);
 }
   
	public static QuestionSumPerPerson findQuestionSumPerPersonByAssesmentAndSortorde(Assesment assesment, int sort_order) {
		if (assesment == null) throw new IllegalArgumentException("The assesment argument is required");
       EntityManager em = QuestionSumPerPerson.entityManager();
       TypedQuery<QuestionSumPerPerson> q = em.createQuery("SELECT qsum FROM QuestionSumPerPerson qsum " + 
       		" WHERE qsum.assesment = :assesment AND qsum.sort_order = " + sort_order, QuestionSumPerPerson.class);
       q.setParameter("assesment", assesment);
       List<QuestionSumPerPerson> resultList = q.getResultList();
       if(resultList.isEmpty()) {
    	   return null;
       } else {
    	   return resultList.get(0);
       }
	}

	public static QuestionSumPerPerson findQuestionSumPerPersonByEventNonRoo(Long eventId) {
		
		QuestionEvent questionEvent = QuestionEvent.findQuestionEvent(eventId);

		if (questionEvent == null) throw new IllegalArgumentException("The questionEvent argument is required");
       EntityManager em = QuestionSumPerPerson.entityManager();
       TypedQuery<QuestionSumPerPerson> q = em.createQuery("SELECT qsum FROM QuestionSumPerPerson qsum " + 
       		" WHERE qsum.questionEvent = :questionEvent",  QuestionSumPerPerson.class);
       q.setParameter("questionEvent", questionEvent);
   

       return q.getSingleResult();
	}
	
	/* Implemented for top element to on left side of assesment question.
	 * 
	 * find % of Question Assigned to examiner for assesment*/
	public static List<QuestionSumPerPerson> findPercentageOfQuestionAssignedToExaminer(Assesment a,Person p)
	{
		EntityManager em=QuestionSumPerPerson.entityManager();
		if(p==null)
		p=Person.myGetLoggedPerson();
		
		TypedQuery<QuestionSumPerPerson> q = em.createQuery("SELECT qsum FROM QuestionSumPerPerson qsum " + 
	       		" WHERE responsiblePerson=:p and assesment=:a",  QuestionSumPerPerson.class);
	       q.setParameter("a", a);
	       q.setParameter("p", p);
		   
		
		return q.getResultList();
	}
	
	public static List<QuestionEvent> findQuestionEventOfExaminer(Assesment a, Person p)
	{
		EntityManager em=QuestionSumPerPerson.entityManager();
		if(p==null)
		p=Person.myGetLoggedPerson();
		
		TypedQuery<QuestionEvent> q = em.createQuery("SELECT qsum.questionEvent FROM QuestionSumPerPerson qsum " + 
	       		" WHERE qsum.responsiblePerson=:p and qsum.assesment=:a",  QuestionEvent.class);
	       q.setParameter("a", a);
	       q.setParameter("p", p);
		   
		
		return q.getResultList();

	}
	
	public static List<QuestionSumPerPerson> findQuestionSumPerPersonByLoggedUser(boolean isAdminOrInstitutionalAdmin)
	{
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");
				
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<QuestionSumPerPerson> criteriaQuery = criteriaBuilder.createQuery(QuestionSumPerPerson.class);
		Root<QuestionSumPerPerson> from = criteriaQuery.from(QuestionSumPerPerson.class);

		Date dateClosed =new Date();
    	Date dateOpen = new Date(); 
    	Boolean isClosed=false;
		
		if (isAdminOrInstitutionalAdmin)
		{
			//write code remaining
		}
		
		Predicate pre1 = criteriaBuilder.equal(from.get("responsiblePerson").get("id"), loggedUser.getId());
		Expression<Date> closedDateExp = from.get("assesment").get("dateClosed");
		Expression<Date> openDateExp = from.get("assesment").get("dateOpen");
		Predicate pre2 = criteriaBuilder.greaterThanOrEqualTo(closedDateExp, dateClosed);
		Predicate pre3 = criteriaBuilder.lessThanOrEqualTo(openDateExp, dateOpen);
		Predicate pre4 = criteriaBuilder.equal(from.get("isClosed"), isClosed);
		
		criteriaQuery.where(criteriaBuilder.and(pre1, pre2, pre3, pre4));
		
		TypedQuery<QuestionSumPerPerson> query = entityManager().createQuery(criteriaQuery);
		
		return query.getResultList();
	}
	
	public static Boolean removeAndUpdateOrder(QuestionSumPerPerson toDeleteQuestionSumPerPerson) {
		if(toDeleteQuestionSumPerPerson == null) {
			return false;
		}
		
		Long assesmentId = toDeleteQuestionSumPerPerson.getAssesment().getId();
		toDeleteQuestionSumPerPerson.remove();
		
		Assesment assesment = Assesment.findAssesment(assesmentId);
		
		List<QuestionSumPerPerson> questionSumPerPersons = Lists.newArrayList(assesment.getQuestionSumPerPerson());
		
		Collections.sort(questionSumPerPersons,new Comparator<QuestionSumPerPerson>() {

			@Override
			public int compare(QuestionSumPerPerson o1, QuestionSumPerPerson o2) {
				return Ints.compare(o1.getSort_order(), o2.getSort_order());
			}
		});
		
		int order = 1;
		for (QuestionSumPerPerson questionSumPerPerson : questionSumPerPersons) {
			questionSumPerPerson.setSort_order(order);
			questionSumPerPerson.persist();
			order+=1;
		}
		return true;
	}
}
