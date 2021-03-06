package medizin.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.FlushModeType;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import medizin.shared.AccessRights;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class UserAccessRights {

	private static Logger log = Logger.getLogger(UserAccessRights.class);
	
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
			   
			   /*log.info("~~QUERY : " + query.unwrap(Query.class).getQueryString());*/
			   
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
			   query.setFirstResult(start);
			   query.setMaxResults(length);
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
		   	Person person = Person.findLoggedPersonByShibId();
			Institution institution = Institution.myGetInstitution();
			
		   	if (person == null || institution == null)
				throw new IllegalArgumentException("The person and institution arguments are required");
			
	    	if (person.getIsAdmin())
	    		return true;
	    	
	    	Long instId = institution.getId();
    		
	    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
	  		CriteriaQuery<UserAccessRights> criteriaQuery = criteriaBuilder.createQuery(UserAccessRights.class);
	  		Root<UserAccessRights> from = criteriaQuery.from(UserAccessRights.class);
	  		
	  		Predicate pre1 = criteriaBuilder.equal(from.get("person").get("id"), person.getId());
	  		
	  		Predicate pre2 = criteriaBuilder.equal(from.get("accRights"), AccessRights.AccPrimaryAdmin.ordinal());
	  		Predicate pre3 = criteriaBuilder.equal(from.get("accRights"), AccessRights.AccSecondaryAdmin.ordinal());
	  		
	  		Predicate pre4 = criteriaBuilder.equal(from.get("institution").get("id"), instId);;  		
	  		
	  		criteriaQuery.where(criteriaBuilder.and(pre1,pre4,criteriaBuilder.or(pre2,pre3)));
	  		
	  		TypedQuery<UserAccessRights> query = entityManager().createQuery(criteriaQuery);
	  		
	  		query.setFlushMode(FlushModeType.COMMIT);
	  		
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
			
			Predicate ansPre1 = criteriaBuilder.equal(ansFrom.get("autor").get("id"), personId);
			Predicate ansPre2 = criteriaBuilder.equal(ansFrom.get("rewiewer").get("id"), personId);
			
			ansCq.where(ansCb.or(ansPre1, ansPre2));
			
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
			log.error(e.getMessage(), e);
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
	
	//check if user has access to particular institution if yes return UserAccessRight else null
	public static UserAccessRights findUserAccessRightsByPersonAndInstitution(Person user,Institution institution)
	{
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<UserAccessRights> cq = cb.createQuery(UserAccessRights.class);
		Root<UserAccessRights> from = cq.from(UserAccessRights.class);
		Predicate pre1 = cb.equal(from.get("person"), user);
		Predicate pre2 = cb.equal(from.get("institution"), institution);
		
		cq.where(cb.or(pre1,pre2));
		TypedQuery<UserAccessRights> query = entityManager().createQuery(cq);
		
		List<UserAccessRights> userAccessRights=query.getResultList();
		
		if(userAccessRights.size()==0)
			return null;
		else
		{
			return userAccessRights.get(0);
		}
	}
	
	//find question access(any access)
	
	public static List<Question> findQuestionByPerson(Person person)
	{
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Question> cq = cb.createQuery(Question.class);
		Root<UserAccessRights> from = cq.from(UserAccessRights.class);
		Predicate p1=cb.equal(from.get("person"), person);
		Predicate p2=cb.isNotNull(from.get("question"));
		cq.where(cb.and(p1,p2));
		cq.distinct(true);
		cq.select(from.<Question>get("question"));
		TypedQuery<Question> query = entityManager().createQuery(cq);
		List<Question> questions=query.getResultList();
		return questions;
		
	}
	
	public static List<UserAccessRights> findQuestionEventAccessByPerson(Long personId)
	{
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<UserAccessRights> criteriaQuery = criteriaBuilder.createQuery(UserAccessRights.class);
		Root<UserAccessRights> from = criteriaQuery.from(UserAccessRights.class);
		
		Predicate pre1 = criteriaBuilder.equal(from.get("person"), personId);
		Predicate pre2 = from.get("accRights").in(AccessRights.AccRead, AccessRights.AccWrite, AccessRights.AccAddQuestions);
		Predicate pre3 = from.get("questionEvent").isNotNull();
		
		Predicate predicate = criteriaBuilder.and(pre1, pre2, pre3);
		criteriaQuery.where(predicate);
		
		TypedQuery<UserAccessRights> query = entityManager().createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public static List<UserAccessRights> findQuestionAccessByPerson(Long personId)
	{
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<UserAccessRights> criteriaQuery = criteriaBuilder.createQuery(UserAccessRights.class);
		Root<UserAccessRights> from = criteriaQuery.from(UserAccessRights.class);
		
		Predicate pre1 = criteriaBuilder.equal(from.get("person"), personId);
		Predicate pre2 = from.get("accRights").in(AccessRights.AccRead, AccessRights.AccWrite, AccessRights.AccAddAnswers);
		Predicate pre3 = from.get("question").isNotNull();
		
		Predicate predicate = criteriaBuilder.and(pre1, pre2, pre3);
		criteriaQuery.where(predicate);
		
		TypedQuery<UserAccessRights> query = entityManager().createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public static Boolean persistQuestionEventAccess(AccessRights rights, Long personId, Long questionEventId)
	{
		if (rights.equals(AccessRights.AccWrite))
		{
			UserAccessRights userAccessRights = checkAccessRightsForQuestionEvent(AccessRights.AccRead, personId, questionEventId);
			if (userAccessRights != null)
				userAccessRights.remove();
		}
		
		UserAccessRights userAccRights = checkAccessRightsForQuestionEvent(rights, personId, questionEventId);
		
		if (userAccRights != null)
			return false;
		else
		{
			UserAccessRights userAccessRights = new UserAccessRights();
			userAccessRights.setAccRights(rights);
			userAccessRights.setPerson(Person.findPerson(personId));
			userAccessRights.setQuestionEvent(QuestionEvent.findQuestionEvent(questionEventId));
			userAccessRights.persist();
			return true;
		}
	}
	
	public static Boolean persistQuestionAccess(AccessRights rights, Long personId, Long questionId)
	{
		if (rights.equals(AccessRights.AccWrite))
		{
			UserAccessRights readRights = checkAccessRightsForQuestion(AccessRights.AccRead, personId, questionId);
			
			if (readRights != null)
				readRights.remove();			
		}
		
		UserAccessRights userAccssRight = checkAccessRightsForQuestion(rights, personId, questionId);
		
		if (userAccssRight != null)
			return false;
		else
		{
			UserAccessRights userAccessRights = new UserAccessRights();
			userAccessRights.setAccRights(rights);
			userAccessRights.setPerson(Person.findPerson(personId));
			userAccessRights.setQuestion(Question.findQuestion(questionId));
			userAccessRights.persist();
			return true;
		}
	}

	public static UserAccessRights checkAccessRightsForQuestionEvent(AccessRights rights, Long personId, Long questionEventId)
	{
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<UserAccessRights> criteriaQuery = criteriaBuilder.createQuery(UserAccessRights.class);
		Root<UserAccessRights> from = criteriaQuery.from(UserAccessRights.class);
		
		Predicate pre1 = criteriaBuilder.equal(from.get("person").get("id"), personId);
		Predicate pre2 = criteriaBuilder.equal(from.get("questionEvent").get("id"), questionEventId);
		Predicate pre3 = criteriaBuilder.equal(from.get("accRights"), rights);
		
		Predicate predicate = criteriaBuilder.and(pre1, pre2, pre3);
		
		criteriaQuery.where(predicate);
		
		TypedQuery<UserAccessRights> query = entityManager().createQuery(criteriaQuery);
		
		if (query.getResultList() != null && query.getResultList().isEmpty() == false)
			return query.getResultList().get(0);
		else
			return null;		
	}
	
	public static UserAccessRights checkAccessRightsForQuestion(AccessRights rights, Long personId, Long questionId)
	{
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<UserAccessRights> criteriaQuery = criteriaBuilder.createQuery(UserAccessRights.class);
		Root<UserAccessRights> from = criteriaQuery.from(UserAccessRights.class);
		
		Predicate pre1 = criteriaBuilder.equal(from.get("person").get("id"), personId);
		Predicate pre2 = criteriaBuilder.equal(from.get("question").get("id"), questionId);
		Predicate pre3 = criteriaBuilder.equal(from.get("accRights"), rights);
		
		Predicate predicate = criteriaBuilder.and(pre1, pre2, pre3);
		
		criteriaQuery.where(predicate);
		
		TypedQuery<UserAccessRights> query = entityManager().createQuery(criteriaQuery);
		
		if (query.getResultList() != null && query.getResultList().isEmpty() == false)
			return query.getResultList().get(0);
		else
			return null;
	}

	public static boolean checkHasAnyRightsOnQuestionOrQuestionEvent(Long personId, QuestionEvent questEvent, Question question) {
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<UserAccessRights> from = criteriaQuery.from(UserAccessRights.class);
		criteriaQuery.select(criteriaBuilder.count(from));
		
		Predicate pre1 = criteriaBuilder.equal(from.get("person").get("id"), personId);
		
		Predicate questionEventPre2 = criteriaBuilder.equal(from.get("questionEvent").get("id"), questEvent.getId());
		Predicate questionEventPre3 = criteriaBuilder.equal(from.get("accRights"), AccessRights.AccWrite);
		Predicate questionEventPre = criteriaBuilder.and(questionEventPre2,questionEventPre3);
		
		if(question.getId() != null) {
			Predicate questionPre2 = criteriaBuilder.equal(from.get("question").get("id"), question.getId());
			Predicate questionPre3 = criteriaBuilder.equal(from.get("accRights"), AccessRights.AccWrite);
			Predicate questionPre = criteriaBuilder.and(questionPre2,questionPre3);
			questionEventPre = criteriaBuilder.or(questionEventPre,questionPre);
		}
		
		Predicate predicate = criteriaBuilder.and(pre1, questionEventPre);
		
		criteriaQuery.where(predicate);
		
		TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);
		query.setFlushMode(FlushModeType.COMMIT);
		
		if(query.getSingleResult() > 0) {
			return true;
		} else {
			return false;
		}
	}
}
