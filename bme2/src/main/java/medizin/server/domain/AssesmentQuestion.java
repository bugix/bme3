package medizin.server.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.persistence.criteria.Subquery;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import medizin.server.mail.EmailServiceImpl;
import medizin.shared.Status;
import medizin.shared.utils.PersonAccessRight;
import medizin.shared.utils.SharedConstant;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.mortbay.log.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assesmentQuestion")
    private Set<AnswerToAssQuestion> answersToAssQuestion = new HashSet<AnswerToAssQuestion>();
    
    //RedactionalBase code
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateAdded;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateChanged;

    @NotNull
    @ManyToOne
    private Person rewiewer;

    @NotNull
    @ManyToOne
    private Person autor;
    
    
    /*
	 * Author pull down for admin / institutional Admin
	 * 
	 * Show Author / examiner assigned in particular assessment
	 * */
     
     public static List<Person> findAuthorListByAssesment(Assesment assesment){
     	
     	Log.info("findAuthorListByAssesment ");
     	
     	Set<QuestionSumPerPerson> questionSumPerPersons=assesment.getQuestionSumPerPerson();
     	
     	List<Person> authorList=new ArrayList<Person>();
     	for(QuestionSumPerPerson questionSumPerPerson:questionSumPerPersons)
     	{
     		if(!authorList.contains(questionSumPerPerson.getResponsiblePerson()))
     		authorList.add(questionSumPerPerson.getResponsiblePerson());
     	}
     	return authorList;

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
    
    public static List<AssesmentQuestion> findAssesmentQuestionsByMc(Long assesmentId,Long id,String questionId,String questionType,String questionName){
    	
    	Log.info("Past Question Tab ");
    	
        Boolean isAcceptedAdmin = true;
        
        Mc mc = Mc.findMc(id);
        if (mc == null) throw new IllegalArgumentException("The mcs argument is required");
        EntityManager em = Question.entityManager();
        
        //get user who is logged in
        Person userLoggedIn=Person.myGetLoggedPerson();
        
        //get institution
        HttpSession session1 = RequestFactoryServlet.getThreadLocalRequest().getSession();
        Long instId = (Long) session1.getAttribute("institutionId");
		Institution institution = Institution.findInstitution(instId);
        
		//create query
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<AssesmentQuestion> criteriaQuery = criteriaBuilder
				.createQuery(AssesmentQuestion.class);
		
		//from
		Root<AssesmentQuestion> from = criteriaQuery.from(AssesmentQuestion.class);
		
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
		if(!questionId.equalsIgnoreCase(""))
		{
			searchPre1=criteriaBuilder.equal(questionJoin.get("id"), new Long(questionId));
		}
		if(!questionType.equalsIgnoreCase(""))
		{
			
			Predicate shortNamePre=criteriaBuilder.like(questionJoin.get("questionType").<String>get("shortName"),"%"+questionType+"%");
			Predicate longNamePre=criteriaBuilder.like(questionJoin.get("questionType").<String>get("longName"),"%"+questionType+"%");
			searchPre2=criteriaBuilder.or(shortNamePre,longNamePre);
		}
		if(!questionName.equalsIgnoreCase(""))
		{
			Predicate shortNamePre=criteriaBuilder.like(questionJoin.<String>get("questionShortName"),"%"+questionName+"%");
			Predicate namePre=criteriaBuilder.like(questionJoin.<String>get("questionText"),"%"+questionName+"%");
		
			searchPre3=criteriaBuilder.or(shortNamePre,namePre);
			
		}
		
		Predicate searchPre=null;
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
		
		
		
		//group by question- no duplicate question
		//criteriaQuery.groupBy(from.get("question"));
		//criteriaQuery.having(from.<Set>get("answersToAssQuestion").equals(obj))//answersToAssQuestion
		
		//criteriaQuery.orderBy(criteriaBuilder.asc(from.get("question").get("id")));
		
		
		//institution filter
		Predicate pre6 = criteriaBuilder.equal(from.get("question").get("questEvent")
				.get("institution"), institution);
		
        if(userLoggedIn.getIsAdmin()) //Main Admin
        {
        	Log.info("Main Admin");
    		
    		
			
			
			
			Predicate pre;
    		if(pre7==null)
    		{
    			if(searchPre==null)
    			pre=criteriaBuilder.and(pre3,pre4,pre5,pre6);
    			else
    			{
    				pre=criteriaBuilder.and(pre3,pre4,pre5,pre6,searchPre);
    			}
    		}else
    		{	if(searchPre==null)
    			pre=criteriaBuilder.and(pre3,pre4,pre5,pre6,pre7);
        			else
        			{
        				pre=criteriaBuilder.and(pre3,pre4,pre5,pre6,pre7,searchPre);
        			}
    			 
    		}
    		
    		select.where(pre);
    		
    		
    		
    		TypedQuery<AssesmentQuestion> q=entityManager().createQuery(criteriaQuery);
    		
    		//remove duplicate question
    		
    		return removeDuplicateAssesmentQuestion(q.getResultList());
        }
        else
        {
        	Log.info("Not Main Admin");
        	
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
			
			
    		if(pre7==null)
    		{
    			if(searchPre==null)
    			select.where(criteriaBuilder.and(pre3,pre4,pre5,pre6,andAdminPredicate));
    			else
    			{
    				select.where(criteriaBuilder.and(pre3,pre4,pre5,pre6,andAdminPredicate,searchPre));
    			}
    		}
    		else
    		{	if(searchPre==null)
    			select.where(criteriaBuilder.and(pre3,pre4,pre5,pre7,pre6,andAdminPredicate));
	    		else
	    		{
	    			select.where(criteriaBuilder.and(pre3,pre4,pre5,pre7,pre6,andAdminPredicate,searchPre));
	    		}
    		}
			TypedQuery<AssesmentQuestion> q=entityManager().createQuery(criteriaQuery);
			
			
			
			//remove duplicate question
		
    		return removeDuplicateAssesmentQuestion(q.getResultList());
        }
        
       
		/*StringBuilder queryBuilder = new StringBuilder("SELECT assesmentauestion FROM AssesmentQuestion AS assesmentauestion " +
        		"INNER JOIN assesmentauestion.question AS quest WHERE (assesmentauestion.isAssQuestionAcceptedAdmin = :isAcceptedAdmin or assesmentauestion.isForcedByAdmin=true) AND"+
        		" quest.status = :status AND :mcs_item MEMBER OF quest.mcs and assesmentauestion.assesment.dateOfAssesment < :dateOfAssesment");
        
        TypedQuery<AssesmentQuestion> q = em.createQuery(queryBuilder.toString(), AssesmentQuestion.class);
        q.setParameter("isAcceptedAdmin", isAcceptedAdmin);
        q.setParameter("status", Status.ACTIVE);

        q.setParameter("mcs_item", mc);
        q.setParameter("dateOfAssesment", new Date());*/
       
        
      //  return q.getResultList();
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
        
        PersonAccessRight accessRights=Person.getLoggedPersonAccessRights();
        
        //get institution
        HttpSession session1 = RequestFactoryServlet.getThreadLocalRequest().getSession();
        Long instId = (Long) session1.getAttribute("institutionId");
		Institution institution = Institution.findInstitution(instId);
        
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
    
    /*
     * Proposed Question Tab only for examiner
     * 
     * Logic
     * 1. Assessment Question with (status proposed) 
     * 2. An examiner may see only questions of specialization 	where he has access.	
     * */
    public static List<AssesmentQuestion> findAssesmentQuestionsByMcProposal(Long assesmentId,String questionId,String questionType,String questionName){
        Boolean isAssQuestionAdminProposal = true;
       
        Assesment assesment = Assesment.findAssesment(assesmentId);
        Person userLoggedIn=Person.myGetLoggedPerson();
        
        //get institution
        HttpSession session1 = RequestFactoryServlet.getThreadLocalRequest().getSession();
        Long instId = (Long) session1.getAttribute("institutionId");
		Institution institution = Institution.findInstitution(instId);
        
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
        
/*        if(!questionName.equalsIgnoreCase(""))
        {
        	 q.setParameter("questionName", questionName);
        	 q.setParameter("questionName1", questionName);
        	
        }
        if(!questionType.equalsIgnoreCase(""))
        {
        	 q.setParameter("questionType", questionType);
        	 q.setParameter("questionType1", questionType);
        	
        }
        */
        return q.getResultList();
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
    	
    	
    	PersonAccessRight accessRights=Person.getLoggedPersonAccessRights();
    	
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
	
	public static List<AssesmentQuestion> findAssesmentQuestionsByQuestionEventAssIdQuestType(java.lang.Long questEventId, java.lang.Long assesmentId,  List<Long> questionTypesId) {
		QuestionEvent questionEvent = QuestionEvent.findQuestionEvent(questEventId);
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
		cq.where(pre);
		
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
	
	/*Send Mail*/
	public static Boolean sendMail(List<Person> toExaminerList, String messageContent,	String mailSubject, Assesment assesment)
	{
		try
		{
		Person userLoggedIn=Person.myGetLoggedPerson();
		String fromAddress=userLoggedIn.getEmail();
		String toAddresses[]=new String[toExaminerList.size()];
		

		messageContent=messageContent.replace("[fromName]", userLoggedIn.getPrename() +" "+userLoggedIn.getName());
		messageContent=messageContent.replace("[assesment]", assesment.getName());
		EmailServiceImpl emailService=new EmailServiceImpl();
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(RequestFactoryServlet.getThreadLocalServletContext());
		((EmailServiceImpl)emailService).setSender(applicationContext.getBean(JavaMailSenderImpl.class));
		
		for(int i=0;i<toExaminerList.size();i++)
		{
			
			Person examiner=toExaminerList.get(i);
			toAddresses[i]=examiner.getEmail();
			String newMessage=messageContent.replace("[toName]", examiner.getPrename()+" "+examiner.getName());
			emailService.sendMail(new String[]{examiner.getEmail()}, fromAddress, mailSubject, newMessage);
		}
		
		return true;
		}
		catch(Exception e)
		{
			Log.info("sendMail exception : " + e.getMessage());
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
}
