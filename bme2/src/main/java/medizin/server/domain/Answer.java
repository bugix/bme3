package medizin.server.domain;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PostRemove;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import medizin.client.shared.Validity;
import medizin.shared.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.google.common.collect.Sets;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Answer {

	private static Logger log = Logger.getLogger(Answer.class);

    @Size(min = 0, max = 999)
    private String answerText;

    @NotNull
    @Column(columnDefinition="BIT", length = 1)
    private Boolean isAnswerActive;

    @NotNull
    @Value("false")
    @Column(columnDefinition="BIT", length = 1)
    private Boolean isMedia;

    @NotNull
    @Value("false")
    @Column(columnDefinition="BIT", length = 1)
    private Boolean isAnswerAcceptedReviewWahrer;

    @NotNull
    @Value("false")
    @Column(columnDefinition="BIT", length = 1)
    private Boolean isAnswerAcceptedAutor;

    @NotNull
    @Value("false")
    @Column(columnDefinition="BIT", length = 1)
    private Boolean isAnswerAcceptedAdmin;

    @NotNull
    @Enumerated
    private Validity validity;

    @Size(min = 0, max = 255)
    private String mediaPath;

    @ManyToOne
    private Question question;
    
    //RedactionalBase code
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateAdded;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateChanged;

    
    @ManyToOne
    private Person rewiewer;

    @NotNull
    @ManyToOne
    private Person autor;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Comment comment;
    
    @NotNull
    @Value("false")
    @Column(columnDefinition="BIT", length = 1)
    private Boolean submitToReviewComitee;
    
    private String points;
    
    private String additionalKeywords;
    
	private Integer sequenceNumber;
	
	private Status status;
    
	public static List<Answer> findAnswersEntriesByQuestion(Long id, int start, int max){
        Question question = Question.findQuestion(id);
        if (question == null) throw new IllegalArgumentException("The question argument is required");
        EntityManager em = QuestionEvent.entityManager();
        TypedQuery<Answer> q = em.createQuery("SELECT ans FROM Answer ans " + 
        		" WHERE ans.question = :question AND ans.status IN( " + Status.NEW.ordinal() +"," + Status.ACTIVE.ordinal() + ")", Answer.class).setFirstResult(start).setMaxResults(max);
        q.setParameter("question", question);
        return q.getResultList();
	}
	
	public static List<Answer> findAnswersByQuestion(Long id){
        Question question = Question.findQuestion(id);
        if (question == null) throw new IllegalArgumentException("The question argument is required");
        EntityManager em = QuestionEvent.entityManager();
        TypedQuery<Answer> q = em.createQuery("SELECT ans FROM Answer ans " + 
        		" WHERE ans.question = :question", Answer.class);
        q.setParameter("question", question);
        return q.getResultList();		
	}
	public static long contAnswersByQuestion(Long id){
        Question question = Question.findQuestion(id);
        if (question == null) throw new IllegalArgumentException("The question argument is required");
        EntityManager em = QuestionEvent.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT count(ans) FROM Answer ans " + 
        		" WHERE ans.question = :question AND ans.status IN( " + Status.NEW.ordinal() +"," + Status.ACTIVE.ordinal() + ")" , Long.class);
        q.setParameter("question", question);
        return q.getSingleResult();
	}
	public void eliminateAnswer(){
  	  if (this.entityManager == null) this.entityManager = entityManager();
  	  List<AnswerToAssQuestion> answerToAssQuestion = Collections.emptyList();
	  answerToAssQuestion = AnswerToAssQuestion.findAnswerToAssQuestionByAnswer(this.getId());
	  if (answerToAssQuestion.size()>0){
		  this.isAnswerActive = false;
		  this.persist();
	  }
	  else{

		  this.remove();
	  }
	  this.flush();
	}
	
	public static long countAnswersNonAcceptedAdminByQuestion(Long questionId){
		
		HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		
		String shibdId2 = (String) session.getAttribute("shibdId");
		long institutionId2 = (Long) session.getAttribute("institutionId");
		Question question = Question.findQuestion(questionId);
		
		Person loggedUser = Person.findPersonByShibId(shibdId2);
		Institution institution = Institution.findInstitution(institutionId2);
		if (loggedUser == null || institution == null) throw new IllegalArgumentException("The person and institution arguments are required");
		// End filter fuctionality
		
		 /*StringBuilder queryBuilder = new StringBuilder("SELECT count(ans) FROM Answer ans INNER JOIN ans.question as question  WHERE  ans.question = :question  AND  ");
		 
		 Boolean isAccepted = false;
		 
		if(loggedUser.getIsAdmin()){
			
			queryBuilder.append("ans.isAnswerAcceptedAdmin = :isAccepted ");
			
			
		} else {
			queryBuilder.append("((ans.isAnswerAcceptedReviewWahrer = :isAccepted AND ans.rewiewer = :person) OR (ans.isAnswerAcceptedAutor = :isAccepted AND ans.autor = :person) )");
		}
		
		EntityManager em = Question.entityManager();
       
        
        TypedQuery<Long> q = em.createQuery(queryBuilder.toString(), Long.class);
		q.setParameter("question", question);
        if(!loggedUser.getIsAdmin()){
        	 q.setParameter("person", loggedUser);
        }
        
        q.setParameter("isAccepted", isAccepted);*/
		
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Answer> from = cq.from(Answer.class);
		
		cq.select(cb.count(from));
		
		Predicate pre1 = cb.equal(from.get("question").get("id"), questionId);
		
		if (!loggedUser.getIsAdmin())
		{
			pre1 = cb.and(from.get("status").in(Status.NEW, Status.ACCEPTED_ADMIN), pre1);
			pre1 = cb.and(pre1, cb.equal(from.get("rewiewer"), loggedUser.getId()));
		}
		else
		{
			pre1 = cb.and(from.get("status").in(Status.NEW, Status.ACCEPTED_REVIEWER), pre1);
		}
		
		cq.where(pre1);
		
        TypedQuery<Long> q = entityManager().createQuery(cq);
        
        //System.out.println("Q1 : " + q.unwrap(Query.class).getQueryString());
        
        return q.getSingleResult();

//		
//		
//			boolean isAnswerAcceptedAdmin = false;
//			Question question = Question.findQuestion(questionId);
//	        if (question == null) throw new IllegalArgumentException("The question argument is required");
//	        EntityManager em = QuestionEvent.entityManager();
//	        TypedQuery<Long> q = em.createQuery("SELECT count(ans) FROM Answer ans INNER JOIN ans.question as question INNER JOIN  question.questEvent AS queEvent " + 
//	        		" WHERE ans.question = :question AND queEvent.institution = :institution AND ans.isAnswerAcceptedAdmin=:isAnswerAcceptedAdmin", Long.class);
//	        q.setParameter("question", question);
//	        q.setParameter("isAnswerAcceptedAdmin", isAnswerAcceptedAdmin);
//	        return q.getSingleResult();
	}
	public static List<Answer> findAnswersEntriesNonAcceptedAdminByQuestion(
			Long questionId, Integer start, Integer length){
//		boolean isAnswerAcceptedAdmin = false;
//		 Question question = Question.findQuestion(questionId);
//	        if (question == null) throw new IllegalArgumentException("The question argument is required");
//	        EntityManager em = QuestionEvent.entityManager();
//	        TypedQuery<Answer> q = em.createQuery("SELECT ans FROM Answer ans " + 
//	        		" WHERE ans.question = :question AND ans.isAnswerAcceptedAdmin=:isAnswerAcceptedAdmin", Answer.class);
//	        q.setParameter("question", question);
//	        q.setParameter("isAnswerAcceptedAdmin", isAnswerAcceptedAdmin);
		
		HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		
		String shibdId2 = (String) session.getAttribute("shibdId");
		long institutionId2 = (Long) session.getAttribute("institutionId");
		Question question = Question.findQuestion(questionId);
		
		Person loggedUser = Person.findPersonByShibId(shibdId2);
		Institution institution = Institution.findInstitution(institutionId2);
		if (loggedUser == null || institution == null) throw new IllegalArgumentException("The person and institution arguments are required");
		// End filter fuctionality
		
		 /*StringBuilder queryBuilder = new StringBuilder("SELECT ans FROM Answer ans INNER JOIN ans.question as question  WHERE  ans.question = :question  AND  ");
		 
		 Boolean isAccepted = false;
		 
		if(loggedUser.getIsAdmin()){
			
			queryBuilder.append("ans.isAnswerAcceptedAdmin = :isAccepted ");
			
			
		} else {
			queryBuilder.append("((ans.isAnswerAcceptedReviewWahrer = :isAccepted AND ans.rewiewer = :person) OR (ans.isAnswerAcceptedAutor = :isAccepted AND ans.autor = :person) )");
		}
		
		EntityManager em = Question.entityManager();
       
	
        TypedQuery<Answer> q = em.createQuery(queryBuilder.toString(), Answer.class);
		q.setParameter("question", question);
        if(!loggedUser.getIsAdmin()){
        	 q.setParameter("person", loggedUser);
        }
        
        q.setParameter("isAccepted", isAccepted);
	        return q.getResultList();		*/
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Answer> cq = cb.createQuery(Answer.class);
		Root<Answer> from = cq.from(Answer.class);
		
		Predicate pre1 = cb.equal(from.get("question").get("id"), questionId);
		
		if (!loggedUser.getIsAdmin())
		{
			pre1 = cb.and(from.get("status").in(Status.NEW, Status.ACCEPTED_ADMIN), pre1);
			pre1 = cb.and(pre1, cb.equal(from.get("rewiewer"), loggedUser.getId()));
		}
		else
		{
			pre1 = cb.and(from.get("status").in(Status.NEW, Status.ACCEPTED_REVIEWER), pre1);
		}
		
		cq.where(pre1);
		
        TypedQuery<Answer> q = entityManager().createQuery(cq);
        return q.getResultList();
	}	
	
	public static List<String> findAllAnswersPoints(Long questionId) {
		
		EntityManager em = entityManager();
		String sql = "SELECT ans.points FROM Answer ans WHERE ans.question.id = " + questionId;
		
		TypedQuery<String> q = em.createQuery(sql, String.class);
	    //q.setParameter("question", question);
		log.info("SIZE : " + q.getResultList());
	    return q.getResultList();
	}
	
	@PostRemove
	void onPostRemove() {
		log.info("in post remove method of answer");
		if(this instanceof Answer) {
			if(this.getMediaPath() != null) {
				QuestionResource.deleteFiles(Sets.newHashSet(this.getMediaPath()));
			}
		}
	}
	
	/*public static Boolean acceptMatrixAnswer(Question question, Person userLoggedIn)
	{
		for (Answer answer : question.getAnswers())
		{	
			if(userLoggedIn.getIsAdmin()){
				answer.setIsAnswerAcceptedAdmin(true);
				answer.setIsAnswerActive(true);
				answer.setStatus(Status.ACTIVE);
			} 
			if(answer.getRewiewer().getId() == userLoggedIn.getId()) {
				answer.setIsAnswerAcceptedReviewWahrer(true);
			}
			if(answer.getAutor().getId() == userLoggedIn.getId())
			{
				answer.setIsAnswerAcceptedAutor(true);
			}
			
			answer.persist();
		}
		return true;
	}*/
	
	public static Boolean acceptMatrixAnswer(Question question, Person userLoggedIn)
	{
		for (Answer answer : question.getAnswers())
		{ 
			if(userLoggedIn.getIsAdmin()){
				answer.setIsAnswerAcceptedAdmin(true);
				if (answer.getIsAnswerAcceptedReviewWahrer())
				{
					answer.setIsAnswerActive(true);
					answer.setStatus(Status.ACTIVE);
				}
				else
				{
					answer.setStatus(Status.ACCEPTED_ADMIN);
				}
			} 
	  
			if(answer.getRewiewer().getId() == userLoggedIn.getId()) {
				answer.setIsAnswerAcceptedReviewWahrer(true);
				
				if (answer.getIsAnswerAcceptedAdmin()){
					answer.setIsAnswerActive(true);
					answer.setStatus(Status.ACTIVE);
				}
				else
				{
					answer.setStatus(Status.ACCEPTED_REVIEWER);
				}
			}
	   
			if(answer.getAutor().getId() == userLoggedIn.getId())
			{
				answer.setIsAnswerAcceptedAutor(true);
			}

			answer.persist();
		}
	  
		return true;
	 
	}
}
