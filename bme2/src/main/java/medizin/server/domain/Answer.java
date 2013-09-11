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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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

import medizin.shared.Status;
import medizin.shared.Validity;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Answer {

	private static Logger log = Logger.getLogger(Answer.class);

    @Size(min = 0, max = 5000,message="answerTextErrorMessage")
    private String answerText;

   /* @NotNull
    @Column(columnDefinition="BIT", length = 1)
    private Boolean isAnswerActive;*/

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

    @NotNull(message="questionTypeMayNotBeNull")
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
	
	@NotNull
	private Status status;
    
	@NotNull
	@Value("false")
	@Column(columnDefinition="BIT", length = 1)
	private Boolean isForcedActive;
	
	@ManyToOne
	private Person createdBy;
	
	@ManyToOne
	private Person modifiedBy;
    
	public static List<Answer> findAnswersEntriesByQuestion(Long id, int start, int max){
        Question question = Question.findQuestion(id);
        if (question == null) throw new IllegalArgumentException("The question argument is required");
        EntityManager em = QuestionEvent.entityManager();
        TypedQuery<Answer> q = em.createQuery("SELECT ans FROM Answer ans " + 
        		" WHERE ans.question = :question AND ans.status != " + Status.DEACTIVATED.ordinal() , Answer.class).setFirstResult(start).setMaxResults(max);
        q.setParameter("question", question);
        return q.getResultList();
	}
	
	public static List<Answer> findAnswersByQuestion(Long id){
        Question question = Question.findQuestion(id);
        if (question == null) throw new IllegalArgumentException("The question argument is required");
        EntityManager em = QuestionEvent.entityManager();
        TypedQuery<Answer> q = em.createQuery("SELECT ans FROM Answer ans " + 
        		" WHERE ans.question = :question and ans.status = :status", Answer.class);
        q.setParameter("question", question);
        q.setParameter("status", Status.ACTIVE);
        return q.getResultList();		
	}
	public static long contAnswersByQuestion(Long id){
        Question question = Question.findQuestion(id);
        if (question == null) throw new IllegalArgumentException("The question argument is required");
        EntityManager em = QuestionEvent.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT count(ans) FROM Answer ans " + 
        		" WHERE ans.question = :question AND ans.status != " + Status.DEACTIVATED.ordinal()   , Long.class);
        q.setParameter("question", question);
        return q.getSingleResult();
	}
	public void eliminateAnswer(){
  	  if (this.entityManager == null) this.entityManager = entityManager();
  	  List<AnswerToAssQuestion> answerToAssQuestion = Collections.emptyList();
	  answerToAssQuestion = AnswerToAssQuestion.findAnswerToAssQuestionByAnswer(this.getId());
	  if (answerToAssQuestion.size()>0){
		  //this.isAnswerActive = false;
		  this.status = Status.DEACTIVATED;
		  this.persist();
	  }
	  else{

		  this.remove();
	  }
	  this.flush();
	}
	
	public static long countAnswersNonAcceptedAdminByQuestion(Long questionId){
		
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");
		
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
		
		Predicate pre0 = cb.equal(from.get("question").get("questEvent").get("institution"), institution);
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
		
		cq.where(cb.and(pre0,pre1));
		
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
		
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");
		
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
		
		Predicate pre0 = cb.equal(from.get("question").get("questEvent").get("institution"), institution);
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
		
		cq.where(cb.and(pre0,pre1));
		
        TypedQuery<Answer> q = entityManager().createQuery(cq);
        return q.getResultList();
	}	
	
	public static List<String> findAllAnswersPoints(Long questionId,Long currentAnswerId) {
		
		EntityManager em = entityManager();
		String sql = "SELECT ans.points FROM Answer ans WHERE ans.question.id = " + questionId;
		
		if(currentAnswerId != null) {
			sql = sql + " and ans.id != " + currentAnswerId;
		}
		
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
	
	public static Boolean acceptMatrixAnswer(Question question, Boolean isAdmin, Boolean isInstitutionalAdmin)
	{
		Person userLoggedIn = Person.myGetLoggedPerson();
		
		if(userLoggedIn == null) {
			return false;
		}
		
		for (Answer answer : question.getAnswers())
		{ 
			if(isAdmin || isInstitutionalAdmin){
				answer.setIsAnswerAcceptedAdmin(true);
				if (answer.getIsAnswerAcceptedReviewWahrer())
				{
					//answer.setIsAnswerActive(true);
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
					//answer.setIsAnswerActive(true);
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
	
	public static Long countAnswerForAcceptQuestion(Long questionId)
	{
		Question question = Question.findQuestion(questionId);
		
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null) throw new IllegalArgumentException("The person and institution arguments are required");
		
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Answer> from = cq.from(Answer.class);
		
		cq.select(cb.count(from));
		
		Predicate pre1 = cb.equal(from.get("question").get("id"), questionId);
		Predicate pre2 = cb.notEqual(from.get("status"), Status.DEACTIVATED);
		pre1 = cb.and(pre2, pre1);
		
		cq.where(pre1);
		
        TypedQuery<Long> q = entityManager().createQuery(cq);
        
        //System.out.println("Q1 : " + q.unwrap(Query.class).getQueryString());
        
        return q.getSingleResult();
	}
	
	public static List<Answer> findAnswerForAcceptQuestion(Long questionId, Integer start, Integer length)
	{
		HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		
		Question question = Question.findQuestion(questionId);
		
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null) throw new IllegalArgumentException("The person and institution arguments are required");
		
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Answer> cq = cb.createQuery(Answer.class);
		Root<Answer> from = cq.from(Answer.class);
		
		Predicate pre1 = cb.equal(from.get("question").get("id"), questionId);
		Predicate pre2 = cb.notEqual(from.get("status"), Status.DEACTIVATED);
		pre1 = cb.and(pre2, pre1);
		
		cq.where(pre1);
		
        TypedQuery<Answer> q = entityManager().createQuery(cq);
        q.setFirstResult(start);
        q.setMaxResults(length);        
        //System.out.println("Q1 : " + q.unwrap(Query.class).getQueryString());
        
        return q.getResultList();
	}
	
	public static List<Long> maxDifferenceBetweenAnswerForQuestion(Long answerId, Long questionId) {
		List<Long> range = Lists.newArrayList();
		
		if(questionId == null) {
			return range;
		}
		
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Answer> cq = cb.createQuery(Answer.class);
		Root<Answer> from = cq.from(Answer.class);
		
		Predicate pre1 = cb.equal(from.get("question").get("id"), questionId);
		Predicate pre2 = cb.notEqual(from.get("status"), Status.DEACTIVATED);
		if(answerId != null) {
			Predicate pre3 = cb.notEqual(from.get("id"), answerId);
			pre1 = cb.and(pre3,pre1);
		}
		pre1 = cb.and(pre2, pre1);
		
		cq.where(pre1);
		
        TypedQuery<Answer> q = entityManager().createQuery(cq);
        
        log.info("Q1 : " + q.unwrap(Query.class).getQueryString());
        List<String> allAnswerText = FluentIterable.from(q.getResultList()).filter(com.google.common.base.Predicates.notNull()).transform(new Function<Answer, String>() {

			@Override
			public String apply(Answer input) {
			
				return input.getAnswerText();
			}			
		}).toImmutableList();
        
        if(allAnswerText.isEmpty() == false) {
        	int total = 0;
            for (String answerText : allAnswerText) {
            	log.info("Answer Text length : " + Jsoup.parse(answerText).text().length());
            	total += Jsoup.parse(answerText).text().length();
    		}
            double mean = total / ((double) allAnswerText.size());
            Double diff = Question.findQuestion(questionId).getQuestionType().getDiffBetAnswer();
            double max;
            double min;
            if(diff != null) {
            	max = mean + ((mean*diff) / 100.0);
            	min = mean - ((mean*diff) / 100.0);
            	range.add(Math.round(max));
            	range.add(Math.round(min));
            	log.info(Objects.toStringHelper("Answer Diff").add("Max", range.get(0)).add("Min", range.get(1)).add("Total", total).add("Diff", diff).toString());
            }
        } 
		return range;
	}
	
	public static Boolean forceAcceptMatrixAnswer(Question question, Boolean isAdmin, Boolean isInstitutionalAdmin)
	{
		Person userLoggedIn = Person.myGetLoggedPerson();
		
		if(userLoggedIn == null) {
			return false;
		}
		
		for (Answer answer : question.getAnswers())
		{ 
			if(isAdmin || isInstitutionalAdmin){
				answer.setStatus(Status.ACTIVE);
				answer.setIsForcedActive(true);
			} 
	  	
			answer.persist();
		}
	  
		return true;	 
	}

	public static long countAnswersForForceActiveByQuestion(Long questionId){

		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");
		
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Answer> from = cq.from(Answer.class);
		
		cq.select(cb.count(from));
		
		Predicate pre0 = cb.equal(from.get("question").get("questEvent").get("institution"), institution);
		Predicate pre1 = cb.equal(from.get("question").get("id"), questionId);
		
		pre1 = cb.and(from.get("status").in(Status.NEW, Status.ACCEPTED_REVIEWER, Status.ACCEPTED_ADMIN), pre1);
		
		cq.where(cb.and(pre0,pre1));
		
        TypedQuery<Long> q = entityManager().createQuery(cq);
        
        return q.getSingleResult();
	}
	
	public static List<Answer> findAnswersForForceActiveByQuestion(Long questionId, Integer start, Integer length){
		
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");
		
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Answer> cq = cb.createQuery(Answer.class);
		Root<Answer> from = cq.from(Answer.class);
		
		Predicate pre0 = cb.equal(from.get("question").get("questEvent").get("institution"), institution);
		Predicate pre1 = cb.equal(from.get("question").get("id"), questionId);
		
		pre1 = cb.and(from.get("status").in(Status.NEW, Status.ACCEPTED_REVIEWER, Status.ACCEPTED_ADMIN), pre1);
		
		cq.where(cb.and(pre0,pre1));
		
        TypedQuery<Answer> q = entityManager().createQuery(cq);
        return q.getResultList();
	}
	
	@PrePersist
	@PreUpdate
	public void preAnswerPersist()
	{
		Person loggedPerson = Person.findLoggedPersonByShibId();
		
		if (loggedPerson == null)
		{
			if (this.createdBy == null)
				this.setCreatedBy(loggedPerson);
			else if (this.createdBy != null)
				this.setModifiedBy(loggedPerson);
		}
	}
	
	public static Long countAnswerByLoggedUser(boolean isAdminOrInstitutionalAdmin, Long loggedUserId)
	{
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");
		
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Answer> from = cq.from(Answer.class);
		
		cq.select(cb.count(from));
		
		Predicate pre1 = cb.equal(from.get("question").get("questEvent").get("institution"), institution);
		
		if (isAdminOrInstitutionalAdmin)
			pre1 = cb.and(pre1, from.get("status").in(Status.NEW, Status.ACCEPTED_REVIEWER));
		else
		{
			pre1 = cb.and(pre1, from.get("status").in(Status.NEW, Status.ACCEPTED_ADMIN));
			pre1 = cb.and(pre1, cb.equal(from.get("rewiewer"), loggedUserId));
		}
		
		cq.where(pre1);
		
        TypedQuery<Long> q = entityManager().createQuery(cq);
        
        return q.getSingleResult();
	}
}
