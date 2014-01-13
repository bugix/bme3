package medizin.server.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import medizin.shared.AccessRights;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findQuestionEventsByInstitution" })
public class QuestionEvent {
	
	private static Logger log = Logger.getLogger(Question.class);
	
    @NotNull
    @Column(unique = true)
    @Size(min = 3, max = 60)
    private String eventName;

    @NotNull
    @ManyToOne
    private Institution institution;
    
    public static long countQuestionEventsByInstitutionNonRoo(java.lang.Long institutionId){

    	Institution inst = Institution.findInstitution(institutionId);

        if (inst == null) throw new IllegalArgumentException("The institution argument is required");
        EntityManager em = QuestionEvent.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT count(questionevent) FROM QuestionEvent AS questionevent WHERE questionevent.institution = :institution", Long.class);
        q.setParameter("institution", inst);
        return q.getSingleResult();
    }
    

	
	public static List<QuestionEvent> findAllQuestionEventsByQuestionTypeAndAssesmentID(Long assesmentId,  List<Long> questionTypesId) {
//		QuestionEvent qEvent = QuestionEvent.findQuestionEvent(assesmentId);
		
		
		
		Assesment assesment = Assesment.findAssesment(assesmentId);
		Iterator<Long> iter = questionTypesId.iterator();
		List<QuestionType> questionType = new ArrayList<QuestionType>();
		while (iter.hasNext()) {
			Long long1 = (Long) iter.next();
			questionType.add(QuestionType.findQuestionType(long1));
		}
		//QuestionType questionType = QuestionType.findQuestionType(questionTypeId);
		
        if (assesment == null) throw new IllegalArgumentException("The QuestionEvent argument is required");
        //if (questionType == null) throw new IllegalArgumentException("The QuestionT argument is required");
        EntityManager em = QuestionEvent.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT qevent FROM QuestionSumPerPerson AS qsum INNER JOIN qsum.questionEvent AS qevent WHERE qsum.questionEvent IN (" +
        		"SELECT DISTINCT queste FROM AssesmentQuestion" +
        		" assesq INNER JOIN assesq.question quest INNER JOIN " +
                "quest.questEvent queste WHERE assesq.assesment=:assesment AND ( ");
        
        Iterator<QuestionType> iterQuestType = questionType.iterator();
        int firstTime = 0;
        while (iterQuestType.hasNext()) {
			QuestionType questionType2 = (QuestionType) iterQuestType.next();
			if(firstTime>0){
				queryBuilder.append(" OR ");
			}
			queryBuilder.append(" quest.questionType=:questiontype" + questionType2.getId());
			firstTime++;
		} 
        
        
        
        
        queryBuilder.append(  " )) ORDER BY qsum.sort_order");
  
        
        TypedQuery<QuestionEvent> q = em.createQuery( queryBuilder.toString(), QuestionEvent.class);
        
        Iterator<QuestionType> iterQuestType2 = questionType.iterator();
        while (iterQuestType2.hasNext()) {
			QuestionType questionType2 = (QuestionType) iterQuestType2.next();
			q.setParameter("questiontype"+questionType2.getId(), questionType2);
		}
        
        q.setParameter("assesment", assesment);
        
        return q.getResultList();
    }
	

	
	

    
    
	public static List<QuestionEvent> findQuestionEventsByInstitutionNonRoo(java.lang.Long institutionId, int firstResult, int maxResults){
		Institution inst = Institution.findInstitution(institutionId);
	    if (inst == null) throw new IllegalArgumentException("The institution argument is required");
	    EntityManager em = QuestionEvent.entityManager();
	    TypedQuery<QuestionEvent> q = em.createQuery("SELECT QuestionEvent FROM QuestionEvent AS questionevent WHERE questionevent.institution = :institution", QuestionEvent.class).setFirstResult(firstResult).setMaxResults(maxResults);
	    q.setParameter("institution", inst);
	    return q.getResultList();
    }
//	public static long countQuestionEvents() {
//        return entityManager().createQuery("select count(o) from QuestionEvent o", Long.class).getSingleResult();
//    }
	
	   public static long countQuestionEventsByPersonNonRoo(java.lang.Long personId) {
	        Person person = Person.findPerson(personId);
	        if (person == null) throw new IllegalArgumentException("The institution argument is required");
	        EntityManager em = QuestionEvent.entityManager();
	        TypedQuery<Long> q = em.createQuery("SELECT count(qevent) FROM UserAccessRights qaccess " + 
	        		"INNER JOIN qaccess.questionEvent qevent WHERE qaccess.person = :person", Long.class);
	        q.setParameter("person", person);
	        return q.getSingleResult();
	    }

	    public static List<QuestionEvent> findQuestionEventsByPersonNonRoo(java.lang.Long personId, int firstResult, int maxResults) {
	        Person person = Person.findPerson(personId);
	        if (person == null) throw new IllegalArgumentException("The institution argument is required");
	        EntityManager em = QuestionEvent.entityManager();
	        TypedQuery<QuestionEvent> q = em.createQuery("SELECT qevent FROM UserAccessRights qaccess " + 
	        		"INNER JOIN qaccess.questionEvent qevent WHERE qaccess.person = :person", QuestionEvent.class).setFirstResult(firstResult).setMaxResults(maxResults);
//	        TypedQuery<QuestionEvent> q = em.createQuery("SELECT QuestionEvent FROM QuestionEvent AS questionevent ", QuestionEvent.class).setFirstResult(firstResult).setMaxResults(maxResults);
	        q.setParameter("person", person);
	        return q.getResultList();
	    }
	    
	    public static long countQuestionEventsByInstitutionOrEvent(java.lang.Long institutionId, String eventNameFilter){
	    	/*String queryStr = "SELECT count(qevent) FROM QuestionEvent qevent";
	    	Institution institution=null;
	    	if(institutionId!=null) {
	    		institution = Institution.findInstitution(institutionId);
	    		queryStr+=" WHERE institution = :institution";
	    	}
	    	if(!eventNameFilter.equals("")){
		    	if(institutionId==null) {
		    		queryStr+=" WHERE ";
		    	}
		    	else {
		    		queryStr+=" AND ";
		    	}
		    	queryStr+=" eventName LIKE '%" + eventNameFilter + "%'";
	    	}
	        
	        EntityManager em = QuestionEvent.entityManager();
	        TypedQuery<Long> q = em.createQuery(queryStr, Long.class);
	        if(institutionId!=null) {
	        	q.setParameter("institution", institution);
	        }
	        return q.getSingleResult();*/
	    	
	    	Person loggedPerson = Person.myGetLoggedPerson();
	    	Institution loggedInstitute = Institution.myGetInstitutionToWorkWith();
	    	
	    	if (loggedPerson == null || loggedInstitute == null)
	    		throw new IllegalArgumentException("Logged person or institution is null");
	    	
	    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
	    	CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
	    	Root<QuestionEvent> from = criteriaQuery.from(QuestionEvent.class);
	    	
	    	criteriaQuery.select(criteriaBuilder.count(from.get("id")));
	    	
	    	Predicate pre1 = null;
	    	
	    	if (loggedPerson.getIsAdmin())
	    	{
	    		if (institutionId != null)
	    		{
	    			pre1 = criteriaBuilder.equal(from.get("institution").get("id"), institutionId);
	    		}
	    	}
	    	else
	    	{
	    		pre1 = criteriaBuilder.equal(from.get("institution").get("id"), loggedInstitute.getId());	    
	    	}
	    	
	    	Expression<String> eventNameExp = from.get("eventName");
			Predicate pre2 = criteriaBuilder.like(eventNameExp, "%" + eventNameFilter + "%");
			
			if (pre1 == null)
				criteriaQuery.where(pre2);
			else	
				criteriaQuery.where(criteriaBuilder.and(pre1, pre2));
			
			TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);
			
			return query.getSingleResult();
	    }
		
	    public static List<QuestionEvent> findQuestionEventsByInstitutionOrEvent(java.lang.Long institutionId, String eventNameFilter, int firstResult, int maxResults){
	    	/*String queryStr = "SELECT qevent FROM QuestionEvent qevent";
	    	Institution institution=null;
	    	if(institutionId!=null) {
	    		institution = Institution.findInstitution(institutionId);
	    		queryStr+=" WHERE institution = :institution";
	    	}
	    	if(!eventNameFilter.equals("")){
		    	if(institutionId==null) {
		    		queryStr+=" WHERE ";
		    	}
		    	else {
		    		queryStr+=" AND ";
		    	}
		    	queryStr+=" eventName LIKE '%" + eventNameFilter + "%'";
	    	}
	        
	        EntityManager em = QuestionEvent.entityManager();
	        TypedQuery<QuestionEvent> q = em.createQuery( queryStr, QuestionEvent.class).setFirstResult(firstResult).setMaxResults(maxResults);
//	        TypedQuery<QuestionEvent> q = em.createQuery("SELECT QuestionEvent FROM QuestionEvent AS questionevent ", QuestionEvent.class).setFirstResult(firstResult).setMaxResults(maxResults);
	        if(institution!=null) {
	        	q.setParameter("institution", institution);
	        }
	        return q.getResultList();*/
	    	
	    	Person loggedPerson = Person.myGetLoggedPerson();
	    	Institution loggedInstitute = Institution.myGetInstitutionToWorkWith();
	    	
	    	if (loggedPerson == null || loggedInstitute == null)
	    		throw new IllegalArgumentException("Logged person or institution is null");
	    	
	    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
	    	CriteriaQuery<QuestionEvent> criteriaQuery = criteriaBuilder.createQuery(QuestionEvent.class);
	    	Root<QuestionEvent> from = criteriaQuery.from(QuestionEvent.class);
	    	
	    	Predicate pre1 = null;
	    	
	    	if (loggedPerson.getIsAdmin())
	    	{
	    		if (institutionId != null)
	    		{
	    			pre1 = criteriaBuilder.equal(from.get("institution").get("id"), institutionId);
	    		}
	    	}
	    	else
	    	{
	    		pre1 = criteriaBuilder.equal(from.get("institution").get("id"), loggedInstitute.getId());	    
	    	}
	    	
	    	Expression<String> eventNameExp = from.get("eventName");
			Predicate pre2 = criteriaBuilder.like(eventNameExp, "%" + eventNameFilter + "%");
			
			if (pre1 == null)
				criteriaQuery.where(pre2);
			else	
				criteriaQuery.where(criteriaBuilder.and(pre1, pre2));
			
			TypedQuery<QuestionEvent> query = entityManager().createQuery(criteriaQuery);
			
			return query.getResultList();
	    	
	    }
	    
	    public static List<QuestionEvent> findQuestionEventByInstitutionAndAccRights(Boolean isAdmin, Long personId, Long instId)
	    {
	    	CriteriaBuilder cb = entityManager().getCriteriaBuilder();
	    	CriteriaQuery<QuestionEvent> cq = cb.createQuery(QuestionEvent.class);
	    	Root<QuestionEvent> from = cq.from(QuestionEvent.class);
	    	
	    	Predicate pre1 = cb.equal(from.get("institution").get("id"), instId);
	    	
	    	if (!isAdmin)
	    	{
	    		Subquery<UserAccessRights> subQry = cq.subquery(UserAccessRights.class);
				Root queAccRoot = subQry.from(UserAccessRights.class);
				
				Predicate subP1 = cb.equal(queAccRoot.get("person").get("id"), personId);
				Predicate subP2 = cb.equal(queAccRoot.get("accRights"), AccessRights.AccAddQuestions.ordinal());
				
				subQry.select(queAccRoot.get("questionEvent").get("id")).where(cb.and(subP1, subP2));
				
				Predicate pre2 = cb.in(from.get("id")).value(subQry);
				
				pre1 = cb.and(pre1, pre2);
	    	}
	    	
	    	cq.where(pre1);
	    	
	    	TypedQuery<QuestionEvent> query = entityManager().createQuery(cq);
	    	
	    	return query.getResultList();
	    }
	    
	    public static List<QuestionEvent> findQuestionEventByInstitution(Institution institution)
	    {
	    	CriteriaBuilder cb = entityManager().getCriteriaBuilder();
	    	CriteriaQuery<QuestionEvent> cq = cb.createQuery(QuestionEvent.class);
	    	Root<QuestionEvent> from = cq.from(QuestionEvent.class);
	    	Predicate pre1 = cb.equal(from.get("institution"), institution);
	    	cq.where(pre1);
	    	cq.orderBy(cb.asc(from.get("eventName")));
	    	TypedQuery<QuestionEvent> query = entityManager().createQuery(cq);
	    	log.info("ADVANCED QUERY : " + query.unwrap(Query.class).getQueryString());
	    	return query.getResultList();
	    }
	    
	    public static List<QuestionEvent> findAllQuestionEventByLoggedPerson()
	    {
	    	Person loggedPerson = Person.myGetLoggedPerson();
	    	Institution loggedInstitute = Institution.myGetInstitutionToWorkWith();
	    	
	    	if (loggedPerson == null || loggedInstitute == null)
	    		throw new IllegalArgumentException("Logged person or institution is null");
	    	
	    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
	    	CriteriaQuery<QuestionEvent> criteriaQuery = criteriaBuilder.createQuery(QuestionEvent.class);
	    	Root<QuestionEvent> from = criteriaQuery.from(QuestionEvent.class);
	    	criteriaQuery.orderBy(criteriaBuilder.asc(from.get("eventName")));
	    	
	    	if (loggedPerson.getIsAdmin() == false)
	    	{
	    		Predicate pre1 = criteriaBuilder.equal(from.get("institution").get("id"), loggedInstitute.getId());
	    		criteriaQuery.where(pre1);
	    	}
	    	
	    	TypedQuery<QuestionEvent> query = entityManager().createQuery(criteriaQuery);
	    	
	    	return query.getResultList();
	    }
	    
	    @PrePersist
	    @PreUpdate
	    @PreRemove
	    public void preQuestionEventPersist()
	    {
	    	Person loggedPerson = Person.findLoggedPersonByShibId();
			Institution loggedInstitution = Institution.myGetInstitution();
			
			if (loggedPerson == null || loggedInstitution == null)
				throw new IllegalArgumentException("Logged person or instution may not be null");
			else if (loggedPerson != null && loggedPerson.getIsAdmin() == false && UserAccessRights.checkInstitutionalAdmin() == false)
				throw new IllegalArgumentException("Only overall admin or institutional admin can use this functionality");
	    }

		public static QuestionEvent findQuestionEventByName(String questionEvent) {
			
			CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
	    	CriteriaQuery<QuestionEvent> criteriaQuery = criteriaBuilder.createQuery(QuestionEvent.class);
	    	Root<QuestionEvent> from = criteriaQuery.from(QuestionEvent.class);
	    	Predicate pre1 = criteriaBuilder.equal(from.get("eventName"), questionEvent);
	    	criteriaQuery.where(pre1);
			TypedQuery<QuestionEvent> query = entityManager().createQuery(criteriaQuery);
			List<QuestionEvent> resultList = query.getResultList();
			if(resultList != null && resultList.size() > 0) {
				return resultList.get(0);
			}
			return null;
		}
}
