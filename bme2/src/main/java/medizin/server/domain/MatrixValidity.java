package medizin.server.domain;

import java.util.List;

import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import medizin.client.shared.Validity;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;


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

		criteriaQuery.where(criteriaBuilder.and(p1, p2));
		TypedQuery<MatrixValidity> query = entityManager().createQuery(criteriaQuery);

		log.info("Query is : " + query.unwrap(Query.class).getQueryString());

		log.info("Result list size :" + query.getResultList().size());
		return query.getResultList();

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
				
				matrixValidity.remove();
				if(fullMatrixSize == matrixValidities.size()) {
					log.info("Delete the full matrix validity for given answerX");
					otherAnswer.remove();
				}
			}
			answer.remove();
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

		criteriaQuery.where(criteriaBuilder.and(p1, p2));
		TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);

		log.info("Query is : " + query.unwrap(Query.class).getQueryString());

		log.info("Result list count :" + query.getSingleResult());
		return query.getSingleResult();

	}
}
