package medizin.server.domain;

import java.util.List;

import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import medizin.shared.Status;
import medizin.shared.Validity;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.google.common.collect.Lists;


@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class MatrixValidity {

	private static Logger log = Logger.getLogger(MatrixValidity.class);

	@NotNull
	@ManyToOne
	private Answer answerX;

	@NotNull
	@ManyToOne
	private Answer answerY;

	@NotNull
	@Enumerated
	private Validity validity;

	public static List<MatrixValidity> findAllMatrixValidityForQuestion(Long id) {

		log.info("Find all matrix validty with answer for given question id : "+ id);

		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<MatrixValidity> criteriaQuery = criteriaBuilder.createQuery(MatrixValidity.class);
		Root<MatrixValidity> from = criteriaQuery.from(MatrixValidity.class);

		Predicate p1 = criteriaBuilder.equal(from.get("answerX").get("question").get("id"), id);
		Predicate p2 = criteriaBuilder.equal(from.get("answerY").get("question").get("id"), id);
		Expression<String> exp1 = from.get("answerX").get("status");
		Expression<String> exp2 = from.get("answerY").get("status");
		Predicate p3 = criteriaBuilder.notEqual(exp1, Status.DEACTIVATED);
		Predicate p4 = criteriaBuilder.notEqual(exp2, Status.DEACTIVATED);
		criteriaQuery.where(criteriaBuilder.and(p1, p2, p3, p4));
		TypedQuery<MatrixValidity> query = entityManager().createQuery(criteriaQuery);

		log.info("Query is : " + query.unwrap(Query.class).getQueryString());

		log.info("Result list size :" + query.getResultList().size());
		return query.getResultList();

	}
	
	public static List<MatrixValidity> findAllMatrixValidityForQuestionForAcceptAnswerView(Long id, Boolean isInstitutionalAdmin, Integer start, Integer length) {
		
		Person userLoggedIn = Person.myGetLoggedPerson();
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<MatrixValidity> criteriaQuery = criteriaBuilder.createQuery(MatrixValidity.class);
		
		Root<MatrixValidity> from = criteriaQuery.from(MatrixValidity.class);
		
		Predicate predicate = matrixValidityForQuestionForAcceptAnswerViewPredicate(id, isInstitutionalAdmin, userLoggedIn, criteriaBuilder, from);
		criteriaQuery.where(predicate);
		TypedQuery<MatrixValidity> query = entityManager().createQuery(criteriaQuery);

		query.setFirstResult(start);
		query.setMaxResults(length);
		
		log.info("Query is : " + query.unwrap(Query.class).getQueryString());

		log.info("Result list size :" + query.getResultList().size());
		return query.getResultList();
	}

	private static Predicate matrixValidityForQuestionForAcceptAnswerViewPredicate(Long id, Boolean isInstitutionalAdmin, Person userLoggedIn, CriteriaBuilder criteriaBuilder, Root<MatrixValidity> from) {
		Predicate p1 = criteriaBuilder.equal(from.get("answerX").get("question").get("id"), id);
		Predicate p2 = criteriaBuilder.equal(from.get("answerY").get("question").get("id"), id);
		Expression<Status> exp1 = from.get("answerX").get("status");
		Expression<Status> exp2 = from.get("answerY").get("status");
		
		/*List<Status> statusList = Lists.newArrayList(Status.NEW);
		if(userLoggedIn != null) {
			if(userLoggedIn.getIsAdmin() || isInstitutionalAdmin == true) {
				// it is admin
				statusList.add(Status.ACCEPTED_REVIEWER);
			}else {
				// it is reviewer
				statusList.add(Status.ACCEPTED_ADMIN);
			}	
		}
		Predicate p3 = exp1.in(statusList);
		Predicate p4 = exp2.in(statusList);*/
		Predicate p3;
		Predicate p4;
		if (userLoggedIn.getIsAdmin() || isInstitutionalAdmin == true)
		{
			p3 = criteriaBuilder.equal(from.get("answerX").get("isAnswerAcceptedAdmin"), false);
			p4 = criteriaBuilder.equal(from.get("answerY").get("isAnswerAcceptedAdmin"), false);
		}
		else
		{
			final Predicate preX2 = criteriaBuilder.and(criteriaBuilder.equal(from.get("answerX").get("isAnswerAcceptedReviewWahrer"), false), criteriaBuilder.equal(from.get("answerX").get("rewiewer").get("id"), userLoggedIn.getId()));
			final Predicate preX3 = criteriaBuilder.and(criteriaBuilder.equal(from.get("answerX").get("isAnswerAcceptedAutor"), false), criteriaBuilder.equal(from.get("answerX").get("autor").get("id"), userLoggedIn.getId()));
			p3 = criteriaBuilder.or(preX2, preX3);
			
			final Predicate preY2 = criteriaBuilder.and(criteriaBuilder.equal(from.get("answerY").get("isAnswerAcceptedReviewWahrer"), false), criteriaBuilder.equal(from.get("answerX").get("rewiewer").get("id"), userLoggedIn.getId()));
			final Predicate preY3 = criteriaBuilder.and(criteriaBuilder.equal(from.get("answerY").get("isAnswerAcceptedAutor"), false), criteriaBuilder.equal(from.get("answerX").get("autor").get("id"), userLoggedIn.getId()));
			p4 = criteriaBuilder.or(preY2, preY3);
		}
		return criteriaBuilder.and(p1, p2, p3, p4);
	}
	
	public static Long countAllMatrixValidityForQuestionForAcceptAnswerView(Long id,Boolean isInstitutionalAdmin) {
		
		Person userLoggedIn = Person.myGetLoggedPerson();
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		
		Root<MatrixValidity> from = criteriaQuery.from(MatrixValidity.class);
		criteriaQuery.select(criteriaBuilder.count(from));
		
		Predicate predicate = matrixValidityForQuestionForAcceptAnswerViewPredicate(id, isInstitutionalAdmin, userLoggedIn, criteriaBuilder, from);
		criteriaQuery.where(predicate);
		TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);

		//log.info("Query is : " + query.unwrap(Query.class).getQueryString());

		log.info("Result list size :" + query.getResultList().size());
		return query.getSingleResult();
	}
	
	public static Predicate nonAcceptAnswerPredicate(Long questionId, Person loggedUser, Institution institution,Boolean isInstitutionalAdmin, CriteriaBuilder cb, Root<Answer> from) {
		final Predicate pre0 = cb.equal(from.get("question").get("questEvent").get("institution"), institution);
		Predicate pre1 = cb.equal(from.get("question").get("id"), questionId);
		
		if (loggedUser.getIsAdmin() || isInstitutionalAdmin == true)
		{
			pre1 = cb.and(cb.equal(from.get("isAnswerAcceptedAdmin"), false),pre1);
		}
		else
		{
			final Predicate pre2 = cb.and(cb.equal(from.get("isAnswerAcceptedReviewWahrer"), false), cb.equal(from.get("rewiewer").get("id"), loggedUser.getId()));
			final Predicate pre3 = cb.and(cb.equal(from.get("isAnswerAcceptedAutor"), false), cb.equal(from.get("autor").get("id"), loggedUser.getId()));

			pre1 = cb.and(pre1, cb.or(pre2, pre3));
		}
		
		return cb.and(pre0,pre1);
	}
	
	public static Boolean deleteAnswerAndItsMatrixValidity(Long answerId, Boolean isAnswerX) {
		log.info("answer : " + answerId);
		log.info("isAnswerX : " + isAnswerX);
		
		if(answerId != null && isAnswerX != null) {
			Answer answer = Answer.findAnswer(answerId);
			List<MatrixValidity> matrixValidities = MatrixValidity.findAllMatrixValidityForAnswer(answerId,isAnswerX);
			Long fullMatrixSize = MatrixValidity.countAllMatrixValidityForQuestion(answer.getQuestion().getId());
			
			for (MatrixValidity matrixValidity : matrixValidities) {
				
				Answer otherAnswer;
				if(isAnswerX == true) {
					otherAnswer = matrixValidity.answerY;
				}else {
					otherAnswer = matrixValidity.answerX;
				}
				
				//matrixValidity.remove();
				if(fullMatrixSize == matrixValidities.size()) {
					log.info("Delete the full matrix validity for given answerX");
					otherAnswer.setStatus(Status.DEACTIVATED);
					otherAnswer.persist();
					//otherAnswer.remove();
				}
			}
			answer.setStatus(Status.DEACTIVATED);
			answer.persist();
			//answer.remove();
			return fullMatrixSize == matrixValidities.size();
		}else {
			log.error("Answer is null");
		}
		return false;
	}


	public static List<MatrixValidity> findAllMatrixValidityForAnswer(Long answerId, Boolean isAnswerX) {
		log.info("find All MatrixValidity for given answer : "+ answerId);

		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<MatrixValidity> criteriaQuery = criteriaBuilder.createQuery(MatrixValidity.class);
		Root<MatrixValidity> from = criteriaQuery.from(MatrixValidity.class);
		Predicate p1;
		if(isAnswerX == true) {
			p1 = criteriaBuilder.equal(from.get("answerX").get("id"), answerId);	
		}else {
			p1 = criteriaBuilder.equal(from.get("answerY").get("id"), answerId);	
		}
		
		criteriaQuery.where(p1);
		TypedQuery<MatrixValidity> query = entityManager().createQuery(criteriaQuery);

		log.info("Query is : " + query.unwrap(Query.class).getQueryString());

		log.info("Result list size :" + query.getResultList().size());
		return query.getResultList();
		
	}
	
	public static Long countAllMatrixValidityForQuestion(Long id) {

		log.info("Find all matrix validty size with answer for given question id : "+ id);

		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<MatrixValidity> from = criteriaQuery.from(MatrixValidity.class);
		criteriaQuery.select(criteriaBuilder.count(from));	

		Predicate p1 = criteriaBuilder.equal(from.get("answerX").get("question").get("id"), id);
		Predicate p2 = criteriaBuilder.equal(from.get("answerY").get("question").get("id"), id);
		Expression<String> exp1 = from.get("answerX").get("status");
		Expression<String> exp2 = from.get("answerY").get("status");
		Predicate p3 = criteriaBuilder.notEqual(exp1,Status.DEACTIVATED);
		Predicate p4 = criteriaBuilder.notEqual(exp2,Status.DEACTIVATED);
		criteriaQuery.where(criteriaBuilder.and(p1, p2, p3, p4));
		TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);

		log.info("Query is : " + query.unwrap(Query.class).getQueryString());

		log.info("Result list count :" + query.getSingleResult());
		return query.getSingleResult();

	}
	
	public static List<MatrixValidity> findAllMatrixValidityForAcceptQuestion(Long id, Integer start, Integer length) {
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<MatrixValidity> criteriaQuery = criteriaBuilder.createQuery(MatrixValidity.class);
		
		Root<MatrixValidity> from = criteriaQuery.from(MatrixValidity.class);
		
		Predicate p1 = criteriaBuilder.equal(from.get("answerX").get("question").get("id"), id);
		Predicate p2 = criteriaBuilder.equal(from.get("answerY").get("question").get("id"), id);
		
		Expression<Status> exp1 = from.get("answerX").get("status");
		Expression<Status> exp2 = from.get("answerY").get("status");
		
		Predicate p3 = criteriaBuilder.notEqual(exp1, Status.DEACTIVATED);
		Predicate p4 = criteriaBuilder.notEqual(exp2, Status.DEACTIVATED);
		
		criteriaQuery.where(criteriaBuilder.and(p1, p2, p3, p4));
		TypedQuery<MatrixValidity> query = entityManager().createQuery(criteriaQuery);

		query.setFirstResult(start);
		query.setMaxResults(length);
		
		/*log.info("Query is : " + query.unwrap(Query.class).getQueryString());
		log.info("Result list size :" + query.getResultList().size());*/

		return query.getResultList();
	}
	
	public static Long countAllMatrixValidityForAcceptQuestion(Long id) {
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		
		Root<MatrixValidity> from = criteriaQuery.from(MatrixValidity.class);
		criteriaQuery.select(criteriaBuilder.count(from));
		
		Predicate p1 = criteriaBuilder.equal(from.get("answerX").get("question").get("id"), id);
		Predicate p2 = criteriaBuilder.equal(from.get("answerY").get("question").get("id"), id);
		
		Expression<Status> exp1 = from.get("answerX").get("status");
		Expression<Status> exp2 = from.get("answerY").get("status");
	
		Predicate p3 = criteriaBuilder.notEqual(exp1, Status.DEACTIVATED);
		Predicate p4 = criteriaBuilder.notEqual(exp2, Status.DEACTIVATED);
		
		criteriaQuery.where(criteriaBuilder.and(p1, p2, p3, p4));
		TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);

		/*log.info("Query is : " + query.unwrap(Query.class).getQueryString());*/
		/*log.info("Result list size :" + query.getResultList().size());*/
		
		return query.getSingleResult();
	}
	
	public static List<MatrixValidity> findAllMatrixValidityForQuestionWithAnyStatus(Long id) {

		log.info("Find all matrix validty with answer for given question id : "+ id);

		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<MatrixValidity> criteriaQuery = criteriaBuilder.createQuery(MatrixValidity.class);
		Root<MatrixValidity> from = criteriaQuery.from(MatrixValidity.class);

		Predicate p1 = criteriaBuilder.equal(from.get("answerX").get("question").get("id"), id);
		Predicate p2 = criteriaBuilder.equal(from.get("answerY").get("question").get("id"), id);
		
		criteriaQuery.where(criteriaBuilder.and(p1, p2));
		TypedQuery<MatrixValidity> query = entityManager().createQuery(criteriaQuery);

		log.info("Query is : " + query.unwrap(Query.class).getQueryString());

		log.info("Result list size :" + query.getResultList().size());
		return query.getResultList();
	}

	public static List<MatrixValidity> findallMatrixValidityForAnswers(List<Long> answerList) {
		
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<MatrixValidity> criteriaQuery = criteriaBuilder.createQuery(MatrixValidity.class);
		
		Root<MatrixValidity> from = criteriaQuery.from(MatrixValidity.class);
		
		Predicate p1 = from.get("answerX").get("id").in(answerList);
		Predicate p2 = from.get("answerY").get("id").in(answerList);
		
		criteriaQuery.where(criteriaBuilder.or(p1, p2));
		TypedQuery<MatrixValidity> query = entityManager().createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public static Long countAllMatrixValidityForForceActiveByQuestion(Long id,Boolean isInstitutionalAdmin) {
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		
		Root<MatrixValidity> from = criteriaQuery.from(MatrixValidity.class);
		criteriaQuery.select(criteriaBuilder.count(from));
		
		Predicate p1 = criteriaBuilder.equal(from.get("answerX").get("question").get("id"), id);
		Predicate p2 = criteriaBuilder.equal(from.get("answerY").get("question").get("id"), id);
		Expression<Status> exp1 = from.get("answerX").get("status");
		Expression<Status> exp2 = from.get("answerY").get("status");
		
		Person userLoggedIn = Person.myGetLoggedPerson();
		List<Status> statusList = Lists.newArrayList(Status.NEW, Status.ACCEPTED_ADMIN, Status.ACCEPTED_REVIEWER);
		Predicate p3 = exp1.in(statusList);
		Predicate p4 = exp2.in(statusList);
		criteriaQuery.where(criteriaBuilder.and(p1, p2, p3, p4));
		TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);
		log.info("Result list size :" + query.getResultList().size());
		return query.getSingleResult();
	}
	
	public static List<MatrixValidity> findAllMatrixValidityForForceActiveByQuestion(Long id, Boolean isInstitutionalAdmin, Integer start, Integer length) {
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<MatrixValidity> criteriaQuery = criteriaBuilder.createQuery(MatrixValidity.class);
		
		Root<MatrixValidity> from = criteriaQuery.from(MatrixValidity.class);
		
		Predicate p1 = criteriaBuilder.equal(from.get("answerX").get("question").get("id"), id);
		Predicate p2 = criteriaBuilder.equal(from.get("answerY").get("question").get("id"), id);
		Expression<Status> exp1 = from.get("answerX").get("status");
		Expression<Status> exp2 = from.get("answerY").get("status");
		
		Person userLoggedIn = Person.myGetLoggedPerson();
		List<Status> statusList = Lists.newArrayList(Status.NEW, Status.ACCEPTED_ADMIN, Status.ACCEPTED_REVIEWER);
		Predicate p3 = exp1.in(statusList);
		Predicate p4 = exp2.in(statusList);
		criteriaQuery.where(criteriaBuilder.and(p1, p2, p3, p4));
		TypedQuery<MatrixValidity> query = entityManager().createQuery(criteriaQuery);

		query.setFirstResult(start);
		query.setMaxResults(length);
		log.info("Result list size :" + query.getResultList().size());
		return query.getResultList();
	}
}
