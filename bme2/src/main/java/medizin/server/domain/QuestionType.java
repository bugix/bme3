package medizin.server.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import medizin.client.ui.widget.Sorting;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.SelectionType;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.google.common.collect.Lists;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class QuestionType {

	private static Logger log = Logger.getLogger(QuestionType.class);
	
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
    
    @NotNull
    private QuestionTypes questionType;
    
    private Integer sumAnswer;
    
    private Integer sumTrueAnswer;
    
    private Integer sumFalseAnswer;
    
    private Integer questionLength;
    
    private Integer answerLength;
    
    private Double diffBetAnswer;
    
    @Column(columnDefinition="BIT", length = 1)
    private Boolean queHaveImage;
    
    @Column(columnDefinition="BIT", length = 1)
    private Boolean queHaveVideo;
    
    @Column(columnDefinition="BIT", length = 1)
    private Boolean queHaveSound;
    
    private Integer keywordCount;
    
    @Column(columnDefinition="BIT", length = 1)
    private Boolean showAutocomplete;
    
    @Column(columnDefinition="BIT", length = 1)
    private Boolean isDictionaryKeyword;
    
    @Column(columnDefinition="BIT", length = 1)
    private Boolean allowTyping;
    
    private Integer minAutoCompleteLetter;
    
    @Column(columnDefinition="BIT", length = 1)
    private Boolean acceptNonKeyword;
    
    private Integer lengthShortAnswer;
    
    private Integer imageWidth;
    
    private Integer imageHeight;
    
    private String imageProportion;
    
    @Column(columnDefinition="BIT", length = 1)
    private Boolean allowOneToOneAss;
    
    @Column(columnDefinition="BIT", length = 1)
    private Boolean linearPoint;
    
    private Double linearPercentage;
    
    @Column(columnDefinition="BIT", length = 1)
    private Boolean richText;
    
    private MultimediaType multimediaType;
    
    private SelectionType selectionType;
    
    private Integer columns;
    
    private Integer thumbWidth;
    
    private Integer thumbHeight;
    
    @Column(columnDefinition="BIT", length = 1)
    private Boolean allowZoomOut;
    
    @Column(columnDefinition="BIT", length = 1)
    private Boolean allowZoomIn;
    
    private Integer maxBytes;
    
    @Column(columnDefinition="BIT", length = 1)
    private Boolean keywordHighlight;
    
    private Integer minLength;
    
    private Integer maxLength;
    
    private Integer minWordCount;
    
    private Integer maxWordCount;
    
    @Column(columnDefinition="BIT", length = 1)
    private Boolean showFilterDialog;
    
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
    	
    	Institution institution = Institution.myGetInstitutionToWorkWith();
    	
    	if (institution == null)
    		return 0l;
    	
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<QuestionType> from = criteriaQuery.from(QuestionType.class);
		criteriaQuery.select(criteriaBuilder.count(from));				
		
		Expression<String> shortNameExp = from.get("shortName");
 		Expression<String> longNameExp = from.get("longName");
 		
 		criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(from.get("institution").get("id"), institution.getId()) ,criteriaBuilder.or(criteriaBuilder.like(shortNameExp, "%" + searchValue + "%"), criteriaBuilder.like(longNameExp, "%" + searchValue + "%"))));
 		
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
    	
    	//System.out.println("in find");
    	
    	Institution institution = Institution.myGetInstitutionToWorkWith();
    	
    	if (institution == null)
    		return Lists.newArrayList();
    	
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
 		criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(from.get("institution").get("id"), institution.getId()), criteriaBuilder.or(criteriaBuilder.like(shortNameExp, "%" + searchValue + "%"), criteriaBuilder.like(longNameExp, "%" + searchValue + "%"))));
 		
 		
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
   
    public static Long countQuestionsForQuestionType(Long questionTypeId) {
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		
		Root<Question> from = criteriaQuery.from(Question.class);
		criteriaQuery.select(criteriaBuilder.count(from));
		
		Predicate p1 = criteriaBuilder.equal(from.get("questionType").get("id"), questionTypeId);
	
		criteriaQuery.where(p1);
		TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);

		log.info("Query is : " + query.unwrap(Query.class).getQueryString());

		log.info("Result list size :" + query.getSingleResult() );
		return query.getSingleResult();
    }
    
    public static List<QuestionType> findAllQuestionTypesForInstituteInSession() {
    	Person userLoggedIn = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (userLoggedIn == null || institution == null)
			return Lists.newArrayList();

		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<QuestionType> criteriaQuery = criteriaBuilder.createQuery(QuestionType.class);
		Root<QuestionType> from = criteriaQuery.from(QuestionType.class);
    	return allQuestionTypesForInstitute(institution, criteriaBuilder, criteriaQuery, from).getResultList();
    }
    
    /*exclude question types already added in assesment*/
    public static List<QuestionType> findAllQuestionTypesForInstituteInSession(Assesment a) {
    	Person userLoggedIn = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (userLoggedIn == null || institution == null)
			return Lists.newArrayList();

		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<QuestionType> criteriaQuery = criteriaBuilder.createQuery(QuestionType.class);
		Root<QuestionType> from = criteriaQuery.from(QuestionType.class);
		
		Predicate p1 = criteriaBuilder.equal(from.get("institution"),institution);	
		criteriaQuery.where(p1);
		TypedQuery<QuestionType> query = entityManager().createQuery(criteriaQuery);

		log.info("Query is : " + query.unwrap(Query.class).getQueryString());
		
		List<QuestionType> questionTypeList=query.getResultList();
		
		List<QuestionType> returnTypeList=new ArrayList<QuestionType>();
		
		 /*exclude question types already added in assesment*/
		 TypedQuery<QuestionTypeCountPerExam> q = entityManager().createQuery("SELECT questTypePerExam FROM QuestionTypeCountPerExam AS questTypePerExam WHERE questTypePerExam.assesment = :assesment ", QuestionTypeCountPerExam.class);
	        q.setParameter("assesment", a);
		List<QuestionTypeCountPerExam> questionTypeCountPerExamList=q.getResultList();
		
		for(QuestionType questionType:questionTypeList)
		{
			boolean excludeQuestionType=false;
			for(QuestionTypeCountPerExam questionTypeCountPerExam:questionTypeCountPerExamList)
			{
				Set<QuestionType> questionTypes=questionTypeCountPerExam.getQuestionTypesAssigned();
				if(questionTypes.contains(questionType))
				{
					excludeQuestionType=true;
					break;
				}
			}
			if(!excludeQuestionType)
			{
				returnTypeList.add(questionType);
			}
		}
		
    	return returnTypeList;
    }
    
    
    private final static <T> TypedQuery<T> allQuestionTypesForInstitute(Institution institution,CriteriaBuilder criteriaBuilder,CriteriaQuery<T> criteriaQuery,Root<QuestionType> from) {
    	
		Predicate p1 = criteriaBuilder.equal(from.get("institution"),institution);	
		criteriaQuery.where(p1);
		TypedQuery<T> query = entityManager().createQuery(criteriaQuery);

		log.info("Query is : " + query.unwrap(Query.class).getQueryString());

		return query;
    }
    
    @PreRemove
    public void preRemove() {
    	
    	Person loggedPerson = Person.findLoggedPersonByShibId();
    	Institution loggedInstitute = Institution.myGetInstitution();
    	
    	if (loggedPerson == null || loggedInstitute == null)
    	{
    		//throw exception
    		throw new IllegalArgumentException("Question type is not removed");
    	}
    	
    	Long questionCount = countQuestionsForQuestionType(this.getId());
    	if (questionCount > 0)
    	{
    		//throw exception
    		throw new IllegalArgumentException("Question type is not removed");
    	}
    }

	public static QuestionType findQuestionTypeByShortName(String questionTypeName, Institution institution) {
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
    	CriteriaQuery<QuestionType> criteriaQuery = criteriaBuilder.createQuery(QuestionType.class);
    	Root<QuestionType> from = criteriaQuery.from(QuestionType.class);
    	Predicate pre1 = criteriaBuilder.equal(from.get("shortName"), questionTypeName);
    	Predicate pre2 = criteriaBuilder.equal(from.get("institution").get("id"), institution.getId());
    	criteriaQuery.where(criteriaBuilder.and(pre1,pre2));
		TypedQuery<QuestionType> query = entityManager().createQuery(criteriaQuery);
		List<QuestionType> resultList = query.getResultList();
		if(resultList != null && resultList.size() > 0) {
			return resultList.get(0);
		}
		return null;
	}
}
