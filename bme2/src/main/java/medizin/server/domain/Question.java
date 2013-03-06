package medizin.server.domain;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import medizin.server.utils.BMEUtils;
import medizin.shared.Status;
import medizin.shared.utils.SharedConstant;

import org.apache.log4j.Logger;
import org.hibernate.Query;
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
public class Question {

	private static Logger log = Logger.getLogger(Question.class);

	@Size(max = 255)
	private String questionShortName;

	@Size(min = 10, max = 600)
	private String questionText;

	@Size(min = 2, max = 255)
	private String picturePath;

	// @NotNull
	private Double questionVersion;

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
	private Boolean isActive;

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

	private Status status;

	// @NotNull
	@ManyToMany(cascade = CascadeType.ALL)
	private Set<Mc> mcs = new HashSet<Mc>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
	private Set<Answer> answers = new HashSet<Answer>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
	private Set<QuestionResource> questionResources = new HashSet<QuestionResource>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
	private Set<UserAccessRights> questionAccess = new HashSet<UserAccessRights>();

	// RedactionalBase code
	// @NotNull
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

	public static List<Question> findQuestionsByMc(Long mcId) {
		Boolean isAcceptedAdmin = true;
		Boolean isActive = true;
		Mc mc = Mc.findMc(mcId);
		if (mc == null)
			throw new IllegalArgumentException("The mcs argument is required");
		EntityManager em = Question.entityManager();
		StringBuilder queryBuilder = new StringBuilder(
				"SELECT Question FROM Question AS question WHERE question.isAcceptedAdmin = :isAcceptedAdmin AND question.isActive = :isActive AND"
						+ " :mcs_item MEMBER OF question.mcs");

		TypedQuery<Question> q = em.createQuery(queryBuilder.toString(),
				Question.class);
		q.setParameter("isAcceptedAdmin", isAcceptedAdmin);
		q.setParameter("isActive", isActive);

		q.setParameter("mcs_item", mc);

		return q.getResultList();
	}

	public static Long countQuestionsNonAcceptedAdmin() {
		// Gets the Sessionattributes
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
		// End filter fuctionality

		StringBuilder queryBuilder = new StringBuilder(
				"SELECT count(Question) FROM Question AS question WHERE ");

		Boolean isAccepted = false;

		if (loggedUser.getIsAdmin()) {

			queryBuilder.append("question.isAcceptedAdmin = :isAccepted ");

		} else {
			queryBuilder
					.append("question.isAcceptedRewiever = :isAccepted AND question.rewiewer = :person");
		}

		EntityManager em = Question.entityManager();

		TypedQuery<Long> q = em
				.createQuery(queryBuilder.toString(), Long.class);

		if (!loggedUser.getIsAdmin()) {
			q.setParameter("person", loggedUser);
		}

		q.setParameter("isAccepted", isAccepted);

		return q.getSingleResult();
	}

	public static List<Question> findQuestionsEntriesNonAcceptedAdmin(
			int start, int length) {
		// Gets the Sessionattributes
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
		// End filter fuctionality

		StringBuilder queryBuilder = new StringBuilder(
				"SELECT Question FROM Question AS question WHERE ");

		Boolean isAccepted = false;

		if (loggedUser.getIsAdmin()) {

			queryBuilder.append("question.isAcceptedAdmin = :isAccepted ");

		} else {
			queryBuilder
					.append("question.isAcceptedRewiever = :isAccepted AND question.rewiewer = :person");
		}

		EntityManager em = Question.entityManager();

		TypedQuery<Question> q = em.createQuery(queryBuilder.toString(),
				Question.class);

		if (!loggedUser.getIsAdmin()) {
			q.setParameter("person", loggedUser);
		}

		q.setParameter("isAccepted", isAccepted);

		return q.getResultList();
	}

	public static List<Question> findQuestionsAnswersNonAcceptedAdmin() {
		Boolean isAcceptedAdmin = false;

		HttpSession session = RequestFactoryServlet.getThreadLocalRequest()
				.getSession();
		;
		String shibdId2 = (String) session.getAttribute("shibdId");
		long institutionId2 = (Long) session.getAttribute("institutionId");

		Person loggedUser = Person.findPersonByShibId(shibdId2);
		Institution institution = Institution.findInstitution(institutionId2);
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException(
					"The person and institution arguments are required");

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
		
		Predicate mainPre2 = criteriaBuilder.equal(from.get("questEvent").get("institution").get("id"), institution.getId());
		Predicate mainpre = criteriaBuilder.and(mainPre2, criteriaBuilder.in(from.get("id")).value(subQuery));
		
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

		query = entityManager().createQuery(findQuestionBySearchFilter(searchText, searchField, institution.getId(), loggedUser));
		
		System.out.println("~~QUERY : " + query.unwrap(Query.class).getQueryString());
		
		return new Long(query.getResultList().size());
	}

	public static List<Question> findQuestionEntriesByPerson(String shibdId,
			Long institutionId, String searchText,
			List<String> searchField, int start, int length) {
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
				institution.getId(), loggedUser));
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
			this.previousVersion.isActive = false;
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
			List<String> searchField, Long institutionId, Person loggedUser) {
		
		try
		{
			CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
			CriteriaQuery<Question> criteriaQuery = criteriaBuilder
					.createQuery(Question.class);
			Root<Question> from = criteriaQuery.from(Question.class);
			CriteriaQuery<Question> select = criteriaQuery.select(from);

			Predicate andAdminPredicate = null;
			Predicate andPredicate = null;

			if (!loggedUser.getIsAdmin()) {
				
				Predicate p1 = criteriaBuilder.equal(from.get("autor").get("id"), loggedUser.getId());
				Predicate p2 = criteriaBuilder.equal(from.get("rewiewer").get("id"), loggedUser.getId());
								
				Predicate mainpre1 = criteriaBuilder.equal(from.get("questEvent")
						.get("institution").get("id"), institutionId);

				mainpre1 = criteriaBuilder.and(criteriaBuilder.or(p2, p1), mainpre1);
				
				Subquery<UserAccessRights> subQry = criteriaQuery
						.subquery(UserAccessRights.class);
				Root queAccRoot = subQry.from(UserAccessRights.class);
				subQry.select(queAccRoot.get("question").get("id")).where(
						criteriaBuilder.equal(queAccRoot.get("person").get("id"),
								loggedUser.getId()));
				Predicate mainpre2 = criteriaBuilder.in(from.get("id")).value(
						subQry);

				Subquery<UserAccessRights> subQuery = criteriaQuery
						.subquery(UserAccessRights.class);
				Root questionAccessRoot = subQuery.from(UserAccessRights.class);
				subQuery.select(questionAccessRoot.get("questionEvent").get("id"))
						.where(criteriaBuilder.equal(
								questionAccessRoot.get("person").get("id"),
								loggedUser.getId()));
				Predicate mainpre3 = criteriaBuilder.in(
						from.get("questEvent").get("id")).value(subQuery);
				
				Subquery<UserAccessRights> instSubQuery = criteriaQuery.subquery(UserAccessRights.class);
				Root instAccessRoot = instSubQuery.from(UserAccessRights.class);
				Predicate instP1 = criteriaBuilder.equal(instAccessRoot.get("person").get("id"), loggedUser.getId());
				Predicate instP2 = criteriaBuilder.equal(instAccessRoot.get("institution").get("id"), institutionId);
				instSubQuery.select(instAccessRoot.get("institution").get("id")).where(criteriaBuilder.and(instP1, instP2));
				Predicate mainpre4 = criteriaBuilder.in(from.get("questEvent").get("institution").get("id")).value(instSubQuery);

				andAdminPredicate = criteriaBuilder.or(mainpre1,
						criteriaBuilder.or(mainpre2, mainpre3, mainpre4));
			} else {
				Predicate adminpre1 = criteriaBuilder.equal(from.get("questEvent")
						.get("institution").get("id"), institutionId);
				andAdminPredicate = adminpre1;
			}
			
			Predicate statusActivePredicate = criteriaBuilder.equal(from.get("isActive"), Boolean.TRUE);
			//Predicate statusNewPredicate = criteriaBuilder.equal(from.get("status"),Status.NEW);
			Predicate statusNewPredicate = from.get("status").in(Status.ACTIVE, Status.NEW);
			
			andAdminPredicate = criteriaBuilder.and(criteriaBuilder.or(statusNewPredicate, statusActivePredicate), andAdminPredicate);

			if (!searchText.equals("")) {
				Expression<String> exp1 = from.get("questionShortName");
				Predicate pre1 = criteriaBuilder.like(exp1, "%" + searchText + "%");
				andPredicate = pre1;
			}
			
			if (searchField.size() > 0 && (!searchText.equals(""))) {
				
				Date dt1 = null;
				Date dt2 = null;
				DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				int ctr = 0;
				
				while (ctr < searchField.size())
				{
					if (searchField.get(ctr).equals("quesitontext")) {
						Expression<String> exp2 = from.get("questionText");
						Predicate pre2 = criteriaBuilder.like(exp2, "%"
								+ searchText + "%");
						andPredicate = criteriaBuilder.and(andPredicate, pre2);
						ctr += 1;
					}

					if (searchField.get(ctr).equals("author")) {
						Expression<String> authorExp = from.get("autor")
								.get("name");
						Predicate pre3 = criteriaBuilder.equal(authorExp, "%"
								+ searchText + "%");
						andPredicate = criteriaBuilder.and(andPredicate, pre3);
						ctr += 1;
					}

					if (searchField.get(ctr).equals("reviewer")) {
						Expression<String> reviwerExp = from.get("rewiewer").get(
								"name");
						Predicate pre4 = criteriaBuilder.equal(reviwerExp, "%"
								+ searchText + "%");
						andPredicate = criteriaBuilder.and(andPredicate, pre4);
						ctr += 1;
					}

					if (searchField.get(ctr).equals("instruction")) {
						Expression<String> exp3 = from.get("comment")
								.get("comment");
						Predicate pre5 = criteriaBuilder.like(exp3, "%"
								+ searchText + "%");
						andPredicate = criteriaBuilder.and(andPredicate, pre5);
						ctr += 1;
					}

					if (searchField.get(ctr).equals("keyword")) {
						ctr += 1;
					}

					if (searchField.get(ctr).equals("institution")) {
						ctr += 1;
						Long selectInstitutionId = Long.parseLong(searchField.get(ctr));
						Predicate pre7 = criteriaBuilder
								.equal(from.get("questEvent").get("institution")
										.get("id"), selectInstitutionId);
						andPredicate = criteriaBuilder.and(andPredicate, pre7);
						ctr += 1;
					}

					if (searchField.get(ctr).equals("specialiation")) {
						ctr += 1;
						Long questionEventId = Long.parseLong(searchField.get(ctr));
						Predicate pre8 = criteriaBuilder.equal(
								from.get("questEvent").get("id"),
								questionEventId);
						andPredicate = criteriaBuilder.and(andPredicate, pre8);
					}

					if (searchField.get(ctr).equals("status")) {
						ctr += 1;
						Status statusVal = Status.valueOf(searchField.get(ctr));
						Predicate pre9 = criteriaBuilder.equal(from.get("status"),
								statusVal);
						andPredicate = criteriaBuilder.and(andPredicate, pre9);
					}

					if (searchField.get(ctr).equals("createdDateFrom"))
					{
						ctr += 1;
						log.info("DATE VALUE : " + searchField.get(ctr));
						
						dt1 = df.parse(searchField.get(ctr));
					}
					
					if (searchField.get(ctr).equals("createdDateTo")) {
						ctr += 1;
						dt2 = df.parse(searchField.get(ctr));
						
						if (dt1 != null && dt2 != null)
						{
							Expression<Date> createdDateExp = from.get("dateAdded");
							Predicate pre10 = criteriaBuilder.between(createdDateExp, dt1, dt2);
							andPredicate = criteriaBuilder.and(andPredicate, pre10);
						}
					}

					if (searchField.get(ctr).equals("usedMcFrom")
							&& searchField.get(ctr).equals("usedMcTo")) {
						ctr += 1;
					}
					
					ctr += 1;
				}
			}

			if (andPredicate == null)
				criteriaQuery.where(andAdminPredicate);
			else
				criteriaQuery.where(criteriaBuilder.and(andAdminPredicate,andPredicate));
			
			return criteriaQuery;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			
		}	
		return null;
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
}
