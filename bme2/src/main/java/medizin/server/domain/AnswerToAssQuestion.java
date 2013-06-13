package medizin.server.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class AnswerToAssQuestion {

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
        return q.getResultList();
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
