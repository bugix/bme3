package medizin.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import medizin.client.shared.AccessRights;

import org.hibernate.Query;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.allen_sauer.gwt.log.client.Log;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class UserAccessRights {

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
	        TypedQuery<Long> q = em.createQuery("SELECT count(qaccess) FROM UserAccessRights qaccess " + 
	        		"INNER JOIN qaccess.questionEvent qevent WHERE qaccess.person = :person", Long.class);
	        q.setParameter("person", person);
	        return q.getSingleResult();
	    }

	    public static List<UserAccessRights> findQuestionEventAccessByPersonNonRooNonRoo(java.lang.Long personId, int firstResult, int maxResults) {
	        Person person = Person.findPerson(personId);
	        if (person == null) throw new IllegalArgumentException("The institution argument is required");
	        EntityManager em = QuestionEvent.entityManager();
	        TypedQuery<UserAccessRights> q = em.createQuery("SELECT qaccess FROM UserAccessRights qaccess " + 
	        		"INNER JOIN qaccess.questionEvent qevent WHERE qaccess.person = :person", UserAccessRights.class).setFirstResult(firstResult).setMaxResults(maxResults);
//	        TypedQuery<QuestionEvent> q = em.createQuery("SELECT QuestionEvent FROM QuestionEvent AS questionevent ", QuestionEvent.class).setFirstResult(firstResult).setMaxResults(maxResults);
	        q.setParameter("person", person);
	        return q.getResultList();
	    }
	    
	    
	    public static List<UserAccessRights> findQuestionAccessQuestionByPersonNonRoo(java.lang.Long personId, int firstResult, int maxResults) {
	        Person person = Person.findPerson(personId);
	        if (person == null) throw new IllegalArgumentException("The institution argument is required");
	        EntityManager em = QuestionEvent.entityManager();
	        TypedQuery<UserAccessRights> q = em.createQuery("SELECT qaccess FROM UserAccessRights qaccess " + 
	        		"INNER JOIN qaccess.question question WHERE qaccess.person = :person", UserAccessRights.class).setFirstResult(firstResult).setMaxResults(maxResults);
//	        TypedQuery<QuestionEvent> q = em.createQuery("SELECT QuestionEvent FROM QuestionEvent AS questionevent ", QuestionEvent.class).setFirstResult(firstResult).setMaxResults(maxResults);
	        q.setParameter("person", person);
	        return q.getResultList();
	    }
	    
		   public static long countQuestionAccessQuestionByPersonNonRoo(java.lang.Long personId) {
		        Person person = Person.findPerson(personId);
		        if (person == null) throw new IllegalArgumentException("The institution argument is required");
		        EntityManager em = QuestionEvent.entityManager();
		        TypedQuery<Long> q = em.createQuery("SELECT count(qaccess) FROM UserAccessRights qaccess " + 
		        		"INNER JOIN qaccess.question question WHERE qaccess.person = :person", Long.class);
		        q.setParameter("person", person);
		        return q.getSingleResult();
		    }
		   
		   public static long countInstiuteAccessByPerson(java.lang.Long personId){
			   EntityManager em = QuestionEvent.entityManager();
			   CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			   CriteriaQuery<UserAccessRights> criteriaQuery = criteriaBuilder.createQuery(UserAccessRights.class);
			   Root<UserAccessRights> from = criteriaQuery.from(UserAccessRights.class);
			   
			   Predicate pre1 = criteriaBuilder.equal(from.get("person").get("id"), personId);
			   Predicate pre2 = criteriaBuilder.isNotNull(from.get("institution"));
			   
			   criteriaQuery.where(criteriaBuilder.and(pre1,pre2));
			   
			   TypedQuery<UserAccessRights> query = em.createQuery(criteriaQuery);
			   
			   Log.info("~~QUERY : " + query.unwrap(Query.class).getQueryString());
			   
			   return query.getResultList().size();
		   }
		   
		   public static List<UserAccessRights> findInstiuteAccessByPerson(java.lang.Long personId, int start, int length){
			   EntityManager em = QuestionEvent.entityManager();
			   CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			   CriteriaQuery<UserAccessRights> criteriaQuery = criteriaBuilder.createQuery(UserAccessRights.class);
			   Root<UserAccessRights> from = criteriaQuery.from(UserAccessRights.class);
			   
			   Predicate pre1 = criteriaBuilder.equal(from.get("person").get("id"), personId);
			   Predicate pre2 = criteriaBuilder.isNotNull(from.get("institution"));
			   
			   criteriaQuery.where(criteriaBuilder.and(pre1,pre2));
			   
			   TypedQuery<UserAccessRights> query = em.createQuery(criteriaQuery);
			   
			   return query.getResultList();
		   }
		   
	   
	  /* public static Person checkInstitutionalAdmin(Long personId)
	    {
	    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
	  		CriteriaQuery<UserAccessRights> criteriaQuery = criteriaBuilder.createQuery(UserAccessRights.class);
	  		Root<UserAccessRights> from = criteriaQuery.from(UserAccessRights.class);
	  		
	  		Predicate pre1 = criteriaBuilder.equal(from.get("person").get("id"), personId);
	  		Predicate pre2 = criteriaBuilder.equal(from.get("accRights"), AccessRights.AccPrimaryAdmin.ordinal());
	  		Predicate pre3 = criteriaBuilder.isNotNull(from.get("institution"));  		
	  		criteriaQuery.where(criteriaBuilder.and(pre1,pre2,pre3));
	  		
	  		TypedQuery<UserAccessRights> query = entityManager().createQuery(criteriaQuery);
	  		
	  		UserAccessRights questionAccess = query.getSingleResult();
	  		
	  		return questionAccess.getPerson();
	    }*/
	   
	   public static Boolean checkInstitutionalAdmin()
	    {
		   	HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
	    	HttpSession session1 = RequestFactoryServlet.getThreadLocalRequest().getSession();
	    	
	    	Person person = Person.findPersonByShibId(session.getAttribute("shibdId").toString());
	    	
	    	if (person.getIsAdmin())
	    		return true;
	    	
	    	Long instId = (Long) session1.getAttribute("institutionId");
    		
	    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
	  		CriteriaQuery<UserAccessRights> criteriaQuery = criteriaBuilder.createQuery(UserAccessRights.class);
	  		Root<UserAccessRights> from = criteriaQuery.from(UserAccessRights.class);
	  		
	  		Predicate pre1 = criteriaBuilder.equal(from.get("person").get("id"), person.getId());
	  		
	  		Predicate pre2 = criteriaBuilder.equal(from.get("accRights"), AccessRights.AccPrimaryAdmin.ordinal());
	  		Predicate pre3 = criteriaBuilder.equal(from.get("accRights"), AccessRights.AccSecondaryAdmin.ordinal());
	  		
	  		Predicate pre4 = criteriaBuilder.equal(from.get("institution").get("id"), instId);;  		
	  		
	  		criteriaQuery.where(criteriaBuilder.and(pre1,pre4,criteriaBuilder.or(pre2,pre3)));
	  		
	  		TypedQuery<UserAccessRights> query = entityManager().createQuery(criteriaQuery);
	  		
	  		List<UserAccessRights> list = query.getResultList();
	  		
	  		if (list.size() > 0)	  		
	  			return true;
	  		else
	  			return false;
	    }
	   
	   public static List<Institution> findInstituionFromQuestionAccessByPerson(Long personId)
	   {
		  List<Institution> institutionList = new ArrayList<Institution>();
		try {
			
			CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
			CriteriaQuery<UserAccessRights> criteriaQuery = criteriaBuilder.createQuery(UserAccessRights.class);
			Root<UserAccessRights> from = criteriaQuery.from(UserAccessRights.class);

			Predicate pre1 = criteriaBuilder.equal(	from.get("person").get("id"), personId);
			criteriaQuery.where(pre1);
			
			TypedQuery<UserAccessRights> query = entityManager().createQuery(criteriaQuery);

			List<UserAccessRights> questionAccessList = query.getResultList();

			// List<Institution> institutionList = new ArrayList<Institution>();

			for (UserAccessRights questionAccess : questionAccessList) {
				if (questionAccess.getInstitution() != null) {
					if (!institutionList.contains(questionAccess.getInstitution())) {
						institutionList.add(questionAccess.getInstitution());
					}
				} else {
					if (questionAccess.getQuestionEvent() != null && questionAccess.getQuestionEvent().getInstitution() != null)
					{
						if (!institutionList.contains(questionAccess.getQuestionEvent().getInstitution())) {
							institutionList.add(questionAccess.getQuestionEvent().getInstitution());
						}
					}
					
					if (questionAccess.getQuestionEvent() == null && questionAccess.getQuestion() != null && questionAccess.getQuestion().getQuestEvent() != null && questionAccess.getQuestion().getQuestEvent().getInstitution() != null)
					{
						if (!institutionList.contains(questionAccess.getQuestion().getQuestEvent().getInstitution())) {
							institutionList.add(questionAccess.getQuestion().getQuestEvent().getInstitution());
						}
					}
				}
			}
			
			CriteriaBuilder cb = entityManager().getCriteriaBuilder();
			CriteriaQuery<Question> cq = cb.createQuery(Question.class);
			Root<Question> fromQue = cq.from(Question.class);
			
			Predicate quePre1 = criteriaBuilder.equal(fromQue.get("autor").get("id"), personId);
			Predicate quePre2 = criteriaBuilder.equal(fromQue.get("rewiewer").get("id"), personId);
			
			cq.where(cb.or(quePre1, quePre2));
			
			TypedQuery<Question> q = entityManager().createQuery(cq);
			
			for (Question question : q.getResultList())
			{
				if (question.getQuestEvent() != null && question.getQuestEvent().getInstitution() !=null)
				{
					if (!institutionList.contains(question.getQuestEvent().getInstitution())) {
						institutionList.add(question.getQuestEvent().getInstitution());
					}
				}
			}
			
			CriteriaBuilder ansCb = entityManager().getCriteriaBuilder();
			CriteriaQuery<Answer> ansCq = ansCb.createQuery(Answer.class);
			Root<Answer> ansFrom = ansCq.from(Answer.class);
			
			Predicate ansPre2 = criteriaBuilder.equal(ansFrom.get("rewiewer").get("id"), personId);
			
			cq.where(quePre2);
			
			TypedQuery<Answer> ansq = entityManager().createQuery(ansCq);
			
			for (Answer answer : ansq.getResultList())
			{
				if (answer.getQuestion() !=null && answer.getQuestion().getQuestEvent() != null && answer.getQuestion().getQuestEvent().getInstitution() !=null)
				{
					if (!institutionList.contains(answer.getQuestion().getQuestEvent().getInstitution())) {
						institutionList.add(answer.getQuestion().getQuestEvent().getInstitution());
					}
				}
			}
			
			//System.out.println("result size :" + institutionList.size());
			 return institutionList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		  //return institutionList;  
	   }
	   
	  public static List<UserAccessRights> checkAddAnswerRightsByQuestionAndPerson(Long personid, Long questionid)
	  {
		  CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		  CriteriaQuery<UserAccessRights> cq = cb.createQuery(UserAccessRights.class);
		  Root<UserAccessRights> from = cq.from(UserAccessRights.class);
			
		  Predicate pre1 = cb.equal(from.get("person").get("id"), personid);
		  Predicate pre3 = cb.equal(from.get("question").get("id"), questionid);
		  //Predicate pre4 = cb.equal(from.get("accRights"), AccessRights.AccAddAnswers.ordinal());
		  
		  cq.where(cb.and(pre1,pre3));
		  
		  TypedQuery<UserAccessRights> query = entityManager().createQuery(cq);
		 
		  return query.getResultList();
	  }

	public static List<UserAccessRights> findUserAccessRightsByQuestion(Long questionId) {
		
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<UserAccessRights> cq = cb.createQuery(UserAccessRights.class);
		Root<UserAccessRights> from = cq.from(UserAccessRights.class);
		Predicate pre = cb.equal(from.get("question").get("id"), questionId);
		cq.where(pre);
		
		TypedQuery<UserAccessRights> query = entityManager().createQuery(cq);
	 
		return query.getResultList();
	}
}
