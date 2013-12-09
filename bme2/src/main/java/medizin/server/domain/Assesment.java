package medizin.server.domain;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
import javax.servlet.ServletContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import medizin.server.mail.EmailServiceImpl;
import medizin.shared.utils.PersonAccessRight;
import medizin.shared.utils.SharedConstant;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Assesment {

	private static Logger log = Logger.getLogger(Assesment.class);
	
    @NotNull
    @Size(min = 5, max = 255)
    private String name;
    
    // remeber examiner before closing in days
    @NotNull
    private Integer rememberBeforeClosing;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="dd.MM.YYYY hh:mm:ss")
    private Date dateOfAssesment;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="dd.MM.YYYY hh:mm:ss")
    private Date dateOpen;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="dd.MM.YYYY hh:mm:ss")
    private Date dateClosed;

    @NotNull
    @Size(min = 5, max = 100)
    private String place;

    /*@NotNull
    @Size(min = 5, max = 255)*/
    private String logo;

    @NotNull
    @Value("false")
    @Column(columnDefinition="BIT", length = 1)
    private Boolean isClosed;

    @NotNull
    @Value("1")
    private Integer assesmentVersion;

    @NotNull
    @ManyToOne
    private Mc mc;

    @OneToOne
    private medizin.server.domain.Assesment repeFor;

    private Integer percentSameQuestion;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assesment",fetch=FetchType.LAZY)
    private Set<QuestionSumPerPerson> questionSumPerPerson = new HashSet<QuestionSumPerPerson>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assesment",fetch=FetchType.LAZY)
    private Set<AssesmentQuestion> assesmentQuestions = new HashSet<AssesmentQuestion>();
    
    @NotNull
    @ManyToOne
    Institution institution;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assesment",fetch=FetchType.LAZY)
    private List<QuestionTypeCountPerExam> questionTypeCountPerExams=new ArrayList<QuestionTypeCountPerExam>();
    
    /* Business Login
     * 
     * Find Assessment
     * 
     * Left side
     * 
     * For Admin and Institutional Admin : Open Assessment between  start date and dateOfAssesment
     * 
     * For Examiner :  Open Assessment between  start date and dateClosed , and Assessment for which examiner is assigned
     */
    public static List<Assesment> findAssesmentsOpenBetween() {
    	Date dateClosed =new Date();
    	Date dateOpen = new Date(); 
    	Boolean isClosed=false;
    	
    	Logger log = Logger.getLogger(Assesment.class);
    	log.debug("Datum geschlossen: " + dateClosed);

        EntityManager em = Assesment.entityManager();
        
        Person userLoggedIn=Person.myGetLoggedPerson();
        Boolean isAdmin=userLoggedIn.getIsAdmin();
        PersonAccessRight accessRights=userLoggedIn.fetchLoggedPersonAccessRights();
        Institution institution=Institution.myGetInstitutionToWorkWith();
        Boolean isInstitutionAdmin=accessRights.getIsInstitutionalAdmin();
        TypedQuery<Assesment> q=null;
        if(isAdmin || isInstitutionAdmin)//For Admin and Institutional Admin user
        	q = em.createQuery("SELECT Assesment FROM Assesment AS assesment WHERE assesment.dateOfAssesment >= :dateClosed  AND assesment.dateOpen <= :dateOpen  AND assesment.isClosed IS :isClosed and assesment.institution=:institution", Assesment.class);
        else //for examiner
        {
        	q = em.createQuery("SELECT Assesment FROM Assesment AS assesment WHERE assesment.dateClosed >= :dateClosed  AND assesment.dateOpen <= :dateOpen  AND assesment.isClosed IS :isClosed  and assesment.institution=:institution", Assesment.class);
        	 
        }
        q.setParameter("dateClosed", dateClosed);
        q.setParameter("dateOpen", dateOpen);
        q.setParameter("isClosed", isClosed);
        q.setParameter("institution", institution);
        List<Assesment> assesments=q.getResultList();
        
        if(!(isAdmin || isInstitutionAdmin))
        {
	        for(int i=0;i<assesments.size();i++)
	        {
	        	Assesment a=assesments.get(i);
	        	Set<QuestionSumPerPerson> questionSumPerPersons=a.getQuestionSumPerPerson();
	        	
	        	boolean flag=false;
	        	
	        	for(QuestionSumPerPerson questionSumPerPerson:questionSumPerPersons)
	        	{
	        		if(questionSumPerPerson.getResponsiblePerson().getId().longValue()==userLoggedIn.getId().longValue())
	            	{
	            		flag=true;
	            		break;
	            	}
	        		
	        	}
	
	    		if(!flag || questionSumPerPersons.size()==0)
	    		{
	    			assesments.remove(i);
	    			i--;
	    		}
	        }
        }
        
        return assesments;
    }
    
    public static List<Assesment> findActiveAssesments(Long selectedInstitutionId) {
    	Date dateOfAssesment =new Date();
    	Date dateOpen = new Date(); 
    	//Boolean isClosed=false;
    	
    	//Logger log = Logger.getLogger(Assesment.class);
    	//log.debug("Datum geschlossen: " + dateClosed);

        EntityManager em = Assesment.entityManager();
        TypedQuery<Assesment> q = em.createQuery("SELECT Assesment FROM Assesment AS assesment WHERE assesment.dateOfAssesment >= :dateOfAssesment  AND assesment.dateOpen <= :dateOpen AND assesment.institution.id = :institution", Assesment.class);
        q.setParameter("dateOfAssesment", dateOfAssesment);
        q.setParameter("dateOpen", dateOpen);
      //  q.setParameter("isClosed", isClosed);
        q.setParameter("institution", selectedInstitutionId);
        return q.getResultList();
    }
    
    
    public static List<Assesment> findAssesmentByInsitute(int firstResult, int maxResults)
    {
    	EntityManager em = Assesment.entityManager();
    	
    	Institution activeInstitute=Institution.myGetInstitutionToWorkWith();
    	
    	if (activeInstitute == null)
    		throw new IllegalArgumentException("The person and institution arguments are required");
    	
    	//create query  	
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
    	CriteriaQuery<Assesment> criteriaQuery = criteriaBuilder
				.createQuery(Assesment.class);
    	//from
    	Root<Assesment> from = criteriaQuery.from(Assesment.class);
    	criteriaQuery.orderBy(criteriaBuilder.asc(from.get("id")));
    	
    	criteriaQuery.where(criteriaBuilder.equal(from.get("institution"), activeInstitute));
    	TypedQuery<Assesment> q=entityManager().createQuery(criteriaQuery);
    	q.setFirstResult(firstResult);
    	q.setMaxResults(maxResults);
    	return q.getResultList();
    }
    
    public static Long countAssesmentByInsitute()
    {
    	EntityManager em = Assesment.entityManager();
    	
    	Institution activeInstitute=Institution.myGetInstitutionToWorkWith();
    	
    	if (activeInstitute == null)
    		throw new IllegalArgumentException("The person and institution arguments are required");
    	
    	//create query  	
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
    	CriteriaQuery<Assesment> criteriaQuery = criteriaBuilder
				.createQuery(Assesment.class);
    	//from
    	Root<Assesment> from = criteriaQuery.from(Assesment.class);
    	
    	criteriaQuery.where(criteriaBuilder.equal(from.get("institution"), activeInstitute));
    	TypedQuery<Assesment> q=entityManager().createQuery(criteriaQuery);
    
    	return new Long(q.getResultList().size());
    }
    
    public static List<Assesment> findAssessmentByLoggedUser(Long loggedUserId, boolean isAdminOrInstitutionalAdmin)
    {
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");
				
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Assesment> criteriaQuery = criteriaBuilder.createQuery(Assesment.class);
		Root<Assesment> from = criteriaQuery.from(Assesment.class);
		criteriaQuery.distinct(true);

		Date dateClosed =new Date();
    	Date dateOpen = new Date(); 
    	Boolean isClosed=false;
		
		SetJoin<Assesment, QuestionSumPerPerson> questionSumPerPersonSetJoin = from.joinSet("questionSumPerPerson", JoinType.LEFT);
		Predicate pre1 = criteriaBuilder.equal(questionSumPerPersonSetJoin.get("responsiblePerson").get("id"), loggedUserId);		
		Expression<Date> closedDateExp = from.get("dateClosed");
		Expression<Date> openDateExp = from.get("dateOpen");
		Predicate pre2 = criteriaBuilder.greaterThanOrEqualTo(closedDateExp, dateClosed);
		Predicate pre3 = criteriaBuilder.lessThanOrEqualTo(openDateExp, dateOpen);
		Predicate pre4 = criteriaBuilder.equal(from.get("isClosed"), isClosed);
		Predicate pre5 = criteriaBuilder.equal(from.get("institution").get("id"), institution.getId());;
		
		criteriaQuery.where(criteriaBuilder.and(pre1, pre2, pre3, pre4, pre5));	
	
		TypedQuery<Assesment> query = entityManager().createQuery(criteriaQuery);
		return query.getResultList();
    }
    
    public static List<Assesment> findAssessmentForJob(Long personId)
    {
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Assesment> criteriaQuery = criteriaBuilder.createQuery(Assesment.class);
		Root<Assesment> from = criteriaQuery.from(Assesment.class);
		criteriaQuery.distinct(true);

		Date dateClosed =new Date();
    	Date dateOpen = new Date(); 
    	Boolean isClosed=false;
		
		SetJoin<Assesment, QuestionSumPerPerson> questionSumPerPersonSetJoin = from.joinSet("questionSumPerPerson", JoinType.LEFT);
		Predicate pre1 = criteriaBuilder.equal(questionSumPerPersonSetJoin.get("responsiblePerson").get("id"), personId);		
		Expression<Date> closedDateExp = from.get("dateClosed");
		Expression<Date> openDateExp = from.get("dateOpen");
		Predicate pre2 = criteriaBuilder.greaterThanOrEqualTo(closedDateExp, dateClosed);
		Predicate pre3 = criteriaBuilder.lessThanOrEqualTo(openDateExp, dateOpen);
		Predicate pre4 = criteriaBuilder.equal(from.get("isClosed"), isClosed);
		
		criteriaQuery.where(criteriaBuilder.and(pre1, pre2, pre3, pre4));	
	
		TypedQuery<Assesment> query = entityManager().createQuery(criteriaQuery);
		return query.getResultList();
    }
    
    private static final Function<QuestionType, String> QUESTIONTYPE_TO_SHORTNAME = new Function<QuestionType, String>() {

		@Override
		public String apply(QuestionType input) {
			return input.getShortName();
		}
	};
    
    public static Boolean systemOverviewSendMail(Long personId, List<Assesment> assesmentList, String msgContent, String mailSubject)
	{
		try
		{
			Person userLoggedIn=Person.myGetLoggedPerson();
			Person examiner = Person.findPerson(personId);
			
			if (userLoggedIn == null || examiner == null)
				return false;
			
			String template = "";
			String filePath=RequestFactoryServlet.getThreadLocalServletContext().getRealPath(SharedConstant.SYSTEM_OVERVIEW_MAIL_TEMPLATE);
			File file=new File(filePath);
			try{
				template = FileUtils.readFileToString(file);
			}catch(IOException e)
			{
				template = "";
			}
	
			return sendMail(template, assesmentList, msgContent, mailSubject, examiner, (userLoggedIn.getPrename() + " " + userLoggedIn.getName()), userLoggedIn.getEmail(), RequestFactoryServlet.getThreadLocalServletContext());
		
		}
		catch(Exception e)
		{
			log.error(e.getMessage(), e);
			return false;
		}
	}

    
	public static Boolean sendMail(String template, List<Assesment> assesmentList, String msgContent, String mailSubject, Person examiner, String fromName, String fromAddress, ServletContext servletContext) {
		try
		{
			template = template.substring(template.indexOf("[ASSESSMENT LOOP]"), template.indexOf("[ASSESSMENT END LOOP]"));
			
			SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MMM-yy");
			
			EmailServiceImpl emailService=new EmailServiceImpl();
			WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			((EmailServiceImpl)emailService).setSender(applicationContext.getBean(JavaMailSenderImpl.class));
			
			String messageContent = msgContent;
			
			messageContent = messageContent.replace("[fromName]", fromName);
			messageContent = messageContent.replace("[toName]", examiner.getPrename() + " " + examiner.getName());
			String newMessage = messageContent;	
			String finalMsgContent = "";
			
			for (int i=0; i<assesmentList.size(); i++)
			{
				Assesment assesment = assesmentList.get(i);
				
				newMessage = newMessage.replace("[assesmentName]", assesment.getName());
				newMessage = newMessage.replace("[assesmentStartDate]", dateFormat.format(assesment.getDateOpen()));
				newMessage = newMessage.replace("[assesmentClosedDate]", dateFormat.format(assesment.getDateClosed()));
				newMessage = newMessage.replace("[assesmentMC]", assesment.getMc().getMcName());
				
				Integer assesmentQuestionProposedCount = 0;
				List<AssesmentQuestion> assQueList = AssesmentQuestion.findAssesmentQuestionsByMcProposalForSystemOverview(assesment.getId(), examiner.getId());
				if (assQueList != null)
					assesmentQuestionProposedCount = assQueList.size();
					
				//assesmentQuestionProposedCount = AssesmentQuestion.findAssesmentQuestionsByMcProposal(assesment.getId(), "", "", "").size();				
				newMessage = newMessage.replace("[assesmentQuestionProposedCount]", assesmentQuestionProposedCount.toString());
				
				List<QuestionSumPerPerson> questionSumPerPersonList = QuestionSumPerPerson.findPercentageOfQuestionAssignedToExaminer(assesment, examiner);
				List<QuestionTypeCountPerExam> questionTypeCountPerExamList = assesment.getQuestionTypeCountPerExams();
				
				StringBuilder totalCountString = new StringBuilder();
				StringBuilder totalRemainingString = new StringBuilder();				
				totalCountString.append("<table>");
				totalRemainingString.append("<table>");
				
				for(QuestionTypeCountPerExam questionTypeCountPerExam:questionTypeCountPerExamList)
				{
					for(QuestionSumPerPerson questionSumPerPerson:questionSumPerPersonList)
					{
						Set<QuestionType> questionTypes=questionTypeCountPerExam.getQuestionTypesAssigned();
						// make comma separated question type name from question type set
						String questionTypeStr = Joiner.on(", ").join(FluentIterable.from(
								questionTypeCountPerExam.getQuestionTypesAssigned()).transform(QUESTIONTYPE_TO_SHORTNAME))		
								+ "(" + questionSumPerPerson.getQuestionEvent().getEventName() + ")";
						
						int questionTypeCount = questionTypeCountPerExam.getQuestionTypeCount();
						int percentAllocated = questionSumPerPerson.getQuestionSum();
						
						Integer totalQuestionAllocated = (int) Math.ceil((questionTypeCount*percentAllocated)/100.0);
						Integer questionAssigned = 0 ;
						
						List<AssesmentQuestion> assesmentQuestionList = AssesmentQuestion.findAssesmentQuestionsByAssesmentAndPerson(assesment.getId(), examiner);
						
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
						
						totalCountString.append("<tr>")
							.append("<td stype='vertical-align:middle'>")
							.append(questionTypeStr)
							.append(" : ")
							.append("</td>")
							.append("<td stype='vertical-align:middle'>")
							.append(totalQuestionAllocated)
							.append("</td>")
							.append("</tr>");
						
						totalRemainingString.append("<tr>")
							.append("<td stype='vertical-align:middle'>")
							.append(questionTypeStr)
							.append(" : ")
							.append("</td>")
							.append("<td stype='vertical-align:middle'>")
							.append((questionAssigned - totalQuestionAllocated))
							.append("</td>")
							.append("</tr>");
					}
					
					newMessage=newMessage.replace("[LOOP]", "");
					newMessage=newMessage.replace("[totalCount]", "");
					newMessage=newMessage.replace("[END LOOP]", "");
					newMessage=newMessage.replace("[totalRemaining]", "");					
				}				
				totalCountString.append("</table>");
				totalRemainingString.append("</table>");
				
				if (i < (assesmentList.size() - 1))
					totalRemainingString.append(template);
				
				newMessage = newMessage.replace("[[assesmentQuestionType]([assesmentSpecialization]) : [assesmentQuestionAllocatedCount]]", totalCountString.toString());
				newMessage = newMessage.replace("[[assesmentQuestionType]([assesmentSpecialization]) : [assesmentQuestionRemainingCount]]", totalRemainingString.toString());
				
				newMessage = newMessage.replace("[ASSESSMENT LOOP]", "");
				newMessage = newMessage.replace("[ASSESSMENT END LOOP]", "");
				
				finalMsgContent = finalMsgContent + newMessage;
			}
			
			emailService.sendMail(new String[]{examiner.getEmail()}, fromAddress, mailSubject, messageContent);
			
			return true;
		}
		catch(Exception e)
		{
			log.error(e);
		}
		return false;
	}

	public static Assesment findAssessmentByDateAndMC(Date assessmentdDate, Mc mc) {
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
    	CriteriaQuery<Assesment> criteriaQuery = criteriaBuilder.createQuery(Assesment.class);
    	Root<Assesment> from = criteriaQuery.from(Assesment.class);
    	Predicate pre1 = criteriaBuilder.equal(from.get("dateOfAssesment"), assessmentdDate);
    	Predicate pre2 = criteriaBuilder.equal(from.get("mc").get("id"), mc.getId());
    	criteriaQuery.where(criteriaBuilder.and(pre1,pre2));
		TypedQuery<Assesment> query = entityManager().createQuery(criteriaQuery);
		List<Assesment> resultList = query.getResultList();
		if(resultList != null && resultList.size() > 0) {
			return resultList.get(0);
		}
		return null;

	}
	public static List<Assesment> findAssesmentOfGivenYear(String selectedYear){
		 EntityManager em = Assesment.entityManager();
	    TypedQuery<Assesment> q = em.createQuery("SELECT Assesment FROM Assesment AS assesment WHERE assesment.dateOfAssesment >='" +selectedYear + "-01-01 00:00:00'  AND assesment.dateOfAssesment <='"+selectedYear + "-12-31 00:00:00'", Assesment.class);
	    log.info("Query is : " + q.toString());
	    return q.getResultList();
		
	}
}
