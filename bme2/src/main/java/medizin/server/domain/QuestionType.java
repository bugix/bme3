package medizin.server.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import medizin.client.ui.widget.Sorting;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.SelectionType;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class QuestionType {

    /*@NotNull
    @Column(unique = true)
    @Size(min = 1, max = 20)
    private String questionTypeName;

    @Value("false")
    private Boolean isWeil;

    @NotNull
    @Min(0L)
    @Max(99L)
    private Integer trueAnswers;

    @NotNull
    @Min(0L)
    @Max(99L)
    private Integer falseAnswers;

    @NotNull
    @Min(0L)
    @Max(99L)
    private Integer sumAnswers;

    @NotNull
    @Min(0L)
    @Max(999L)
    private Integer maxLetters;*/
    
    private String shortName;
    
    private String longName;
    
    private String description;
    
    @ManyToOne
    private Institution institution;
    
    private QuestionTypes questionType;
    
    private Integer sumAnswer;
    
    private Integer sumTrueAnswer;
    
    private Integer sumFalseAnswer;
    
    private Integer questionLength;
    
    private Integer answerLength;
    
    private Double diffBetAnswer;
    
    private Boolean queHaveImage;
    
    private Boolean queHaveVideo;
    
    private Boolean queHaveSound;
    
    private Integer keywordCount;
    
    private Boolean showAutocomplete;
    
    private Boolean isDictionaryKeyword;
    
    private Boolean allowTyping;
    
    private Integer minAutoCompleteLetter;
    
    private Boolean acceptNonKeyword;
    
    private Integer lengthShortAnswer;
    
    private Integer imageWidth;
    
    private Integer imageHeight;
    
    private String imageProportion;
    
    private Boolean allowOneToOneAss;
    
    private Boolean linearPoint;
    
    private Double linearPercentage;
    
    private Boolean richText;
    
    private MultimediaType multimediaType;
    
    private SelectionType selectionType;
    
    private Integer columns;
    
    private Integer thumbWidth;
    
    private Integer thumbHeight;
    
    private Boolean allowZoomOut;
    
    private Boolean allowZoomIn;
    
    private Integer maxBytes;
    
    private Boolean keywordHighlight;
    
    private Integer minLength;
    
    private Integer maxLength;
    
    private Integer minWordCount;
    
    private Integer maxWordCount;
    
    public static List<QuestionType> findAllQuestionTypesByAssesment(Long assesmentId) {
		Assesment assesment = Assesment.findAssesment(assesmentId);
        if (assesment == null) throw new IllegalArgumentException("The institution argument is required");
        EntityManager em = QuestionEvent.entityManager();
        TypedQuery<QuestionType> q = em.createQuery("SELECT DISTINCT questtype FROM AssesmentQuestion assquests " + 
        		"INNER JOIN assquests.question quest INNER JOIN quest.questionType questtype WHERE assquests.assesment = :assesment", QuestionType.class);
        q.setParameter("assesment", assesment);
        return q.getResultList();
    }
    
     
    public static Long countAllQuestionType(String searchValue) {
    //	EntityManager em = QuestionEvent.entityManager();
    //	System.out.println("countAllQuestionType call");
    	
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<QuestionType> from = criteriaQuery.from(QuestionType.class);
		criteriaQuery.select(criteriaBuilder.count(from));				
		
		Expression<String> shortNameExp = from.get("shortName");
 		Expression<String> longNameExp = from.get("longName");
 		criteriaQuery.where(criteriaBuilder.or(criteriaBuilder.like(shortNameExp, "%" + searchValue + "%"), criteriaBuilder.like(longNameExp, "%" + searchValue + "%")));
 		
		//criteriaQuery.where(criteriaBuilder.equal(from.get("topic"), topicId));
		TypedQuery<Long> result = entityManager().createQuery(criteriaQuery);
	//	System.out.println("Count Query String: " + result.unwrap(Query.class).getQueryString());
    	/*TypedQuery<Long> q = em.createQuery("SELECT COUNT(role) FROM RoleTopic AS role WHERE specialisation = :sp and role.name LIKE :name1", Long.class);
    	q.setParameter("sp", specialisation);
    	q.setParameter("name1", "%" + name + "%");
    	return q.getSingleResult();*/
    	return result.getSingleResult();
    }
    
    public static List<QuestionType> findAllQuestionType(Integer start,Integer end,String sortBy,Sorting sortOrder,String searchValue) {
		
    	/* CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
 		CriteriaQuery<QuestionType> criteriaQuery = criteriaBuilder.createQuery(QuestionType.class);
 		Root<QuestionType> from = criteriaQuery.from(QuestionType.class);
 		CriteriaQuery<QuestionType> select = criteriaQuery.select(from);
 		TypedQuery<QuestionType> result = entityManager().createQuery(criteriaQuery);
 		return result.getResultList();*/
    //	System.out.println("Call findAllQuestionType");
    	
    	System.out.println("in find");
    	 CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
 		CriteriaQuery<QuestionType> criteriaQuery = criteriaBuilder.createQuery(QuestionType.class);
 		Root<QuestionType> from = criteriaQuery.from(QuestionType.class);
 		criteriaQuery.select(from);
 		if(sortOrder==Sorting.ASC)
 		{
 			criteriaQuery.orderBy(criteriaBuilder.asc(from.get(sortBy)));
 		}
 		else
 		{
 			criteriaQuery.orderBy(criteriaBuilder.desc(from.get(sortBy)));
 		}
 		
 		
 		Expression<String> shortNameExp = from.get("shortName");
 		Expression<String> longNameExp = from.get("longName");
 		criteriaQuery.where(criteriaBuilder.or(criteriaBuilder.like(shortNameExp, "%" + searchValue + "%"), criteriaBuilder.like(longNameExp, "%" + searchValue + "%")));
 		
 		
 		TypedQuery<QuestionType> q = entityManager().createQuery(criteriaQuery);
 		q.setFirstResult(start);
 		q.setMaxResults(end);
 	//	System.out.println("Query String: " + q.unwrap(Query.class).getQueryString());
 		return q.getResultList();

    	
 		
        /*
        EntityManager em = QuestionEvent.entityManager();
        TypedQuery<QuestionType> q = em.createQuery("SELECT DISTINCT questtype FROM AssesmentQuestion assquests " + 
        		"INNER JOIN assquests.question quest INNER JOIN quest.questionType questtype WHERE assquests.assesment = :assesment", QuestionType.class);
        q.setParameter("assesment", assesment);
        return q.getResultList();*/
    	/*result.setFirstResult(start);
    	result.setMaxResults(end);*/
    	
    	
    }
   
    
}
