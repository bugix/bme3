package medizin.server.domain;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostRemove;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.persistence.criteria.Subquery;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import medizin.server.utils.BMEUtils;
import medizin.shared.QuestionTypes;
import medizin.shared.Status;
import medizin.shared.utils.SharedConstant;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.mortbay.log.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Question {

	private static Logger log = Logger.getLogger(Question.class);

	@Size(max = 255)
	private String questionShortName;

	@Size(min = 1, max = 9000)
	private String questionText;

	@Size(min = 2, max = 255)
	private String picturePath;

	@NotNull
	private Integer questionVersion;

	@NotNull
	private Integer questionSubVersion;

	@NotNull
	@Value("false")
	@Column(columnDefinition="BIT", length = 1)
	private Boolean isAcceptedRewiever;

	@NotNull
	@Value("false")
	@Column(columnDefinition="BIT", length = 1)
	private Boolean submitToReviewComitee;

	@NotNull
	@Value("false")
	@Column(columnDefinition="BIT", length = 1)
	private Boolean isAcceptedAdmin;
	
	@NotNull
	@Value("false")
	@Column(columnDefinition="BIT", length = 1)
	private Boolean isAcceptedAuthor;
	
	@NotNull
	@Value("false")
	@Column(columnDefinition="BIT", length = 1)
	private Boolean isForcedActive;

	/*@NotNull
	@Value("false")
	@Column(columnDefinition="BIT", length = 1)
	private Boolean isActive;*/

	@OneToOne
	private medizin.server.domain.Question previousVersion;

	@ManyToMany(cascade = CascadeType.ALL)
	private Set<Keyword> keywords = new HashSet<Keyword>();

	@NotNull
	@ManyToOne
	private QuestionEvent questEvent;

	@OneToOne(cascade = CascadeType.ALL)
	private Comment comment;

	@NotNull
	@ManyToOne
	private QuestionType questionType;

	@NotNull
	private Status status;

	@NotNull
	@ManyToMany(cascade = CascadeType.ALL)
	protected Set<Mc> mcs = new HashSet<Mc>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
	private Set<Answer> answers = new HashSet<Answer>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
	private Set<QuestionResource> questionResources = new HashSet<QuestionResource>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
	private Set<UserAccessRights> questionAccess = new HashSet<UserAccessRights>();

	// RedactionalBase code
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date dateAdded;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date dateChanged;

	// @NotNull
	@ManyToOne
	private Person rewiewer;

	@NotNull
	@ManyToOne
	private Person autor;

	@NotNull
	@Value("false")
	@Column(columnDefinition="BIT", length = 1)
	private Boolean isReadOnly;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
	private Set<AssesmentQuestion> assesmentQuestionSet = new HashSet<AssesmentQuestion>();
	
	public static long countQuestionAccessByPersonNonRoo(java.lang.Long personId) {
		Person person = Person.findPerson(personId);
		if (person == null)
			throw new IllegalArgumentException(
					"The institution argument is required");
		EntityManager em = QuestionEvent.entityManager();
		TypedQuery<Long> q = em
				.createQuery(
						"SELECT count(quest) FROM UserAccessRights qaccess "
								+ "INNER JOIN qaccess.question quest WHERE qaccess.person = :person",
						Long.class);
		q.setParameter("person", person);
		return q.getSingleResult();
	}

	public static List<Question> findQuestionAccessByPersonNonRoo(
			java.lang.Long personId, int firstResult, int maxResults) {
		Person person = Person.findPerson(personId);
		if (person == null)
			throw new IllegalArgumentException(
					"The institution argument is required");
		EntityManager em = QuestionEvent.entityManager();
		TypedQuery<Question> q = em
				.createQuery(
						"SELECT quest FROM QuestionAccess qaccess "
								+ "INNER JOIN qaccess.question quest WHERE qaccess.person = :person",
						Question.class).setFirstResult(firstResult)
				.setMaxResults(maxResults);
		// TypedQuery<QuestionEvent> q =
		// em.createQuery("SELECT QuestionEvent FROM QuestionEvent AS questionevent ",
		// QuestionEvent.class).setFirstResult(firstResult).setMaxResults(maxResults);
		q.setParameter("person", person);
		return q.getResultList();
	}

	public static long countQuestionByInstitutionOrEventOrQuestionNameOrKeyword(
			Long institutionId, Long eventId, String questiuonStringFilter,
			Boolean filterQuestionText, Boolean filterKeywords) {

		Institution inst = null;
		QuestionEvent event = null;
		String queryString = "SELECT COUNT(quest) FROM Question quest INNER JOIN quest.questEvent queEvent LEFT JOIN quest.keywords keyw ";
		if (institutionId != null || eventId != null
				|| !questiuonStringFilter.equals("")) {
			queryString += "WHERE ";
		}

		if (institutionId != null) {
			inst = Institution.findInstitution(institutionId);
			queryString += "queEvent.institution= :institution ";
		}
		if (eventId != null) {
			if (institutionId != null) {

				queryString += "OR ";
			}
			event = QuestionEvent.findQuestionEvent(eventId);
			queryString += "quest.questEvent= :event ";
		}
		if (!questiuonStringFilter.equals("")) {
			if (institutionId != null || eventId != null) {

				queryString += "OR ";
			}
			if (filterQuestionText) {
				queryString += "quest.questionText LIKE '%"
						+ questiuonStringFilter + "%' ";
			}
			if (filterKeywords) {
				if (filterQuestionText)
					queryString += "OR ";

				queryString += "keyw.name LIKE '%" + questiuonStringFilter
						+ "%' ";

			}
		}
		EntityManager em = QuestionEvent.entityManager();
		TypedQuery<Long> q = em.createQuery(queryString, Long.class);
		if (inst != null) {
			q.setParameter("institution", inst);

		}
		if (event != null) {

			q.setParameter("event", event);

		}

		return q.getSingleResult();
	}

	public static List<Question> findQuestionByInstitutionOrEventOrQuestionNameOrKeyword(
			Long institutionId, Long eventId, String questiuonStringFilter,
			Boolean filterQuestionText, Boolean filterKeywords, int start,
			int length) {
		Institution inst = null;
		QuestionEvent event = null;
		String queryString = "SELECT quest FROM Question quest INNER JOIN quest.questEvent queEvent LEFT JOIN quest.keywords keyw  ";
		if (institutionId != null || eventId != null
				|| !questiuonStringFilter.equals("")) {
			queryString += "WHERE ";
		}

		if (institutionId != null) {
			inst = Institution.findInstitution(institutionId);
			queryString += "queEvent.institution= :institution ";
		}
		if (eventId != null) {
			if (institutionId != null) {

				//queryString += "OR ";
				queryString += "AND ";
			}
			event = QuestionEvent.findQuestionEvent(eventId);
			queryString += "quest.questEvent= :event ";
		}
		if (!questiuonStringFilter.equals("")) {
			if (institutionId != null || eventId != null) {

				//queryString += "OR ";
				queryString += "AND ";
			}
			if (filterQuestionText) {
				queryString += "quest.questionText LIKE '%"
						+ questiuonStringFilter + "%' ";
			}
			if (filterKeywords) {
				if (filterQuestionText)
					queryString += "OR ";

				queryString += "keyw.name LIKE '%" + questiuonStringFilter
						+ "%' ";

			}
		}
		EntityManager em = QuestionEvent.entityManager();
		TypedQuery<Question> q = em.createQuery(queryString, Question.class)
				.setFirstResult(start).setMaxResults(length);
		if (inst != null) {
			q.setParameter("institution", inst);

		}
		if (event != null) {

			q.setParameter("event", event);

		}

		return q.getResultList();

	}

	
	/**
	 * New Question TAb
	 * 
	 * @param mcId
	 * @return QuestionList
	 * 
	 * Retrive Query same as Question link, + exclude assessment question
	 */
	public static List<Question> findQuestionsByMc(Long mcId,String questionId,String questionType,String questionName,Assesment a,Person author) {
		
		
		Mc mc = Mc.findMc(mcId);
		if (mc == null)
			throw new IllegalArgumentException("The mcs argument is required");
		EntityManager em = Question.entityManager();
		
		Person userLoggedIn=Person.myGetLoggedPerson();
		if(userLoggedIn == null)
		{
			return new ArrayList<Question>();
		}
		
		 //get institution
        HttpSession session1 = RequestFactoryServlet.getThreadLocalRequest().getSession();
        Long instId = (Long) session1.getAttribute("institutionId");
		Institution institution = Institution.findInstitution(instId);
		
        /*Boolean isAdmin=userLoggedIn.getIsAdmin();
        PersonAccessRight accessRights=userLoggedIn.getLoggedPersonAccessRights();
        Boolean isInstitutionAdmin=accessRights.getIsInstitutionalAdmin();
        
        Set<UserAccessRights> access=userLoggedIn.getQuestionAccesses();
        List<QuestionEvent> questionEvents=new ArrayList<QuestionEvent>();
        
        for(UserAccessRights u:access)
        {
        	questionEvents.add(u.getQuestionEvent());
        }
        
       
		
		
		//get Question Event of particular selected institution
		List<QuestionEvent> questionEvents=QuestionEvent.findQuestionEventByInstitution(institution);
		//find userAccessRight for institution
	//	UserAccessRights institutionRight=UserAccessRights.findUserAccessRightsByPersonAndInstitution(userLoggedIn,institution);
		
		
       // userLoggedIn.getQuestionAccesses()
        //For Admin and Institutional Admin user
        StringBuilder queryBuilder=null;
        if(isAdmin || (isInstitutionAdmin))
        {
		 queryBuilder = new StringBuilder(
				"SELECT Question FROM Question AS question WHERE  question.status = :isActive AND"
						+ " :mcs_item MEMBER OF question.mcs and question.questEvent.institution = :institution");
		 TypedQuery<Question> q = em.createQuery(queryBuilder.toString(),
					Question.class);
			
			q.setParameter("isActive", Status.ACTIVE);

			q.setParameter("mcs_item", mc);
			q.setParameter("institution", institution);
			
			CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
			CriteriaQuery<Question> criteriaQuery = criteriaBuilder.createQuery(Question.class);
    		Root<Question> from=criteriaQuery.from(Question.class);
    		Predicate statusPredicate=criteriaBuilder.equal(from.get("status"), Status.ACTIVE);
    		Predicate institutionPredicate=criteriaBuilder.equal(from.get("questEvent").get("institution"), institution);
    		SetJoin<Question, Mc> mcSetJoin=from.joinSet("mcs",JoinType.LEFT);
    		
    		Predicate mcPredicate=criteriaBuilder.equal(mcSetJoin.get("id"), mcId);
    		
    		Predicate finalPredicate=criteriaBuilder.and(institutionPredicate,statusPredicate,mcPredicate);
    		criteriaQuery.where(finalPredicate);
    		criteriaQuery.select(from);
    		
    		TypedQuery<Question> q = entityManager().createQuery(criteriaQuery);
    		Log.info(q.toString());
    		return q.getResultList();
        }*/
       /* else //for examiner
        {*/
        	
        	
        	List<Question> questions=Question.findQuestionEntriesByPerson(userLoggedIn.getShidId(), institution.getId(), "", new ArrayList<String>(), 0	, Integer.MAX_VALUE,true,questionId,questionType,questionName);
        	
        	// question should belong to particular mc of assesment
        	ArrayList<Question> questionList=new ArrayList<Question>();
        	
        	//filter it by question type and question event specified in assesment module for examiner/ author
    		List<QuestionEvent> questionEventList=QuestionSumPerPerson.findQuestionEventOfExaminer(a, author);    		
    		List<QuestionType> questionTypeList=QuestionTypeCountPerExam.findQuestionTypePerExam(a);
    		
        	for(Question question : questions)
        	{
	        	if(question.getMcs().contains(mc) && questionEventList.contains(question.getQuestEvent()) && questionTypeList.contains(question.getQuestionType()))
	        	{
	        		questionList.add(question);
	        	}
        	}
        	
        	
    		
        	
        	return questionList;
        	
        	/*List<Question> questions=UserAccessRights.findQuestionByPerson(userLoggedIn);
        	
        	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
    		CriteriaQuery<Question> criteriaQuery = criteriaBuilder.createQuery(Question.class);
    		Root<Question> from=criteriaQuery.from(Question.class);
    		//SetJoin<Question, UserAccessRights> questionAccessSetJoin=from.joinSet("questionAccess",JoinType.LEFT);
    		SetJoin<Question, Mc> mcSetJoin=from.joinSet("mcs",JoinType.LEFT);
    		//questionAccessSetJoin.get("person");
    		
    		//filter by institution selected
    		
    		
    		Predicate institutionPredicate=criteriaBuilder.equal(from.get("questEvent").get("institution"), institution);
    		Predicate questEventPredicate=from.get("questEvent").in(questionEvents);
    		//Predicate questionAccessPredicate=criteriaBuilder.equal(questionAccessSetJoin.get("person"), userLoggedIn);
    		//Predicate questionAccessPredicate=criteriaBuilder.gt(criteriaBuilder.count(from.get("questionAccess").), 0);
    		
    		Predicate accessPredicate=criteriaBuilder.or(questEventPredicate);
    		Predicate statusPredicate=criteriaBuilder.equal(from.get("status"), Status.ACTIVE);
    		Predicate mcPredicate=criteriaBuilder.equal(mcSetJoin.get("id"), mcId);
    		
    		Predicate finalPredicate=criteriaBuilder.and(institutionPredicate,questEventPredicate,statusPredicate,mcPredicate);
    		criteriaQuery.where(finalPredicate);
    		criteriaQuery.select(from);
    		
    		
    		//from.get("questEvent").in(userLoggedIn.get)
    	//	from.get("questEvent.institution");
    		
    		
    	//	criteriaQuery.where(userEqualPredicate);
    		
    		TypedQuery<Question> q = entityManager().createQuery(criteriaQuery);
    		Log.info(criteriaQuery.toString());
    		questions.addAll(q.getResultList());
    		return questions;
    		//from.join(from.getModel().get)
*/      //  }
		
	}
	
	


	public static Long countQuestionsNonAcceptedAdmin() {
		// Gets the Sessionattributes
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException(
					"The person and institution arguments are required");
		// End filter fuctionality

		/*StringBuilder queryBuilder = new StringBuilder(
				"SELECT count(Question) FROM Question AS question WHERE question.status != " + Status.DEACTIVATED.ordinal() +" AND ");

		Boolean isAccepted = false;

		if (loggedUser.getIsAdmin()) {

			queryBuilder.append("question.isAcceptedAdmin = :isAccepted ");

		} else {
			queryBuilder
					.append("question.isAcceptedRewiever = :isAccepted AND question.rewiewer = :person");
			queryBuilder.append("(question.isAcceptedRewiever = :isAccepted AND question.rewiewer = :person) OR (question.autor = :person AND question.status IN("+ Status.CORRECTION_FROM_ADMIN.ordinal() +","+ Status.CORRECTION_FROM_REVIEWER.ordinal() +"))");
		}*/
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Question> from = criteriaQuery.from(Question.class);
		criteriaQuery.select(criteriaBuilder.count(from));	

		Predicate pre1 = criteriaBuilder.and(criteriaBuilder.equal(from.get("questEvent").get("institution").get("id"), institution.getId()), criteriaBuilder.notEqual(from.get("status"), Status.DEACTIVATED));

		if (loggedUser.getIsAdmin())
		{
			pre1 = criteriaBuilder.and(pre1, criteriaBuilder.equal(from.get("isAcceptedAdmin"), false));
		}
		else
		{
			Predicate pre2 = criteriaBuilder.and(criteriaBuilder.equal(from.get("isAcceptedRewiever"), false), criteriaBuilder.equal(from.get("rewiewer").get("id"), loggedUser.getId()));
			Predicate pre3 = criteriaBuilder.and(criteriaBuilder.equal(from.get("autor").get("id"), loggedUser.getId()), criteriaBuilder.equal(from.get("isAcceptedAuthor"), false));

			pre1 = criteriaBuilder.and(pre1, criteriaBuilder.or(pre2, pre3));
		}

		criteriaQuery.where(pre1);
		
		TypedQuery<Long> q = entityManager().createQuery(criteriaQuery);

		return q.getSingleResult();
	}

	public static List<Question> findQuestionsEntriesNonAcceptedAdmin(
			int start, int length) {
		// Gets the Sessionattributes
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException(
					"The person and institution arguments are required");
		// End filter fuctionality

		/*StringBuilder queryBuilder = new StringBuilder(
				"SELECT Question FROM Question AS question WHERE  question.status != " + Status.DEACTIVATED.ordinal() +" AND ");

		Boolean isAccepted = false;

		if (loggedUser.getIsAdmin()) {

			queryBuilder.append("question.isAcceptedAdmin = :isAccepted ");

		} else {
			queryBuilder
					.append("question.isAcceptedRewiever = :isAccepted AND question.rewiewer = :person");
			queryBuilder.append("(question.isAcceptedRewiever = :isAccepted AND question.rewiewer = :person) OR (question.autor = :person AND question.status IN("+ Status.CORRECTION_FROM_ADMIN.ordinal() +","+ Status.CORRECTION_FROM_REVIEWER.ordinal() +"))");
		}

		EntityManager em = Question.entityManager();*/
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Question> criteriaQuery = criteriaBuilder.createQuery(Question.class);
		Root<Question> from = criteriaQuery.from(Question.class);
		
		Predicate pre1 = criteriaBuilder.and(criteriaBuilder.equal(from.get("questEvent").get("institution").get("id"), institution.getId()), criteriaBuilder.notEqual(from.get("status"), Status.DEACTIVATED));

		if (loggedUser.getIsAdmin())
		{
			pre1 = criteriaBuilder.and(pre1, criteriaBuilder.equal(from.get("isAcceptedAdmin"), false));
		}
		else
		{
			Predicate pre2 = criteriaBuilder.and(criteriaBuilder.equal(from.get("isAcceptedRewiever"), false), criteriaBuilder.equal(from.get("rewiewer").get("id"), loggedUser.getId()));
			//Predicate pre3 = criteriaBuilder.and(criteriaBuilder.equal(from.get("autor").get("id"), loggedUser.getId()), from.get("status").in(Status.CORRECTION_FROM_ADMIN, Status.CORRECTION_FROM_REVIEWER));
			Predicate pre3 = criteriaBuilder.and(criteriaBuilder.equal(from.get("autor").get("id"), loggedUser.getId()), criteriaBuilder.equal(from.get("isAcceptedAuthor"), false));

			pre1 = criteriaBuilder.and(pre1, criteriaBuilder.or(pre2, pre3));
		}

		criteriaQuery.where(pre1);

		TypedQuery<Question> q = entityManager().createQuery(criteriaQuery);

		q.setFirstResult(start);
		q.setMaxResults(length);

		return q.getResultList();
	}

	public static List<Question> findQuestionsAnswersNonAcceptedAdmin() {
		//Boolean isAcceptedAdmin = false;
		
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");

		/*EntityManager em = Question.entityManager();
		StringBuilder queryBuilder;

		if (loggedUser.getIsAdmin()) {
			queryBuilder = new StringBuilder(
					"SELECT DISTINCT Question FROM Question AS question INNER JOIN question.answers AS answ WHERE answ.isAnswerAcceptedAdmin = :isAccepted");
		} else {
			queryBuilder = new StringBuilder(
					"SELECT DISTINCT Question FROM Question AS question INNER JOIN question.answers AS answ "
							+ "WHERE (answ.isAcceptedRewiever = :isAccepted AND answ.rewiewer=:person ) OR (answ.autor=:person AND answ.isAcceptedAutor = :isAccepted) ");

		}

		TypedQuery<Question> q = em.createQuery(queryBuilder.toString(),
				Question.class);
		q.setParameter("isAccepted", isAcceptedAdmin);
		if (!loggedUser.getIsAdmin()) {
			q.setParameter("person", loggedUser);
		}*/
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Question> criteriaQuery = criteriaBuilder.createQuery(Question.class);
		Root<Question> from = criteriaQuery.from(Question.class);

		Subquery<Answer> subQuery = criteriaQuery.subquery(Answer.class);
		Root answerRoot = subQuery.from(Answer.class);
		
		//Predicate subPre = criteriaBuilder.equal(from.get("question").get("questEvent").get("institution").get("id"), institution.getId());
		Predicate subPre1 = null;
		
		if (!loggedUser.getIsAdmin())
		{
			Predicate subPre2 = criteriaBuilder.equal(answerRoot.get("rewiewer"), loggedUser.getId());
			subPre1 = criteriaBuilder.and(answerRoot.get("status").in(Status.NEW, Status.ACCEPTED_ADMIN), subPre2);
		}
		else
		{
			subPre1 = answerRoot.get("status").in(Status.NEW, Status.ACCEPTED_REVIEWER);
		}
			
		subQuery.select(answerRoot.get("question").get("id")).where(subPre1);
		
		Predicate mainPre1 = criteriaBuilder.equal(from.get("status"), Status.ACTIVE);
		Predicate mainPre2 = criteriaBuilder.equal(from.get("questEvent").get("institution").get("id"), institution.getId());
		Predicate mainpre = criteriaBuilder.and(mainPre1, mainPre2, criteriaBuilder.in(from.get("id")).value(subQuery));
		
		criteriaQuery.where(mainpre);
		
		TypedQuery<Question> q = entityManager().createQuery(criteriaQuery);
		
		//System.out.println("Q : " + q.unwrap(Query.class).getQueryString());
		
		return q.getResultList();
	}

	public static Long countQuestionsByPerson(String shibdId,
			Long institutionId, String searchText, List<String> searchField) {

		HttpSession session = RequestFactoryServlet.getThreadLocalRequest()
				.getSession();
		log.info(session.getAttribute("shibdId"));
		log.info(session.getAttribute("institutionId"));
		String shibdId2 = (String) session.getAttribute("shibdId");
		long institutionId2 = (Long) session.getAttribute("institutionId");

		Person loggedUser = Person.findPersonByShibId(shibdId2);
		Institution institution = Institution.findInstitution(institutionId2);
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException(
					"The person and institution arguments are required");
		// TypedQuery<Long> q;

		// EntityManager em = Question.entityManager();

		TypedQuery<Question> query;
		/*CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Question> criteriaQuery = criteriaBuilder
				.createQuery(Question.class);
		Root<Question> from = criteriaQuery.from(Question.class);*/
		/*
		 * Root<Question> from = criteriaQuery.from(Question.class);
		 * CriteriaQuery<Question> select = criteriaQuery.select(from);
		 * 
		 * if(!loggedUser.getIsAdmin()){ StringBuilder queryBuilder = new
		 * StringBuilder(
		 * "SELECT count(question) FROM Question AS question INNER JOIN question.questEvent AS queEvent "
		 * +
		 * "WHERE queEvent.institution = :institution AND (question IN (SELECT questAccess.question FROM QuestionAccess AS questAccess WHERE questAccess.person = :loggedUser)"
		 * +
		 * " OR question.questEvent IN (SELECT questAccess2.questionEvent FROM QuestionAccess AS questAccess2 WHERE questAccess2.person = :loggedUser))"
		 * );
		 * 
		 * //SetJoin<Question, QuestionAccess> join =
		 * from.joinSet("questionAccess", JoinType.LEFT);
		 * 
		 * Predicate pre1 =
		 * criteriaBuilder.equal(from.get("questEvent").get("institution"
		 * ).get("id"), institution.getId());
		 * 
		 * Subquery<QuestionAccess> subQry =
		 * criteriaQuery.subquery(QuestionAccess.class); Root queAccRoot =
		 * subQry.from(QuestionAccess.class);
		 * subQry.select(queAccRoot.get("question"
		 * ).get("id")).where(criteriaBuilder
		 * .equal(queAccRoot.get("person").get("id"), loggedUser.getId()));
		 * 
		 * Predicate pre2 = criteriaBuilder.in(from.get("id")).value(subQry);
		 * 
		 * Subquery<QuestionAccess> subQuery =
		 * criteriaQuery.subquery(QuestionAccess.class); Root questionAccessRoot
		 * = subQuery.from(QuestionAccess.class);
		 * subQuery.select(questionAccessRoot
		 * .get("questionEvent").get("id")).where
		 * (criteriaBuilder.equal(questionAccessRoot.get("person").get("id"),
		 * loggedUser.getId()));
		 * 
		 * Predicate pre3 =
		 * criteriaBuilder.in(from.get("questEvent").get("id")).value(subQuery);
		 * 
		 * criteriaQuery.where(criteriaBuilder.and(pre1,
		 * criteriaBuilder.or(pre2,pre3)));
		 * 
		 * query = entityManager().createQuery(criteriaQuery);
		 * 
		 * q = em.createQuery(queryBuilder.toString(), Long.class);
		 * 
		 * q.setParameter("institution", institution);
		 * q.setParameter("loggedUser", loggedUser); } else{ StringBuilder
		 * queryBuilder = new StringBuilder(
		 * "SELECT count(question) FROM Question AS question INNER JOIN question.questEvent AS queEvent "
		 * + "WHERE queEvent.institution = :institution "); q =
		 * em.createQuery(queryBuilder.toString(), Long.class);
		 * q.setParameter("institution", institution); Predicate pre1 =
		 * criteriaBuilder
		 * .equal(from.get("questEvent").get("institution").get("id"),
		 * institution.getId());
		 * 
		 * criteriaQuery.where(pre1);
		 * 
		 * query = entityManager().createQuery(criteriaQuery); }
		 */

		// System.out.println("~~QUERY : " +
		// query.unwrap(Query.class).getQueryString());
		// return q.getSingleResult();
		/*criteriaQuery.where(findQuestionBySearchFilter(searchText, searchField,
				institution.getId(), loggedUser));*/

		
		query = entityManager().createQuery(findQuestionBySearchFilter(searchText, searchField, institution.getId(), loggedUser,false,"","",""));
	
		log.info("~~QUERY : " + query.unwrap(Query.class).getQueryString());
	
		return new Long(query.getResultList().size());
		
	}

	public static List<Question> findQuestionEntriesByPerson(String shibdId,
			Long institutionId, String searchText,
			List<String> searchField, int start, int length,boolean newQuestion,String questionId,String questionType,String questionName) {
		Person loggedUser = Person.findPersonByShibId(shibdId);
		Institution institution = Institution.findInstitution(institutionId);
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException(
					"The person and institution arguments are required");
		// EntityManager em = Question.entityManager();
		// TypedQuery<Question> q;
		TypedQuery<Question> query;
		/*CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Question> criteriaQuery = criteriaBuilder
				.createQuery(Question.class);
		Root<Question> from = criteriaQuery.from(Question.class);*/
		/*
		 * Root<Question> from = criteriaQuery.from(Question.class);
		 * CriteriaQuery<Question> select = criteriaQuery.select(from);
		 * 
		 * if(!loggedUser.getIsAdmin()){ StringBuilder queryBuilder = new
		 * StringBuilder(
		 * "SELECT question FROM Question AS question INNER JOIN question.questEvent AS queEvent "
		 * +
		 * "WHERE queEvent.institution = :institution AND question IN (SELECT questAccess.question FROM QuestionAccess AS questAccess WHERE questAccess.person = :loggedUser)"
		 * +
		 * " OR question.questEvent IN (SELECT questAccess2.questionEvent FROM QuestionAccess AS questAccess2 WHERE questAccess2.person = :loggedUser))"
		 * );
		 * 
		 * q = em.createQuery(queryBuilder.toString(),
		 * Question.class).setFirstResult(start).setMaxResults(length);
		 * q.setParameter("institution", institution);
		 * q.setParameter("loggedUser", loggedUser);
		 * 
		 * Predicate pre1 =
		 * criteriaBuilder.equal(from.get("questEvent").get("institution"
		 * ).get("id"), institution.getId());
		 * 
		 * Subquery<QuestionAccess> subQry =
		 * criteriaQuery.subquery(QuestionAccess.class); Root queAccRoot =
		 * subQry.from(QuestionAccess.class);
		 * subQry.select(queAccRoot.get("question"
		 * ).get("id")).where(criteriaBuilder
		 * .equal(queAccRoot.get("person").get("id"), loggedUser.getId()));
		 * 
		 * Predicate pre2 = criteriaBuilder.in(from.get("id")).value(subQry);
		 * 
		 * Subquery<QuestionAccess> subQuery =
		 * criteriaQuery.subquery(QuestionAccess.class); Root questionAccessRoot
		 * = subQuery.from(QuestionAccess.class);
		 * subQuery.select(questionAccessRoot
		 * .get("questionEvent").get("id")).where
		 * (criteriaBuilder.equal(questionAccessRoot.get("person").get("id"),
		 * loggedUser.getId()));
		 * 
		 * Predicate pre3 =
		 * criteriaBuilder.in(from.get("questEvent").get("id")).value(subQuery);
		 * 
		 * criteriaQuery.where(criteriaBuilder.and(pre1,
		 * criteriaBuilder.or(pre2,pre3)));
		 * 
		 * query = entityManager().createQuery(criteriaQuery); } else{
		 * StringBuilder queryBuilder = new StringBuilder(
		 * "SELECT question FROM Question AS question INNER JOIN question.questEvent AS queEvent "
		 * + "WHERE queEvent.institution = :institution");
		 * 
		 * q = em.createQuery(queryBuilder.toString(),
		 * Question.class).setFirstResult(start).setMaxResults(length);
		 * q.setParameter("institution", institution); Predicate pre1 =
		 * criteriaBuilder
		 * .equal(from.get("questEvent").get("institution").get("id"),
		 * institution.getId());
		 * 
		 * criteriaQuery.where(pre1);
		 * 
		 * query = entityManager().createQuery(criteriaQuery); }
		 */

		// return q.getResultList();
		/*criteriaQuery.where(findQuestionBySearchFilter(searchText, searchField,
				institution.getId(), loggedUser));*/

		query = entityManager().createQuery(findQuestionBySearchFilter(searchText, searchField,
				institution.getId(), loggedUser,newQuestion,questionId,questionType,questionName));
		query.setFirstResult(start);
		query.setMaxResults(length);

		return query.getResultList();
	}

	public static Boolean deleteMediaFileFromDisk(String path) {
		boolean flag = true;

		String appPath = BMEUtils.getRealPath(path);
		String sysPath = SharedConstant.getUploadBaseDIRPath() + path;
		log.info("applicatin Path : " + appPath);
		log.info("system Path : " + sysPath);
		try {
			File real = new File(appPath);
			if (real.exists()) {
				real.delete();
			}

			File sys = new File(sysPath);
			if (sys.exists()) {
				sys.delete();
			}
		} catch (Exception e) {
			flag = false;
			log.error(e.getMessage(), e);
		}
		return flag;
	}

	public void persistAndSetPreviousInactive() {
		if (this.entityManager == null)
			this.entityManager = entityManager();

		if (this.getPreviousVersion() != null) {
			this.previousVersion.status = Status.DEACTIVATED;
			/*this.previousVersion.isActive = false;*/
			// this.entityManager.persist(previousVersion);
			// this.previousVersion.flush();
		}
		this.entityManager.persist(this);
		this.flush();
		this.entityManager.refresh(this);
	}

	public void persistNonRoo() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
		this.flush();
		this.entityManager.refresh(this);
	}

	public void generateNewVersion() {
		if (this.entityManager == null)
			this.entityManager = entityManager();

		// this.previousVersion.setIsActive(false);
		// this.entityManager.persist(this.previousVersion);
		this.entityManager.persist(this);
		this.flush();
		// this.entityManager.refresh(this.previousVersion);
		this.entityManager.refresh(this);
	}

	public static CriteriaQuery<Question> findQuestionBySearchFilter(String searchText,
			List<String> searchField, Long institutionId, Person loggedUser,boolean newQuestion,String questionId,String questionType,String questionName) {
		
		try
		{
			CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
			CriteriaQuery<Question> criteriaQuery = criteriaBuilder
					.createQuery(Question.class);
			Root<Question> from = criteriaQuery.from(Question.class);
			CriteriaQuery<Question> select = criteriaQuery.select(from);

			Predicate andAdminPredicate = null;
			
			Predicate institutionPre = criteriaBuilder.equal(from.get("questEvent").get("institution").get("id"), institutionId);

			if (!loggedUser.getIsAdmin()) {
				
				Predicate p1 = criteriaBuilder.equal(from.get("autor").get("id"), loggedUser.getId());
				Predicate p2 = criteriaBuilder.equal(from.get("rewiewer").get("id"), loggedUser.getId());
								
				//Predicate mainpre1 = criteriaBuilder.equal(from.get("questEvent").get("institution").get("id"), institutionId);

				Predicate mainpre1 = criteriaBuilder.and(criteriaBuilder.or(p2, p1));
				
				Subquery<UserAccessRights> subQry = criteriaQuery.subquery(UserAccessRights.class);
				Root queAccRoot = subQry.from(UserAccessRights.class);
				subQry.select(queAccRoot.get("question").get("id")).where(criteriaBuilder.equal(queAccRoot.get("person").get("id"), loggedUser.getId()));
				Predicate mainpre2 = criteriaBuilder.in(from.get("id")).value(subQry);

				Subquery<UserAccessRights> subQuery = criteriaQuery.subquery(UserAccessRights.class);
				Root questionAccessRoot = subQuery.from(UserAccessRights.class);
				subQuery.select(questionAccessRoot.get("questionEvent").get("id")).where(criteriaBuilder.equal(questionAccessRoot.get("person").get("id"),loggedUser.getId()));
				Predicate mainpre3 = criteriaBuilder.in(from.get("questEvent").get("id")).value(subQuery);
				
				Subquery<UserAccessRights> instSubQuery = criteriaQuery.subquery(UserAccessRights.class);
				Root instAccessRoot = instSubQuery.from(UserAccessRights.class);
				Predicate instP1 = criteriaBuilder.equal(instAccessRoot.get("person").get("id"), loggedUser.getId());
				Predicate instP2 = criteriaBuilder.equal(instAccessRoot.get("institution").get("id"), institutionId);
				instSubQuery.select(instAccessRoot.get("institution").get("id")).where(criteriaBuilder.and(instP1, instP2));
				Predicate mainpre4 = criteriaBuilder.in(from.get("questEvent").get("institution").get("id")).value(instSubQuery);

				andAdminPredicate = criteriaBuilder.or(mainpre1, criteriaBuilder.or(mainpre2, mainpre3, mainpre4));
			} else {
				/*Predicate adminpre1 = criteriaBuilder.equal(from.get("questEvent")
						.get("institution").get("id"), institutionId);
				andAdminPredicate = adminpre1;*/
			}
			
			if (andAdminPredicate == null)
				andAdminPredicate = institutionPre;
			else
				andAdminPredicate = criteriaBuilder.and(institutionPre, andAdminPredicate);
			
			//Predicate statusActivePredicate = criteriaBuilder.equal(from.get("isActive"), Boolean.TRUE);
			//Predicate statusNewPredicate = criteriaBuilder.equal(from.get("status"),Status.NEW);
			Predicate statusNewPredicate = criteriaBuilder.equal(from.get("status"),Status.ACTIVE);
			//andAdminPredicate = criteriaBuilder.and(statusNewPredicate, andAdminPredicate);
			
			Map<String, String> searchFilterMap = BMEUtils.convertToMap(searchField);
			if (searchFilterMap.containsKey("showNew")) {
				statusNewPredicate =  from.get("status").in(Status.NEW,Status.ACTIVE);
			}
			
			if(!newQuestion)
				andAdminPredicate = criteriaBuilder.and(statusNewPredicate, andAdminPredicate);
			else //new question tab inside assessment question
			{
				SetJoin<Question, AssesmentQuestion> assesmentQuestionSet=from.joinSet("assesmentQuestionSet",JoinType.LEFT);
				Predicate assesmentQuestionPredicate1=criteriaBuilder.notEqual(assesmentQuestionSet.get("question").get("id"), from.get("id"));		
				Predicate assesmentQuestionPredicate2=criteriaBuilder.isNull(assesmentQuestionSet);
				Predicate assesmentQuestionPredicate=criteriaBuilder.or(assesmentQuestionPredicate1,assesmentQuestionPredicate2);
				
				//search filter for assesment quetion new question tab
				
				Predicate searchPre=searchForNewQuestionTab(questionId,questionType,questionName,from,criteriaBuilder);				
				
				if(searchPre == null)
					andAdminPredicate = criteriaBuilder.and(statusNewPredicate, andAdminPredicate, assesmentQuestionPredicate);
				else
				{
					andAdminPredicate = criteriaBuilder.and(statusNewPredicate, andAdminPredicate, assesmentQuestionPredicate,searchPre);
				}
			}
			
			
			Predicate andPredicate = searchFilter(searchText,searchFilterMap , criteriaBuilder, from);
			
			
			
			
			//andAdminPredicate = criteriaBuilder.and(statusNewPredicate, andAdminPredicate);
			
			if (andPredicate == null)
				criteriaQuery.where(andAdminPredicate);
			else
				criteriaQuery.where(criteriaBuilder.and(andAdminPredicate,andPredicate));
			
			return criteriaQuery;
		}
		catch (Exception e)
		{
			log.error("Error in Question filter",e);
		}	
		return null;
	}

	private static Predicate searchForNewQuestionTab(String questionId,
			String questionType, String questionName, Root<Question> from, CriteriaBuilder criteriaBuilder) {
		Predicate searchPre1=null;
		Predicate searchPre2=null;
		Predicate searchPre3=null;
		Predicate searchPre=null;
		if(!questionId.equalsIgnoreCase(""))
		{
			searchPre1=criteriaBuilder.equal(from.get("id"), new Long(questionId));
		}
		if(!questionType.equalsIgnoreCase(""))
		{
			
			Predicate shortNamePre=criteriaBuilder.like(from.get("questionType").<String>get("shortName"),"%"+questionType+"%");
			Predicate longNamePre=criteriaBuilder.like(from.get("questionType").<String>get("longName"),"%"+questionType+"%");
			searchPre2=criteriaBuilder.or(shortNamePre,longNamePre);
		}
		if(!questionName.equalsIgnoreCase(""))
		{
			Predicate shortNamePre=criteriaBuilder.like(from.<String>get("questionShortName"),"%"+questionName+"%");
			Predicate namePre=criteriaBuilder.like(from.<String>get("questionText"),"%"+questionName+"%");
		
			searchPre3=criteriaBuilder.or(shortNamePre,namePre);
			
		}
		
		
		if(searchPre1!=null && searchPre2!=null && searchPre3!=null)
		{
			searchPre=criteriaBuilder.or(searchPre1,searchPre2,searchPre3);
		}
		else if(searchPre1!=null && searchPre2!=null)
		{
			searchPre=criteriaBuilder.or(searchPre1,searchPre2);
		}
		else if(searchPre2!=null && searchPre3!=null)
		{
			searchPre=criteriaBuilder.or(searchPre2,searchPre3);
		}
		else if(searchPre1!=null && searchPre3!=null)
		{
			searchPre=criteriaBuilder.or(searchPre1,searchPre3);
		}
		else if(searchPre1!=null)
		{
			searchPre=criteriaBuilder.or(searchPre1);
		}
		else if(searchPre2 != null)
		{
			searchPre=criteriaBuilder.or(searchPre2);
		}
		else if(searchPre3 != null)
		{
			searchPre=criteriaBuilder.or(searchPre3);
		}
		
		return searchPre;
	}

	private static Predicate searchFilter(String searchText, Map<String,String> searchField, CriteriaBuilder criteriaBuilder, Root<Question> from) {
		 Predicate orPredicate = criteriaBuilder.conjunction();
		 
		if (!searchText.equals("")) {
			Expression<String> exp1 = from.get("questionShortName");
			Predicate pre1 = criteriaBuilder.like(exp1, "%" + searchText + "%");
			orPredicate = pre1;
		}
		
		if (searchField.size() > 0) {
			
			Date dt1 = null;
			Date dt2 = null;
			Date usedMcDt1 = null;
			Date usedMcDt2 = null;
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//			int ctr = 0;
				
			if (searchField.containsKey("quesitontext") && !searchText.equals("")) {
				Expression<String> exp2 = from.get("questionText");
				Predicate pre2 = criteriaBuilder.like(exp2, "%" + searchText + "%");
				orPredicate = criteriaBuilder.or(orPredicate, pre2);
			}

			if (searchField.containsKey("author") && !searchText.equals("")) {
				Expression<String> authorExp = from.get("autor").get("name");
				Predicate pre3 = criteriaBuilder.like(authorExp, "%" + searchText + "%");
				orPredicate = criteriaBuilder.or(orPredicate, pre3);
			}

			if (searchField.containsKey("reviewer") && !searchText.equals("")) {
				Expression<String> reviwerExp = from.get("rewiewer").get("name");
				Predicate pre4 = criteriaBuilder.like(reviwerExp, "%" + searchText + "%");
				orPredicate = criteriaBuilder.or(orPredicate, pre4);
			}

			if (searchField.containsKey("instruction") && !searchText.equals("")) {
				Expression<String> exp3 = from.get("comment").get("comment");
				Predicate pre5 = criteriaBuilder.like(exp3, "%" + searchText + "%");
				orPredicate = criteriaBuilder.or(orPredicate, pre5);
			}

			if (searchField.containsKey("keyword") && !searchText.equals("")) {
				SetJoin<Question, Keyword> join1 = from.joinSet("keywords", JoinType.LEFT);
				Expression<String> exp4 = join1.get("name");
				Predicate pre6 = criteriaBuilder.like(exp4, "%" + searchText + "%");
				orPredicate = criteriaBuilder.or(orPredicate, pre6);
			}
			
			//should be maintain in own method.
			/*if (searchField.containsKey("showNew")) {
				Predicate showNewPre = criteriaBuilder.equal(from.get("status"),Status.NEW);
				orPredicate = criteriaBuilder.or(orPredicate, showNewPre);
			}*/

			/*if (searchField.containsKey("institution")) {
				Long selectInstitutionId = Long.parseLong(searchField.get("institution"));
				Predicate pre7 = criteriaBuilder.equal(from.get("questEvent").get("institution").get("id"), selectInstitutionId);
				orPredicate = criteriaBuilder.or(orPredicate, pre7);
			}*/

			if (searchField.containsKey("specialiation")) {
				Long questionEventId = Long.parseLong(searchField.get("specialiation"));
				Predicate pre8 = criteriaBuilder.equal(from.get("questEvent").get("id"),questionEventId);
				orPredicate = criteriaBuilder.or(orPredicate, pre8);
			}

			if (searchField.containsKey("createdDateFrom"))
			{
				log.info("DATE VALUE : " + searchField.get("createdDateFrom"));
				
				try {
					dt1 = df.parse(searchField.get("createdDateFrom"));
				} catch (ParseException e) {
					log.error("Error in createdDateFrom ",e);
				}
			}
			
			if (searchField.containsKey("createdDateTo")) {
				try {
					dt2 = df.parse(searchField.get("createdDateTo"));
				} catch (ParseException e) {
					log.error("Error in createdDateTo ",e);
				}
			}

			if (searchField.containsKey("usedMcFrom")) {
				try {
					usedMcDt1 = df.parse(searchField.get("usedMcFrom"));
				} catch (ParseException e) {
					log.error("Error in usedMcFrom ",e);
				}
			}
			
			if (searchField.containsKey("usedMcTo")){
				try {
					usedMcDt2 = df.parse(searchField.get("usedMcTo"));
				} catch (ParseException e) {
					log.error("Error in usedMcTo ",e);
				}
			}
			
			if (dt1 != null && dt2 != null)
			{
				Expression<Date> createdDateExp = from.get("dateAdded");
				Predicate pre10 = criteriaBuilder.between(createdDateExp, dt1, dt2);
				orPredicate = criteriaBuilder.or(orPredicate, pre10);
			}
			
			if (usedMcDt1 != null && usedMcDt2 != null)
			{
				SetJoin<Question, AssesmentQuestion> join2 = from.joinSet("assesmentQuestionSet", JoinType.LEFT);
				Expression<Date> assessmentDate = join2.get("assesment").get("dateOfAssesment");
				Predicate pre11 = criteriaBuilder.between(assessmentDate, usedMcDt1, usedMcDt2);
				orPredicate = criteriaBuilder.or(orPredicate, pre11);
			}
			
			/*while (ctr < searchField.size())
			{
				if (searchField.get(ctr).equals("quesitontext") && !searchText.equals("")) {
					Expression<String> exp2 = from.get("questionText");
					Predicate pre2 = criteriaBuilder.like(exp2, "%" + searchText + "%");
					orPredicate = criteriaBuilder.or(orPredicate, pre2);
					ctr += 1;
				}

				if (searchField.get(ctr).equals("author") && !searchText.equals("")) {
					Expression<String> authorExp = from.get("autor").get("name");
					Predicate pre3 = criteriaBuilder.like(authorExp, "%" + searchText + "%");
					orPredicate = criteriaBuilder.or(orPredicate, pre3);
					ctr += 1;
				}

				if (searchField.get(ctr).equals("reviewer") && !searchText.equals("")) {
					Expression<String> reviwerExp = from.get("rewiewer").get("name");
					Predicate pre4 = criteriaBuilder.like(reviwerExp, "%" + searchText + "%");
					orPredicate = criteriaBuilder.or(orPredicate, pre4);
					ctr += 1;
				}

				if (searchField.get(ctr).equals("instruction") && !searchText.equals("")) {
					Expression<String> exp3 = from.get("comment").get("comment");
					Predicate pre5 = criteriaBuilder.like(exp3, "%" + searchText + "%");
					orPredicate = criteriaBuilder.or(orPredicate, pre5);
					ctr += 1;
				}

				if (searchField.get(ctr).equals("keyword") && !searchText.equals("")) {
					SetJoin<Question, Keyword> join1 = from.joinSet("keywords", JoinType.LEFT);
					Expression<String> exp4 = join1.get("name");
					Predicate pre6 = criteriaBuilder.like(exp4, "%" + searchText + "%");
					orPredicate = criteriaBuilder.or(orPredicate, pre6);
					ctr += 1;
				}
				
				if (searchField.get(ctr).equals("showNew")) {
					ctr += 1;	
					Predicate showNewPre = criteriaBuilder.equal(from.get("status"),Status.NEW);
					if (orPredicate == null)
						orPredicate = showNewPre;
					else
						orPredicate = criteriaBuilder.or(orPredicate, showNewPre);
					
//					ctr += 1;
				}

				if (searchField.get(ctr).equals("institution")) {
					ctr += 1;
					Long selectInstitutionId = Long.parseLong(searchField.get(ctr));
					Predicate pre7 = criteriaBuilder.equal(from.get("questEvent").get("institution").get("id"), selectInstitutionId);
					
					if (orPredicate == null)
						orPredicate = pre7;
					else
						orPredicate = criteriaBuilder.or(orPredicate, pre7);
					
//					ctr += 1;
				}

				if (searchField.get(ctr).equals("specialiation")) {
					ctr += 1;
					Long questionEventId = Long.parseLong(searchField.get(ctr));
					Predicate pre8 = criteriaBuilder.equal(from.get("questEvent").get("id"),questionEventId);
					
					if (orPredicate == null)
						orPredicate = pre8;
					else
						orPredicate = criteriaBuilder.or(orPredicate, pre8);
				}

				if (searchField.get(ctr).equals("createdDateFrom"))
				{
					ctr += 1;
					log.info("DATE VALUE : " + searchField.get(ctr));
					
					try {
						dt1 = df.parse(searchField.get(ctr));
					} catch (ParseException e) {
						log.error("Error in createdDateFrom ",e);
					}
				}
				
				if (searchField.get(ctr).equals("createdDateTo")) {
					ctr += 1;
					try {
						dt2 = df.parse(searchField.get(ctr));
					} catch (ParseException e) {
						log.error("Error in createdDateTo ",e);
					}
					
					if (dt1 != null && dt2 != null)
					{
						Expression<Date> createdDateExp = from.get("dateAdded");
						Predicate pre10 = criteriaBuilder.between(createdDateExp, dt1, dt2);
						
						if (orPredicate == null)
							orPredicate = pre10;
						else
							orPredicate = criteriaBuilder.or(orPredicate, pre10);
					}
				}

				if (searchField.get(ctr).equals("usedMcFrom")) {
					ctr += 1;
					try {
						usedMcDt1 = df.parse(searchField.get(ctr));
					} catch (ParseException e) {
						log.error("Error in usedMcFrom ",e);
					}
				}
				
				if (searchField.get(ctr).equals("usedMcTo")){
					ctr += 1;
					try {
						usedMcDt2 = df.parse(searchField.get(ctr));
					} catch (ParseException e) {
						log.error("Error in usedMcTo ",e);
					}
					
					if (usedMcDt1 != null && usedMcDt2 != null)
					{
						SetJoin<Question, AssesmentQuestion> join2 = from.joinSet("assesmentQuestionSet", JoinType.LEFT);
						Expression<Date> assessmentDate = join2.get("assesment").get("dateOfAssesment");
						Predicate pre11 = criteriaBuilder.between(assessmentDate, usedMcDt1, usedMcDt2);
						
						if (orPredicate == null)
							orPredicate = pre11;
						else
							orPredicate = criteriaBuilder.or(orPredicate, pre11);
					}
				}
				
				ctr += 1;
			}*/
		}
		return orPredicate;
	}
	
	@Transactional
	public static Question persistNewQuestion(Long questionTypeId, String questionShortName, String questionText, Long autherId,Long reviewerId,
			Boolean submitToReviewComitee,Long questionEventId, List<Long> mcIds, String questionComment, int questionVersion, int questionSubVersion, 
			String picturePath, Status status, Set<QuestionResource> questionResources, Long oldQuestionId) {
		
		boolean flag = Question.findQuestionHasNewQuestion(oldQuestionId);
		
		if(flag == true) {
			throw new IllegalStateException("This Question alreay has new status. Try refreshing the page");
		}
		
		Comment newComment = new Comment();
		newComment.setComment(questionComment);
		newComment.persist();
		
		Question question = new Question();
		question.setQuestionType(QuestionType.findQuestionType(questionTypeId));
		question.setQuestionText(questionText);	
		question.setAutor(Person.findPerson(autherId));
		question.setQuestionShortName(questionShortName);
		question.setRewiewer(Person.findPerson(reviewerId));
		question.setSubmitToReviewComitee(submitToReviewComitee);
		question.setQuestEvent(QuestionEvent.findQuestionEvent(questionEventId));
		question.setDateAdded(new Date());
		
		Iterator<Mc> iterator = FluentIterable.from(mcIds).filter(Predicates.notNull()).transform(new Function<Long, Mc>() {

			@Override
			public Mc apply(Long input) {
				
				return Mc.findMc(input);
			}
		}).iterator();
		
		question.setMcs(Sets.newHashSet(iterator));
		question.setQuestionVersion(questionVersion);
		question.setQuestionSubVersion(questionSubVersion);
		question.setComment(newComment);
		question.setPicturePath(picturePath);
		
		if(Status.NEW.equals(status)) {
			question.setIsAcceptedAdmin(false);
			question.setIsAcceptedRewiever(false);
			question.setIsAcceptedAuthor(true);
			//question.setIsActive(false);
			question.setStatus(Status.NEW);
		}else if(Status.CORRECTION_FROM_ADMIN.equals(status)) {
			question.setIsAcceptedAdmin(true);
			question.setIsAcceptedRewiever(false);
			question.setIsAcceptedAuthor(false);
			//question.setIsActive(false);
			question.setStatus(Status.CORRECTION_FROM_ADMIN);
		}else if(Status.CORRECTION_FROM_REVIEWER.equals(status)) {
			question.setIsAcceptedAdmin(false);
			question.setIsAcceptedRewiever(true);
			question.setIsAcceptedAuthor(false);
			//question.setIsActive(false);
			question.setStatus(Status.CORRECTION_FROM_REVIEWER);
		}else if(Status.ACCEPTED_ADMIN.equals(status)) {
			question.setIsAcceptedAdmin(true);
			question.setStatus(Status.ACCEPTED_ADMIN);
			if(question.getIsAcceptedRewiever() && question.getIsAcceptedAuthor()) {
				//question.setIsActive(true);
				question.setStatus(Status.ACTIVE);
			}			
		}else if(Status.ACCEPTED_REVIEWER.equals(status)) {
			question.setIsAcceptedRewiever(true);
			question.setStatus(Status.ACCEPTED_REVIEWER);
			if(question.getIsAcceptedAdmin() && question.getIsAcceptedAuthor()) {
				//question.setIsActive(true);
				question.setStatus(Status.ACTIVE);
			}			
		}else {
			log.info("Do nothing");
		}
		/*question.setIsAcceptedAdmin(false);
		question.setIsAcceptedRewiever(false);
		question.setIsActive(false);
		question.setStatus(Status.NEW);*/
		question.setPreviousVersion(Question.findQuestion(oldQuestionId));
		question.persist();
		
		for (QuestionResource questionResource : questionResources) {
			questionResource.setQuestion(question);
			questionResource.persist();
		}
		
		Question oldQuestion = Question.findQuestion(oldQuestionId);
		Map<Answer,Answer> answerMap = Maps.newHashMap();
		if(oldQuestion != null) {
			
			for(Answer answer : oldQuestion.getAnswers()) {
				Answer newAnswer = new Answer();
				// copy answer comment
				Comment newCommentAnswer = answer.getComment();
				if(answer.getComment() != null) {
					newCommentAnswer = new Comment();
					newCommentAnswer.setComment(answer.getComment().getComment());
					newCommentAnswer.persist();
				}	
				
				BMEUtils.copyValues(answer, newAnswer, Answer.class);
				newAnswer.setQuestion(question);
				newAnswer.setComment(newCommentAnswer);
				newAnswer.persist();
				
				if(QuestionTypes.Matrix.equals(oldQuestion.getQuestionType().getQuestionType()) == true) {
					answerMap.put(answer, newAnswer);	
				}
			}
			
			// for user matrix validity
			if(QuestionTypes.Matrix.equals(oldQuestion.getQuestionType().getQuestionType()) == true && answerMap.isEmpty() == false) {
				
				List<MatrixValidity> matrixValiditys = MatrixValidity.findAllMatrixValidityForQuestionWithAnyStatus(oldQuestionId);

				for (MatrixValidity oldMatrixValidity : matrixValiditys) {
					
					if(answerMap.containsKey(oldMatrixValidity.getAnswerX()) && answerMap.containsKey(oldMatrixValidity.getAnswerY())) {
						MatrixValidity newMatrixValidity = new MatrixValidity();
						newMatrixValidity.setValidity(oldMatrixValidity.getValidity());
						newMatrixValidity.setAnswerX(answerMap.get(oldMatrixValidity.getAnswerX()));
						newMatrixValidity.setAnswerY(answerMap.get(oldMatrixValidity.getAnswerY()));
						newMatrixValidity.persist();
					}
				}
			}
			
			List<UserAccessRights> userAccessRights = UserAccessRights.findUserAccessRightsByQuestion(oldQuestion.getId());
			for (UserAccessRights userAccessRight : userAccessRights) {
				UserAccessRights newUserAccessRights = new UserAccessRights();
				BMEUtils.copyValues(userAccessRight, newUserAccessRights, UserAccessRights.class);
				newUserAccessRights.setQuestion(question);
				newUserAccessRights.persist();
			}
			
			List<AssesmentQuestion> assesmentQuestions = AssesmentQuestion.findAssesmentQuestionsByQuestion(oldQuestion.getId());
			for (AssesmentQuestion assesmentQuestion : assesmentQuestions) {
				AssesmentQuestion newAssesmentQuestion = new AssesmentQuestion();
				BMEUtils.copyValues(assesmentQuestion, newAssesmentQuestion, AssesmentQuestion.class);
				newAssesmentQuestion.setQuestion(question);
				newAssesmentQuestion.persist();
			}
			
			if(Status.ACTIVE.equals(oldQuestion.status) == false) {
				oldQuestion.setStatus(Status.DEACTIVATED);
			}else {
				oldQuestion.setIsReadOnly(true);
			}
				
			oldQuestion.persist();
		}
		
		return question;
	}
	
	private static boolean findQuestionHasNewQuestion(Long id) {
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		
		Root<Question> from = criteriaQuery.from(Question.class);
		criteriaQuery.select(criteriaBuilder.count(from));
		Predicate p1 = criteriaBuilder.equal(from.get("previousVersion").get("id"), id);
		criteriaQuery.where(p1);
		
		TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);
		log.info("Query is : " + query.unwrap(Query.class).getQueryString());

		log.info("Result list size :" + query.getSingleResult());
		
		return query.getSingleResult() > 0;
	}

	
	public static void questionAccepted(Question question, Boolean isAdminOrInstitutionalAdmin) {
		Person userLoggedIn = Person.myGetLoggedPerson();

		if (userLoggedIn == null)
			return;

		if (isAdminOrInstitutionalAdmin) {
			question.setIsAcceptedAdmin(true);

			if (question.getIsAcceptedRewiever() && question.getIsAcceptedAuthor()) {
				question.setStatus(Status.ACTIVE);
				//question.setIsActive(true);
			} else
				question.setStatus(Status.ACCEPTED_ADMIN);
		}
		
		if (question.getRewiewer().getId().equals(userLoggedIn.getId())) {
			question.setIsAcceptedRewiever(true);

			if (question.getIsAcceptedAdmin() && question.getIsAcceptedAuthor()) {
				question.setStatus(Status.ACTIVE);
				//question.setIsActive(true);
			} else
				question.setStatus(Status.ACCEPTED_REVIEWER);
		} 
		
		if (question.getAutor().getId().equals(userLoggedIn.getId())) {
			question.setIsAcceptedAuthor(true);
			
			if (question.getIsAcceptedAdmin() && question.getIsAcceptedRewiever())
				question.setStatus(Status.ACTIVE);
				
			/*if (question.getStatus().equals(Status.CORRECTION_FROM_ADMIN)) {
				question.setIsAcceptedAdmin(true);
				question.setStatus(Status.ACCEPTED_ADMIN);
			} else if (question.getStatus().equals(Status.CORRECTION_FROM_REVIEWER)) {
				question.setIsAcceptedRewiever(true);
				question.setStatus(Status.ACCEPTED_REVIEWER);
			} else if (question.getStatus().equals(Status.ACCEPTED_ADMIN)) {
				question.setStatus(Status.ACTIVE);
				//question.setIsActive(true);
			} else if (question.getStatus().equals(Status.ACCEPTED_REVIEWER)) {
				question.setStatus(Status.ACTIVE);
				//question.setIsActive(true);
			}*/
		}

		if (Status.ACTIVE.equals(question.getStatus()))
			inActivePreviousQuestion(question.getPreviousVersion());

		question.persist();
	}

	private static void inActivePreviousQuestion(Question tempQuestion) {
		if (tempQuestion == null)
			return;

		do {
			//tempQuestion.setIsActive(false);
			tempQuestion.setStatus(Status.DEACTIVATED);
			tempQuestion.persist();

			tempQuestion = tempQuestion.getPreviousVersion();
		} while (tempQuestion != null);
	}

	@PostRemove
	void onPostRemove() {
		log.info("in post remove method of question");
		if(this instanceof Question) {
			if(this.getPicturePath() != null) {
				QuestionResource.deleteFiles(Sets.newHashSet(this.getPicturePath()));
			}
		}
	}
	
	public void deactivatedQuestion() {
		this.status = Status.DEACTIVATED;
		this.persist();
	}
	
	public Question questionResendToReviewWithMajorVersion(boolean isAdmin) {
		log.info("save with major version here");
		
		Person userLoggedIn = Person.myGetLoggedPerson();
		if(userLoggedIn == null) {
			return null;
		}
		
		if(isAdmin == true) {
			this.status = Status.CORRECTION_FROM_ADMIN;
		}else if(userLoggedIn.getId().equals(this.rewiewer.getId()) == true){
			this.status = Status.CORRECTION_FROM_REVIEWER;
		}else if(userLoggedIn.getId().equals(this.autor.getId()) == true) {
			log.error("Author cannnot see this button.");
		}else {
			log.error("Error in logic");
		}
		
		Iterator<Long> mcIds = FluentIterable.from(mcs).filter(Predicates.notNull()).transform(new Function<Mc, Long>() {

			@Override
			public Long apply(Mc input) {
				return input.getId();
			}
		}).iterator();
				
		
		Iterator<QuestionResource> newQuestionResources = FluentIterable.from(this.questionResources).filter(Predicates.notNull()).transform(new Function<QuestionResource, QuestionResource>() {

			@Override
			public QuestionResource apply(QuestionResource input) {
				QuestionResource r = new QuestionResource();
				r.setPath(input.getPath());
				r.setSequenceNumber(input.getSequenceNumber());
				r.setType(input.getType());
				return r;
			}
			
		}).iterator();
		
		Long questionTypeId = this.questionType.getId();
		String shortName = this.questionShortName;
		String questionTxt = this.questionText;
		Long authorId = this.autor.getId();
		Long reviewerId = this.rewiewer.getId();
		boolean submitReviewComitee = this.submitToReviewComitee;
		Long questionEventId =  this.questEvent.getId();
		String questionComment = this.comment.getComment();
		String picPath = this.picturePath;
		Status newStatus = this.status;
		entityManager.refresh(this);
		int questionNextVersion = this.questionVersion + 1;
		
		
		Question question = persistNewQuestion(questionTypeId, shortName, questionTxt, authorId, reviewerId, submitReviewComitee, 
				questionEventId, Lists.newArrayList(mcIds), questionComment, questionNextVersion, 0, picPath, newStatus, 
				Sets.newHashSet(newQuestionResources), this.getId());
		
		if(question != null && isAdmin == true) {
			if(userLoggedIn.getId().equals(this.rewiewer.getId()) == true){
				question.isAcceptedRewiever = true;
			}
			if(userLoggedIn.getId().equals(this.autor.getId()) == true) {
				question.isAcceptedAuthor = true;
			}
			if(question.isAcceptedAdmin && question.isAcceptedAuthor && question.isAcceptedRewiever) {
				question.status = Status.ACTIVE;
				inActivePreviousQuestion(question.previousVersion);
			}
			question.persist();
		}
		return question;
	}
	
	public static Long countNotActivatedQuestionsByPerson(String searchText, List<String> searchField) {
		
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Question> from = criteriaQuery.from(Question.class);
		criteriaQuery.select(criteriaBuilder.count(from));	
		
		TypedQuery<Long> q = notActivatedQuestionsByPerson(institution.getId(), searchText, searchField, criteriaBuilder, criteriaQuery, from);

		return q.getSingleResult();
	}
	
	public static List<Question> findAllNotActivatedQuestionsByPerson(String searchText, List<String> searchField, int start, int length) {
		
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Question> criteriaQuery = criteriaBuilder.createQuery(Question.class);
		Root<Question> from = criteriaQuery.from(Question.class);
		TypedQuery<Question> q = notActivatedQuestionsByPerson(institution.getId(), searchText, searchField, criteriaBuilder, criteriaQuery, from);
		q.setFirstResult(start);
		q.setMaxResults(length);
		return q.getResultList();
	}
	
	private static <T> TypedQuery<T> notActivatedQuestionsByPerson(Long institutionId, String searchText, List<String> searchField, CriteriaBuilder cb, CriteriaQuery<T> cq, Root<Question> from) {
		
		Predicate preIns1 = cb.equal(from.get("questEvent").get("institution").get("id"), institutionId);
		Predicate preStatus2 = from.get("status").in(Status.ACCEPTED_ADMIN,Status.ACCEPTED_REVIEWER,Status.CORRECTION_FROM_ADMIN,Status.CORRECTION_FROM_REVIEWER,Status.NEW); 		
		Predicate andPredicate = null;
		
		andPredicate = searchFilter(searchText, BMEUtils.convertToMap(searchField), cb, from);
		
		if(andPredicate != null) preIns1 = cb.and(preIns1,andPredicate);
		
		cq.where(cb.and(preIns1,preStatus2));
		
		return entityManager().createQuery(cq);
	}
	
	public static void forcedActiveQuestion(Long questionId) {
		
		Person userLoggedIn = Person.myGetLoggedPerson();

		if (userLoggedIn == null)
			return;

		Question question = Question.findQuestion(questionId);
		if(question == null){
			log.error("Question is null");
			return;
		}
			
		question.setStatus(Status.ACTIVE);
		question.setIsForcedActive(true);
				
		if (Status.ACTIVE.equals(question.getStatus()))
			inActivePreviousQuestion(question.getPreviousVersion());

		question.persist();	
	}
	
	public void persistQuestion() {
		
		if(previousVersion != null){
			boolean flag = Question.findQuestionHasNewQuestion(this.previousVersion.getId());
			
			if(flag == true) {
				throw new IllegalStateException("This Question alreay has new status. Try refreshing the page");
			}	
		}
		
		
		this.persist();
		
		Question oldQuestion = this.previousVersion;
		Map<Answer,Answer> answerMap = Maps.newHashMap();
		if(oldQuestion != null) {
			
			for(Answer answer : oldQuestion.getAnswers()) {
				Answer newAnswer = new Answer();
				// copy answer comment
				Comment newCommentAnswer = answer.getComment();
				if(answer.getComment() != null) {
					newCommentAnswer = new Comment();
					newCommentAnswer.setComment(answer.getComment().getComment());
					newCommentAnswer.persist();
				}	
				
				BMEUtils.copyValues(answer, newAnswer, Answer.class);
				newAnswer.setQuestion(this);
				newAnswer.setComment(newCommentAnswer);
				newAnswer.persist();
				
				if(QuestionTypes.Matrix.equals(oldQuestion.getQuestionType().getQuestionType()) == true) {
					answerMap.put(answer, newAnswer);	
				}
			}
			
			// for user matrix validity
			if(QuestionTypes.Matrix.equals(oldQuestion.getQuestionType().getQuestionType()) == true && answerMap.isEmpty() == false) {
				
				List<MatrixValidity> matrixValiditys = MatrixValidity.findAllMatrixValidityForQuestionWithAnyStatus(oldQuestion.getId());

				for (MatrixValidity oldMatrixValidity : matrixValiditys) {
					
					if(answerMap.containsKey(oldMatrixValidity.getAnswerX()) && answerMap.containsKey(oldMatrixValidity.getAnswerY())) {
						MatrixValidity newMatrixValidity = new MatrixValidity();
						newMatrixValidity.setValidity(oldMatrixValidity.getValidity());
						newMatrixValidity.setAnswerX(answerMap.get(oldMatrixValidity.getAnswerX()));
						newMatrixValidity.setAnswerY(answerMap.get(oldMatrixValidity.getAnswerY()));
						newMatrixValidity.persist();
					}
				}
			}
			
			List<UserAccessRights> userAccessRights = UserAccessRights.findUserAccessRightsByQuestion(oldQuestion.getId());
			for (UserAccessRights userAccessRight : userAccessRights) {
				UserAccessRights newUserAccessRights = new UserAccessRights();
				BMEUtils.copyValues(userAccessRight, newUserAccessRights, UserAccessRights.class);
				newUserAccessRights.setQuestion(this);
				newUserAccessRights.persist();
			}
			
			List<AssesmentQuestion> assesmentQuestions = AssesmentQuestion.findAssesmentQuestionsByQuestion(oldQuestion.getId());
			for (AssesmentQuestion assesmentQuestion : assesmentQuestions) {
				AssesmentQuestion newAssesmentQuestion = new AssesmentQuestion();
				BMEUtils.copyValues(assesmentQuestion, newAssesmentQuestion, AssesmentQuestion.class);
				newAssesmentQuestion.setQuestion(this);
				newAssesmentQuestion.persist();
			}
			
			if(Status.ACTIVE.equals(oldQuestion.status) == false) {
				oldQuestion.setStatus(Status.DEACTIVATED);
			}else {
				oldQuestion.setIsReadOnly(true);
			}
				
			oldQuestion.persist();
		}
	}
}
