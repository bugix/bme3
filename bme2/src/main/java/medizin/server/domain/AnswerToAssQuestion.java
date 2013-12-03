package medizin.server.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.validation.constraints.NotNull;

import medizin.shared.Status;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class AnswerToAssQuestion {

	private static Logger log = Logger.getLogger(Question.class);
	
    @NotNull
    private Integer sortOrder;

    @NotNull
    @ManyToOne
    private Answer answers;

    @NotNull
    @ManyToOne(cascade=CascadeType.ALL)
    private AssesmentQuestion assesmentQuestion;
    
    public static List<AnswerToAssQuestion> findAnswerToAssQuestionByAnswer(java.lang.Long answerId) {
		Answer answer = Answer.findAnswer(answerId);
        if (answer == null) throw new IllegalArgumentException("The Answer argument is required");
        EntityManager em = QuestionEvent.entityManager();
        TypedQuery<AnswerToAssQuestion> q = em.createQuery("SELECT a FROM AnswerToAssQuestion a " + 
        		"WHERE a.answers = :answer", AnswerToAssQuestion.class);
        q.setParameter("answer", answer);
		q.setFlushMode(FlushModeType.COMMIT);
        return q.getResultList();
    }
    
    public static Long countAnswerToAssQuestionByAnswer(java.lang.Long answerId) {
		//Answer answer = Answer.findAnswer(answerId);
        if (answerId == null) throw new IllegalArgumentException("The Answer argument is required");
        
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<AnswerToAssQuestion> from = cq.from(AnswerToAssQuestion.class);
		
		cq.select(cb.count(from));
		Predicate predicate = cb.equal(from.get("answers").get("id"), answerId);
		cq.where(predicate);
		
        TypedQuery<Long> q = entityManager().createQuery(cq);
        
        return q.getSingleResult();
       /* EntityManager em = QuestionEvent.entityManager();
        
        
        TypedQuery<Long> q = em.createQuery("SELECT count(a) FROM AnswerToAssQuestion a " + 
        		"WHERE a.answers = :answer", Long.class);
        q.setParameter("answer", answer);
        return q.getSingleResult();*/
    }
    
	public static List<AnswerToAssQuestion> findAnswerToAssQuestionByAssesmentQuestion(java.lang.Long assQuestId) {
		AssesmentQuestion assesmentQuestion = AssesmentQuestion.findAssesmentQuestion(assQuestId);
        if (assesmentQuestion == null) throw new IllegalArgumentException("The AssesmentQuestion argument is required");
        EntityManager em = QuestionEvent.entityManager();
        TypedQuery<AnswerToAssQuestion> q = em.createQuery("SELECT a FROM AnswerToAssQuestion a " + 
        		"WHERE a.assesmentQuestion = :assesmentQuestion ORDER BY a.sortOrder", AnswerToAssQuestion.class);
        q.setParameter("assesmentQuestion", assesmentQuestion);
        return q.getResultList();
    }
	

	public static Long countAnswerToAssQuestionByMatrixValidity(Long questionId) {
		
        if (questionId == null) throw new IllegalArgumentException("The Question argument is required");
        
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<AnswerToAssQuestion> from = cq.from(AnswerToAssQuestion.class);
		
		cq.select(cb.count(from));
		Predicate predicate = cb.equal(from.get("answers").get("question").get("id"), questionId);
		Predicate predicate1 = cb.notEqual(from.get("answers").get("status"), Status.DEACTIVATED);
		cq.where(cb.and(predicate,predicate1));
		
        TypedQuery<Long> q = entityManager().createQuery(cq);
        q.setFlushMode(FlushModeType.COMMIT);
        
        return q.getSingleResult();
    }

	public static List<Answer> findAllAnswerToAssQuestion(Long assessmentQuestionId, int start, int length) {
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");
		
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Answer> cq = cb.createQuery(Answer.class);
		Root<AnswerToAssQuestion> from = cq.from(AnswerToAssQuestion.class);
		cq.orderBy(cb.asc(from.get("sortOrder")));
		Selection<Answer> select = from.get("answers");
		cq.select(select);
		Predicate predicate = allAnswerToAssQuestionByAssessmnetQuestionPredicate(assessmentQuestionId,cb, from);
		cq.where(predicate);
        TypedQuery<Answer> q = entityManager().createQuery(cq);
        q.setFirstResult(start);
        q.setMaxResults(length);
        return q.getResultList();
	}
	
	public static Long countAllAnswerToAssQuestion(Long assessmentQuestionId) {
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");
		
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<AnswerToAssQuestion> from = cq.from(AnswerToAssQuestion.class);
		
		cq.select(cb.count(from));
		
		Predicate predicate = allAnswerToAssQuestionByAssessmnetQuestionPredicate(assessmentQuestionId,cb, from);
		cq.where(predicate);
		
        TypedQuery<Long> q = entityManager().createQuery(cq);
        
        Long size = q.getSingleResult();
        log.info("Assessment Question : " + assessmentQuestionId);
        log.info("Size : " + size);
        return size;
	}

	private static Predicate allAnswerToAssQuestionByAssessmnetQuestionPredicate(Long assessmentQuestionId, CriteriaBuilder cb, Root<AnswerToAssQuestion> from) {
		return cb.equal(from.get("assesmentQuestion").get("id"), assessmentQuestionId);
	}
	
	
//	public static List<AnswerToAssQuestion> findAnswerToAssQuestionByAnswer(java.lang.Long answerId) {
//		Answer answer = Answer.findAnswer(answerId);
//        if (answer == null) throw new IllegalArgumentException("The Answer argument is required");
//        EntityManager em = QuestionEvent.entityManager();
//        TypedQuery<AnswerToAssQuestion> q = em.createQuery("SELECT answerToAssQuestion FROM AnswerToAssQuestion answerToAssQs " + 
//        		"INNER JOIN answerToAssQs.answers answers WHERE answerToAssQs.answers = :answerId", AnswerToAssQuestion.class);
//        q.setParameter("answer", answer);
//        return q.getResultList();
//    }
}
