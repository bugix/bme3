package medizin.server.domain;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.SetJoin;
import javax.persistence.criteria.Subquery;
import javax.validation.constraints.NotNull;

import medizin.client.ui.widget.Sorting;
import medizin.server.mail.EmailServiceImpl;
import medizin.server.utils.BMEUtils;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.Status;
import medizin.shared.Validity;
import medizin.shared.criteria.AdvancedSearchCriteria;
import medizin.shared.criteria.AdvancedSearchCriteriaUtils;
import medizin.shared.criteria.BindType;
import medizin.shared.criteria.Comparison;
import medizin.shared.criteria.PossibleFields;
import medizin.shared.utils.PersonAccessRight;
import medizin.shared.utils.SharedConstant;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class AssesmentQuestion {

	private static Logger log = Logger.getLogger(AssesmentQuestion.class);

    @Value("0")
    private Integer orderAversion;

    @Value("0")
    private Integer orderBversion;

    private Double trenschaerfe;

    private Double schwierigkeit;

    @NotNull
    @Value("false")
    @Column(columnDefinition="BIT", length = 1)
    private Boolean isAssQuestionAcceptedRewiever;

    @NotNull
    @Value("false")
    @Column(columnDefinition="BIT", length = 1)
    private Boolean isAssQuestionAcceptedAdmin;//accepted status

    @NotNull
    @Value("false")
    @Column(columnDefinition="BIT", length = 1)
    private Boolean isAssQuestionAdminProposal;//proposed question

    @Column(columnDefinition="BIT", length = 1)
    private Boolean isAssQuestionAcceptedAutor;//new status
    
    @NotNull
    @Value("false")
    @Column(columnDefinition="BIT", length = 1)
    private Boolean isForcedByAdmin;//force status
    
    @NotNull
    @ManyToOne
    private Question question;

    @ManyToOne
    private Assesment assesment;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assesmentQuestion",fetch=FetchType.LAZY)
    private Set<AnswerToAssQuestion> answersToAssQuestion = new HashSet<AnswerToAssQuestion>();
    
    //RedactionalBase code
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="dd.MM.YYYY hh:mm:ss")
    private Date dateAdded;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="dd.MM.YYYY hh:mm:ss")
    private Date dateChanged;

    //@NotNull
    @ManyToOne
    private Person rewiewer;

    @NotNull
    @ManyToOne
    private Person autor;
    
    private String percent;
    
    private String points;
    
    @Column(columnDefinition="BIT", length = 1)
    private Boolean eliminateQuestion;
    
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    /*
	 * Author pull down for admin / institutional Admin
	 * 
	 * Show Author / examiner assigned in particular assessment
	 * */
     
     public static List<Person> findAuthorListByAssesment(Assesment assesment){
     	
     	log.info("findAuthorListByAssesment ");
     	
     	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
     	CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
     	Root<QuestionSumPerPerson> from = criteriaQuery.from(QuestionSumPerPerson.class);
     	criteriaQuery.orderBy(criteriaBuilder.asc(from.get("responsiblePerson").get("id")));
     	criteriaQuery.distinct(true);
     	Path<Person> path = from.get("responsiblePerson");
     	criteriaQuery.select(path);
     	Predicate pre1 = criteriaBuilder.equal(from.get("assesment").get("id"), assesment.getId());
     	criteriaQuery.where(pre1);
     	
     	TypedQuery<Person> query = entityManager().createQuery(criteriaQuery);
     	return query.getResultList();
     	
     	/*Set<QuestionSumPerPerson> questionSumPerPersons=assesment.getQuestionSumPerPerson();
     	
     	List<Person> authorList=new ArrayList<Person>();
     	for(QuestionSumPerPerson questionSumPerPerson:questionSumPerPersons)
     	{
     		if(!authorList.contains(questionSumPerPerson.getResponsiblePerson()))
     		authorList.add(questionSumPerPerson.getResponsiblePerson());
     	}
     	return authorList;*/

     }
    
     public static List<AssesmentQuestion> findQuestionsByAssesmentRepeFor(Long assessmentId,List<Long> availableQuestionTypeList)
 	{
		
 		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
 		CriteriaQuery<AssesmentQuestion> criteriaQuery = criteriaBuilder.createQuery(AssesmentQuestion.class);

 		Root<AssesmentQuestion> assesmentQuestionRoot = criteriaQuery.from(AssesmentQuestion.class); 	
 		
 		Predicate p1 = criteriaBuilder.equal(assesmentQuestionRoot.get("assesment").get("id"),assessmentId);
 		Predicate p2 = criteriaBuilder.in(assesmentQuestionRoot.get("question").get("questionType").get("id")).value(availableQuestionTypeList);
 		Predicate p3 = criteriaBuilder.equal(assesmentQuestionRoot.get("isAssQuestionAcceptedAdmin"), Boolean.TRUE);
 		Predicate p4 = criteriaBuilder.equal(assesmentQuestionRoot.get("isForcedByAdmin"), Boolean.TRUE);
 		
 		Predicate mainPredicate = criteriaBuilder.and(p1, criteriaBuilder.or(p3, p4), p2);
 		criteriaQuery.where(mainPredicate);
 		
 		TypedQuery<AssesmentQuestion> query = entityManager().createQuery(criteriaQuery);
 			
 		return query.getResultList();
 		
 	}
    
     
    public static List<Long> findQuestionsByAssesment(Long assessmentId)
  	{
  				
  		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
  		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
  		Root<AssesmentQuestion> assesmentQuestionRoot = criteriaQuery.from(AssesmentQuestion.class);
  	
   		Predicate p2 = criteriaBuilder.equal(assesmentQuestionRoot.get("assesment"),assessmentId);
  		
  		criteriaQuery.select(assesmentQuestionRoot.get("question").get("id"));
  		  	
  		criteriaQuery.where(p2);
  		
  		Query query = entityManager().createQuery(criteriaQuery);
  		
  		
  		List<Long> result = query.getResultList();
  		
  		
  		return result;
  		
  	}
    
   /*
    * Past Question Tab
    * 
    * Logic
    * 1. Assessment Question with (status force or accepted) and current date > date of 	assessment.  
    * 2. An examiner may see only questions of specialization 	where he has access.	
    * 3. group by question- no duplicate question
    * 4. Not Assigned in current Assesment
    * */
    
   /* public static List<AssesmentQuestion> findAssesmentQuestionsByMc(Long assesmentId,Long id, List<String> criteriaStringList, String questionId,String questionType,String questionName,Person author){
    	
    	log.info("Past Question Tab ");
    	
        Boolean isAcceptedAdmin = true;
        
        Mc mc = Mc.findMc(id);
        if (mc == null) throw new IllegalArgumentException("The mcs argument is required");
        EntityManager em = Question.entityManager();
        
        //get user who is logged in
        Person userLoggedIn=Person.myGetLoggedPerson();
        
        //get institution
		Institution institution = Institution.myGetInstitutionToWorkWith();
        
		//create query
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<AssesmentQuestion> criteriaQuery = criteriaBuilder.createQuery(AssesmentQuestion.class);
		Root<AssesmentQuestion> from = criteriaQuery.from(AssesmentQuestion.class);
		criteriaQuery.distinct(true);
		
		//Join
		Join<AssesmentQuestion,Question> questionJoin=from.join("question");
		SetJoin<Question,Mc> mcsJoin=questionJoin.joinSet("mcs");
		CriteriaQuery<AssesmentQuestion> select = criteriaQuery.select(from);
		
		//accepted
		Predicate pre1=criteriaBuilder.equal(from.get("isAssQuestionAcceptedAdmin"), new Boolean(true));
		
		//force accepted
		Predicate pre2=criteriaBuilder.equal(from.get("isForcedByAdmin"), new Boolean(true));
		
		//accepted / force accepted
		Predicate pre3=criteriaBuilder.or(pre1,pre2);		
		
		//active question
		Predicate pre4=criteriaBuilder.equal(questionJoin.get("status"), Status.ACTIVE);
		
		//search criteria of question
		
		Predicate searchPre1=null;
		Predicate searchPre2=null;
		Predicate searchPre3=null;
		Predicate searchPre=null;
		
		if(!questionId.equalsIgnoreCase(""))
		{
			searchPre1=criteriaBuilder.equal(questionJoin.get("id"), new Long(questionId));
			searchPre = searchPre1;
		}
		if(!questionType.equalsIgnoreCase(""))
		{
			Predicate shortNamePre=criteriaBuilder.like(questionJoin.get("questionType").<String>get("shortName"),"%"+questionType+"%");
			Predicate longNamePre=criteriaBuilder.like(questionJoin.get("questionType").<String>get("longName"),"%"+questionType+"%");
			searchPre2=criteriaBuilder.or(shortNamePre,longNamePre);
			if (searchPre == null)
				searchPre = searchPre2;
			else
				searchPre = criteriaBuilder.or(searchPre, searchPre2);
		}
		if(!questionName.equalsIgnoreCase(""))
		{
			Predicate shortNamePre=criteriaBuilder.like(questionJoin.<String>get("questionShortName"),"%"+questionName+"%");
			Predicate namePre=criteriaBuilder.like(questionJoin.<String>get("questionText"),"%"+questionName+"%");
			searchPre3=criteriaBuilder.or(shortNamePre,namePre);
			if (searchPre == null)
				searchPre = searchPre3;
			else
				searchPre = criteriaBuilder.or(searchPre, searchPre3);
		}
		
		//past assessment
		Predicate pre5=criteriaBuilder.lessThan(from.get("assesment").<Date>get("dateOfAssesment"), criteriaBuilder.currentDate());
		
		//Not Assigned in current Assesment		
		Set<AssesmentQuestion> assignedassesmentQuestion=Assesment.findAssesment(assesmentId).getAssesmentQuestions();
		
		List<Question> assignedQuestions=new ArrayList<Question>();
		for(AssesmentQuestion aq:assignedassesmentQuestion)
		{
			assignedQuestions.add(aq.getQuestion());
		}
		
		Predicate pre7=null;
		if(assignedQuestions.size()>0)
			pre7=criteriaBuilder.not(from.get("question").in(assignedQuestions));
		
		//institution filter
		Predicate pre6 = criteriaBuilder.equal(from.get("question").get("questEvent").get("institution"), institution);
		
		Assesment a=Assesment.findAssesment(assesmentId);
		
        if(userLoggedIn.getIsAdmin()) //Main Admin
        {
        	log.info("Main Admin");
    		
			Predicate pre;
    		if(pre7==null)
    		{
    			if(searchPre==null)
    				pre=criteriaBuilder.and(pre3,pre4,pre5,pre6);
    			else
    				pre=criteriaBuilder.and(pre3,pre4,pre5,pre6,searchPre);    			
    		}
    		else
    		{	
    			if(searchPre==null)
    				pre=criteriaBuilder.and(pre3,pre4,pre5,pre6,pre7);
        		else
        			pre=criteriaBuilder.and(pre3,pre4,pre5,pre6,pre7,searchPre);        			 
    		}
    		
    		Predicate advPredicate = findAssesmentQuestionByAdvancedSearchCriteria(criteriaStringList, criteriaBuilder, criteriaQuery, from, pre);
    		
    		select.where(advPredicate);
    		
    		TypedQuery<AssesmentQuestion> q=entityManager().createQuery(criteriaQuery);
    		List<AssesmentQuestion> pastAqs=q.getResultList();
    		
    		//question should belong to assesment mcq
    		List<AssesmentQuestion> aqList=new ArrayList<AssesmentQuestion>();
    		
    		//filter it by question type and question event specified in assesment module for examiner/ author
    		List<QuestionEvent> questionEventList=QuestionSumPerPerson.findQuestionEventOfExaminer(a, author);    		
    		List<QuestionType> questionTypeList=QuestionTypeCountPerExam.findQuestionTypePerExam(a);
    		
    		for(AssesmentQuestion aq:pastAqs)
    		{
    			if(aq.getQuestion().getMcs().contains(mc) && questionEventList.contains(aq.getQuestion().getQuestEvent()) && questionTypeList.contains(aq.getQuestion().getQuestionType()))
    			{
    				aqList.add(aq);
    			}
    		}
    		
    		return aqList;
        }
        else
        {
        	log.info("Not Main Admin");
        	//Author
			Predicate p1 = criteriaBuilder.equal(from.get("autor"), userLoggedIn);
			
			//institution filter
			Predicate mainpre1 = criteriaBuilder.equal(from.get("question").get("questEvent").get("institution"), institution);
			
			//Author and institution filter
			mainpre1 = criteriaBuilder.and( p1, mainpre1);
			
			//Question Access
			Subquery<UserAccessRights> subQry = criteriaQuery.subquery(UserAccessRights.class);
			Root queAccRoot = subQry.from(UserAccessRights.class);
			subQry.select(queAccRoot.get("question").get("id")).where(criteriaBuilder.equal(queAccRoot.get("person"), userLoggedIn));
			Predicate mainpre2 = criteriaBuilder.in(from.get("question").get("id")).value(subQry);
			
			//Event Access
			Subquery<UserAccessRights> subQuery = criteriaQuery.subquery(UserAccessRights.class);
			Root questionAccessRoot = subQuery.from(UserAccessRights.class);
			subQuery.select(questionAccessRoot.get("questionEvent").get("id")).where(criteriaBuilder.equal(questionAccessRoot.get("person"),userLoggedIn));
			Predicate mainpre3 = criteriaBuilder.in(from.get("question").get("questEvent").get("id")).value(subQuery);
			
			//institutional Access
			Subquery<UserAccessRights> instSubQuery = criteriaQuery.subquery(UserAccessRights.class);
			Root instAccessRoot = instSubQuery.from(UserAccessRights.class);
			Predicate instP1 = criteriaBuilder.equal(instAccessRoot.get("person"), userLoggedIn);
			Predicate instP2 = criteriaBuilder.equal(instAccessRoot.get("institution"), institution);
			instSubQuery.select(instAccessRoot.get("institution").get("id")).where(criteriaBuilder.and(instP1, instP2));
			Predicate mainpre4 = criteriaBuilder.in(from.get("question").get("questEvent").get("institution").get("id")).value(instSubQuery);
			
			//Author and institution filter or (Question Access / Event Access / institutional Access)
			Predicate andAdminPredicate = criteriaBuilder.or(mainpre1, criteriaBuilder.or(mainpre2, mainpre3, mainpre4));
			
			Predicate pre=null;
    		if(pre7==null)
    		{
    			if(searchPre==null)
    				pre = criteriaBuilder.and(pre3,pre4,pre5,pre6,andAdminPredicate);
    			else
    				pre = criteriaBuilder.and(pre3,pre4,pre5,pre6,andAdminPredicate,searchPre);
    		}
    		else
    		{	if(searchPre==null)
    				pre = criteriaBuilder.and(pre3,pre4,pre5,pre7,pre6,andAdminPredicate);
	    		else
	    			pre = criteriaBuilder.and(pre3,pre4,pre5,pre7,pre6,andAdminPredicate,searchPre);
    		}
    		
    		Predicate advPredicate = findAssesmentQuestionByAdvancedSearchCriteria(criteriaStringList, criteriaBuilder, criteriaQuery, from, pre);
    		
    		select.where(advPredicate);
    		
			TypedQuery<AssesmentQuestion> q = entityManager().createQuery(criteriaQuery);
			
			//remove duplicate question
    		List<AssesmentQuestion> pastAqs = q.getResultList();
    		
    		//question should belong to assesment mcq
    		List<AssesmentQuestion> aqList=new ArrayList<AssesmentQuestion>();
    		
    		//filter it by question type and question event specified in assesment module for examiner/ author
    		List<QuestionEvent> questionEventList=QuestionSumPerPerson.findQuestionEventOfExaminer(a, author);    		
    		List<QuestionType> questionTypeList=QuestionTypeCountPerExam.findQuestionTypePerExam(a);
    		
    		for(AssesmentQuestion aq:pastAqs)
    		{
    			if(aq.getQuestion().getMcs().contains(mc) && questionEventList.contains(aq.getQuestion().getQuestEvent()) && questionTypeList.contains(aq.getQuestion().getQuestionType()))
    			{
    				aqList.add(aq);
    			}
    		}
    		
    		return aqList;
        }        
    }*/
    
    public static List<AssesmentQuestion> findAssesmentQuestionsByMc(Long assesmentId, Long mcId, List<String> criteriaStringList, String questionId, String questionType, String questionName, Person author){
    	
    	log.info("Past Question Tab ");
    	try
    	{
    		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
    		CriteriaQuery<AssesmentQuestion> criteriaQuery = criteriaBuilder.createQuery(AssesmentQuestion.class);
    		Root<AnswerToAssQuestion> from = criteriaQuery.from(AnswerToAssQuestion.class);
    		Selection<AssesmentQuestion> assQueSelection = from.get("assesmentQuestion");
    		criteriaQuery.select(assQueSelection);
    		criteriaQuery.distinct(true);
            criteriaQuery.groupBy(from.get("assesmentQuestion").get("question").get("id"), from.get("answers").get("id"));
    		
    		Subquery<Long> cq = criteriaQuery.subquery(Long.class);
    		Root<AssesmentQuestion> assQuefrom = cq.from(AssesmentQuestion.class);
    		cq.distinct(true);
    		
    		Predicate pre1=criteriaBuilder.equal(assQuefrom.get("isAssQuestionAcceptedAdmin"), new Boolean(true));
    		Predicate pre2=criteriaBuilder.equal(assQuefrom.get("isForcedByAdmin"), new Boolean(true));
    		Predicate pre3=criteriaBuilder.or(pre1,pre2);		
    		Predicate pre4=criteriaBuilder.equal(assQuefrom.get("question").get("status"), Status.ACTIVE);
    		
    		if (author == null) //if author is null then it is examiner
    			author = Person.myGetLoggedPerson();
    		    		
    		Subquery<Long> queSumSubQuery = cq.subquery(Long.class);
    		Root<QuestionSumPerPerson> queSumRoot = queSumSubQuery.from(QuestionSumPerPerson.class);
    		Predicate subPre11 = criteriaBuilder.equal(queSumRoot.get("responsiblePerson").get("id"), author.getId());
    		Predicate subPre22 = criteriaBuilder.equal(queSumRoot.get("assesment").get("id"), assesmentId);
    		Expression<Long> questioneEventIdSelection = queSumRoot.get("questionEvent").get("id");
    		queSumSubQuery.select(questioneEventIdSelection).where(criteriaBuilder.and(subPre11, subPre22));
    		Predicate queSumPerPersonPredicate = criteriaBuilder.in(assQuefrom.get("question").get("questEvent").get("id")).value(queSumSubQuery);
    		
    		Subquery<Long> queTypeSubQuery = cq.subquery(Long.class);
    		Root<QuestionTypeCountPerExam> queTypeRoot = queTypeSubQuery.from(QuestionTypeCountPerExam.class);
    		Predicate queTypePre1 = criteriaBuilder.equal(queTypeRoot.get("assesment").get("id"), assesmentId);
    		SetJoin<QuestionTypeCountPerExam, QuestionType> questionTypeJoin = queTypeRoot.joinSet("questionTypesAssigned");
    		Expression<Long> questionTypeIdSelection = questionTypeJoin.get("id");
    		queTypeSubQuery.select(questionTypeIdSelection).where(queTypePre1);
    		Predicate queTypeCountPerExamPredicate = criteriaBuilder.in(assQuefrom.get("question").get("questionType").get("id")).value(queTypeSubQuery);
    		
    		Predicate searchPre1=null;
    		Predicate searchPre2=null;
    		Predicate searchPre3=null;
    		Predicate searchPre=null;
    		
    		if(!questionId.equalsIgnoreCase(""))
    		{
    			searchPre1=criteriaBuilder.equal(assQuefrom.get("question").get("id"), new Long(questionId));
    			searchPre = searchPre1;
    		}
    		if(!questionType.equalsIgnoreCase(""))
    		{
    			Predicate shortNamePre=criteriaBuilder.like(assQuefrom.get("question").get("questionType").<String>get("shortName"),"%"+questionType+"%");
    			Predicate longNamePre=criteriaBuilder.like(assQuefrom.get("question").get("questionType").<String>get("longName"),"%"+questionType+"%");
    			searchPre2=criteriaBuilder.or(shortNamePre,longNamePre);
    			if (searchPre == null)
    				searchPre = searchPre2;
    			else
    				searchPre = criteriaBuilder.or(searchPre, searchPre2);
    		}
    		if(!questionName.equalsIgnoreCase(""))
    		{
    			Predicate shortNamePre=criteriaBuilder.like(assQuefrom.get("question").<String>get("questionShortName"),"%"+questionName+"%");
    			Predicate namePre=criteriaBuilder.like(assQuefrom.get("question").<String>get("questionText"),"%"+questionName+"%");
    			searchPre3=criteriaBuilder.or(shortNamePre,namePre);
    			if (searchPre == null)
    				searchPre = searchPre3;
    			else
    				searchPre = criteriaBuilder.or(searchPre, searchPre3);
    		}
    		
    		//past assessment
    		Predicate pre5=criteriaBuilder.lessThan(assQuefrom.get("assesment").<Date>get("dateOfAssesment"), criteriaBuilder.currentDate());
    		
    		//Not Assigned in current Assesment		
    		Subquery<Assesment> assSubQuery = cq.subquery(Assesment.class);
    		Root assRoot = assSubQuery.from(Assesment.class);
    		Predicate subPre1 = criteriaBuilder.equal(assRoot.get("mc").get("id"), mcId);
    		Predicate subPre2 = criteriaBuilder.notEqual(assRoot.get("id"), assesmentId);
    		assSubQuery.select(assRoot.get("id")).where(criteriaBuilder.and(subPre1, subPre2));
    		
    		Predicate pre7 = criteriaBuilder.in(assQuefrom.get("assesment").get("id")).value(assSubQuery);
    	
    		Predicate p1 = criteriaBuilder.or(criteriaBuilder.equal(assQuefrom.get("question").get("autor").get("id"), author.getId()), criteriaBuilder.equal(assQuefrom.get("question").get("rewiewer").get("id"), author.getId()));
    			
			Predicate mainpre1 = criteriaBuilder.in(assQuefrom.get("question").get("questEvent").get("id")).value(queSumSubQuery);
			mainpre1 = criteriaBuilder.and( p1, mainpre1);
			
			//Question Access
			Subquery<UserAccessRights> subQry = cq.subquery(UserAccessRights.class);
			Root queAccRoot = subQry.from(UserAccessRights.class);
			Predicate quePre1 = criteriaBuilder.equal(queAccRoot.get("person"), author.getId());
			Predicate quePre2 = criteriaBuilder.in(queAccRoot.get("question").get("questEvent").get("id")).value(queSumSubQuery);
			subQry.select(queAccRoot.get("question").get("id")).where(criteriaBuilder.and(quePre1, quePre2));
			Predicate mainpre2 = criteriaBuilder.in(assQuefrom.get("question").get("id")).value(subQry);
			
			//Event Access
			Subquery<UserAccessRights> subQuery = cq.subquery(UserAccessRights.class);
			Root questionAccessRoot = subQuery.from(UserAccessRights.class);
			subQuery.select(questionAccessRoot.get("questionEvent").get("id")).where(criteriaBuilder.equal(questionAccessRoot.get("person"),author.getId()));
			Predicate mainpre3 = criteriaBuilder.in(assQuefrom.get("question").get("questEvent").get("id")).value(subQuery);
			
			//Author and institution filter or (Question Access / Event Access / institutional Access)
			Predicate andAdminPredicate = criteriaBuilder.or(mainpre1, criteriaBuilder.or(mainpre2, mainpre3));
			
			Predicate pre=null;
			
			if(searchPre==null)
				pre = criteriaBuilder.and(pre3,pre4,pre5,pre7,andAdminPredicate, queSumPerPersonPredicate, queTypeCountPerExamPredicate);
			else
				pre = criteriaBuilder.and(pre3,pre4,pre5,pre7,andAdminPredicate,searchPre, queSumPerPersonPredicate, queTypeCountPerExamPredicate);
    		
    		Predicate advPredicate = findAssesmentQuestionByAdvancedSearchCriteria(criteriaStringList, criteriaBuilder, criteriaQuery, assQuefrom, pre);
    		Expression<Long> assQueId = assQuefrom.get("id");        		
    		cq.select(assQueId).where(advPredicate);
    		
    		Predicate answerPredicate = criteriaBuilder.in(from.get("assesmentQuestion").get("id")).value(cq);
    		criteriaQuery.where(answerPredicate);
    		
			TypedQuery<AssesmentQuestion> q = entityManager().createQuery(criteriaQuery);
			
    		return q.getResultList();                    
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	
        return null;
    }
    
    /**
     * Left side view of Assesment Question
     * @param id
     * @return
     * 
     * for admin / institutional admin : Author is selected by admin from author pull down.
     * 
     * for examiner : author argument is null. Show assessment question where autor=user logged in.
     * retrieve all assesment question except proposed. Because proposed assesment question will be shown in proposed tab.
     */
    public static List<AssesmentQuestion> findAssesmentQuestionsByAssesment(Long id,Person author){
//        Boolean isAcceptedAdmin = true;
//        Boolean isActive = true;
        Assesment assesment = Assesment.findAssesment(id);
        
        //get user who is logged in
        Person userLoggedIn=Person.myGetLoggedPerson();
        
        PersonAccessRight accessRights=Person.fetchLoggedPersonAccessRights();
        
        //get institution
		Institution institution = Institution.myGetInstitutionToWorkWith();
        
        if (assesment == null) throw new IllegalArgumentException("The assesment argument is required");
        EntityManager em = Question.entityManager();
        
        String query="SELECT assesmentauestion FROM AssesmentQuestion AS assesmentauestion " +
        		"WHERE assesmentauestion.assesment = :assesment    and assesmentauestion.question.questEvent.institution=:institution ";
        if(!(accessRights.getIsAdmin() || accessRights.getIsInstitutionalAdmin())) //examiner
        {
        	query=query+" and (assesmentauestion.isAssQuestionAdminProposal=false Or assesmentauestion.isForcedByAdmin=true Or assesmentauestion.isAssQuestionAcceptedAutor=true Or assesmentauestion.isAssQuestionAcceptedAdmin=true)  and assesmentauestion.autor=:author ";
        }
        else if(author!=null) // for admin / institutional admin
        {
        	query=query+"and autor=:author";
        }
        
       
        
        TypedQuery<AssesmentQuestion> q = em.createQuery(query, AssesmentQuestion.class);
        
//        q.setParameter("isAcceptedAdmin", isAcceptedAdmin);
//        q.setParameter("isActive", isActive);

        q.setParameter("assesment", assesment);
        q.setParameter("institution", institution);
      
        if(!(accessRights.getIsAdmin() || accessRights.getIsInstitutionalAdmin())) //examiner
        {
        	
            q.setParameter("author", userLoggedIn);
        }
        else if(author!=null)
        {
        	q.setParameter("author", author); // selected author by admin / institutional admin
        }
        return q.getResultList();
    }
    
    public static List<AssesmentQuestion> findAssesmentQuestionsByAssesmentAndPerson(Long id,Person author){
      Assesment assesment = Assesment.findAssesment(id);
      
      if (assesment == null) throw new IllegalArgumentException("The assesment argument is required");

      EntityManager em = Question.entityManager();
      
      String query="SELECT assesmentauestion FROM AssesmentQuestion AS assesmentauestion " +
      		"WHERE assesmentauestion.assesment = :assesment and (assesmentauestion.isAssQuestionAdminProposal=false Or assesmentauestion.isForcedByAdmin=true Or assesmentauestion.isAssQuestionAcceptedAutor=true Or assesmentauestion.isAssQuestionAcceptedAdmin=true)  and assesmentauestion.autor=:author ";
       
      TypedQuery<AssesmentQuestion> q = em.createQuery(query, AssesmentQuestion.class);
      q.setParameter("assesment", assesment);
      q.setParameter("author", author); 
      return q.getResultList();
  }
    
    /*
     * Proposed Question Tab only for examiner
     * 
     * Logic
     * 1. Assessment Question with (status proposed) 
     * 2. An examiner may see only questions of specialization 	where he has access.	
     * */
    public static List<AssesmentQuestion> findAssesmentQuestionsByMcProposal(Long assesmentId, List<String> encodedStringList, String questionId, String questionType, String questionName){
        /*Boolean isAssQuestionAdminProposal = true;
       
        Assesment assesment = Assesment.findAssesment(assesmentId);
        Person userLoggedIn=Person.myGetLoggedPerson();
        
        //get institution
		Institution institution = Institution.myGetInstitutionToWorkWith();
        
        if (assesment == null) throw new IllegalArgumentException("The mcs argument is required");
        EntityManager em = Question.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT assesmentauestion FROM AssesmentQuestion AS assesmentauestion " +
        		"left JOIN assesmentauestion.question AS quest WHERE assesmentauestion.isAssQuestionAdminProposal = :isAssQuestionAdminProposal  AND  assesmentauestion.assesment=:assesment and assesmentauestion.autor=:autor and quest.questEvent.institution=:institution ");
        
        
        List<String> searchList=new ArrayList<String>();
        if(!questionId.equalsIgnoreCase(""))
        {
        	searchList.add(" assesmentauestion.question.id=:questionId ");
        }
        
        if(!questionName.equalsIgnoreCase(""))
        {
        	searchList.add(" ( assesmentauestion.question.questionShortName like '%"+questionName+"%' or assesmentauestion.question.questionText like '%"+questionName+"%' ) ");
        }
        if(!questionType.equalsIgnoreCase(""))
        {
        	searchList.add(" ( assesmentauestion.question.questionType.shortName like '%"+questionType+"%' or assesmentauestion.question.questionType.longName like '%"+questionType+"%' ) ");
        }
        StringBuilder searchstr=new StringBuilder();
        
        if(searchList.size()==1)
        {
        	queryBuilder.append(" and "+searchList.get(0));
        }
        else
        {
        	
        	if(searchList.size()>0)
        	{
        		searchstr.append(" and (");
        	}
        	
        	for(int i=0;i<searchList.size();i++)
        	{
        		String search=searchList.get(i);
        		
        		
        		if(i==searchList.size()-1)
        		{
        			searchstr.append(search);
        		}
        		else
        		{
        			searchstr.append(search +" or ");
        		}
        	}
        	
        	if(searchList.size()>0)
        	{
        		searchstr.append(" ) ");
        		queryBuilder.append(searchstr);
        		
        	}
        }
        
        
        TypedQuery<AssesmentQuestion> q = em.createQuery(queryBuilder.toString(), AssesmentQuestion.class);
        q.setParameter("isAssQuestionAdminProposal", isAssQuestionAdminProposal);
       // q.setParameter("status", Status.ACTIVE);
        q.setParameter("autor", userLoggedIn);
        q.setParameter("assesment", assesment);
        q.setParameter("institution", institution);
        if(!questionId.equalsIgnoreCase(""))
        {
        	 q.setParameter("questionId", new Long(questionId));
        	
        }
        
        if(!questionName.equalsIgnoreCase(""))
        {
        	 q.setParameter("questionName", questionName);
        	 q.setParameter("questionName1", questionName);
        	
        }
        if(!questionType.equalsIgnoreCase(""))
        {
        	 q.setParameter("questionType", questionType);
        	 q.setParameter("questionType1", questionType);
        	
        }
        
        return q.getResultList();*/
    	
    	Boolean isAssQuestionAdminProposal = true;
    	Assesment assesment = Assesment.findAssesment(assesmentId);
    	Person userLoggedIn=Person.myGetLoggedPerson();
         //get institution
 		Institution institution = Institution.myGetInstitutionToWorkWith();
 		
 		if (userLoggedIn == null || institution == null)
 			return new ArrayList<AssesmentQuestion>();
 		
 		if (assesment == null) throw new IllegalArgumentException("The mcs argument is required");
 		
 		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
 		CriteriaQuery<AssesmentQuestion> criteriaQuery = criteriaBuilder.createQuery(AssesmentQuestion.class);
 		Root<AssesmentQuestion> from = criteriaQuery.from(AssesmentQuestion.class);
 		criteriaQuery.distinct(true);
 		
 		Expression<Boolean> queProExpression = from.get("isAssQuestionAdminProposal");
 		Predicate userPredicate = criteriaBuilder.equal(from.get("autor").get("id"), userLoggedIn.getId());
 		Predicate institutePredicate = criteriaBuilder.equal(from.get("assesment").get("id"), assesmentId);
 		Predicate quePropsalPredicate = criteriaBuilder.equal(queProExpression, isAssQuestionAdminProposal);
 		Predicate mainPredicate = criteriaBuilder.and(userPredicate, institutePredicate, quePropsalPredicate);
 		Predicate orPredicate = null;
 		
 		if(!questionId.equalsIgnoreCase(""))
 		{
 			Long queId = Long.parseLong(questionId);
 			Predicate predicate = criteriaBuilder.equal(from.get("question").get("id"), queId);
 			if (orPredicate == null)
 				orPredicate = predicate;
 			else
 				orPredicate = criteriaBuilder.or(orPredicate, predicate);
 		}
         
 		if(!questionName.equalsIgnoreCase(""))
 		{
 			Expression<String> questionShortNameExp = from.get("question").get("questionShortName");
 			Expression<String> questionTextExp = from.get("question").get("questionText");
 			Expression<String> questionExp = criteriaBuilder.concat(questionShortNameExp, questionTextExp);
        	 
 			Predicate predicate = criteriaBuilder.like(questionExp, "%" + questionName + "%");
 			
 			if (orPredicate == null)
 				orPredicate = predicate;
 			else
 				orPredicate = criteriaBuilder.or(orPredicate, predicate);
 		}
 		if(!questionType.equalsIgnoreCase(""))
 		{
 			Expression<String> queTypeShortNameExp = from.get("question").get("questionType").get("shortName");
 			Expression<String> queTypeLongNameExp = from.get("question").get("questionType").get("longName");
 			Expression<String> queTypeExp = criteriaBuilder.concat(queTypeShortNameExp, queTypeLongNameExp);
        	 
 			Predicate predicate = criteriaBuilder.like(queTypeExp, "%" + questionType + "%");
 			if (orPredicate == null)
 				orPredicate = predicate;
 			else
 				orPredicate = criteriaBuilder.or(orPredicate, predicate);
 		}
 		
 		if (orPredicate != null)
 			mainPredicate = criteriaBuilder.and(mainPredicate, orPredicate);
 			
 		mainPredicate = findAssesmentQuestionByAdvancedSearchCriteria(encodedStringList, criteriaBuilder, criteriaQuery, from, mainPredicate);
 		criteriaQuery.where(mainPredicate);
 		
 		TypedQuery<AssesmentQuestion> query = entityManager().createQuery(criteriaQuery);
 		return query.getResultList();
    }
    
    public static List<AssesmentQuestion> findAssesmentQuestionsByMcProposalForSystemOverview(Long assesmentId, Long personId){
        Boolean isAssQuestionAdminProposal = true;
        Assesment assesment = Assesment.findAssesment(assesmentId);
       /* Person userLoggedIn=Person.myGetLoggedPerson();
        //get institution
		Institution institution = Institution.myGetInstitutionToWorkWith();
		
		if (userLoggedIn == null || institution == null)
			return new ArrayList<AssesmentQuestion>();*/
		
		if (assesment == null) throw new IllegalArgumentException("The mcs argument is required");
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<AssesmentQuestion> criteriaQuery = criteriaBuilder.createQuery(AssesmentQuestion.class);
		Root<AssesmentQuestion> from = criteriaQuery.from(AssesmentQuestion.class);
		
		Expression<Boolean> queProExpression = from.get("isAssQuestionAdminProposal");
		Predicate userPredicate = criteriaBuilder.equal(from.get("autor").get("id"), personId);
		Predicate institutePredicate = criteriaBuilder.equal(from.get("assesment").get("id"), assesmentId);
		Predicate quePropsalPredicate = criteriaBuilder.equal(queProExpression, isAssQuestionAdminProposal);
		
		criteriaQuery.where(criteriaBuilder.and(userPredicate, institutePredicate, quePropsalPredicate));
		
		TypedQuery<AssesmentQuestion> query = entityManager().createQuery(criteriaQuery);
		return query.getResultList();
	}
    
    /**
     * This medthod is common to past and proposed tab
     * 
     * @param assementQuestionId
     * @param assementId
     * @param selectedAuthor
     * @return
     */
    public static AssesmentQuestion copyAssesmentQuestion(Long assementQuestionId, Long assementId,Person selectedAuthor){
    	//EntityManager em = entityManager();
    	//em.getTransaction().begin();
    	
    	
    	AssesmentQuestion assesmentQuestionToCopyFrom = AssesmentQuestion.findAssesmentQuestion(assementQuestionId);
    	
    	
    	
    	if(!assesmentQuestionToCopyFrom.getIsAssQuestionAdminProposal()) //past Question tab : copy assesment question and change status to proposed if admin
    	{
    		
    	
    	Assesment assesment = Assesment.findAssesment(assementId);
    	AssesmentQuestion assesmentQuestionNew = new AssesmentQuestion();
    	
    	Set<AnswerToAssQuestion> answersToAssementToCopy = assesmentQuestionToCopyFrom.getAnswersToAssQuestion();
    	//Set<AnswerToAssQuestion> answersToAssementCopied = new HashSet<AnswerToAssQuestion>();
    	
    	/*Iterator<AnswerToAssQuestion> iter = answersToAssementToCopy.iterator();
    	while (iter.hasNext()){
    		AnswerToAssQuestion oldOne = iter.next();
    		AnswerToAssQuestion newOne = new AnswerToAssQuestion();
    		newOne.setAnswers(oldOne.getAnswers());
    		newOne.setAssesmentQuestion(assesmentQuestionNew);
    		newOne.setSortOrder(oldOne.getSortOrder());
    		newOne.persist();
    		//answersToAssementCopied.add(newOne);
    	}*/
    	//assesmentQuestionNew.setAnswersToAssQuestion(answersToAssementCopied);
    	assesmentQuestionNew.setAssesment(assesment);
    	Person userLoggedIn=Person.myGetLoggedPerson();
    	if(selectedAuthor!=null)
    	{
    		assesmentQuestionNew.setAutor(selectedAuthor);
    	}
    	else
    		assesmentQuestionNew.setAutor(userLoggedIn);
    	assesmentQuestionNew.setDateAdded(new Date());
    	
    	
    	PersonAccessRight accessRights=Person.fetchLoggedPersonAccessRights();
    	
    	/*
    	 * if person is both admin and author
    	 * */
/*    	if(assesmentQuestionToCopyFrom.getAutor().equals(userLoggedIn) && (accessRights.getIsInstitutionalAdmin() || accessRights.getIsAdmin() ))
    	{
    		assesmentQuestionNew.setIsAssQuestionAcceptedAdmin(true);
        	assesmentQuestionNew.setIsAssQuestionAcceptedAutor(false);
        	assesmentQuestionNew.setIsAssQuestionAcceptedRewiever(false);
        	assesmentQuestionNew.setIsForcedByAdmin(false);
        	assesmentQuestionNew.setIsAssQuestionAdminProposal(false);
    	}
    	else*/ if((accessRights.getIsInstitutionalAdmin() || accessRights.getIsAdmin() )) //admin
    	{
    		assesmentQuestionNew.setIsAssQuestionAcceptedAdmin(false);
        	assesmentQuestionNew.setIsAssQuestionAcceptedAutor(false);
        	assesmentQuestionNew.setIsAssQuestionAcceptedRewiever(false);
        	assesmentQuestionNew.setIsForcedByAdmin(false);
        	assesmentQuestionNew.setIsAssQuestionAdminProposal(true);
    	}
    	/*else if(assesmentQuestionToCopyFrom.getIsAssQuestionAdminProposal()) //proposed question
    	{
    		assesmentQuestionNew.setIsAssQuestionAcceptedAdmin(true);
        	assesmentQuestionNew.setIsAssQuestionAcceptedAutor(false);
        	assesmentQuestionNew.setIsAssQuestionAcceptedRewiever(false);
        	assesmentQuestionNew.setIsForcedByAdmin(false);
        	assesmentQuestionNew.setIsAssQuestionAdminProposal(false);
    	}*/
    	else	//author / examiner
    	{
    		assesmentQuestionNew.setIsAssQuestionAcceptedAdmin(false);
        	assesmentQuestionNew.setIsAssQuestionAcceptedAutor(true);
        	assesmentQuestionNew.setIsAssQuestionAcceptedRewiever(false);
        	assesmentQuestionNew.setIsForcedByAdmin(false);
        	assesmentQuestionNew.setIsAssQuestionAdminProposal(false);
    	}
	    	assesmentQuestionNew.setQuestion(assesmentQuestionToCopyFrom.getQuestion());
	    	assesmentQuestionNew.setRewiewer(assesmentQuestionToCopyFrom.getRewiewer());
    	
    	assesmentQuestionNew.persist();
    	assesmentQuestionNew.flush();
    	
    	
    	Iterator<AnswerToAssQuestion> iter1 = answersToAssementToCopy.iterator();
    	while (iter1.hasNext()){
    		AnswerToAssQuestion oldOne = iter1.next();
    		AnswerToAssQuestion newOne = new AnswerToAssQuestion();
    		newOne.setAnswers(oldOne.getAnswers());
    		newOne.setAssesmentQuestion(assesmentQuestionNew);
    		newOne.setSortOrder(oldOne.getSortOrder());
    		newOne.persist();
    		//answersToAssementCopied.add(newOne);
    	}
    	
    	//em.getTransaction().commit();
    	
    	//entityManager().flush();
    	entityManager().refresh(assesmentQuestionNew);
    	
    	return assesmentQuestionNew;
    	}
    	else //proposed tab : change status from proposed to accept
    	{
    		assesmentQuestionToCopyFrom.setIsAssQuestionAcceptedAdmin(true);
    		assesmentQuestionToCopyFrom.setIsAssQuestionAcceptedAutor(false);
    		assesmentQuestionToCopyFrom.setIsAssQuestionAcceptedRewiever(false);
    		assesmentQuestionToCopyFrom.setIsForcedByAdmin(false);
    		assesmentQuestionToCopyFrom.setIsAssQuestionAdminProposal(false);
    		assesmentQuestionToCopyFrom.persist();
    		assesmentQuestionToCopyFrom.flush();
    		entityManager().refresh(assesmentQuestionToCopyFrom);
    		
    		return assesmentQuestionToCopyFrom;
    	}
    }
    
    
    public static AssesmentQuestion findAssesmentQuestionByAssesmentAndQuestion(Long questionId, Long assesmentId){
        Assesment assesment = Assesment.findAssesment(assesmentId);
        Question question = Question.findQuestion(questionId);
        if (assesment == null) throw new IllegalArgumentException("The assesment argument is required");
        if (question == null) throw new IllegalArgumentException("The question argument is required");
        EntityManager em = AssesmentQuestion.entityManager();
        
        TypedQuery<AssesmentQuestion> q = em.createQuery("SELECT assesmentauestion FROM AssesmentQuestion AS assesmentauestion " +
        		"WHERE assesmentauestion.assesment = :assesment AND assesmentauestion.question = :question", AssesmentQuestion.class);
//        q.setParameter("isAcceptedAdmin", isAcceptedAdmin);
//        q.setParameter("isActive", isActive);

        q.setParameter("assesment", assesment);
        q.setParameter("question", question);
        
        return q.getSingleResult();
	}
    
    
	public static List<AssesmentQuestion> findAssesmentQuestionsByQuestionEvent(java.lang.Long questEventId, java.lang.Long assesmentId) {
		QuestionEvent questionEvent = QuestionEvent.findQuestionEvent(questEventId);
		Assesment assesment = Assesment.findAssesment(assesmentId);
        if (questionEvent == null) throw new IllegalArgumentException("The QuestionEvent argument is required");
        if (assesment == null) throw new IllegalArgumentException("The Assesment argument is required");
        EntityManager em = AssesmentQuestion.entityManager();
        TypedQuery<AssesmentQuestion> q = em.createQuery("SELECT a FROM AssesmentQuestion a " + 
        		"INNER JOIN a.question quest WHERE quest.questEvent = :questionEvent AND a.assesment = :assesment ORDER BY a.orderAversion", AssesmentQuestion.class);
        q.setParameter("questionEvent", questionEvent);
        q.setParameter("assesment", assesment);
        return q.getResultList();
    }
	
	public static List<AssesmentQuestion> findAssesmentQuestionsByQuestionEventAssIdQuestType(java.lang.Long questEventId, java.lang.Long assesmentId,  List<Long> questionTypesId, final boolean isVersionA, final boolean printAllQuestions) {
		/*QuestionEvent questionEvent = QuestionEvent.findQuestionEvent(questEventId);
		Assesment assesment = Assesment.findAssesment(assesmentId);
		
		Iterator<Long> iter = questionTypesId.iterator();
		List<QuestionType> questionType = new ArrayList<QuestionType>();
		while (iter.hasNext()) {
			Long long1 = (Long) iter.next();
			questionType.add(QuestionType.findQuestionType(long1));
		}
		
		//QuestionType questionType = QuestionType.findQuestionType(questionTypeId);
        if (questionEvent == null) throw new IllegalArgumentException("The QuestionEvent argument is required");
        if (assesment == null) throw new IllegalArgumentException("The Assesment argument is required");
        //if (questionType == null) throw new IllegalArgumentException("The QuestionType argument is required");
        EntityManager em = AssesmentQuestion.entityManager();
        
        StringBuilder queryBuilder = new StringBuilder("SELECT a FROM AssesmentQuestion a " + 
        		"INNER JOIN a.question quest WHERE quest.questEvent = :questionEvent AND a.assesment = :assesment AND ( ");
        
        Iterator<QuestionType> iterQuestType = questionType.iterator();
        int firstTime = 0;
        log.error(questionType.size());
        while (iterQuestType.hasNext()) {
			QuestionType questionType2 = (QuestionType) iterQuestType.next();
			if(firstTime>0){
				queryBuilder.append(" OR ");
			}
			queryBuilder.append(" quest.questionType=:questiontype" + questionType2.getId());
			firstTime++;
		} 
        
        
        
        
        queryBuilder.append(  " )) ORDER BY a.orderAversion");
  
        
        
        TypedQuery<AssesmentQuestion> q = em.createQuery( queryBuilder.toString(), AssesmentQuestion.class);
       
        
        log.error(questionType.size());
        log.error(queryBuilder.toString());
        Iterator<QuestionType> iterQuestType2 = questionType.iterator();
        while (iterQuestType2.hasNext()) {
			QuestionType questionType2 = (QuestionType) iterQuestType2.next();
			q.setParameter("questiontype"+questionType2.getId(), questionType2);
		}
        
        q.setParameter("questionEvent", questionEvent);
        q.setParameter("assesment", assesment);
        //q.setParameter("questionType", questionType);
        */
        
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery<AssesmentQuestion> criteriaQuery = criteriaBuilder.createQuery(AssesmentQuestion.class);
        Root<AssesmentQuestion> from = criteriaQuery.from(AssesmentQuestion.class);
        
        Predicate predicateQuestionEvent = criteriaBuilder.equal(from.get("question").get("questEvent").get("id"), questEventId);		
        Predicate predicateAssessment = criteriaBuilder.equal(from.get("assesment").get("id"), assesmentId);
        Predicate predicateQuestionTypeIn = from.get("question").get("questionType").get("id").in(questionTypesId);
        //accepted
        Predicate predicateAccepted=criteriaBuilder.equal(from.get("isAssQuestionAcceptedAdmin"), new Boolean(true));
        
  		//force accepted
  		Predicate predicateForcedAccepted=criteriaBuilder.equal(from.get("isForcedByAdmin"), new Boolean(true));
  		Predicate predicateAcceptedOr;
  		if(printAllQuestions == true) {
  	        //non accepted
  	        Predicate predicateNonAccepted=criteriaBuilder.equal(from.get("isAssQuestionAcceptedAdmin"), Boolean.FALSE);
  	        
  	  		//non force accepted
  	  		Predicate predicateNonForcedAccepted=criteriaBuilder.equal(from.get("isForcedByAdmin"), Boolean.FALSE);

  	  		predicateAcceptedOr = criteriaBuilder.or(predicateAccepted,predicateForcedAccepted,predicateNonAccepted,predicateNonForcedAccepted);
  		}else {
  			//accepted / force accepted
  	  		predicateAcceptedOr = criteriaBuilder.or(predicateAccepted,predicateForcedAccepted);	
  		}
        
        if(isVersionA == true) {
        	criteriaQuery.orderBy(criteriaBuilder.asc(from.get("orderAversion")));	
        }else {
        	criteriaQuery.orderBy(criteriaBuilder.asc(from.get("orderBversion")));
        }
        
        criteriaQuery.where(criteriaBuilder.and(predicateQuestionEvent,predicateAssessment,predicateQuestionTypeIn,predicateAcceptedOr));
        
        TypedQuery<AssesmentQuestion> q = entityManager().createQuery(criteriaQuery);      
        
        return q.getResultList();
    }
	
	
	public static List<AssesmentQuestion> findAssementQuestionForAssementBook(Long assesmentId){
		Assesment assesment = Assesment.findAssesment(assesmentId);
        
        if (assesment == null) throw new IllegalArgumentException("The Assesment argument is required");
        EntityManager em = AssesmentQuestion.entityManager();
        TypedQuery<AssesmentQuestion> q = em.createQuery("SELECT assquest FROM AssesmentQuestion assquest " + 
        		"INNER JOIN assquest.question quest WHERE assquest.assesment = :assesment ORDER BY assquest.orderAversion", AssesmentQuestion.class);

        q.setParameter("assesment", assesment);
        return q.getResultList();
	}

	public static List<AssesmentQuestion> findAssesmentQuestionsByQuestion(Long questionId) {
		
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<AssesmentQuestion> cq = cb.createQuery(AssesmentQuestion.class);
		Root<AssesmentQuestion> from = cq.from(AssesmentQuestion.class);
		Predicate pre = cb.equal(from.get("question").get("id"), questionId);
		Expression<Date> closeDateExp = from.get("assesment").get("dateClosed");
		Date currDate = new Date();
		Predicate pre2 = cb.greaterThan(closeDateExp, currDate);
		cq.where(cb.and(pre, pre2));		
		TypedQuery<AssesmentQuestion> query = entityManager().createQuery(cq);
		return query.getResultList();
	}
	
	/*load mail template*/
	public static String loadTemplate()
	{
		String filePath=RequestFactoryServlet.getThreadLocalServletContext().getRealPath(SharedConstant.MAIL_TEMPLATE);
		File file=new File(filePath);
		try{
		return FileUtils.readFileToString(file);
		}catch(IOException e)
		{
			return "";
		}
	}
	
	// load system overview mail template
	public static String loadSystemOverviewTemplate()
	{
		String filePath=RequestFactoryServlet.getThreadLocalServletContext().getRealPath(SharedConstant.SYSTEM_OVERVIEW_MAIL_TEMPLATE);
		File file=new File(filePath);
		try{
			return FileUtils.readFileToString(file);
		}catch(IOException e)
		{
			return "";
		}
	}
	
	/*Send Mail*/
	public static Boolean sendMail(List<Person> toExaminerList, String messageContent,	String mailSubject, Assesment assesment)
	{
		try
		{
			Person userLoggedIn=Person.myGetLoggedPerson();
			String fromAddress=userLoggedIn.getEmail();
			String toAddresses[]=new String[toExaminerList.size()];
			
			SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MMM-yy");
			
			
			messageContent=messageContent.replace("[fromName]", userLoggedIn.getPrename() +" "+userLoggedIn.getName());
			messageContent=messageContent.replace("[assesmentName]", assesment.getName());
			messageContent=messageContent.replace("[assesmentStartDate]", dateFormat.format(assesment.getDateOpen()));
			messageContent=messageContent.replace("[assesmentClosedDate]", dateFormat.format(assesment.getDateClosed()));
			messageContent=messageContent.replace("[assesmentMC]", assesment.getMc().getMcName());
			
			EmailServiceImpl emailService=new EmailServiceImpl();
			WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(RequestFactoryServlet.getThreadLocalServletContext());
			((EmailServiceImpl)emailService).setSender(applicationContext.getBean(JavaMailSenderImpl.class));
			
			for(int i=0;i<toExaminerList.size();i++)
			{
				
				
				
				Person examiner=toExaminerList.get(i);
				toAddresses[i]=examiner.getEmail();
				String newMessage=messageContent.replace("[toName]", examiner.getPrename()+" "+examiner.getName());
				Integer assesmentQuestionProposedCount=findAssesmentQuestionsByMcProposal(assesment.getId(), new ArrayList<String>(), "", "", "").size();
				
				newMessage=newMessage.replace("[assesmentQuestionProposedCount]", assesmentQuestionProposedCount.toString());
				List<QuestionSumPerPerson> questionSumPerPersonList=QuestionSumPerPerson.findPercentageOfQuestionAssignedToExaminer(assesment, examiner);
				List<QuestionTypeCountPerExam> questionTypeCountPerExamList=assesment.getQuestionTypeCountPerExams();
				StringBuilder totalCountString=new StringBuilder();
				StringBuilder totalRemainingString=new StringBuilder();
				
				totalCountString.append("<table>");
				totalRemainingString.append("<table>");
				for(QuestionTypeCountPerExam questionTypeCountPerExam:questionTypeCountPerExamList)
				{
					for(QuestionSumPerPerson questionSumPerPerson:questionSumPerPersonList)
					{
						
						

						
						
						String questionTypeStr="";
						
						Set<QuestionType> questionTypes=questionTypeCountPerExam.getQuestionTypesAssigned();
						int k=0;
						for(QuestionType questionType:questionTypes)
						{
							if(k==0)
							questionTypeStr=questionTypeStr+questionType.getShortName();
							else
							{
								questionTypeStr=questionTypeStr+","+questionType.getShortName();

							}
							
						
							k++;	
						}
						
						
						questionTypeStr=questionTypeStr+"("+questionSumPerPerson.getQuestionEvent().getEventName()+")";
						
						int questionTypeCount=questionTypeCountPerExam.getQuestionTypeCount();
						int percentAllocated=questionSumPerPerson.getQuestionSum();
						Integer totalQuestionAllocated=(int)(questionTypeCount*percentAllocated)/100;
						Integer questionAssigned=-totalQuestionAllocated;
						
						List<AssesmentQuestion> assesmentQuestionList=AssesmentQuestion.findAssesmentQuestionsByAssesment(assesment.getId(), examiner);
						
						int count=0;
						for(int l=0;l<assesmentQuestionList.size();l++)
						{
							AssesmentQuestion question =assesmentQuestionList.get(l);
							if(questionTypes.contains(question.getQuestion().getQuestionType()) && question.getQuestion().getQuestEvent().equals(questionSumPerPerson.getQuestionEvent()))
							{						
								questionAssigned++;
								count++;
							}
						}
						totalCountString.append("<tr>");
						totalCountString.append("<td stype='vertical-align:middle'>");
						totalCountString.append(questionTypeStr);
						totalCountString.append(" : ");
						totalCountString.append("</td>");
						totalCountString.append("<td stype='vertical-align:middle'>");
						totalCountString.append(totalQuestionAllocated);
						totalCountString.append("</td>");
						totalCountString.append("</tr>");
						
						totalRemainingString.append("<tr>");
						totalRemainingString.append("<td stype='vertical-align:middle'>");
						totalRemainingString.append(questionTypeStr);
						totalRemainingString.append(" : ");
						totalRemainingString.append("</td>");
						totalRemainingString.append("<td stype='vertical-align:middle'>");
						totalRemainingString.append(-questionAssigned);
						totalRemainingString.append("</td>");
						totalRemainingString.append("</tr>");
						
						
					/*	proxy.setQuestionSumPerPersonProxy(questionSumPerPersonProxy);
						proxy.setQuestionTypeCountPerExamProxy(questionTypeCountPerExamProxy);
						proxy.setCount(questionAssigned);
						proxy.setTotalQuestionAllocated(totalQuestionAllocated);
						proxy.setTotalQuestionAllowed(count);*/
						
						
						
					}
					newMessage=newMessage.replace("[LOOP]", "");
					newMessage=newMessage.replace("[totalCount]", "");
					newMessage=newMessage.replace("[END LOOP]", "");
					newMessage=newMessage.replace("[totalRemaining]", "");
					
				}
				totalCountString.append("</table>");
				totalRemainingString.append("</table>");
				newMessage=newMessage.replace("[[assesmentQuestionType]([assesmentSpecialization]) : [assesmentQuestionAllocatedCount]]", totalCountString.toString());
				newMessage=newMessage.replace("[[assesmentQuestionType]([assesmentSpecialization]) : [assesmentQuestionRemainingCount]]", totalRemainingString.toString());
				
				emailService.sendMail(new String[]{examiner.getEmail()}, fromAddress, mailSubject, newMessage);
			}
			
			return true;
		}
		catch(Exception e)
		{
			log.info("sendMail exception : " + e.getMessage());
			return false;
		}
	}
	
	/* 
	 * Used in New Question Tab 
	 * Used to exclude assessment Question for show New Question
	 * 
	public static Long countAssessmentQuestionByQuestionID(Question question)
	{
		Log.info("countAssessmentQuestionByQuestionID");
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<AssesmentQuestion> from = criteriaQuery.from(AssesmentQuestion.class);
		criteriaQuery.select(criteriaBuilder.count(from));	
		
		Predicate pre=criteriaBuilder.equal(from.get("question"), question.getId());
		criteriaQuery.where(pre);
		TypedQuery<Long> q = entityManager().createQuery(criteriaQuery);
		return q.getSingleResult();
	}*/
	
	@Override
    public boolean equals(Object obj) {
		
		if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        AssesmentQuestion aq=(AssesmentQuestion)obj;
        
        if(!aq.getQuestion().equals(this.getQuestion()))
        {
        	return false;
        }
        else
        {
        	Set<AnswerToAssQuestion> ans=aq.getAnswersToAssQuestion();
        	Set<AnswerToAssQuestion> ans1=this.getAnswersToAssQuestion();
        	
        	if(ans.size()==0 && ans1.size()==0)
        	{
        		return true;
        	}
        	
        	for(AnswerToAssQuestion a:ans)
        	{
        		for(AnswerToAssQuestion a1:ans1)
        		{
        			if(a.getAnswers().equals(a1.getAnswers()))
        			{
        				return true;
        			}
        		}
        	}
        }

		
		return false;
	}
	
	//remove duplicate question
			/*
			 *  from a question a lot of different assessment questions may be created. So if a user adds a question to assessment and the assessment_question is created, check 
			 *  if the question is already in assessment. If no, all is ok, if yes, check if one of the answers is the same. If no, it may be created, if yes, popup a message that the same question 
			 *  may not be placed twice.
			 * */
	public static List<AssesmentQuestion> removeDuplicateAssesmentQuestion(List<AssesmentQuestion> aqs )
	{
		for(int i=0;i<aqs.size();i++)
		{
			AssesmentQuestion a1=aqs.get(i);
			for(int j=i+1;j<aqs.size();j++)
			{
				AssesmentQuestion a2=aqs.get(j);
				
				if(a1.equals(a2))//remove duplicate here
				{
					aqs.remove(i);
					i=-1;
					break;
					
				}
				
			}
		}
		return aqs;
	}
	
	public static void shuffleQuestionsAnswers(Long assessmentID,Boolean disallowSorting) {
		
		if(assessmentID == null) {
			log.error("Error in assessment id");
			return;
		}
		
		Assesment assesment = Assesment.findAssesment(assessmentID);
		if(assesment.getDisallowSorting() != null && assesment.getDisallowSorting() == true) {
			log.error("Shuffle is not allowed");
			return;
		}
		
		List<QuestionTypeCountPerExam> countPerExams = QuestionTypeCountPerExam.findQuestionTypesCountSortedByAssesmentNonRoo(assessmentID);
		int index = 1;
		for (QuestionTypeCountPerExam questionTypeCountPerExam : countPerExams) {
			Set<QuestionType> questionTypes = questionTypeCountPerExam.getQuestionTypesAssigned();
			
			List<Long> questionTypeIds = Lists.newArrayList(FluentIterable.from(questionTypes).transform(getQuestionTypeToIdFunction()).iterator());
			
			List<QuestionEvent> questionEvents =  QuestionEvent.findAllQuestionEventsByQuestionTypeAndAssesmentID(assessmentID, questionTypeIds);
			
			for (QuestionEvent questionEvent : questionEvents) {
				List<AssesmentQuestion> assesmentQuestions = AssesmentQuestion.findAssesmentQuestionsByQuestionEventAssIdQuestType(questionEvent.getId(), assessmentID, questionTypeIds,true,false);
				
				for (AssesmentQuestion assesmentQuestion : assesmentQuestions) {
					log.info("Assessment question id " + assesmentQuestion.getId());
				}
				log.info("--------------------------------------------------------");
				Collections.shuffle(assesmentQuestions);
				int size = (index-1) + assesmentQuestions.size();
				int last = size;
				for (AssesmentQuestion assesmentQuestion : assesmentQuestions) {
					assesmentQuestion.setOrderAversion(index);
					assesmentQuestion.setOrderBversion(last);
					assesmentQuestion.persist();
					index++;
					last--;
				}
				for (AssesmentQuestion assesmentQuestion : assesmentQuestions) {
					log.info("Assessment question id " + assesmentQuestion.getId());
				}
					
				List<Integer> preTrueAnswerSequence = Lists.newArrayList();
				for (AssesmentQuestion assesmentQuestion : assesmentQuestions) {
					
					
					List<Integer> newtrueAnswerSequence = shuffleAnswer(assesmentQuestion);
					
					boolean flag = BMEUtils.compareTwoList(preTrueAnswerSequence,newtrueAnswerSequence);
					
					if(flag == true) {
						newtrueAnswerSequence = shuffleAnswer(assesmentQuestion);
					}
					
					preTrueAnswerSequence = newtrueAnswerSequence;
				}
			}
		}
		
		assesment.setDisallowSorting(disallowSorting);
		assesment.persist();
	}


	private static Function<QuestionType, Long> getQuestionTypeToIdFunction() {
		return new Function<QuestionType, Long>() {

			@Override
			public Long apply(QuestionType input) {
				return input.getId();
			}	
		};
	}

	private static List<Integer> shuffleAnswer(AssesmentQuestion assesmentQuestion ) {
		
		List<Integer> newtrueAnswerSequence = Lists.newArrayList();
		
		List<AnswerToAssQuestion>  answerToAssQuestions = AnswerToAssQuestion.findAnswerToAssQuestionByAssesmentQuestion(assesmentQuestion.getId());
		
		for (AnswerToAssQuestion answerToAssQuestion : answerToAssQuestions) {
			log.info("Answer Order : " + answerToAssQuestion.getId() + "Validity : " + answerToAssQuestion.getAnswers().getValidity());
		}
		
		Collections.shuffle(answerToAssQuestions);
		
		for (int i = 0; i < answerToAssQuestions.size(); i++) {
			if(Validity.Wahr.equals(answerToAssQuestions.get(i).getAnswers().getValidity())) {
				newtrueAnswerSequence.add(i);
			}
			
			answerToAssQuestions.get(i).setSortOrder(i);
			answerToAssQuestions.get(i).persist();
		}
		
		for (AnswerToAssQuestion answerToAssQuestion : answerToAssQuestions) {
			log.info("Answer Order : " + answerToAssQuestion.getId() + "Validity : " + answerToAssQuestion.getAnswers().getValidity());
		}
		return newtrueAnswerSequence;
	}
	
	private static Predicate findAssesmentQuestionByAdvancedSearchCriteria(List<String> criteriaStringList, CriteriaBuilder criteriaBuilder, CriteriaQuery<AssesmentQuestion> criteriaQuery, Root<AssesmentQuestion> from, Predicate mainPredicate)
	{
		try
		{
			List<AdvancedSearchCriteria> criteriaList = AdvancedSearchCriteriaUtils.decodeList(criteriaStringList);
			Predicate predicate = null;
			Predicate advPredicate = null;
			
			for (AdvancedSearchCriteria criteria : criteriaList)
			{
				if (PossibleFields.KEYWORD.equals(criteria.getPossibleFields()))
				{
					Join<AssesmentQuestion,Question> questionJoin = from.join("question");
					SetJoin<Question, Keyword> keywordSetJoin = questionJoin.joinSet("keywords", JoinType.LEFT);
					Expression<String> keywordTextExp = keywordSetJoin.get("name");
					predicate = likeComparison(criteriaBuilder, criteria, keywordTextExp);
				}
				else if (PossibleFields.QUESTION_EVENT.equals(criteria.getPossibleFields()))
				{
					Expression<String> queEventNameExp = from.get("question").get("questEvent").get("eventName");
					predicate = likeComparison(criteriaBuilder, criteria, queEventNameExp);
				}
				else if (PossibleFields.COMMENT.equals(criteria.getPossibleFields()))
				{
					Expression<String> commentExp = from.get("question").get("comment");
					predicate = likeComparison(criteriaBuilder, criteria, commentExp);
				}
				else if (PossibleFields.ANSWER_TEXT.equals(criteria.getPossibleFields()))
				{
					Subquery<Answer> subQuery = criteriaQuery.subquery(Answer.class);
					Root answerRoot = subQuery.from(Answer.class);
					Expression<String> answerTextPre = answerRoot.get("answerText");
					Predicate subPre1 = likeComparison(criteriaBuilder, criteria, answerTextPre);
					subQuery.select(answerRoot.get("question").get("id")).where(subPre1);
					
					predicate = criteriaBuilder.in(from.get("question").get("id")).value(subQuery);					
				}
				else if (PossibleFields.QUESTION_TEXT.equals(criteria.getPossibleFields()))
				{
					Expression<String> quesitonTextExp = from.get("question").get("questionText");
					predicate = likeComparison(criteriaBuilder, criteria, quesitonTextExp);
				}
				else if (PossibleFields.QUESTION_SHORT_NAME.equals(criteria.getPossibleFields()))
				{
					Expression<String> quesitonShortTextExp = from.get("question").get("questionShortName");
					predicate = likeComparison(criteriaBuilder, criteria, quesitonShortTextExp);				
				}
				else if (PossibleFields.CREATED_QUESTION_DATE.equals(criteria.getPossibleFields()))
				{
					Expression<Date> createdQueDateExp = from.get("question").get("dateAdded");
					predicate = dateComparison(criteriaBuilder, criteria, createdQueDateExp);
				}
				else if (PossibleFields.CHANGED_QUESTION_DATE.equals(criteria.getPossibleFields()))
				{
					Expression<Date> changedQueDateExp = from.get("question").get("dateChanged");
					predicate = dateComparison(criteriaBuilder, criteria, changedQueDateExp);
				}	
				else if (PossibleFields.USED_IN_MC_DATE.equals(criteria.getPossibleFields()))
				{
					Subquery<AssesmentQuestion> subQuery = criteriaQuery.subquery(AssesmentQuestion.class);
					Root assQueRoot = subQuery.from(AssesmentQuestion.class);
					Expression<Date> assessmentDateExp = assQueRoot.get("assesment").get("dateOfAssesment");
					Predicate subPre1 = dateComparison(criteriaBuilder, criteria, assessmentDateExp);					
					subQuery.select(assQueRoot.get("question").get("id")).where(subPre1);
					
					predicate = criteriaBuilder.in(from.get("question").get("id")).value(subQuery);
				}
				else if (PossibleFields.AUTHOR.equals(criteria.getPossibleFields()))
				{
					Expression<String> authorNameExp = from.get("question").get("autor").get("name");
					Expression<String> authorPreNameExp = from.get("question").get("autor").get("prename");
					Expression<String> authorFullNameExp = criteriaBuilder.concat(authorNameExp, authorPreNameExp);
					
					predicate = likeComparison(criteriaBuilder, criteria, authorFullNameExp);
					//predicate = userTypeComparison(criteriaBuilder, criteria, authorNameExp, authorPreNameExp);
				}
				else if (PossibleFields.REVIEWER.equals(criteria.getPossibleFields()))
				{
					Expression<String> reviewerNameExp = from.get("question").get("rewiewer").get("name");
					Expression<String> reviewerPreNameExp = from.get("question").get("rewiewer").get("prename");
					Expression<String> reviewerFullNameExp = criteriaBuilder.concat(reviewerNameExp, reviewerPreNameExp);
					
					predicate = likeComparison(criteriaBuilder, criteria, reviewerFullNameExp);
					//predicate = userTypeComparison(criteriaBuilder, criteria, reviewerNameExp, reviewerPreNameExp);
				}
				else if (PossibleFields.MEDIA_AVAILABILITY.equals(criteria.getPossibleFields()))
				{
					Subquery<QuestionResource> subQuery = criteriaQuery.subquery(QuestionResource.class);
					Root queResourceRoot = subQuery.from(QuestionResource.class);
					Predicate subPre1 = null;
					
					if (Comparison.EQUALS.equals(criteria.getComparison())) {
						subPre1 = criteriaBuilder.equal(queResourceRoot.get("type"), MultimediaType.valueOf(criteria.getValue()));
					} else if (Comparison.NOT_EQUALS.equals(criteria.getComparison()))
						subPre1 = criteriaBuilder.notEqual(queResourceRoot.get("type"), MultimediaType.valueOf(criteria.getValue()));
					
					if(subPre1 != null) {
						subQuery.select(queResourceRoot.get("question").get("id")).where(subPre1);
						
						predicate = criteriaBuilder.in(from.get("question").get("id")).value(subQuery);							
					}	
				}
				else if (PossibleFields.QUESTION_TYPE.equals(criteria.getPossibleFields()))
				{
					if (Comparison.EQUALS.equals(criteria.getComparison())) {
						predicate = criteriaBuilder.equal(from.get("question").get("questionType").get("questionType"), QuestionTypes.valueOf(criteria.getValue()));
					} else if (Comparison.NOT_EQUALS.equals(criteria.getComparison())) {
						predicate = criteriaBuilder.notEqual(from.get("question").get("questionType").get("questionType"), QuestionTypes.valueOf(criteria.getValue()));
					}
				}
				else if (PossibleFields.QUESTION_TYPE_NAME.equals(criteria.getPossibleFields()))
				{
					Expression<String> quesitonTypeShortTextExp = from.get("question").get("questionType").get("shortName");
					predicate = likeComparison(criteriaBuilder, criteria, quesitonTypeShortTextExp);	
				}
				
				if (BindType.AND.equals(criteria.getBindType()))
				{
					if (advPredicate == null)
						advPredicate = criteriaBuilder.and(predicate);
					else
						advPredicate = criteriaBuilder.and(advPredicate, predicate);
				}
				else if (BindType.OR.equals(criteria.getBindType()))
				{
					if (advPredicate == null)
						advPredicate = criteriaBuilder.or(predicate);
					else
						advPredicate = criteriaBuilder.or(advPredicate, predicate);
				}				
			}
			
			if (advPredicate != null)
				mainPredicate = criteriaBuilder.and(mainPredicate, advPredicate);
			
			return mainPredicate;
		}
		catch(Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return mainPredicate;
	}
	
	private static Predicate likeComparison(CriteriaBuilder criteriaBuilder, AdvancedSearchCriteria criteria, Expression<String> expression) {
		Predicate predicate = null;
		if (Comparison.EQUALS.equals(criteria.getComparison()))
			predicate = criteriaBuilder.like(expression, "%" + criteria.getValue() + "%");
		else if (Comparison.NOT_EQUALS.equals(criteria.getComparison()))
			predicate = criteriaBuilder.notLike(expression, "%" + criteria.getValue() + "%");
		return predicate;
	}
	
	private static Predicate dateComparison(CriteriaBuilder criteriaBuilder, AdvancedSearchCriteria criteria, Expression<Date> createdQueDateExp) {
		try
		{
			Predicate predicate = null;
			Date userEnterdDate = df.parse(criteria.getValue());
			
			if (Comparison.EQUALS.equals(criteria.getComparison()))
				predicate = criteriaBuilder.equal(createdQueDateExp, userEnterdDate);
			else if (Comparison.NOT_EQUALS.equals(criteria.getComparison()))
				predicate = criteriaBuilder.notEqual(createdQueDateExp, userEnterdDate);
			else if (Comparison.MORE.equals(criteria.getComparison()))
				predicate = criteriaBuilder.greaterThan(createdQueDateExp, userEnterdDate);
			else if (Comparison.LESS.equals(criteria.getComparison()))
				predicate = criteriaBuilder.lessThan(createdQueDateExp, userEnterdDate);
			
			return predicate;
		}
		catch(Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	public static List<AssesmentQuestion> findAssessmentQuestionByAssesmentAndAuthor(Long assessmentId, Long authorId)
	{
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<AssesmentQuestion> criteriaQuery = criteriaBuilder.createQuery(AssesmentQuestion.class);
		Root<AssesmentQuestion> from = criteriaQuery.from(AssesmentQuestion.class);
		
		Predicate pre1 = criteriaBuilder.equal(from.get("assesment").get("id"), assessmentId);
		Predicate pre2 = criteriaBuilder.equal(from.get("autor").get("id"), authorId);
		
		criteriaQuery.where(criteriaBuilder.and(pre1, pre2));
		
		TypedQuery<AssesmentQuestion> query = entityManager().createQuery(criteriaQuery);
		return query.getResultList();
	}

	public static AssesmentQuestion findAssesmentQuestionByQuestion(Question question, Assesment assesment, Person author, Person reviewer) {
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<AssesmentQuestion> criteriaQuery = criteriaBuilder.createQuery(AssesmentQuestion.class);
		Root<AssesmentQuestion> from = criteriaQuery.from(AssesmentQuestion.class);
		
		//Predicate pre1 = criteriaBuilder.equal(from.get("assesment").get("id"), assesment.getId());
		Predicate pre2 = criteriaBuilder.equal(from.get("autor").get("id"), author.getId());
		Predicate pre3 = criteriaBuilder.equal(from.get("rewiewer").get("id"), reviewer.getId());
		Predicate pre4 = criteriaBuilder.equal(from.get("question").get("id"), question.getId());
		
		criteriaQuery.where(criteriaBuilder.and(pre2, pre3, pre4));
		
		TypedQuery<AssesmentQuestion> query = entityManager().createQuery(criteriaQuery);
		List<AssesmentQuestion> resultList = query.getResultList();
		if(resultList != null && resultList.size() > 0) {
			return resultList.get(0);
		}
		return null;
	}
	
	public static List<AssesmentQuestion> findAllAssesmentForInstitution2() {
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<AssesmentQuestion> criteriaQuery = criteriaBuilder.createQuery(AssesmentQuestion.class);
		Root<AssesmentQuestion> from = criteriaQuery.from(AssesmentQuestion.class);
		
		Predicate pre1 = criteriaBuilder.equal(from.get("assesment").get("institution").get("id"), 2);
		
		
		criteriaQuery.where(pre1);
		
		TypedQuery<AssesmentQuestion> query = entityManager().createQuery(criteriaQuery);
		return query.getResultList();
		
	}
	
	public static Integer countAllAssesmentQuestionByQuestion(Long questionId) {
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");
		
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<AssesmentQuestion> from = cq.from(AssesmentQuestion.class);
		
		cq.select(cb.count(from));
		
		Predicate predicate = allAssessmnetQuestionByQuestionPredicate(questionId,cb, from);
		cq.where(predicate);
		
        TypedQuery<Long> q = entityManager().createQuery(cq);
        
        //System.out.println("Q1 : " + q.unwrap(Query.class).getQueryString());
        
        return q.getSingleResult().intValue();
	}

	private static Predicate allAssessmnetQuestionByQuestionPredicate(Long questionId, CriteriaBuilder cb, Root<AssesmentQuestion> from) {
		return cb.equal(from.get("question").get("id"), questionId);
	}


	public static List<AssesmentQuestion> findAllAssesmentQuestionByQuestion(Long questionId, int start, int length) {
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");
		
		
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<AssesmentQuestion> cq = cb.createQuery(AssesmentQuestion.class);
		Root<AssesmentQuestion> from = cq.from(AssesmentQuestion.class);
		cq.orderBy(cb.asc(from.get("assesment").get("dateOfAssesment")));
		Predicate predicate = allAssessmnetQuestionByQuestionPredicate(questionId,cb, from);
		cq.where(predicate);
        TypedQuery<AssesmentQuestion> q = entityManager().createQuery(cq);
        q.setFirstResult(start);
        q.setMaxResults(length);
        return q.getResultList();
	}

	public static List<AssesmentQuestion> findAllAssesmentQueByQueId(Long questionId)
	{
		/*Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");*/
		
		
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<AssesmentQuestion> cq = cb.createQuery(AssesmentQuestion.class);
		Root<AssesmentQuestion> from = cq.from(AssesmentQuestion.class);
		cq.orderBy(cb.asc(from.get("assesment").get("dateOfAssesment")));
		Predicate predicate = allAssessmnetQuestionByQuestionPredicate(questionId,cb, from);
		cq.where(predicate);
        TypedQuery<AssesmentQuestion> q = entityManager().createQuery(cq);
        return q.getResultList();
	}

	public static List<AssesmentQuestion> findAllAssesmentQuestionsForAssesment(Long assessmentId) {
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<AssesmentQuestion> cq = cb.createQuery(AssesmentQuestion.class);
		Root<AssesmentQuestion> from = cq.from(AssesmentQuestion.class);
		cq.orderBy(cb.asc(from.get("orderAversion")));
		Predicate predicate = cb.equal(from.get("assesment").get("id"), assessmentId);
		cq.where(predicate);
        TypedQuery<AssesmentQuestion> q = entityManager().createQuery(cq);
        return q.getResultList();
	}


	public static void checkIfShuffleQuestionsAnswersNeeded(Integer assessmentId,Boolean disallowSorting) {
		if(assessmentId == null) {
			return;
		}

		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<AssesmentQuestion> from = criteriaQuery.from(AssesmentQuestion.class);
        criteriaQuery.select(criteriaBuilder.count(from));
       
        Predicate predicate = findPredicateForAssementQuestionForAssementBookByQuestionTypeAndQuestionEvent(assessmentId.longValue(),criteriaBuilder,criteriaQuery,from);
        Predicate orderAVersionPredicate = criteriaBuilder.equal(from.get("orderAversion"), 0); 
        criteriaQuery.where(criteriaBuilder.and(predicate,orderAVersionPredicate));
        TypedQuery<Long> q = entityManager().createQuery(criteriaQuery);      
        
        
        if(q.getSingleResult() > 1) {
        	shuffleQuestionsAnswers(assessmentId.longValue(),disallowSorting);
        }
	}

	public static List<AssesmentQuestion> findAssementQuestionForAssementBookByQuestionTypeAndQuestionEvent(Long assessmentId) {
		
		if(assessmentId == null) {
			return Lists.newArrayList();
		}

		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery<AssesmentQuestion> criteriaQuery = criteriaBuilder.createQuery(AssesmentQuestion.class);
        Root<AssesmentQuestion> from = criteriaQuery.from(AssesmentQuestion.class);
        
        Predicate predicate = findPredicateForAssementQuestionForAssementBookByQuestionTypeAndQuestionEvent(assessmentId.longValue(),criteriaBuilder,criteriaQuery,from);
        criteriaQuery.where(predicate);
        TypedQuery<AssesmentQuestion> q = entityManager().createQuery(criteriaQuery);   
        return q.getResultList();
	}
	
	private static <T> Predicate findPredicateForAssementQuestionForAssementBookByQuestionTypeAndQuestionEvent(Long assessmentId, CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery, Root<AssesmentQuestion> from){
		List<QuestionTypeCountPerExam> countPerExams = QuestionTypeCountPerExam.findQuestionTypesCountSortedByAssesmentNonRoo(assessmentId.longValue());
		Set<Long> questionTypeIds = Sets.newHashSet();
		Set<Long> questionEventIds = Sets.newHashSet();
		for (QuestionTypeCountPerExam questionTypeCountPerExam : countPerExams) {
			Set<QuestionType> questionTypes = questionTypeCountPerExam.getQuestionTypesAssigned();
			List<Long> currentQuestionTypeIds = FluentIterable.from(questionTypes).transform(getQuestionTypeToIdFunction()).toImmutableList();
			List<QuestionEvent> questionEvents = QuestionEvent.findAllQuestionEventsByQuestionTypeAndAssesmentID(assessmentId, currentQuestionTypeIds);
			questionTypeIds.addAll(currentQuestionTypeIds);
			questionEventIds.addAll(FluentIterable.from(questionEvents).transform(getQuestionEventToIdFunction()).toImmutableList());  
		}
		
		Predicate predicateQuestionEvent = from.get("question").get("questEvent").get("id").in(questionEventIds);		
        Predicate predicateAssessment = criteriaBuilder.equal(from.get("assesment").get("id"), assessmentId.longValue());
        Predicate predicateQuestionTypeIn = from.get("question").get("questionType").get("id").in(questionTypeIds);
        //accepted
        Predicate predicateAccepted=criteriaBuilder.equal(from.get("isAssQuestionAcceptedAdmin"), new Boolean(true));
  		//force accepted
  		Predicate predicateForcedAccepted=criteriaBuilder.equal(from.get("isForcedByAdmin"), new Boolean(true));
  		Predicate predicateAcceptedOr;
  		//accepted / force accepted
  	  	predicateAcceptedOr = criteriaBuilder.or(predicateAccepted,predicateForcedAccepted);	
        criteriaQuery.orderBy(criteriaBuilder.asc(from.get("orderAversion")));	
        return criteriaBuilder.and(predicateQuestionEvent,predicateAssessment,predicateQuestionTypeIn,predicateAcceptedOr);
	}
	
	private static Function<QuestionEvent, Long> getQuestionEventToIdFunction() {
		return new Function<QuestionEvent, Long>() {

			@Override
			public Long apply(QuestionEvent input) {
				return input.getId();
			}
		};
	}
	
	public static AssesmentQuestion findPastAssesmentOfQuestion(Long assesmentId,Long questionId){
		log.info("findPastAssesmentOfQuestion assesment id:" + assesmentId + "Question id  : " + questionId);
		
		Assesment assesment = Assesment.findAssesment(assesmentId);
		String mcName= assesment.getMc().getMcName().substring(0,assesment.getMc().getMcName().indexOf(".")+1);
		
		EntityManager em = entityManager();
		String queryString = "SELECT aq from AssesmentQuestion as aq where aq.assesment in (select id from Assesment as a where a.mc in (select id from Mc as m where" +
				" m.mcName like '"+ mcName +"%' )) and aq.question= "+ questionId + " order by date_added desc";
        
		//System.out.println("Query is "+ queryString);
		
		TypedQuery<AssesmentQuestion> query = em.createQuery(queryString, AssesmentQuestion.class);
		List<AssesmentQuestion> assesmentQuestionList = query.getResultList();
		log.info("retrieve query String :" + queryString);
		log.info("AssesmentQuestion List Size :" + assesmentQuestionList.size());
        
		if(assesmentQuestionList.size() > 0){
			return assesmentQuestionList.get(0);
		}else{
			return null;
		}
			
	}
	
	public static Integer countAssessmentQuestionByAssessment(Long assessmentId, List<String> criteriaStringList)
	{
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<AssesmentQuestion> from = criteriaQuery.from(AssesmentQuestion.class);
		criteriaQuery.distinct(true);
		Selection<Long> path = from.get("id");
		criteriaQuery.select(path);		
		criteriaQuery.orderBy(criteriaBuilder.asc(from.get("id")));
		
		Predicate pre = criteriaBuilder.equal(from.get("assesment").get("id"), assessmentId);
		Predicate mainPredicate = advancedSearchCriteriaForQuestion(criteriaStringList, criteriaBuilder, criteriaQuery, from); //findQusetionByAdvancedSearchCriteria(loggedUser,institution, criteriaBuilder, criteriaQuery, from,criteriaStringList, searchField, searchText);
		
		if (mainPredicate != null)
			pre = criteriaBuilder.and(pre, mainPredicate);
		
		criteriaQuery.where(pre);
		TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);
		//log.info("COUNT ADVANCED QUERY : " + query.unwrap(Query.class).getQueryString());
		log.info("Result count : " + query.getResultList().size());
		return query.getResultList().size();
	}
	
	public static List<AssesmentQuestion> findAssessmentQuestionByAssessmentForAdmin(Long assessmentId, String sortname,Sorting sortorder,List<String> criteriaStringList, int start, int length)
	{
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<AssesmentQuestion> criteriaQuery = criteriaBuilder.createQuery(AssesmentQuestion.class);
		Root<AssesmentQuestion> from = criteriaQuery.from(AssesmentQuestion.class);
		criteriaQuery.distinct(true);
		if(sortorder==Sorting.ASC){
			if(sortname.equals("autor")){
				criteriaQuery.orderBy(criteriaBuilder.asc(from.get(sortname).get("name")));				
			}else{
				criteriaQuery.orderBy(criteriaBuilder.asc(from.get(sortname)));
			}
		}else{
			if(sortname.equals("autor")){
				criteriaQuery.orderBy(criteriaBuilder.desc(from.get(sortname).get("name")));
			}else{
				criteriaQuery.orderBy(criteriaBuilder.desc(from.get(sortname)));
			}
		}
		
		Predicate pre = criteriaBuilder.equal(from.get("assesment").get("id"), assessmentId);
		Predicate mainPredicate = advancedSearchCriteriaForQuestion(criteriaStringList, criteriaBuilder, criteriaQuery, from); //findQusetionByAdvancedSearchCriteria(loggedUser,institution, criteriaBuilder, criteriaQuery, from, criteriaStringList, searchField, searchText);
		
		if (mainPredicate != null)
			pre = criteriaBuilder.and(pre, mainPredicate);
		
		criteriaQuery.where(pre);
		TypedQuery<AssesmentQuestion> query = entityManager().createQuery(criteriaQuery);
		//log.info("ADVANCED QUERY : " + query.unwrap(Query.class).getQueryString());
		query.setFirstResult(start);
		query.setMaxResults(length);
		return query.getResultList();
	}
	
	public static <T> Predicate advancedSearchCriteriaForQuestion(List<String> criteriaStringList, CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery, Root<AssesmentQuestion> from) {
		try
		{
			List<AdvancedSearchCriteria> criteriaList = AdvancedSearchCriteriaUtils.decodeList(criteriaStringList);
			Predicate predicate = null;
			Predicate advPredicate = null;
			
			for (AdvancedSearchCriteria criteria : criteriaList)
			{
				if (PossibleFields.KEYWORD.equals(criteria.getPossibleFields()))
				{
					Join<AssesmentQuestion,Question> questionJoin = from.join("question");
					SetJoin<Question, Keyword> keywordSetJoin = questionJoin.joinSet("keywords", JoinType.LEFT);
					Expression<String> keywordTextExp = keywordSetJoin.get("name");
					predicate = likeComparison(criteriaBuilder, criteria, keywordTextExp);
				}
				else if (PossibleFields.QUESTION_EVENT.equals(criteria.getPossibleFields()))
				{
					Expression<String> queEventNameExp = from.get("question").get("questEvent").get("eventName");
					predicate = likeComparison(criteriaBuilder, criteria, queEventNameExp);
				}
				else if (PossibleFields.COMMENT.equals(criteria.getPossibleFields()))
				{
					Expression<String> commentExp = from.get("question").get("comment");
					predicate = likeComparison(criteriaBuilder, criteria, commentExp);
				}
				else if (PossibleFields.ANSWER_TEXT.equals(criteria.getPossibleFields()))
				{
					Subquery<Answer> subQuery = criteriaQuery.subquery(Answer.class);
					Root answerRoot = subQuery.from(Answer.class);
					Expression<String> answerTextPre = answerRoot.get("answerText");
					Predicate subPre1 = likeComparison(criteriaBuilder, criteria, answerTextPre);
					subQuery.select(answerRoot.get("question").get("id")).where(subPre1);
					
					predicate = criteriaBuilder.in(from.get("question").get("id")).value(subQuery);					
				}
				else if (PossibleFields.QUESTION_TEXT.equals(criteria.getPossibleFields()))
				{
					Expression<String> quesitonTextExp = from.get("question").get("questionText");
					predicate = likeComparison(criteriaBuilder, criteria, quesitonTextExp);
				}
				else if (PossibleFields.QUESTION_SHORT_NAME.equals(criteria.getPossibleFields()))
				{
					Expression<String> quesitonShortTextExp = from.get("question").get("questionShortName");
					predicate = likeComparison(criteriaBuilder, criteria, quesitonShortTextExp);				
				}
				else if (PossibleFields.CREATED_QUESTION_DATE.equals(criteria.getPossibleFields()))
				{
					Expression<Date> createdQueDateExp = from.get("question").get("dateAdded");
					predicate = dateComparison(criteriaBuilder, criteria, createdQueDateExp);
				}
				else if (PossibleFields.CHANGED_QUESTION_DATE.equals(criteria.getPossibleFields()))
				{
					Expression<Date> changedQueDateExp = from.get("question").get("dateChanged");
					predicate = dateComparison(criteriaBuilder, criteria, changedQueDateExp);
				}	
				else if (PossibleFields.USED_IN_MC_DATE.equals(criteria.getPossibleFields()))
				{
					Subquery<AssesmentQuestion> subQuery = criteriaQuery.subquery(AssesmentQuestion.class);
					Root assQueRoot = subQuery.from(AssesmentQuestion.class);
					Expression<Date> assessmentDateExp = assQueRoot.get("assesment").get("dateOfAssesment");
					Predicate subPre1 = dateComparison(criteriaBuilder, criteria, assessmentDateExp);					
					subQuery.select(assQueRoot.get("question").get("id")).where(subPre1);
					
					predicate = criteriaBuilder.in(from.get("question").get("id")).value(subQuery);
				}
				else if (PossibleFields.AUTHOR.equals(criteria.getPossibleFields()))
				{
					Expression<String> authorNameExp = from.get("question").get("autor").get("name");
					Expression<String> authorPreNameExp = from.get("question").get("autor").get("prename");
					Expression<String> authorFullNameExp = criteriaBuilder.concat(authorNameExp, authorPreNameExp);
					
					predicate = likeComparison(criteriaBuilder, criteria, authorFullNameExp);
					//predicate = userTypeComparison(criteriaBuilder, criteria, authorNameExp, authorPreNameExp);
				}
				else if (PossibleFields.REVIEWER.equals(criteria.getPossibleFields()))
				{
					Expression<String> reviewerNameExp = from.get("question").get("rewiewer").get("name");
					Expression<String> reviewerPreNameExp = from.get("question").get("rewiewer").get("prename");
					Expression<String> reviewerFullNameExp = criteriaBuilder.concat(reviewerNameExp, reviewerPreNameExp);
					
					predicate = likeComparison(criteriaBuilder, criteria, reviewerFullNameExp);
					//predicate = userTypeComparison(criteriaBuilder, criteria, reviewerNameExp, reviewerPreNameExp);
				}
				else if (PossibleFields.MEDIA_AVAILABILITY.equals(criteria.getPossibleFields()))
				{
					Subquery<QuestionResource> subQuery = criteriaQuery.subquery(QuestionResource.class);
					Root queResourceRoot = subQuery.from(QuestionResource.class);
					Predicate subPre1 = null;
					
					if (Comparison.EQUALS.equals(criteria.getComparison())) {
						subPre1 = criteriaBuilder.equal(queResourceRoot.get("type"), MultimediaType.valueOf(criteria.getValue()));
					} else if (Comparison.NOT_EQUALS.equals(criteria.getComparison()))
						subPre1 = criteriaBuilder.notEqual(queResourceRoot.get("type"), MultimediaType.valueOf(criteria.getValue()));
					
					if(subPre1 != null) {
						subQuery.select(queResourceRoot.get("question").get("id")).where(subPre1);
						
						predicate = criteriaBuilder.in(from.get("question").get("id")).value(subQuery);							
					}	
				}
				else if (PossibleFields.QUESTION_TYPE.equals(criteria.getPossibleFields()))
				{
					if (Comparison.EQUALS.equals(criteria.getComparison())) {
						predicate = criteriaBuilder.equal(from.get("question").get("questionType").get("questionType"), QuestionTypes.valueOf(criteria.getValue()));
					} else if (Comparison.NOT_EQUALS.equals(criteria.getComparison())) {
						predicate = criteriaBuilder.notEqual(from.get("question").get("questionType").get("questionType"), QuestionTypes.valueOf(criteria.getValue()));
					}
				}
				else if (PossibleFields.QUESTION_TYPE_NAME.equals(criteria.getPossibleFields()))
				{
					Expression<String> quesitonTypeShortTextExp = from.get("question").get("questionType").get("shortName");
					predicate = likeComparison(criteriaBuilder, criteria, quesitonTypeShortTextExp);	
				}
				
				if (BindType.AND.equals(criteria.getBindType()))
				{
					if (advPredicate == null)
						advPredicate = criteriaBuilder.and(predicate);
					else
						advPredicate = criteriaBuilder.and(advPredicate, predicate);
				}
				else if (BindType.OR.equals(criteria.getBindType()))
				{
					if (advPredicate == null)
						advPredicate = criteriaBuilder.or(predicate);
					else
						advPredicate = criteriaBuilder.or(advPredicate, predicate);
				}				
			}
			
				
			return advPredicate;
		}
		catch(Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	private static <T> Predicate multimediaPredicate(CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery, Root<AssesmentQuestion> from, AdvancedSearchCriteria criteria) {
		Predicate predicate;
		Subquery<QuestionResource> subQuery = criteriaQuery.subquery(QuestionResource.class);
		Root queResourceRoot = subQuery.from(QuestionResource.class);
		Predicate subPre1 = null;
		
		if (Comparison.EQUALS.equals(criteria.getComparison())) {
			subPre1 = criteriaBuilder.equal(queResourceRoot.get("type"), MultimediaType.valueOf(criteria.getValue()));
		} else if (Comparison.NOT_EQUALS.equals(criteria.getComparison()))
			subPre1 = criteriaBuilder.notEqual(queResourceRoot.get("type"), MultimediaType.valueOf(criteria.getValue()));
		
		if(subPre1 != null) {
			subQuery.select(queResourceRoot.get("question").get("id")).where(subPre1);
			
			predicate = criteriaBuilder.in(from.get("question").get("id")).value(subQuery);
			return predicate;	
		}else {
			return null;
		}
		
	}

	private static Predicate questionTypeComparison(CriteriaBuilder criteriaBuilder, Root<AssesmentQuestion> from,AdvancedSearchCriteria criteria) {
		
		Predicate predicate = null;
		if (Comparison.EQUALS.equals(criteria.getComparison())) {
			predicate = criteriaBuilder.equal(from.get("question").get("questionType").get("questionType"), QuestionTypes.valueOf(criteria.getValue()));
		} else if (Comparison.NOT_EQUALS.equals(criteria.getComparison())) {
			predicate = criteriaBuilder.notEqual(from.get("question").get("questionType").get("questionType"), QuestionTypes.valueOf(criteria.getValue()));
		}
		
		return predicate;
	}
}
